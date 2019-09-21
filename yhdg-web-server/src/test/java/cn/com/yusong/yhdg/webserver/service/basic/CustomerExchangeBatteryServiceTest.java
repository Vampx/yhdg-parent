package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerExchangeBatteryServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerExchangeBatteryService service;

	@Test
	public void findByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryOrder batteryOrder = newBatteryOrder(battery.getId(), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
		insertBatteryOrder(batteryOrder);

		CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
		insertCustomerExchangeBattery(customerExchangeBattery);

		assertTrue(1 == service.findByCustomerId(customer.getId()).size());
	}
}
