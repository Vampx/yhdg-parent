package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.ShopStoreVehicleBatteryMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopStoreVehicleBatteryService extends AbstractService {

    @Autowired
    ShopStoreVehicleBatteryMapper shopStoreVehicleBatteryMapper;

    public List<ShopStoreVehicleBattery> findByStoreVehicle(Long id) {
        return shopStoreVehicleBatteryMapper.findByStoreVehicle(id);
    }

    public int existBattery(String batteryId) {
        return shopStoreVehicleBatteryMapper.existBattery(batteryId);
    }
}
