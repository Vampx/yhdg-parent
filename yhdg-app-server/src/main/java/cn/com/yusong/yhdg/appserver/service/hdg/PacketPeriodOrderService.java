package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PacketPeriodOrderService extends AbstractService {

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
    PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    PacketPeriodPriceRenewMapper packetPeriodPriceRenewMapper;
    @Autowired
    VipPacketPeriodPriceRenewMapper vipPacketPeriodPriceRenewMapper;
    @Autowired
    PacketPeriodActivityMapper packetPeriodActivityMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public int insert(PacketPeriodOrder packetPeriodOrder) {
        return packetPeriodOrderMapper.insert(packetPeriodOrder);
    }

    public PacketPeriodOrder find(String id) {
        return packetPeriodOrderMapper.find(id);
    }

    public List<PacketPeriodOrder> findList(long customerId, int offset, int limit) {
        return packetPeriodOrderMapper.findList(customerId, offset, limit);
    }

    public PacketPeriodOrder findLastEndTime(long customerId) {
        return packetPeriodOrderMapper.findLastEndTime(customerId, PacketPeriodOrder.Status.USED.getValue());
    }

    public List<PacketPeriodOrder> findListByNoUsed(long customerId) {
        return packetPeriodOrderMapper.findListByNoUsed(customerId, PacketPeriodOrder.Status.NOT_USE.getValue());
    }

    public List<PacketPeriodOrder> findListByCustomerId(long customerId) {
        return packetPeriodOrderMapper.findListByCustomerId(customerId);
    }

    public List<PacketPeriodOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return packetPeriodOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    public PacketPeriodOrder findOneEnabled(long customerId, int agentId, int batteryType) {
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.USED.getValue(), agentId, batteryType);
        if (packetPeriodOrder == null) {
            packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.NOT_USE.getValue(),  agentId, batteryType);
        }
        return packetPeriodOrder;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, int agentId, String scanCabinetId, int batteryType,
                                 long activityId, long vipId, long priceId, int price,
                                 long couponTicketId, ConstEnum.PayType payType) {
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

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

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

        //查询套餐
        if (priceId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                PacketPeriodPriceRenew packetPeriodPriceRenew = packetPeriodPriceRenewMapper.find(priceId);
                if (packetPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }
                if (packetPeriodPriceRenew.getPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPriceRenew.getDayCount();
                limitCount = packetPeriodPriceRenew.getLimitCount();
                dayLimitCount = packetPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            } else {
                PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(priceId);
                if (packetPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }

                if (packetPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPrice.getDayCount();
                limitCount = packetPeriodPrice.getLimitCount();
                dayLimitCount = packetPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {

                VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew = vipPacketPeriodPriceRenewMapper.find(vipId);
                if (vipPacketPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPriceRenew.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPriceRenew.getDayCount();
                limitCount = vipPacketPeriodPriceRenew.getLimitCount();
                dayLimitCount = vipPacketPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }

            } else {
                VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipId);
                if (vipPacketPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPrice.getDayCount();
                limitCount = vipPacketPeriodPrice.getLimitCount();
                dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        if(activityId != 0) {
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

        //查询对应结算终端id
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        String cabinetId = customerExchangeInfo.getBalanceCabinetId();
        String stationId = customerExchangeInfo.getBalanceStationId();
        Integer vehicleForegiftFlag = customerExchangeInfo.getVehicleForegiftFlag();

        if (vehicleForegiftFlag != null && vehicleForegiftFlag == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        //存在柜子id和门店id都为空的情况，提示联系管理员，查明原因
        if (StringUtils.isEmpty(cabinetId) && StringUtils.isEmpty(stationId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐没有绑定结算换电柜或站点，请联系管理员处理");
        }

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, packetMoney, CustomerMultiOrder.Type.HD.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
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
        packetPeriodOrder.setScanCabinetId(scanCabinetId);
        packetPeriodOrder.setCabinetId(cabinetId);
        packetPeriodOrder.setBatteryType(batteryType);
        packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        packetPeriodOrder.setTicketMoney(price - packetMoney);
        packetPeriodOrder.setTicketName(ticketName);
        packetPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
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
        packetPeriodOrder.setPrice(price);
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


        NotNullMap map = new NotNullMap();
        map.putString("packetOrderId", packetPeriodOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, int agentId, String scanCabinetId, int batteryType,
                                 long activityId, long vipId, long priceId, int price,
                                 long couponTicketId, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

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

        //查询套餐
        if (priceId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                PacketPeriodPriceRenew packetPeriodPriceRenew = packetPeriodPriceRenewMapper.find(priceId);
                if (packetPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }
                if (packetPeriodPriceRenew.getPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPriceRenew.getDayCount();
                limitCount = packetPeriodPriceRenew.getLimitCount();
                dayLimitCount = packetPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            } else {
                PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(priceId);
                if (packetPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }

                if (packetPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPrice.getDayCount();
                limitCount = packetPeriodPrice.getLimitCount();
                dayLimitCount = packetPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {

                VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew = vipPacketPeriodPriceRenewMapper.find(vipId);
                if (vipPacketPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPriceRenew.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPriceRenew.getDayCount();
                limitCount = vipPacketPeriodPriceRenew.getLimitCount();
                dayLimitCount = vipPacketPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }

            } else {
                VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipId);
                if (vipPacketPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPrice.getDayCount();
                limitCount = vipPacketPeriodPrice.getLimitCount();
                dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        if(activityId != 0) {
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

        //查询对应结算终端id
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        String cabinetId = customerExchangeInfo.getBalanceCabinetId();
        String stationId = customerExchangeInfo.getBalanceStationId();
        Integer vehicleForegiftFlag = customerExchangeInfo.getVehicleForegiftFlag();

        if (vehicleForegiftFlag != null && vehicleForegiftFlag == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        //存在柜子id和门店id都为空的情况，提示联系管理员，查明原因
        if (StringUtils.isEmpty(cabinetId) && StringUtils.isEmpty(stationId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐没有绑定结算换电柜或站点，请联系管理员处理");
        }

        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
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
        packetPeriodOrder.setScanCabinetId(scanCabinetId);
        packetPeriodOrder.setCabinetId(cabinetId);
        packetPeriodOrder.setBatteryType(batteryType);
        packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        packetPeriodOrder.setTicketMoney(price - packetMoney);
        packetPeriodOrder.setTicketName(ticketName);
        packetPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
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
        packetPeriodOrder.setPrice(price);
        packetPeriodOrder.setLimitCount(limitCount);
        packetPeriodOrder.setDayLimitCount(dayLimitCount);
        packetPeriodOrder.setOrderCount(0);
        packetPeriodOrder.setConsumeDepositBalance(0);
        packetPeriodOrder.setConsumeGiftBalance(0);
        packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());

        packetPeriodOrderMapper.insert(packetPeriodOrder);


        String memo = String.format("租金:%.2f", packetMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(packetMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
            weixinPayOrder.setSourceId(packetPeriodOrder.getId());
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);

        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(packetMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
            weixinmpPayOrder.setSourceId(packetPeriodOrder.getId());
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);

        } else if(payType == ConstEnum.PayType.WEIXIN_MA) {
            WeixinmaPayOrder weixinmaPayOrder = new WeixinmaPayOrder();
            weixinmaPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMA_PAY_ORDER));
            weixinmaPayOrder.setPartnerId(customer.getPartnerId());
            weixinmaPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinmaPayOrder.setCustomerId(customerId);
            weixinmaPayOrder.setMobile(customer.getMobile());
            weixinmaPayOrder.setCustomerName(customer.getFullname());
            weixinmaPayOrder.setMoney(packetMoney);
            weixinmaPayOrder.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
            weixinmaPayOrder.setSourceId(packetPeriodOrder.getId());
            weixinmaPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmaPayOrder.setMemo(memo);
            weixinmaPayOrder.setCreateTime(new Date());
            weixinmaPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinmaPayOrderMapper.insert(weixinmaPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmaPayOrder);

        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), packetPeriodOrder.getId(), packetMoney, customerId, AlipayPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue(), "套餐订单支付", "套餐订单支付");
            map.put("orderId", packetPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), packetPeriodOrder.getAgentId(), packetPeriodOrder.getId(), packetMoney, customerId, AlipayPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue(), "套餐订单支付", "套餐订单支付");
            map.put("orderId", packetPeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId,  int agentId, String scanCabinetId, int batteryType,
                                 long activityId, long vipId, long priceId, int price, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        if (activityId == 0 && priceId == 0 && vipId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐必须使用一种");
        }

        if (activityId != 0 && priceId != 0 && vipId != 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "活动和套餐不能同时使用");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

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

        //查询套餐
        if (priceId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                PacketPeriodPriceRenew packetPeriodPriceRenew = packetPeriodPriceRenewMapper.find(priceId);
                if (packetPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }
                if (packetPeriodPriceRenew.getPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPriceRenew.getDayCount();
                limitCount = packetPeriodPriceRenew.getLimitCount();
                dayLimitCount = packetPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            } else {
                PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(priceId);
                if (packetPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
                }

                if (packetPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "套餐价格不正确，请确认");
                }
                dayCount = packetPeriodPrice.getDayCount();
                limitCount = packetPeriodPrice.getLimitCount();
                dayLimitCount = packetPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        //查询VIP租金套餐
        if (vipId != 0) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {

                VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew = vipPacketPeriodPriceRenewMapper.find(vipId);
                if (vipPacketPeriodPriceRenew == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPriceRenew.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPriceRenew.getDayCount();
                limitCount = vipPacketPeriodPriceRenew.getLimitCount();
                dayLimitCount = vipPacketPeriodPriceRenew.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }

            } else {
                VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipId);
                if (vipPacketPeriodPrice == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐不存在");
                }

                if (vipPacketPeriodPrice.getPrice() != price ) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "vip租金套餐价格不正确，请确认");
                }
                dayCount = vipPacketPeriodPrice.getDayCount();
                limitCount = vipPacketPeriodPrice.getLimitCount();
                dayLimitCount = vipPacketPeriodPrice.getDayLimitCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        }

        if(activityId != 0) {
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

        List<Integer> balanceList = new ArrayList<Integer>();

        RestResult restResult = updateCustomerBalance(customer, packetMoney, balanceList);
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        //查询对应结算终端id
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
        String cabinetId = customerExchangeInfo.getBalanceCabinetId();
        String stationId = customerExchangeInfo.getBalanceStationId();
        Integer vehicleForegiftFlag = customerExchangeInfo.getVehicleForegiftFlag();

        if (vehicleForegiftFlag != null && vehicleForegiftFlag == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已交纳租车押金，不能续租");
        }

        //存在柜子id和门店id都为空的情况，提示联系管理员，查明原因
        if (StringUtils.isEmpty(cabinetId) && StringUtils.isEmpty(stationId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐没有绑定结算换电柜或站点，请联系管理员处理");
        }

        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
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
        packetPeriodOrder.setScanCabinetId(scanCabinetId);
        packetPeriodOrder.setCabinetId(cabinetId);
        packetPeriodOrder.setBatteryType(batteryType);
        packetPeriodOrder.setActivityId(activityId == 0 ? null : activityId);
        packetPeriodOrder.setTicketMoney(price - packetMoney);
        packetPeriodOrder.setTicketName(ticketName);
        packetPeriodOrder.setCouponTicketId(couponTicketId == 0 ? null : couponTicketId);
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
        packetPeriodOrder.setPrice(price);
        packetPeriodOrder.setLimitCount(limitCount);
        packetPeriodOrder.setDayLimitCount(dayLimitCount);
        packetPeriodOrder.setOrderCount(0);
        packetPeriodOrder.setConsumeDepositBalance(balanceList.get(0));
        packetPeriodOrder.setConsumeGiftBalance(balanceList.get(1));
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
            inOutMoney.setBalance(customer.getBalance() - packetMoney );
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //租金赠送
        Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
        String sourceId = packetPeriodOrder.getId();
        Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();

        super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type,dayCount, agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());

        if (packetPeriodOrder.getCouponTicketId() != null) {
            if (customerCouponTicketMapper.useTicket(packetPeriodOrder.getCouponTicketId(),
                    new Date(),
                    CustomerCouponTicket.Status.NOT_USER.getValue(),
                    CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("优惠券不可用");
            }
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

        //余额充值，套餐充值成功,推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(packetPeriodOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //客户购买租金续租消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("换电续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * price / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        return RestResult.SUCCESS;
    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult refundOrder(String orderId, String reason, long customerId) {
        if (reason == null || "".equals(reason)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "申请原因不能为空");
        }

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(orderId);
        if (packetPeriodOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if (packetPeriodOrder.getStatus() != PacketPeriodOrder.Status.NOT_USE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该套餐状态不能申请退款");
        }

        PacketPeriodOrderRefund packetPeriodOrderRefund = new PacketPeriodOrderRefund();
        packetPeriodOrderRefund.setId(packetPeriodOrder.getId());
        packetPeriodOrderRefund.setAgentId(packetPeriodOrder.getAgentId());
        packetPeriodOrderRefund.setMoney(packetPeriodOrder.getMoney());
        packetPeriodOrderRefund.setCustomerId(customerId);
        packetPeriodOrderRefund.setCustomerMobile(customer.getMobile());
        packetPeriodOrderRefund.setCustomerFullname(customer.getFullname());
        packetPeriodOrderRefund.setApplyRefundTime(new Date());
        packetPeriodOrderRefund.setRefundStatus(PacketPeriodOrderRefund.RefundStatus.APPLY_REFUND.getValue());
        packetPeriodOrderRefund.setCreateTime(new Date());
        packetPeriodOrderRefund.setRefundReason(reason);
        packetPeriodOrderRefund.setRefundMoney(packetPeriodOrder.getMoney());

        packetPeriodOrderRefundMapper.insert(packetPeriodOrderRefund);

        if (packetPeriodOrderMapper.updateRefund(orderId, PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue()) == 0) {
            throw new IllegalArgumentException("update 退款 包时段订单状态出错");
        }

        return RestResult.SUCCESS;
    }

    public RestResult getList(long customerId, int offset, int limit) {
        List<Map> list = new ArrayList<Map>(limit);

        List<PacketPeriodOrder> packetPeriodOrders = packetPeriodOrderMapper.findNoStatusList(customerId, offset, limit, PacketPeriodOrder.Status.NOT_PAY.getValue());
        for (PacketPeriodOrder packetPeriodOrder : packetPeriodOrders) {
            Map line = new HashMap();
            line.put("id", packetPeriodOrder.getId());
            line.put("dayCount", packetPeriodOrder.getDayCount());
            line.put("beginDate", packetPeriodOrder.getBeginTime() == null ? "" : DateFormatUtils.format(packetPeriodOrder.getBeginTime(), Constant.DATE_TIME_FORMAT));
            line.put("endDate", packetPeriodOrder.getEndTime() == null ? "" : DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_TIME_FORMAT));
            line.put("money", packetPeriodOrder.getMoney());
            line.put("customerMobile", packetPeriodOrder.getCustomerMobile());
            line.put("createTime", DateFormatUtils.format(packetPeriodOrder.getCreateTime(), Constant.DATE_TIME_FORMAT));

            line.put("payTypeName", "");
            if (packetPeriodOrder.getPayType() != null) {
                line.put("payTypeName", ConstEnum.PayType.getName(packetPeriodOrder.getPayType()));
            }
            line.put("ticketName", "");
            Integer ticketMoney = packetPeriodOrder.getTicketMoney();
            if (ticketMoney != null) {
                String ticketName = packetPeriodOrder.getTicketName();
                if (ticketName == null) {
                    ticketName = "";
                }
                line.put("ticketName", String.format("%.2f", 1d * (ticketMoney / 100)) + "元" + ticketName);
            }

            line.put("status", packetPeriodOrder.getStatus());

            if (packetPeriodOrder.getPayTime() != null) {
                line.put("payTime", DateFormatUtils.format(packetPeriodOrder.getPayTime(), Constant.DATE_TIME_FORMAT));
            }
            line.put("limitCount",packetPeriodOrder.getLimitCount());
            line.put("orderCount",packetPeriodOrder.getOrderCount());
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public RestResult willExpireList(int agentId) {
        List<Map> list = new ArrayList<Map>();
        Date now = new Date();
        int expireDay = Integer.valueOf(systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.PACKET_PERIOD_ORDER_EXPIRE.getValue()));
        Date expireDate = DateUtils.addDays(now, expireDay);

        List<PacketPeriodOrder> packetPeriodOrders = packetPeriodOrderMapper.findExpireList(expireDate, agentId);
        for (PacketPeriodOrder packetPeriodOrder : packetPeriodOrders) {
            Map line = new HashMap();
            line.put("id", packetPeriodOrder.getId());
            line.put("customerFullname", packetPeriodOrder.getCustomerFullname());
            line.put("customerMobile", packetPeriodOrder.getCustomerMobile());
            String leavingDay = AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - now.getTime()) / 1000);
            line.put("leavingDay",leavingDay);
            line.put("endDate", packetPeriodOrder.getEndTime() == null ? "" : DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_FORMAT));
            line.put("cabinetName",packetPeriodOrder.getCabinetName());
            list.add(line);
        }

        Map returnMap = new HashMap();
        returnMap.put("expireDay",expireDay);
        returnMap.put("count",packetPeriodOrders.size());
        returnMap.put("orderList",list);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
    }

}
