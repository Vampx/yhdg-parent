package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftRefundDetailed;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerForegiftRefundDetailedServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerForegiftRefundDetailedService service;

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerForegiftRefundDetailed entity = newCustomerForegiftRefundDetailed();
		entity.setId(customerForegiftOrder.getId());
		insertCustomerForegiftRefundDetailed(entity);

		assertTrue(1 == service.findPage(entity).getTotalItems());
		assertTrue(1 == service.findPage(entity).getResult().size());
	}

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		CustomerForegiftRefundDetailed entity = newCustomerForegiftRefundDetailed();
		entity.setId(customerForegiftOrder.getId());
		insertCustomerForegiftRefundDetailed(entity);

		assertNotNull(service.find(entity.getId(), entity.getNum()));
	}
}
