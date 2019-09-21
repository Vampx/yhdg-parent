package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryParameterLogServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryParameterLogService service;

    private BatteryParameterLog batteryParameterLog;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        batteryParameterLog = newBatteryParameterLog(battery.getId(), "111");
    }

    @Test
    public void findPage() {
        insertBatteryParameterLog(batteryParameterLog);

        assertTrue(1 == service.findPage(batteryParameterLog).getTotalItems());
        assertTrue(1 == service.findPage(batteryParameterLog).getResult().size());
    }

    @Test
    public void find() {
        insertBatteryParameterLog(batteryParameterLog);

        assertNotNull(service.find(batteryParameterLog.getId()));
    }
}