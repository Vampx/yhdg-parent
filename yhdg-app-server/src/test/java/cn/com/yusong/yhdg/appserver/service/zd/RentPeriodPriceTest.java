package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RentPeriodPriceTest extends BaseJunit4Test {
    @Autowired
    RentPeriodPriceService rentPeriodPriceService;

    @Test
    public void findByAgent() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentBatteryType rentBatteryType = newRentBatteryType(agent.getId(), systemBatteryType.getId());
        insertRentBatteryType(rentBatteryType);

        RentBatteryForegift rentBatteryForegift = newRentBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertRentBatteryForegift(rentBatteryForegift);

        RentPeriodPrice rentPeriodPrice = newRentPeriodPrice(agent.getId(), rentBatteryForegift.getId());
        insertRentPeriodPrice(rentPeriodPrice);

        assertNotNull(rentPeriodPriceService.findList(agent.getId(), systemBatteryType.getIsActive(), rentBatteryForegift.getId()));
    }

}
