package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryService batteryService;

    @Test
    public void getBatteryTypeName() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        assertNotNull(batteryService.getBatteryTypeName(systemBatteryType.getId()));
    }

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
        assertNotNull(batteryService.find(battery.getId()));
    }

    //查询门店库存电池
    @Test
    public void shopStoreList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(agent.getId(), shop.getId(), battery.getId());
        insertShopStoreBattery(shopStoreBattery);

        assertEquals(1, batteryService.shopStoreList(shop.getId(), null, 0, 100).size());
    }

    //查询门店客户使用电池
    @Test
    public void shopCustomerUseList() {
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

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), null);
        insertCustomerExchangeBattery(customerExchangeBattery);

        assertEquals(1, batteryService.shopCustomerUseList(shop.getId(), null, 0, 100).size());
    }

    //查询门店柜子电池
    @Test
    public void shopCabinetList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setShopId(shop.getId());
        insertCabinet(cabinet);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCabinetId(cabinet.getId());
        insertBattery(battery);

        assertEquals(1, batteryService.shopCabinetList(shop.getId(), null, 0, 100).size());
    }

    @Test
    public void updateShopStoreBattery() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        assertNotNull(batteryService.updateShopStoreBattery(battery.getId(), shop.getId(), battery, agent, shop));
    }

    @Test
    public void switchShopBattery1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery1.setCustomerId(customer.getId());
        insertBattery(battery1);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery1.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        battery1.setOrderId(batteryOrder.getId());

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery1.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("456");
        battery2.setShellCode("789");
        battery2.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery2);


        ShopStoreBattery shopStoreBattery = newShopStoreBattery(battery2.getAgentId(), shop.getId(), battery2.getId());
        insertShopStoreBattery(shopStoreBattery);

        assertNotNull(batteryService.switchShopBattery(shop.getId(), battery1.getShellCode(), battery2.getShellCode()));
    }

    @Test
    public void switchShopBattery2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCategory(Battery.Category.RENT.getValue());
        battery1.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery1.setCustomerId(customer.getId());
        insertBattery(battery1);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery1.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        battery1.setOrderId(batteryOrder.getId());

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery1.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        Battery battery2 = newBattery(agent.getId(), systemBatteryType.getId());
        battery2.setId("456");
        battery2.setShellCode("789");
        battery2.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        insertBattery(battery2);


        ShopStoreBattery shopStoreBattery = newShopStoreBattery(battery2.getAgentId(), shop.getId(), battery2.getId());
        insertShopStoreBattery(shopStoreBattery);

        assertNotNull(batteryService.switchShopBattery(shop.getId(), battery1.getShellCode(), battery2.getShellCode()));
    }

    @Test
    public void backBattery1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setShopId(shop.getId());
        insertCabinet(cabinet);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCategory(Battery.Category.EXCHANGE.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setCustomerId(customer.getId());
        battery.setCabinetId(cabinet.getId());
        insertBattery(battery);

        CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery.getId(), battery.getType());
        insertCustomerRentBattery(CustomerRentBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery.getId(), customer.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        String[] batteryId = new String[] {battery.getId()};

        assertEquals(0, batteryService.backBattery(batteryId,100,agent,shop, user).getCode());

    }

    @Test
    public void backBattery2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Role role = newRole(agent.getId());
        insertRole(role);

        Dept dept = newDept(agent.getId());
        insertDept(dept);

        User user = newUser(agent.getId(), role.getId(), dept.getId());
        insertUser(user);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setShopId(shop.getId());
        insertCabinet(cabinet);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCategory(Battery.Category.RENT.getValue());
        battery1.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery1.setCustomerId(customer.getId());
        battery1.setCabinetId(cabinet.getId());
        insertBattery(battery1);

        String orderId = newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        RentOrder rentOrder = newRentOrder(orderId, partner.getId(), agent.getId(), shop.getId(), customer.getId(), battery1.getId(), systemBatteryType.getId());
        insertRentOrder(rentOrder);

        CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery1.getId(), battery1.getType());
        CustomerRentBattery.setRentOrderId(rentOrder.getId());
        insertCustomerRentBattery(CustomerRentBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerRentInfo.setBalanceShopId(shop.getId());
        insertCustomerRentInfo(customerRentInfo);

        String[] batteryId = new String[] {battery1.getId()};

        assertEquals(0, batteryService.backBattery(batteryId,100,agent,shop, user).getCode());

    }

    @Test
    public void countShopCabinetBattery() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        insertAgentSystemConfig(agentSystemConfig);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        cabinet.setShopId(shop.getId());
        insertCabinet(cabinet);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setCategory(Battery.Category.RENT.getValue());
        battery1.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery1.setCustomerId(customer.getId());
        battery1.setCabinetId(cabinet.getId());
        insertBattery(battery1);

        int count = batteryService.countShopCabinetBattery(shop.getId());

        assertEquals(1, count);
    }

    @Test
    public void countShopCustomerUseNum() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerAgentBalance customerAgentBalance = newCustomerAgentBalance(customer.getId(), agent.getId());
        insertCustomerAgentBalance(customerAgentBalance);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery1);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery1.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery1.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setBalanceShopId(shop.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        battery1.setId("456");
        insertBattery(battery1);

        CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery1.getId(), battery1.getType());
        insertCustomerRentBattery(CustomerRentBattery);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        rentForegiftOrder.setMoney(160);
        rentForegiftOrder.setDeductionTicketMoney(100);
        insertRentForegiftOrder(rentForegiftOrder);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerRentInfo.setBalanceShopId(shop.getId());
        insertCustomerRentInfo(customerRentInfo);

        int num2 = batteryService.countShopCustomerUseNum(shop.getId());

        assertEquals(2, num2);

    }
}
