package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinSettingServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinSettingService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
		insertLaxinSetting(laxinSetting);

		assertNotNull(service.find(laxinSetting.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
		insertLaxinSetting(laxinSetting);

		assertTrue(1 == service.findPage(laxinSetting).getTotalItems());
		assertTrue(1 == service.findPage(laxinSetting).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinSetting laxinSetting = newLaxinSetting(agent.getId());

		assertTrue(service.create(laxinSetting).isSuccess());
		assertNotNull(service.find(laxinSetting.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
		laxinSetting.setType(LaxinSetting.Type.NORMAL.getValue());
		laxinSetting.setIncomeType(LaxinSetting.IncomeType.MONTH.getValue());
		insertLaxinSetting(laxinSetting);

		laxinSetting.setLaxinMoney(1533);
		assertTrue(service.update(laxinSetting).isSuccess());
		assertEquals(service.find(laxinSetting.getId()).getLaxinMoney(), laxinSetting.getLaxinMoney());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
		insertLaxinSetting(laxinSetting);

		assertTrue(service.delete(laxinSetting.getId()).isSuccess());
		assertNull(service.find(laxinSetting.getId()));
	}
}
