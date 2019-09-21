package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CabinetServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetService cabinetService;

    @Test
    public void refreshOnline() {
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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(),"1");
        subcabinetBox.setIsOnline(1);
        insertCabinetBox(subcabinetBox);

        CabinetOnlineStats onlineStats = newSubcabinetOnlineStats(cabinet.getId());
        onlineStats.setBeginTime(new Date());
        onlineStats.setEndTime(null);
        insertSubcabinetOnlineStats(onlineStats);

        cabinetService.refreshOnline();

        assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_online from hdg_cabinet where id = ?", cabinet.getId()));
        assertEquals(ConstEnum.Flag.FALSE.getValue(), jdbcTemplate.queryForInt("select is_online from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));
    }

    @Test
    public void refreshOfflineMessageTime() {
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

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(),"1");
        subcabinetBox.setIsOnline(0);
        insertCabinetBox(subcabinetBox);

        cabinetService.refreshOfflineMessageTime();
        assertNotNull(jdbcTemplate.queryForMap("select offline_fault_log_id from hdg_cabinet where id = ?", cabinet.getId()).get("offline_fault_log_id"));

    }

}
