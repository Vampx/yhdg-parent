package cn.com.yusong.yhdg.appserver.service.hdg;


import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopStoreBatteryService extends AbstractService {
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;

    public ShopStoreBattery findByBattery(String batteryId) {
        return shopStoreBatteryMapper.findByBattery(batteryId);
    }

    public List<ShopStoreBattery> findByShopId(String shopId, Integer category) {
        return shopStoreBatteryMapper.findByShopId(shopId, category);
    }

}
