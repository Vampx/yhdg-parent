package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class TerminalStrategyServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalStrategyService terminalStrategyService;

    @Test
    public void find() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));

    }

    @Test
    public void findByAgent() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        List<TerminalStrategy> list = terminalStrategyService.findByAgent(agent.getId());
        assertFalse(list.isEmpty());
    }

    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Page page = terminalStrategyService.findPage(terminalStrategy);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void create() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        assertEquals(1, terminalStrategyService.create(terminalStrategy, "sdagar"));
    }

    @Test
    public void update() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        TerminalStrategy entity = new TerminalStrategy();
        entity.setId(terminalStrategy.getId());
        entity.setAgentId(agent.getId());
        entity.setStrategyName("终端策略");
        entity.setVersion(4);
        entity.setCreateTime(new Date());
        assertEquals(1, terminalStrategyService.update(entity, "areger"));
    }

    @Test
    public void delete() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        assertTrue("create fail", terminalStrategyService.delete(terminalStrategy.getId()).isSuccess());
    }

    @Test
    public void hasRef() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        ExtResult extResult = terminalStrategyService.hasRef(terminal.getStrategyId());

        String result = String.format("策略被使用:%s不能删除", terminal.getId());
        assertEquals(result, extResult.getMessage());
    }

    @Test
    public void findStrategyXml() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        assertNotNull(terminalStrategyService.find(terminalStrategy.getId()));
    }

}