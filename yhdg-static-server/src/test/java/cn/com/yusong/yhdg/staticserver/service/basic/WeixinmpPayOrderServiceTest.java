package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class WeixinmpPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinmpPayOrderService weixinmpPayOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        insertWeixinmpPayOrder(weixinmpPayOrder);

        assertNotNull(weixinmpPayOrderService.find(weixinmpPayOrder.getId()));
    }

    @Test
    public void payOk() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(), customer.getId());
        weixinmpPayOrder.setOrderStatus(WeixinmpPayOrder.Status.INIT.getValue());
        insertWeixinmpPayOrder(weixinmpPayOrder);

        assertEquals(1, weixinmpPayOrderService.payOk(weixinmpPayOrder));
    }
}