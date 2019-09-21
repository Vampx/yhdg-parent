package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AgentDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentDayStatsService service;

    @Test
    public void findPage(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentDayStats agentDayStats = newAgentDayStats("2017-01-01", agent.getId());
        insertAgentDayStats(agentDayStats);
        assertTrue(1 == service.findPage(agentDayStats).getTotalItems());
        assertTrue(1 == service.findPage(agentDayStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentDayStats agentDayStats = newAgentDayStats("2017-01-01", agent.getId());
        insertAgentDayStats(agentDayStats);
        assertTrue(1 == service.findForExcel(agentDayStats).size());
    }
}
