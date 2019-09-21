package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentSystemConfigServiceTest extends BaseJunit4Test {

    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @Test
    public void findConfigValue() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);
        assertNotNull(agentSystemConfigService.findConfigValue(agentSystemConfig.getId(), agent.getId()));
    }
}
