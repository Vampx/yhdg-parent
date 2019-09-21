package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangePriceTimeTest extends BaseJunit4Test {
    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;

    @Test
    public void findByBatteryType() {
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

        CabinetBatteryType cabinetBatteryType = newCabinetBatteryType(cabinet.getId(), systemBatteryType.getId());
        insertCabinetBatteryType(cabinetBatteryType);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
        insertExchangePriceTime(exchangePriceTime);

        assertNotNull(exchangePriceTimeService.findByBatteryType(agent.getId(), systemBatteryType.getId()));
    }


}
