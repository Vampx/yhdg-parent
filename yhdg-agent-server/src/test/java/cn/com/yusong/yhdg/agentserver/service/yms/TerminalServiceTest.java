package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalServiceTest extends BaseJunit4Test {

    @Autowired
    TerminalService terminalService;
    @Autowired
    CabinetService cabinetService;

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
        assertNotNull(terminalService.find(terminal.getId()));
    }

    @Test
    public void findIdByStrategy() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);
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

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        Page page = terminalService.findPage(terminal);
        assertEquals(1, page.getTotalItems());
    }

    @Test
    public void findNotAssociatedPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);
        terminal.setId("2e5r");
        insertTerminal(terminal);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertTrue(1 == terminalService.findNotAssociatedPage(terminal).getTotalItems());
        assertTrue(1 == terminalService.findNotAssociatedPage(terminal).getResult().size());
    }

    @Test
    public void updateBasicInfo() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Terminal entity = new Terminal();
        entity.setId(terminal.getId());
        entity.setPlaylistId(playlist.getId());
        assertTrue("update fail", terminalService.updateBasicInfo(entity).isSuccess());
    }

    @Test
    public void delete() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalOnline terminalOnline = newTerminalOnline(agent.getId());
        insertTerminalOnline(terminalOnline);

        assertTrue(terminalService.delete(terminal.getId()).isSuccess());
    }

    @Test
    public void update() throws Exception {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        String[] terminalIds = {terminal.getId()};
        assertTrue(terminalService.update(terminalIds, playlist.getId()) > 0);
    }

    @Test
    public void relevanceCabinet() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        insertCabinet(cabinet);

        assertTrue(terminalService.relevanceCabinet(cabinet.getId(), terminal.getId()).isSuccess());
        assertEquals(cabinet.getTerminalId(), cabinetService.find(cabinet.getId()).getTerminalId());
    }

    @Test
    public void findId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        assertTrue(1 == terminalService.findId().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        assertTrue(terminalService.insert(terminal).isSuccess());
    }

}