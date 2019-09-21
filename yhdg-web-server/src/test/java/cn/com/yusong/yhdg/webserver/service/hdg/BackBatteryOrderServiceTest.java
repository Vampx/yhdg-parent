package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class BackBatteryOrderServiceTest extends BaseJunit4Test {
    final String suffix = DateFormatUtils.format(new Date(), "yyyyww");

    @Autowired
    BackBatteryOrderService service;

    @Test
    public void findPage(){
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

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);



        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "0-1", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);

        assertNotNull(service.find(backOrder.getId()));

        assertTrue(1 == service.findPage(backOrder).getTotalItems());
        assertTrue(1 == service.findPage(backOrder).getResult().size());
    }
    @Test
    public void find(){
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

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);



        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "0-1");
        insertCabinetBox(cabinetBox);

        CabinetCode subcabinetCode = newCabinetCode();
        subcabinetCode.setId("10086");
        subcabinetCode.setCode(cabinet.getId());
        insertCabinetCode(subcabinetCode);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        BackBatteryOrder backOrder = newBackBatteryOrder(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER), agent.getId(),
                cabinet.getId(), "0-1", battery.getId(), customer.getId());
        insertBackBatteryOrder(backOrder);

        assertNotNull(service.find(backOrder.getId()));

        assertNotNull(service.find(backOrder.getId()));
    }
}
