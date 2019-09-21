package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class CustomerDepositOrderServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerDepositOrderService customerDepositOrderService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        CustomerDepositOrder customerDepositOrder = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(customerDepositOrder);
        assertNotNull(customerDepositOrderService.findList(customer.getId(), 0, 1));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder order = newCustomerDepositOrder(partner.getId(), customer.getId());
        customerDepositOrderService.insert(order);
    }

    @Test
    public void payByAlipay() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder order = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(order);
        assertNotNull(customerDepositOrderService.payByAlipay(order.getMoney(), customer.getId(), true));
    }

    @Test
    public void payByAlipayfw() throws IOException {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder order = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(order);
        assertNotNull(customerDepositOrderService.payByAlipayfw(order.getMoney(), customer.getId(), true));
    }

    @Test
    public void payByWeixin() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder order = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(order);
        assertNotNull(customerDepositOrderService.payByWeixin(partner.getId(), order.getId(), order.getMoney(), customer.getId()));
    }

    @Test
    public void payByWeixinMp() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder order = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(order);
        assertNotNull(customerDepositOrderService.payByWeixinMp(true, partner.getId(), order.getId(), order.getMoney(), customer.getId(), null));
    }
}
