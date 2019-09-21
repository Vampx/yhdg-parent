package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AgentMonthStatsServiceTest extends BaseJunit4Test {
    @Autowired
    AgentMonthStatsService service;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMonthStats agentMonthStats = newAgentMonthStats("2017-08", agent.getId());
        insertAgentMonthStats(agentMonthStats);

        assertTrue(1 == service.findPage(agentMonthStats).getTotalItems());
        assertTrue(1 == service.findPage(agentMonthStats).getResult().size());

    }

    @Test
    public void findForExcel() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentMonthStats agentMonthStats = newAgentMonthStats("2017-08", agent.getId());
        insertAgentMonthStats(agentMonthStats);

        assertTrue(1 == service.findForExcel(agentMonthStats).size());
    }

}
