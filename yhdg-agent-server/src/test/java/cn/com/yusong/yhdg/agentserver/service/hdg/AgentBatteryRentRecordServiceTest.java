package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.AgentBatteryRentRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AgentBatteryRentRecordServiceTest extends BaseJunit4Test {
    @Autowired
    private AgentBatteryRentRecordService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
        insertAgentMaterialDayStats(agentMaterialDayStats);

        AgentBatteryRentRecord rentRecord = newAgentBatteryRentRecord(agent.getId(), battery.getId(), agentMaterialDayStats.getId());
        insertAgentBatteryRentRecord(rentRecord);

        assertTrue(1 == service.findPage(rentRecord).getTotalItems());
        assertTrue(1 == service.findPage(rentRecord).getResult().size());
    }
}