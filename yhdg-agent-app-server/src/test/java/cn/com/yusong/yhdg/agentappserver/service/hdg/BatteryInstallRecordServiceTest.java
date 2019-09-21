package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryInstallRecordServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryInstallRecordService batteryInstallRecordService;

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
        assertEquals(1, batteryInstallRecordService.insert(batteryInstallRecord, Battery.Category.EXCHANGE.getValue()));
    }
}
