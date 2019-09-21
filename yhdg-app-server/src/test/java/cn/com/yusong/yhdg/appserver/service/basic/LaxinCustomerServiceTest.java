package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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

        PartnerMpOpenId partnerMpOpenId = newPartnerMpOpenId(partner.getId(), null);
        partnerMpOpenId.setOpenId("333");
        insertPartnerMpOpenId(partnerMpOpenId);

        PartnerFwOpenId partnerFwOpenId = newPartnerFwOpenId(partner.getId(), null);
        partnerFwOpenId.setOpenId("333");
        insertPartnerFwOpenId(partnerFwOpenId);

        laxinCustomerService.insert(laxin.getId(), "1", "1:111:222", 1);
        laxinCustomerService.insert(laxin.getId(), "2", "1:333:444", 1);
        laxinCustomerService.insert(laxin.getId(), "3", "1:333:444", 2);

        assertEquals(3, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where partner_id = ? and mobile = ?", partner.getId(), "2"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where partner_id = ? and mobile = ?", partner.getId(), "3"));
    }

    @Test
    public void sumPayForegiftCount() {
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
        laxinCustomer.setForegiftTime(new Date());
        insertLaxinCustomer(laxinCustomer);

        assertEquals(1, laxinCustomerService.sumPayForegiftCount(laxin.getId()));
    }
}
