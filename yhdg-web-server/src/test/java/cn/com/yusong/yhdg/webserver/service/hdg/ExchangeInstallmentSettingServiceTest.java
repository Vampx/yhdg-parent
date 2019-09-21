package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ExchangeInstallmentSettingServiceTest extends BaseJunit4Test {
	@Autowired
	ExchangeInstallmentSettingService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
		insertInsurance(insurance);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		exchangeBatteryForegift.setMoney(100);
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
		insertExchangeInstallmentSetting(exchangeInstallmentSetting);

		assertNotNull(service.find(exchangeInstallmentSetting.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
		insertInsurance(insurance);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		exchangeBatteryForegift.setMoney(100);
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
		insertExchangeInstallmentSetting(exchangeInstallmentSetting);

		ExchangeInstallmentDetail exchangeInstallmentDetail = newExchangeInstallmentDetail(exchangeInstallmentSetting.getId());
		insetExchangeInstallmentDetail(exchangeInstallmentDetail);

		assertTrue(1 == service.findPage(exchangeInstallmentSetting).getTotalItems());
		assertTrue(1 == service.findPage(exchangeInstallmentSetting).getResult().size());
	}

	@Test
	public void findUnique() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
		insertInsurance(insurance);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		exchangeBatteryForegift.setMoney(100);
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		assertTrue(service.findUnique(1L, "asdf"));

		ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
		insertExchangeInstallmentSetting(exchangeInstallmentSetting);
		assertTrue(service.findUnique(exchangeInstallmentSetting.getId(), exchangeInstallmentSetting.getMobile()));
		assertFalse(service.findUnique(1L, exchangeInstallmentSetting.getMobile()));
	}

	@Test
	public void create() throws IOException, ParseException {
		Map map = new HashMap<String, Object>();
		map.put("mobile", "1234");
		map.put("fullname", "test");
		map.put("agentId", 1);
		map.put("deadlineTime", "2012-12-12 23:59:59");
		map.put("totalMoney", 300);
		map.put("batteryType", 1);
		map.put("foregiftId", 1);
		map.put("foregiftMoney", 100);
		map.put("packetId", 1);
		map.put("packetMoney", 100);
		map.put("insuranceId", 1);
		map.put("insuranceMoney", 100);
		List<Map> detailList = new ArrayList<Map>();
		Map detail = new HashMap<String, Object>();
		detail.put("recentForegiftMoney", 50);
		detail.put("recentPacketMoney", 50);
		detail.put("recentInsuranceMoney", 50);
		detail.put("num", 1);
		detail.put("recentExpireTime", "2012-12-13 23:59:59");
		detailList.add(detail);
		Map detail2 = new HashMap<String, Object>();
		detail2.put("recentForegiftMoney", 50);
		detail2.put("recentPacketMoney", 50);
		detail2.put("recentInsuranceMoney", 50);
		detail2.put("num", 2);
		detail2.put("recentExpireTime", "2012-12-14 23:59:59");
		detailList.add(detail2);
		map.put("detailList", detailList);
		String data = AppUtils.encodeJson(map);
		ExtResult extResult = service.create(data);
		assertTrue(extResult.isSuccess());
		ExchangeInstallmentSetting exchangeInstallmentSetting = new ExchangeInstallmentSetting();
		assertTrue(1 == service.findPage(exchangeInstallmentSetting).getTotalItems());
		assertTrue(1 == service.findPage(exchangeInstallmentSetting).getResult().size());
	}

	@Test
	public void update() throws IOException, ParseException {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
		insertInsurance(insurance);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		exchangeBatteryForegift.setMoney(100);
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
		insertExchangeInstallmentSetting(exchangeInstallmentSetting);

		Map map = new HashMap<String, Object>();
		map.put("id", exchangeInstallmentSetting.getId());
		map.put("mobile", "1234");
		map.put("fullname", "test");
		map.put("agentId", 1);
		map.put("deadlineTime", "2012-12-12 23:59:59");
		map.put("totalMoney", 300);
		map.put("batteryType", 1);
		map.put("foregiftId", 1);
		map.put("foregiftMoney", 100);
		map.put("packetId", 1);
		map.put("packetMoney", 6381);
		map.put("insuranceId", 1);
		map.put("insuranceMoney", 100);
		List<Map> detailList = new ArrayList<Map>();
		Map detail = new HashMap<String, Object>();
		detail.put("recentForegiftMoney", 50);
		detail.put("recentPacketMoney", 50);
		detail.put("recentInsuranceMoney", 50);
		detail.put("num", 1);
		detail.put("recentExpireTime", "2012-12-13 23:59:59");
		detailList.add(detail);
		Map detail2 = new HashMap<String, Object>();
		detail2.put("recentForegiftMoney", 50);
		detail2.put("recentPacketMoney", 50);
		detail2.put("recentInsuranceMoney", 50);
		detail2.put("num", 2);
		detail2.put("recentExpireTime", "2012-12-14 23:59:59");
		detailList.add(detail2);
		map.put("detailList", detailList);
		String data = AppUtils.encodeJson(map);
		ExtResult extResult = service.update(data);
		assertTrue(extResult.isSuccess());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
		insertInsurance(insurance);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		exchangeBatteryForegift.setMoney(100);
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
		insertExchangeInstallmentSetting(exchangeInstallmentSetting);

		assertTrue(service.delete(exchangeInstallmentSetting.getId()).isSuccess());
		assertNull(service.find(exchangeInstallmentSetting.getId()));
	}


}
