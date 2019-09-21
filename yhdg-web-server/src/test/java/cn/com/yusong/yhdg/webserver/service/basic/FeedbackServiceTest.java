package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FeedbackServiceTest extends BaseJunit4Test {

    @Autowired
    FeedbackService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Feedback feedback = newFeedback(partner.getId(), customer.getId());
        insertFeedback(feedback);

        assertNotNull(service.find(feedback.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Feedback feedback = newFeedback(partner.getId(), customer.getId());
        insertFeedback(feedback);

        assertTrue(1 == service.findPage(feedback).getTotalItems());
        assertTrue(1 == service.findPage(feedback).getResult().size());
    }

    @Test
    public void delete(){
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Feedback feedback = newFeedback(partner.getId(), customer.getId());
        insertFeedback(feedback);

        assertTrue(1 == service.delete(feedback.getId()));
    }


}
