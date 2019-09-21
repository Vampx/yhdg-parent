package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerNoticeMessageServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerNoticeMessageService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerNoticeMessage customerNoticeMessage = newCustomerNoticeMessage(customer.getId());
        insertCustomerNoticeMessage(customerNoticeMessage);

        assertNotNull(service.find(customerNoticeMessage.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerNoticeMessage customerNoticeMessage = newCustomerNoticeMessage(customer.getId());
        insertCustomerNoticeMessage(customerNoticeMessage);

        assertTrue(1 == service.findPage(customerNoticeMessage).getTotalItems());
        assertTrue(1 == service.findPage(customerNoticeMessage).getResult().size());
    }


}
