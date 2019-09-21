package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentInOutMoneyServiceTest extends BaseJunit4Test {
	@Autowired
	AgentInOutMoneyService service;

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentInOutMoney agentInOutMoney = newAgentInOutMoney(agent.getId());
		insertAgentInOutMoney(agentInOutMoney);

		assertTrue(1 == service.findPage(agentInOutMoney).getTotalItems());
		assertTrue(1 == service.findPage(agentInOutMoney).getResult().size());
	}

	@Test
	public void findForExcel() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentInOutMoney agentInOutMoney = newAgentInOutMoney(agent.getId());
		insertAgentInOutMoney(agentInOutMoney);

		assertTrue(1 == service.findForExcel(agentInOutMoney).size());
	}
}
