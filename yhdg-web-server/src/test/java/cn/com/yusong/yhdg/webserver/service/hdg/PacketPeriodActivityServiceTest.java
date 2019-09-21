package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PacketPeriodActivityServiceTest extends BaseJunit4Test {
	@Autowired
	PacketPeriodActivityService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodActivity packetPeriodActivity = newPacketPeriodActivity(agent.getId(), systemBatteryType.getId());
		insertPacketPeriodActivity(packetPeriodActivity);

		assertNotNull(service.find(packetPeriodActivity.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodActivity packetPeriodActivity = newPacketPeriodActivity(agent.getId(), systemBatteryType.getId());
		insertPacketPeriodActivity(packetPeriodActivity);

		assertTrue(1 == service.findPage(packetPeriodActivity).getTotalItems());
		assertTrue(1 == service.findPage(packetPeriodActivity).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodActivity packetPeriodActivity = newPacketPeriodActivity(agent.getId(), systemBatteryType.getId());
		assertTrue(service.create(packetPeriodActivity).isSuccess());
		assertNotNull(service.find(packetPeriodActivity.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodActivity packetPeriodActivity = newPacketPeriodActivity(agent.getId(), systemBatteryType.getId());
		insertPacketPeriodActivity(packetPeriodActivity);

		packetPeriodActivity.setDayCount(1234);
		assertTrue(service.update(packetPeriodActivity).isSuccess());
		assertEquals(service.find(packetPeriodActivity.getId()).getDayCount(), packetPeriodActivity.getDayCount());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodActivity packetPeriodActivity = newPacketPeriodActivity(agent.getId(), systemBatteryType.getId());
		insertPacketPeriodActivity(packetPeriodActivity);

		assertTrue(service.delete(packetPeriodActivity.getId()).isSuccess());
		assertNull(service.find(packetPeriodActivity.getId()));
	}
}
