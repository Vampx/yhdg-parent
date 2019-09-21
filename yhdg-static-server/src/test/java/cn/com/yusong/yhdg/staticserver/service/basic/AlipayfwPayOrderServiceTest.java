package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AlipayfwPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayfwPayOrderService alipayfwPayOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
        insertAlipayfwPayOrder(alipayfwPayOrder);

        assertNotNull(alipayfwPayOrderService.find(alipayfwPayOrder.getId()));
    }

    @Test
    public void payOk() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
        alipayfwPayOrder.setOrderStatus(AlipayfwPayOrder.Status.INIT.getValue());
        insertAlipayfwPayOrder(alipayfwPayOrder);

        assertEquals(1, alipayfwPayOrderService.payOk(alipayfwPayOrder));
    }
}