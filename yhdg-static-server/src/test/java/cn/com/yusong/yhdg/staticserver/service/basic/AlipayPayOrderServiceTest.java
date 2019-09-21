package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayPayOrderService alipayPayOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);

        assertNotNull(alipayPayOrderService.find(order.getId()));
    }

    @Test
    public void payOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);

        assertEquals(1, alipayPayOrderService.payOk(order));
    }

}
