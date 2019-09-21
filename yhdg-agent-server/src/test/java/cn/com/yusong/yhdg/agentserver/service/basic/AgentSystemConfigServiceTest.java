package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentSystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    AgentSystemConfigService service;

    @Test
    public void findAll() {
        int count = jdbcTemplate.queryForInt("select count(*) from bas_agent_system_config");
        assertEquals(count, service.findAll().size());
    }

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        assertNotNull(service.find(agentSystemConfig.getId(), agent.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        assertTrue(1 == service.findPage(agentSystemConfig).getResult().size());
    }

    @Test
    public void update_0() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setIsActive(ConstEnum.Flag.FALSE.getValue());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue());
        insertAgentSystemConfig(agentSystemConfig);

        service.update(agentSystemConfig);

        assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_active from bas_agent where id = ?", agent.getId()));
    }

    @Test
    public void update_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setIsActive(ConstEnum.Flag.FALSE.getValue());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue());
        insertAgentSystemConfig(agentSystemConfig);

        service.update(agentSystemConfig);

        assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_active from bas_agent" +
				" where id = ?", agent.getId()));
    }

    @Test
    public void update_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setIsActive(ConstEnum.Flag.FALSE.getValue());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue());
        insertAgentSystemConfig(agentSystemConfig);

        service.update(agentSystemConfig);

        assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_active from bas_agent where id = ?", agent.getId()));
    }

}
