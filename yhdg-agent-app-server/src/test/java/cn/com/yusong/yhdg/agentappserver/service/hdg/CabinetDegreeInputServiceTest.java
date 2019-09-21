package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetDegreeInputServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetDegreeInputService service;

    @Test
    public void find() {
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

        assertEquals(0,service.create(cabinet.getId(),1000, 500,"xxx").getCode());

        assertEquals(100000L - 500L, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(estate.getBalance() + 500L, jdbcTemplate.queryForInt("select balance from hdg_estate where id = ?", estate.getId()));

    }


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

        assertTrue(1 == service.findListByEstate(cabinet.getAgentId(), estate.getId(), 0, 20).size());

    }

}
