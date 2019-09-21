/*
package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zc.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by ruanjian5 on 2017/11/6.
 *//*


public class VehicleForegiftOrderServiceTest extends BaseJunit4Test {
    static final Logger log = org.apache.logging.log4j.LogManager.getLogger(VehicleForegiftOrderServiceTest.class);
    //static Logger log = LogManager.

    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;
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

        VehicleForegiftOrder entity = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertVehicleForegiftOrder(entity);

        assertNotNull(vehicleForegiftOrderService.find(entity.getId()));
    }

    */
/**
     * 押金第三方支付(使用优惠券购买套餐)
     *//*

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

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
        insertRentPrice(rentPrice);


        RestResult restResult = vehicleForegiftOrderService.payByThird(customer.getId(), rentPrice.getId(), 0, rentPrice.getForegiftPrice(),
                rentPrice.getRentPrice(), 0, rentTicket.getId(), 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());


        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals((int)rentPrice.getVehicleForegiftPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_foregift_order where customer_id = ?", customer.getId()));
        assertEquals((int)rentPrice.getVehicleRentPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_period_order where customer_id = ?", customer.getId()));
        assertEquals(rentPrice.getForegiftPrice() + (rentPrice.getRentPrice()-rentTicket.getMoney()), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));
    }

    */
/**
     * 押金余额支付(使用优惠券购买套餐)
     *//*

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

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
        insertRentPrice(rentPrice);

        RestResult restResult = vehicleForegiftOrderService.payBalance(customer.getId(), rentPrice.getId(), 0, rentPrice.getForegiftPrice(),
                rentPrice.getRentPrice(), 0, rentTicket.getId(), 0);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        assertEquals(10000L- (rentPrice.getForegiftPrice() + (rentPrice.getRentPrice()-rentTicket.getMoney())), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals((int)rentPrice.getVehicleForegiftPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_foregift_order where customer_id = ?", customer.getId()));
        assertEquals((int)rentPrice.getVehicleRentPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_period_order where customer_id = ?", customer.getId()));
        assertEquals(rentPrice.getForegiftPrice() + (rentPrice.getRentPrice()-rentTicket.getMoney()), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));

    }

    */
/**
     * 押金余额支付(不使用优惠券购买套餐)
     *//*


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

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
        insertRentPrice(rentPrice);

        RestResult restResult = vehicleForegiftOrderService.payBalance(customer.getId(), rentPrice.getId(), 0, rentPrice.getForegiftPrice(),
                rentPrice.getRentPrice(), 0, 0, 0);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - rentPrice.getForegiftPrice() - rentPrice.getRentPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(rentPrice.getForegiftPrice() + rentPrice.getRentPrice(), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));

    }

    @Test
    public void payByMulti() {
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

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
        insertRentPrice(rentPrice);

        RestResult restResult = vehicleForegiftOrderService.payByMulti(customer.getId(), rentPrice.getId(), 0, rentPrice.getForegiftPrice(), rentPrice.getRentPrice(), 0, 0, 0);
        Map data = (Map) restResult.getData();
        Integer money = (Integer) data.get("money");
        assertEquals(money.intValue(), rentPrice.getForegiftPrice() + rentPrice.getRentPrice());
        assertEquals(GroupOrder.Status.WAIT_PAY.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_foregift_order where customer_id = ?", customer.getId()));

    }

}
*/
