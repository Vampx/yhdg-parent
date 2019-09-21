package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;

    @Test
    public void find() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(systemConfigService.find(systemConfig.getId()));
    }
}