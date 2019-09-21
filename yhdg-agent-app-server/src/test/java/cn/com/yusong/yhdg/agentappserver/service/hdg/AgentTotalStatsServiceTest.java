package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentTotalStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentTotalStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentTotalStats agentTotalStats = newAgentTotalStats(agent.getId());
        agentTotalStats.setCategory(1);
        insertAgentTotalStats(agentTotalStats);

        assertNotNull(service.find(agent.getId(), 1));
    }

}
