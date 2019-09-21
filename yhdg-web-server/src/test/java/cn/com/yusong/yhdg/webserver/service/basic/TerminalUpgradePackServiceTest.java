package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalUpgradePackServiceTest extends BaseJunit4Test {
	@Autowired
	TerminalUpgradePackService service;

	@Test
	public void find() {
		TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
		insertTerminalUpgradePack(terminalUpgradePack);
		assertNotNull(service.find(terminalUpgradePack.getId()));
	}
	@Test
	public void findPage() {
		TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
		insertTerminalUpgradePack(terminalUpgradePack);
		assertTrue(1 == service.findPage(terminalUpgradePack).getTotalItems());
		assertTrue(1 == service.findPage(terminalUpgradePack).getResult().size());
	}

	@Test
	public void insert() {
		TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
		assertTrue(service.insert(terminalUpgradePack).isSuccess());
	}

	@Test
	public void update() throws Exception{
		TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
		insertTerminalUpgradePack(terminalUpgradePack);
		terminalUpgradePack.setFileName("d3e3");
		assertTrue(service.update(terminalUpgradePack).isSuccess());
		assertEquals(terminalUpgradePack.getFileName(), service.find(terminalUpgradePack.getId()).getFileName());
	}

	@Test
	public void delete(){
		TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
		insertTerminalUpgradePack(terminalUpgradePack);
		assertTrue(service.delete(terminalUpgradePack.getId().longValue()).isSuccess());
		assertNull(service.find(terminalUpgradePack.getId()));
	}

}
