package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class BatteryBarcodeServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryBarcodeService service;

	@Test
	public void findMaxCode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setBarcode("1");
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode2.setBarcode("2");
		insertBatteryBarcode(batteryBarcode2);

		assertNotNull(service.findMaxCode(batteryFormat.getId()));
		assertEquals(service.findMaxCode(batteryFormat.getId()), batteryBarcode2.getBarcode());
	}

	@Test
	public void findMaxCodeBatteryBarcode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setBarcode("1");
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode2.setBarcode("2");
		insertBatteryBarcode(batteryBarcode2);

		assertNotNull(service.findMaxCodeBatteryBarcode(batteryFormat.getId()));
		assertEquals(service.findMaxCodeBatteryBarcode(batteryFormat.getId()).getBarcode(), batteryBarcode2.getBarcode());
	}

	@Test
	public void findList() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setBarcode("1");
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode2.setBarcode("2");
		insertBatteryBarcode(batteryBarcode2);

		assertTrue(2 == service.findList(batteryFormat.getId()).size());
	}

	@Test
	public void findByBarcode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setBarcode("1");
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode2.setBarcode("2");
		insertBatteryBarcode(batteryBarcode2);

		DataResult result = (DataResult) service.findByBarcode(batteryBarcode2.getBarcode(), "admin");
		BatteryBarcode batteryBarcode1 = (BatteryBarcode) result.getData();
		assertEquals(batteryBarcode2.getBarcode(), batteryBarcode1.getBarcode());
	}

	@Test
	public void findPage() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setBarcode("1");
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode2.setBarcode("2");
		insertBatteryBarcode(batteryBarcode2);

		batteryBarcode.setBarcode(null);
		assertTrue(2 == service.findPage(batteryBarcode).getResult().size());
		assertTrue(2 == service.findPage(batteryBarcode).getTotalItems());
	}

	@Test
	public void checkCodeCount() {
		String equipmentNo = "YYYYMMDDNNNN";
		int num = 1000;
		int codeCount = 1000;
		assertTrue(service.checkCodeCount(equipmentNo, num, codeCount).isSuccess());
	}

	@Test
	public void create() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		batteryCellRegular.setRegularType(BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());
		insertBatteryCellRegular(batteryCellRegular);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		batteryBarcode.setCodeCount(5);

		assertTrue(service.create(batteryBarcode).isSuccess());
		batteryBarcode.setBarcode(null);
		assertTrue(5 == service.findPage(batteryBarcode).getTotalItems());
		assertTrue(5 == service.findPage(batteryBarcode).getResult().size());
	}

	@Test
	public void getNewEquipmentNo() {
		String equipMentNo = "YYYYMMDDNNNN";
		int num = 1;
		int str = 1;
		Date now = new Date();
		String today = DateFormatUtils.format(now, "yyyyMMdd");
		assertEquals(today + "0002", service.getNewEquipmentNo(equipMentNo, num, str));
	}

	@Test
	public void delete() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		insertBatteryBarcode(batteryBarcode);

		assertTrue(service.delete(batteryBarcode.getId()).isSuccess());
		assertTrue(0 == service.findPage(batteryBarcode).getTotalItems());
		assertTrue(0 == service.findPage(batteryBarcode).getResult().size());
	}

	@Test
	public void batchDelete() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
		insertBatteryBarcode(batteryBarcode);

		BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
		insertBatteryBarcode(batteryBarcode2);

		long[] idList = {batteryBarcode.getId(), batteryBarcode2.getId()};
		ExtResult result = service.batchDelete(idList);
		assertTrue(result.isSuccess());
		assertEquals("成功删除2条电池条码", StringUtils.trim(result.getMessage()));
	}
}
