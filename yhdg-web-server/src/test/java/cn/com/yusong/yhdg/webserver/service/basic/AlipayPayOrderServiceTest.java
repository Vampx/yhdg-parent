package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chen on 2017/9/4.
 */
public class AlipayPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayPayOrderService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);

        assertNotNull(service.find(order));
    }


    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);

        assertTrue(1 == service.findPage(order).getTotalItems());
        assertTrue(1 == service.findPage(order).getResult().size());

    }

    @Test
    public void findBySourceId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);
        insertAlipayPayOrder(order);

        assertNotNull(service.findBySourceId(order.getSourceId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String orderId = newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        AlipayPayOrder order = newAlipayPayOrder(partner.getId(), agent.getId(), customer.getId(), orderId);

        assertTrue(1 == service.insert(order));

    }

}
