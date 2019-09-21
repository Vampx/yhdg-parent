package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinPayOrderService weixinPayOrderService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(), customer.getId(), newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));


        assertEquals(1, weixinPayOrderService.insert(weixinPayOrder));
    }
}
