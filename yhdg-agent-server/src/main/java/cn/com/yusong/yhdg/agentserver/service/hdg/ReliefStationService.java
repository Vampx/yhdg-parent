package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerMapper;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ReliefStationMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReliefStationService extends AbstractService {
    @Autowired
    ReliefStationMapper reliefStationMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    AreaCache areaCache;

    public ReliefStation find(long id) {
        ReliefStation reliefStation = reliefStationMapper.find(id);
        return (ReliefStation) setAreaProperties(areaCache, reliefStation);
    }

    public Page findPage(ReliefStation search) {
        Page page = search.buildPage();
        page.setTotalItems(reliefStationMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ReliefStation> reliefStationList = reliefStationMapper.findPageResult(search);
        for (ReliefStation reliefStation : reliefStationList) {
            Partner partner = partnerMapper.find(reliefStation.getPartnerId());
            if (partner != null) {
                reliefStation.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(setAreaProperties(areaCache, reliefStationList));
        return page;
    }

    public ExtResult create(ReliefStation shop) {
        if (shop != null) {
            if (shop.getMinPrice() > shop.getMaxPrice()) {
                int price = shop.getMinPrice();
                shop.setMinPrice(shop.getMaxPrice());
                shop.setMaxPrice(price);
            }
        }
        shop.setProvinceId(Constant.PROVINCE_ID);
        shop.setCityId(Constant.CITY_ID);
        shop.setDistrictId(Constant.DISTRICT_ID);
        shop.setLng(Constant.LNG);
        shop.setLat(Constant.LAT);
        reliefStationMapper.insert(shop);
        return ExtResult.successResult();
    }

    public ExtResult newCreate(ReliefStation shop) {
        if (shop != null) {
            if (shop.getMinPrice() > shop.getMaxPrice()) {
                int price = shop.getMinPrice();
                shop.setMinPrice(shop.getMaxPrice());
                shop.setMaxPrice(price);
            }
        }
        if (reliefStationMapper.newInsert(shop) == 0) {
            return DataResult.failResult("对不起! 保存失败", null);
        }
        return DataResult.successResult("操作成功",shop.getId());
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(ReliefStation reliefStation) {
        if (reliefStation != null) {
            if (reliefStation.getMinPrice() != null && reliefStation.getMinPrice() != null) {
                if (reliefStation.getMinPrice() > reliefStation.getMaxPrice()) {
                    int price = reliefStation.getMinPrice();
                    reliefStation.setMinPrice(reliefStation.getMaxPrice());
                    reliefStation.setMaxPrice(price);
                }
            }
        }
        ReliefStation entity = reliefStationMapper.find(reliefStation.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (reliefStation.getProvinceName() != null) {
            if (reliefStation.getProvinceName().substring(reliefStation.getProvinceName().length() - 1).equals("市")) {
                reliefStation.setProvinceName(reliefStation.getProvinceName().substring(0, reliefStation.getProvinceName().length() - 1));
            }
            Area area = areaCache.getByName(reliefStation.getProvinceName());
            if (area != null) {
                reliefStation.setProvinceId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (reliefStation.getCityName() != null) {
            Area area = areaCache.getByName(reliefStation.getCityName());
            if (area != null) {
                reliefStation.setCityId(Integer.valueOf(area.getAreaCode()));
            }
        }
        if (reliefStation.getDistrictName() != null) {
            Area area = areaCache.getByName(reliefStation.getDistrictName());
            if (area != null) {
                reliefStation.setDistrictId(Integer.valueOf(area.getAreaCode()));
            }
        }
//        String street = null;
//        if (reliefStation.getStreetName() != null) {
//            street = reliefStation.getStreetName();
//        }
//        if (reliefStation.getStreetNumber() != null) {
//            street = reliefStation.getStreetNumber();
//        }
//        if (reliefStation.getStreetName() != null && reliefStation.getStreetNumber() != null) {
//            street = reliefStation.getStreetName() + reliefStation.getStreetNumber();
//        }
//        reliefStation.setStreet(street);

        if (reliefStationMapper.update(reliefStation) == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult("操作成功");
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(long id) {
        int total = reliefStationMapper.delete(id);
        if (total == 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("删除失败");
        }
    }
}
