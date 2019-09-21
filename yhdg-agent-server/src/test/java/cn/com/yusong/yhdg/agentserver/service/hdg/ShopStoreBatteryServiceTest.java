package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopStoreBatteryServiceTest extends BaseJunit4Test {

    @Autowired
    ShopStoreBatteryService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Shop shop = newShop(agent.getId());
        insertShop(shop);
        Battery battery = newBattery(agent.getId(),1);
        insertBattery(battery);
        ShopStoreBattery shopStoreBattery = newShopStoreBattery(agent.getId(),shop.getId(),battery.getId());
        insertShopStoreBattery(shopStoreBattery);
        assertTrue(1 <= service.findPage(shopStoreBattery).getTotalItems());
    }
}
