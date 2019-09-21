package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic.CustomerForegiftOrderController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/6.
 */

public class CustomerForegiftOrderServiceTest extends BaseJunit4Test {

    static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CustomerForegiftOrderServiceTest.class);
    //static Logger log = LogManager.

    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertNotNull(customerForegiftOrderService.find(entity.getId()));
    }

    /**
     * 押金多通道支付(使用优惠券购买套餐)
     */
    @Test
    public void payByMulti_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        //押金券
        CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(foregiftTicket);
        //租金券
        CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
        rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(rentTicket);
        //抵扣券
        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(deductionTicket);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByMulti(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0, exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(), 0,  packetPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId(), ConstEnum.PayType.WEIXIN);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        //判断多通道订单
        Map map = (Map) restResult.getData();
        assertNotNull(map);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(packetPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));

        assertEquals(CustomerMultiOrder.Status.NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order"));
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select source_type from bas_customer_multi_order_detail");
        assertEquals(2, list.size());
        int foregift=0;
        int packet=0;
        for(Map map1:list){
            if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.HDGFOREGIFT.getValue()){
                foregift=1;
            }else if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue()){
                packet=1;
            }
        }
        assertEquals(1, foregift);
        assertEquals(1, packet);
    }

    /**
     * 押金第三方支付(使用优惠券购买套餐)
     */
    @Test
    public void payByThird_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        //押金券
        CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(foregiftTicket);
        //租金券
        CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
        rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(rentTicket);
        //抵扣券
        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(deductionTicket);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);


        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByThird(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0, exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(), 0,  packetPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId(),ConstEnum.PayType.WEIXIN);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(packetPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(packetPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));
    }

    /**
     * 押金第三方支付(不使用优惠券购买套餐)
     */

    @Test
    public void payByThird_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);


        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByThird(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,  exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(), 0, packetPeriodPrice.getPrice(),
                0, 0, 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(packetPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100L, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100L, jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));
    }

    /**
     * 押金第三方支付(不购买套餐)
     */

    @Test
    public void payByThird_3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByThird(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0, exchangeBatteryForegift.getMoney(),
                0, 0, 0, 0,
                0, 0, 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));

    }


    /**
     * 活动套餐 + 优惠券
     */

    @Test
    public void payByThird_4() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);


        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);


        PacketPeriodActivity activity = newPacketPeriodActivity(agent.getId(), agentBatteryType.getBatteryType());
        insertPacketPeriodActivity(activity);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByThird(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,  exchangeBatteryForegift.getMoney(),
                activity.getId(), 0, 0, 1000,
                0, 0, 0,  ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(packetPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));

    }


    /**
     * vip
     */

    @Test
    public void payByThird_5() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        vipExchangeBatteryForegift.setReduceMoney(10);
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), exchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        //减免
        int foregiftPrice = exchangeBatteryForegift.getMoney() - vipExchangeBatteryForegift.getReduceMoney();
        // 不使用优惠券
        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payByThird(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), vipExchangeBatteryForegift.getId(),  foregiftPrice,
                0, 0, vipPacketPeriodPrice.getId(), vipPacketPeriodPrice.getPrice(),
                0, 0, 0,  ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(packetPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(foregiftPrice, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));

    }

    /**
     * 押金余额支付(使用优惠券购买套餐)
     */
    @Test
    public void payBalance_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        //押金券
        CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(foregiftTicket);
        //租金券
        CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
        rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(rentTicket);
        //抵扣券
        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(deductionTicket);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(0);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(), 0,packetPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId());
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));

        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", deductionTicket.getId()));

    }

    /**
     * 押金余额支付(不使用优惠券购买套餐)
     */

    @Test
    public void payBalance_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(),0, exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(),0, packetPeriodPrice.getPrice(),
                0, 0, 0);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - exchangeBatteryForegift.getMoney() - packetPeriodPrice.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(exchangeBatteryForegift.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(exchangeBatteryForegift.getMoney().intValue(), jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));


    }

    /**
     * 押金余额支付(不购买套餐)
     */

    @Test
    public void payBalance_3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,exchangeBatteryForegift.getMoney(),
                0, 0, 0,0,
                0, 0, 0);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - exchangeBatteryForegift.getMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
    }

    /**
     * 押金余额支付(余额不足)
     */

    @Test
    public void payBalance_4() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        


        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(10000);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        packetPeriodPrice.setPrice(10000);
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,exchangeBatteryForegift.getMoney(),
                0, packetPeriodPrice.getId(), 0,packetPeriodPrice.getPrice(),
                0, 0, 0);
        assertEquals(2, restResult.getCode());

    }


    /**
     * 购买活动套餐
     */
    @Test
    public void payBalance_5() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        PacketPeriodActivity activity = newPacketPeriodActivity(agent.getId(), agentBatteryType.getBatteryType());
        insertPacketPeriodActivity(activity);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), 0,exchangeBatteryForegift.getMoney(),
                activity.getId(), 0, 0,packetPeriodPrice.getPrice(),
                0, 0, 0);
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - exchangeBatteryForegift.getMoney() - packetPeriodPrice.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));

    }

    /**
     * vip套餐
     */
    @Test
    public void payBalance_6() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        
        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        VipPrice vipPrice = newVipPrice(agent.getId(), systemBatteryType.getId());
        insertVipPrice(vipPrice);

        VipExchangeBatteryForegift vipExchangeBatteryForegift = newVipExchangeBatteryForegift(agent.getId(), exchangeBatteryForegift.getId().longValue(), vipPrice.getId());
        vipExchangeBatteryForegift.setReduceMoney(10);
        insertVipExchangeBatteryForegift(vipExchangeBatteryForegift);

        VipPacketPeriodPrice vipPacketPeriodPrice = newVipPacketPeriodPrice(vipExchangeBatteryForegift.getId(),vipPrice.getId(), agent.getId(), exchangeBatteryForegift.getId().longValue());
        insertVipPacketPeriodPrice(vipPacketPeriodPrice);

        //减免
        int foregiftPrice = exchangeBatteryForegift.getMoney() - vipExchangeBatteryForegift.getReduceMoney();

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);
        RestResult restResult = customerForegiftOrderService.payBalance(customer.getId(), agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), vipExchangeBatteryForegift.getId(), foregiftPrice,
                0, 0, vipPacketPeriodPrice.getId(),packetPeriodPrice.getPrice(),
                0, 0, 0);
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - foregiftPrice - packetPeriodPrice.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(foregiftPrice, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));

    }

    /**
     * 分期付
     */
    @Test
    public void payInstallment() {
       Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);


        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        exchangeBatteryForegift.setMoney(100000);
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Station station = newStation(agent.getId());
        insertStation(station);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        //押金券
        CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(foregiftTicket);
        //租金券
        CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
        rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(rentTicket);
        //抵扣券
        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(deductionTicket);

        //标准分期
        ExchangeInstallmentSetting exchangeInstallmentSetting = newExchangeInstallmentSetting(agent.getId());
        insertExchangeInstallmentSetting(exchangeInstallmentSetting);

        ExchangeInstallmentCabinet exchangeInstallmentCabinet =newExchangeInstallmentCabinet(exchangeInstallmentSetting.getId(),cabinet.getId(),cabinet.getCabinetName());
        insertExchangeInstallmentCabinet(exchangeInstallmentCabinet);

        ExchangeInstallmentCustomer exchangeInstallmentCustomer =newExchangeInstallmentCustomer(exchangeInstallmentSetting.getId(),customer.getId(),customer.getFullname(),customer.getMobile());
        insertExchangeInstallmentCustomer(exchangeInstallmentCustomer);


        ExchangeInstallmentStation exchangeInstallmentStation = newExchangeInstallmentStation(exchangeInstallmentSetting.getId(),station.getId(),station.getStationName());
        insertExchangeInstallmentStation(exchangeInstallmentStation);

        ExchangeInstallmentCount exchangeInstallmentCount =newExchangeInstallmentCount(exchangeInstallmentSetting.getId());
        insertExchangeInstallmentCount(exchangeInstallmentCount);

        //自定义分期
        ExchangeInstallmentSetting exchangeInstallmentSetting1 = newExchangeInstallmentSetting(agent.getId());
        exchangeInstallmentSetting1.setSettingType(ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue());
        exchangeInstallmentSetting1.setMobile("18974684549");
        insertExchangeInstallmentSetting(exchangeInstallmentSetting1);

        ExchangeInstallmentCustomer exchangeInstallmentCustomer1 =newExchangeInstallmentCustomer(exchangeInstallmentSetting1.getId(),customer.getId(),customer.getFullname(),customer.getMobile());
        insertExchangeInstallmentCustomer(exchangeInstallmentCustomer1);

        ExchangeInstallmentCabinet exchangeInstallmentCabinet1 =newExchangeInstallmentCabinet(exchangeInstallmentSetting1.getId(),cabinet.getId(),cabinet.getCabinetName());
        insertExchangeInstallmentCabinet(exchangeInstallmentCabinet1);

        ExchangeInstallmentStation exchangeInstallmentStation1 = newExchangeInstallmentStation(exchangeInstallmentSetting1.getId(),station.getId(),station.getStationName());
        insertExchangeInstallmentStation(exchangeInstallmentStation1);

        ExchangeInstallmentCount exchangeInstallmentCount1 =newExchangeInstallmentCount(exchangeInstallmentSetting1.getId());
        insertExchangeInstallmentCount(exchangeInstallmentCount1);

        ExchangeInstallmentCountDetail exchangeInstallmentCountDetail =newExchangeInstallmentCountDetail(exchangeInstallmentCount1.getId());
        insertExchangeInstallmentCountDetail(exchangeInstallmentCountDetail);


        CustomerForegiftOrderController.BatteryCreateByInstallmentParam.ExchangeInstallmentCountDetail [] list=null;
        RestResult restResult = customerForegiftOrderService.payInstallment(
                customer.getId(), exchangeInstallmentSetting.getId(), exchangeInstallmentSetting.getSettingType(),agent.getId(), station.getId(),cabinet.getId(),
                agentBatteryType.getBatteryType(), exchangeBatteryForegift.getId(), exchangeBatteryForegift.getMoney(),packetPeriodPrice.getId(), packetPeriodPrice.getPrice(),
                deductionTicket.getId(),foregiftTicket.getId(),rentTicket.getId(),
                deductionTicket.getMoney(),foregiftTicket.getMoney(),rentTicket.getMoney(),exchangeInstallmentCount.getId().intValue(),list
                );
        assertEquals(0, restResult.getCode());

        assertEquals(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_installment_record_pay_detail"));
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));

    }

    @Test
    public void updateRefund() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        customerForegiftOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
        customerForegiftOrder.setAgentId(agent.getId());
        customerForegiftOrder.setPartnerId(partner.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        customerForegiftOrder.setPrice(2000);
        customerForegiftOrder.setMoney(2000);
        customerForegiftOrder.setCustomerId(customer.getId());
        customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        customerForegiftOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());
        customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        customerForegiftOrder.setCreateTime(new Date());
        customerForegiftOrder.setPayTime(new Date());
        customerForegiftOrder.setConsumeDepositBalance(0);
        customerForegiftOrder.setConsumeGiftBalance(0);
        customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        int flag = customerForegiftOrderService.insert(customerForegiftOrder);

        CustomerForegiftOrder customerForegiftOrder1 = new CustomerForegiftOrder();
        customerForegiftOrder1.setHandleTime(new Date());
        customerForegiftOrder1.setStatus(2);
        customerForegiftOrder1.setId(customerForegiftOrder.getId());

        int effect = customerForegiftOrderService.updateRefund(customerForegiftOrder.getId(),
                new Date(), "申请退还电池押金",
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue(),
                CustomerForegiftOrder.Status.PAY_OK.getValue());

        assertEquals(1, effect);
    }

    //测试拉新按次
    @Test
    public void handleLaxinCustomer_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxin.setLaxinMoney(100);
        laxin.setMobile("aaaaaaa");
        insertLaxin(laxin);

        Customer laxinCustomerAccount = newCustomer(partner.getId());
        laxinCustomerAccount.setMobile("aaaaaaa");
        insertCustomer(laxinCustomerAccount);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(null);
        insertLaxinCustomer(laxinCustomer);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        laxinSetting.setType(LaxinSetting.Type.REGISTER.getValue());
        insertLaxinSetting(laxinSetting);

        customerForegiftOrderService.handleLaxinCustomer(agent, customer, 100, 100, new Date(), new Date());

        assertEquals(100, jdbcTemplate.queryForInt("select laxin_money from bas_laxin_record where laxin_id = ? and target_customer_id = ?", laxin.getId(), customer.getId()));
    }

    //测试拉新按月
    @Test
    public void handleLaxinCustomer_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        laxin.setIncomeType(Laxin.IncomeType.MONTH.getValue());
        laxin.setPacketPeriodMoney(100);
        laxin.setPacketPeriodMonth(10);
        insertLaxin(laxin);

        LaxinCustomer laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
        laxinCustomer.setTargetMobile(customer.getMobile());
        laxinCustomer.setForegiftTime(null);
        insertLaxinCustomer(laxinCustomer);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        laxinSetting.setType(LaxinSetting.Type.REGISTER.getValue());
        insertLaxinSetting(laxinSetting);

        customerForegiftOrderService.handleLaxinCustomer(agent, customer, 100, 100, new Date(), new Date());
    }


    @Test
    public void payInstallment1() throws Exception {
        int a =2000;
        int b = 3;
        int i = a / b;
        int c=1;
        int n =0;
        for (int j = 1; j <= b; j++) {
            if(c==b){
                n+=a-(a/b)*(b-1);
            }else{
                n+=a/b;
            }
            c++;
        }

        System.out.println(i);
        System.out.println(n);
    }

}
