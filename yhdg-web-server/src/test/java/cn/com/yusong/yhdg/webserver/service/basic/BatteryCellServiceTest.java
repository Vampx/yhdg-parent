package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryCellService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.service.hdg.UnregisterBatteryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryCellServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryCellService service;
	@Autowired
	UnregisterBatteryService unregisterBatteryService;

	@Test
	public void find() {
		BatteryCell batteryCell = newBatteryCell();
		insertBatteryCell(batteryCell);

		assertNotNull(service.find(batteryCell.getId()));
	}

	@Test
	public void findByBarcode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCell batteryCell = newBatteryCell();
		batteryCell.setBarcode(batteryCellBarcode.getBarcode());
		insertBatteryCell(batteryCell);

		DataResult dataResult = (DataResult) service.findByBarcode(batteryCellBarcode.getBarcode());
		assertTrue(dataResult.isSuccess());
		BatteryCell dbBatteryCell = (BatteryCell) dataResult.getData();
		assertEquals(batteryCell.getBarcode(), dbBatteryCell.getBarcode());
	}

	@Test
	public void findPage() {
		BatteryCell batteryCell = newBatteryCell();
		insertBatteryCell(batteryCell);

		assertTrue(1 == service.findPage(batteryCell).getTotalItems());
		assertTrue(1 == service.findPage(batteryCell).getResult().size());
	}

	@Test
	public void findBoundPage() {

		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryCell batteryCell = newBatteryCell();
		batteryCell.setBatteryId(battery.getId());
		insertBatteryCell(batteryCell);

		assertTrue(1 == service.findBoundPage(batteryCell).getTotalItems());
		assertTrue(1 == service.findBoundPage(batteryCell).getResult().size());
	}

	@Test
	public void create() {
		BatteryCell batteryCell = newBatteryCell();

		assertTrue(service.create(batteryCell).isSuccess());
		assertNotNull(service.find(batteryCell.getId()));
	}

	@Test
	public void update() {
		BatteryCell batteryCell = newBatteryCell();
		insertBatteryCell(batteryCell);

		batteryCell.setResilienceVol(1245);
		assertTrue(service.update(batteryCell).isSuccess());
		assertEquals(batteryCell.getResilienceVol(), service.find(batteryCell.getId()).getResilienceVol());
	}

	@Test
	public void unbind() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		UnregisterBattery unregisterBattery = newUnregisterBattery();
		unregisterBattery.setCellCount(12);
		insertUnregisterBattery(unregisterBattery);

		BatteryCell batteryCell = newBatteryCell();
		batteryCell.setBatteryId(unregisterBattery.getId());
		insertBatteryCell(batteryCell);

		assertTrue(service.unbind(batteryCell.getId(), unregisterBattery.getId()).isSuccess());
		assertEquals(null, service.find(batteryCell.getId()).getBatteryId());
		Integer newCellCount = unregisterBatteryService.find(unregisterBattery.getId()).getCellCount();
		assertEquals(newCellCount.intValue(), unregisterBattery.getCellCount() - 1);
	}

	@Test
	public void bindBattery() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		batteryFormat.setCellCount(15);
		insertBatteryFormat(batteryFormat);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		UnregisterBattery unregisterBattery = newUnregisterBattery();
		unregisterBattery.setCellCount(12);
		insertUnregisterBattery(unregisterBattery);

		BatteryCell batteryCell = newBatteryCell();
		insertBatteryCell(batteryCell);

		assertTrue(service.bindBattery(batteryCell.getId(), unregisterBattery.getId(), batteryFormat.getId().intValue()).isSuccess());
		assertEquals(service.find(batteryCell.getId()).getBatteryId(), unregisterBattery.getId());
		assertEquals(unregisterBattery.getCellCount() + 1, unregisterBatteryService.find(unregisterBattery.getId()).getCellCount().intValue());
	}
}
