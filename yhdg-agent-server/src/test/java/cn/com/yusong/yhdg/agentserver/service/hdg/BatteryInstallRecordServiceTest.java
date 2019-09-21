package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryInstallRecordServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryInstallRecordService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
        insertBatteryInstallRecord(batteryInstallRecord);

        assertNotNull(service.find(batteryInstallRecord.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
        insertBatteryInstallRecord(batteryInstallRecord);

        assertTrue(1 == service.findPage(batteryInstallRecord).getTotalItems());
        assertTrue(1 == service.findPage(batteryInstallRecord).getResult().size());
    }

    @Test
    public void findByBatteryType() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
        insertBatteryInstallRecord(batteryInstallRecord);

        assertNotNull(service.findByBatteryType(1, agent.getId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
        assertEquals(1, service.insert(batteryInstallRecord));
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
        insertBatteryInstallRecord(batteryInstallRecord);

        assertEquals(1, service.update(batteryInstallRecord.getId(), BatteryInstallRecord.Status.NOTONLINE.getValue()));
    }
}