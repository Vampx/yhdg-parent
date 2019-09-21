package cn.com.yusong.yhdg.routeserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.routeserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;

    @Test
    public void findConfigValue() {
        systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue());
    }
}
