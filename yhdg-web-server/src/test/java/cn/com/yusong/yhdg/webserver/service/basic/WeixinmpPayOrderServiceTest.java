package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinmpPayOrderServiceTest extends BaseJunit4Test {
	@Autowired
	WeixinmpPayOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
	
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertWeixinmpPayOrder(weixinmpPayOrder);
		assertNotNull(service.find(weixinmpPayOrder.getId()));
	}

	@Test
	public void findBySourceId() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertWeixinmpPayOrder(weixinmpPayOrder);
		assertNotNull(service.findBySourceId(weixinmpPayOrder.getSourceId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
	
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertWeixinmpPayOrder(weixinmpPayOrder);
		assertTrue(1 == service.findPage(weixinmpPayOrder).getTotalItems());
		assertTrue(1 == service.findPage(weixinmpPayOrder).getResult().size());
	}
	@Test
	public void findList() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
	
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
		weixinmpPayOrder.setOrderStatus(WeixinmpPayOrder.Status.SUCCESS.getValue());
		insertWeixinmpPayOrder(weixinmpPayOrder);

		assertTrue(1 == service.findList(weixinmpPayOrder.getMobile()).size());
	}
}
