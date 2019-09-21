package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerService customerService;

    @Test
    public void findByMpOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        assertNotNull(customerService.findByMpOpenId(partner.getId(),"123"));
    }

    @Test
    public void findByFwOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setFwOpenId("123");
        insertCustomer(customer);

        assertNotNull(customerService.findByFwOpenId(partner.getId(),"123"));
    }

    @Test
    public void updateAuthFacePath() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateAuthFacePath(customer.getId(), "111"));
    }

    @Test
    public void updateCertification() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertEquals(1, customerService.updateCertification(customer.getId(), "11", "111", Customer.AuthStatus.AUDIT_PASS.getValue()));
    }
}
