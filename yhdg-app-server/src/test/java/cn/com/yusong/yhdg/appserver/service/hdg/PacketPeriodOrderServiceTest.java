package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
public class PacketPeriodOrderServiceTest extends BaseJunit4Test {

    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;

    @Test
    public void findRemainingTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder.setEndTime(new Date());
        insertPacketPeriodOrder(packetPeriodOrder);
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        assertEquals(1, packetPeriodOrderService.insert(packetPeriodOrder));

    }

    @Test
    public void findList() {
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

        List<PacketPeriodOrder> list = packetPeriodOrderService.findList(customer.getId(), 0, 10);
        for (PacketPeriodOrder order : list) {
            String time = DateFormatUtils.format(order.getBeginTime(), Constant.DATE_FORMAT) + "-" + order.getEndTime();
            System.out.print(time);
        }
    }

    @Test
    public void getList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setTicketMoney(1);
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);
        RestResult restResult = packetPeriodOrderService.getList(customer.getId(), 0, 10);

        assertEquals(0, restResult.getCode());
    }

    /**
     * 套餐第三方支付(使用优惠券购买套餐)
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

        //押金券
        CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(foregiftTicket);
        //租金券
        CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
        rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(rentTicket);
        //抵扣券
//        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
//        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
//        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
//        insertCustomerCouponTicket(deductionTicket);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

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

        Station station = newStation(agent.getId());
        insertStation(station);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setBalanceStationId(station.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        RestResult restResult = packetPeriodOrderService.payByMulti(customer.getId(), agent.getId(),null ,systemBatteryType.getId(),0, 0,
                packetPeriodPrice.getId(), packetPeriodPrice.getPrice(),rentTicket.getId(), ConstEnum.PayType.MULTI_CHANNEL);

        assertEquals(0, restResult.getCode());

        assertEquals(packetPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from hdg_packet_period_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));

        assertEquals(CustomerMultiOrder.Status.NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order"));
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select source_type from bas_customer_multi_order_detail");
        assertEquals(1, list.size());
        int packet=0;
        for(Map map1:list){
            if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue()){
                packet=1;
            }
        }
        assertEquals(1, packet);
    }

    /**
     * 套餐第三方支付(使用优惠券购买套餐)
     */
    @Test
    public void payByThird_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);


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
        insertCustomer(customer);

    }

    /**
     * 活动套餐+优惠券
     */
    @Test
    public void payByThird_3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
        insertCustomerCouponTicket(customerCouponTicket);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        ExchangeBatteryForegift exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        PacketPeriodPrice packetPeriodPrice = newPacketPeriodPrice(agent.getId(), exchangeBatteryForegift.getId());
        insertPacketPeriodPrice(packetPeriodPrice);

        long couponTicketId = customerCouponTicket.getId();

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        Station station = newStation(agent.getId());
        insertStation(station);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        customerExchangeInfo.setBalanceStationId(station.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        RestResult restResult = packetPeriodOrderService.payByThird(customer.getId(),
        agent.getId(), null, agentBatteryType.getBatteryType(), 0,0,
        packetPeriodPrice.getId(),
        packetPeriodPrice.getPrice(),
        couponTicketId, ConstEnum.PayType.WEIXIN);

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();

        //判断套餐订单
        String packetPeriodOrderId = weixinPayOrder.getSourceId();
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(packetPeriodOrderId);
        assertNotNull(packetPeriodOrder);
    }


    /**
     * 套餐余额支付(使用优惠券购买套餐)
     */
    @Test
    public void payBalance_1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
    }

    /**
     * 套餐余额支付(不使用优惠券购买套餐)
     */
    @Test
    public void payBalance_2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
    }

    /**
     * 活动套餐 + 优惠券
     */
    @Test
    public void payBalance_3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
    }

    @Test
    public void findOneEnabled() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
    }


    @Test
    public void refundOrder1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
    }
}
