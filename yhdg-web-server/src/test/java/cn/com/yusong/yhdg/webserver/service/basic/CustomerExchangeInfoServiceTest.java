package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerExchangeInfoServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerExchangeInfoService service;

	@Test
	public void findByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
		customerExchangeInfo.setId(customer.getId());
		insertCustomerExchangeInfo(customerExchangeInfo);

		assertNotNull(service.findByCustomerId(customer.getId()));

	}
}
