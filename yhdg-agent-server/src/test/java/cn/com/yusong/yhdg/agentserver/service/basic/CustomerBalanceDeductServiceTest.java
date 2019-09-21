package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerBalanceDeduct;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerBalanceDeductServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerBalanceDeductService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		CustomerBalanceDeduct customerBalanceDeduct = newCustomerBalanceDeduct(partner.getId(), customer.getId().intValue());
		insertCustomerBalanceDeduct(customerBalanceDeduct);

		assertNotNull(service.find(customerBalanceDeduct.getId()));
	}
	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		CustomerBalanceDeduct customerBalanceDeduct = newCustomerBalanceDeduct(partner.getId(), customer.getId().intValue());
		insertCustomerBalanceDeduct(customerBalanceDeduct);

		assertTrue(1 == service.findPage(customerBalanceDeduct).getTotalItems());
		assertTrue(1 == service.findPage(customerBalanceDeduct).getResult().size());
	}

}
