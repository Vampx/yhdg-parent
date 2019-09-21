package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RentBatteryTypeServiceTest extends BaseJunit4Test {
    @Autowired
    RentBatteryTypeService service;

    @Test
    public void findListByAgentId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentBatteryType rentBatteryType = newRentBatteryType(agent.getId(), systemBatteryType.getId());
        insertRentBatteryType(rentBatteryType);

        assertEquals(service.findListByAgentId(rentBatteryType.getAgentId()).size(),1);
    }
}
