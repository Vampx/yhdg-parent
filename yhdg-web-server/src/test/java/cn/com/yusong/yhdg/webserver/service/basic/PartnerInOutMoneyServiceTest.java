package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PartnerInOutMoneyServiceTest extends BaseJunit4Test {
	@Autowired
	PartnerInOutMoneyService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		PartnerInOutMoney partnerInOutMoney = newPartnerInOutMoney(partner.getId());
		insertPartnerInOutMoney(partnerInOutMoney);

		assertNotNull(service.find(partnerInOutMoney.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		PartnerInOutMoney partnerInOutMoney = newPartnerInOutMoney(partner.getId());
		insertPartnerInOutMoney(partnerInOutMoney);

		assertTrue(1 == service.findPage(partnerInOutMoney).getTotalItems());
		assertTrue(1 == service.findPage(partnerInOutMoney).getResult().size());
	}
}
