package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopInOutMoneyServiceTest extends BaseJunit4Test {
	@Autowired
	ShopInOutMoneyService service;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		ShopInOutMoney shopInOutMoney = newShopInOutMoney(shop.getId());
		insertShopInOutMoney(shopInOutMoney);

		assertTrue(1 == service.findPage(shopInOutMoney).getTotalItems());
		assertTrue(1 == service.findPage(shopInOutMoney).getResult().size());
	}
}
