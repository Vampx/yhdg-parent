package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import cn.com.yusong.yhdg.staticserver.entity.result.RestResult;
import cn.com.yusong.yhdg.staticserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.staticserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/17.
 */
public class BatteryOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOrderService batteryOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(battery.getId(), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        assertNotNull(batteryOrderService.find(batteryOrder.getId()));
    }

    @Test
    public void createNewOrder() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, null);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from hdg_battery_order"));
    }
}
