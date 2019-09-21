package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

public class BatteryParameterServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryParameterService service;
	@Autowired
	BatteryParameterLogService batteryParameterLogService;

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

		BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
		insertBatteryParameter(batteryParameter);

		assertNotNull(service.find(batteryParameter.getId()));
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

		BatteryParameter batteryParameter = newBatteryParameter(battery.getId());
		insertBatteryParameter(batteryParameter);

		BatteryParameterLog batteryParameterLog = newBatteryParameterLog(battery.getId());
		batteryParameterLog.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
		int batteryParameterLogResults1 = batteryParameterLogService.findPage(batteryParameterLog).getResult().size();

		batteryParameter.setCellFullVol(123);
		assertTrue(service.update(batteryParameter, "admin").isSuccess());

		assertEquals(service.find(batteryParameter.getId()).getCellFullVol(), batteryParameter.getCellFullVol());
		int batteryParameterLogResults2 = batteryParameterLogService.findPage(batteryParameterLog).getResult().size();
		assertEquals(batteryParameterLogResults1 + 1, batteryParameterLogResults2);
	}

	@Test
	public void batchUpdate() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery1);
		Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
		battery2.setId("234");
		insertBattery(battery2);

		BatteryParameter batteryParameter1 = newBatteryParameter(battery1.getId());
		batteryParameter1.setOcvTable("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1");
		insertBatteryParameter(batteryParameter1);
		BatteryParameter batteryParameter2 = newBatteryParameter(battery2.getId());
		batteryParameter2.setOcvTable("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1");
		insertBatteryParameter(batteryParameter2);

		BatteryParameterLog batteryParameterLog1 = newBatteryParameterLog(battery1.getId());
		batteryParameterLog1.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
		int batteryParameterLogResults1 = batteryParameterLogService.findPage(batteryParameterLog1).getResult().size();
		BatteryParameterLog batteryParameterLog2 = newBatteryParameterLog(battery2.getId());
		batteryParameterLog2.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
		int batteryParameterLogResults2 = batteryParameterLogService.findPage(batteryParameterLog2).getResult().size();

		BatteryParameter newBatteryParameter = new BatteryParameter();
		newBatteryParameter.setCellFullVol(123);
		String idsData = batteryParameter1.getId() + "," + batteryParameter2.getId();
		newBatteryParameter.setIdsData(idsData);
		String ocvTable = "2,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
		newBatteryParameter.setOcvTable(ocvTable);
		newBatteryParameter.setMfd("//");
		newBatteryParameter.setRsns(0);
		newBatteryParameter.setFunction(0);
		newBatteryParameter.setNtcConfig(0);
		assertTrue(service.batchUpdate(newBatteryParameter, "admin").isSuccess());

		assertEquals(service.find(batteryParameter1.getId()).getCellFullVol(), newBatteryParameter.getCellFullVol());
		assertEquals(service.find(batteryParameter1.getId()).getOcvTable(), newBatteryParameter.getOcvTable());
		assertEquals(service.find(batteryParameter2.getId()).getCellFullVol(), newBatteryParameter.getCellFullVol());
		assertEquals(service.find(batteryParameter2.getId()).getOcvTable(), newBatteryParameter.getOcvTable());
		int batteryParameterLogResults11 = batteryParameterLogService.findPage(batteryParameterLog1).getResult().size();
		int batteryParameterLogResults22 = batteryParameterLogService.findPage(batteryParameterLog2).getResult().size();

		assertEquals(batteryParameterLogResults1 + 2, batteryParameterLogResults11);
		assertEquals(batteryParameterLogResults2 + 2, batteryParameterLogResults22);
	}


}
