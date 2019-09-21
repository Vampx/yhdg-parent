package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformMonthStats;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class PlatformMonthStatsServiceTest extends BaseJunit4Test {
    @Autowired
    PlatformMonthStatsService service;

    @Test
    public void findPage(){
        PlatformMonthStats platformMonthStats = newPlatformMonthStats("2017-01");
        insertPlatformMonthStats(platformMonthStats);
        assertTrue(1 == service.findPage(platformMonthStats).getTotalItems());
        assertTrue(1 == service.findPage(platformMonthStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        PlatformMonthStats platformMonthStats = newPlatformMonthStats("2017-01");
        insertPlatformMonthStats(platformMonthStats);
        assertTrue(1 == service.findForExcel(platformMonthStats).size());
    }
}
