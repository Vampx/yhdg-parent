package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class CustomerForegiftOrderServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;

    @Test
    public void countShopTodayOrderNum() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        customerForegiftOrder.setId("1234");
        customerForegiftOrder.setShopId(shop.getId());
        customerForegiftOrder.setPayTime(new Date());
        insertCustomerForegiftOrder(customerForegiftOrder);

        Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

        int num = customerForegiftOrderService.countShopTodayOrderNum(shop.getId(), startTime, endTime);
        assertEquals(1, num);
    }

}