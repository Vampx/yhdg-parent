package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chen on 2017/5/17.
 */
public class TerminalUpgradePackDetailServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        terminal.setId(cabinet.getId());
        insertTerminal(terminal);

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        TerminalUpgradePackDetail terminalUpgradePackDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(), terminal.getId());
        insertTerminalUpgradePackDetail(terminalUpgradePackDetail);

        assertNotNull(terminalUpgradePackDetailService.find(terminalUpgradePack.getId(), cabinet.getId()));
    }

}
