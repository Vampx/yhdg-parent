package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TerminalUploadLogServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalUploadLogService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalUploadLog uploadLog = newTerminalUploadLog(agent.getId(), terminal.getId());
        insertTerminalUploadLog(uploadLog);

        assertNotNull(service.find(uploadLog.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalUploadLog uploadLog = newTerminalUploadLog(agent.getId(), terminal.getId());
        insertTerminalUploadLog(uploadLog);

        assertTrue(1 == service.findPage(uploadLog).getTotalItems());
        assertTrue(1 == service.findPage(uploadLog).getResult().size());
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

        TerminalUploadLog uploadLog = newTerminalUploadLog(agent.getId(), terminal.getId());

        assertEquals(1, service.insert(uploadLog));
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalUploadLog uploadLog = newTerminalUploadLog(agent.getId(), terminal.getId());
        insertTerminalUploadLog(uploadLog);

        assertEquals(1, service.delete(uploadLog.getId()));
    }

    @Test
    public void noticeUpload() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        TerminalUploadLog uploadLog = newTerminalUploadLog(agent.getId(), terminal.getId());
        uploadLog.setQueryLogTime(new Date());
        insertTerminalUploadLog(uploadLog);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String logTime = sdf.format(uploadLog.getQueryLogTime());
        assertTrue(service.noticeUpload(uploadLog, logTime).isSuccess());
    }
}
