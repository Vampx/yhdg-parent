package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopServiceTest extends BaseJunit4Test {
	@Autowired
	ShopService service;
	@Autowired
	AreaCache areaCache;

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Shop shop = newShop(agent.getId());
		insertShop(shop);
		assertTrue(1 == service.findPage(shop).getTotalItems());
		assertTrue(1 == service.findPage(shop).getResult().size());
	}

	@Test
	public void findUnboundPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(1 == service.findUnboundPage(shop).getTotalItems());
		assertTrue(1 == service.findUnboundPage(shop).getResult().size());
	}

	@Test
	public void findMaxId() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);
		assertNotNull(service.findMaxId());
	}

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertNotNull(service.find(shop.getId()));
	}

	@Test
	public void delete() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.delete(shop.getId()).isSuccess());
		assertNull(service.find(shop.getId()));

	}

	@Test
	public void insert() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());

		assertTrue(service.insert(shop).isSuccess());

	}

	@Test
	public void findUnique() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertFalse(service.findUnique(shop.getId()).isSuccess());
		assertEquals("编号重复", service.findUnique(shop.getId()).getMessage());
	}

	@Test
	public void updateBasic() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.updateBasic(shop).isSuccess());
	}

	@Test
	public void updateImage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.updateImage(shop).isSuccess());

	}
	@Test
	public void updateLocation() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.updateLocation(shop).isSuccess());

	}

	@Test
	public void updateNewLocation() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());

		shop.setProvinceName("广东省");
		shop.setCityName("深圳市");
		shop.setDistrictName("福田区");
		insertShop(shop);

		Integer provinceId = null;
		Integer cityId = null;
		Integer districtId = null;
		if (shop.getProvinceName() != null) {
			Area area = areaCache.getByName(shop.getProvinceName());
			provinceId = Integer.valueOf(area.getAreaCode());
		}
		if (shop.getCityName() != null) {
			Area area = areaCache.getByName(shop.getCityName());
			cityId = Integer.valueOf(area.getAreaCode());
		}
		if (shop.getDistrictName() != null) {
			Area area = areaCache.getByName(shop.getDistrictName());
			districtId = Integer.valueOf(area.getAreaCode());
		}
		assertTrue(service.updateNewLocation(shop).isSuccess());
		assertEquals(provinceId, service.find(shop.getId()).getProvinceId());
		assertEquals(cityId, service.find(shop.getId()).getCityId());
		assertEquals(districtId, service.find(shop.getId()).getDistrictId());

		String provinceName = null;
		String cityName = null;
		String districtName = null;
		Shop newShop = service.find(shop.getId());
		if (newShop.getProvinceId() != null) {
			provinceName = areaCache.get(newShop.getProvinceId()).getAreaName();
		}
		if (newShop.getCityId() != null) {
			cityName = areaCache.get(newShop.getCityId()).getAreaName();
		}
		if (newShop.getDistrictId() != null) {
			districtName = areaCache.get(newShop.getDistrictId()).getAreaName();
		}
		assertEquals(provinceName,"广东省");
		assertEquals(cityName, "深圳市");
		assertEquals(districtName, "福田区");
	}

	@Test
	public void clearImage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		assertTrue(service.clearImage(shop.getId(), 1).isSuccess());
	}

}
