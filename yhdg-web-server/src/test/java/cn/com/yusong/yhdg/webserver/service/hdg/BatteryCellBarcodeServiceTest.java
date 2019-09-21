package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryCellBarcodeServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryCellBarcodeService service;

	@Test
	public void findMaxCode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setBarcode("1");
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode2.setBarcode("2");
		insertBatteryCellBarcode(batteryCellBarcode2);

		assertNotNull(service.findMaxCode(batteryCellFormat.getId()));
		assertEquals(service.findMaxCode(batteryCellFormat.getId()), batteryCellBarcode2.getBarcode());
	}

	@Test
	public void findMaxCodeCellBarcode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setBarcode("1");
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode2.setBarcode("2");
		insertBatteryCellBarcode(batteryCellBarcode2);

		assertNotNull(service.findMaxCodeCellBarcode(batteryCellFormat.getId()));
		assertEquals(service.findMaxCodeCellBarcode(batteryCellFormat.getId()).getBarcode(), batteryCellBarcode2.getBarcode());
	}

	@Test
	public void findList() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setBarcode("1");
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode2.setBarcode("2");
		insertBatteryCellBarcode(batteryCellBarcode2);

		assertTrue(2 == service.findList(batteryCellFormat.getId()).size());
	}

	@Test
	public void findByBarcode() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setBarcode("1");
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode2.setBarcode("2");
		insertBatteryCellBarcode(batteryCellBarcode2);

		DataResult result = (DataResult) service.findByBarcode(batteryCellBarcode2.getBarcode(), "admin");
		BatteryCellBarcode batteryCellBarcode1 = (BatteryCellBarcode) result.getData();
		assertEquals(batteryCellBarcode2.getBarcode(), batteryCellBarcode1.getBarcode());
	}

	@Test
	public void findPage() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setBarcode("1");
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode2.setBarcode("2");
		insertBatteryCellBarcode(batteryCellBarcode2);

		batteryCellBarcode.setBarcode(null);
		assertTrue(2 == service.findPage(batteryCellBarcode).getResult().size());
		assertTrue(2 == service.findPage(batteryCellBarcode).getTotalItems());
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
		insertBatteryCellRegular(batteryCellRegular);

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		batteryCellBarcode.setCodeCount(5);

		assertTrue(service.create(batteryCellBarcode).isSuccess());
		batteryCellBarcode.setBarcode(null);
		assertTrue(5 == service.findPage(batteryCellBarcode).getTotalItems());
		assertTrue(5 == service.findPage(batteryCellBarcode).getResult().size());
	}

	@Test
	public void update() {
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

		batteryCellBarcode.setCellModel("测试的cellModel");
		assertTrue(service.update(batteryCellBarcode).isSuccess());

		DataResult result = (DataResult) service.findByBarcode(batteryCellBarcode.getBarcode(), "admin");
		BatteryCellBarcode batteryCellBarcode1 = (BatteryCellBarcode) result.getData();
		assertEquals(batteryCellBarcode.getCellModel(), batteryCellBarcode1.getCellModel());
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


		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		insertBatteryCellBarcode(batteryCellBarcode);

		assertTrue(service.delete(batteryCellBarcode.getId()).isSuccess());
		assertTrue(0 == service.findPage(batteryCellBarcode).getTotalItems());
		assertTrue(0 == service.findPage(batteryCellBarcode).getResult().size());
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

		BatteryCellBarcode batteryCellBarcode = newBatteryCellBarcode(batteryCellFormat.getId());
		insertBatteryCellBarcode(batteryCellBarcode);

		BatteryCellBarcode batteryCellBarcode2 = newBatteryCellBarcode(batteryCellFormat.getId());
		insertBatteryCellBarcode(batteryCellBarcode2);

		long[] idList = {batteryCellBarcode.getId(), batteryCellBarcode2.getId()};
		ExtResult result = service.batchDelete(idList);
		assertTrue(result.isSuccess());
		assertEquals("成功删除2条电芯条码", result.getMessage());
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
}
