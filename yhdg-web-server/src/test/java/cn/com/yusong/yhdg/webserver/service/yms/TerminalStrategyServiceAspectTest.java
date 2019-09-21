package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TerminalStrategyServiceAspectTest extends BaseJunit4Test {
    @Autowired
    TerminalStrategyService terminalStrategyService;

    @Test
    public void find(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));

        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());
        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));
        assertNotNull(memCachedClient.get(key));
    }

    @Test
    public void findByAgent(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));

        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());
        List<TerminalStrategy> list = terminalStrategyService.findByAgent(agent.getId());
        assertFalse(list.isEmpty());
        assertNotNull(memCachedClient.get(key));
    }

    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());
        Page page = terminalStrategyService.findPage(terminalStrategy);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void create() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());

        assertEquals(1, terminalStrategyService.create(terminalStrategy, "sdagar"));
    }

    @Test
    public void update(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        TerminalStrategy terminalStrategy2 = new TerminalStrategy();
        terminalStrategy2.setId(terminalStrategy.getId());
        terminalStrategy2.setVersion(12);
        terminalStrategy2.setStrategyName("ri");
        terminalStrategyService.update(terminalStrategy2,"ardgea");

        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());
        memCachedClient.delete(key);
        terminalStrategyService.delete(terminalStrategy.getId());
        assertNull(memCachedClient.get(key));
    }

    @Test
    public void delete(){
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));

        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, terminalStrategy.getId());
        memCachedClient.delete(key);
        terminalStrategyService.delete(terminalStrategy.getId());
        assertNull(memCachedClient.get(key));
    }

}