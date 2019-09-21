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
public class BespeakOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BespeakOrderService bespeakOrderService;

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
        subcabinetBox.setIsActive(1);
        subcabinetBox.setIsOnline(1);
        insertCabinetBox(subcabinetBox);

        BespeakOrder bespeakOrder = newBespeakOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER)
                ,  partner.getId(), agent.getId(), cabinet.getId(), subcabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBespeakOrder(bespeakOrder);
        assertNotNull(bespeakOrderService.find(bespeakOrder.getId()));

    }

    @Test
    public void findSuccessByCustomer() {
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

        BespeakOrder bespeakOrder = newBespeakOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER)
                ,  partner.getId(), agent.getId(), cabinet.getId(), subcabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBespeakOrder(bespeakOrder);
        assertNotNull(bespeakOrderService.findSuccessByCustomer(customer.getId()));

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


        CabinetBox subcabinetBox = newCabinetBox(cabinet.getId(), "001");
        subcabinetBox.setBoxStatus(CabinetBox.BoxStatus.BESPEAK.getValue());
        subcabinetBox.setBatteryId(battery.getId());
        insertCabinetBox(subcabinetBox);

        BespeakOrder bespeakOrder = newBespeakOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER)
                ,  partner.getId(), agent.getId(), cabinet.getId(), subcabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBespeakOrder(bespeakOrder);

        RestResult restResult = bespeakOrderService.cancelOrder(bespeakOrder.getId(), customer.getId());

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

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

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


        RestResult restResult =
                bespeakOrderService.createOrder(cabinet.getId(), subcabinetBox.getBoxNum(), customer.getId());

        System.out.println("error==" + restResult.getMessage());

        assertEquals(0, restResult.getCode());
    }
}
