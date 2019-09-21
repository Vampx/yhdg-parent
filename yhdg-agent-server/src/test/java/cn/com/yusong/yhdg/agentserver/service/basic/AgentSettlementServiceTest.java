package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSettlement;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AgentSettlementServiceTest extends BaseJunit4Test {
    @Autowired
    private AgentSettlementService service;

    private AgentSettlement agentSettlement;

    @Before
    public void before() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        agentSettlement = newAgentSettlement(agent.getId());
    }

   /* @Test
    public void find() {
        insertAgentSettlement(agentSettlement);

        assertNotNull(service.find(agentSettlement.getId()));
    }

    @Test
    public void findPage() {
        insertAgentSettlement(agentSettlement);
        assertNotNull(service.findPage(agentSettlement));
    }

    @Test
    public void update() {
        insertAgentSettlement(agentSettlement);

        assertTrue(service.update(agentSettlement.getId(), "test").isSuccess());

        assertEquals(jdbcTemplate.queryForObject("SELECT operator FROM hdg_battery_type_income_ratio", String.class), "test");
    }*/
}