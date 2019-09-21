package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SystemConfigServiceTest extends BaseJunit4Test {

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    MemCachedClient memCachedClient;

    @Test
    public void findConfigValue() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(systemConfigService.findConfigValue(systemConfig.getId()));
    }

    @Test
    public void find() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(systemConfigService.find(systemConfig.getId()));
    }
}