package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrderRefund;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwPayOrderRefundServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwPayOrderRefundService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		AlipayfwPayOrderRefund alipayfwPayOrderRefund = newAlipayfwPayOrderRefund(partner.getId(), newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD));
		insertAlipayfwPayOrderRefund(alipayfwPayOrderRefund);

		assertTrue(1 == service.findPage(alipayfwPayOrderRefund).getTotalItems());
		assertTrue(1 == service.findPage(alipayfwPayOrderRefund).getResult().size());
	}
}
