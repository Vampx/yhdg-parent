package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwPayOrderServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwPayOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertNotNull(service.find(alipayfwPayOrder.getId()));
	}

	@Test
	public void findBySourceId() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertNotNull(service.findBySourceId(alipayfwPayOrder.getSourceId()));
	}

	@Test
	public void findList() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertTrue(1 == service.findList(alipayfwPayOrder.getMobile()).size());
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertTrue(1 == service.findPage(alipayfwPayOrder).getTotalItems());
		assertTrue(1 == service.findPage(alipayfwPayOrder).getResult().size());
	}

	@Test
	public void findPageByPacketRefund() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertNotNull(service.findPageByPacketRefund(alipayfwPayOrder));
	}

}
