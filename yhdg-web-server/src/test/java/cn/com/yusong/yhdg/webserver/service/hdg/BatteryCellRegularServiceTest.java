package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatteryCellRegularServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryCellRegularService service;

	@Test
	public void find() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		assertNotNull(service.find(batteryCellRegular.getId()));
	}

	@Test
	public void findByCellFormatIdAndType() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		assertNotNull(service.findByCellFormatIdAndType(batteryCellFormat.getId(), BatteryCellRegular.RegularType.CELL_FORMAT.getValue()));
	}

	@Test
	public void findByBatteryFormatIdAndType() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		batteryCellRegular.setRegularType(BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());
		insertBatteryCellRegular(batteryCellRegular);

		assertNotNull(service.findByBatteryFormatIdAndType(batteryFormat.getId(), BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue()));
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

		assertTrue(service.create(batteryCellRegular).isSuccess());
		assertNotNull(service.find(batteryCellRegular.getId()));
	}

	@Test
	public void checkParam() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		assertTrue(service.checkParam(batteryCellRegular).isSuccess());
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

		batteryCellRegular.setRegular("YYYYNNNN");
		batteryCellRegular.setBatteryFormatId(null);
		assertTrue(service.update(batteryCellRegular).isSuccess());
		assertEquals(service.find(batteryCellRegular.getId()).getRegular(), batteryCellRegular.getRegular());
	}

	@Test
	public void updateNumByCellFormatId() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		batteryCellRegular.setNum(999);
		assertTrue(service.updateNumByCellFormatId(batteryCellFormat.getId(), batteryCellRegular.getNum()).isSuccess());
		assertEquals(service.find(batteryCellRegular.getId()).getNum(), batteryCellRegular.getNum());
	}

	@Test
	public void updateNumByBatteryFormatId() {
		BatteryCellModel batteryCellModel = newBatteryCellModel();
		insertBatteryCellModel(batteryCellModel);

		BatteryCellFormat batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
		insertBatteryCellFormat(batteryCellFormat);

		BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
		insertBatteryFormat(batteryFormat);

		BatteryCellRegular batteryCellRegular = newBatteryCellRegular(batteryCellFormat.getId(), batteryFormat.getId());
		insertBatteryCellRegular(batteryCellRegular);

		batteryCellRegular.setNum(999);
		assertTrue(service.updateNumByBatteryFormatId(batteryFormat.getId(), batteryCellRegular.getNum()).isSuccess());
		assertEquals(service.find(batteryCellRegular.getId()).getNum(), batteryCellRegular.getNum());
	}

	@Test
	public void text() {
		String s ="[['1','2','3'],['4','5','6']]";
		String expected="[{\"a\":12},{\"b\":23},{\"name\":\"Ryan\"}]";
		JSONArray jsonArray = JSONArray.fromObject(s);

		for (int i = 0; i < jsonArray.size(); i++) {
			String s1 = jsonArray.get(i).toString();
			Integer[] ss =(Integer[])JSONArray.toArray(JSONArray.fromObject(s1),Integer.class);
			for (int j = 0; j <ss.length ; j++) {
				System.out.println(ss[j]);
			}
		}
		//Integer[] ss =(Integer[])JSONArray.toArray(JSONArray.fromObject(s),Integer.class);

	}


}
