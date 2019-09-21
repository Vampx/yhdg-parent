package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerCouponTicketServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerCouponTicketService service;

    @Test
    public void find() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        assertNotNull(service.find(customerCouponTicket.getId()));

    }


    @Test
    public void findList() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, service.findList(agent.getId(), null, CustomerCouponTicket.Status.NOT_USER.getValue(), 1,0, 10).size());

    }

    @Test
    public void create() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());

        assertNotNull(service.create(customerCouponTicket));

    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        assertNotNull(service.delete(customerCouponTicket.getId()));

    }

}
