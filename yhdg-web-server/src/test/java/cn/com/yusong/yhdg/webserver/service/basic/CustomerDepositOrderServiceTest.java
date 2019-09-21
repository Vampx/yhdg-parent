package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerDepositOrderServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerDepositOrderService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder entity = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(entity);

        assertNotNull(service.find(entity.getId()));
    }

    @Test
    public void sumMoney() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder entity = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(entity);

        assertTrue(entity.getMoney() == service.sumMoney(entity.getStatus()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerDepositOrder entity = newCustomerDepositOrder(partner.getId(), customer.getId());
        insertCustomerDepositOrder(entity);

        assertTrue(1 == service.findPage(entity).getTotalItems());
        assertTrue(1 == service.findPage(entity).getResult().size());

    }

//    @Test
//    public void create() {
//        Partner partner = newPartner(); insertPartner(partner);
//        Customer customer = newCustomer(partner.getId());
//        insertCustomer(customer);
//
//        CustomerDepositOrder entity = newCustomerDepositOrder(partner.getId(), customer.getId());
//
//        assertTrue("delete fail", service.create(entity).isSuccess());
//        assertNotNull(service.find(entity.getId()));
//    }

//    @Test
//    public void confirmRefund() throws Exception{
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Customer customer = newCustomer(partner.getId());
//        customer.setBalance(1234);
//        insertCustomer(customer);
//
//
//
//        CustomerDepositOrder customerDepositOrder = newCustomerDepositOrder(partner.getId(), customer.getId());
//        customerDepositOrder.setStatus(CustomerDepositOrder.Status.OK.getValue());
//        customerDepositOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
//        insertCustomerDepositOrder(customerDepositOrder);
//
//        //用户退款
//        assertTrue(service.confirmRefund(customerDepositOrder.getId(), 1, "asdf", "asdf", "asdf").isSuccess());
//    }



}
