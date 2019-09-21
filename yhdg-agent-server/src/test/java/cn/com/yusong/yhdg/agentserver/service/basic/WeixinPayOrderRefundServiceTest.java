package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrderRefund;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class WeixinPayOrderRefundServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinPayOrderRefundService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        WeixinPayOrderRefund weixinPayOrderRefund = newWeixinPayOrderRefund(partner.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        insertWeixinmpPayOrderRefund(weixinPayOrderRefund);

        assertTrue(1 == service.findPage(weixinPayOrderRefund).getTotalItems());
        assertTrue(1 == service.findPage(weixinPayOrderRefund).getResult().size());
    }
}