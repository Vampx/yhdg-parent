package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		assertNotNull(service.find(laxin.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		assertTrue(1 == service.findPage(laxin).getTotalItems());
		assertTrue(1 == service.findPage(laxin).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());

		assertTrue(service.create(laxin).isSuccess());
		assertNotNull(service.find(laxin.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		laxin.setLaxinMoney(1245);
		assertTrue(service.update(laxin).isSuccess());
		assertEquals(laxin.getLaxinMoney(), service.find(laxin.getId()).getLaxinMoney());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		assertTrue(service.delete(laxin.getId()).isSuccess());
		assertNull(service.find(laxin.getId()));
	}
}
