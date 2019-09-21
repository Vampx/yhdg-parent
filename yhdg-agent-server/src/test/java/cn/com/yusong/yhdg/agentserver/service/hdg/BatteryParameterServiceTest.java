package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryParameterServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryParameterService service;
    @Autowired
    private BatteryParameterLogService batteryParameterLogService;


    private BatteryParameter batteryParameter;

    @Before
    public void before() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        batteryParameter = newBatteryParameter(battery.getId());
    }

    @Test
    public void find() {
        insertBatteryParameter(batteryParameter);

        assertNotNull(service.find(batteryParameter.getId()));
    }

    @Test
    public void update() {
        insertBatteryParameter(batteryParameter);

        BatteryParameterLog batteryParameterLog = newBatteryParameterLog(batteryParameter.getId());
        batteryParameterLog.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
        int batteryParameterLogResults1 = batteryParameterLogService.findPage(batteryParameterLog).getResult().size();

        batteryParameter.setCellFullVol(123);
        assertTrue(service.update(batteryParameter, "admin").isSuccess());

        assertEquals(service.find(batteryParameter.getId()).getCellFullVol(), batteryParameter.getCellFullVol());
        int batteryParameterLogResults2 = batteryParameterLogService.findPage(batteryParameterLog).getResult().size();
        assertEquals(batteryParameterLogResults1 + 1, batteryParameterLogResults2);
    }
}