package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
public class BackBatteryOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BackBatteryOrderService backBatteryOrderService;

    @Autowired
    OrderIdService orderIdService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
        insertExchangePriceTime(exchangePriceTime);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "001");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        BackBatteryOrder backBatteryOrder = newBackBatteryOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER)
                , agent.getId(), cabinet.getId(), subcabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBackBatteryOrder(backBatteryOrder);
        assertNotNull(backBatteryOrderService.find(backBatteryOrder.getId()));

    }


    @Test
    public void cancelOrder() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
        insertExchangePriceTime(exchangePriceTime);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "0001");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        BackBatteryOrder backBatteryOrder = newBackBatteryOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER)
                , agent.getId(), cabinet.getId(), subcabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBackBatteryOrder(backBatteryOrder);

        RestResult restResult = backBatteryOrderService.cancelOrder(backBatteryOrder.getId(), customer.getId());

        System.out.println("error==" + restResult.getMessage());

        assertEquals(0, restResult.getCode());
    }

    @Test
    public void createOrder() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);


        ExchangePriceTime exchangePriceTime = newExchangePriceTime(agent.getId(), systemBatteryType.getId());
        insertExchangePriceTime(exchangePriceTime);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "0001");

        insertCabinetBox(subcabinetBox);

        CabinetBox subcabinetBox2 = newCabinetBox(cabinet.getId(), "0002");
        insertCabinetBox(subcabinetBox2);

        RestResult restResult =
                backBatteryOrderService.createOrder(cabinet.getId(), customer.getId());

        System.out.println("error==" + restResult.getMessage());

        assertEquals(0, restResult.getCode());
    }
}
