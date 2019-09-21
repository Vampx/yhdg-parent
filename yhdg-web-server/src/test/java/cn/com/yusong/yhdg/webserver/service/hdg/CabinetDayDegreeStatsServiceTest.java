package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetDayDegreeStatsServiceTest extends BaseJunit4Test {
	@Autowired
	CabinetDayDegreeStatsService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);


		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
		cabinetDayDegreeStats.setCabinetName("cabinetName");
		insertCabinetDayDegreeStats(cabinetDayDegreeStats);

		assertTrue(1 == service.findPage(cabinetDayDegreeStats).getTotalItems());
		assertTrue(1 == service.findPage(cabinetDayDegreeStats).getResult().size());
	}


}
