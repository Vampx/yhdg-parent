package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RentPeriodOrderServiceTest extends BaseJunit4Test {
    @Autowired
    private RentPeriodOrderService service;

    @Test
    public void findListByShop() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        assertTrue(service.findListByShop(shop.getId(), "", 0, 100).size() > 0);
    }

    @Test
    public void findLastEndTime() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder);

        assertNotNull(service.findLastEndTime(customer.getId()));
    }

    @Test
    public void findCountByShopAndStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPeriodOrder rentPeriodOrder1 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder1.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder1.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder1);

        RentPeriodOrder rentPeriodOrder2 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder2.setStatus(RentPeriodOrder.Status.EXPIRED.getValue());
        rentPeriodOrder2.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder2);

        RentPeriodOrder rentPeriodOrder3 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder3.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder3.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder3);

        List<Integer> statusList = Arrays.asList(RentPeriodOrder.Status.NOT_USE.getValue(),
                RentPeriodOrder.Status.USED.getValue(),
                RentPeriodOrder.Status.EXPIRED.getValue(),
                RentPeriodOrder.Status.APPLY_REFUND.getValue(),
                RentPeriodOrder.Status.REFUND.getValue());

        int orderCount = service.findCountByShopAndStatus(shop.getId(), statusList);
        assertTrue(orderCount == 2);
    }

    @Test
    public void countShopTodayOrderMoney() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentPeriodOrder rentPeriodOrder1 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder1.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder1.setShopId(shop.getId());
        rentPeriodOrder1.setMoney(10);
        insertRentPeriodOrder(rentPeriodOrder1);

        Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

        int money = service.countShopTodayOrderMoney(shop.getId(), startTime, endTime);

        assertEquals(10, money);
    }
}