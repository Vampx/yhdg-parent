package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMultiOrderDetailMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/6.
 */

public class CustomerMultiOrderServiceTest extends BaseJunit4Test {


    static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CustomerMultiOrderServiceTest.class);
    //static Logger log = LogManager.

    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;

    /**
     * 押金多渠道
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

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        insertCustomerMultiOrder(customerMultiOrder);

        RestResult restResult = customerMultiOrderService.payByThird(customer, customerMultiOrder.getId(), 200, ConstEnum.PayType.WEIXIN_MP);
        assertEquals(0, restResult.getCode());


        //判断返回微信订单
        WeixinmpPayOrder weixinmpPayOrder = (WeixinmpPayOrder) restResult.getData();
        assertNotNull(weixinmpPayOrder);
    }


    /**
     * 余额支付-换电押金
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

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setMoney(300);
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMoney(300);
        insertPacketPeriodOrder(packetPeriodOrder);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setMoney(300);
        insertInsuranceOrder(insuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setTotalMoney(customerForegiftOrder.getMoney()+packetPeriodOrder.getMoney()+insuranceOrder.getMoney());
        customerMultiOrder.setDebtMoney(customerForegiftOrder.getMoney()+packetPeriodOrder.getMoney()+insuranceOrder.getMoney());
        insertCustomerMultiOrder(customerMultiOrder);
        Integer num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());
        CustomerMultiOrderDetail customerMultiOrderDetail = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                customerForegiftOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGFOREGIFT.getValue(), customerForegiftOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail);
        CustomerMultiOrderDetail customerMultiOrderDetail2 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                packetPeriodOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue(), packetPeriodOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail2);
        CustomerMultiOrderDetail customerMultiOrderDetail3 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                insuranceOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGINSURANCE.getValue(), insuranceOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail3);

        RestResult restResult = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult.getCode());
        assertEquals(CustomerMultiOrder.Status.IN_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        RestResult restResult2 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult2.getCode());
        RestResult restResult3 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult3.getCode());

        assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id='"+customerForegiftOrder.getId()+"'"));
        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id='"+packetPeriodOrder.getId()+"'"));
        assertEquals(InsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from hdg_insurance_order where id='"+insuranceOrder.getId()+"'"));
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        assertEquals(10000L-300-300-300, jdbcTemplate.queryForInt("select balance from bas_customer where id="+customer.getId()));

    }

    /**
     * 余额支付-租电押金
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

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setMoney(300);
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setMoney(300);
        insertRentPeriodOrder(rentPeriodOrder);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setMoney(300);
        insertRentInsuranceOrder(rentInsuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setTotalMoney(rentForegiftOrder.getMoney()+rentPeriodOrder.getMoney()+rentInsuranceOrder.getMoney());
        customerMultiOrder.setDebtMoney(rentForegiftOrder.getMoney()+rentPeriodOrder.getMoney()+rentInsuranceOrder.getMoney());
        insertCustomerMultiOrder(customerMultiOrder);
        Integer num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());
        CustomerMultiOrderDetail customerMultiOrderDetail = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                rentForegiftOrder.getId(), CustomerMultiOrderDetail.SourceType.ZDFOREGIFT.getValue(), rentForegiftOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail);
        CustomerMultiOrderDetail customerMultiOrderDetail2 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                rentPeriodOrder.getId(), CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue(), rentPeriodOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail2);
        CustomerMultiOrderDetail customerMultiOrderDetail3 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                rentInsuranceOrder.getId(), CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue(), rentInsuranceOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail3);

        RestResult restResult = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult.getCode());
        assertEquals(CustomerMultiOrder.Status.IN_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        RestResult restResult2 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult2.getCode());
        RestResult restResult3 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult3.getCode());

        assertEquals(RentForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_foregift_order where id='"+rentForegiftOrder.getId()+"'"));
        assertEquals(RentPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id='"+rentPeriodOrder.getId()+"'"));
        assertEquals(RentInsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_insurance_order where id='"+rentInsuranceOrder.getId()+"'"));
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        assertEquals(10000L-300-300-300, jdbcTemplate.queryForInt("select balance from bas_customer where id="+customer.getId()));

    }

    /**
     * 余额支付-换电租金
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

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setMoney(300);
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setMoney(300);
        insertPacketPeriodOrder(packetPeriodOrder);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setMoney(300);
        insertInsuranceOrder(insuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setTotalMoney(packetPeriodOrder.getMoney()+insuranceOrder.getMoney());
        customerMultiOrder.setDebtMoney(packetPeriodOrder.getMoney()+insuranceOrder.getMoney());
        insertCustomerMultiOrder(customerMultiOrder);
        Integer num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());
        CustomerMultiOrderDetail customerMultiOrderDetail2 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                packetPeriodOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue(), packetPeriodOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail2);
        CustomerMultiOrderDetail customerMultiOrderDetail3 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                insuranceOrder.getId(), CustomerMultiOrderDetail.SourceType.HDGINSURANCE.getValue(), insuranceOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail3);

        RestResult restResult = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult.getCode());
        assertEquals(CustomerMultiOrder.Status.IN_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        RestResult restResult2 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult2.getCode());

        assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id='"+packetPeriodOrder.getId()+"'"));
        assertEquals(InsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from hdg_insurance_order where id='"+insuranceOrder.getId()+"'"));
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        assertEquals(10000L-300-300, jdbcTemplate.queryForInt("select balance from bas_customer where id="+customer.getId()));

    }

    /**
     * 余额支付-租电租金
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

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setMoney(300);
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setMoney(300);
        insertRentPeriodOrder(rentPeriodOrder);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setMoney(300);
        insertRentInsuranceOrder(rentInsuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setTotalMoney(rentPeriodOrder.getMoney()+rentInsuranceOrder.getMoney());
        customerMultiOrder.setDebtMoney(rentPeriodOrder.getMoney()+rentInsuranceOrder.getMoney());
        insertCustomerMultiOrder(customerMultiOrder);
        Integer num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());
        CustomerMultiOrderDetail customerMultiOrderDetail2 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                rentPeriodOrder.getId(), CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue(), rentPeriodOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail2);
        CustomerMultiOrderDetail customerMultiOrderDetail3 = newCustomerMultiOrderDetail(customerMultiOrder.getId(),
                ++num,
                rentInsuranceOrder.getId(), CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue(), rentInsuranceOrder.getMoney());
        insertCustomerMultiOrderDetail(customerMultiOrderDetail3);

        RestResult restResult = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult.getCode());
        assertEquals(CustomerMultiOrder.Status.IN_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        RestResult restResult2 = customerMultiOrderService.payBalance(customer, customerMultiOrder.getId(), 300);
        assertEquals(0, restResult2.getCode());

        assertEquals(RentPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id='"+rentPeriodOrder.getId()+"'"));
        assertEquals(RentInsuranceOrder.Status.PAID.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_insurance_order where id='"+rentInsuranceOrder.getId()+"'"));
        assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id="+customerMultiOrder.getId()));
        assertEquals(10000L-300-300, jdbcTemplate.queryForInt("select balance from bas_customer where id="+customer.getId()));

    }

    @Test
    public void findListByCustomerIdAndStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setStatus(CustomerMultiOrder.Status.IN_PAY.getValue());
        insertCustomerMultiOrder(customerMultiOrder);

        List<CustomerMultiOrder> list = customerMultiOrderService.findListByCustomerIdAndStatus(customer.getId(), CustomerMultiOrder.Status.IN_PAY.getValue());

        assertEquals(1, list.size());
    }

    @Test
    public void countMultiWaitPay_1() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        int num = customerMultiOrderService.countMultiWaitPay(customer.getId(), CustomerMultiOrder.Type.HD.getValue());

        assertEquals(0, num);

    }
    @Test
    public void countMultiWaitPay_2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setStatus(CustomerMultiOrder.Status.IN_PAY.getValue());
        insertCustomerMultiOrder(customerMultiOrder);

        int num = customerMultiOrderService.countMultiWaitPay(customer.getId(), CustomerMultiOrder.Type.HD.getValue());

        assertEquals(1, num);

    }

    @Test
    public void countMultiWaitPay_3() throws Exception {

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        customerMultiOrder.setStatus(CustomerMultiOrder.Status.IN_PAY.getValue());
        customerMultiOrder.setType(CustomerMultiOrder.Type.ZC.getValue());
        insertCustomerMultiOrder(customerMultiOrder);

        int num = customerMultiOrderService.countMultiWaitPay(customer.getId(), CustomerMultiOrder.Type.ZC.getValue());

        assertEquals(1, num);

    }
}
