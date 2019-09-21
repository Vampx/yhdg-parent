package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalCommandServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalCommandService terminalCommandService;

    @Test
    public void find() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        insertTerminalCommand(terminalCommand);
       assertNotNull(terminalCommandService.find(terminalCommand.getId()));

    }

    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        insertTerminalCommand(terminalCommand);

        Page page = terminalCommandService.findPage(terminalCommand);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void create() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        assertEquals(1, terminalCommandService.create(terminalCommand));
    }

}