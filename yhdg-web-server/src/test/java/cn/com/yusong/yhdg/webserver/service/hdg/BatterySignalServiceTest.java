package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatterySignal;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatterySignalServiceTest extends BaseJunit4Test {
	@Autowired
	BatterySignalService service;

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatterySignal batterySignal = newBatterySignal(battery.getId());
		insertBatterySignal(batterySignal);
		assertTrue(1 == service.findPage(batterySignal).getTotalItems());
		assertTrue(1 == service.findPage(batterySignal).getResult().size());

	}
}
