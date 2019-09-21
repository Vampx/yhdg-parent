package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryInstallRecordServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryInstallRecordService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
		batteryInstallRecord.setBatteryId(battery.getId());

		insertBatteryInstallRecord(batteryInstallRecord);

		assertNotNull(service.find(batteryInstallRecord.getId()));
	}

	@Test
	public void findByBatteryType() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
		batteryInstallRecord.setBatteryId(battery.getId());
		insertBatteryInstallRecord(batteryInstallRecord);

		assertNotNull(service.findByBatteryType(batteryInstallRecord.getBatteryType(), batteryInstallRecord.getAgentId()));
	}

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

		BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
		batteryInstallRecord.setBatteryId(battery.getId());
		insertBatteryInstallRecord(batteryInstallRecord);

		assertTrue(1 == service.findPage(batteryInstallRecord).getTotalItems());
		assertTrue(1 == service.findPage(batteryInstallRecord).getResult().size());
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
		batteryInstallRecord.setBatteryId(battery.getId());
		insertBatteryInstallRecord(batteryInstallRecord);

		assertTrue(1 == service.update(batteryInstallRecord.getId(), BatteryInstallRecord.Status.NOTONLINE.getValue()));
		assertTrue(BatteryInstallRecord.Status.NOTONLINE.getValue() == service.find(batteryInstallRecord.getId()).getStatus());
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryInstallRecord batteryInstallRecord = newBatteryInstallRecord(battery.getId(), agent.getId());
		batteryInstallRecord.setBatteryId(battery.getId());
		insertBatteryInstallRecord(batteryInstallRecord);

		assertTrue(1 == service.insert(batteryInstallRecord));
		assertNotNull(service.find(batteryInstallRecord.getId()));
	}
}
