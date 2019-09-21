package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopStoreBatteryService extends AbstractService{
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;

    public int findCount(String shopId) {
        return shopStoreBatteryMapper.findCount(shopId);
    }

    public int findByBatteryId(String batteryId) {
        return shopStoreBatteryMapper.findByBatteryId(batteryId);
    }

}
