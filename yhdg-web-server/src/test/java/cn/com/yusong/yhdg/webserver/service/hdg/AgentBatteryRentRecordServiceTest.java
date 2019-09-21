package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.AgentBatteryRentRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AgentBatteryRentRecordServiceTest extends BaseJunit4Test {
	@Autowired
	AgentBatteryRentRecordService service;

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

		AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
		insertAgentMaterialDayStats(agentMaterialDayStats);

		AgentBatteryRentRecord agentBatteryRentRecord = newAgentBatteryRentRecord(agent.getId(), battery.getId(), agentMaterialDayStats.getId());
		insertAgentBatteryRentRecord(agentBatteryRentRecord);

		assertTrue(1 == service.findPage(agentBatteryRentRecord).getTotalItems());
		assertTrue(1 == service.findPage(agentBatteryRentRecord).getResult().size());
	}
}
