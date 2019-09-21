package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CabinetService extends AbstractService {

    static Logger log = LoggerFactory.getLogger(CabinetService.class);

    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetDynamicCodeCustomerMapper cabinetDynamicCodeCustomerMapper;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    InsuranceMapper insuranceMapper;

    public Cabinet find(String id) {
        return (Cabinet) setAreaProperties(areaCache, cabinetMapper.find(id));
    }

    public List<Cabinet> findBatteryCabinetList(Integer agentId, String keyword, int offset, int limit) {
        return cabinetMapper.findBatteryCabinetList(agentId, keyword, offset, limit);
    }

    public List<Cabinet> findVipCabinetList(Integer agentId, String keyword, int offset, int limit) {
        return cabinetMapper.findVipCabinetList(agentId, keyword, offset, limit);
    }

    public List<Cabinet> findList(Integer agentId, String shopId, String keyword, Integer offset, Integer limit) {
        return (List<Cabinet>) setAreaProperties(areaCache, cabinetMapper.findList(agentId, shopId, keyword, offset, limit));
    }

    public List<Cabinet> findNearest(Integer agentId, String geoHash, double lng, double lat, String keyword, Integer provinceId, Integer cityId, List<Integer> upLineStatus, List<Integer> isOnLine, Integer offset, Integer limit) {
        return (List<Cabinet>) setAreaProperties(areaCache,
                cabinetMapper.findNearest(agentId,
                        CabinetBox.BoxStatus.FULL.getValue(),
                        Battery.Status.IN_BOX.getValue(),
                        geoHash,
                        keyword,
                        lng,
                        lat,
                        provinceId,
                        cityId,
                        upLineStatus,
                        isOnLine,
                        offset, limit
                )
        );
    }

    public int findCountByAgentId (Integer agentId, List<Integer> upLineStatus,List<Integer> isOnLine) {
        return cabinetMapper.findCountByAgentId(agentId, upLineStatus, isOnLine);
    }

    public int updateDynamicCode(String id, String dynamicCode) {
        Cabinet dbCabinet = cabinetMapper.find(id);
        //如果修改了动态码,清空原先动态码和客户的绑定关系
        if (!dynamicCode.equals(dbCabinet.getDynamicCode())) {
            cabinetDynamicCodeCustomerMapper.deleteByCabinetId(id);
        }
        return cabinetMapper.updateDynamicCode(id, dynamicCode);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult update(Cabinet cabinet) {
        Cabinet dbCabinet = cabinetMapper.find(cabinet.getId());

        boolean changed = false;

        if (dbCabinet.getTerminalId() == null && cabinet.getTerminalId() != null) {
            changed = true;
        } else if (dbCabinet.getTerminalId() != null && cabinet.getTerminalId() == null) {
            changed = true;
        } else if (dbCabinet.getTerminalId() != null && cabinet.getTerminalId() != null
            &&  !dbCabinet.getTerminalId().equals(cabinet.getTerminalId())) {
            changed = true;
        }

        cabinetMapper.updateCabinetByAgent(cabinet);

        if (changed && StringUtils.isNotEmpty(dbCabinet.getLoginToken())) {
            String key = "token:" + dbCabinet.getLoginToken();
            memCachedClient.delete(key);
            if (log.isDebugEnabled()) {
                log.debug("柜子token删除了 {} token: {}", dbCabinet.getId(), key);
            }
        }

       //修改格口对应的满电电量
       cabinetBoxMapper.updateChargeFullVolume(cabinet.getId(), cabinet.getChargeFullVolume());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult updateAddress(String cabinetId,
                                         Integer areaId,
                                         String street,
                                         Double lng,
                                         Double lat){

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if(cabinet == null){
            return  RestResult.result(RespCode.CODE_2.getValue(),"换电柜不存在");
        }

        Area area = areaCache.get(areaId);
        Integer provinceId = null, cityId = null,districtId = null;
        if (area.getAreaLevel() == 3) {
            districtId = area.getId();
            cityId = area.getParentId();
            provinceId = areaCache.get(cityId).getParentId();
        } else if (area.getAreaLevel() == 2) {
            cityId = area.getId();
            provinceId = areaCache.get(cityId).getParentId();
        } else if (area.getAreaLevel() == 1) {
            provinceId = area.getId();
        }


        String geoHash = null;
        if(lng != null && lat != null && lng != 0 && lat != 0 ) {
            geoHash = GeoHashUtils.getGeoHashString(lng, lat);
        }else{
            lng = null;
            lat = null;
        }
        String address = cabinet.getAddress();
        if( provinceId != null || cityId != null || districtId != null || StringUtils.isNotEmpty(street)) {
            if (provinceId != null) {
                address = areaCache.get(provinceId).getAreaName();
            } else {
                address = areaCache.get(cabinet.getProvinceId()).getAreaName();
            }
            if (cityId != null) {
                address += areaCache.get(cityId).getAreaName();
            } else {
                address += areaCache.get(cabinet.getCityId()).getAreaName();
            }

            if (districtId != null) {
                address += areaCache.get(districtId).getAreaName();
            }else {
                address += areaCache.get(cabinet.getDistrictId()).getAreaName();
            }
            if (StringUtils.isNotEmpty(street)) {
                address += street;
            }else {
                address += cabinet.getStreet();
            }
        }

        if(StringUtils.equals(address,cabinet.getAddress())){
            address = null;
        }

        if(cabinetMapper.updateLocation(cabinetId, provinceId, cityId, districtId, street, lng, lat, geoHash, address, null, null) == 0){
            log.error("CabinetAddressCorrectionService.createResult.updateLocation:{}","更新地理位置出错");
            return RestResult.result(RespCode.CODE_1.getValue(),"更新换电柜地理位置出错") ;
        }
        return  RestResult.result(RespCode.CODE_0.getValue(),"换电柜纠错位置成功");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult cabinetBatteryList(String cabinetId) {

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if(cabinet == null){
            return  RestResult.result(RespCode.CODE_2.getValue(),"换电柜不存在");
        }

        List<Map> list = new ArrayList<Map>();
        //查询柜子对应电池类型
        List<CabinetBatteryType> batteryTypeList = cabinetBatteryTypeMapper.findListByCabinet(cabinetId);
        for(CabinetBatteryType cabinetBatteryType : batteryTypeList){
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.putInteger("batteryType", cabinetBatteryType.getBatteryType());
            notNullMap.putString("batteryTypeName", findBatteryType(cabinetBatteryType.getBatteryType()).getTypeName());
            list.add(notNullMap);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult cabinetForegiftList(Integer batteryType, Integer agentId) {

        List<Map> list = new ArrayList<Map>();

        List<ExchangeBatteryForegift>  exchangeBatteryForegiftList = exchangeBatteryForegiftMapper.findList(batteryType, agentId);
        for(ExchangeBatteryForegift exchangeBatteryForegift : exchangeBatteryForegiftList){
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("batteryType", exchangeBatteryForegift.getBatteryType());
            notNullMap.put("batteryTypeName", findBatteryType(exchangeBatteryForegift.getBatteryType()).getTypeName());
            notNullMap.put("foregiftId", exchangeBatteryForegift.getId());
            notNullMap.put("money", exchangeBatteryForegift.getMoney());
            notNullMap.put("memo", exchangeBatteryForegift.getMemo());
            list.add(notNullMap);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult cabinetPriceList(Integer batteryType, Integer agentId, Long foregiftId) {

        List<Map> list = new ArrayList<Map>();
        List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(foregiftId, batteryType, agentId);
        for (PacketPeriodPrice e : packetPeriodPriceList) {
            Map map = new HashMap();
            map.put("id", e.getId());
            map.put("dayCount", e.getDayCount());
            map.put("price", e.getPrice());
            map.put("limitCount", e.getLimitCount());
            map.put("dayLimitCount", e.getDayLimitCount());
            map.put("memo", e.getMemo());
            list.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult cabinetInsuranceList(Integer batteryType, Integer agentId) {

        List<Map> list = new ArrayList<Map>();
        List<Insurance> insuranceList = insuranceMapper.findListByBatteryType(batteryType, agentId);
        for (Insurance e : insuranceList) {
            Map map = new HashMap();
            map.put("id", e.getId());
            map.put("price", e.getPrice());
            map.put("money", e.getPaid());
            map.put("monthCount", e.getMonthCount());
            map.put("memo", e.getMemo());
            list.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }
}
