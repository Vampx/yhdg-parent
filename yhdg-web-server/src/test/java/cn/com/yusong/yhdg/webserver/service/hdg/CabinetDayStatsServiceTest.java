package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CabinetDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetDayStatsService service;

    @Test
    public void findPage(){
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

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findPage(cabinetDayStats).getTotalItems());
        assertTrue(1 == service.findPage(cabinetDayStats).getResult().size());
    }

    @Test
    public void findForExcel(){
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

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findForExcel(cabinetDayStats).size());
    }
}
