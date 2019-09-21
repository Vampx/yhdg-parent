package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.SystemConfigService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;

    @Test
    public void findAll() {
        systemConfigService.findAll();
    }
}
