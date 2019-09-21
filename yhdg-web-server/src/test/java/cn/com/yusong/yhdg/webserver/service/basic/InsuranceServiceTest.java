package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.hdg.InsuranceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsuranceServiceTest extends BaseJunit4Test {
	@Autowired
	InsuranceService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Insurance insurance = newInsurance(agent.getId(), systemBatteryType.getId());
		insertInsurance(insurance);

		assertNotNull(service.find(insurance.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Insurance insurance = newInsurance(agent.getId(), systemBatteryType.getId());
		insertInsurance(insurance);

		assertTrue(1 == service.findPage(insurance).getTotalItems());
		assertTrue(1 == service.findPage(insurance).getResult().size());
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Insurance insurance = newInsurance(agent.getId(), systemBatteryType.getId());

		assertTrue(service.create(insurance).isSuccess());
		assertNotNull(service.find(insurance.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Insurance insurance = newInsurance(agent.getId(), systemBatteryType.getId());
		insertInsurance(insurance);

		insurance.setInsuranceName("测试的insuranceName");
		assertTrue(service.update(insurance).isSuccess());
		assertEquals(insurance.getInsuranceName(), service.find(insurance.getId()).getInsuranceName());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Insurance insurance = newInsurance(agent.getId(), systemBatteryType.getId());
		insertInsurance(insurance);

		assertTrue(service.delete(insurance.getId()).isSuccess());
		assertNull(service.find(insurance.getId()));
	}
}
