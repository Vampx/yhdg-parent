package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RentPeriodOrderService extends AbstractService {

    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    RentActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    RentPeriodPriceMapper rentPeriodPriceMapper;
    @Autowired
    VipRentPeriodPriceMapper vipRentPeriodPriceMapper;
    @Autowired
    RentPeriodActivityMapper rentPeriodActivityMapper;
    @Autowired
    RentInsuranceMapper rentInsuranceMapper;
    @Autowired
    RentInsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;


    public RentPeriodOrder find(String id) {
        return rentPeriodOrderMapper.find(id);
    }

    public List<RentPeriodOrder> findList(long customerId, int offset, int limit) {
        return rentPeriodOrderMapper.findList(customerId, offset, limit);
    }

    public RentPeriodOrder findLastEndTime(long customerId) {
        return rentPeriodOrderMapper.findLastEndTime(customerId);
    }

    public List<RentPeriodOrder> findListByCustomerId(long customerId) {
        return rentPeriodOrderMapper.findListByCustomerId(customerId);
    }

    public List<RentPeriodOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return rentPeriodOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    public RentPeriodOrder findOneEnabled(long customerId, int agentId, int batteryType) {
        RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.findOneEnabled(customerId, RentPeriodOrder.Status.USED.getValue(), agentId, batteryType);
        if (rentPeriodOrder == null) {
            rentPeriodOrder = rentPeriodOrderMapper.findOneEnabled(customerId, RentPeriodOrder.Status.NOT_USE.getValue(),  agentId, batteryType);
        }
        return rentPeriodOrder;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, long activityId, long priceId, int price,long vipId,
                                 long couponTicketId,  long insuranceId, int insurancePrice, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (customer.getFullname() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户未实名认证");
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }


        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户没有交押金");
        }

        if (customerRentInfo.getVehicleForegiftFlag() != null && customerRentInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        int batteryType = customerRentInfo.getBatteryType();

        Shop shop = shopMapper.find(customerRentInfo.getBalanceShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;

        int insuranceMoney = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        //查询套餐
        if (priceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(priceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        if(activityId != 0) {
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            RentPeriodActivity rentPeriodActivity = rentPeriodActivityMapper.find(activityId);
            if(rentPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(rentPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();
        }

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, packetMoney, CustomerMultiOrder.Type.ZD.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        rentPeriodOrder.setPartnerId(customer.getPartnerId());
        rentPeriodOrder.setAgentId(agent.getId());
        rentPeriodOrder.setAgentName(agent.getAgentName());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setShopName(shop.getShopName());
        rentPeriodOrder.setBatteryType(batteryType);
        rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        rentPeriodOrder.setTicketMoney(price - packetMoney);
        rentPeriodOrder.setTicketName(ticketName);
        rentPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
        rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder.setMoney(packetMoney);
        rentPeriodOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentPeriodOrder.setPayType(payType.getValue());
        rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentPeriodOrder.setCreateTime(new Date());
        rentPeriodOrder.setDayCount(dayCount);
        rentPeriodOrder.setPrice(price);
        rentPeriodOrder.setConsumeDepositBalance(0);
        rentPeriodOrder.setConsumeGiftBalance(0);
        rentPeriodOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
        rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentPeriodOrderMapper.insert(rentPeriodOrder);


        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
        customerMultiOrderDetail.setNum(++num);
        customerMultiOrderDetail.setSourceId(rentPeriodOrder.getId());
        customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue());
        customerMultiOrderDetail.setMoney(rentPeriodOrder.getMoney());
        customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);

        //查询保险
        if (insuranceId != 0) {
            RentInsurance insurance = rentInsuranceMapper.find(insuranceId);
            if (insurance == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险不存在");
            }

            if (insurance.getPrice() !=  insurancePrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险价格不正确，请确认");
            }

            insuranceMoney = insurance.getPrice();
            insurancePaid = insurance.getPaid();
            insuranceMonthCount = insurance.getMonthCount();
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            insuranceOrder.setAgentName(agent.getAgentName());
            insuranceOrder.setBatteryType(batteryType);
            insuranceOrder.setId(newOrderId(OrderId.OrderIdType.RENT_INSURANCE_ORDER));
            insuranceOrder.setStatus(RentInsuranceOrder.Status.NOT_PAY.getValue());
            insuranceOrder.setPrice(insuranceMoney);
            insuranceOrder.setPaid(insurancePaid);
            insuranceOrder.setMoney(insuranceMoney);
            insuranceOrder.setMonthCount(insuranceMonthCount);
            insuranceOrder.setBeginTime(beginTime);
            insuranceOrder.setEndTime(endTime);
            insuranceOrder.setCustomerId(customerId);
            insuranceOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            insuranceOrder.setPayType(payType.getValue());
            insuranceOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            insuranceOrder.setCreateTime(new Date());
            insuranceOrder.setConsumeDepositBalance(0);
            insuranceOrder.setConsumeGiftBalance(0);
            insuranceOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
            insuranceOrderMapper.insert(insuranceOrder);

            CustomerMultiOrderDetail customerMultiOrderDetail2 = new CustomerMultiOrderDetail();
            customerMultiOrderDetail2.setOrderId(customerMultiOrder.getId());
            customerMultiOrderDetail2.setNum(++num);
            customerMultiOrderDetail2.setSourceId(insuranceOrder.getId());
            customerMultiOrderDetail2.setSourceType(CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue());
            customerMultiOrderDetail2.setMoney(insuranceOrder.getMoney());
            customerMultiOrderDetailMapper.insert(customerMultiOrderDetail2);
        }


        NotNullMap map = new NotNullMap();
        map.putString("packetOrderId", rentPeriodOrder.getId());
        map.putString("insuranceOrderId", insuranceOrder==null?"":insuranceOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, long activityId, long priceId, int price,long vipId,
                                 long couponTicketId,  long insuranceId, int insurancePrice, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户没有交押金");
        }

        if (customerRentInfo.getVehicleForegiftFlag() != null && customerRentInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        int batteryType = customerRentInfo.getBatteryType();

        Shop shop = shopMapper.find(customerRentInfo.getBalanceShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;

        int insuranceMoney = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        //查询套餐
        if (priceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(priceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        if(activityId != 0) {
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            RentPeriodActivity rentPeriodActivity = rentPeriodActivityMapper.find(activityId);
            if(rentPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(rentPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();
        }

        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        rentPeriodOrder.setPartnerId(customer.getPartnerId());
        rentPeriodOrder.setAgentId(agent.getId());
        rentPeriodOrder.setAgentName(agent.getAgentName());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setShopName(shop.getShopName());
        rentPeriodOrder.setBatteryType(batteryType);
        rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        rentPeriodOrder.setTicketMoney(price - packetMoney);
        rentPeriodOrder.setTicketName(ticketName);
        rentPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
        rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder.setMoney(packetMoney);
        rentPeriodOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentPeriodOrder.setPayType(payType.getValue());
        rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentPeriodOrder.setCreateTime(new Date());
        rentPeriodOrder.setDayCount(dayCount);
        rentPeriodOrder.setPrice(price);
        rentPeriodOrder.setConsumeDepositBalance(0);
        rentPeriodOrder.setConsumeGiftBalance(0);
        rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentPeriodOrderMapper.insert(rentPeriodOrder);

        //查询保险
        if (insuranceId != 0) {
            RentInsurance insurance = rentInsuranceMapper.find(insuranceId);
            if (insurance == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险不存在");
            }

            if (insurance.getPrice() !=  insurancePrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险价格不正确，请确认");
            }

            insuranceMoney = insurance.getPrice();
            insurancePaid = insurance.getPaid();
            insuranceMonthCount = insurance.getMonthCount();
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            insuranceOrder.setAgentName(agent.getAgentName());
            insuranceOrder.setBatteryType(batteryType);
            insuranceOrder.setId(newOrderId(OrderId.OrderIdType.RENT_INSURANCE_ORDER));
            insuranceOrder.setStatus(RentInsuranceOrder.Status.NOT_PAY.getValue());
            insuranceOrder.setPrice(insuranceMoney);
            insuranceOrder.setPaid(insurancePaid);
            insuranceOrder.setMoney(insuranceMoney);
            insuranceOrder.setMonthCount(insuranceMonthCount);
            insuranceOrder.setBeginTime(beginTime);
            insuranceOrder.setEndTime(endTime);
            insuranceOrder.setCustomerId(customerId);
            insuranceOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            insuranceOrder.setPayType(payType.getValue());
            insuranceOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            insuranceOrder.setCreateTime(new Date());
            insuranceOrder.setConsumeDepositBalance(0);
            insuranceOrder.setConsumeGiftBalance(0);
            insuranceOrderMapper.insert(insuranceOrder);
        }
        String memo = String.format("租金:%.2f, 保险:%.2f", packetMoney / 100.0, insuranceMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(packetMoney + insuranceMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
            weixinPayOrder.setSourceId(rentPeriodOrder.getId());
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrder.setAgentId(rentPeriodOrder.getAgentId());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);

        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(rentPeriodOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(packetMoney + insuranceMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
            weixinmpPayOrder.setSourceId(rentPeriodOrder.getId());
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrder.setAgentId(rentPeriodOrder.getAgentId());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);

        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), rentPeriodOrder.getId(), packetMoney + insuranceMoney, customerId, AlipayPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue(), "套餐订单支付", "套餐订单支付");
            map.put("orderId", rentPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), rentPeriodOrder.getAgentId(), rentPeriodOrder.getId(), packetMoney + insuranceMoney, customerId, AlipayPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue(), "套餐订单支付", "套餐订单支付");
            map.put("orderId", rentPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, long activityId, long priceId, int price, long vipId,
                                 long couponTicketId,  long insuranceId, int insurancePrice) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Agent agent = agentMapper.find(customer.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户没有交押金");
        }

        if (customerRentInfo.getVehicleForegiftFlag() != null && customerRentInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        int batteryType = customerRentInfo.getBatteryType();

        Shop shop = shopMapper.find(customerRentInfo.getBalanceShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;

        int insuranceMoney = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        //查询套餐
        if (priceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(priceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != price ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }

            dayCount = rentPeriodPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        if(activityId != 0) {
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            RentPeriodActivity rentPeriodActivity = rentPeriodActivityMapper.find(activityId);
            if(rentPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(rentPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();
        }

        List<Integer> balanceList = new ArrayList<Integer>();

        RestResult restResult = updateCustomerBalance(customer, packetMoney, balanceList);
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        Date beginTime = null;
        Date endTime = null;
        int status = RentPeriodOrder.Status.NOT_USE.getValue();
        //没有使用中电池，为使用变为已经使用
        RentPeriodOrder usedOrder = rentPeriodOrderMapper.findOneEnabled(customer.getId(), RentPeriodOrder.Status.USED.getValue(), agent.getId(),  batteryType);
        if (usedOrder == null) {
            beginTime = new Date();
            endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);
            status = RentPeriodOrder.Status.USED.getValue();
        }

        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        rentPeriodOrder.setPartnerId(customer.getPartnerId());
        rentPeriodOrder.setAgentId(agent.getId());
        rentPeriodOrder.setAgentName(agent.getAgentName());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setShopName(shop.getShopName());
        rentPeriodOrder.setBatteryType(batteryType);
        rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        rentPeriodOrder.setTicketMoney(price - packetMoney);
        rentPeriodOrder.setTicketName(ticketName);
        rentPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
        rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
        rentPeriodOrder.setStatus(status);
        rentPeriodOrder.setBeginTime(beginTime);
        rentPeriodOrder.setEndTime(endTime);
        rentPeriodOrder.setMoney(packetMoney);
        rentPeriodOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentPeriodOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentPeriodOrder.setCreateTime(new Date());
        rentPeriodOrder.setPayTime(new Date());
        rentPeriodOrder.setDayCount(dayCount);
        rentPeriodOrder.setPrice(price);
        rentPeriodOrder.setConsumeDepositBalance(balanceList.get(0));
        rentPeriodOrder.setConsumeGiftBalance(balanceList.get(1));
        rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentPeriodOrderMapper.insert(rentPeriodOrder);

        //清空缓存
        String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
        memCachedClient.delete(key);

        if (packetMoney > 0) {
            //客户流水（支出）
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
            inOutMoney.setMoney(-rentPeriodOrder.getMoney());
            inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
            inOutMoney.setBizId(rentPeriodOrder.getId());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(customer.getBalance() - packetMoney );
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //租金赠送
        String sourceId = rentPeriodOrder.getId();
        Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
        Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

        super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

        if (rentPeriodOrder.getCouponTicketId() != null) {
            if (customerCouponTicketMapper.useTicket(rentPeriodOrder.getCouponTicketId(),
                    new Date(),
                    CustomerCouponTicket.Status.NOT_USER.getValue(),
                    CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("优惠券不可用");
            }
        }



        if(activityId != 0) {
            RentActivityCustomer activityCustomer = new RentActivityCustomer();
            activityCustomer.setActivityId(activityId);
            activityCustomer.setCustomerId(customerId);
            activityCustomer.setFullname(customer.getFullname());
            activityCustomer.setMobile(customer.getMobile());
            activityCustomer.setCreateTime(new Date());
            activityCustomerMapper.insert(activityCustomer);
        }

        //余额充值，套餐充值成功,推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(rentPeriodOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.RENT_PERIOD_ORDER_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);


        //查询保险
        if (insuranceId != 0) {
            RentInsurance insurance = rentInsuranceMapper.find(insuranceId);
            if (insurance == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险不存在");
            }

            if (insurance.getPrice() !=  insurancePrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "保险价格不正确，请确认");
            }

            insuranceMoney = insurance.getPrice();
            insurancePaid = insurance.getPaid();
            insuranceMonthCount = insurance.getMonthCount();
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            balanceList = new ArrayList<Integer>();

            restResult = updateCustomerBalance(customer, insuranceMoney, balanceList);
            if (restResult.getCode() != RespCode.CODE_0.getValue()) {
                throw new BalanceNotEnoughException();
            }

            beginTime = new Date();
            endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            insuranceOrder.setAgentName(agent.getAgentName());
            insuranceOrder.setBatteryType(batteryType);
            insuranceOrder.setId(newOrderId(OrderId.OrderIdType.RENT_INSURANCE_ORDER));
            insuranceOrder.setStatus(RentInsuranceOrder.Status.PAID.getValue());
            insuranceOrder.setPrice(insuranceMoney);
            insuranceOrder.setPaid(insurancePaid);
            insuranceOrder.setMoney(insuranceMoney);
            insuranceOrder.setMonthCount(insuranceMonthCount);
            insuranceOrder.setBeginTime(beginTime);
            insuranceOrder.setEndTime(endTime);
            insuranceOrder.setCustomerId(customerId);
            insuranceOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            insuranceOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
            insuranceOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            insuranceOrder.setCreateTime(new Date());
            insuranceOrder.setConsumeDepositBalance(balanceList.get(0));
            insuranceOrder.setConsumeGiftBalance(balanceList.get(1));
            insuranceOrderMapper.insert(insuranceOrder);


            if (insuranceMoney > 0) {
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customer.getId());
                inOutMoney.setMoney(-insuranceMoney);
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_INSURANCE.getValue());
                inOutMoney.setBizId(insuranceOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(customer.getBalance() - packetMoney - insuranceMoney);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            }
        }

        //客户购买租金续租消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * price / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
        return RestResult.SUCCESS;
    }


    public RestResult getList(long customerId, int offset, int limit) {
        List<Map> list = new ArrayList<Map>(limit);

        List<RentPeriodOrder> rentPeriodOrders = rentPeriodOrderMapper.findNoStatusList(customerId, offset, limit, RentPeriodOrder.Status.NOT_PAY.getValue());
        for (RentPeriodOrder rentPeriodOrder : rentPeriodOrders) {
            Map line = new HashMap();
            line.put("id", rentPeriodOrder.getId());
            line.put("dayCount", rentPeriodOrder.getDayCount());
            line.put("beginDate", rentPeriodOrder.getBeginTime() == null ? "" : DateFormatUtils.format(rentPeriodOrder.getBeginTime(), Constant.DATE_TIME_FORMAT));
            line.put("endDate", rentPeriodOrder.getEndTime() == null ? "" : DateFormatUtils.format(rentPeriodOrder.getEndTime(), Constant.DATE_TIME_FORMAT));
            line.put("money", rentPeriodOrder.getMoney());
            line.put("customerMobile", rentPeriodOrder.getCustomerMobile());
            line.put("customerName", rentPeriodOrder.getCustomerFullname());
            line.put("createTime", DateFormatUtils.format(rentPeriodOrder.getCreateTime(), Constant.DATE_TIME_FORMAT));

            line.put("payTypeName", "");
            if (rentPeriodOrder.getPayType() != null) {
                line.put("payTypeName", ConstEnum.PayType.getName(rentPeriodOrder.getPayType()));
            }
            line.put("ticketName", "");
            Integer ticketMoney = rentPeriodOrder.getTicketMoney();
            if (ticketMoney != null) {
                String ticketName = rentPeriodOrder.getTicketName();
                if (ticketName == null) {
                    ticketName = "";
                }
                line.put("ticketName", String.format("%.2f", 1d * (ticketMoney / 100)) + "元" + ticketName);
            }

            line.put("status", rentPeriodOrder.getStatus());

            if (rentPeriodOrder.getPayTime() != null) {
                line.put("payTime", DateFormatUtils.format(rentPeriodOrder.getPayTime(), Constant.DATE_TIME_FORMAT));
            }
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public Map getRentExpireTime( CustomerRentInfo customerRentInfo){
        Date endTime = null;
        int money = 0;

        if(customerRentInfo != null){
            //查询存在最后结束时间的订单
            RentPeriodOrder lastOrder = rentPeriodOrderMapper.findOneEnabled(customerRentInfo.getId(), RentPeriodOrder.Status.USED.getValue(), customerRentInfo.getAgentId(), customerRentInfo.getBatteryType());
            if(lastOrder != null){
                endTime = lastOrder.getEndTime();
                money = lastOrder.getMoney();
            }

            List<RentPeriodOrder>  rentPeriodOrderList = rentPeriodOrderMapper.findListByCustomerIdAndStatus(customerRentInfo.getId(), RentPeriodOrder.Status.NOT_USE.getValue());
            //查询未使用套餐
            for(RentPeriodOrder rentPeriodOrder : rentPeriodOrderList){
                if(endTime == null || endTime.compareTo(new Date()) < 0 ){
                    endTime = new Date();
                }
                endTime = DateUtils.addDays(endTime, rentPeriodOrder.getDayCount());
                money += rentPeriodOrder.getMoney();
            }
        }

        Map map = new HashMap();
        map.put("endTime", endTime);
        map.put("money", money);

        return map;

    }

}