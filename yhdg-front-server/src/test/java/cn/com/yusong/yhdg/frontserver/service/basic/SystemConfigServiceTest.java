package cn.com.yusong.yhdg.frontserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AppConfig appConfig;

    @Test
    public void find() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(systemConfigService.find(systemConfig.getId()));
    }

    @Test
    public void initConfig() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        systemConfigService.initConfig();

        assertNotNull(appConfig.ftpEncoding);
        assertNotNull(appConfig.ftpPassword);
        assertNotNull(appConfig.ftpUser);
        assertNotNull(appConfig.ftpPort);
        assertNotNull(appConfig.downloadCount);
    }

    @Test
    public void findAll() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertTrue(systemConfigService.findAll().size()>0);
    }
}
