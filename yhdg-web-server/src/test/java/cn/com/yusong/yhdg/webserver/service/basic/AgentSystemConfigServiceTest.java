package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

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
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
		insertAgentSystemConfig(agentSystemConfig);

		assertNotNull(service.find(agentSystemConfig.getId(),agent.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
		insertAgentSystemConfig(agentSystemConfig);

		assertTrue(1 == service.findPage(agentSystemConfig).getResult().size());
	}

	@Test
	public void findConfigValue() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
		insertAgentSystemConfig(agentSystemConfig);

		assertNotNull(service.findConfigValue(agentSystemConfig.getId(), agent.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
		agentSystemConfig.setId("sddfwebgfe");
		insertAgentSystemConfig(agentSystemConfig);

		agentSystemConfig.setConfigValue("6");
		service.update(agentSystemConfig);

		Map<String, Object> map = jdbcTemplate.queryForMap("select config_value from bas_agent_system_config where id = ?", agentSystemConfig.getId());
		assertEquals("6", map.get("config_value"));
	}

//	@Test
//	public void update_0() {
//		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//		agent.setIsSelfBalance(ConstEnum.Flag.FALSE.getValue());
//		insertAgent(agent);
//
//		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
//		agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.FW_APP_ID.getValue());
//		insertAgentSystemConfig(agentSystemConfig);
//
//		service.update(agentSystemConfig);
//
//		assertEquals(ConstEnum.Flag.TRUE.getValue(), jdbcTemplate.queryForInt("select is_self_platform from bas_agent where id = ?", agent.getId()));
//	}
//
//	@Test
//	public void update_1() {
//		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//		agent.setIsSelfBalance(ConstEnum.Flag.FALSE.getValue());
//		insertAgent(agent);
//
//		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
//		agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.WXMP_APP_ID.getValue());
//		insertAgentSystemConfig(agentSystemConfig);
//
//		service.update(agentSystemConfig);
//
//		assertEquals(ConstEnum.Flag.TRUE.getValue(), jdbcTemplate.queryForInt("select is_self_platform from bas_agent where id = ?", agent.getId()));
//	}
//
//	@Test
//	public void update_2() {
//		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//		agent.setIsSelfBalance(ConstEnum.Flag.FALSE.getValue());
//		insertAgent(agent);
//
//		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
//		agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.ALIPAY_ACCOUNT_NAME.getValue());
//		insertAgentSystemConfig(agentSystemConfig);
//
//		service.update(agentSystemConfig);
//
//		assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_self_balance from bas_agent where id = ?", agent.getId()));
//	}
//
//	@Test
//	public void initConfig() {
//		service.initConfig();
//	}
}
