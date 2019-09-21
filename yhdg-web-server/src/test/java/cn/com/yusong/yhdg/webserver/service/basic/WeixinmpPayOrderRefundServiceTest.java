package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrderRefund;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinmpPayOrderRefundServiceTest extends BaseJunit4Test {
	@Autowired
	WeixinmpPayOrderRefundService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		WeixinmpPayOrderRefund weixinmpPayOrderRefund = newWeixinmpPayOrderRefund(partner.getId(),"asdf");
		insertWeixinmpPayOrderRefund(weixinmpPayOrderRefund);

		assertTrue(1 == service.findPage(weixinmpPayOrderRefund).getTotalItems());
		assertTrue(1 == service.findPage(weixinmpPayOrderRefund).getResult().size());
	}
}
