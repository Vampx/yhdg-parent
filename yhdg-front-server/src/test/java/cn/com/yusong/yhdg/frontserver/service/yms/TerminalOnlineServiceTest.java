package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class TerminalOnlineServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalOnlineService service;

    @Test
    public void updateHeartInfo() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        terminalOnline.setMemory(2048000F);
        terminalOnline.setCardCapacity(54545454545L);

        assertEquals(1, service.updateHeartInfo(terminalOnline));
    }

    @Test
    public void updateLoginInfo() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertEquals(1, service.updateLoginInfo(terminalOnline.getId(), 1, new Date()));
    }

    @Test
    public void updatePlayFile() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertEquals(1, service.updatePlayFile(terminalOnline.getId(), "4erahgreahaeh"));
    }

    @Test
    public void updateDownloadPlaylistProgress() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertEquals(1, service.updateDownloadPlaylistProgress(terminalOnline.getId(), 3555F, 447F));
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());

        assertEquals(1, service.insert(terminalOnline));
    }
}
