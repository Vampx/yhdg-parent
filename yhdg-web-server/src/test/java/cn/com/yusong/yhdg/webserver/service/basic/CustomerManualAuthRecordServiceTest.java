package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerManualAuthRecordServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerManualAuthRecordService service;

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerManualAuthRecord customerManualAuthRecord = newCustomerManualAuthRecord(partner.getId(), customer.getId());
        insertCustomerManualAuthRecord(customerManualAuthRecord);

        assertTrue(1 == service.findPage(customerManualAuthRecord).getTotalItems());
        assertTrue(1 == service.findPage(customerManualAuthRecord).getResult().size());

    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerManualAuthRecord customerManualAuthRecord = newCustomerManualAuthRecord(partner.getId(), agent.getId());
        insertCustomerManualAuthRecord(customerManualAuthRecord);

        assertNotNull(service.find(customerManualAuthRecord.getId()));
    }


    @Test
    public void audit() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerManualAuthRecord customerManualAuthRecord = newCustomerManualAuthRecord (partner.getId(), agent.getId());
        insertCustomerManualAuthRecord(customerManualAuthRecord);

        assertTrue(service.audit(customerManualAuthRecord.getId(),customerManualAuthRecord.getStatus(),customerManualAuthRecord.getAuditMemo(),customerManualAuthRecord.getAuditUser()).isSuccess());
    }
}
