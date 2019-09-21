package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CustomerRefundRecordServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerRefundRecordService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setAgentId(agent.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		assertNotNull(service.find(customerRefundRecord.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setAgentId(agent.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		assertTrue(1 == service.findPage(customerRefundRecord).getTotalItems());
		assertTrue(1 == service.findPage(customerRefundRecord).getResult().size());
	}

	@Test
	public void findByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setAgentId(agent.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
		customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		assertTrue(1 == service.findByCustomerId(customer.getId(), customerRefundRecord.getStatus()).size());
	}

	@Test
	public void newOrderId() {
		assertNotNull(service.newOrderId());
	}

	@Test
	public void updateStates() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setAgentId(agent.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
		customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		service.updateHdStatus(customerRefundRecord.getId(), CustomerRefundRecord.RefundType.BALANCE.getValue(), "asdf", customerRefundRecord.getStatus(), 1234, new Date());
		assertEquals(customerRefundRecord.getStatus().intValue(), service.find(customerRefundRecord.getId()).getStatus().intValue());
	}
}
