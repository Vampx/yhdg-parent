package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaServiceTest extends BaseJunit4Test {

    @Autowired
    AreaService areaService;

    @Test
    public void spy() throws InterruptedException {
        memCachedClient.set("aaa", "aaa", MemCachedConfig.CACHE_TEN_SECOND);
        Thread.sleep(1000 * 9);
        assertNotNull(memCachedClient.get("aaa"));
        Thread.sleep(1000 * 2);
        assertNull(memCachedClient.get("aaa"));
    }

    @Test
    public void findAll() {
        Area area = newArea(null);
        insertArea(area);
        assertNotNull(areaService.findAll());
    }
}
