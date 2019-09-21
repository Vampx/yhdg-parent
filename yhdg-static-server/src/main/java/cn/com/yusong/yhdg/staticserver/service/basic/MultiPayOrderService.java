package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.persistence.basic.*;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.*;
import cn.com.yusong.yhdg.staticserver.persistence.zd.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MultiPayOrderService extends AbstractService {
    static Logger log = LogManager.getLogger(MultiPayOrderService.class);

    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;
    @Autowired
    MobileMessageMapper mobileMessageMapper;
    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;
    @Autowired
    RentActivityCustomerMapper rentActivityCustomerMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    LaxinPayOrderMapper laxinPayOrderMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinSettingMapper laxinSettingMapper;
    @Autowired
    AgentDepositOrderMapper agentDepositOrderMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    DeductionTicketOrderMapper deductionTicketOrderMapper;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    CustomerMultiPayDetailMapper customerMultiPayDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;

    /**
     * 客户电池押金
     *
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void foregiftOrderPayOk(PayOrder payOrder) {
        String[] sourceIdList = StringUtils.split(payOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            }
        }

        int foregiftMoney = 0, packetPeriodMoney = 0;
        int foregiftPrice = 0, packetPeriodPrice = 0;

        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(foregiftOrderfId);
        if (customerForegiftOrder != null) {
            foregiftPrice = customerForegiftOrder.getPrice();
            foregiftMoney = customerForegiftOrder.getMoney();

            if (customerForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), new Date(), CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //更新用户换电押金状态
                customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());
                if (customerForegiftOrder.getDeductionTicketId() != null) {
                    //更新电池抵用券状态
                    customerCouponTicketMapper.setUsed(customerForegiftOrder.getDeductionTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());

                    CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(customerForegiftOrder.getDeductionTicketId());
                    //保存抵扣券订单
                    insertDeductionTicketOrder(customerForegiftOrder, customerCouponTicket);
                }

                if (customerForegiftOrder.getCouponTicketId() != null) {
                    //更新押金券状态
                    customerCouponTicketMapper.setUsed(customerForegiftOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                }

                //运营商首单赠送押金券
                int count = customerForegiftOrderMapper.findCountByCustomerId(customerForegiftOrder.getId(), customerForegiftOrder.getAgentId(), customerForegiftOrder.getCustomerId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送
                    int agentId = customerForegiftOrder.getAgentId() == null ? 0 : customerForegiftOrder.getAgentId();

                    String sourceId = customerForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type,0, agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customerForegiftOrder.getCustomerMobile());
                }

                Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());

                //回写用户运营商id
                customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());
                //更新用户归属柜子
                if(StringUtils.isEmpty(customer.getBelongCabinetId() )){
                    customerMapper.updateCabinet(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getCabinetId());
                }


                if (log.isDebugEnabled()) {
                    log.debug("CustomerForegiftOrder id: {}支付成功", foregiftOrderfId);
                }

                CustomerExchangeInfo customerExchangeInfo = new CustomerExchangeInfo();
                customerExchangeInfo.setId(customerForegiftOrder.getCustomerId());
                customerExchangeInfo.setAgentId(customerForegiftOrder.getAgentId());
                customerExchangeInfo.setBatteryType(customerForegiftOrder.getBatteryType());
                customerExchangeInfo.setForegift(customerForegiftOrder.getMoney());
                customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
                customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
                customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
                customerExchangeInfo.setBalanceStationId(customerForegiftOrder.getStationId());
                customerExchangeInfo.setVehicleForegiftFlag(ConstEnum.Flag.FALSE.getValue());
                customerExchangeInfo.setCreateTime(new Date());
                customerExchangeInfoMapper.insert(customerExchangeInfo);

                //押金加入押金池
                handleAgentForegift(customerForegiftOrder);

                Agent agent = agentMapper.find(customerForegiftOrder.getAgentId());

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

                //支付宝或微信支付押金成功后，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(customerForegiftOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);

            } else {
                log.error("更新订单CustomerForegiftOrder状态失败, id: {}", foregiftOrderfId);
            }
        } else {
            log.error("CustomerForegiftOrder 订单不存在, {}", foregiftOrderfId);
        }

        if (packetPeriodOrderId != null) {
            PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(packetPeriodOrderId);
            if (packetPeriodOrder != null) {
                packetPeriodPrice = packetPeriodOrder.getPrice();
                packetPeriodMoney = packetPeriodOrder.getMoney();

                if (packetPeriodOrderMapper.payOk(packetPeriodOrderId, PacketPeriodOrder.Status.NOT_PAY.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue(),new Date()) == 1) {

                    if (packetPeriodOrder.getCouponTicketId() != null) {
                        //更新租金券状态
                        customerCouponTicketMapper.setUsed(packetPeriodOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }

                    //租金赠送
                    int agentId = packetPeriodOrder.getAgentId() == null ? 0 : packetPeriodOrder.getAgentId();

                    String sourceId = packetPeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, packetPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());


                    if (packetPeriodOrder.getActivityId() != null && activityCustomerMapper.exist(packetPeriodOrder.getActivityId(), packetPeriodOrder.getCustomerId()) == 0) {
                        Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
                        ActivityCustomer activityCustomer = new ActivityCustomer();
                        activityCustomer.setActivityId(packetPeriodOrder.getActivityId());
                        activityCustomer.setCustomerId(packetPeriodOrder.getCustomerId());
                        activityCustomer.setFullname(customer.getFullname());
                        activityCustomer.setMobile(customer.getMobile());
                        activityCustomer.setCreateTime(new Date());
                        activityCustomerMapper.insert(activityCustomer);
                        //  activityMapper.refreshJoinCount(packetPeriodOrder.getActivityId());
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("PacketPeriodOrder id: {}支付成功", packetPeriodOrderId);
                    }

                } else {
                    log.error("更新订单PacketPeriodOrder状态失败, id: {}", packetPeriodOrderId);
                }

            } else {
                log.error("PacketPeriodOrder 订单不存在, {}", packetPeriodOrderId);
            }
        }

        Agent agent = agentMapper.find(customerForegiftOrder.getAgentId());
        Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());
       /* if (agent != null && customer != null) {
            handleLaxinCustomer(agent, customer, foregiftMoney, packetPeriodMoney);
        }*/

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftPrice}，租金: ${packetPrice}。",
                new String[]{"${foregiftPrice}", "${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * foregiftPrice / 100.0), String.format("%.2f元", 1d * packetPeriodPrice / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

    /**
     * 客户租电电池押金
     *
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rentForegiftOrderPayOk(PayOrder payOrder) {
        String[] sourceIdList = StringUtils.split(payOrder.getSourceId(), ",");
        String foregiftOrderfId = null, rentPeriodOrderId = null, rentInsuranceOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                rentPeriodOrderId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue()) {
                rentInsuranceOrderId = list[1];
            }
        }

        int foregiftMoney = 0, rentPeriodMoney = 0;
        int foregiftPrice = 0, rentPeriodPrice = 0;

        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(foregiftOrderfId);
        if (rentForegiftOrder != null) {
            foregiftPrice = rentForegiftOrder.getPrice();
            foregiftMoney = rentForegiftOrder.getMoney();

            if (rentForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), new Date(), RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //更新客户租电押金状态
                customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());
                if (rentForegiftOrder.getDeductionTicketId() != null) {
                    //更新电池抵用券状态
                    customerCouponTicketMapper.setUsed(rentForegiftOrder.getDeductionTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());

                    CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(rentForegiftOrder.getDeductionTicketId());
                    //保存抵扣券订单
                    insertRentDeductionTicketOrder(rentForegiftOrder, customerCouponTicket);
                }

                if (rentForegiftOrder.getCouponTicketId() != null) {
                    //更新押金券状态
                    customerCouponTicketMapper.setUsed(rentForegiftOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                }

                //运营商首单赠送押金券
                int count = rentForegiftOrderMapper.findCountByCustomerId(rentForegiftOrder.getId(), rentForegiftOrder.getAgentId(), rentForegiftOrder.getCustomerId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送

                    int agentId = rentForegiftOrder.getAgentId() == null ? 0 : rentForegiftOrder.getAgentId();

                    String sourceId = rentForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, 0, agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), rentForegiftOrder.getCustomerMobile());
                }

                Customer customer = customerMapper.find(rentForegiftOrder.getCustomerId());

                //回写用户运营商id
                customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

                if (log.isDebugEnabled()) {
                    log.debug("CustomerForegiftOrder id: {}支付成功", foregiftOrderfId);
                }

                CustomerRentInfo customerRentInfo = new CustomerRentInfo();
                customerRentInfo.setId(rentForegiftOrder.getCustomerId());
                customerRentInfo.setAgentId(rentForegiftOrder.getAgentId());
                customerRentInfo.setBatteryType(rentForegiftOrder.getBatteryType());
                customerRentInfo.setForegift(rentForegiftOrder.getMoney());
                customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
                customerRentInfo.setBalanceShopId(rentForegiftOrder.getShopId());
                customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.FALSE.getValue());
                customerRentInfo.setCreateTime(new Date());
                customerRentInfoMapper.insert(customerRentInfo);


                //押金加入押金池
                handleZdAgentForegift(rentForegiftOrder);

                Agent agent = agentMapper.find(rentForegiftOrder.getAgentId());
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
                Shop shop = shopMapper.find(rentForegiftOrder.getShopId());
                Battery battery = batteryMapper.find(rentForegiftOrder.getBatteryId());

                //生成租电订单
                RentOrder rentOrder = new RentOrder();
                rentOrder.setId(newOrderId(OrderId.OrderIdType.RENT_ORDER));
                rentOrder.setPartnerId(agent.getPartnerId());
                rentOrder.setAgentId(agent.getId());
                rentOrder.setAgentName(agent.getAgentName());
                rentOrder.setAgentCode(agent.getAgentCode());
                rentOrder.setShopId(shop.getId());
                rentOrder.setShopName(shop.getShopName());
                rentOrder.setCustomerId(customer.getId());
                rentOrder.setCustomerMobile(customer.getMobile());
                rentOrder.setCustomerFullname(customer.getFullname());
                rentOrder.setBatteryType(rentForegiftOrder.getBatteryType());
                rentOrder.setBatteryId(rentForegiftOrder.getBatteryId());
                rentOrder.setStatus(RentOrder.Status.RENT.getValue());
                rentOrder.setCurrentVolume(battery.getVolume());
                rentOrder.setCurrentDistance(0);
                rentOrder.setCreateTime(new Date());
                rentOrderMapper.insert(rentOrder);

                //租金成功就绑定电池
                CustomerRentBattery customerRentBattery = new CustomerRentBattery();
                customerRentBattery.setCustomerId(rentForegiftOrder.getCustomerId());
                customerRentBattery.setAgentId(rentForegiftOrder.getAgentId());
                customerRentBattery.setBatteryId(rentForegiftOrder.getBatteryId());
                customerRentBattery.setBatteryType(rentForegiftOrder.getBatteryType());
                customerRentBattery.setRentOrderId(rentOrder.getId());
                customerRentBatteryMapper.insert(customerRentBattery);

                //库存表去除该电池
                shopStoreBatteryMapper.clearBattery(rentForegiftOrder.getShopId(), rentForegiftOrder.getBatteryId());

                //电池使用
                batteryMapper.updateCustomerUse(rentForegiftOrder.getBatteryId(), Battery.Status.CUSTOMER_OUT.getValue(), rentOrder.getId(), new Date(), rentForegiftOrder.getCustomerId(), rentForegiftOrder.getCustomerMobile(), rentForegiftOrder.getCustomerFullname());



                //支付宝或微信支付押金成功后，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(rentForegiftOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.RENT_FOREGIFT_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);

            } else {
                log.error("更新订单CustomerForegiftOrder状态失败, id: {}", foregiftOrderfId);
            }
        } else {
            log.error("CustomerForegiftOrder 订单不存在, {}", foregiftOrderfId);
        }

        if (rentPeriodOrderId != null) {
            RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(rentPeriodOrderId);
            if (rentPeriodOrder != null) {
                rentPeriodPrice = rentPeriodOrder.getPrice();
                rentPeriodMoney = rentPeriodOrder.getMoney();

                Date beginTime = new Date();
                Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                if (rentPeriodOrderMapper.payOk(rentPeriodOrderId, RentPeriodOrder.Status.NOT_PAY.getValue(), RentPeriodOrder.Status.USED.getValue(),new Date(), beginTime, endTime) == 1) {
                    //清空缓存
                    String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
                    memCachedClient.delete(key);

                    if (rentPeriodOrder.getCouponTicketId() != null) {
                        //更新租金券状态
                        customerCouponTicketMapper.setUsed(rentPeriodOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }

                    //租金赠送
                    int agentId = rentPeriodOrder.getAgentId() == null ? 0 : rentPeriodOrder.getAgentId();

                    String sourceId = rentPeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type,rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

                    if (rentPeriodOrder.getActivityId() != null && activityCustomerMapper.exist(rentPeriodOrder.getActivityId(), rentPeriodOrder.getCustomerId()) == 0) {
                        Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
                        RentActivityCustomer activityCustomer = new RentActivityCustomer();
                        activityCustomer.setActivityId(rentPeriodOrder.getActivityId());
                        activityCustomer.setCustomerId(rentPeriodOrder.getCustomerId());
                        activityCustomer.setFullname(customer.getFullname());
                        activityCustomer.setMobile(customer.getMobile());
                        activityCustomer.setCreateTime(new Date());
                        rentActivityCustomerMapper.insert(activityCustomer);
                        //  activityMapper.refreshJoinCount(rentPeriodOrder.getActivityId());
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("PacketPeriodOrder id: {}支付成功", rentPeriodOrderId);
                    }

                } else {
                    log.error("更新订单PacketPeriodOrder状态失败, id: {}", rentPeriodOrderId);
                }

            } else {
                log.error("PacketPeriodOrder 订单不存在, {}", rentPeriodOrderId);
            }
        }

        if (rentInsuranceOrderId != null) {
            RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderMapper.find(rentInsuranceOrderId);
            if (rentInsuranceOrder != null) {
                if (rentInsuranceOrderMapper.payOk(rentInsuranceOrderId, RentInsuranceOrder.Status.NOT_PAY.getValue(), RentInsuranceOrder.Status.PAID.getValue(),new Date()) == 1) {

                    if (log.isDebugEnabled()) {
                        log.debug("insuranceOrder id: {}支付成功", rentInsuranceOrderId);
                    }

                } else {
                    log.error("更新订单insuranceOrder状态失败, id: {}", rentInsuranceOrderId);
                }

            } else {
                log.error("PacketPeriodOrder 订单不存在, {}", rentInsuranceOrderId);
            }
        }

        Agent agent = agentMapper.find(rentForegiftOrder.getAgentId());
        Customer customer = customerMapper.find(rentForegiftOrder.getCustomerId());

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftPrice}，租金: ${packetPrice}。",
                new String[]{"${foregiftPrice}", "${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * foregiftPrice / 100.0), String.format("%.2f元", 1d * rentPeriodPrice / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void packetPeriodOrderPayOk(PayOrder payOrder) {
        String packetPeriodOrderId = payOrder.getSourceId();
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(payOrder.getSourceId());
        if (packetPeriodOrder != null) {
            if (packetPeriodOrderMapper.payOk(payOrder.getSourceId(), PacketPeriodOrder.Status.NOT_PAY.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue(),new Date()) == 1) {

                //租金赠送
                int agentId = packetPeriodOrder.getAgentId() == null ? 0 : packetPeriodOrder.getAgentId();

                String sourceId = packetPeriodOrder.getId();
                Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
                Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, packetPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());

                Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());

                if (packetPeriodOrder.getCouponTicketId() != null) {
                    customerCouponTicketMapper.setUsed(packetPeriodOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                }

                if (packetPeriodOrder.getActivityId() != null && activityCustomerMapper.exist(packetPeriodOrder.getActivityId(), packetPeriodOrder.getCustomerId()) == 0) {

                    ActivityCustomer activityCustomer = new ActivityCustomer();
                    activityCustomer.setActivityId(packetPeriodOrder.getActivityId());
                    activityCustomer.setCustomerId(packetPeriodOrder.getCustomerId());
                    activityCustomer.setFullname(customer.getFullname());
                    activityCustomer.setMobile(customer.getMobile());
                    activityCustomer.setCreateTime(new Date());
                    activityCustomerMapper.insert(activityCustomer);
                }

                //微信或支付宝，客户套餐充值成功，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(packetPeriodOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);

                if (log.isDebugEnabled()) {
                    log.debug("PacketPeriodOrder id: {}支付成功", packetPeriodOrderId);
                }

            } else {
                log.error("更新订单PacketPeriodOrder状态失败, id: {}", packetPeriodOrderId);
            }

        } else {
            log.error("PacketPeriodOrder 订单不存在, {}", packetPeriodOrderId);
        }

        Agent agent = agentMapper.find(packetPeriodOrder.getAgentId());
        Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
        //客户购买租金续租消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * packetPeriodOrder.getPrice() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

    /**
     * 租电包时段支付
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rentPeriodOrderPayOk(PayOrder payOrder) {
        String rentPeriodOrderId = payOrder.getSourceId();
        RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(payOrder.getSourceId());
        if (rentPeriodOrder != null) {
            Date beginTime = null;
            Date endTime = null;
            int status = RentPeriodOrder.Status.NOT_USE.getValue();
            //没有使用中电池，为使用变为已经使用
            RentPeriodOrder usedOrder = rentPeriodOrderMapper.findOneEnabled(rentPeriodOrder.getCustomerId(), RentPeriodOrder.Status.USED.getValue(), rentPeriodOrder.getAgentId(), rentPeriodOrder.getBatteryType());
            if (usedOrder == null) {
                beginTime = new Date();
                endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                status = RentPeriodOrder.Status.USED.getValue();
            }

            if (rentPeriodOrderMapper.payOk(payOrder.getSourceId(), RentPeriodOrder.Status.NOT_PAY.getValue(), status ,new Date(), beginTime, endTime) == 1) {
               //清空缓存
                String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
                memCachedClient.delete(key);

                //租金赠送
                int agentId = rentPeriodOrder.getAgentId() == null ? 0 : rentPeriodOrder.getAgentId();

                String sourceId = rentPeriodOrder.getId();
                Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
                Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(),agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

                Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());

                if (rentPeriodOrder.getCouponTicketId() != null) {
                    customerCouponTicketMapper.setUsed(rentPeriodOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                }

                if (rentPeriodOrder.getActivityId() != null && activityCustomerMapper.exist(rentPeriodOrder.getActivityId(), rentPeriodOrder.getCustomerId()) == 0) {

                    ActivityCustomer activityCustomer = new ActivityCustomer();
                    activityCustomer.setActivityId(rentPeriodOrder.getActivityId());
                    activityCustomer.setCustomerId(rentPeriodOrder.getCustomerId());
                    activityCustomer.setFullname(customer.getFullname());
                    activityCustomer.setMobile(customer.getMobile());
                    activityCustomer.setCreateTime(new Date());
                    activityCustomerMapper.insert(activityCustomer);
                }

                //微信或支付宝，客户套餐充值成功，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(rentPeriodOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.RENT_PERIOD_ORDER_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);

                if (log.isDebugEnabled()) {
                    log.debug("RentPeriodOrder id: {}支付成功", rentPeriodOrderId);
                }

            } else {
                log.error("更新订单RentPeriodOrder状态失败, id: {}", rentPeriodOrderId);
            }

        } else {
            log.error("RentPeriodOrder 订单不存在, {}", rentPeriodOrderId);
        }

        Agent agent = agentMapper.find(rentPeriodOrder.getAgentId());
        Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
        //客户购买租金续租消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * rentPeriodOrder.getPrice() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }


    private void insertDeductionTicketOrder(CustomerForegiftOrder customerForegiftOrder, CustomerCouponTicket deductionTicket){
        if(customerForegiftOrder.getDeductionTicketMoney()>0) {
            DeductionTicketOrder order = new DeductionTicketOrder();
            order.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            order.setAgentId(customerForegiftOrder.getAgentId());
            order.setAgentName(customerForegiftOrder.getAgentName());
            order.setAgentCode(customerForegiftOrder.getAgentCode());
            order.setCustomerId(customerForegiftOrder.getCustomerId());
            order.setFullname(customerForegiftOrder.getCustomerFullname());
            order.setMobile(customerForegiftOrder.getCustomerMobile());
            order.setCreateTime(new Date());
            order.setMoney(customerForegiftOrder.getDeductionTicketMoney());
            order.setTicketMoney(deductionTicket.getMoney());
            deductionTicketOrderMapper.insert(order);
        }
    }

    private void insertRentDeductionTicketOrder(RentForegiftOrder rentForegiftOrder, CustomerCouponTicket deductionTicket){
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

    /**
     * 运营商押金池
     * @param customerForegiftOrder
     */
    private void handleAgentForegift(CustomerForegiftOrder customerForegiftOrder) {
        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(customerForegiftOrder.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateForegift(customerForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
            inOutMoney.setAgentId(customerForegiftOrder.getAgentId());
            inOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            inOutMoney.setMoney(customerForegiftOrder.getMoney());
            inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_CUSTOMER_FOREGIFT.getValue());
            inOutMoney.setBizId(customerForegiftOrder.getId());
            inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
            inOutMoney.setBalance(foregiftBalance);
            inOutMoney.setRemainMoney(foregiftRemainMoney);
            inOutMoney.setRatio(foregiftBalanceRatio);
            inOutMoney.setOperator(customerForegiftOrder.getCustomerFullname());
            inOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(inOutMoney);
        }
    }
	
	  /**
     * 运营商租电押金池
     * @param rentForegiftOrder
     */
    private void handleZdAgentForegift(RentForegiftOrder rentForegiftOrder) {
        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(rentForegiftOrder.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateZdForegift(rentForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
            inOutMoney.setAgentId(rentForegiftOrder.getAgentId());
            inOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
            inOutMoney.setMoney(rentForegiftOrder.getMoney());
            inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_CUSTOMER_FOREGIFT.getValue());
            inOutMoney.setBizId(rentForegiftOrder.getId());
            inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
            inOutMoney.setBalance(foregiftBalance);
            inOutMoney.setRemainMoney(foregiftRemainMoney);
            inOutMoney.setRatio(foregiftBalanceRatio);
            inOutMoney.setOperator(rentForegiftOrder.getCustomerFullname());
            inOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(inOutMoney);
        }
    }


    /**
     * 多通道支付回调
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void multiPayDetailPayOk(PayOrder payOrder) {
        Long multiPayDetailId = null;
        try {
            multiPayDetailId = Long.parseLong(payOrder.getSourceId());
        }catch (Exception e){
            log.error("payOrderService.multiOrderPayOk, 多通道支付订单ID有误, multiPayDetailId: {}", payOrder.getSourceId());
            e.printStackTrace();
        }

        CustomerMultiPayDetail customerMultiPayDetail = customerMultiPayDetailMapper.find(multiPayDetailId);
        if(customerMultiPayDetail == null){
            log.error("CustomerMultiPayDetail 订单不存在, {}", payOrder.getSourceId());
        }

        //更新多通道支付订单状态
        if(customerMultiPayDetailMapper.payOk(multiPayDetailId, new Date(), CustomerMultiPayDetail.Status.NOT_PAY.getValue(), CustomerMultiPayDetail.Status.HAVE_PAY.getValue())==1) {

            int hadPayMoney = customerMultiPayDetailMapper.sumMoneyByOrderIdAndStatus(customerMultiPayDetail.getOrderId(), CustomerMultiPayDetail.Status.HAVE_PAY.getValue());

            CustomerMultiOrder customerMultiOrder = customerMultiOrderMapper.find(customerMultiPayDetail.getOrderId());
            if(customerMultiOrder == null){
                log.error("CustomerMultiOrder 订单不存在, {}", customerMultiPayDetail.getOrderId());
            }

            int debtMoney = customerMultiOrder.getTotalMoney() - hadPayMoney;
            int status=debtMoney>0?CustomerMultiOrder.Status.IN_PAY.getValue():CustomerMultiOrder.Status.HAVE_PAY.getValue();
            //更新多通道订单状态
            customerMultiOrderMapper.updateDebtMoneyAndStatus(customerMultiOrder.getId(), debtMoney, status);

            //添加流水
            Customer customer = customerMapper.find(customerMultiOrder.getCustomerId());
            if (customerMultiPayDetail.getMoney() > 0) {
                //客户流水（收入）
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customer.getId());
                inOutMoney.setMoney(customerMultiPayDetail.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(customerMultiPayDetail.getId().toString());
                inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(customer.getBalance() + customerMultiPayDetail.getMoney());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
                //客户流水（支出）
                inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customer.getId());
                inOutMoney.setMoney(-customerMultiPayDetail.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_MULTI_PAY.getValue());
                inOutMoney.setBizId(customerMultiPayDetail.getId().toString());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(customer.getBalance());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);

                //商户流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(customerMultiPayDetail.getPayType()));
                partnerInOutMoney.setPartnerId(customer.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_MULTI_PAY.getValue());
                partnerInOutMoney.setBizId(customerMultiPayDetail.getId().toString());
                partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(customerMultiPayDetail.getMoney());
                partnerInOutMoney.setOperator(customer.getFullname());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);
            }


            List<CustomerMultiOrderDetail> customerMultiOrderDetailList = customerMultiOrderDetailMapper.findListByOrderId(customerMultiPayDetail.getOrderId());

            if(customerMultiOrderDetailList.isEmpty()){
                log.error("多渠道支付订单明细不存在,bas_customer_multi_order_detail,order_id:{}", customerMultiPayDetail.getOrderId());
                throw new UnsupportedOperationException();
            }

            //付款完成
            if (debtMoney <= 0){
                String customerForegiftOrderId = null;
                String packetPeriodOrderId = null;
                String insuranceOrderId = null;
                String rentForegiftOrderId = null;
                String rentPeriodOrderId = null;
                String rentInsuranceOrderId = null;
                for(CustomerMultiOrderDetail orderDetail : customerMultiOrderDetailList){
                    if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.HDGFOREGIFT.getValue()){
                        customerForegiftOrderId = orderDetail.getSourceId();
                    }else if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.HDGPACKETPERIOD.getValue()){
                        packetPeriodOrderId = orderDetail.getSourceId();
                    }else if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.HDGINSURANCE.getValue()){
                        insuranceOrderId = orderDetail.getSourceId();
                    }else if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.ZDFOREGIFT.getValue()){
                        rentForegiftOrderId = orderDetail.getSourceId();
                    }else if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.ZDPACKETPERIOD.getValue()){
                        rentPeriodOrderId = orderDetail.getSourceId();
                    }else if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.ZDINSURANCE.getValue()){
                        rentInsuranceOrderId = orderDetail.getSourceId();
                    }
                }
                String sourceId;
                if(customerForegiftOrderId != null){
                    sourceId = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue() + ":" + customerForegiftOrderId;
                    if (packetPeriodOrderId != null) {
                        sourceId += "," + OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue() + ":" + packetPeriodOrderId;
                    }
                    if (insuranceOrderId != null) {
                        sourceId += "," + OrderId.OrderIdType.INSURANCE_ORDER.getValue() + ":" + insuranceOrderId;
                    }
                    payOrder.setSourceId(sourceId);
                    foregiftOrderPayOk(payOrder);
                }else if(rentForegiftOrderId != null){
                    sourceId = OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue() + ":" + rentForegiftOrderId;
                    if (rentPeriodOrderId != null) {
                        sourceId += "," + OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue() + ":" + rentPeriodOrderId;
                    }
                    if (rentInsuranceOrderId != null) {
                        sourceId += "," + OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue() + ":" + rentInsuranceOrderId;
                    }
                    payOrder.setSourceId(sourceId);
                    rentForegiftOrderPayOk(payOrder);
                }else if(packetPeriodOrderId != null){
                    payOrder.setSourceId(packetPeriodOrderId);
                    packetPeriodOrderPayOk(payOrder);
                }else if(rentInsuranceOrderId != null){
                    payOrder.setSourceId(rentInsuranceOrderId);
                    rentPeriodOrderPayOk(payOrder);
                }else {
                    log.error("customerMultiOrderDetailList的源订单ID（source_id）未在枚举CustomerMultiOrderDetail.SourceType中, {}", customerMultiPayDetail.getOrderId());
                    throw new UnsupportedOperationException();
                }
            }

        } else {
            log.error("更新订单CustomerMultiPayDetail状态失败, id: {}", multiPayDetailId);
        }
    }
}
