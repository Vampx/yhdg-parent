package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentDayStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentDayStats agentDayStats = newAgentDayStats("2019-08-06", agent.getId());
        insertAgentDayStats(agentDayStats);

        assertNotNull(service.find(agentDayStats.getAgentId(), "2019-08-06", 1));
    }


    @Test
    public void findAgentDayTotal() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentDayStats agentDayStats = newAgentDayStats("2019-08-06", agent.getId());
        insertAgentDayStats(agentDayStats);

        assertNotNull(service.findAgentDayTotal(agent.getId(), "2019-08-06", "2019-08-06", 1));
    }
}
