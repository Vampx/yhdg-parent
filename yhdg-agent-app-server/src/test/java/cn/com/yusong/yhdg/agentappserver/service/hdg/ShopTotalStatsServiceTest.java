package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopTotalStatsServiceTest extends BaseJunit4Test {
    @Autowired
    ShopTotalStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopTotalStats shopTotalStats = newShopTotalStats(partner.getId(), agent.getId(), shop.getId());
        insertShopTotalStats(shopTotalStats);

        assertNotNull(service.find(shop.getId(), shopTotalStats.getCategory(), agent.getId()));
    }

    @Test
    public void findListByAgentId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopTotalStats shopTotalStats = newShopTotalStats(partner.getId(), agent.getId(), shop.getId());
        insertShopTotalStats(shopTotalStats);

        assertEquals(1,service.findListByAgentId(agent.getId(), null, shopTotalStats.getCategory(),0,10).size());
    }


    @Test
    public void sumAll() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopTotalStats shopTotalStats = newShopTotalStats(partner.getId(), agent.getId(), shop.getId());
        insertShopTotalStats(shopTotalStats);

        assertNotNull(service.sumAll(agent.getId()));
    }

    @Test
    public void findCountByAgentId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopTotalStats shopTotalStats = newShopTotalStats(partner.getId(), agent.getId(), shop.getId());
        insertShopTotalStats(shopTotalStats);

        assertNotNull(service.findCountByAgentId(agent.getId(), shopTotalStats.getCategory()));
    }
}