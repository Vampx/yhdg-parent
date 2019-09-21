package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.basic.LaxinCustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinCustomerServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinCustomerService laxinCustomerService;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
		insertLaxinCustomer(laxinCustomer);

		assertNotNull(laxinCustomerService.find(String.valueOf(laxinCustomer.getId())));

	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
		insertLaxinCustomer(laxinCustomer);

		assertTrue(1 == laxinCustomerService.findPage(laxinCustomer).getTotalItems());
		assertTrue(1 == laxinCustomerService.findPage(laxinCustomer).getResult().size());
	}
}
