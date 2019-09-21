package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleVipPriceServiceTest extends BaseJunit4Test {
	@Autowired
	VehicleVipPriceService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		VehicleVipPrice vehicleVipPrice = newVehicleVipPrice(agent.getId());
		insertVehicleVipPrice(vehicleVipPrice);

		assertNotNull(service.find(vehicleVipPrice.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		VehicleVipPrice vehicleVipPrice = newVehicleVipPrice(agent.getId());
		insertVehicleVipPrice(vehicleVipPrice);

		assertTrue(1 == service.findPage(vehicleVipPrice).getTotalItems());
		assertTrue(1 == service.findPage(vehicleVipPrice).getResult().size());
	}

	@Test
	public void create() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		VehicleVipPrice vehicleVipPrice = newVehicleVipPrice(agent.getId());
		assertTrue(service.create(vehicleVipPrice).isSuccess());
		assertTrue(1 == service.findPage(vehicleVipPrice).getTotalItems());
		assertTrue(1 == service.findPage(vehicleVipPrice).getResult().size());
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		VehicleVipPrice vehicleVipPrice = newVehicleVipPrice(agent.getId());
		insertVehicleVipPrice(vehicleVipPrice);

		vehicleVipPrice.setDayCount(200);
		assertTrue(service.update(vehicleVipPrice).isSuccess());
		assertEquals(vehicleVipPrice.getDayCount(), service.find(vehicleVipPrice.getId()).getDayCount());
	}
}
