package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalPropertyServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalPropertyService service;

    @Test
    public void findByTerminal() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalProperty terminalProperty = newTerminalProperty(terminal.getId(), 1);
        insertTerminalProperty(terminalProperty);

        assertNotNull(service.findByTerminal(terminalProperty.getTerminalId()));
    }
}
