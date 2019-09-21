package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsuranceOrderServiceTest extends BaseJunit4Test {
	@Autowired
	InsuranceOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
		insertInsuranceOrder(insuranceOrder);

		assertNotNull(service.find(insuranceOrder.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
		insuranceOrder.setAgentId(agent.getId());
		insuranceOrder.setBatteryType(agentBatteryType.getBatteryType());
		insertInsuranceOrder(insuranceOrder);

		assertTrue(1 == service.findPage(insuranceOrder).getTotalItems());
		assertTrue(1 == service.findPage(insuranceOrder).getResult().size());
	}

	@Test
	public void findCanRefundByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
		insertInsuranceOrder(insuranceOrder);

		assertTrue(1 == service.findCanRefundByCustomerId(insuranceOrder.getCustomerId()).size());
	}
}
