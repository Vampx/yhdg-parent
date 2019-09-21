package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AlipayPayOrderRefundServiceTest extends BaseJunit4Test {
    @Autowired
    private AlipayPayOrderRefundService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);

        AlipayPayOrderRefund alipayPayOrderRefund = newAlipayPayOrderRefund(partner.getId(), order.getId());
        insertAlipayPayOrderRefund(alipayPayOrderRefund);

        assertTrue(1 == service.findPage(new AlipayPayOrderRefund()).getTotalItems());
        assertTrue(1 == service.findPage(new AlipayPayOrderRefund()).getResult().size());
    }
}