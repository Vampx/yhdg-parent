package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TerminalUpgradePackDetailServiceTest extends BaseJunit4Test {
    @Autowired
    private TerminalUpgradePackDetailService service;

    private TerminalUpgradePackDetail terminalUpgradePackDetail;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
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

        terminalUpgradePackDetail = newUpgradePackTerminalDetail(terminalUpgradePack.getId(), terminal.getId());
    }

    @Test
    public void findPage() {
        insertTerminalUpgradePackDetail(terminalUpgradePackDetail);

        assertTrue(1 == service.findPage(terminalUpgradePackDetail).getTotalItems());
        assertTrue(1 == service.findPage(terminalUpgradePackDetail).getResult().size());
    }

    @Test
    public void create() {
        String[] ids = {terminalUpgradePackDetail.getTerminalId()};
        assertTrue(service.create(terminalUpgradePackDetail.getUpgradePackId(), ids).isSuccess());
    }

    @Test
    public void delete() {
        insertTerminalUpgradePackDetail(terminalUpgradePackDetail);

        assertTrue(service.delete(terminalUpgradePackDetail.getUpgradePackId(), terminalUpgradePackDetail.getTerminalId()).isSuccess());
    }
}