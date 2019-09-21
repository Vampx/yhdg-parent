package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BatteryOperateLogServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOperateLogService service;

    @Test
    public void findList() {


        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);



        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        BatteryOperateLog operateLog = newBatteryOperateLog(battery.getId(), battery.getCustomerId(), cabinet.getId(), cabinet.getId(), null);
        insertBatteryOperateLog(operateLog);

        List<BatteryOperateLog> list = service.findList(null, battery.getId());
        assertTrue(list.size() > 0);
    }
}
