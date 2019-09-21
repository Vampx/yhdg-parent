package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalOnlineServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalOnlineService terminalOnlineService;

    @Test
    public void find() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertNotNull(terminalOnlineService.find(terminalOnline.getId()));
    }

    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        terminalOnline.setId(cabinet.getTerminalId());
        insertTerminalOnline(terminalOnline);

        Page page = terminalOnlineService.findPage(terminalOnline);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void offline() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);
        assertEquals(1, terminalOnlineService.offline(terminalOnline.getId()));
    }

}