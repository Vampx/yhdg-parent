package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryTypeIncomeRatioServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryTypeIncomeRatioService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
		insertBatteryTypeIncomeRatio(batteryTypeIncomeRatio);

		assertNotNull(service.find(batteryTypeIncomeRatio.getId()));
	}

	@Test
	public void findByBatteryType() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
		insertBatteryTypeIncomeRatio(batteryTypeIncomeRatio);

		assertNotNull(service.findByBatteryType(systemBatteryType.getId(), agent.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
		insertBatteryTypeIncomeRatio(batteryTypeIncomeRatio);

		assertTrue(1 == service.findPage(batteryTypeIncomeRatio).getTotalItems());
		assertTrue(1 == service.findPage(batteryTypeIncomeRatio).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());

		assertTrue(service.create(batteryTypeIncomeRatio).isSuccess());
		assertNotNull(service.find(batteryTypeIncomeRatio.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
		insertBatteryTypeIncomeRatio(batteryTypeIncomeRatio);

		int rentPeriodMoney = 1234987;
		batteryTypeIncomeRatio.setRentPeriodMoney(rentPeriodMoney);
		assertTrue(service.update(batteryTypeIncomeRatio).isSuccess());
		assertEquals(rentPeriodMoney, service.find(batteryTypeIncomeRatio.getId()).getRentPeriodMoney().intValue());

	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		BatteryTypeIncomeRatio batteryTypeIncomeRatio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
		insertBatteryTypeIncomeRatio(batteryTypeIncomeRatio);

		assertTrue(service.delete(batteryTypeIncomeRatio.getId()).isSuccess());
		assertNull(service.find(batteryTypeIncomeRatio.getId()));
	}


}
