package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityCustomerServiceTest extends BaseJunit4Test {

    @Autowired
    ActivityCustomerService service;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(),battery.getType());
        insertAgentBatteryType(agentBatteryType);


        PacketPeriodActivity activity = newPacketPeriodActivity(agent.getId(),agentBatteryType.getBatteryType());
        insertPacketPeriodActivity(activity);

        ActivityCustomer activityCustomer = newActivityCustomer(activity.getId(), customer.getId());
        insertActivityCustomer(activityCustomer);

        assertTrue(1 == service.findPage(activityCustomer).getTotalItems());
        assertTrue(1 == service.findPage(activityCustomer).getResult().size());

    }


}

