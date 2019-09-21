package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderServiceTest;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
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

public class RentForegiftOrderServiceTest extends BaseJunit4Test {
    static final Logger log = org.apache.logging.log4j.LogManager.getLogger(RentForegiftOrderServiceTest.class);
    //static Logger log = LogManager.

    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        RentForegiftOrder entity = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(entity);

        assertNotNull(rentForegiftOrderService.find(entity.getId()));
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

        RestResult restResult = rentForegiftOrderService.payByMulti(customer.getId(), battery.getId(), rentBatteryForegift.getId(),0,
                rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(), 0,rentPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId(), insurance.getId().intValue(), insurance.getPrice(), ConstEnum.PayType.WEIXIN);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        //判断多通道订单
        Map map = (Map) restResult.getData();
        assertNotNull(map);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from zd_rent_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(rentPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from zd_rent_period_order where customer_id = ?", customer.getId()));
        assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", foregiftTicket.getId()));

        assertEquals(CustomerMultiOrder.Status.NOT_PAY.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_multi_order"));
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select source_type from bas_customer_multi_order_detail");
        assertEquals(3, list.size());
        int foregift=0;
        int packet=0;
        int insuranc=0;
        for(Map map1:list){
            if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.ZDFOREGIFT.getValue()){
                foregift=1;
            }else if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue()){
                packet=1;
            }else if((Integer)map1.get("source_type") == CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue()){
                insuranc=1;
            }
        }
        assertEquals(1, foregift);
        assertEquals(1, packet);
        assertEquals(1, insuranc);
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

