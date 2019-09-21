package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityCustomerServiceTest extends BaseJunit4Test {

    @Autowired
    ActivityCustomerService service;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        ActivityCustomer activityCustomer = newActivityCustomer(1, customer.getId());
        insertActivityCustomer(activityCustomer);

        assertTrue(1 == service.findPage(activityCustomer).getTotalItems());
        assertTrue(1 == service.findPage(activityCustomer).getResult().size());

    }


}

