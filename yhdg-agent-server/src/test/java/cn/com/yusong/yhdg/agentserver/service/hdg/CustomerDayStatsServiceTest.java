package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.CustomerDayStats;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CustomerDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerDayStatsService customerDayStatsService;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer entity = newCustomer(partner.getId());
        insertCustomer(entity);

        CustomerDayStats customerDayStats = newCustomerDayStats(entity.getId(), "2017-09-07");
        customerDayStats.setUpdateTime(new Date());
        insertCustomerDayStats(customerDayStats);

        assertTrue(1 == customerDayStatsService.findPage(customerDayStats).getTotalItems());
        assertTrue(1 == customerDayStatsService.findPage(customerDayStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer entity = newCustomer(partner.getId());
        insertCustomer(entity);

        CustomerDayStats customerDayStats = newCustomerDayStats(entity.getId(), "2017-09-07");
        customerDayStats.setUpdateTime(new Date());
        insertCustomerDayStats(customerDayStats);

        assertTrue(1 == customerDayStatsService.findForExcel(customerDayStats).size());
    }
}
