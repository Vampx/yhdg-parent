package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentPublicNoticeServiceTest extends BaseJunit4Test {
	@Autowired AgentPublicNoticeService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentPublicNotice agentPublicNotice = newAgentPublicNotice(agent.getId());
		insertAgentPublicNotice(agentPublicNotice);

		assertNotNull(service.find(agentPublicNotice.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentPublicNotice agentPublicNotice = newAgentPublicNotice(agent.getId());
		insertAgentPublicNotice(agentPublicNotice);

		assertTrue(1 == service.findPage(agentPublicNotice).getTotalItems());
		assertTrue(1 == service.findPage(agentPublicNotice).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentPublicNotice agentPublicNotice = newAgentPublicNotice(agent.getId());

		assertTrue(service.create(agentPublicNotice).isSuccess());
	}

	@Test
	public void update() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentPublicNotice agentPublicNotice = newAgentPublicNotice(agent.getId());
		insertAgentPublicNotice(agentPublicNotice);
		agentPublicNotice.setTitle("qwer");
		assertTrue(service.update(agentPublicNotice).isSuccess());
		assertTrue(service.find(agentPublicNotice.getId()).getTitle().equals("qwer"));
	}

	@Test
	public void delete() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		AgentPublicNotice agentPublicNotice = newAgentPublicNotice(agent.getId());
		insertAgentPublicNotice(agentPublicNotice);
		assertTrue(service.delete(agentPublicNotice.getId()).isSuccess());
		assertNull(service.find(agentPublicNotice.getId()));
	}

	@Test
	public void pushMetaDataCreate() {
		assertTrue(1 == service.pushMetaDataCreate("1", 1));
	}
}
