package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerService customerService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertNotNull(customerService.find(customer.getId()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        assertNotNull(customerService.findList(agent.getId(), "admin", 1, 10));
    }

    @Test
    public void findListOrderByForegift() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        assertNotNull(customerService.findListOrderByForegift(agent.getId(), "admin", 1, 10));
    }

    @Test
    public void findAgentCompanyCustomer() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        insertAgentCompany(agentCompany);

        assertNotNull(customerService.findAgentCompanyCustomer(agent.getId(), agentCompany.getId(), null,1, 10));
    }


    @Test
    public void findCustomerCount() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        insertAgentCompany(agentCompany);

        assertEquals(1, customerService.findCustomerCount(agent.getId()));
    }

    @Test
    public void findHdCustomerCountByStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setHdForegiftStatus(Customer.HdForegiftStatus.PAID.getValue());
        insertCustomer(customer);

        assertEquals(1, customerService.findHdCustomerCountByStatus(agent.getId(), Customer.HdForegiftStatus.PAID.getValue(), null, null));
    }


    @Test
    public void findZdCustomerCountByStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setZdForegiftStatus(Customer.ZdForegiftStatus.PAID.getValue());
        insertCustomer(customer);

        assertEquals(1, customerService.findZdCustomerCountByStatus(agent.getId(), Customer.HdForegiftStatus.PAID.getValue(), null, null));
    }

    @Test
    public void findMobileList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setZdForegiftStatus(Customer.ZdForegiftStatus.PAID.getValue());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        insertAgentCompany(agentCompany);

        assertEquals(0, customerService.findMobileList(agent.getId(),agent.getPartnerId(),customer.getMobile(), 0, 10).getCode());
    }

    @Test
    public void findMobileInfo() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setZdForegiftStatus(Customer.ZdForegiftStatus.PAID.getValue());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        insertAgentCompany(agentCompany);

        assertEquals(0, customerService.findMobileInfo(agent.getPartnerId(),customer.getMobile()).getCode());
    }
}
