package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.LaxinCustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinCustomerServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinCustomerService laxinCustomerService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("1");
        insertCustomer(customer);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile("1");
        insertLaxinCustomer(laxinCustomer);

        laxinCustomerService.insert(laxin.getId(), "1");
        laxinCustomerService.insert(laxin.getId(), "2");

        assertEquals(2, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));
    }
}
