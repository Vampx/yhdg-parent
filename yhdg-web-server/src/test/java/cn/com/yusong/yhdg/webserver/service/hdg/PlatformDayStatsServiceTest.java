package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class PlatformDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    PlatformDayStatsService service;

    @Test
    public void findPage(){
        PlatformDayStats platformDayStats = newPlatformDayStats("2017-01-01");
        insertPlatformDayStats(platformDayStats);
        assertTrue(1 == service.findPage(platformDayStats).getTotalItems());
        assertTrue(1 == service.findPage(platformDayStats).getResult().size());
    }

    @Test
    public void findForExcel() {
        PlatformDayStats platformDayStats = newPlatformDayStats("2017-01-01");
        insertPlatformDayStats(platformDayStats);
        assertTrue(1 == service.findForExcel(platformDayStats).size());
    }

    @Test
    public void findByDate() {
        PlatformDayStats platformDayStats = newPlatformDayStats("2017-01-01");
        platformDayStats.setStatsDate("2017-01-01");
        insertPlatformDayStats(platformDayStats);

        assertNotNull(service.findByDate("2017-01-01"));
    }
}
