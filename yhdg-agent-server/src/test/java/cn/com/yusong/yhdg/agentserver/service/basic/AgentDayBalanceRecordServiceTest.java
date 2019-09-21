package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentDayBalanceRecordServiceTest extends BaseJunit4Test {
	@Autowired
	AgentDayBalanceRecordService service;

	/*@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
		insertAgentDayBalanceRecord(agentDayBalanceRecord);

		assertNotNull(service.find(agentDayBalanceRecord.getId()));
	}

	@Test
	public void findBayagentlist() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
		insertAgentDayBalanceRecord(agentDayBalanceRecord);

		assertTrue(1==service.findBayagentlist(agentDayBalanceRecord.getAgentId(),agentDayBalanceRecord.getBizType()).size());
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
		insertAgentDayBalanceRecord(agentDayBalanceRecord);

		assertTrue(1 == service.findPage(agentDayBalanceRecord).getTotalItems());
		assertTrue(1==service.findPage(agentDayBalanceRecord).getResult().size());
	}

	@Test
	public void confirm() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
		insertAgentDayBalanceRecord(agentDayBalanceRecord);
		AgentDayBalanceRecord agentDayBalanceRecord2 = newAgentDayBalanceRecord(agent.getId());
		agentDayBalanceRecord2.setId(2L);
		insertAgentDayBalanceRecord(agentDayBalanceRecord2);

		Long[] ids = {agentDayBalanceRecord.getId(), agentDayBalanceRecord2.getId()};
		assertEquals("confirm fail", String.format("成功确认%d条", 2), service.confirm(ids,agentDayBalanceRecord.getConfirmTime(),agentDayBalanceRecord.getConfirmUser()).getMessage());
	}

	@Test
	public void confirmStatus() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
		insertAgentDayBalanceRecord(agentDayBalanceRecord);
		AgentDayBalanceRecord agentDayBalanceRecord2 = newAgentDayBalanceRecord(agent.getId());
		agentDayBalanceRecord2.setId(2L);
		insertAgentDayBalanceRecord(agentDayBalanceRecord2);

		Long[] ids = {agentDayBalanceRecord.getId(), agentDayBalanceRecord2.getId()};
		assertEquals("confirmStatus fail", String.format("成功确认%d条", 2), service.confirmStatus(ids,agentDayBalanceRecord.getConfirmTime(),agentDayBalanceRecord.getConfirmUser()).getMessage());
	}*/
}
