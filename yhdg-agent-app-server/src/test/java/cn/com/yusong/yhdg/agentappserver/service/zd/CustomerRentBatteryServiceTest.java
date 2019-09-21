package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.agentappserver.service.hdg.AgentDayStatsService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerRentBatteryServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerRentBatteryService service;

    @Test
    public void findByCustomerId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery.getId(), battery.getType());
        insertCustomerRentBattery(CustomerRentBattery);

        assertEquals(service.findByCustomerId(customer.getId()).size(),1);
    }
}
