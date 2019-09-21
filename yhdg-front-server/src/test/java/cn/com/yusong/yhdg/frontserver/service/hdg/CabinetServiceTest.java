package cn.com.yusong.yhdg.frontserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalGroup;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.frontserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetService service;

    @Test
    public void findTerminal() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
        insertExchangePriceTime(exchangePriceTime);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        assertNotNull(service.findTerminal(terminal.getId()));
    }
}