        RestResult restResult = rentForegiftOrderService.payByThird(customer.getId(), battery.getId(), rentBatteryForegift.getId(),0,
                rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(), 0,rentPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId(), insurance.getId().intValue(), insurance.getPrice(), ConstEnum.PayType.WEIXIN);
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, rentPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                rentPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(rentPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from zd_rent_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(rentPeriodPrice.getPrice() - rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from zd_rent_period_order where customer_id = ?", customer.getId()));
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



        RestResult restResult = rentForegiftOrderService.payByThird(customer.getId(), battery.getId(), rentBatteryForegift.getId(),0,
                rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(),0, rentPeriodPrice.getPrice(),
                0, 0, 0, 0, 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, rentPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                rentPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(rentPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100L, jdbcTemplate.queryForInt("select money from zd_rent_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100L, jdbcTemplate.queryForInt("select money from zd_rent_period_order where customer_id = ?", customer.getId()));
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


        RestResult restResult = rentForegiftOrderService.payByThird(customer.getId(), battery.getId(), rentBatteryForegift.getId(),0,
                rentBatteryForegift.getMoney(),
                0, 0, 0,0,
                0, 0, 0, 0, 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, rentPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                rentPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from zd_rent_foregift_order where customer_id = ?", customer.getId()));

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

        RentPeriodActivity activity = newRentPeriodActivity(agent.getId(), rentBatteryType.getBatteryType());
        insertRentPeriodActivity(activity);

        RestResult restResult = rentForegiftOrderService.payByThird(customer.getId(), battery.getId(), rentBatteryForegift.getId(),0,
                rentBatteryForegift.getMoney(),
                activity.getId(), 0, 0,1000,
                0, 0, 0, 0, 0, ConstEnum.PayType.WEIXIN);
        assertEquals(0, restResult.getCode());

        //判断返回微信订单
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
        assertNotNull(weixinPayOrder);

        //判断押金和套餐订单
        String[] sourceIdList = StringUtils.split(weixinPayOrder.getSourceId(), ",");
        String foregiftOrderfId = null, rentPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                rentPeriodOrderId = list[1];
            }
        }

        assertNotNull(foregiftOrderfId);
        assertNotNull(rentPeriodOrderId);

        assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from zd_rent_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select money from zd_rent_period_order where customer_id = ?", customer.getId()));

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

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        RestResult restResult = rentForegiftOrderService.payBalance(customer.getId(), battery.getId(),
                rentBatteryForegift.getId(),0, rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(),0, rentPeriodPrice.getPrice(),
                foregiftTicket.getId(), rentTicket.getId(), deductionTicket.getId(),
                insurance.getId().intValue(), insurance.getPrice());
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());


        assertEquals(10000L - rentBatteryForegift.getMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
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

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        RestResult restResult = rentForegiftOrderService.payBalance(customer.getId(), battery.getId(),
                rentBatteryForegift.getId(),0, rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(), 0,rentPeriodPrice.getPrice(),
                0, 0, 0,
                insurance.getId().intValue(), insurance.getPrice());
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - rentBatteryForegift.getMoney() - rentPeriodPrice.getPrice() - insurance.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));


        assertEquals(rentBatteryForegift.getMoney().intValue(), jdbcTemplate.queryForInt("select zd_foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(rentBatteryForegift.getMoney().intValue(), jdbcTemplate.queryForInt("select zd_foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_balance_ratio from bas_agent where id = ?", agent.getId()));


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

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        RestResult restResult = rentForegiftOrderService.payBalance(customer.getId(), battery.getId(),
                rentBatteryForegift.getId(), 0,rentBatteryForegift.getMoney(),
                0, 0,0,0,
                0, 0, 0,
                insurance.getId().intValue(), insurance.getPrice());
        System.out.println(restResult.getMessage());
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - rentBatteryForegift.getMoney() - insurance.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
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

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(battery.getAgentId(), shop.getId(),battery.getId());
        insertShopStoreBattery(shopStoreBattery);

        RentBatteryType rentBatteryType = newRentBatteryType(agent.getId(), battery.getType());
        insertRentBatteryType(rentBatteryType);

        RentInsurance insurance = newRentInsurance(agent.getId(), rentBatteryType.getBatteryType());
        insertRentInsurance(insurance);

        RentBatteryForegift rentBatteryForegift = newRentBatteryForegift(agent.getId(), systemBatteryType.getId());
        rentBatteryForegift.setMoney(10000);
        insertRentBatteryForegift(rentBatteryForegift);

        RentPeriodPrice rentPeriodPrice = newRentPeriodPrice(agent.getId(), rentBatteryForegift.getId());
        rentPeriodPrice.setPrice(10000);
        insertRentPeriodPrice(rentPeriodPrice);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        RestResult restResult = rentForegiftOrderService.payBalance(customer.getId(), battery.getId(),
                rentBatteryForegift.getId(), 0,rentBatteryForegift.getMoney(),
                0, rentPeriodPrice.getId(),0, rentPeriodPrice.getPrice(),
                0, 0,0,
                0, 0);
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

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        ShopStoreBattery shopStoreBattery = newShopStoreBattery(battery.getAgentId(), shop.getId(),battery.getId());
        insertShopStoreBattery(shopStoreBattery);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
        insertAgentBatteryType(agentBatteryType);

        Insurance insurance = newInsurance(agent.getId(), agentBatteryType.getBatteryType());
        insertInsurance(insurance);

        RentBatteryForegift rentBatteryForegift = newRentBatteryForegift(agent.getId(), systemBatteryType.getId());
        insertRentBatteryForegift(rentBatteryForegift);

        RentPeriodPrice rentPeriodPrice = newRentPeriodPrice(agent.getId(), rentBatteryForegift.getId());
        insertRentPeriodPrice(rentPeriodPrice);

        RentPeriodActivity activity = newRentPeriodActivity(agent.getId(), agentBatteryType.getBatteryType());
        insertRentPeriodActivity(activity);

        CustomerPayTrack customerPayTrack = newCustomerPayTrack(agent.getId(), customer.getId());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        insertCustomerPayTrack(customerPayTrack);

        RestResult restResult = rentForegiftOrderService.payBalance(customer.getId(), battery.getId(),
                rentBatteryForegift.getId(),0, rentBatteryForegift.getMoney(),
                activity.getId(), 0,0, rentPeriodPrice.getPrice(),
                0, 0, 0,
                0, 0);
        assertEquals(0, restResult.getCode());

        assertEquals(10000L - rentBatteryForegift.getMoney() - rentPeriodPrice.getPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
    }

}
