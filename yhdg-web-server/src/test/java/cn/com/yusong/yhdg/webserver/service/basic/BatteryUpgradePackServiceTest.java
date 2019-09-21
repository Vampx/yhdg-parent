package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class BatteryUpgradePackServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryUpgradePackService service;

	@Test
	public void find() {
		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		assertNotNull(service.find(batteryUpgradePack.getId()));
	}

	@Test
	public void insert() {
		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		assertTrue(service.insert(batteryUpgradePack).isSuccess());

		assertNotNull(service.find(batteryUpgradePack.getId()));
	}

	@Test
	public void update() throws IOException {
		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		batteryUpgradePack.setMemo("测试的备注");
		assertTrue(service.update(batteryUpgradePack).isSuccess());
		assertNotNull(service.find(batteryUpgradePack.getId()));
	}

	@Test
	public void delete() {
		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		assertTrue(service.delete(batteryUpgradePack.getId().longValue()).isSuccess());
		assertNull(service.find(batteryUpgradePack.getId()));
	}
}
