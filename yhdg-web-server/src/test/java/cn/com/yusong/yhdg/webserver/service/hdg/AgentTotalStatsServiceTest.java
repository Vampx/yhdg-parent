package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentTotalStats;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentTotalStatsServiceTest extends BaseJunit4Test {
	@Autowired
	AgentTotalStatsService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentTotalStats stats = newAgentTotalStats(agent.getId());
		insertAgentTotalStats(stats);

		assertTrue(1 == service.findPage(stats).getTotalItems());
		assertTrue(1 == service.findPage(stats).getResult().size());
	}

	@Test
	public void findForExcel() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentTotalStats stats = newAgentTotalStats(agent.getId());
		insertAgentTotalStats(stats);

		assertTrue(1 == service.findForExcel(stats).size());
	}
}
