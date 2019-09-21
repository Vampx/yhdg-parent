package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemBatteryTypeService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic.CustomerForegiftOrderController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CustomerForegiftOrderService extends AbstractService {

    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
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
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    PacketPeriodActivityMapper packetPeriodActivityMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    ExchangeInstallmentCountMapper exchangeInstallmentCountMapper;
    @Autowired
    ExchangeInstallmentCountDetailMapper exchangeInstallmentCountDetailMapper;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    public CustomerForegiftOrder find(String orderId) {
        return customerForegiftOrderMapper.find(orderId);
    }

    public List<CustomerForegiftOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return customerForegiftOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    public int insert(CustomerForegiftOrder customerForegiftOrder) {
        return customerForegiftOrderMapper.insert(customerForegiftOrder);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int updateRefund(String id, Date applyRefundTime, String refundReason, int toStatus, int fromStatus) {
        int total = customerForegiftOrderMapper.updateRefund(id, applyRefundTime, refundReason, toStatus, fromStatus);
        return total;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, int agentId, String stationId, String cabinetId, Integer batteryType, long foregiftId, long vipForegiftId,
                                 int foregiftPrice,  long activityId, long packetPeriodPriceId, long vipPacketPeriodPriceId,
                                 int packetPrice,  long foregiftTicketId, long packetTicketId, long deductionTicketId,
                                 ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (customer.getFullname() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户未实名认证");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        if (customerExchangeInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if (batteryType == null || batteryType == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不能为空");
        }

        if(packetPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）


        int insuranceMoney = 0;
        int totalMoney = 0;
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;
        int insurancePaid = 0;
        int insuranceMonthCount = 0;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(exchangeBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (exchangeBatteryForegift.getMoney() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
            }
        }
        //如果是VIP套餐押金金额已抵扣
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
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (packetPeriodPriceId != 0) {
            PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(packetPeriodPriceId);
            if (packetPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (packetPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = packetPeriodPrice.getDayCount();
            limitCount = packetPeriodPrice.getLimitCount();
            dayLimitCount = packetPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询VIP租金套餐
        if (vipPacketPeriodPriceId != 0) {
            VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipPacketPeriodPriceId);
            if (vipPacketPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipPacketPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipPacketPeriodPrice.getDayCount();
            limitCount = vipPacketPeriodPrice.getLimitCount();
            dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(packetTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            PacketPeriodActivity packetPeriodActivity = packetPeriodActivityMapper.find(activityId);
            if(packetPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(packetPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = packetPeriodActivity.getPrice();
            dayCount = packetPeriodActivity.getDayCount();
            limitCount = packetPeriodActivity.getLimitCount();
            dayLimitCount = packetPeriodActivity.getDayLimitCount();
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

        totalMoney = packetMoney + foregiftMoney + insuranceMoney;

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, totalMoney, CustomerMultiOrder.Type.HD.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        PacketPeriodOrder packetPeriodOrder = null;
        if (packetPeriodPriceId != 0 || activityId != 0 || vipPacketPeriodPriceId != 0) {
            packetPeriodOrder = new PacketPeriodOrder();
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agentId);
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setStationId(stationId);
            if(StringUtils.isNotEmpty(stationId)){
                Station station = stationMapper.find(stationId);
                if(station != null){
                    packetPeriodOrder.setStationName(station.getStationName());
                }
            }
            packetPeriodOrder.setCabinetId(cabinetId);
            packetPeriodOrder.setBatteryType(batteryType);
            packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            packetPeriodOrder.setTicketMoney(packetPrice - packetMoney);
            packetPeriodOrder.setTicketName(packetTicketName);
            //绑定租金优惠券id
            packetPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setMoney(packetMoney);
            packetPeriodOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                packetPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    packetPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(payType.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setPrice(packetPrice);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            packetPeriodOrderMapper.insert(packetPeriodOrder);


            CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
            customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
            customerMultiOrderDetail.setNum(++num);
            customerMultiOrderDetail.setSourceId(packetPeriodOrder.getId());
            customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue());
            customerMultiOrderDetail.setMoney(packetPeriodOrder.getMoney());
            customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);

        }

        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
        customerForegiftOrder.setPartnerId(customer.getPartnerId());
        customerForegiftOrder.setAgentId(agentId);
        customerForegiftOrder.setAgentName(agent.getAgentName());
        customerForegiftOrder.setStationId(stationId);
        if(StringUtils.isNotEmpty(stationId)){
            Station station = stationMapper.find(stationId);
            if(station != null){
                customerForegiftOrder.setStationName(station.getStationName());
            }
        }
        customerForegiftOrder.setCabinetId(cabinetId);
        customerForegiftOrder.setBatteryType(batteryType);
        customerForegiftOrder.setPrice(foregiftPrice);
        customerForegiftOrder.setMoney(foregiftMoney);
        customerForegiftOrder.setConsumeDepositBalance(0);
        customerForegiftOrder.setConsumeGiftBalance(0);
        customerForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            customerForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                customerForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setPayType(payType.getValue());
        customerForegiftOrder.setForegiftId(foregiftId);
        customerForegiftOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
        customerForegiftOrder.setCreateTime(new Date());

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            customerForegiftOrder.setCouponTicketId(foregiftTicketId);
            customerForegiftOrder.setTicketName(foregiftTicketName);
            customerForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            customerForegiftOrder.setDeductionTicketId(deductionTicketId);
            customerForegiftOrder.setDeductionTicketName(deductionTicketName);
            customerForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
//            insertDeductionTicketOrder(customerForegiftOrder, deductionTicket);
        }

        //vip押金保存减免金额
        if (vipForegiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            customerForegiftOrder.setReduceMoney(vipForegift.getReduceMoney());// 减免金额
        }
        customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        customerForegiftOrderMapper.insert(customerForegiftOrder);


        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
        customerMultiOrderDetail.setNum(++num);
        customerMultiOrderDetail.setSourceId(customerForegiftOrder.getId());
        customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.HDGFOREGIFT.getValue());
        customerMultiOrderDetail.setMoney(customerForegiftOrder.getMoney());
        customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);


        NotNullMap map = new NotNullMap();
        map.putInteger("batteryType", batteryType);
        map.putString("foregiftOrderId", customerForegiftOrder.getId());
        map.putString("packetOrderId", packetPeriodOrder==null?"":packetPeriodOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, int agentId, String stationId, String cabinetId, Integer batteryType, long foregiftId, long vipForegiftId,
                                 int foregiftPrice,  long activityId, long packetPeriodPriceId, long vipPacketPeriodPriceId,
                                 int packetPrice,  long foregiftTicketId, long packetTicketId, long deductionTicketId,
                                 ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (customer.getAuthStatus() == null || customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户未认证");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        if (customerExchangeInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if (batteryType == null || batteryType == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不能为空");
        }

        if(packetPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）


        int totalMoney = 0;
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(exchangeBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (exchangeBatteryForegift.getMoney() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
            }
        }
        //如果是VIP套餐押金金额已抵扣
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
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (packetPeriodPriceId != 0) {
            PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(packetPeriodPriceId);
            if (packetPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (packetPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = packetPeriodPrice.getDayCount();
            limitCount = packetPeriodPrice.getLimitCount();
            dayLimitCount = packetPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询VIP租金套餐
        if (vipPacketPeriodPriceId != 0) {
            VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipPacketPeriodPriceId);
            if (vipPacketPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipPacketPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipPacketPeriodPrice.getDayCount();
            limitCount = vipPacketPeriodPrice.getLimitCount();
            dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(packetTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            PacketPeriodActivity packetPeriodActivity = packetPeriodActivityMapper.find(activityId);
            if(packetPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(packetPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = packetPeriodActivity.getPrice();
            dayCount = packetPeriodActivity.getDayCount();
            limitCount = packetPeriodActivity.getLimitCount();
            dayLimitCount = packetPeriodActivity.getDayLimitCount();
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


        totalMoney = packetMoney + foregiftMoney;

        PacketPeriodOrder packetPeriodOrder = null;
        if (packetPeriodPriceId != 0 || activityId != 0 || vipPacketPeriodPriceId != 0) {
            packetPeriodOrder = new PacketPeriodOrder();
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agentId);
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setStationId(stationId);
            if(StringUtils.isNotEmpty(stationId)){
                Station station = stationMapper.find(stationId);
                if(station != null){
                    packetPeriodOrder.setStationName(station.getStationName());
                }
            }
            packetPeriodOrder.setCabinetId(cabinetId);
            packetPeriodOrder.setBatteryType(batteryType);
            packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            packetPeriodOrder.setTicketMoney(packetPrice - packetMoney);
            packetPeriodOrder.setTicketName(packetTicketName);
            //绑定租金优惠券id
            packetPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setMoney(packetMoney);
            packetPeriodOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                packetPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    packetPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(payType.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setPrice(packetPrice);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            packetPeriodOrderMapper.insert(packetPeriodOrder);
        }


        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
        customerForegiftOrder.setPartnerId(customer.getPartnerId());
        customerForegiftOrder.setAgentId(agentId);
        customerForegiftOrder.setAgentName(agent.getAgentName());
        customerForegiftOrder.setStationId(stationId);
        if(StringUtils.isNotEmpty(stationId)){
            Station station = stationMapper.find(stationId);
            if(station != null){
                customerForegiftOrder.setStationName(station.getStationName());
            }
        }
        customerForegiftOrder.setCabinetId(cabinetId);
        customerForegiftOrder.setBatteryType(batteryType);
        customerForegiftOrder.setPrice(foregiftPrice);
        customerForegiftOrder.setMoney(foregiftMoney);
        customerForegiftOrder.setConsumeDepositBalance(0);
        customerForegiftOrder.setConsumeGiftBalance(0);
        customerForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            customerForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                customerForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        customerForegiftOrder.setPayType(payType.getValue());
        customerForegiftOrder.setForegiftId(foregiftId);
        customerForegiftOrder.setCreateTime(new Date());

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            customerForegiftOrder.setCouponTicketId(foregiftTicketId);
            customerForegiftOrder.setTicketName(foregiftTicketName);
            customerForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            customerForegiftOrder.setDeductionTicketId(deductionTicketId);
            customerForegiftOrder.setDeductionTicketName(deductionTicketName);
            customerForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
//            insertDeductionTicketOrder(customerForegiftOrder, deductionTicket);
        }

        //vip押金保存减免金额
        if (vipForegiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            customerForegiftOrder.setReduceMoney(vipForegift.getReduceMoney());// 减免金额
        }
        customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        customerForegiftOrderMapper.insert(customerForegiftOrder);

        String sourceId = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue() + ":" + customerForegiftOrder.getId();
        if (packetPeriodOrder != null) {
            sourceId += "," + OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue() + ":" + packetPeriodOrder.getId();
        }

        String memo = String.format("押金:%.2f, 租金:%.2f", foregiftMoney / 100.0, packetMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(totalMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
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
            weixinmpPayOrder.setAgentId(customerForegiftOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(totalMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
            weixinmpPayOrder.setSourceId(sourceId);
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        } else if(payType == ConstEnum.PayType.WEIXIN_MA) {
            WeixinmaPayOrder weixinmaPayOrder = new WeixinmaPayOrder();
            weixinmaPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMA_PAY_ORDER));
            weixinmaPayOrder.setPartnerId(customer.getPartnerId());
            weixinmaPayOrder.setAgentId(customerForegiftOrder.getAgentId());
            weixinmaPayOrder.setCustomerId(customerId);
            weixinmaPayOrder.setMobile(customer.getMobile());
            weixinmaPayOrder.setCustomerName(customer.getFullname());
            weixinmaPayOrder.setMoney(totalMoney);
            weixinmaPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
            weixinmaPayOrder.setSourceId(sourceId);
            weixinmaPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmaPayOrder.setMemo(memo);
            weixinmaPayOrder.setCreateTime(new Date());
            weixinmaPayOrderMapper.insert(weixinmaPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmaPayOrder);
        }  else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue(), "换电押金订单支付", "换电押金订单支付", memo);
            map.put("batteryType", batteryType);
            map.put("foregiftOrderId", customerForegiftOrder.getId());
            map.put("packetOrderId", packetPeriodOrder == null ? "" : packetPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), customerForegiftOrder.getAgentId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue(), "换电押金订单支付", "换电押金订单支付", memo);
            map.put("batteryType", batteryType);
            map.put("foregiftOrderId", customerForegiftOrder.getId());
            map.put("packetOrderId", packetPeriodOrder == null ? "" : packetPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, int agentId, String stationId, String cabinetId, Integer batteryType,
                                 long foregiftId, long vipForegiftId, int foregiftPrice,
                                 long activityId, long packetPeriodPriceId, long vipPacketPeriodPriceId,
                                 int packetPrice, long foregiftTicketId, long packetTicketId, long deductionTicketId) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        if (customerExchangeInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
        }

        if (batteryType == null || batteryType == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不能为空");
        }

        SystemBatteryType systemBatteryType = systemBatteryTypeService.find(batteryType);
        if(systemBatteryType==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不存在");
        }

        if(packetPeriodPriceId != 0 && activityId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String deductionTicketName = null;//电池抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //电池抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        //VIP
        if (vipForegiftId != 0 && foregiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            Integer money = Math.max(exchangeBatteryForegift.getMoney() - vipForegift.getReduceMoney(), 0);
            if (vipForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip电池押金不存在");
            }
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (money != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip押金减免金额不正确，请确认");
            }
        }

        if (foregiftId != 0 && vipForegiftId == 0) {
            //查询押金
            ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
            if (exchangeBatteryForegift == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
            }
            if (exchangeBatteryForegift.getMoney() != foregiftPrice) {
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
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (packetPeriodPriceId != 0) {
            PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(packetPeriodPriceId);
            if (packetPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (packetPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = packetPeriodPrice.getDayCount();
            limitCount = packetPeriodPrice.getLimitCount();
            dayLimitCount = packetPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询VIP租金套餐
        if (vipPacketPeriodPriceId != 0) {
            VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipPacketPeriodPriceId);
            if (vipPacketPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
            }

            if (vipPacketPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
            }
            dayCount = vipPacketPeriodPrice.getDayCount();
            limitCount = vipPacketPeriodPrice.getLimitCount();
            dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        //查询活动套餐
        if(activityId != 0) {
            if(packetTicketMoney > 0){//租金优惠券不能和活动同时使用
                return RestResult.result(RespCode.CODE_2.getValue(), "购买活动套餐时不能使用优惠券");
            }
            if(activityCustomerMapper.exist(activityId, customerId) > 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已经参加了此活动，不能重复参加");
            }
            PacketPeriodActivity packetPeriodActivity = packetPeriodActivityMapper.find(activityId);
            if(packetPeriodActivity.getBeginTime().compareTo(new Date()) > 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动尚未开始，请稍后购买");
            }
            if(packetPeriodActivity.getEndTime().compareTo(new Date()) < 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "活动已结束，请选择其他套餐");
            }
            packetMoney = packetPeriodActivity.getPrice();
            dayCount = packetPeriodActivity.getDayCount();
            limitCount = packetPeriodActivity.getLimitCount();
            dayLimitCount = packetPeriodActivity.getDayLimitCount();
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

        int totalMoney = packetMoney + foregiftMoney;

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

        if (packetTicketId != 0 && (packetPeriodPriceId != 0 || vipPacketPeriodPriceId != 0)) {
            if (customerCouponTicketMapper.useTicket(packetTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("租金券已被使用或已过期");
            }
        }

        PacketPeriodOrder packetPeriodOrder = null;
        if (packetPeriodPriceId != 0 || activityId != 0 || vipPacketPeriodPriceId != 0) {
            int consumeBalance = 0, consumeGiftBalance = 0;
            if (balance >= packetMoney) {
                consumeBalance = packetMoney;
            } else {
                consumeBalance = customer.getBalance();
                consumeGiftBalance = packetMoney - balance;
            }
            balance -= consumeBalance;
            giftBalance -= consumeGiftBalance;

            packetPeriodOrder = new PacketPeriodOrder();
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agentId);
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setStationId(stationId);
            if(StringUtils.isNotEmpty(stationId)){
                Station station = stationMapper.find(stationId);
                if(station != null){
                    packetPeriodOrder.setStationName(station.getStationName());
                }
            }
            packetPeriodOrder.setCabinetId(cabinetId);
            packetPeriodOrder.setBatteryType(batteryType);
            packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
            packetPeriodOrder.setTicketMoney(packetPrice - packetMoney);
            packetPeriodOrder.setTicketName(packetTicketName);
            //绑定租金优惠券id
            packetPeriodOrder.setCouponTicketId(packetTicketId == 0 ? null : packetTicketId);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
            packetPeriodOrder.setMoney(packetMoney);
            packetPeriodOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                packetPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    packetPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setPayTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setPrice(packetPrice);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setConsumeDepositBalance(consumeBalance);
            packetPeriodOrder.setConsumeGiftBalance(consumeGiftBalance);
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            packetPeriodOrderMapper.insert(packetPeriodOrder);

            if (packetMoney > 0) {
                //客户流水（支出）
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                inOutMoney.setMoney(-packetPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                inOutMoney.setBizId(packetPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(balance);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            }

            //租金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
            String sourceId = packetPeriodOrder.getId();
            Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, dayCount, agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());
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

        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
        customerForegiftOrder.setPartnerId(customer.getPartnerId());
        customerForegiftOrder.setAgentId(agentId);
        customerForegiftOrder.setAgentName(agent.getAgentName());
        customerForegiftOrder.setStationId(stationId);
        if(StringUtils.isNotEmpty(stationId)){
            Station station = stationMapper.find(stationId);
            if(station != null){
                customerForegiftOrder.setStationName(station.getStationName());
            }
        }
        customerForegiftOrder.setCabinetId(cabinetId);
        customerForegiftOrder.setBatteryType(batteryType);
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        customerForegiftOrder.setPrice(foregiftPrice);
        customerForegiftOrder.setMoney(foregiftMoney);
        customerForegiftOrder.setCustomerId(customerId);
        Customer dbCustomer = customerMapper.find(customerId);
        if (dbCustomer != null) {
            customerForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                customerForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        customerForegiftOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        customerForegiftOrder.setCreateTime(new Date());
        customerForegiftOrder.setPayTime(new Date());
        customerForegiftOrder.setConsumeDepositBalance(consumeBalance);
        customerForegiftOrder.setConsumeGiftBalance(consumeGiftBalance);
        customerForegiftOrder.setForegiftId(foregiftId);

        if (foregiftMoney > 0) {
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
            inOutMoney.setMoney(-foregiftMoney);
            inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT_PAY.getValue());
            inOutMoney.setBizId(customerForegiftOrder.getId());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(balance);
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            customerForegiftOrder.setCouponTicketId(foregiftTicketId);
            customerForegiftOrder.setTicketName(foregiftTicketName);
            customerForegiftOrder.setTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存电池抵扣券
        if (deductionTicketId != 0) {
            customerForegiftOrder.setDeductionTicketId(deductionTicketId);
            customerForegiftOrder.setDeductionTicketName(deductionTicketName);
            customerForegiftOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额

            //保存抵扣券订单
            insertDeductionTicketOrder(customerForegiftOrder, deductionTicket);
        }

        //vip押金保存减免金额
        if (vipForegiftId != 0) {
            VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.find(vipForegiftId);
            customerForegiftOrder.setReduceMoney(vipForegift.getReduceMoney());// 减免金额
        }
        customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        customerForegiftOrderMapper.insert(customerForegiftOrder);


        //运营商首单赠送押金券
        int count = customerForegiftOrderMapper.findCountByCustomerId(customerForegiftOrder.getId(), agent.getId(), customer.getId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        if (count == 0) {
            //押金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
            String sourceId = customerForegiftOrder.getId();
            Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, dayCount,agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());
        }

        if(activityId != 0) {
            ActivityCustomer activityCustomer = new ActivityCustomer();
            activityCustomer.setActivityId(activityId);
            activityCustomer.setCustomerId(customerId);
            activityCustomer.setFullname(customer.getFullname());
            activityCustomer.setMobile(customer.getMobile());
            activityCustomer.setCreateTime(new Date());
            activityCustomerMapper.insert(activityCustomer);
        }

        //更新用户押金
        customerExchangeInfo = new CustomerExchangeInfo();
        customerExchangeInfo.setId(customerId);
        customerExchangeInfo.setAgentId(agentId);
        customerExchangeInfo.setBatteryType(batteryType);
        customerExchangeInfo.setForegift(foregiftMoney);
        customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
        customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
        customerExchangeInfo.setBalanceStationId(stationId);
        customerExchangeInfo.setVehicleForegiftFlag(ConstEnum.Flag.FALSE.getValue());
        customerExchangeInfo.setCreateTime(new Date());
        customerExchangeInfoMapper.insert(customerExchangeInfo);

        //回写用户运营商id
        customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());
        //更新用户归属柜子
        if(StringUtils.isEmpty(customer.getBelongCabinetId())){
            customerMapper.updateCabinet(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getCabinetId());

        }
        //更新用户换电押金状态
        customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

        //押金加入押金池
        handleAgentForegift(customerForegiftOrder);

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

        //handleLaxinCustomer(agent, customer, foregiftMoney, packetMoney);

        //余额支付押金成功后，推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(customerForegiftOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //套餐充值成功,推送
        if(packetPeriodOrder != null){
            metaData = new PushMetaData();
            metaData.setSourceId(packetPeriodOrder.getId());
            metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
            metaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(metaData);
        }

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agentId);
        customerPayTrack.setCustomerId(customerId);
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftMoney}，租金: ${packetMoney}。",
                new String[]{"${foregiftMoney}", "${packetMoney}"},
                new String[]{String.format("%.2f元", 1d * foregiftMoney / 100.0), String.format("%.2f元", 1d * packetMoney / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payInstallment(long customerId, long installmentId, int settingType, int agentId, String stationId, String cabinetId,
                                     Integer batteryType, long foregiftId, int foregiftPrice, long packetPeriodPriceId,int packetPrice,
                                     long deductionTicketId,long foregiftTicketId,long rentPeriodTicketId,
                                     int deductionTicketMoney,int foregiftTicketMoney,int rentPeriodTicketMoney,int countId,
                                      CustomerForegiftOrderController.BatteryCreateByInstallmentParam.ExchangeInstallmentCountDetail [] detailList) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        ExchangeInstallmentSetting setting = exchangeInstallmentSettingMapper.find(installmentId);
        if (setting == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "押金分期设置不存在");
        }
        //客户欠款记录付款明细
        if(settingType!=setting.getSettingType()){
            return RestResult.result(RespCode.CODE_2.getValue(), "押金分期设置类型不同");
        }
        PacketPeriodPrice packetPeriodPrices = packetPeriodPriceMapper.find(packetPeriodPriceId);
        if(packetPeriodPrices == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
        }
        if(packetPeriodPrices.getDayCount() == null && packetPeriodPrices.getDayCount()==0){
            return RestResult.result(RespCode.CODE_2.getValue(), "包时段套餐天数为空");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        if (customerExchangeInfo != null) {
            if(customerExchangeInfo.getVehicleForegiftFlag()!=1){
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
            }
        }
        if(deductionTicketId!=0||deductionTicketMoney!=0){
            CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(deductionTicketId);
            if(customerCouponTicket!=null){
                if(!customerCouponTicket.getCustomerMobile().equals(customer.getMobile())){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此用户与抵扣卷绑定手机不同");
                }
                if(customerCouponTicket.getStatus()!=CustomerCouponTicket.Status.NOT_USER.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "抵扣卷绑定状态不是未使用");
                }
                if(customerCouponTicket.getTicketType()!=CustomerCouponTicket.TicketType.DEDUCTION.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此优惠卷不是抵扣卷");
                }
                if(deductionTicketMoney!=customerCouponTicket.getMoney()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "抵扣卷金额与实际抵扣卷金额不符");
                }
                if(customerCouponTicket.getCategory()!=CustomerCouponTicket.Category.EXCHANGE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此抵扣卷不是换电抵扣卷");
                }
            }else{
                return RestResult.result(RespCode.CODE_2.getValue(), "未找出用户关联抵扣卷");
            }
        }
        if(foregiftTicketId!=0||foregiftTicketMoney!=0){
            CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if(customerCouponTicket!=null){
                if(!customerCouponTicket.getCustomerMobile().equals(customer.getMobile())){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此用户与押金消费卷绑定手机不同");
                }
                if(customerCouponTicket.getStatus()!=CustomerCouponTicket.Status.NOT_USER.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "押金消费卷绑定状态不是未使用");
                }
                if(customerCouponTicket.getTicketType()!=CustomerCouponTicket.TicketType.FOREGIFT.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此优惠卷不是押金消费卷");
                }
                if(foregiftTicketMoney!=customerCouponTicket.getMoney()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "押金消费卷金额与实际押金消费卷金额不符");
                }
                if(customerCouponTicket.getCategory()!=CustomerCouponTicket.Category.EXCHANGE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此押金消费卷不是换电押金消费卷");
                }
            }else{
                return RestResult.result(RespCode.CODE_2.getValue(), "未找出用户关联押金消费卷");
            }
        }
        if(rentPeriodTicketId!=0||rentPeriodTicketMoney!=0){
            CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(rentPeriodTicketId);
            if(customerCouponTicket!=null){
                if(!customerCouponTicket.getCustomerMobile().equals(customer.getMobile())){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此用户与租金消费卷绑定手机不同");
                }
                if(customerCouponTicket.getStatus()!=CustomerCouponTicket.Status.NOT_USER.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金消费卷绑定状态不是未使用");
                }
                if(customerCouponTicket.getTicketType()!=CustomerCouponTicket.TicketType.RENT.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此优惠卷不是租金消费卷");
                }
                if(rentPeriodTicketMoney!=customerCouponTicket.getMoney()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金消费卷金额与实际租金消费卷金额不符");
                }
                if(customerCouponTicket.getCategory()!=CustomerCouponTicket.Category.EXCHANGE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此租金消费卷不是换电租金消费卷");
                }
            }else{
                return RestResult.result(RespCode.CODE_2.getValue(), "未找出用户关联租金消费卷");
            }
        }

        if (batteryType == null || batteryType == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不能为空");
        }

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        int num = 1;
        //查询押金
        ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
        if (exchangeBatteryForegift == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池押金不存在");
        }
        if (exchangeBatteryForegift.getMoney() != foregiftPrice) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池押金价格不正确，请确认");
        }
        foregiftMoney = (foregiftPrice -deductionTicketMoney-foregiftTicketMoney)<0?0:(foregiftPrice -deductionTicketMoney-foregiftTicketMoney);

        //查询套餐
        if (packetPeriodPriceId != 0) {
            PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(packetPeriodPriceId);
            if (packetPeriodPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
            }

            if (packetPeriodPrice.getPrice() != packetPrice ) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
            }
            dayCount = packetPeriodPrice.getDayCount();
            limitCount = packetPeriodPrice.getLimitCount();
            dayLimitCount = packetPeriodPrice.getDayLimitCount();
            packetMoney = (packetPrice-rentPeriodTicketMoney)<0?0:(packetPrice-rentPeriodTicketMoney);
        }else{
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐Id不能为空");
        }


        int totlMoney = foregiftMoney+packetMoney;
        //判断逻辑
        if(settingType == ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue()){

            int foregiftMoneys = 0;
            int packetPeriodMoneys = 0;
            int minForegiftPercentage =0;//总最高押金比例
            int minPacketPercentage =0;//总最高租金比例
            int foregiftMoneyes=0;//上次循环押金和
            int packetPeriodMoneyes=0;//上次循环租金和
            ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountMapper.find(countId);
            if(exchangeInstallmentCount == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "分期总表不存在");
            }
            List<ExchangeInstallmentCountDetail> countId1 = exchangeInstallmentCountDetailMapper.findCountId(exchangeInstallmentCount.getId());
            if(countId1.size()<=0){
                return RestResult.result(RespCode.CODE_2.getValue(), "分期套餐详情不存在");
            }

            for (ExchangeInstallmentCountDetail exchangeInstallmentCountDetail: countId1) {
                minForegiftPercentage+=exchangeInstallmentCountDetail.getMinForegiftPercentage();
                minPacketPercentage+=exchangeInstallmentCountDetail.getMinPacketPeriodPercentage();
            }
            //计算输入金额与总金额是否相同
            for (int i = 0; i < detailList.length; i++) {
                CustomerForegiftOrderController.BatteryCreateByInstallmentParam.ExchangeInstallmentCountDetail cbe = detailList[i];
                packetPeriodMoneys+=cbe.packetPeriodMoney;
                foregiftMoneys+=cbe.foregiftMoney;
            }
            if(totlMoney!=(foregiftMoneys+packetPeriodMoneys)){
                return RestResult.result(RespCode.CODE_2.getValue(), "总金额与输入金额不相等");
            }

            for (int i = 0; i < detailList.length; i++) {
                ExchangeInstallmentCountDetail exchangeInstallmentCountDetail =null;
                CustomerForegiftOrderController.BatteryCreateByInstallmentParam.ExchangeInstallmentCountDetail cbe =detailList [i];

                if(cbe.id!=null){
                    exchangeInstallmentCountDetail = exchangeInstallmentCountDetailMapper.find(cbe.id);
                }
                if(exchangeInstallmentCountDetail == null){
                    return RestResult.result(RespCode.CODE_2.getValue(), "第"+(i+1)+"期分期套餐详情不存在");
                }
                if(cbe.foregiftMoney <((int)(foregiftMoney*((double)exchangeInstallmentCountDetail.getMinForegiftPercentage())/100))){
                    return RestResult.result(RespCode.CODE_2.getValue(), "第"+(i+1)+"期押金最低金额不能少于"+(foregiftMoney*((double)exchangeInstallmentCountDetail.getMinForegiftPercentage()/10000))+"元");
                }else if(cbe.foregiftMoney>((foregiftMoney-foregiftMoneyes)-(int)(foregiftMoney*((double)(minForegiftPercentage-exchangeInstallmentCountDetail.getMinForegiftPercentage())/100)))){
                    return RestResult.result(RespCode.CODE_2.getValue(), "第"+(i+1)+"期押金最高金额不能高于"+((foregiftMoney-foregiftMoneyes)-(int)(foregiftMoney*((double)(minForegiftPercentage-exchangeInstallmentCountDetail.getMinForegiftPercentage())/100)))/100+"元");
                }
                if(cbe.packetPeriodMoney <((int)(packetMoney*((double)exchangeInstallmentCountDetail.getMinPacketPeriodPercentage())/100))){
                    return RestResult.result(RespCode.CODE_2.getValue(), "第"+(i+1)+"期租金最低金额不能少于"+(packetMoney*((double)exchangeInstallmentCountDetail.getMinPacketPeriodPercentage()/10000))+"元");
                }else if(cbe.packetPeriodMoney>((packetMoney-packetPeriodMoneyes)-(int)(packetMoney*((double)(minPacketPercentage-exchangeInstallmentCountDetail.getMinPacketPeriodPercentage())/100)))){
                    return RestResult.result(RespCode.CODE_2.getValue(), "第"+(i+1)+"期租金最高金额不能高于"+((packetMoney-packetPeriodMoneyes)-(int)(packetMoney*((double)(minPacketPercentage-exchangeInstallmentCountDetail.getMinPacketPeriodPercentage())/100)))/100+"元");
                }
                foregiftMoneyes+=cbe.foregiftMoney;
                packetPeriodMoneyes+=cbe.packetPeriodMoney;
                minForegiftPercentage-=exchangeInstallmentCountDetail.getMinForegiftPercentage();
                minPacketPercentage-=exchangeInstallmentCountDetail.getMinPacketPeriodPercentage();
            }
        }else if(settingType == ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue()){
            if(countId == 0 ){
                return RestResult.result(RespCode.CODE_2.getValue(), "分期期数表ID不能为空");
            }
            ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountMapper.find(countId);
            if(exchangeInstallmentCount == null ){
                return RestResult.result(RespCode.CODE_2.getValue(), "分期期数表不存在");
            }

        }
        //开始生成分期订单
        //手续费
        Integer feeMoney =0;
        ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountMapper.find(countId);
        if (exchangeInstallmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.RATE.getValue()){
            feeMoney = (int)(totlMoney*((double)exchangeInstallmentCount.getFeePercentage()/10000));
        }else if(exchangeInstallmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.FIXED_HANDLING_FEE.getValue()){
            feeMoney=exchangeInstallmentCount.getFeeMoney()==null?0:exchangeInstallmentCount.getFeeMoney();
        }
        if(exchangeInstallmentCount.getCount()==0||exchangeInstallmentCount.getCount()==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "分期数不能为0");
        }
        //每次递增天数
        int day = packetPeriodPrices.getDayCount() / exchangeInstallmentCount.getCount();
        //客户分期记录
        CustomerInstallmentRecord record = new CustomerInstallmentRecord();
        record.setPartnerId(customer.getPartnerId());
        record.setCustomerId(customer.getId());
        record.setFullname(customer.getFullname());
        record.setMobile(customer.getMobile());
        record.setAgentId(agentId);
        record.setAgentCode(agent.getAgentCode());
        record.setAgentName(agent.getAgentName());
        record.setPaidMoney(0);//已支付金额
        //支付状态
        record.setForegiftMoney(foregiftMoney);
        record.setPacketMoney(packetMoney);
        record.setFeeMoney(feeMoney*exchangeInstallmentCount.getCount());
        record.setStatus(CustomerInstallmentRecord.Status.WAIT_PAY.getValue());
        record.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        record.setExchangeSettingId(setting.getId());
        record.setTotalMoney(totlMoney+(feeMoney*exchangeInstallmentCount.getCount()));
        record.setCreateTime(new Date());
        customerInstallmentRecordMapper.insert(record);
        if(settingType == ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue()){
            for (int i = 0; i < detailList.length; i++) {
                CustomerInstallmentRecordPayDetail payDetail = new CustomerInstallmentRecordPayDetail();
                CustomerForegiftOrderController.BatteryCreateByInstallmentParam.ExchangeInstallmentCountDetail cbe =detailList [i];
                payDetail.setRecordId(record.getId());
                payDetail.setPartnerId(record.getPartnerId());
                payDetail.setCustomerId(customer.getId());
                payDetail.setFullname(customer.getFullname());
                payDetail.setMobile(customer.getMobile());
                payDetail.setAgentId(agentId);
                payDetail.setAgentCode(agent.getAgentCode());
                payDetail.setAgentName(agent.getAgentName());
                payDetail.setStatus(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue());
                payDetail.setTotalMoney(record.getTotalMoney());//总金额
                payDetail.setCategory(record.getCategory());
                payDetail.setFeeType(exchangeInstallmentCount.getFeeType());
                payDetail.setFeeMoney(feeMoney);//手续费
                payDetail.setMoney(cbe.foregiftMoney+cbe.packetPeriodMoney+feeMoney);//*本次支付金额*//*
                payDetail.setForegiftMoney(cbe.foregiftMoney);
                payDetail.setPacketMoney(cbe.packetPeriodMoney);
                payDetail.setInsuranceMoney(0);
                payDetail.setExpireTime(DateUtils.addDays(new Date(),day*i));
                payDetail.setNum(cbe.num);
                payDetail.setCreateTime(new Date());
                customerInstallmentRecordPayDetailMapper.insert(payDetail);
            }
        }else if(settingType == ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue()){
            CustomerInstallmentRecordPayDetail payDetail = new CustomerInstallmentRecordPayDetail();
            for (int i = 0; i < exchangeInstallmentCount.getCount(); i++) {
                payDetail.setRecordId(record.getId());
                payDetail.setPartnerId(record.getPartnerId());
                payDetail.setCustomerId(customer.getId());
                payDetail.setFullname(customer.getFullname());
                payDetail.setMobile(customer.getMobile());
                payDetail.setAgentId(agentId);
                payDetail.setAgentCode(agent.getAgentCode());
                payDetail.setAgentName(agent.getAgentName());
                payDetail.setStatus(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue());
                payDetail.setTotalMoney(record.getTotalMoney());//总金额
                payDetail.setCategory(record.getCategory());
                payDetail.setFeeMoney(feeMoney);//手续费
                payDetail.setFeeType(exchangeInstallmentCount.getFeeType());
                if((i+1)==exchangeInstallmentCount.getCount()){//此操作解决金额除不尽时最后一天给余下所有
                    payDetail.setForegiftMoney(record.getForegiftMoney()-(record.getForegiftMoney()/exchangeInstallmentCount.getCount())*i);
                    payDetail.setPacketMoney(record.getPacketMoney()-(record.getPacketMoney()/exchangeInstallmentCount.getCount())*i);
                }else{
                    payDetail.setForegiftMoney(record.getForegiftMoney()/exchangeInstallmentCount.getCount());
                    payDetail.setPacketMoney(record.getPacketMoney()/exchangeInstallmentCount.getCount());
                }
                payDetail.setMoney(payDetail.getForegiftMoney()+payDetail.getPacketMoney()+feeMoney);//*本次支付金额*//*
                payDetail.setInsuranceMoney(0);
                payDetail.setExpireTime(DateUtils.addDays(new Date(),day*i));
                payDetail.setNum(i+1);
                payDetail.setCreateTime(new Date());
                customerInstallmentRecordPayDetailMapper.insert(payDetail);
            }
        }

        PacketPeriodOrder packetPeriodOrder = null;
        if (packetPeriodPriceId != 0) {

            packetPeriodOrder = new PacketPeriodOrder();
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agentId);
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setStationId(stationId);
            if(StringUtils.isNotEmpty(stationId)){
                Station station = stationMapper.find(stationId);
                if(station != null){
                    packetPeriodOrder.setStationName(station.getStationName());
                }
            }
            packetPeriodOrder.setCabinetId(cabinetId);
            packetPeriodOrder.setBatteryType(batteryType);
            packetPeriodOrder.setActivityId(null);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            //分期租金状态
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            //默认订单金额为0 分期支付再更新
            packetPeriodOrder.setMoney(0);
            packetPeriodOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                packetPeriodOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    packetPeriodOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.INSTALLMENT.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            //分期完成更新支付时间
           // packetPeriodOrder.setPayTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setPrice(packetPrice);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            packetPeriodOrderMapper.insert(packetPeriodOrder);

            //欠款订单明细
            CustomerInstallmentRecordOrderDetail recordOrderDetail = new CustomerInstallmentRecordOrderDetail();
            recordOrderDetail.setRecordId(record.getId());
            recordOrderDetail.setNum(num);
            recordOrderDetail.setSourceId(packetPeriodOrder.getId());
            recordOrderDetail.setSourceType(OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
            recordOrderDetail.setMoney(packetPeriodOrder.getMoney());
            recordOrderDetail.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            customerInstallmentRecordOrderDetailMapper.insert(recordOrderDetail);

        }

        if (foregiftId != 0) {
            CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
            customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
            customerForegiftOrder.setPartnerId(customer.getPartnerId());
            customerForegiftOrder.setAgentId(agentId);
            customerForegiftOrder.setAgentName(agent.getAgentName());
            customerForegiftOrder.setShopId(stationId);
            if(StringUtils.isNotEmpty(stationId)){
                Station station = stationMapper.find(stationId);
                if(station != null){
                    customerForegiftOrder.setStationName(station.getStationName());
                }
            }
            customerForegiftOrder.setCabinetId(cabinetId);
            customerForegiftOrder.setBatteryType(batteryType);
            //分期保险状态 未付款
            customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
            customerForegiftOrder.setPrice(foregiftPrice);
            //默认订单金额为0 分期支付再更新
            customerForegiftOrder.setMoney(0);
            customerForegiftOrder.setCustomerId(customerId);
            Customer dbCustomer = customerMapper.find(customerId);
            if (dbCustomer != null) {
                customerForegiftOrder.setAgentCompanyId(dbCustomer.getAgentCompanyId());
                AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
                if (agentCompany != null) {
                    customerForegiftOrder.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            customerForegiftOrder.setPayType(ConstEnum.PayType.INSTALLMENT.getValue());
            customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            customerForegiftOrder.setCreateTime(new Date());
            //分期完成更新支付时间
            //customerForegiftOrder.setPayTime(new Date());
            customerForegiftOrder.setForegiftId(foregiftId);
            customerForegiftOrder.setConsumeDepositBalance(0);
            customerForegiftOrder.setConsumeGiftBalance(0);

            customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

            customerForegiftOrderMapper.insert(customerForegiftOrder);

            //欠款订单明细
            CustomerInstallmentRecordOrderDetail recordOrderDetail = new CustomerInstallmentRecordOrderDetail();
            recordOrderDetail.setRecordId(record.getId());
            recordOrderDetail.setNum(++num);
            recordOrderDetail.setSourceId(customerForegiftOrder.getId());
            recordOrderDetail.setSourceType(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
            recordOrderDetail.setMoney(customerForegiftOrder.getMoney());
            recordOrderDetail.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            customerInstallmentRecordOrderDetailMapper.insert(recordOrderDetail);
        }
        Map map = new HashMap();
        List<CustomerInstallmentRecordPayDetail> details = customerInstallmentRecordPayDetailMapper.findList(record.getId(), customerId, ConstEnum.Category.EXCHANGE.getValue());
        map.put("detailsId", details.get(0).getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


}
