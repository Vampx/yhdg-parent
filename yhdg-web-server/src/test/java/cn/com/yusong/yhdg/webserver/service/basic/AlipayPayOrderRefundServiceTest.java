package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrderRefund;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayPayOrderRefundServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayPayOrderRefundService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		AlipayPayOrderRefund alipayPayOrderRefund = newAlipayPayOrderRefund(partner.getId(), "asdf123");
		insertAlipayfwPayOrderRefund(alipayPayOrderRefund);

		assertTrue(1 == service.findPage(alipayPayOrderRefund).getTotalItems());
		assertTrue(1 == service.findPage(alipayPayOrderRefund).getResult().size());
	}
}
