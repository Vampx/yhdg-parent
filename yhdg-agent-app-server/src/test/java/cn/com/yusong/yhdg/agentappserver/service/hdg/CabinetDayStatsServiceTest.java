package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetDayStatsServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetDayStatsService service;


    @Test
    public void findListByEstate() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findByCabinetList(agent.getId(), cabinet.getId(), null,null, null,0,20).size());

    }

    @Test
    public void findTotalCabinetStatsList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findTotalCabinetStatsList(agent.getId(), "2017-01-01","2017-01-01", null,0,20).size());

    }

    @Test
    public void findTotalStatsListByCabinetId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertNotNull(service.findTotalStatsListByCabinetId(agent.getId(), cabinet.getId(),"2017-01-01","2017-01-01"));

    }

    @Test
    public void findListByCabinetId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findListByCabinetId(agent.getId(), cabinet.getId()).size());

    }


    @Test
    public void findForCabinet() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertNotNull(service.findForCabinet(cabinet.getId(),"2017-01-01"));

    }


    @Test
    public void findForStats() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertTrue(1 == service.findForStats(agent.getId(), "2017-01-01",null,0,10).size());

    }


    @Test
    public void findTotalByStats() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        agent.setBalance(100000);
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Estate estate = newEstate(agent.getId());
        insertEstate(estate);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());
        cabinet.setEstateId(estate.getId());
        insertCabinet(cabinet);

        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(), "2012-12-12");
        cabinetDayDegreeStats.setCabinetName("cabinetName");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);

        CabinetDegreeInput cabinetDegreeInput = newCabinetDegreeInput(agent.getId(), cabinet.getId(), estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        CabinetDayStats cabinetDayStats = newCabinetDayStats("2017-01-01", agent.getId(), cabinet.getId());
        insertCabinetDayStats(cabinetDayStats);

        assertNotNull(service.findTotalByStats(agent.getId(),"2017-01-01"));

    }
}
