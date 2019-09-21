package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetInstallRecordServiceTest extends BaseJunit4Test {
	@Autowired
	CabinetInstallRecordService service;
	@Autowired
	CabinetService cabinetService;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner);

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

		CabinetInstallRecord cabinetInstallRecord = newCabinetInstallRecord(agent.getId(), cabinet.getId());
		insertCabinetInstallRecord(cabinetInstallRecord);

		assertNotNull(service.find(cabinetInstallRecord.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner);

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

		CabinetInstallRecord cabinetInstallRecord = newCabinetInstallRecord(agent.getId(), cabinet.getId());
		insertCabinetInstallRecord(cabinetInstallRecord);

		assertTrue(1 == service.findPage(cabinetInstallRecord).getTotalItems());
		assertTrue(1 == service.findPage(cabinetInstallRecord).getResult().size());
	}

	@Test
	public void updateUnline() {
		Partner partner = newPartner(); insertPartner(partner);

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

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		cabinet.setUpLineStatus(Cabinet.UpLineStatus.NOT_ONLINE.getValue());
		insertCabinet(cabinet);

		CabinetInstallRecord cabinetInstallRecord = newCabinetInstallRecord(agent.getId(), cabinet.getId());
		cabinetInstallRecord.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
		insertCabinetInstallRecord(cabinetInstallRecord);

		cabinetInstallRecord.setRentMoney(4890203);
		assertTrue(service.updateUpLine(cabinetInstallRecord).isSuccess());
		assertEquals(cabinetInstallRecord.getRentMoney(), service.find(cabinetInstallRecord.getId()).getRentMoney());

		Cabinet cabinet1 = cabinetService.find(cabinet.getId());
		assertEquals(cabinet1.getUpLineStatus().intValue(), Cabinet.UpLineStatus.ONLINE.getValue());
	}
}
