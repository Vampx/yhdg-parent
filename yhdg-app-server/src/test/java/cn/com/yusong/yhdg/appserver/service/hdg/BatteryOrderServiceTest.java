package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/17.
 */
public class BatteryOrderServiceTest extends BaseJunit4Test {

    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CabinetBoxService cabinetBoxService;

    DriverManagerDataSource dataSource;

    Date date = new Date();
    final String suffix = DateFormatUtils.format(date, "yyyyww");
    final String tableName = "hdg_battery_order_battery_report_log_" + suffix;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        assertNotNull(batteryOrderService.find(batteryOrder.getId()));
    }

    /**
     * 校验包含预约订单的新订单
     */
    @Test
    public void createNewOrder_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "01");
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.BESPEAK.getValue());
        insertCabinetBox(cabinetBox);

        BespeakOrder bespeakOrder = newBespeakOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER)
                ,  partner.getId(), agent.getId(), cabinet.getId(), cabinetBox.getBoxNum(), battery.getId(), customer.getId());
        insertBespeakOrder(bespeakOrder);

        batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

        BespeakOrder bespeakOrder1 = bespeakOrderService.find(bespeakOrder.getId());
        int status = bespeakOrder1.getStatus();
        assertEquals(2, status);//取出

        CabinetBox cabinetBox1 = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
        status = cabinetBox1.getBoxStatus();
        assertEquals(7, status);//客户使用
    }

    /**
     * 校验包含预约订单的新订单(不是预约的格口)
     */
    @Test
    public void createNewOrder_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "01");
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL_LOCK.getValue());
        insertCabinetBox(cabinetBox);

        CabinetBox cabinetBox2 = newCabinetBox(cabinet.getId(), "02");
        cabinetBox2.setBoxStatus(CabinetBox.BoxStatus.BESPEAK.getValue());
        insertCabinetBox(cabinetBox2);

        BespeakOrder bespeakOrder = newBespeakOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER)
                ,  partner.getId(), agent.getId(), cabinet.getId(), cabinetBox2.getBoxNum(), battery.getId(), customer.getId());
        insertBespeakOrder(bespeakOrder);

        batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

        BespeakOrder bespeakOrder1 = bespeakOrderService.find(bespeakOrder.getId());
        int status = bespeakOrder1.getStatus();
        assertEquals(3, status);//他箱取出

        CabinetBox cabinetBox1 = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
        status = cabinetBox1.getBoxStatus();
        assertEquals(7, status);//客户使用

        CabinetBox cabinetBox22 = cabinetBoxService.find(cabinetBox2.getCabinetId(), cabinetBox2.getBoxNum());
        status = cabinetBox22.getBoxStatus();
        assertEquals(2, status);//满箱
    }


    @Test
    public void updateMoney() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);
        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);

        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        insertCustomerCouponTicket(customerCouponTicket);

        assertEquals(1, batteryOrderService.updateMoney(batteryOrder.getId(), ConstEnum.PayType.ALIPAY.getValue(),
                11, 11, 1, "asd", customerCouponTicket.getId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

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
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        // batteryOrderService.insert(batteryOrder);


    }

    @Test
    public void xx() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);
    }

    /**
     * 余额支付旧电费用(使用套餐)
     */
    @Test
    public void payByBalance_1() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "01");
        insertCabinetBox(cabinetBox);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setCurrentVolume(100);
        insertBatteryOrder(batteryOrder);

        batteryOrderService.payByBalance(batteryOrder.getId(), customer.getId(), 0);

        //判断价格

        Customer customer2 = customerService.find(customer.getId());
        int price = (int) (customer.getBalance() - batteryOrder.getPrice());
        long balance = customer2.getBalance();
        assertEquals(price, balance);

        //判断订单状态
        BatteryOrder batteryOrder2 = batteryOrderService.find(batteryOrderId);
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), (int) batteryOrder2.getOrderStatus());

        //判断电池状态
        Battery battery2 = batteryService.find(battery.getId());
        assertEquals(Battery.Status.IN_BOX.getValue(), (int) battery2.getStatus());
    }

    /**
     * 余额支付旧电费用(不使用套餐,使用优惠券)
     */
    @Test
    public void payByBalance_2() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        insertCustomerCouponTicket(customerCouponTicket);


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

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "01");
        insertCabinetBox(cabinetBox);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setPrice(200);
        batteryOrder.setCurrentVolume(100);
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByBalance(batteryOrder.getId(), customer.getId(),
                customerCouponTicket.getId());
        Integer price = (Integer) batteryOrder.getPrice();
        //判断价格
        Customer customer2 = customerService.find(customer.getId());
        assertEquals(customer.getBalance() - (price - customerCouponTicket.getMoney()), customer2.getBalance().intValue());

        //判断订单状态
        BatteryOrder batteryOrder2 = batteryOrderService.find(batteryOrderId);
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), (int) batteryOrder2.getOrderStatus());

        //判断电池状态
        Battery battery2 = batteryService.find(battery.getId());
        assertEquals(Battery.Status.IN_BOX.getValue(), (int) battery2.getStatus());

        //判断优惠券
        customerCouponTicket = customerCouponTicketService.find(customerCouponTicket.getId());
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), (int) customerCouponTicket.getStatus());
    }

    /**
     * 余额支付旧电费用(不使用套餐,不使用优惠券)
     */
    @Test
    public void payByBalance_3() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

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

        CabinetBox cabinetBox = newCabinetBox(cabinet.getId(), "01");
        insertCabinetBox(cabinetBox);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setCurrentVolume(100);
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByBalance(batteryOrder.getId(), customer.getId(), 0);
        Integer price = (Integer) batteryOrder.getPrice();
        //判断价格
        Customer customer2 = customerService.find(customer.getId());
        assertEquals(customer.getBalance() - price, customer2.getBalance().intValue());

        //判断订单状态
        BatteryOrder batteryOrder2 = batteryOrderService.find(batteryOrderId);
        assertEquals(BatteryOrder.OrderStatus.PAY.getValue(), (int) batteryOrder2.getOrderStatus());

        //判断电池状态
        Battery battery2 = batteryService.find(battery.getId());
        assertEquals(Battery.Status.IN_BOX.getValue(), (int) battery2.getStatus());

    }

    /**
     * 支付宝支付旧电费用(不使用套餐,使用优惠券)
     */
    @Test
    public void payByAlipay_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        insertCustomerCouponTicket(customerCouponTicket);

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

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        batteryOrder.setPrice(1000);
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByAlipay(true, batteryOrderId, customer.getId(), customerCouponTicket.getId());

        //判断返回支付宝订单
        Map<String, String> map = (Map<String, String>) restResult.getData();
        assertNotNull(map);
    }

    /**
     * 支付宝支付旧电费用(不使用套餐,不使用优惠券)
     */
    @Test
    public void payByAlipay_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

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


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByAlipay(true, batteryOrderId, customer.getId(), 0);

        //判断返回支付宝订单
        Map<String, String> map = (Map<String, String>) restResult.getData();
        assertNotNull(map);
    }

    @Test
    public void qrcodeByAlipay() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

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

