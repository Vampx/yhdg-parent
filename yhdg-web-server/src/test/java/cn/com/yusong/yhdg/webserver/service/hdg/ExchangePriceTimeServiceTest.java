package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangePriceTimeServiceTest extends BaseJunit4Test {
	@Autowired
	ExchangePriceTimeService service;

	@Test
	public void findByBatteryType() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		assertNotNull(service.findByBatteryType(systemBatteryType.getId(), agent.getId()));
	}

	@Test
	public void insert() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());

		assertTrue(service.insert(exchangePriceTime).isSuccess());
		assertNotNull(service.findByBatteryType(systemBatteryType.getId(), agent.getId()));
	}

	@Test
	public void update() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
		insertExchangePriceTime(exchangePriceTime);

		exchangePriceTime.setTimesPrice(1252);
		assertTrue(service.update(exchangePriceTime).isSuccess());
		assertEquals(exchangePriceTime.getTimesPrice(), service.findByBatteryType(systemBatteryType.getId(), agent.getId()).getTimesPrice());
	}
}
