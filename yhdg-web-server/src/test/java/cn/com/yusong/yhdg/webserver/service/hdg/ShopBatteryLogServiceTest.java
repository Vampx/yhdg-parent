package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopBatteryLog;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopBatteryLogServiceTest extends BaseJunit4Test {

    @Autowired
    ShopBatteryLogService service;

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
        ShopBatteryLog shopBatteryLog = newShopBatteryLog(agent.getId(),shop.getId(),battery.getId());
        insertShopBatteryLog(shopBatteryLog);
        assertTrue(service.findPage(shopBatteryLog).getTotalItems() >= 1);
    }
}
