package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryUpgradePackDetailServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryUpgradePackDetailService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		UpgradePack upgradePack = newUpgradePack();
		insertUpgradePack(upgradePack);

		BatteryUpgradePackDetail batteryUpgradePackDetail = newBatteryUpgradePackDetail(batteryUpgradePack.getId(), battery.getId());
		insertBatteryUpgradePackDetail(batteryUpgradePackDetail);

		assertTrue(1 == service.findPage(batteryUpgradePackDetail).getTotalItems());
		assertTrue(1 == service.findPage(batteryUpgradePackDetail).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		UpgradePack upgradePack = newUpgradePack();
		insertUpgradePack(upgradePack);

		BatteryUpgradePackDetail batteryUpgradePackDetail = newBatteryUpgradePackDetail(batteryUpgradePack.getId(), battery.getId());

		String[] batteryIds = {battery.getId()};
		assertTrue(service.create(batteryUpgradePack.getId(), batteryIds).isSuccess());
		assertTrue(1 == service.findPage(batteryUpgradePackDetail).getTotalItems());
		assertTrue(1 == service.findPage(batteryUpgradePackDetail).getResult().size());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
		insertBatteryUpgradePack(batteryUpgradePack);

		UpgradePack upgradePack = newUpgradePack();
		insertUpgradePack(upgradePack);

		BatteryUpgradePackDetail batteryUpgradePackDetail = newBatteryUpgradePackDetail(batteryUpgradePack.getId(), battery.getId());
		insertBatteryUpgradePackDetail(batteryUpgradePackDetail);

		assertTrue(service.delete(batteryUpgradePack.getId(), battery.getId()).isSuccess());
		assertTrue(0 == service.findPage(batteryUpgradePackDetail).getTotalItems());
		assertTrue(0 == service.findPage(batteryUpgradePackDetail).getResult().size());
	}
}
