package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopServiceTest extends BaseJunit4Test {
	@Autowired
	ShopService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(1 == service.findPage(shop).getTotalItems());
		assertTrue(1 == service.findPage(shop).getResult().size());
	}

	@Test
	public void findMaxId() {
		assertNotNull(service.findMaxId());
	}

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertNotNull(service.find(shop.getId()));
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.delete(shop.getId()).isSuccess());
		assertNull(service.find(shop.getId()));
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());

		assertTrue(service.insert(shop).isSuccess());
		assertNotNull(service.find(shop.getId()));
	}

	@Test
	public void updateBasic() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		shop.setShopName("newShopName");
		assertTrue(service.updateBasic(shop).isSuccess());
		assertEquals(shop.getShopName(), service.find(shop.getId()).getShopName());
	}

	@Test
	public void updateNewLocation() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		shop.setAddress("newAddress");
		assertTrue(service.updateBasic(shop).isSuccess());
		assertEquals(shop.getAddress(), service.find(shop.getId()).getAddress());
	}
}
