package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentMaterialDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentMaterialDayStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentDayStats agentDayStats = newAgentDayStats("2019-08-06", agent.getId());
        insertAgentDayStats(agentDayStats);

        AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
        agentMaterialDayStats.setStatsDate("2012-12-12");
        insertAgentMaterialDayStats(agentMaterialDayStats);

        assertNotNull(service.find(agentDayStats.getAgentId(), "2012-12-12", 1));
    }


    @Test
    public void findTotalMoney() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
        agentMaterialDayStats.setStatsDate("2012-12-12");
        insertAgentMaterialDayStats(agentMaterialDayStats);

        assertNotNull(service.findTotalMoney(agent.getId(), 1));
    }

    @Test
    public void findTotalRentMoney() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
        agentMaterialDayStats.setStatsDate("2012-12-12");
        insertAgentMaterialDayStats(agentMaterialDayStats);

        assertNotNull(service.findTotalRentMoney(agent.getId(), "2012-12-12", "2012-12-12",1));
    }
}
