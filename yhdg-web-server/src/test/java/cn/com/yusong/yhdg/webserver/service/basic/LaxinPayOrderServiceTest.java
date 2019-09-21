package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinPayOrderServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinPayOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinPayOrder laxinPayOrder = newLaxinPayOrder(agent.getId());
		insertLaxinPayOrder(laxinPayOrder);

		assertNotNull(service.find(laxinPayOrder.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinPayOrder laxinPayOrder = newLaxinPayOrder(agent.getId());
		insertLaxinPayOrder(laxinPayOrder);

		assertTrue(1 == service.findPage(laxinPayOrder).getTotalItems());
		assertTrue(1 == service.findPage(laxinPayOrder).getResult().size());
	}
}
