package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.hdg.ShopUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ShopStoreBatteryServiceTest extends BaseJunit4Test {
    @Autowired
    ShopStoreBatteryService service;

    @Test
    public void findCount() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(agent.getId(), shop.getId(), battery.getId());
        insertShopStoreBattery(shopStoreBattery);

        assertTrue(1 == service.findCount(shop.getId()));

    }

    @Test
    public void findByBatteryId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(agent.getId(), shop.getId(), battery.getId());
        insertShopStoreBattery(shopStoreBattery);

        assertTrue(1 == service.findByBatteryId(battery.getId()));
    }



}
