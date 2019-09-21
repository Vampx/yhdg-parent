package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PacketPeriodOrderServiceTest extends BaseJunit4Test {
    @Autowired
    PacketPeriodOrderService service;

    @Test
    public void findCountByShopAndStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setShopId(shop.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder1);

        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setShopId(shop.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        insertPacketPeriodOrder(packetPeriodOrder2);

        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setShopId(shop.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.NOT_AVAIL.getValue());
        insertPacketPeriodOrder(packetPeriodOrder3);

        List<Integer> statusList = Arrays.asList(PacketPeriodOrder.Status.NOT_USE.getValue(),
                PacketPeriodOrder.Status.USED.getValue(),
                PacketPeriodOrder.Status.EXPIRED.getValue(),
                PacketPeriodOrder.Status.APPLY_REFUND.getValue(),
                PacketPeriodOrder.Status.REFUND.getValue());

        int orderCount = service.findCountByShopAndStatus(shop.getId(), statusList);
        assertTrue(orderCount == 2);
    }

    @Test
    public void countShopTodayOrderMoney() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setShopId(shop.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        packetPeriodOrder1.setMoney(10);
        insertPacketPeriodOrder(packetPeriodOrder1);

        Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

        int money = service.countShopTodayOrderMoney(shop.getId(), startTime, endTime);

        assertEquals(10, money);
    }
}
