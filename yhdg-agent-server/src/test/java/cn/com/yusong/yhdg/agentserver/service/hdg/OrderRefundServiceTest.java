package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderRefundServiceTest extends BaseJunit4Test {
    @Autowired
    OrderRefundService service;

    @Test
    public void findPageForBalance() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        OrderRefund orderRefund = newOrderRefund(agent.getId(), customer.getId().intValue());

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        PacketPeriodOrderRefund packetPeriodOrderRefund = newPacketPeriodOrderRefund("asdf", customer.getId());
        insertPacketPeriodOrderRefund(packetPeriodOrderRefund);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder("asdf", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId()
				, customer.getId());
        insertBatteryOrder(batteryOrder);

        BatteryOrderRefund batteryOrderRefund = newBatteryOrderRefund("asdf", customer.getId(), agent.getId());
        batteryOrderRefund.setId(batteryOrder.getId());
        insertBatteryOrderRefund(batteryOrderRefund);

        assertEquals(1, service.findPageForBalance(orderRefund).getTotalItems());
        assertEquals(1, service.findPageForBalance(orderRefund).getResult().size());
    }
}
