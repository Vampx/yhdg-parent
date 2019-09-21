package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.*;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.MultiPayOrderService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VehicleMultiOrderService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(VehicleMultiOrderService.class);

    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    CustomerMultiPayDetailMapper customerMultiPayDetailMapper;
    @Autowired
    MultiPayOrderService multiPayOrderService;
    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;
    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public CustomerMultiOrder find(long id) {
        return customerMultiOrderMapper.find(id);
    }

    public List<CustomerMultiOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return customerMultiOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(Customer customer, long id, int money, ConstEnum.PayType payType) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderMapper.find(id);
        if (customerMultiOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "多通道支付订单不存在");
        }

        //创建多通道支付订单
        CustomerMultiPayDetail customerMultiPayDetail = addCustomerMultiPayDetail(customer, customerMultiOrder.getId(), payType.getValue(), money, CustomerMultiPayDetail.Status.NOT_PAY.getValue(), null);

        String memo = String.format("多通道支付金额:%.2f", money / 100.0);

        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customer.getId());
            weixinPayOrder.setMoney(money);
            weixinPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue());
            weixinPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
        }else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), customerMultiPayDetail.getId().toString(), money, customer.getId(), AlipayPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue(), "换电多通道订单支付", "换电多通道订单支付", memo);
            map.put("orderId", customerMultiPayDetail.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(customer.getAgentId());
            weixinmpPayOrder.setMoney(money);
            weixinmpPayOrder.setCustomerId(customer.getId());
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue());
            weixinmpPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        }else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(
                    customer.getPartnerId(),
                    customer.getAgentId(),
                    customerMultiPayDetail.getId().toString(),
                    money,
                    customer.getId(),
                    AlipayPayOrder.SourceType.MULTI_ORDER_VEHICLE_PAY.getValue(),
                    "租车多通道订单支付",
                    "租车多通道订单支付",
                    memo);
            map.put("orderId", customerMultiPayDetail.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(Customer customer, long id, int money) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderMapper.find(id);
        if(customerMultiOrder == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if ((customer.getBalance() + customer.getGiftBalance()) < money) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        RestResult restResult = updateCustomerBalance(customer, money, new ArrayList<Integer>());
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        //创建多通道支付订单
        CustomerMultiPayDetail customerMultiPayDetail = addCustomerMultiPayDetail(customer, customerMultiOrder.getId(), ConstEnum.PayType.BALANCE.getValue(), money, CustomerMultiPayDetail.Status.HAVE_PAY.getValue(), new Date());

        int hadPayMoney = customerMultiPayDetailMapper.sumMoneyByOrderIdAndStatus(customerMultiPayDetail.getOrderId(), CustomerMultiPayDetail.Status.HAVE_PAY.getValue());

        int debtMoney = customerMultiOrder.getTotalMoney() - hadPayMoney;
        int status=debtMoney>0?CustomerMultiOrder.Status.IN_PAY.getValue():CustomerMultiOrder.Status.HAVE_PAY.getValue();
        //更新多通道订单状态
        customerMultiOrderMapper.updateDebtMoneyAndStatus(customerMultiOrder.getId(), debtMoney, status);

        //添加流水
        if (customerMultiPayDetail.getMoney() > 0) {
            //客户流水（支出）
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(customer.getId());
            inOutMoney.setMoney(-customerMultiPayDetail.getMoney());
            inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_MULTI_PAY.getValue());
            inOutMoney.setBizId(customerMultiPayDetail.getId().toString());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(customer.getBalance());
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //付款完成
        if (debtMoney <= 0){
            List<CustomerMultiOrderDetail> customerMultiOrderDetailList = customerMultiOrderDetailMapper.findListByOrderId(customerMultiPayDetail.getOrderId());
            String groupOrderId = null;
            for(CustomerMultiOrderDetail orderDetail : customerMultiOrderDetailList){
                if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue()){
                    groupOrderId = orderDetail.getSourceId();
                }
            }
            WeixinPayOrder payOrder = new WeixinPayOrder();
            if(groupOrderId != null){
                payOrder.setSourceId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER.getValue() + ":" + groupOrderId);
                multiGroupOrderPayOk(payOrder);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    public int countMultiWaitPay(Long customerId, int type) {
        List statusList = new ArrayList();
        statusList.add(CustomerMultiOrder.Status.IN_PAY.getValue());
        return customerMultiOrderMapper.countMultiWaitPay(customerId, type, statusList);
    }

    public CustomerMultiPayDetail addCustomerMultiPayDetail(Customer customer, long orderId, int payType, int money, int status, Date payTime){
        CustomerMultiPayDetail customerMultiPayDetail = new CustomerMultiPayDetail();
        customerMultiPayDetail.setOrderId(orderId);
        customerMultiPayDetail.setPayType(payType);
        customerMultiPayDetail.setMoney(money);
        customerMultiPayDetail.setStatus(status);
        customerMultiPayDetail.setCustomerId(customer.getId());
        customerMultiPayDetail.setCustomerFullname(customer.getFullname());
        customerMultiPayDetail.setCustomerMobile(customer.getMobile());
        customerMultiPayDetail.setPartnerId(customer.getPartnerId());
        customerMultiPayDetail.setAgentId(customer.getAgentId());
        customerMultiPayDetail.setAgentName(customer.getAgentName());
        customerMultiPayDetail.setCreateTime(new Date());
        customerMultiPayDetail.setPayTime(payTime);
        customerMultiPayDetailMapper.insert(customerMultiPayDetail);
        return customerMultiPayDetail;
    }

    /**
     * 租车多通道组合订单支付
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void multiGroupOrderPayOk(PayOrder payOrder) {
        String groupOrderId = null;
        if (StringUtils.isNotEmpty(payOrder.getSourceId())) {
            String[] list = StringUtils.split(payOrder.getSourceId(), ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.VEHICLE_GROUP_ORDER.getValue()) {
                groupOrderId = list[1];
            }
        }

        GroupOrder groupOrder = null;
        if (StringUtils.isNotEmpty(groupOrderId)) {
            groupOrder = groupOrderMapper.find(groupOrderId);
        }

        Agent agent = null;
        Customer customer = null;
        if (groupOrder != null) {
            agent = agentMapper.find(groupOrder.getAgentId());
            customer = customerMapper.find(groupOrder.getCustomerId());
        }

        //组合订单
        if (groupOrder != null) {
            if (groupOrderMapper.payOk(groupOrder.getId(), new Date(), GroupOrder.Status.WAIT_PAY.getValue(), GroupOrder.Status.PAY_OK.getValue()) == 1) {
                if (groupOrder.getMoney() > 0) {

                    if (groupOrder.getDeductionTicketId() != null) {
                        //更新抵用券状态
                        customerCouponTicketMapper.setUsed(groupOrder.getDeductionTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }

                    if (groupOrder.getForegiftCouponTicketId() != null) {
                        //更新押金券状态
                        customerCouponTicketMapper.setUsed(groupOrder.getForegiftCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }

                    if (groupOrder.getRentCouponTicketId() != null) {
                        //更新租金券状态
                        customerCouponTicketMapper.setUsed(groupOrder.getRentCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }
                }

            } else {
                log.error("更新订单GroupOrder状态失败, id: {}", groupOrder.getId());
            }
        } else {
            log.error("GroupOrder订单不存在, {}", groupOrderId);
        }

        VehicleForegiftOrder vehicleForegiftOrder = null;
        if (groupOrder != null) {
            vehicleForegiftOrder = vehicleForegiftOrderMapper.find(groupOrder.getVehicleForegiftId());
        }

        if (vehicleForegiftOrder != null) {

            if (vehicleForegiftOrderMapper.payOk(vehicleForegiftOrder.getId(), new Date(), VehicleForegiftOrder.Status.WAIT_PAY.getValue(), VehicleForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //运营商首单赠送押金券
                int count = vehicleForegiftOrderMapper.findCountByCustomerId(vehicleForegiftOrder.getId(), vehicleForegiftOrder.getAgentId(), vehicleForegiftOrder.getCustomerId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送
                    int agentId = vehicleForegiftOrder.getAgentId() == null ? 0 : vehicleForegiftOrder.getAgentId();

                    String sourceId = vehicleForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type,0, agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), vehicleForegiftOrder.getCustomerMobile());
                }

                //回写用户运营商id
                customerMapper.updateAgent(vehicleForegiftOrder.getCustomerId(), vehicleForegiftOrder.getAgentId());

                if (log.isDebugEnabled()) {
                    log.debug("VehicleForegiftOrder id: {}支付成功", vehicleForegiftOrder.getId());
                }

                CustomerVehicleInfo customerVehicleInfo = new CustomerVehicleInfo();
                customerVehicleInfo.setId(vehicleForegiftOrder.getCustomerId());
                customerVehicleInfo.setAgentId(agent.getId());
                customerVehicleInfo.setForegift(vehicleForegiftOrder.getMoney());
                customerVehicleInfo.setForegiftOrderId(vehicleForegiftOrder.getId());
                customerVehicleInfo.setRentPriceId(groupOrder.getRentPriceId());
                customerVehicleInfo.setVipPriceId(groupOrder.getVipPriceId());
                customerVehicleInfo.setCategory(groupOrder.getCategory());
                customerVehicleInfo.setModelId(groupOrder.getModelId());
                customerVehicleInfo.setBatteryType(groupOrder.getBatteryType());
                customerVehicleInfo.setCreateTime(new Date());

                customerVehicleInfoMapper.insert(customerVehicleInfo);

                //支付宝或微信支付押金成功后，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(vehicleForegiftOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);

            } else {
                log.error("更新订单VehicleForegiftOrder状态失败, id: {}", vehicleForegiftOrder.getId());
            }
        } else {
            log.error("VehicleForegiftOrder 订单不存在, {}", "组合订单id" + groupOrderId);
        }

        VehiclePeriodOrder vehiclePeriodOrder = null;

        if (groupOrder != null) {
            vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehicleForegiftId());
        }

        if (vehiclePeriodOrder != null) {
            if (groupOrder.getVehicleForegiftId() == null) {
                //续租逻辑
                int modelId = 0;
                int dayCount = 0;
                RentPrice rentPrice = rentPriceMapper.find(groupOrder.getRentPriceId());
                if (rentPrice != null) {
                    PriceSetting priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
                    if (priceSetting != null) {
                        dayCount = rentPrice.getDayCount();
                        modelId = priceSetting.getModelId();
                    }
                }
                Date beginTime = null;
                Date endTime = null;
                int status = VehiclePeriodOrder.Status.NOT_USE.getValue();
                //没有使用中车辆，未使用变为已经使用

                VehiclePeriodOrder usedOrder = vehiclePeriodOrderMapper.findOneEnabled(customer.getId(), status, agent.getId(), modelId);
                if (usedOrder == null) {
                    beginTime = new Date();
                    endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);
                    status = VehiclePeriodOrder.Status.USED.getValue();
                }

                if (vehiclePeriodOrderMapper.payOk(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_PAY.getValue(), status, new Date(), beginTime, endTime) == 1) {

                    //租金赠送
                    int agentId = vehiclePeriodOrder.getAgentId() == null ? 0 : vehiclePeriodOrder.getAgentId();

                    String sourceId = vehiclePeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, vehiclePeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), vehiclePeriodOrder.getCustomerMobile());

                    if (log.isDebugEnabled()) {
                        log.debug("VehiclePeriodOrder id: {}支付成功", vehiclePeriodOrder.getId());
                    }

                    //套餐充值成功,推送
                    if (vehiclePeriodOrder != null) {
                        PushMetaData metaData = new PushMetaData();
                        metaData.setSourceId(vehiclePeriodOrder.getId());
                        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
                        metaData.setCreateTime(new Date());
                        pushMetaDataMapper.insert(metaData);
                    }

                } else {
                    log.error("更新订单VehiclePeriodOrder状态失败, id: {}", vehiclePeriodOrder.getId());
                }
            }else{
                //第一次购买租金，包含押金
                Date beginTime = new Date();
                Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, vehiclePeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                if (vehiclePeriodOrderMapper.payOk(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_PAY.getValue(), VehiclePeriodOrder.Status.NOT_USE.getValue(), new Date(), beginTime, endTime) == 1) {

                    //租金赠送
                    int agentId = vehiclePeriodOrder.getAgentId() == null ? 0 : vehiclePeriodOrder.getAgentId();

                    String sourceId = vehiclePeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, vehiclePeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), vehiclePeriodOrder.getCustomerMobile());

                    if (log.isDebugEnabled()) {
                        log.debug("VehiclePeriodOrder id: {}支付成功", vehiclePeriodOrder.getId());
                    }

                    //套餐充值成功,推送
                    if (vehiclePeriodOrder != null) {
                        PushMetaData metaData = new PushMetaData();
                        metaData.setSourceId(vehiclePeriodOrder.getId());
                        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
                        metaData.setCreateTime(new Date());
                        pushMetaDataMapper.insert(metaData);
                    }

                } else {
                    log.error("更新订单VehiclePeriodOrder状态失败, id: {}", vehiclePeriodOrder.getId());
                }
            }

        } else {
            log.error("VehiclePeriodOrder 订单不存在, {}", "组合订单id" + groupOrderId);
        }

        if (groupOrder != null) {
            if (groupOrder.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {

                CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                if (customerForegiftOrder != null) {

                    if (customerForegiftOrderMapper.payMultiOk(customerForegiftOrder.getId(), new Date(), new Date(), CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                        //更新用户换电押金状态
                        customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

                        //回写用户运营商id
                        customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());

                        CustomerExchangeInfo customerExchangeInfo = new CustomerExchangeInfo();
                        customerExchangeInfo.setId(customer.getId());
                        customerExchangeInfo.setAgentId(agent.getId());
                        customerExchangeInfo.setForegift(customerForegiftOrder.getMoney());
                        customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
                        customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
                        customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
                        customerExchangeInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
                        customerExchangeInfo.setBatteryType(customerForegiftOrder.getBatteryType());
                        customerExchangeInfo.setCreateTime(new Date());

                        customerExchangeInfoMapper.insert(customerExchangeInfo);

                    } else {
                        log.error("更新订单CustomerForegiftOrder状态失败, id: {}", customerForegiftOrder.getId());
                    }
                } else {
                    log.error("CustomerForegiftOrder 订单不存在, {}", "组合订单id" + groupOrderId);
                }

                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                if (packetPeriodOrder != null) {

                    if (packetPeriodOrderMapper.payOk(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_PAY.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue(),new Date()) == 1) {

                        if (log.isDebugEnabled()) {
                            log.debug("PacketPeriodOrder id: {}支付成功", packetPeriodOrder.getId());
                        }

                    } else {
                        log.error("更新订单PacketPeriodOrder状态失败, id: {}", packetPeriodOrder.getId());
                    }

                } else {
                    log.error("PacketPeriodOrder 订单不存在, {}", "组合订单id" + groupOrderId);
                }

            } else if (groupOrder.getCategory() == ConstEnum.Category.RENT.getValue()) {

                RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                if (rentForegiftOrder != null) {

                    if (rentForegiftOrderMapper.payMultiOk(rentForegiftOrder.getId(), new Date(), new Date(), RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                        //更新客户租电押金状态
                        customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());

                        //回写用户运营商id
                        customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

                        CustomerRentInfo customerRentInfo = new CustomerRentInfo();
                        customerRentInfo.setId(customer.getId());
                        customerRentInfo.setAgentId(agent.getId());
                        customerRentInfo.setBatteryType(rentForegiftOrder.getBatteryType());
                        customerRentInfo.setForegift(rentForegiftOrder.getMoney());
                        customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
                        customerRentInfo.setBalanceShopId(rentForegiftOrder.getShopId());
                        customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
                        customerRentInfo.setCreateTime(new Date());

                        customerRentInfoMapper.insert(customerRentInfo);

                    } else {
                        log.error("更新订单RentForegiftOrder状态失败, id: {}", rentForegiftOrder.getId());
                    }
                } else {
                    log.error("RentForegiftOrder 订单不存在, {}", "组合订单id" + groupOrderId);
                }

                RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                if (rentPeriodOrder != null) {

                    Date beginTime = new Date();
                    Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                    if (rentPeriodOrderMapper.payOk(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_PAY.getValue(), RentPeriodOrder.Status.NOT_USE.getValue(),new Date(), beginTime, endTime) == 1) {
                        //清空缓存
                        long customerId = customer.getId();
                        String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
                        memCachedClient.delete(key);

                        if (log.isDebugEnabled()) {
                            log.debug("RentPeriodOrder id: {}支付成功", rentPeriodOrder.getId());
                        }

                    } else {
                        log.error("更新订单RentPeriodOrder状态失败, id: {}", rentPeriodOrder.getId());
                    }

                } else {
                    log.error("RentPeriodOrder 订单不存在, {}", "组合订单id" + groupOrderId);
                }
            }
        }

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

        //客户购买租车押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租车，押金: ${foregiftMoney}，租金: ${packetPeriodMoney}。",
                new String[]{"${foregiftMoney}", "${packetPeriodMoney}"},
                new String[]{String.format("%.2f元", 1d * groupOrder.getForegiftMoney() / 100.0), String.format("%.2f元", 1d * groupOrder.getRentPeriodMoney() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }
}
