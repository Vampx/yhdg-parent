package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.alipay.api.AlipayApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerForegiftOrderServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerForegiftOrderService service;

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertTrue(1 == service.findPage(entity).getTotalItems());
        assertTrue(1 == service.findPage(entity).getResult().size());

    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertNotNull(service.find(entity.getId()));
    }

    @Test
    public void repulseRefund() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        entity.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        entity.setMemo("退款");
        insertCustomerForegiftOrder(entity);

        assertTrue(service.repulseRefund(entity).isSuccess());
        assertTrue(service.find(entity.getId()).getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue());

    }

    @Test
    public void findCanRefundByCustomerId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        entity.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        insertCustomerForegiftOrder(entity);

        assertTrue(1 == service.findCanRefundByCustomerId(customer.getId()).size());

    }


}
