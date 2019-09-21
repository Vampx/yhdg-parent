package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ShopDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    private ShopDayStatsService service;

    private ShopDayStats shopDayStats;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        shopDayStats = newShopDayStats(shop.getId(), partner.getId(), agent.getId());

    }

    @Test
    public void findPage() {
        insertShopDayStats(shopDayStats);

        assertEquals(1, service.findPage(shopDayStats).getTotalItems());
        assertEquals(1, service.findPage(shopDayStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        insertShopDayStats(shopDayStats);

        assertEquals(1, service.findPage(shopDayStats).getResult().size());
    }
}