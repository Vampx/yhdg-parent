package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AgentSystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @Test
    public void findConfigValue() {
//        assertNotNull(agentSystemConfigService.findConfigValue());
    }
}