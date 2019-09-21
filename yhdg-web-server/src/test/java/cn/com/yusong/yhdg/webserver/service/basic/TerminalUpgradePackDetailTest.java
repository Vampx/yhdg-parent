package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalUpgradePackDetailTest extends BaseJunit4Test {

    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setId(terminal.getId());
        insertCabinet(cabinet);

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        TerminalUpgradePackDetail packDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(), cabinet.getId());
        insertTerminalUpgradePackDetail(packDetail);

        assertTrue(1 == terminalUpgradePackDetailService.findPage(packDetail).getTotalItems());
        assertTrue(1 == terminalUpgradePackDetailService.findPage(packDetail).getResult().size());
    }

    @Test
    public void create() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),  terminal.getId());
        cabinet.setId(terminal.getId());
        insertCabinet(cabinet);

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        String [] ids = {terminal.getId()};
        assertTrue(terminalUpgradePackDetailService.create(terminalUpgradePack.getId(), ids).isSuccess());

        TerminalUpgradePackDetail packDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(),cabinet.getId());
        assertEquals(1, terminalUpgradePackDetailService.findPage(packDetail).getResult().size());
    }

    @Test
    public void delete() {
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

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        TerminalUpgradePackDetail terminalUpgradePackDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(), cabinet.getId());
        terminalUpgradePackDetail.setTerminalId(terminal.getId());
        insertTerminalUpgradePackDetail(terminalUpgradePackDetail);

        assertTrue(terminalUpgradePackDetailService.delete(terminalUpgradePack.getId(), terminal.getId()).isSuccess());
        assertEquals(0, terminalUpgradePackDetailService.findPage(terminalUpgradePackDetail).getResult().size());
    }
}
