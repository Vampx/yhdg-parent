package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerCouponTicketServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerCouponTicketService service;

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), "111");
        insertCustomerCouponTicket(customerCouponTicket);

        assertTrue(1 == service.findPage(customerCouponTicket).getTotalItems());
        assertTrue(1 == service.findPage(customerCouponTicket).getResult().size());

    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), "111");
        insertCustomerCouponTicket(customerCouponTicket);

        assertNotNull(service.find(customerCouponTicket.getId()));
    }

    @Test
    public void create() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setId(1L);
        customer.setMobile("1234");
        insertCustomer(customer);
        customer.setId(2L);
        customer.setMobile("2345");
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), "111");
        customerCouponTicket.setIds("1,2");

        assertTrue(service.create(customerCouponTicket).isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), "111");
        insertCustomerCouponTicket(customerCouponTicket);

        assertTrue(service.delete(customerCouponTicket.getId()).isSuccess());
        assertNull(service.find(customerCouponTicket.getId()));

    }


}
