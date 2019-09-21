package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangeWhiteListServiceTest extends BaseJunit4Test {
	@Autowired
	ExchangeWhiteListService service;
	@Autowired
	CustomerService customerService;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), systemBatteryType.getId());
		insertCabinetBatteryType(cabinetBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		ExchangeWhiteList exchangeWhiteList = newExchangeWhiteList(agent.getId(), customer.getId(), systemBatteryType.getId());
		insertExchangeWhiteList(exchangeWhiteList);

		assertNotNull(service.find(exchangeWhiteList.getId().longValue()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), systemBatteryType.getId());
		insertCabinetBatteryType(cabinetBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		ExchangeWhiteList exchangeWhiteList = newExchangeWhiteList(agent.getId(), customer.getId(), systemBatteryType.getId());
		insertExchangeWhiteList(exchangeWhiteList);

		assertTrue(1 == service.findPage(exchangeWhiteList).getTotalItems());
		assertTrue(1 == service.findPage(exchangeWhiteList).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), systemBatteryType.getId());
		insertCabinetBatteryType(cabinetBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		ExchangeWhiteList exchangeWhiteList = newExchangeWhiteList(agent.getId(), customer.getId(), systemBatteryType.getId());

		assertTrue(service.create(exchangeWhiteList).isSuccess());
		assertTrue(1 == service.findPage(exchangeWhiteList).getTotalItems());
		assertTrue(1 == service.findPage(exchangeWhiteList).getResult().size());
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Agent agent2 = newAgent(partner.getId());
		insertAgent(agent2);

		Customer customer = newCustomer(partner.getId());
		customer.setAgentId(agent2.getId());
		insertCustomer(customer);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		insertCabinet(cabinet);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), systemBatteryType.getId());
		insertCabinetBatteryType(cabinetBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		ExchangeWhiteList exchangeWhiteList = newExchangeWhiteList(agent.getId(), customer.getId(), systemBatteryType.getId());
		insertExchangeWhiteList(exchangeWhiteList);

		exchangeWhiteList.setMobile("测试的mobile");
		assertTrue(service.update(exchangeWhiteList).isSuccess());
		assertEquals(service.find(exchangeWhiteList.getId().longValue()).getMobile(), exchangeWhiteList.getMobile());
		assertEquals(customerService.find(customer.getId()).getAgentId(), exchangeWhiteList.getAgentId());
	}
}
