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
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class BatteryOrderServiceTest extends BaseJunit4Test {
    final String suffix = DateFormatUtils.format(new Date(), "yyyyww");

    @Autowired
    BatteryOrderService service;
    @Autowired
    CustomerService customerService;
    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        assertNotNull(service.find(batteryOrder.getId()));

    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        assertTrue(1 == service.findPage(batteryOrder).getTotalItems());
        assertTrue(1 == service.findPage(batteryOrder).getResult().size());
    }

    @Test
    public void findPageForbalance() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        insertBatteryOrder(batteryOrder);

        assertTrue(1 == service.findPageForbalance(batteryOrder).getTotalItems());
        assertTrue(1 == service.findPageForbalance(batteryOrder).getResult().size());
    }

    @Test
    public void findOrderCount() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        batteryOrder.setId("1");
        insertBatteryOrder(batteryOrder);

        assertTrue(1 == service.findOrderCount(agent.getId(), "0", "3", batteryOrder.getTakeCabinetId()));
    }

    @Test
    public void findDateOrderCount() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);


        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        assertTrue(1 == service.findDateOrderCount(agent.getId(), "1", "3", batteryOrder.getTakeCabinetId()).size());
    }

    @Test
    public void complete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        assertTrue(service.complete(batteryOrder.getId()).isSuccess());
    }

    //测试退租清空格口
    @Test
    public void complete2() {
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

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
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

        customerService.update(customer);

        assertTrue(service.complete(batteryOrder.getId()).isSuccess());

    }

    @Test
    public void exchangeBattery() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        assertTrue(service.exchangeBattery(batteryOrder.getId(), batteryOrder.getBatteryId()).isSuccess());
    }

    @Test
    public void toBackBatteryOrder() {
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

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "1");
        insertCabinetBox(subcabinetBox);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder.setPutBoxNum(subcabinetBox.getBoxNum());

        insertBatteryOrder(batteryOrder);

        assertTrue(service.toBackBatteryOrder(batteryOrder.getId()).isSuccess());

    }


}
