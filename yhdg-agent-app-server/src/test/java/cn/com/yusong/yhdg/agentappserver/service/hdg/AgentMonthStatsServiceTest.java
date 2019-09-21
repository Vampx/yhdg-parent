package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentMonthStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentMonthStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMonthStats agentMonthStats = newAgentMonthStats("2017-08", agent.getId());
        insertAgentMonthStats(agentMonthStats);

        assertNotNull(service.find(agent.getId(), "2017-08", 1));
    }


    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMonthStats agentMonthStats = newAgentMonthStats("2017-08", agent.getId());
        insertAgentMonthStats(agentMonthStats);

        assertEquals(1,service.findList(agent.getId(),  1).size());
    }

    @Test
    public void findTotal() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMonthStats agentMonthStats = newAgentMonthStats("2017-08", agent.getId());
        insertAgentMonthStats(agentMonthStats);

        assertNotNull(service.findTotal(agent.getId(),  1));
    }

}
