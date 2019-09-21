package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractServiceTest extends BaseJunit4Test {

    @Autowired
    AbstractService abstractService;

    @Test
    public void findTerminalStrategy(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        TerminalStrategy terminalStrategyInfo = abstractService.findTerminalStrategy(terminalStrategy.getId());
        assertNotNull(terminalStrategyInfo);
    }
}
