package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerNoticeMessageServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerNoticeMessageService customerNoticeMessageService;


    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerNoticeMessage customerNoticeMessage = newCustomerNoticeMessage(customer.getId());
        insertCustomerNoticeMessage(customerNoticeMessage);
        assertEquals(null, customerNoticeMessageService.find(CustomerNoticeMessage.Type.NOTICE.getValue(), customerNoticeMessage.getId()));
        assertNotNull(customerNoticeMessageService.find(CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue(), customerNoticeMessage.getId()));
    }

    @Test
    public void findListByCustomerId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerNoticeMessage customerNoticeMessage = newCustomerNoticeMessage(customer.getId());
        insertCustomerNoticeMessage(customerNoticeMessage);

        assertEquals(1, customerNoticeMessageService.findListByCustomerId(customer.getId(), CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue(), 0, 20).size());
        assertEquals(0, customerNoticeMessageService.findListByCustomerId(customer.getId(), CustomerNoticeMessage.Type.NOTICE.getValue(), 0, 20).size());
    }
}
