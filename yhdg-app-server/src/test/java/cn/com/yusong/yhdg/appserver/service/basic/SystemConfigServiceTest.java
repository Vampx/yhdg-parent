package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;

    @Test
    public void findConfigValue() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);
        assertNotNull(systemConfigService.findConfigValue(systemConfig.getId()));
    }

    @Test
    public void findAll() {
        assertNotNull(systemConfigService.findAll());
    }
}
