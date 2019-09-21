package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BalanceRecordServiceTest extends BaseJunit4Test {
	@Autowired
	BalanceRecordService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		BalanceRecord balanceRecord = newBalanceRecord(partner.getId(), agent.getId(), shop.getId(), null);
		insertBalanceRecord(balanceRecord);

		assertNotNull(service.find(balanceRecord.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		BalanceRecord balanceRecord = newBalanceRecord(partner.getId(), agent.getId(), shop.getId(), null);
		insertBalanceRecord(balanceRecord);

		assertTrue(1 == service.findPage(balanceRecord).getTotalItems());
		assertTrue(1 == service.findPage(balanceRecord).getResult().size());
	}
}
