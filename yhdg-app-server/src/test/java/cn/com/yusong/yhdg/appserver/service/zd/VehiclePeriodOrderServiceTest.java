package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
public class VehiclePeriodOrderServiceTest extends BaseJunit4Test {

    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;

    /**
     * 套餐多渠道支付(使用优惠券购买套餐)
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
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

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
        CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());;
        deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
        deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        insertCustomerCouponTicket(deductionTicket);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(battery.getAgentId(), shop.getId(),battery.getId());
        insertShopStoreBattery(shopStoreBattery);


        RentBatteryType rentBatteryType = newRentBatteryType(agent.getId(), battery.getType());
        insertRentBatteryType(rentBatteryType);

        RentInsurance insurance = newRentInsurance(agent.getId(), rentBatteryType.getBatteryType());
        insertRentInsurance(insurance);

        RentBatteryForegift rentBatteryForegift = newRentBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertRentBatteryForegift(rentBatteryForegift);

        RentPeriodPrice rentPeriodPrice = newRentPeriodPrice(agent.getId(), rentBatteryForegift.getId());
        insertRentPeriodPrice(rentPeriodPrice);

//        RentPeriodActivity activity = newRentPeriodActivity(agent.getId(),rentBatteryType.getBatteryType());
//        insertRentPeriodActivity(activity);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), rentBatteryType.getBatteryType(), agent.getId());
        customerRentInfo.setBalanceShopId(shop.getId());
        insertCustomerRentInfo(customerRentInfo);

        RestResult restResult = rentPeriodOrderService.payByMulti(customer.getId(), 0,
                rentPeriodPrice.getId(), rentPeriodPrice.getPrice(),0,rentTicket.getId(),
                insurance.getId().intValue(), insurance.getPrice(), ConstEnum.PayType.MULTI_CHANNEL);

        assertEquals(0, restResult.getCode());

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(rentPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from zd_rent_period_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));

        assertEquals(CustomerMultiOrder.Status.NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order"));
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select source_type from bas_customer_multi_order_detail");
        assertEquals(2, list.size());
        int packet=0;
        int insuranc=0;
        for(Map map1:list){
            if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue()){
                packet=1;
            }else if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue()){
                insuranc=1;
            }
        }
        assertEquals(1, packet);
        assertEquals(1, insuranc);
    }
}
