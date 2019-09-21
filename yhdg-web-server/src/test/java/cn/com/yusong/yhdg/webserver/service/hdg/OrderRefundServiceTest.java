package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OrderRefundServiceTest extends BaseJunit4Test {
	final String suffix = DateFormatUtils.format(new Date(), "yyyyww");

	@Autowired
	OrderRefundService service;

	@Test
	public void findPageForBalance() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
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

		BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
		insertBatteryOrder(batteryOrder);

		BatteryOrderRefund batteryOrderRefund = newBatteryOrderRefund("asdf", customer.getId(), agent.getId());
		batteryOrderRefund.setId(batteryOrder.getId());
		insertBatteryOrderRefund(batteryOrderRefund);

		assertEquals(1 , service.findPageForBalance(orderRefund).getTotalItems());
		assertEquals(1 , service.findPageForBalance(orderRefund).getResult().size());
	}
}
