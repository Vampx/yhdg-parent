package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinPayOrderServiceTest extends BaseJunit4Test {

    @Autowired
    WeixinPayOrderService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        insertWeixinPayOrder(weixinPayOrder);

        assertNotNull(service.find(weixinPayOrder));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        insertWeixinPayOrder(weixinPayOrder);

        assertTrue(1 == service.findPage(weixinPayOrder).getResult().size());
    }

    @Test
    public void findBySourceId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        insertWeixinPayOrder(weixinPayOrder);

        assertNotNull(service.findBySourceId(weixinPayOrder.getSourceId()));
    }

}
