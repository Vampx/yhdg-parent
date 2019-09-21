package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentCabinetForegiftRecordServiceTest extends BaseJunit4Test {
	@Autowired
	AgentCabinetForegiftRecordService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
		insertAgentMaterialDayStats(agentMaterialDayStats);

		AgentCabinetForegiftRecord agentCabinetForegiftRecord = newAgentCabinetForegiftRecord(agent.getId(), cabinet.getId(), agentMaterialDayStats.getId());
		insertAgentCabinetForegiftRecord(agentCabinetForegiftRecord);

		assertTrue(1 == service.findPage(agentCabinetForegiftRecord).getTotalItems());
		assertTrue(1 == service.findPage(agentCabinetForegiftRecord).getResult().size());
	}

}
