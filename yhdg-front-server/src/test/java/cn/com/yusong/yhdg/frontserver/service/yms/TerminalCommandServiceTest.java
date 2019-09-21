package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class TerminalCommandServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalCommandService service;

    @Test
    public void findWaitExec() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        insertTerminalCommand(terminalCommand);

        assertTrue(service.findWaitExec(terminalCommand.getTerminalId(), TerminalCommand.Status.DISPATCH.getValue()).size() > 0);
    }

    @Test
    public void dispatch() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        insertTerminalCommand(terminalCommand);

        assertEquals(1, service.dispatch(terminalCommand.getId(), TerminalCommand.Status.DISPATCH.getValue(), new Date()));
    }

    @Test
    public void exec() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalCommand terminalCommand = newTerminalCommand(agent.getId(), terminal.getId());
        insertTerminalCommand(terminalCommand);

        assertEquals(1, service.exec(terminalCommand.getId(), TerminalCommand.Status.DISPATCH.getValue(), new Date(), "1111"));
    }

}
