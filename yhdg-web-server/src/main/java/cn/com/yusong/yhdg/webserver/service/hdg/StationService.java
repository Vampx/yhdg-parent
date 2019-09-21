package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.StationRole;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.StationRoleMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StationService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(StationService.class);

    @Autowired
    StationMapper stationMapper;
    @Autowired
    StationBizUserMapper stationBizUserMapper;
    @Autowired
    AppConfig config;
    @Autowired
    AreaCache areaCache;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    StationRoleMapper stationRoleMapper;
    @Autowired
    StationDistributionMapper stationDistributionMapper;
    /**
     * 查询分页
     *
     * @param search
     * @return
     */
    public Page findPage(Station search) {
        Page page = search.buildPage();
        page.setTotalItems(stationMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Station> list = stationMapper.findPageResult(search);
        for (Station station : list) {
            AgentInfo agentInfo = findAgentInfo(station.getAgentId());
            if (agentInfo != null) {
                station.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }

    public List<Station> findListByStationId(String stationId) {
        List<Station> list = stationMapper.findListByStationId(stationId);
        for (Station station : list) {
            AgentInfo agentInfo = findAgentInfo(station.getAgentId());
            if (agentInfo != null) {
                station.setAgentName(agentInfo.getAgentName());
            }
        }
        return list;
    }

    public Station find(String id) {
        Station station = (Station) setAreaProperties(areaCache, stationMapper.find(id));
        if (station != null) {
            if (station.getAgentId() != null) {
                station.setAgentName(findAgentInfo(station.getAgentId()).getAgentName());
            }
        }
        return station;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(String id) {
        List<User> userList = userMapper.findListByStationId(id);
        if (userList.size() > 0) {
            return ExtResult.failResult("该站点包含用户，不能删除");
        }
        List<StationRole> stationRoleList = stationRoleMapper.findByStationId(id);
        if (stationRoleList.size() > 0) {
            return ExtResult.failResult("该站点包含站点角色，不能删除");
        }
        List<StationDistribution> stationDistributionList = stationDistributionMapper.findByStationId(id);
        if (stationDistributionList.size() > 0) {
            return ExtResult.failResult("该站点下存在分成设置，不能删除");
        }

        if (stationMapper.delete(id) == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }


    public String findMaxId() {
        String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String id = stationMapper.findMaxId(date);
        if (StringUtils.isEmpty(id)) {
            id = String.format("%s%0" + Constant.CABINET_ID_SEQUENCE_LENGTH + "d", date, 1);
        } else {
            String num = id.substring(id.length() - Constant.CABINET_ID_SEQUENCE_LENGTH);
            if (Long.parseLong(num) >= Math.pow(10, Constant.CABINET_ID_SEQUENCE_LENGTH) - 1) {
                throw new RuntimeException("今日站点数量已达到上限");
            }

            id = String.valueOf(Long.parseLong(id) + 1);
        }
        return id;
    }

    public ExtResult insert(Station station) {
        if (station.getId() == null) {
            station.setId(findMaxId());
        }
        if (station.getProvinceId() == null) {
            station.setProvinceId(Constant.PROVINCE_ID);
        }
        if (station.getCityId() == null) {
            station.setCityId(Constant.CITY_ID);
        }
        if (station.getDistrictId() == null) {
            station.setDistrictId(Constant.DISTRICT_ID);
        }
        if (station.getLng() == null) {
            station.setLng(Constant.LNG);
        }
        if (station.getLat() == null) {
            station.setLat(Constant.LAT);
        }
        station.setBalance(0);
        if (station.getWorkTime().equals("-")) {
            station.setWorkTime(null);
        }
        station.setCreateTime(new Date());
        if (stationMapper.insert(station) == 0) {
            return DataResult.failResult("对不起! 保存失败", null);
        } else {
            updateNewLocation(station);
        }
        return DataResult.successResult("操作成功",station.getId());
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateBasic(Station station) {
        Station entity = stationMapper.find(station.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (station.getWorkTime().equals("-")) {
            station.setWorkTime(null);
        }
        if (stationMapper.update(station) == 0) {
            return ExtResult.failResult("修改失败！");
        }

        return ExtResult.successResult();
    }

    public ExtResult updateNewLocation(Station station) {
        Station entity = stationMapper.find(station.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (station.getProvinceName() != null) {
            if (station.getProvinceName().substring(station.getProvinceName().length() - 1).equals("市")) {
                station.setProvinceName(station.getProvinceName().substring(0, station.getProvinceName().length() - 1));
            }
            Area area = areaCache.getByName(station.getProvinceName());
            if (area != null) {
                station.setProvinceId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (station.getCityName() != null) {
            Area area = areaCache.getByName(station.getCityName());
            if (area != null) {
                station.setCityId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (station.getDistrictName() != null) {
            Area area = areaCache.getByName(station.getDistrictName());
            if (area != null) {
                station.setDistrictId(Integer.valueOf(area.getAreaCode()));
            }
        }
        station.setGeoHash(GeoHashUtils.getGeoHashString(station.getLng(), station.getLat()));
//        AreaEntity areaEntity = setAreaProperties(areaCache, shop);
//        String address = StringUtils.trimToEmpty(areaEntity.getProvinceName()) + StringUtils.trimToEmpty(areaEntity.getCityName()) + StringUtils.trimToEmpty(areaEntity.getDistrictName()) + StringUtils.trimToEmpty(areaEntity.getStreet());
//        shop.setAddress(address);
        String street = null;
        if (station.getStreetName() != null) {
            street = station.getStreetName();
        }
        if (station.getStreetNumber() != null) {
            street = station.getStreetNumber();
        }
        if (station.getStreetName() != null && station.getStreetNumber() != null) {
            street = station.getStreetName() + station.getStreetNumber();
        }
        station.setStreet(street);
        station.setKeyword(station.getAddress() + entity.getStationName());
//        String address = StringUtils.trimToEmpty(StringUtils.trimToEmpty(shop.getProvinceName()) + StringUtils.trimToEmpty(shop.getCityName()) + StringUtils.trimToEmpty(shop.getDistrictName()) + StringUtils.trimToEmpty(shop.getStreet()));
//        shop.setAddress(address);
        int effect = stationMapper.update(station);
        if (effect == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult("操作成功");
    }


    public void updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {

        stationMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
    }

}
