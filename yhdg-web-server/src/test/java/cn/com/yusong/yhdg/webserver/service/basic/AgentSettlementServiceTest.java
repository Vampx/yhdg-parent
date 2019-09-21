package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AgentSettlement;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentSettlementServiceTest extends BaseJunit4Test {
    @Autowired
    AgentSettlementService agentSettlementService;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSettlement agentSettlement = newAgentSettlement(agent.getId());
        insertAgentSettlement(agentSettlement);
        assertNotNull(agentSettlementService.find(agentSettlement.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSettlement agentSettlement = newAgentSettlement(agent.getId());
        insertAgentSettlement(agentSettlement);
        assertNotNull(agentSettlementService.findPage(agentSettlement));
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSettlement agentSettlement = newAgentSettlement(agent.getId());
        insertAgentSettlement(agentSettlement);
        agentSettlementService.update(agentSettlement.getId(), agentSettlement.getOperator());
    }
}
