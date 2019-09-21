package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerRefundRecordServiceTest extends BaseJunit4Test {
    @Autowired
    private CustomerRefundRecordService service;

    private CustomerRefundRecord customerRefundRecord;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
    }

    @Test
    public void find() {
        insertCustomerRefundRecord(customerRefundRecord);

        assertNotNull(service.find(customerRefundRecord.getId()));
    }

    @Test
    public void findPage() {
        insertCustomerRefundRecord(customerRefundRecord);

        assertTrue(1 == service.findPage(customerRefundRecord).getTotalItems());
        assertTrue(1 == service.findPage(customerRefundRecord).getResult().size());
    }

    @Test
    public void findByCustomerId() {
        insertCustomerRefundRecord(customerRefundRecord);

        assertTrue(1 == service.findByCustomerId(customerRefundRecord.getCustomerId(), customerRefundRecord.getStatus()).size());
    }

    @Test
    public void newOrderId() {
        assertNotNull(service.newOrderId());
    }

    /*@Test
    public void updateStatus() {
        insertCustomerRefundRecord(customerRefundRecord);

        service.updateStatus(customerRefundRecord.getId(), CustomerRefundRecord.RefundType.BALANCE.getValue(), "asdf", customerRefundRecord.getStatus(), 1234, new Date());
        assertEquals(customerRefundRecord.getStatus().intValue(), service.find(customerRefundRecord.getId()).getStatus().intValue());
    }*/
}