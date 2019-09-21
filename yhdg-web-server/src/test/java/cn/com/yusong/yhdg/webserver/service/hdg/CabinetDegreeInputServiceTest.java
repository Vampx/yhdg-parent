package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.entity.Strategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public class CabinetDegreeInputServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetDegreeInputService cabinetDegreeInputService;

    @Test
    public void findPage() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Playlist playlist =newPlaylist(agent.getId());
        insertPlaylist(playlist);
        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        Terminal terminal=newTerminal(agent.getId(),terminalStrategy.getId(),playlist.getId());
        insertTerminal(terminal);
        Cabinet cabinet =newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(),"2019-08-03");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);
        CabinetDegreeInput cabinetDegreeInput =newCabinetDegreeInput(agent.getId(),cabinet.getId(),estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        if(cabinetDegreeInput.getBeginTime()!=null){
            cabinetDegreeInput.setBeginTime(getBeginDate(cabinetDegreeInput.getBeginTime()));
        }
        Page page = cabinetDegreeInputService.findPage(cabinetDegreeInput);
        int totalItems = page.getTotalItems();
        assertTrue(1 == totalItems);

    }

    @Test
    public void findByCabinetId() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Playlist playlist =newPlaylist(agent.getId());
        insertPlaylist(playlist);
        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        Terminal terminal=newTerminal(agent.getId(),terminalStrategy.getId(),playlist.getId());
        insertTerminal(terminal);
        Cabinet cabinet =newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(),"2019-08-03");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);
        CabinetDegreeInput cabinetDegreeInput =newCabinetDegreeInput(agent.getId(),cabinet.getId(),estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        if(cabinetDegreeInput.getBeginTime()!=null){
            cabinetDegreeInput.setBeginTime(getBeginDate(cabinetDegreeInput.getBeginTime()));
        }
        Map<String, Object> byCabinetId = cabinetDegreeInputService.findByCabinetId(cabinet.getId());
        assertNotNull(byCabinetId.get("cabinetId"));

    }

    @Test
    public void findVoidPage() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Playlist playlist =newPlaylist(agent.getId());
        insertPlaylist(playlist);
        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);
        Terminal terminal=newTerminal(agent.getId(),terminalStrategy.getId(),playlist.getId());
        insertTerminal(terminal);
        Cabinet cabinet =newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        Estate estate= newEstate(agent.getId());
        insertEstate(estate);
        CabinetDayDegreeStats cabinetDayDegreeStats = newCabinetDayDegreeStats(cabinet.getId(),"2019-08-03");
        insertCabinetDayDegreeStats(cabinetDayDegreeStats);
        CabinetDegreeInput cabinetDegreeInput =newCabinetDegreeInput(agent.getId(),cabinet.getId(),estate.getId());
        insertCabinetDegreeInput(cabinetDegreeInput);

        if(cabinetDegreeInput.getBeginTime()!=null){
            cabinetDegreeInput.setBeginTime(getBeginDate(cabinetDegreeInput.getBeginTime()));
        }
        assertTrue(1==cabinetDegreeInputService.findVoidPage(cabinetDegreeInput).getTotalItems());
    }
    private Date getBeginDate(Date startDate){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        startDate = calendarStart.getTime();
        return startDate;
    }

}