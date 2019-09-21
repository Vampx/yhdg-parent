package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangeBatteryForegiftServiceTest extends BaseJunit4Test {
	@Autowired
	ExchangeBatteryForegiftService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		assertNotNull(service.find(exchangeBatteryForegift.getId()));
	}

	@Test
	public void findListByBatteryType() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		assertTrue(1 == service.findListByBatteryType(exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId()).size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());

		assertTrue(service.create(exchangeBatteryForegift).isSuccess());
		assertNotNull(service.find(exchangeBatteryForegift.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		exchangeBatteryForegift.setMoney(345435);
		assertTrue(service.update(exchangeBatteryForegift).isSuccess());
		assertEquals(service.find(exchangeBatteryForegift.getId()).getMoney(), exchangeBatteryForegift.getMoney());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
		insertExchangeBatteryForegift(exchangeBatteryForegift);

		assertTrue(service.delete(exchangeBatteryForegift.getId()).isSuccess());
		assertNull(service.find(exchangeBatteryForegift.getId()));
	}
}
