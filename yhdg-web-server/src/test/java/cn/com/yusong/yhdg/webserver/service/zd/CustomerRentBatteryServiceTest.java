package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerRentBatteryServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerRentBatteryService service;

	@Test
	public void findByCustomerId() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Role role = newRole(agent.getId());
		insertRole(role);

		Dept dept = newDept(agent.getId());
		insertDept(dept);

		User user = newUser(agent.getId(), role.getId(), dept.getId());
		insertUser(user);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
		insertAgentSystemConfig(agentSystemConfig);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
		cabinet.setShopId(shop.getId());
		insertCabinet(cabinet);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		battery.setCategory(Battery.Category.EXCHANGE.getValue());
		battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
		battery.setCustomerId(customer.getId());
		battery.setCabinetId(cabinet.getId());
		insertBattery(battery);

		CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery.getId(), battery.getType());
		insertCustomerRentBattery(CustomerRentBattery);

		assertTrue(1 == service.findByCustomerId(customer.getId()).size());
	}
}
