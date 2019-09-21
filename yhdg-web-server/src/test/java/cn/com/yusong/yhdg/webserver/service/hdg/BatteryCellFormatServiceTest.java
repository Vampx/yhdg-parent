package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryCellFormatServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryCellFormatService service;

	@Test
	public void find() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		assertNotNull(service.find(batteryCellFormat.getId()));
	}

	@Test
	public void findPage() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		assertTrue(1 == service.findPage(batteryCellFormat).getTotalItems());
		assertTrue(1 == service.findPage(batteryCellFormat).getResult().size());
	}

	@Test
	public void create() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());

		assertTrue(service.create(batteryCellFormat).isSuccess());
		assertNotNull(service.find(batteryCellFormat.getId()));
	}

	@Test
	public void update() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		batteryCellFormat.setCellMfr("新的厂家");
		assertTrue(service.update(batteryCellFormat).isSuccess());
		assertEquals(batteryCellFormat.getCellMfr(), service.find(batteryCellFormat.getId()).getCellMfr());
	}

	@Test
	public void delete() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		assertTrue(service.delete(batteryCellFormat.getId()).isSuccess());
		assertNull(service.find(batteryCellFormat.getId()));

	}
}
