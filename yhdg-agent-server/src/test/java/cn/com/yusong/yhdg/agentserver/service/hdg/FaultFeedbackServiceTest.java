package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FaultFeedbackServiceTest extends BaseJunit4Test {

    @Autowired
    FaultFeedbackService faultFeedbackService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        FaultFeedback faultFeedback = newFaultFeedback(agent.getId(), customer.getId());
        insertFaultFeedback(faultFeedback);

        assertNotNull(faultFeedbackService.find(faultFeedback.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        FaultFeedback faultFeedback = newFaultFeedback(agent.getId(), customer.getId());
        insertFaultFeedback(faultFeedback);

        assertTrue(1 == faultFeedbackService.findPage(faultFeedback).getTotalItems());
        assertTrue(1 == faultFeedbackService.findPage(faultFeedback).getResult().size());

    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        FaultFeedback faultFeedback = newFaultFeedback(agent.getId(), customer.getId());
        insertFaultFeedback(faultFeedback);

        assertTrue(faultFeedbackService.update(faultFeedback).isSuccess());

    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        FaultFeedback faultFeedback = newFaultFeedback(agent.getId(), customer.getId());
        insertFaultFeedback(faultFeedback);

        assertTrue(faultFeedbackService.delete(faultFeedback.getId()).isSuccess());

    }

    @Test
    public void findFaultFeedbackCount() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        FaultFeedback faultFeedback = newFaultFeedback(agent.getId(), customer.getId());
        faultFeedback.setHandleStatus(FaultFeedback.HandleStatus.UNHANDLED.getValue());
        insertFaultFeedback(faultFeedback);

        assertTrue(1 == faultFeedbackService.findFaultFeedbackCount(faultFeedback.getFaultType()));
    }

}

