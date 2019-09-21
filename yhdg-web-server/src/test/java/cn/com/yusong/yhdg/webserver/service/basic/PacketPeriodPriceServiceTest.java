package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodPriceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PacketPeriodPriceServiceTest extends BaseJunit4Test {
	@Autowired
	PacketPeriodPriceService service;

	@Test
	public void find() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		assertNotNull(service.find(packetPeriodPrice.getId()));
	}

	@Test
	public void findByBatteryType() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		packetPeriodPrice.setBatteryType(agentBatteryType.getBatteryType());
		insertPacketPeriodPrice(packetPeriodPrice);

		assertTrue(1 == service.findListByBatteryType(agentBatteryType.getBatteryType(), agent.getId()).size());
	}

	@Test
	public void findListByForegiftId() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		packetPeriodPrice.setBatteryType(agentBatteryType.getBatteryType());
		insertPacketPeriodPrice(packetPeriodPrice);

		assertTrue(1 == service.findListByForegiftId(exchangeBatteryForegift.getId(), agentBatteryType.getBatteryType(), agent.getId()).size());
	}

	@Test
	public void create() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());

		assertTrue(service.create(packetPeriodPrice).isSuccess());
		assertNotNull(service.find(packetPeriodPrice.getId()));
	}

	@Test
	public void update() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		packetPeriodPrice.setDayLimitCount(12394);
		assertTrue(service.update(packetPeriodPrice).isSuccess());
		assertEquals(packetPeriodPrice.getDayLimitCount(), service.find(packetPeriodPrice.getId()).getDayLimitCount());
	}

	@Test
	public void delete() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), agentBatteryType.getBatteryType());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
		insertPacketPeriodPrice(packetPeriodPrice);

		assertTrue(service.delete(packetPeriodPrice.getId()).isSuccess());
		assertNull(service.find(packetPeriodPrice.getId()));
	}
}
