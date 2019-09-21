package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/11/18.
 */
public class WeixinPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinPayOrderService weixinPayOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER);
        WeixinPayOrder order = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertWeixinPayOrder(order);

        assertNotNull(weixinPayOrderService.find(order.getId()));
    }

    @Test
    public void payOk() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER);
        WeixinPayOrder order = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertWeixinPayOrder(order);

        assertEquals(1, weixinPayOrderService.payOk(order));
    }
}
