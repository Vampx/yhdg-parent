package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UnregisterBatteryServiceTest extends BaseJunit4Test {
    @Autowired
    UnregisterBatteryService service;

    @Test
    public void findPage() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertTrue(1 == service.findPage(unregisterBattery).getTotalItems());
        assertTrue(1 == service.findPage(unregisterBattery).getResult().size());
    }

    @Test
    public void find() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertNotNull(service.find(unregisterBattery.getId()));
    }
}
