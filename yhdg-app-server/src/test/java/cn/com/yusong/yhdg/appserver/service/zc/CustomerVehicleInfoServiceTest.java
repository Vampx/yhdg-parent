package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerVehicleInfoServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerVehicleInfoService service;

	@Test
	public void findByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
		insertCustomerVehicleInfo(customerVehicleInfo);

		assertNotNull(service.findByCustomerId(customer.getId()));

	}


}
