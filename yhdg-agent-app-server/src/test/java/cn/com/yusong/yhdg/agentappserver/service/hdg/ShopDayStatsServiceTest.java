package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ShopDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    ShopDayStatsService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopDayStats shopDayStats1 = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        insertShopDayStats(shopDayStats1);

        assertNotNull(service.find(agent.getId(), shop.getId(), shopDayStats1.getStatsDate(), 1));
    }

    @Test
    public void findTotalShopStatsList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopDayStats shopDayStats = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        insertShopDayStats(shopDayStats);

        assertEquals(1,service.findTotalShopStatsList(agent.getId(), shopDayStats.getCategory(), shopDayStats.getStatsDate(),shopDayStats.getStatsDate(),null ,0,10).size());
    }


    @Test
    public void findTotalStatsListShopId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopDayStats shopDayStats = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        insertShopDayStats(shopDayStats);

        assertNotNull(service.findTotalStatsListShopId(agent.getId(), shop.getId(), shopDayStats.getCategory(), shopDayStats.getStatsDate(),shopDayStats.getStatsDate()));
    }

    @Test
    public void findDateRange() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopDayStats shopDayStats = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        insertShopDayStats(shopDayStats);

        assertEquals(1,service.findDateRange(agent.getId(),shop.getId(), shopDayStats.getStatsDate(),shopDayStats.getStatsDate(), shopDayStats.getCategory()).size());
    }


    @Test
    public void statsOrderAndRefundMoney() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopDayStats shopDayStats1 = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        insertShopDayStats(shopDayStats1);

        ShopDayStats shopDayStats2 = newShopDayStats(shop.getId(), partner.getId(), agent.getId());
        shopDayStats2.setStatsDate("2012-12-13");
        insertShopDayStats(shopDayStats2);

        ShopDayStats result = service.statsOrderAndRefundMoney(shop.getId());
        assertTrue(result.getPacketPeriodMoney() == shopDayStats1.getPacketPeriodMoney() + shopDayStats2.getPacketPeriodMoney());
        assertTrue(result.getRefundPacketPeriodMoney() == shopDayStats1.getRefundPacketPeriodMoney() + shopDayStats2.getRefundPacketPeriodMoney());
//        assertTrue(result.getRentPeriodMoney() == shopDayStats1.getRentPeriodMoney() + shopDayStats2.getRentPeriodMoney());
//        assertTrue(result.getRefundRentPeriodMoney() == shopDayStats1.getRefundRentPeriodMoney() + shopDayStats2.getRefundRentPeriodMoney());
    }
}