package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FaultLogServiceTest extends BaseJunit4Test {
    @Autowired
    FaultLogService service;

    @Test
    public void findByBatteryId() {
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

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setBatteryId(battery.getId());
        insertFaultLog(faultLog);

        assertTrue(1 == service.findByBatteryId(agent.getId(), battery.getId(), 0, 10).size());

    }

    @Test
    public void findByCabinetId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        FaultLog faultLog = newFaultLog(agent.getId());
        faultLog.setCabinetId(cabinet.getId());
        insertFaultLog(faultLog);

        assertTrue(1 == service.findByCabinetId(agent.getId(), cabinet.getId(), 0, 10).size());

    }
}
