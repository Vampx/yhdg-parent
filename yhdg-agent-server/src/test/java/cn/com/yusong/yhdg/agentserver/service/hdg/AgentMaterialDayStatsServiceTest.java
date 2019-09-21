package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AgentMaterialDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    private AgentMaterialDayStatsService service;

    private AgentMaterialDayStats agentMaterialDayStats;
    private Agent agent;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        agent = newAgent(partner.getId());
        insertAgent(agent);

        agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
    }

    @Test
    public void findPage() {
        insertAgentMaterialDayStats(agentMaterialDayStats);

        assertTrue(1 == service.findPage(agentMaterialDayStats).getTotalItems());
        assertTrue(1 == service.findPage(agentMaterialDayStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        insertAgentMaterialDayStats(agentMaterialDayStats);

        assertTrue(1 == service.findForExcel(agentMaterialDayStats).size());
    }

    @Test
    public void payMoney() {
        AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(this.agentMaterialDayStats.getAgentId());
        insertAgentMaterialDayStats(agentMaterialDayStats);

        PlatformAccount platformAccount = newPlatformAccount(agent.getPartnerId());
        insertPlatformAccount(platformAccount);

        int[] ids = {agentMaterialDayStats.getId()};
        assertTrue(service.payMoney(ids, "tewsss").isSuccess());
    }
}