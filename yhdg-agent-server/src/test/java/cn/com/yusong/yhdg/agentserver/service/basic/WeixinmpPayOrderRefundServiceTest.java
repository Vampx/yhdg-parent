package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class WeixinmpPayOrderRefundServiceTest extends BaseJunit4Test {
    @Autowired
    private WeixinmpPayOrderRefundService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        String orderId = newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER);
        weixinmpPayOrder.setId(orderId);

        WeixinmpPayOrderRefund weixinmpPayOrderRefund = newWeixinmpPayOrderRefund(partner.getId(), orderId);
        insertWeixinmpPayOrderRefund(weixinmpPayOrderRefund);

        assertTrue(1 == service.findPage(weixinmpPayOrderRefund).getTotalItems());
        assertTrue(1 == service.findPage(weixinmpPayOrderRefund).getResult().size());
    }
}