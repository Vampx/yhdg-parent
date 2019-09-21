package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.zd.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class RentForegiftOrderService extends AbstractService {

    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
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
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    RentActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    RentBatteryForegiftMapper rentBatteryForegiftMapper;
    @Autowired
    VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;
    @Autowired
    RentInsuranceMapper rentInsuranceMapper;
    @Autowired
    RentPeriodPriceMapper rentPeriodPriceMapper;
    @Autowired
    RentPeriodActivityMapper rentPeriodActivityMapper;
    @Autowired
    RentInsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    DeductionTicketOrderMapper deductionTicketOrderMapper;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    VipRentPeriodPriceMapper vipRentPeriodPriceMapper;

    public RentForegiftOrder find(String orderId) {
        return rentForegiftOrderMapper.find(orderId);
    }

    public List<RentForegiftOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return rentForegiftOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int updateRefund(String id, Date applyRefundTime, String refundReason, int toStatus, int fromStatus) {
        int total = rentForegiftOrderMapper.updateRefund(id, applyRefundTime, refundReason, toStatus, fromStatus);
        return total;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, String batteryId,  long foregiftId,long vipForegiftId,
                                 int foregiftPrice,  long activityId, long rentPeriodPriceId, long vipRentPeriodPriceId,
                                 int rentPrice,  long foregiftTicketId, long packetTicketId, long deductionTicketId,
                                 int insuranceId, int insurancePrice, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (customer.getFullname() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户未实名认证");
        }

        Battery battery = batteryMapper.find(batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }

        Shop shop = shopMapper.find(shopStoreBattery.getShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        
        Agent agent = agentMapper.find(battery.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if(rentPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String rentTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int rentTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int rentMoney = 0; //租金包时段套餐 支付 金额（转换后）


        int insuranceMoney = 0;
        int totalMoney = 0;
        int dayCount = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipRentBatteryForegift vipForegift = vipRentBatteryForegiftMapper.find(vipForegiftId);
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(rentBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (rentBatteryForegift.getMoney() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
            }
        }

        foregiftMoney = foregiftPrice;

        //查询电池抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有电池抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //电池抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //电池抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            rentTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            rentTicketName = rentCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipRentPeriodPriceId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipRentPeriodPriceId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }


        //查询套餐
        if (rentPeriodPriceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(rentPeriodPriceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(rentTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
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
            rentMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();
        }

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

        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {

            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //电池抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
             //押金金额大于电池抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney =  foregiftMoney;
                foregiftMoney = 0;
            }
        }


        totalMoney = rentMoney + foregiftMoney + insuranceMoney;

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, totalMoney, CustomerMultiOrder.Type.ZD.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        RentPeriodOrder rentPeriodOrder = null;
        if (rentPeriodPriceId != 0 || activityId != 0 || vipRentPeriodPriceId != 0) {
            rentPeriodOrder = new RentPeriodOrder();
            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setShopId(shop.getId());
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(battery.getType());
            rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            rentPeriodOrder.setTicketMoney(rentPrice - rentMoney);
            rentPeriodOrder.setTicketName(rentTicketName);
            //绑定租金优惠券id
            rentPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setMoney(rentMoney);
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
            rentPeriodOrder.setPrice(rentPrice);
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
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            //insuranceOrder.setCabinetId(cabinetId);
            insuranceOrder.setAgentName(agent.getAgentName());
            insuranceOrder.setBatteryType(battery.getType());
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

            CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
            customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
            customerMultiOrderDetail.setNum(++num);
            customerMultiOrderDetail.setSourceId(insuranceOrder.getId());
            customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue());
            customerMultiOrderDetail.setMoney(insuranceOrder.getMoney());
            customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);
        }

        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();
        rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
        rentForegiftOrder.setPartnerId(customer.getPartnerId());
        rentForegiftOrder.setAgentId(agent.getId());
        rentForegiftOrder.setAgentName(agent.getAgentName());
        rentForegiftOrder.setShopId(shop.getId());
        rentForegiftOrder.setShopName(shop.getShopName());
        //rentForegiftOrder.setCabinetId(cabinetId);
        rentForegiftOrder.setBatteryType(battery.getType());
        rentForegiftOrder.setBatteryId(batteryId);
        rentForegiftOrder.setPrice(foregiftPrice);
        rentForegiftOrder.setMoney(foregiftMoney);
        rentForegiftOrder.setConsumeDepositBalance(0);
        rentForegiftOrder.setConsumeGiftBalance(0);
        rentForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
        rentForegiftOrder.setPayType(payType.getValue());
        rentForegiftOrder.setForegiftId(foregiftId);
        rentForegiftOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
        rentForegiftOrder.setCreateTime(new Date());

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            rentForegiftOrder.setCouponTicketId(foregiftTicketId);
            rentForegiftOrder.setTicketName(foregiftTicketName);
            rentForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            rentForegiftOrder.setDeductionTicketId(deductionTicketId);
            rentForegiftOrder.setDeductionTicketName(deductionTicketName);
            rentForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
//            insertDeductionTicketOrder(rentForegiftOrder, deductionTicket);
        }
        rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentForegiftOrderMapper.insert(rentForegiftOrder);


        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
        customerMultiOrderDetail.setNum(++num);
        customerMultiOrderDetail.setSourceId(rentForegiftOrder.getId());
        customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.ZDFOREGIFT.getValue());
        customerMultiOrderDetail.setMoney(rentForegiftOrder.getMoney());
        customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);


        NotNullMap map = new NotNullMap();
        map.putString("foregiftOrderId", rentForegiftOrder.getId());
        map.putString("packetOrderId", rentPeriodOrder==null?"":rentPeriodOrder.getId());
        map.putString("insuranceOrderId", insuranceOrder==null?"":insuranceOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, String batteryId,  long foregiftId,long vipForegiftId,
                                 int foregiftPrice,  long activityId, long rentPeriodPriceId,long vipRentPeriodPriceId,
                                 int rentPrice,  long foregiftTicketId, long packetTicketId, long deductionTicketId,
                                 int insuranceId, int insurancePrice, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Battery battery = batteryMapper.find(batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }

        Shop shop = shopMapper.find(shopStoreBattery.getShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Agent agent = agentMapper.find(battery.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if(rentPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String rentTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int rentTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int rentMoney = 0; //租金包时段套餐 支付 金额（转换后）


        int insuranceMoney = 0;
        int totalMoney = 0;
        int dayCount = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipRentBatteryForegift vipForegift = vipRentBatteryForegiftMapper.find(vipForegiftId);
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(rentBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (rentBatteryForegift.getMoney() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
            }
        }

        foregiftMoney = foregiftPrice;

        //查询电池抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有电池抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //电池抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //电池抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            rentTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            rentTicketName = rentCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipRentPeriodPriceId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipRentPeriodPriceId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }

        //查询套餐
        if (rentPeriodPriceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(rentPeriodPriceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(rentTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
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
            rentMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();
        }

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

        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {

            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //电池抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
             //押金金额大于电池抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney =  foregiftMoney;
                foregiftMoney = 0;
            }
        }


        totalMoney = rentMoney + foregiftMoney + insuranceMoney;

        RentPeriodOrder rentPeriodOrder = null;
        if (rentPeriodPriceId != 0 || activityId != 0 || vipRentPeriodPriceId != 0) {
            rentPeriodOrder = new RentPeriodOrder();
            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setShopId(shop.getId());
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(battery.getType());
            rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            rentPeriodOrder.setTicketMoney(rentPrice - rentMoney);
            rentPeriodOrder.setTicketName(rentTicketName);
            //绑定租金优惠券id
            rentPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setMoney(rentMoney);
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
            rentPeriodOrder.setPrice(rentPrice);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            rentPeriodOrderMapper.insert(rentPeriodOrder);
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            //insuranceOrder.setCabinetId(cabinetId);
            insuranceOrder.setAgentName(agent.getAgentName());
            insuranceOrder.setBatteryType(battery.getType());
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

        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();
        rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
        rentForegiftOrder.setPartnerId(customer.getPartnerId());
        rentForegiftOrder.setAgentId(agent.getId());
        rentForegiftOrder.setAgentName(agent.getAgentName());
        rentForegiftOrder.setShopId(shop.getId());
        rentForegiftOrder.setShopName(shop.getShopName());
        //rentForegiftOrder.setCabinetId(cabinetId);
        rentForegiftOrder.setBatteryType(battery.getType());
        rentForegiftOrder.setBatteryId(batteryId);
        rentForegiftOrder.setPrice(foregiftPrice);
        rentForegiftOrder.setMoney(foregiftMoney);
        rentForegiftOrder.setConsumeDepositBalance(0);
        rentForegiftOrder.setConsumeGiftBalance(0);
        rentForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
        rentForegiftOrder.setPayType(payType.getValue());
        rentForegiftOrder.setForegiftId(foregiftId);
        rentForegiftOrder.setCreateTime(new Date());

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            rentForegiftOrder.setCouponTicketId(foregiftTicketId);
            rentForegiftOrder.setTicketName(foregiftTicketName);
            rentForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            rentForegiftOrder.setDeductionTicketId(deductionTicketId);
            rentForegiftOrder.setDeductionTicketName(deductionTicketName);
            rentForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
//            insertDeductionTicketOrder(rentForegiftOrder, deductionTicket);
        }
        rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentForegiftOrderMapper.insert(rentForegiftOrder);

        String sourceId = OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue() + ":" + rentForegiftOrder.getId();
        if (rentPeriodOrder != null) {
            sourceId += "," + OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue() + ":" + rentPeriodOrder.getId();
        }
        if (insuranceOrder != null) {
            sourceId += "," + OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue() + ":" + insuranceOrder.getId();
        }

        String memo = String.format("押金:%.2f, 租金:%.2f, 保险:%.2f", foregiftMoney / 100.0, rentMoney / 100.0, insuranceMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(totalMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue());
            weixinPayOrder.setSourceId(sourceId);
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(rentForegiftOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(totalMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue());
            weixinmpPayOrder.setSourceId(sourceId);
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue(), "换电押金订单支付", "换电押金订单支付", memo);
            map.put("batteryType", battery.getType());
            map.put("foregiftOrderId", rentForegiftOrder.getId());
            map.put("rentOrderId", rentPeriodOrder == null ? "" : rentPeriodOrder.getId());
            map.put("insuranceOrder", insuranceOrder == null ? "" : insuranceOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), rentForegiftOrder.getAgentId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.RENT_FOREGIFT_ORDER_PAY.getValue(), "换电押金订单支付", "换电押金订单支付", memo);
            map.put("batteryType", battery.getType());
            map.put("foregiftOrderId", rentForegiftOrder.getId());
            map.put("rentOrderId", rentPeriodOrder == null ? "" : rentPeriodOrder.getId());
            map.put("insuranceOrder", insuranceOrder == null ? "" : insuranceOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, String batteryId,  long foregiftId,long vipForegiftId,
                                 int foregiftPrice,  long activityId, long rentPeriodPriceId, long vipRentPeriodPriceId,
                                 int rentPrice,  long foregiftTicketId, long packetTicketId, long deductionTicketId,
                                 int insuranceId, int insurancePrice) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Battery battery = batteryMapper.find(batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }

        Shop shop = shopMapper.find(shopStoreBattery.getShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Agent agent = agentMapper.find(battery.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if(rentPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String rentTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int rentTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int rentMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int insuranceMoney = 0;
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipRentBatteryForegift vipForegift = vipRentBatteryForegiftMapper.find(vipForegiftId);
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(rentBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
            if (rentBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (rentBatteryForegift.getMoney() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
            }
        }


        foregiftMoney = foregiftPrice;

        //查询电池抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有电池抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //电池抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //电池抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            rentTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            rentTicketName = rentCouponTicket.getTicketName();
        }

        //查询VIP租金套餐
        if (vipRentPeriodPriceId != 0) {
            VipRentPeriodPrice vipRentPeriodPrice = vipRentPeriodPriceMapper.find(vipRentPeriodPriceId);
            if (vipRentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipRentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipRentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }

        //查询套餐
        if (rentPeriodPriceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(rentPeriodPriceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != rentPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            if (rentPrice >= rentTicketMoney) { //套餐价格大于 租金优惠券金额
                rentMoney = rentPrice - rentTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(rentTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
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
            rentMoney = rentPeriodActivity.getPrice();
            dayCount = rentPeriodActivity.getDayCount();

        }

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

        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {
            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //电池抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
            //押金金额大于电池抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney =  foregiftMoney;
                foregiftMoney = 0;
            }
        }

        int totalMoney = rentMoney + insuranceMoney + foregiftMoney;

        if ((customer.getBalance() + customer.getGiftBalance()) < totalMoney) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        int balance = customer.getBalance(), giftBalance = customer.getGiftBalance();

        RestResult restResult = updateCustomerBalance(customer, totalMoney, new ArrayList<Integer>());
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        if (deductionTicketId != 0 && realDeductionTicketMoney >= 0) {
            if (customerCouponTicketMapper.useTicket(deductionTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("电池抵扣券已被使用或已过期");
            }
        }

        if (foregiftTicketId != 0 && realforegiftTicketMoney >= 0) {
            if (customerCouponTicketMapper.useTicket(foregiftTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("押金券已被使用或已过期");
            }
        }

        if (packetTicketId != 0 && (rentPeriodPriceId != 0 || vipRentPeriodPriceId != 0)) {
            if (customerCouponTicketMapper.useTicket(packetTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("租金券已被使用或已过期");
            }
        }

        RentPeriodOrder rentPeriodOrder = null;
        if (rentPeriodPriceId != 0 || activityId != 0 || vipRentPeriodPriceId != 0) {
            int consumeBalance = 0, consumeGiftBalance = 0;
            if (balance >= rentMoney) {
                consumeBalance = rentMoney;
            } else {
                consumeBalance = customer.getBalance();
                consumeGiftBalance = rentMoney - balance;
            }
            balance -= consumeBalance;
            giftBalance -= consumeGiftBalance;

            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);

            rentPeriodOrder = new RentPeriodOrder();
            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            //rentPeriodOrder.setCabinetId(cabinetId);
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setShopId(shop.getId());
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(battery.getType());
            rentPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            rentPeriodOrder.setTicketMoney(rentPrice - rentMoney);
            rentPeriodOrder.setTicketName(rentTicketName);
            //绑定租金优惠券id
            rentPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.USED.getValue());
            rentPeriodOrder.setBeginTime(beginTime);
            rentPeriodOrder.setEndTime(endTime);
            rentPeriodOrder.setMoney(rentMoney);
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
            rentPeriodOrder.setPrice(rentPrice);
            rentPeriodOrder.setConsumeDepositBalance(consumeBalance);
            rentPeriodOrder.setConsumeGiftBalance(consumeGiftBalance);
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

            if (rentMoney > 0) {
                //客户流水（支出）
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                inOutMoney.setMoney(-rentPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                inOutMoney.setBizId(rentPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(balance);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            }

            //租金赠送
            String sourceId = rentPeriodOrder.getId();
            Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
            Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());
        }

        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            int consumeBalance = 0, consumeGiftBalance = 0;
            if (balance >= insuranceMoney) {
                consumeBalance = insuranceMoney;
            } else {
                consumeBalance = customer.getBalance();
                consumeGiftBalance = insuranceMoney - balance;
            }
            balance -= consumeBalance;
            giftBalance -= consumeGiftBalance;

            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            insuranceOrder.setAgentName(agent.getAgentName());
            //insuranceOrder.setCabinetId(cabinetId);
            insuranceOrder.setBatteryType(battery.getType());
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
            insuranceOrder.setConsumeDepositBalance(consumeBalance);
            insuranceOrder.setConsumeGiftBalance(consumeGiftBalance);
            insuranceOrderMapper.insert(insuranceOrder);

            if (insuranceMoney > 0) {
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customer.getId());
                inOutMoney.setMoney(-insuranceMoney);
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_INSURANCE.getValue());
                inOutMoney.setBizId(insuranceOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(balance);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            }
        }


        int consumeBalance = 0, consumeGiftBalance = 0;
        if (balance >= foregiftMoney) {
            consumeBalance = foregiftMoney;
        } else {
            consumeBalance = customer.getBalance();
            consumeGiftBalance = foregiftMoney - balance;
        }
        balance -= consumeBalance;
        giftBalance -= consumeGiftBalance;

        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();
        rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
        rentForegiftOrder.setPartnerId(customer.getPartnerId());
        rentForegiftOrder.setAgentId(agent.getId());
        rentForegiftOrder.setAgentName(agent.getAgentName());
        //rentForegiftOrder.setCabinetId(cabinetId);
        rentForegiftOrder.setShopId(shop.getId());
        rentForegiftOrder.setShopName(shop.getShopName());
        rentForegiftOrder.setBatteryType(battery.getType());
        rentForegiftOrder.setBatteryId(batteryId);
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        rentForegiftOrder.setPrice(foregiftPrice);
        rentForegiftOrder.setMoney(foregiftMoney);
        rentForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            rentForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                rentForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        rentForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        rentForegiftOrder.setCreateTime(new Date());
        rentForegiftOrder.setPayTime(new Date());
        rentForegiftOrder.setConsumeDepositBalance(consumeBalance);
        rentForegiftOrder.setConsumeGiftBalance(consumeGiftBalance);
        rentForegiftOrder.setForegiftId(foregiftId);

        if (foregiftMoney > 0) {
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
            inOutMoney.setMoney(-foregiftMoney);
            inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_FOREGIFT_PAY.getValue());
            inOutMoney.setBizId(rentForegiftOrder.getId());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(balance);
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            rentForegiftOrder.setCouponTicketId(foregiftTicketId);
            rentForegiftOrder.setTicketName(foregiftTicketName);
            rentForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            rentForegiftOrder.setDeductionTicketId(deductionTicketId);
            rentForegiftOrder.setDeductionTicketName(deductionTicketName);
            rentForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
            insertDeductionTicketOrder(rentForegiftOrder, deductionTicket);
        }
        rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        rentForegiftOrderMapper.insert(rentForegiftOrder);

        //运营商首单赠送押金券
        int count = rentForegiftOrderMapper.findCountByCustomerId(rentForegiftOrder.getId(), agent.getId(), customer.getId(), RentForegiftOrder.Status.WAIT_PAY.getValue());
        if (count == 0) {
            //押金赠送
            String sourceId = rentForegiftOrder.getId();
            Integer sourceType = OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue();
            Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type,0, agent.getId(), CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());
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

        //更新用户押金
        customerRentInfo = new CustomerRentInfo();
        customerRentInfo.setId(customerId);
        customerRentInfo.setAgentId(agent.getId());
        customerRentInfo.setBatteryType(battery.getType());
        customerRentInfo.setForegift(foregiftMoney);
        customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
        customerRentInfo.setBalanceShopId(shopStoreBattery.getShopId());
        customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.FALSE.getValue());
        customerRentInfo.setCreateTime(new Date());
        customerRentInfoMapper.insert(customerRentInfo);

        //生成租电订单
        RentOrder rentOrder = new RentOrder();
        rentOrder.setId(newOrderId(OrderId.OrderIdType.RENT_ORDER));
        rentOrder.setPartnerId(agent.getPartnerId());
        rentOrder.setAgentId(battery.getAgentId());
        rentOrder.setAgentName(agent.getAgentName());
        rentOrder.setAgentCode(agent.getAgentCode());
        rentOrder.setShopId(shop.getId());
        rentOrder.setShopName(shop.getShopName());
        rentOrder.setCustomerId(customer.getId());
        rentOrder.setCustomerMobile(customer.getMobile());
        rentOrder.setCustomerFullname(customer.getFullname());
        rentOrder.setBatteryType(battery.getType());
        rentOrder.setBatteryId(battery.getId());
        rentOrder.setStatus(RentOrder.Status.RENT.getValue());
        rentOrder.setCurrentVolume(battery.getVolume());
        rentOrder.setCurrentDistance(0);
        rentOrder.setCreateTime(new Date());
        rentOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        rentOrderMapper.insert(rentOrder);


        //租金成功就绑定电池
        CustomerRentBattery customerRentBattery = new CustomerRentBattery();
        customerRentBattery.setCustomerId(customer.getId());
        customerRentBattery.setAgentId(battery.getAgentId());
        customerRentBattery.setBatteryId(battery.getId());
        customerRentBattery.setBatteryType(battery.getType());
        customerRentBattery.setRentOrderId(rentOrder.getId());
        customerRentBatteryMapper.insert(customerRentBattery);

        //库存表去除该电池
        shopStoreBatteryMapper.clearBattery(shopStoreBattery.getShopId(), shopStoreBattery.getBatteryId());

        //电池使用
        batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.CUSTOMER_OUT.getValue(), rentOrder.getId(), new Date(), rentForegiftOrder.getCustomerId(), rentForegiftOrder.getCustomerMobile(), rentForegiftOrder.getCustomerFullname());

        //回写用户运营商id
        customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

        //更新客户租电押金状态
        customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());

        //押金加入押金池
        handleZdAgentForegift(rentForegiftOrder);

        //身份证认证记录状态是空 或者 未生成
        if (customer.getIdCardAuthRecordStatus() == null || customer.getIdCardAuthRecordStatus() == Customer.IdCardAuthRecordStatus.NOT.getValue()) {
            String idCardAuthMoneyValue = findConfigValue(ConstEnum.SystemConfigKey.ID_CARD_AUTH_MONEY.getValue());
            int idCardAuthMoney = 0;
            if (StringUtils.isNotEmpty(idCardAuthMoneyValue)) {
                idCardAuthMoney = (int) (Float.parseFloat(idCardAuthMoneyValue) * 100);
            }

            if (idCardAuthMoney > 0) {
                IdCardAuthRecord record = new IdCardAuthRecord();
                record.setAgentId(agent.getId());
                record.setAgentName(agent.getAgentName());
                record.setAgentCode(agent.getAgentCode());
                record.setCustomerId(customer.getId());
                record.setMobile(customer.getMobile());
                record.setFullname(customer.getFullname());
                record.setMoney(idCardAuthMoney);
                record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                record.setCreateTime(new Date());
                idCardAuthRecordMapper.insert(record);
                customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.CREATED.getValue());
            } else {
                customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.AVOID.getValue());
            }
        }

        //余额支付押金成功后，推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(rentForegiftOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.RENT_FOREGIFT_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customerId);
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftMoney}，租金: ${rentMoney}。",
                new String[]{"${foregiftMoney}", "${rentMoney}"},
                new String[]{String.format("%.2f元", 1d * foregiftMoney/ 100.0), String.format("%.2f元", 1d * rentMoney / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult payInstallment(long customerId, String batteryId, long installmentId,
                                     long foregiftId, int foregiftPrice, long packetPeriodPriceId,
                                     int packetPrice, long insuranceId, int insurancePrice) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Battery battery = batteryMapper.find(batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }

        Shop shop = shopMapper.find(shopStoreBattery.getShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Agent agent = agentMapper.find(battery.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        RentInstallmentSetting setting = rentInstallmentSettingMapper.find(installmentId);
        if (setting == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "押金分期设置不存在");
        }

        //客户分期记录
        CustomerInstallmentRecord record = new CustomerInstallmentRecord();
        record.setPartnerId(customer.getPartnerId());
        record.setCustomerId(customer.getId());
        record.setFullname(customer.getFullname());
        record.setMobile(customer.getMobile());
        record.setAgentId(agent.getId());
        record.setAgentCode(agent.getAgentCode());
        record.setAgentName(agent.getAgentName());
        record.setPaidMoney(0);//已支付金额
        record.setStatus(CustomerInstallmentRecord.Status.WAIT_PAY.getValue());
        record.setCategory(ConstEnum.Category.RENT.getValue());
        record.setFeeMoney(0);
        record.setTotalMoney(setting.getTotalMoney());
        record.setRentSettingId(setting.getId());
        record.setCreateTime(new Date());
        customerInstallmentRecordMapper.insert(record);

        //客户欠款记录付款明细
        List<RentInstallmentDetail> detailList = rentInstallmentDetailMapper.findListBySettingId(setting.getId());
        for (RentInstallmentDetail detail : detailList) {
            CustomerInstallmentRecordPayDetail payDetail = new CustomerInstallmentRecordPayDetail();
            payDetail.setRecordId(record.getId());
            payDetail.setPartnerId(record.getPartnerId());
            payDetail.setCustomerId(customer.getId());
            payDetail.setFullname(customer.getFullname());
            payDetail.setMobile(customer.getMobile());
            payDetail.setAgentId(agent.getId());
            payDetail.setAgentCode(agent.getAgentCode());
            payDetail.setAgentName(agent.getAgentName());
            payDetail.setFeeType(CustomerInstallmentRecordPayDetail.FeeType.NO_HANDLING_FEE.getValue());
            payDetail.setFeeMoney(0);
            payDetail.setStatus(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue());
            payDetail.setTotalMoney(record.getTotalMoney());//总金额
            payDetail.setCategory(record.getCategory());
            payDetail.setMoney(detail.getMoney());/*本次支付金额*/
            payDetail.setForegiftMoney(detail.getForegiftMoney());
            payDetail.setPacketMoney(detail.getPacketMoney());
            payDetail.setInsuranceMoney(detail.getInsuranceMoney());
            payDetail.setExpireTime(detail.getExpireTime());
            payDetail.setNum(detail.getNum());
            payDetail.setCreateTime(new Date());
            customerInstallmentRecordPayDetailMapper.insert(payDetail);
        }

        CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
        if (customerRentInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int insuranceMoney = 0;
        int dayCount = 0;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        int num = 1;
        //查询押金
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
        if (rentBatteryForegift == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
        }
        if (rentBatteryForegift.getMoney() != foregiftPrice) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
        }

        foregiftMoney = foregiftPrice;

        //查询套餐
        if (packetPeriodPriceId != 0) {
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceMapper.find(packetPeriodPriceId);
            if (rentPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (rentPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = rentPeriodPrice.getDayCount();

            //分期不使用优惠券 没有优惠
            packetMoney = packetPrice;
        }

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


            RentPeriodOrder rentPeriodOrder = null;
        if (packetPeriodPriceId != 0) {

            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);

            rentPeriodOrder = new RentPeriodOrder();
            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            //rentPeriodOrder.setCabinetId(cabinetId);
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setShopId(shop.getId());
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(battery.getType());
            rentPeriodOrder.setActivityId(null);
            rentPeriodOrder.setTicketMoney(0);
            rentPeriodOrder.setTicketName(null);
            //绑定租金优惠券id
            rentPeriodOrder.setCouponTicketId(null);
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setBeginTime(beginTime);
            rentPeriodOrder.setEndTime(endTime);
            rentPeriodOrder.setMoney(0);
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
            rentPeriodOrder.setPayType(ConstEnum.PayType.INSTALLMENT.getValue());
            rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentPeriodOrder.setCreateTime(new Date());
            // rentPeriodOrder.setPayTime(new Date());
            rentPeriodOrder.setDayCount(dayCount);
            rentPeriodOrder.setPrice(packetPrice);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //欠款订单明细
            CustomerInstallmentRecordOrderDetail recordOrderDetail = new CustomerInstallmentRecordOrderDetail();
            recordOrderDetail.setRecordId(record.getId());
            recordOrderDetail.setNum(num);
            recordOrderDetail.setSourceId(rentPeriodOrder.getId());
            recordOrderDetail.setSourceType(OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
            recordOrderDetail.setMoney(rentPeriodOrder.getMoney());
            recordOrderDetail.setCategory(ConstEnum.Category.RENT.getValue());
            customerInstallmentRecordOrderDetailMapper.insert(recordOrderDetail);

        }


        RentInsuranceOrder insuranceOrder = null;
        if (insuranceId != 0) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addMonths(beginTime, insuranceMonthCount), Calendar.DAY_OF_MONTH),-1);

            insuranceOrder = new RentInsuranceOrder();
            insuranceOrder.setPartnerId(customer.getPartnerId());
            insuranceOrder.setAgentId(agent.getId());
            insuranceOrder.setAgentName(agent.getAgentName());
            //insuranceOrder.setCabinetId(cabinetId);
            insuranceOrder.setBatteryType(battery.getType());
            insuranceOrder.setId(newOrderId(OrderId.OrderIdType.RENT_INSURANCE_ORDER));
            insuranceOrder.setStatus(RentInsuranceOrder.Status.NOT_PAY.getValue());
            insuranceOrder.setPrice(insuranceMoney);
            insuranceOrder.setPaid(insurancePaid);
            insuranceOrder.setMoney(0);
            insuranceOrder.setMonthCount(insuranceMonthCount);
            insuranceOrder.setBeginTime(beginTime);
            insuranceOrder.setEndTime(endTime);
            insuranceOrder.setCustomerId(customerId);
            insuranceOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            insuranceOrder.setPayType(ConstEnum.PayType.INSTALLMENT.getValue());
            insuranceOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            insuranceOrder.setCreateTime(new Date());
            insuranceOrder.setConsumeDepositBalance(0);
            insuranceOrder.setConsumeGiftBalance(0);
            insuranceOrderMapper.insert(insuranceOrder);

            //欠款订单明细
            CustomerInstallmentRecordOrderDetail recordOrderDetail = new CustomerInstallmentRecordOrderDetail();
            recordOrderDetail.setRecordId(record.getId());
            recordOrderDetail.setNum(++num);
            recordOrderDetail.setSourceId(insuranceOrder.getId());
            recordOrderDetail.setSourceType(OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue());
            recordOrderDetail.setMoney(insuranceOrder.getMoney());
            recordOrderDetail.setCategory(ConstEnum.Category.RENT.getValue());
            customerInstallmentRecordOrderDetailMapper.insert(recordOrderDetail);

        }

        if (foregiftId != 0) {

            RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();
            rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
            rentForegiftOrder.setPartnerId(customer.getPartnerId());
            rentForegiftOrder.setAgentId(agent.getId());
            rentForegiftOrder.setAgentName(agent.getAgentName());
            //rentForegiftOrder.setCabinetId(cabinetId);
            rentForegiftOrder.setShopId(shop.getId());
            rentForegiftOrder.setShopName(shop.getShopName());
            rentForegiftOrder.setBatteryType(battery.getType());
            rentForegiftOrder.setBatteryId(batteryId);
            rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
            rentForegiftOrder.setPrice(foregiftPrice);
            rentForegiftOrder.setMoney(0);
            rentForegiftOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                rentForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    rentForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentForegiftOrder.setPayType(ConstEnum.PayType.INSTALLMENT.getValue());
            rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentForegiftOrder.setCreateTime(new Date());
           // rentForegiftOrder.setPayTime(new Date());
            rentForegiftOrder.setConsumeDepositBalance(0);
            rentForegiftOrder.setConsumeGiftBalance(0);
            rentForegiftOrder.setForegiftId(foregiftId);
            rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            rentForegiftOrderMapper.insert(rentForegiftOrder);

            //欠款订单明细
            CustomerInstallmentRecordOrderDetail recordOrderDetail = new CustomerInstallmentRecordOrderDetail();
            recordOrderDetail.setRecordId(record.getId());
            recordOrderDetail.setNum(++num);
            recordOrderDetail.setSourceId(rentForegiftOrder.getId());
            recordOrderDetail.setSourceType(OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
            recordOrderDetail.setMoney(rentForegiftOrder.getMoney());
            recordOrderDetail.setCategory(ConstEnum.Category.RENT.getValue());
            customerInstallmentRecordOrderDetailMapper.insert(recordOrderDetail);

        }

        Map map = new HashMap();
        List<CustomerInstallmentRecordPayDetail> details = customerInstallmentRecordPayDetailMapper.findList(record.getId(), customerId, ConstEnum.Category.RENT.getValue());
        map.put("detailsId", details.get(0).getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    private void insertDeductionTicketOrder(RentForegiftOrder rentForegiftOrder, CustomerCouponTicket deductionTicket){
        if(rentForegiftOrder.getDeductionTicketMoney()>0) {
            DeductionTicketOrder order = new DeductionTicketOrder();
            order.setCategory(ConstEnum.Category.RENT.getValue());
            order.setAgentId(rentForegiftOrder.getAgentId());
            order.setAgentName(rentForegiftOrder.getAgentName());
            order.setAgentCode(rentForegiftOrder.getAgentCode());
            order.setCustomerId(rentForegiftOrder.getCustomerId());
            order.setFullname(rentForegiftOrder.getCustomerFullname());
            order.setMobile(rentForegiftOrder.getCustomerMobile());
            order.setCreateTime(new Date());
            order.setMoney(rentForegiftOrder.getDeductionTicketMoney());
            order.setTicketMoney(deductionTicket.getMoney());
            deductionTicketOrderMapper.insert(order);
        }
    }


}
