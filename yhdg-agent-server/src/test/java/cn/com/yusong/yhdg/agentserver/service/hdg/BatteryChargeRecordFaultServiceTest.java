package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordFault;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryChargeRecordFaultServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryChargeRecordFaultService service;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryChargeRecord chargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(chargeRecord);

        BatteryChargeRecordFault chargeRecordFault = newBatteryChargeRecordFault(chargeRecord.getId());
        insertBatteryChargeRecordFault(chargeRecordFault);

        assertTrue(1 == service.findPage(chargeRecordFault).getTotalItems());
        assertTrue(1 == service.findPage(chargeRecordFault).getResult().size());
    }
}
