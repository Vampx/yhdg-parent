package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.zc.CustomerVehicleInfoMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehiclePeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

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
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinSettingMapper laxinSettingMapper;
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

    /**
     * 客户电池押金
     *
     * @param payOrder
     */
    public void foregiftOrderPayOk(PayOrder payOrder) {
        String[] sourceIdList = StringUtils.split(payOrder.getSourceId(), ",");
        String foregiftOrderfId = null, packetPeriodOrderId = null, insuranceOrderId = null;

        for (String e : sourceIdList) {
            String[] list = StringUtils.split(e, ":");
            if (Integer.parseInt(list[0]) == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                foregiftOrderfId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                packetPeriodOrderId = list[1];
            } else if (Integer.parseInt(list[0]) == OrderId.OrderIdType.INSURANCE_ORDER.getValue()) {
                insuranceOrderId = list[1];
            }
        }

        int foregiftMoney = 0, packetPeriodMoney = 0;
        int foregiftPrice = 0, packetPeriodPrice = 0;

        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(foregiftOrderfId);
        if (customerForegiftOrder != null) {
            foregiftPrice = customerForegiftOrder.getPrice();
            foregiftMoney = customerForegiftOrder.getMoney();

            if (customerForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

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

                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
                    String sourceId = customerForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, 0,agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customerForegiftOrder.getCustomerMobile());
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
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
                    String sourceId = packetPeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();

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

        if (insuranceOrderId != null) {
            InsuranceOrder insuranceOrder = insuranceOrderMapper.find(insuranceOrderId);
            if (insuranceOrder != null) {
                if (insuranceOrderMapper.payOk(insuranceOrderId, InsuranceOrder.Status.NOT_PAY.getValue(), InsuranceOrder.Status.PAID.getValue(),new Date()) == 1) {

                    if (log.isDebugEnabled()) {
                        log.debug("insuranceOrder id: {}支付成功", insuranceOrderId);
                    }

                } else {
                    log.error("更新订单insuranceOrder状态失败, id: {}", insuranceOrderId);
                }

            } else {
                log.error("PacketPeriodOrder 订单不存在, {}", insuranceOrderId);
            }
        }

        Agent agent = agentMapper.find(customerForegiftOrder.getAgentId());
        Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());
        /*if (agent != null && customer != null) {
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

            if (rentForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

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

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type,0, agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), rentForegiftOrder.getCustomerMobile());
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
                rentOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
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

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

                    if (rentPeriodOrder.getActivityId() != null && activityCustomerMapper.exist(rentPeriodOrder.getActivityId(), rentPeriodOrder.getCustomerId()) == 0) {
                        Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
                        RentActivityCustomer activityCustomer = new RentActivityCustomer();
                        activityCustomer.setActivityId(rentPeriodOrder.getActivityId());
                        activityCustomer.setCustomerId(rentPeriodOrder.getCustomerId());
                        activityCustomer.setFullname(customer.getFullname());
                        activityCustomer.setMobile(customer.getMobile());
                        activityCustomer.setCreateTime(new Date());
                        rentActivityCustomerMapper.insert(activityCustomer);
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

                super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

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

}
