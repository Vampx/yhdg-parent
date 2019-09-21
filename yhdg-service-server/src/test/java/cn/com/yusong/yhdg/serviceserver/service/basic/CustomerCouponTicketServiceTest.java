package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CustomerCouponTicketServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;

    @Test
    public void expire() {
        customerCouponTicketService.expire();
    }

    @Test
    public void wagesDay1() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
        String wagesDay = statsDate.substring(statsDate.lastIndexOf("-") + 1, statsDate.length());

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMobile("13777351251");
        customer.setMpOpenId("111");
        customer.setBalance(0);
        customer.setWagesDay(wagesDay);
        insertCustomer(customer);

        int type = CustomerCouponTicketGift.Type.WAGES_GIVE.getValue();
        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.EXCHANGE.getValue());
        customerCouponTicketGift.setType(type);
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        customerCouponTicketService.wagesDay();

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));

    }

    @Test
    public void wagesDay2() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
        String wagesDay = statsDate.substring(statsDate.lastIndexOf("-") + 1, statsDate.length());

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setMobile("13777351251");
        customer.setMpOpenId("111");
        customer.setBalance(0);
        customer.setWagesDay(wagesDay);
        insertCustomer(customer);

        int type = CustomerCouponTicketGift.Type.WAGES_GIVE.getValue();
        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.RENT.getValue());
        customerCouponTicketGift.setType(type);
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        customerCouponTicketService.wagesDay();

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));

    }

    @Test
    public void wagesDay3() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
        String wagesDay = statsDate.substring(statsDate.lastIndexOf("-") + 1, statsDate.length());

        int type = CustomerCouponTicketGift.Type.WAGES_GIVE.getValue();
        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.RENT.getValue());
        customerCouponTicketGift.setType(type);
        customerCouponTicketGift.setWagesDay(Integer.parseInt(wagesDay));
        insertCustomerCouponTicketGift(customerCouponTicketGift);
        WagesDayTicketGift wagesDayTicketGift = newWagesDayTicketGift(customerCouponTicketGift);
        insertWagesDayTicketGift(wagesDayTicketGift);


        customerCouponTicketService.wagesDay();
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));
    }
}
