package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IdCardAuthRecordServiceTest extends BaseJunit4Test {
	@Autowired
	IdCardAuthRecordService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		IdCardAuthRecord idCardAuthRecord = newIdCardAuthRecord(agent.getId(), customer.getId());
		insertIdCardAuthRecord(idCardAuthRecord);

		assertTrue(1 == service.findPage(idCardAuthRecord).getTotalItems());
		assertTrue(1 == service.findPage(idCardAuthRecord).getResult().size());
	}
}
