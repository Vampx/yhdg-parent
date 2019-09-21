package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetOnlineStatsServiceTest extends BaseJunit4Test {
	@Autowired
	CabinetOnlineStatsService service;

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		CabinetOnlineStats subcabinetOnlineStats = newSubcabinetOnlineStats(cabinet.getId());
		insertSubcabinetOnlineStats(subcabinetOnlineStats);

		assertTrue(1 == service.findPage(subcabinetOnlineStats).getTotalItems());
		assertTrue(1 == service.findPage(subcabinetOnlineStats).getResult().size());
	}
}
