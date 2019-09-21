package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopDayStatsServiceTest extends BaseJunit4Test {
	@Autowired
	ShopDayStatsService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		ShopDayStats shopDayStats = newShopDayStats(partner.getId(), agent.getId(), shop.getId());
		insertShopDayStats(shopDayStats);

		assertTrue(1 == service.findPage(shopDayStats).getTotalItems());
		assertTrue(1 == service.findPage(shopDayStats).getResult().size());
	}

	@Test
	public void findForExcel() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		ShopDayStats shopDayStats = newShopDayStats(partner.getId(), agent.getId(), shop.getId());
		insertShopDayStats(shopDayStats);

		assertTrue(1 == service.findForExcel(shopDayStats).size());
	}
}
