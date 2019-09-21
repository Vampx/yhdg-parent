package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/8.
 */
public class CustomerCouponTicketServiceTest extends BaseJunit4Test {


    @Autowired
    CustomerCouponTicketService customerCouponTicketService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);
        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);
        assertNotNull(customerCouponTicketService.find(customerCouponTicket.getId()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);
        List<CustomerCouponTicket> list = customerCouponTicketService.findList(customer.getAgentId(), customer.getMobile(), new Date(), 1, CustomerCouponTicket.Status.NOT_USER.getValue(),
                CustomerCouponTicket.Category.EXCHANGE.getValue(), null, null);
        assertNotNull(list);
    }

    @Test
    public void findCount() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setCustomerMobile(customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, customerCouponTicketService.findCount(partner.getId(), customer.getMobile(), customerCouponTicket.getStatus(), CustomerCouponTicket.Category.EXCHANGE.getValue()));
    }

    @Test
    public void findCount1() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        customerCouponTicket.setCustomerMobile(customer.getMobile());
        customerCouponTicket.setCategory(CustomerCouponTicket.Category.VEHICLE.getValue());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, customerCouponTicketService.findCount(partner.getId(), customer.getMobile(), customerCouponTicket.getStatus(), CustomerCouponTicket.Category.VEHICLE.getValue()));
    }

    @Test
    public void useTicket() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, customerCouponTicketService.useTicket(customerCouponTicket.getId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());

        assertEquals(1, customerCouponTicketService.insert(customerCouponTicket));
    }

}
