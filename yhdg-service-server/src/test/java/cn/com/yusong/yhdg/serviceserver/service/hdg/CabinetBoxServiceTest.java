package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CabinetBoxServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetBoxService cabinetBoxService;

    @Test
    public void clearLockTime() {
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
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL_LOCK.getValue());
        subcabinetBox.setLockTime(DateUtils.addDays(new Date(), -1));
        insertCabinetBox(subcabinetBox);

        CabinetBox subcabinetBox2 = newCabinetBox(cabinet.getId(),"2");
        subcabinetBox2.setBoxStatus(CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
        subcabinetBox2.setLockTime(DateUtils.addDays(new Date(), -1));
        insertCabinetBox(subcabinetBox2);

        cabinetBoxService.clearLockTime();

        assertEquals(CabinetBox.BoxStatus.FULL.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox.getCabinetId(), subcabinetBox.getBoxNum()));
        assertEquals(CabinetBox.BoxStatus.EMPTY.getValue(), jdbcTemplate.queryForInt("select box_status from hdg_cabinet_box where cabinet_id = ? and box_num = ?", subcabinetBox2.getCabinetId(), subcabinetBox2.getBoxNum()));
    }
}
