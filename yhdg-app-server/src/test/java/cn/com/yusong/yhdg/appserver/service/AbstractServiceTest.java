package cn.com.yusong.yhdg.appserver.service;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class AbstractServiceTest extends BaseJunit4Test {

    @Autowired
    AbstractService abstractService;

    @Test
    public void handleLaxinCustomerByMonth() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxin.setPacketPeriodMoney(100);
        laxin.setPacketPeriodMonth(10);
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(new Date());
        laxinCustomer.setPacketPeriodMoney(100);
        laxinCustomer.setPacketPeriodMonth(10);
        laxinCustomer.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxinCustomer.setPacketPeriodExpireTime(DateUtils.addDays(new Date(), 1));
        insertLaxinCustomer(laxinCustomer);

        abstractService.handleLaxinCustomerByMonth(agent, customer, 1);

        assertEquals(100, jdbcTemplate.queryForInt("select laxin_money from bas_laxin_record where laxin_id = ? and target_customer_id = ?", laxin.getId(), customer.getId()));
    }
}
