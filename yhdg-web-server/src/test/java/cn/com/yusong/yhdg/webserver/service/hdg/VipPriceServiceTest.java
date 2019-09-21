package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VipPriceServiceTest extends BaseJunit4Test {
	@Autowired
	VipPriceService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
		insertVipPrice(vipPrice);

		assertNotNull(service.find(vipPrice.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
		insertVipPrice(vipPrice);

		assertTrue(1 == service.findPage(vipPrice).getTotalItems());
		assertTrue(1 == service.findPage(vipPrice).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());

		assertTrue(service.create(vipPrice).isSuccess());
		assertNotNull(service.find(vipPrice.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
		insertVipPrice(vipPrice);

		vipPrice.setPriceName("测试的priceName");
		assertTrue(service.update(vipPrice).isSuccess());
		assertEquals(service.find(vipPrice.getId()).getReduceMoney(), vipPrice.getReduceMoney());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
		insertVipPrice(vipPrice);

		assertTrue(service.delete(vipPrice.getId()).isSuccess());
		assertNull(service.find(vipPrice.getId()));
	}
}
