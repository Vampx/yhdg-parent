package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetIncomeTemplateServiceTest extends BaseJunit4Test {
	@Autowired
	CabinetIncomeTemplateService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CabinetIncomeTemplate cabinetIncomeTemplate = newCabinetIncomeTemplate(agent.getId());
		insertCabinetIncomeTemplate(cabinetIncomeTemplate);

		assertTrue(1 == service.findPage(cabinetIncomeTemplate).getTotalItems());
		assertTrue(1 == service.findPage(cabinetIncomeTemplate).getResult().size());
	}

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CabinetIncomeTemplate cabinetIncomeTemplate = newCabinetIncomeTemplate(agent.getId());
		insertCabinetIncomeTemplate(cabinetIncomeTemplate);

		assertNotNull(service.find(cabinetIncomeTemplate.getId()));
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CabinetIncomeTemplate cabinetIncomeTemplate = newCabinetIncomeTemplate(agent.getId());

		assertTrue(service.create(cabinetIncomeTemplate).isSuccess());
		assertTrue(1 == service.findPage(cabinetIncomeTemplate).getTotalItems());
		assertTrue(1 == service.findPage(cabinetIncomeTemplate).getResult().size());
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CabinetIncomeTemplate cabinetIncomeTemplate = newCabinetIncomeTemplate(agent.getId());
		insertCabinetIncomeTemplate(cabinetIncomeTemplate);

		cabinetIncomeTemplate.setForegiftMoney(1234);
		assertTrue(service.update(cabinetIncomeTemplate).isSuccess());
		assertEquals(cabinetIncomeTemplate.getForegiftMoney(), service.find(cabinetIncomeTemplate.getId()).getForegiftMoney());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		CabinetIncomeTemplate cabinetIncomeTemplate = newCabinetIncomeTemplate(agent.getId());
		insertCabinetIncomeTemplate(cabinetIncomeTemplate);

		assertTrue(service.delete(cabinetIncomeTemplate.getId()).isSuccess());
		assertNull(service.find(cabinetIncomeTemplate.getId()));
	}
}
