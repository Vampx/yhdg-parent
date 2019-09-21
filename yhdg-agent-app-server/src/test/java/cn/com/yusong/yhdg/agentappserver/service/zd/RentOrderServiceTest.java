package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RentOrderServiceTest extends BaseJunit4Test {
    @Autowired
    private RentOrderService service;

    @Test
    public void findListByShop() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        RentOrder rentOrder = newRentOrder(orderId, partner.getId(), agent.getId(), shop.getId(), customer.getId(), battery.getId(), systemBatteryType.getId());
        insertRentOrder(rentOrder);

        List<RentOrder> list = service.findListByShop(agent.getId(), shop.getId(), "", 0, 10);
        assertTrue(1 == list.size());
    }

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        RentOrder rentOrder = newRentOrder(orderId, partner.getId(), agent.getId(), shop.getId(), customer.getId(), battery.getId(), systemBatteryType.getId());
        insertRentOrder(rentOrder);

        assertNotNull(service.find(orderId));
    }

    @Test
    public void countShopTodayOrderNum() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        RentOrder rentOrder = newRentOrder(orderId, partner.getId(), agent.getId(), shop.getId(), customer.getId(), battery.getId(), systemBatteryType.getId());
        insertRentOrder(rentOrder);

        Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

        int num = service.countShopTodayOrderNum(shop.getId(), startTime, endTime);

        assertEquals(1, num);
    }
}