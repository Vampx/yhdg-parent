package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerWhitelist;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerWhitelistServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerWhitelistService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CustomerWhitelist customerWhitelist = newCustomerWhitelist(partner.getId(), agent.getId());
		insertCustomerWhitelist(customerWhitelist);

		assertNotNull(service.find(customerWhitelist.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CustomerWhitelist customerWhitelist = newCustomerWhitelist(partner.getId(), agent.getId());
		insertCustomerWhitelist(customerWhitelist);

		assertTrue(1 == service.findPage(customerWhitelist).getTotalItems());
		assertTrue(1 == service.findPage(customerWhitelist).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CustomerWhitelist customerWhitelist = newCustomerWhitelist(partner.getId(), agent.getId());

		assertTrue(service.create(customerWhitelist).isSuccess());
		assertNotNull(service.find(customerWhitelist.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CustomerWhitelist customerWhitelist = newCustomerWhitelist(partner.getId(), agent.getId());
		insertCustomerWhitelist(customerWhitelist);

		customerWhitelist.setMemo("测试的memo");
		assertTrue(service.update(customerWhitelist).isSuccess());
		assertEquals(service.find(customerWhitelist.getId()).getMemo(), customerWhitelist.getMemo());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CustomerWhitelist customerWhitelist = newCustomerWhitelist(partner.getId(), agent.getId());
		insertCustomerWhitelist(customerWhitelist);

		assertTrue(service.delete(customerWhitelist.getId()).isSuccess());
		assertNull(service.find(customerWhitelist.getId()));
	}
}
