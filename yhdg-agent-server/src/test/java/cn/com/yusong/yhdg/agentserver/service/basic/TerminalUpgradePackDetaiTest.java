package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TerminalUpgradePackDetaiTest extends BaseJunit4Test {

    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Test
    public void testFindPage() {
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

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        TerminalUpgradePackDetail packDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(),cabinet.getId());
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

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        String [] id = {cabinet.getId()};
        ExtResult extResult = terminalUpgradePackDetailService.create(terminalUpgradePack.getId(), id);
        assertTrue(true == extResult.isSuccess());

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

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);

        TerminalUpgradePackDetail terminalUpgradePackDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(),cabinet.getId());
        insertTerminalUpgradePackDetail(terminalUpgradePackDetail);

        ExtResult extResult = terminalUpgradePackDetailService.delete(terminalUpgradePack.getId(), cabinet.getId());
        assertTrue(true == extResult.isSuccess());

    }
}
