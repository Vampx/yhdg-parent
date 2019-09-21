package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutCash;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PartnerInOutCashServiceTest extends BaseJunit4Test {
	@Autowired
	PartnerInOutCashService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		PartnerInOutCash partnerInOutCash = newPartnerInOutCash(partner.getId());
		insertPartnerInOutCash(partnerInOutCash);

		assertTrue(1 == service.findPage(partnerInOutCash).getTotalItems());
		assertTrue(1 == service.findPage(partnerInOutCash).getResult().size());
	}
}
