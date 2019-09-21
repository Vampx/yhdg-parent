package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.ShopStoreVehicleBatteryMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopStoreVehicleBatteryService extends AbstractService {

    @Autowired
    ShopStoreVehicleBatteryMapper vehicleBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    ShopMapper shopMapper;

    public List<ShopStoreVehicleBattery> findByStoreVehicle(long storeVehicleId) {
        return vehicleBatteryMapper.findByStoreVehicle(storeVehicleId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(long storeVehicleId, String shopId,String batteryId){
        String[] ids = batteryId.split(",");
        vehicleBatteryMapper.deleteByStoreVehicle(storeVehicleId);
        for (String id : ids) {
            if (vehicleBatteryMapper.findByBatteryId(id) != null){
                return ExtResult.failResult("电池已存在");
            }
            Shop shop = shopMapper.find(shopId);
            if (shop == null) {
                return ExtResult.failResult("门店不存在");
            }
            Battery battery = batteryMapper.find(id);
            if (battery == null) {
                return ExtResult.failResult("电池不存在");
            }
            Agent agent = agentMapper.find(battery.getAgentId());
            if (agent == null) {
                return ExtResult.failResult("运营商不存在");
            }
            ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
            shopStoreBattery.setCategory(battery.getCategory());
            shopStoreBattery.setAgentId(battery.getAgentId());
            shopStoreBattery.setAgentName(agent.getAgentName());
            shopStoreBattery.setShopId(shopId);
            shopStoreBattery.setShopName(shop.getShopName());
            shopStoreBattery.setBatteryId(id);
            shopStoreBattery.setCreateTime(new Date());
            shopStoreBatteryMapper.insert(shopStoreBattery);
            batteryMapper.updateShopId(id,shopId,shop.getShopName());
            batteryMapper.updateStatus(id, Battery.Status.NOT_USE.getValue());
            ShopStoreVehicleBattery shopStoreVehicleBattery = new ShopStoreVehicleBattery();
            shopStoreVehicleBattery.setStoreVehicleId(storeVehicleId);
            shopStoreVehicleBattery.setBatteryId(id);
            vehicleBatteryMapper.insert(shopStoreVehicleBattery);
        }
        return ExtResult.successResult();
    }

    public ShopStoreVehicleBattery findByBatteryId(String batteryId) {
        return vehicleBatteryMapper.findByBatteryId(batteryId);
    }

    public ExtResult delete(long storeVehicleId, String shopId, String batteryId) {
        List<ShopStoreVehicleBattery> list = vehicleBatteryMapper.findByStoreVehicle(storeVehicleId);
        for (ShopStoreVehicleBattery storeBattery : list) {
            shopStoreBatteryMapper.deleteByShopBatteryId(shopId, batteryId);
            batteryMapper.updateShopId(batteryId,null,null);
            batteryMapper.updateStatus(batteryId, Battery.Status.NOT_USE.getValue());
            vehicleBatteryMapper.delete(storeVehicleId,batteryId);
        }
        return ExtResult.successResult();
    }
}
