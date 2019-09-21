package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryChargeRecordServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryChargeRecordService service;

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

        assertTrue(1 == service.findPage(chargeRecord).getTotalItems());
        assertTrue(1 == service.findPage(chargeRecord).getResult().size());
    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryChargeRecord chargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(chargeRecord);

        assertNotNull(service.find(chargeRecord.getId()));
    }
}