//        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), 0);
//        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        customerCouponTicket.setCustomerMobile(customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setVolume(60);
        insertBattery(battery);


        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setMoney(10000);
        batteryOrder.setInitVolume(100);
        batteryOrder.setPrice(10000);
        insertBatteryOrder(batteryOrder);


        // batteryOrderService.insert(batteryOrder);


        long couponTicketId = customerCouponTicket.getId();
        String orderId = batteryOrder.getId();
        RestResult restResult = batteryOrderService.qrcodeByAlipay(true, orderId, customer.getId(), couponTicketId);
        System.out.println("message--" + restResult.getMessage());
        assertEquals(0, restResult.getCode());
    }


    /**
     * 微信支付旧电费用(不使用套餐,使用优惠券)
     */
    @Test
    public void payByWeixin_1() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        insertCustomerCouponTicket(customerCouponTicket);

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


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByWeixin(batteryOrderId, customer.getId(),
                customerCouponTicket.getId());

        //判断返回微信订单
        Map<String, String> map = (Map<String, String>) restResult.getData();

        //  assertNotNull(map);
    }

    /**
     * 微信支付旧电费用(不使用套餐,不使用优惠券)
     */
    @Test
    public void payByWeixin_2() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        insertPacketPeriodOrder(packetPeriodOrder);

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


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);


        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setPutCabinetId(cabinet.getId());
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue());
        insertBatteryOrder(batteryOrder);

        RestResult restResult = batteryOrderService.payByWeixin(batteryOrderId, customer.getId(), 0);

        //判断返回微信订单
        Map<String, String> map = (Map<String, String>) restResult.getData();
        // assertNotNull(map);
    }

    @Test
    public void findByPacketOrderId() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);


        String batteryOrderId = orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        BatteryOrder batteryOrder = newBatteryOrder(batteryOrderId, systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setMoney(10000);
        batteryOrder.setInitVolume(100);
        batteryOrder.setPrice(10000);
        insertBatteryOrder(batteryOrder);

//        PacketPeriodPrice activePacketPeriodPrice = newPacketPeriodPrice(agent.getId(), 0);
//        insertPacketPeriodPrice(activePacketPeriodPrice);
        List<BatteryOrder> list = batteryOrderService.findByPacketOrderId(batteryOrder.getId(), customer.getId(), 0,
                10);
        assertNotNull(list);
    }


    @Test
    public void getListByCustomer() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(
                "BB123456789", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setTakeTime(new Date());
        batteryOrder.setCurrentVolume(20);
        batteryOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        batteryOrder.setTicketMoney(0);
        batteryOrder.setTicketName(null);
        insertBatteryOrder(batteryOrder);
        RestResult restResult = batteryOrderService.getListByCustomer(customer.getId(), 0, 10);
        List<Map> mapList = (List<Map>) restResult.getData();
        assertEquals(0, restResult.getCode());
        assertNotNull(mapList);
        System.out.println("payTypeName==" + mapList.get(0).get("payTypeName"));
        System.out.println("ticketName==" + mapList.get(0).get("ticketName"));


    }

    @Test
    public void findBatteryReportLogByOrderId() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);


        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        BatteryOrder batteryOrder = newBatteryOrder(
                "BB123456789", systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder.setTakeTime(new Date());
        batteryOrder.setTakeCabinetId(cabinet.getId());
        batteryOrder.setCurrentVolume(20);
        batteryOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        batteryOrder.setTicketMoney(0);
        batteryOrder.setTicketName(null);
        insertBatteryOrder(batteryOrder);


        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        RestResult restResult = batteryOrderService.findBatteryReportLogByOrderId(batteryOrder.getId(), 0, 10);
        List<Map> mapList = (List<Map>) restResult.getData();
        assertEquals(0, restResult.getCode());
        assertEquals(1, mapList.size());
    }

    public JdbcTemplate getJdbcTemplate() {
        //设置数据库信息
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/yhdg_history_test");
        dataSource.setUser("root");
        dataSource.setPassword("root");

        return jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private int dropTable(JdbcTemplate jdbcTemplate) {
        StringBuffer sb = new StringBuffer("");
        sb.append("DROP TABLE if exists " + tableName);
        try {
            jdbcTemplate.update(sb.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int createTable(JdbcTemplate jdbcTemplate) {
        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TABLE if not exists `" + tableName + "` (");
        sb.append("order_id char(32) not null,");
        sb.append("report_time datetime,");
        sb.append("volume smallint(6),");
        sb.append("temp varchar(40),");
        sb.append("lng double,");
        sb.append("lat double,");
        sb.append("distance int,");
        sb.append("current_signal smallint,");
        sb.append("coordinate_type varchar(10),");
        sb.append("address varchar(40),");
        sb.append("primary key(order_id, report_time)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        try {
            jdbcTemplate.update(sb.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
