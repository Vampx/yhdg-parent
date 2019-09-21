package cn.com.yusong.yhdg.batteryserver.service;

import cn.com.yusong.yhdg.batteryserver.BaseJunit4Test;
import cn.com.yusong.yhdg.batteryserver.persistence.hdg.BatteryReportLogMapper;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class AbstractServiceTest extends BaseJunit4Test {

    @Autowired
    AbstractService abstractService;

    @Test
    public void findTable() {
        assertEquals(true, abstractService.findTable("hdg_battery_report_date"));
    }
}
