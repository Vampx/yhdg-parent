package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.persistence.basic.*;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.*;
import cn.com.yusong.yhdg.staticserver.persistence.zc.*;
import cn.com.yusong.yhdg.staticserver.persistence.zd.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.com.yusong.yhdg.common.domain.basic.OrderId.OrderIdType.VEHICLE_GROUP_ORDER;

@Service
public class PayOrderService extends AbstractService {
    static Logger log = LogManager.getLogger(PayOrderService.class);

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
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
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
    @Autowired
    GroupOrderMapper groupOrderMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;

    protected MobileMessageTemplate findMobileMessageTemplate(int agentId, int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_MOBILE_MESSAGE_TEMPLATE, agentId, id);
        MobileMessageTemplate template = (MobileMessageTemplate) memCachedClient.get(key);
        if (template != null) {
            return template;
        }
        template = mobileMessageTemplateMapper.find(agentId, id);
        if (template != null) {
            memCachedClient.set(key, template, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return template;
    }

    /**
     * 充值
     *
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void depositOrderPayOk(PayOrder payOrder) {
        CustomerDepositOrder customerDepositOrder = customerDepositOrderMapper.find(payOrder.getSourceId());
        if (customerDepositOrder != null) {
            int effect = customerDepositOrderMapper.payOk(payOrder.getSourceId(), new Date(), CustomerDepositOrder.Status.NOT.getValue(), CustomerDepositOrder.Status.OK.getValue());
            if (effect > 0) {
                //充值不再赠送
                customerMapper.updateBalance(customerDepositOrder.getCustomerId(), customerDepositOrder.getMoney(), 0);

                //客户流水
                Customer customer = customerMapper.find(customerDepositOrder.getCustomerId());
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customerDepositOrder.getCustomerId());
                inOutMoney.setMoney(customerDepositOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(customerDepositOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(customer.getBalance() + customer.getGiftBalance());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);

                //商户流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(customerDepositOrder.getPayType()));
                partnerInOutMoney.setPartnerId(customerDepositOrder.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                partnerInOutMoney.setBizId(customerDepositOrder.getId());
                partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(customerDepositOrder.getMoney());
                partnerInOutMoney.setOperator(customer.getFullname());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);

                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(customerDepositOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);
            }
        }
    }

    /**
     * 运营商充值
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void agentDepositOrderPayOk(PayOrder payOrder) {
        AgentDepositOrder agentDepositOrder = agentDepositOrderMapper.find(payOrder.getSourceId());
        if (agentDepositOrder != null) {
            int effect = agentDepositOrderMapper.payOk(payOrder.getSourceId(), new Date(), AgentDepositOrder.Status.NOT_PAID.getValue(), AgentDepositOrder.Status.HAVE_PAID.getValue());
            if (effect > 0) {
                //充值不再赠送
                agentMapper.updateBalance(agentDepositOrder.getAgentId(), agentDepositOrder.getMoney());

                //运营商流水
                Agent agent = agentMapper.find(agentDepositOrder.getAgentId());
                AgentInOutMoney inOutMoney = new AgentInOutMoney();
                inOutMoney.setAgentId(agentDepositOrder.getAgentId());
                inOutMoney.setMoney(agentDepositOrder.getMoney());
                inOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(agentDepositOrder.getId());
                inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(agent.getBalance());
                inOutMoney.setCreateTime(new Date());
                agentInOutMoneyMapper.insert(inOutMoney);

                //商户流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(agentDepositOrder.getPayType()));
                partnerInOutMoney.setPartnerId(agentDepositOrder.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                partnerInOutMoney.setBizId(agentDepositOrder.getId());
                partnerInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(agentDepositOrder.getMoney());
                partnerInOutMoney.setOperator(agentDepositOrder.getOperator());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);

            }
        }
    }

    /**
     * 租车组合订单支付
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void vehicleGroupOrderPayOk(PayOrder payOrder) {
        GroupOrder groupOrder = groupOrderMapper.find(payOrder.getSourceId());

        Agent agent = agentMapper.find(groupOrder.getAgentId());
        Customer customer = customerMapper.find(groupOrder.getCustomerId());

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

                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(groupOrder.getCustomerId());
                    inOutMoney.setMoney(groupOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(groupOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + groupOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(groupOrder.getCustomerId());
                    inOutMoney.setMoney(-groupOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_VEHICLE_GROUP_PAY.getValue());
                    inOutMoney.setBizId(groupOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(groupOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(groupOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_GROUP_PAY.getValue());
                    partnerInOutMoney.setBizId(groupOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(groupOrder.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname()==null?customer.getMobile():customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

            } else {
                log.error("更新订单GroupOrder状态失败, id: {}", groupOrder.getId());
            }
        } else {
            log.error("GroupOrder订单不存在, {}", payOrder.getSourceId());
        }


        VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.find(groupOrder.getVehicleForegiftId());

        if (vehicleForegiftOrder != null) {

            if (vehicleForegiftOrderMapper.payOk(vehicleForegiftOrder.getId(), new Date(), VehicleForegiftOrder.Status.WAIT_PAY.getValue(), VehicleForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //运营商首单赠送押金券
                int count = vehicleForegiftOrderMapper.findCountByCustomerId(vehicleForegiftOrder.getId(), vehicleForegiftOrder.getAgentId(), vehicleForegiftOrder.getCustomerId(), VehicleForegiftOrder.Status.WAIT_PAY.getValue());
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
                customerVehicleInfo.setBalanceShopId(groupOrder.getShopId());
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
            log.error("VehicleForegiftOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());

        }

        VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());

        if (vehiclePeriodOrder != null) {

            if (vehiclePeriodOrderMapper.payOk(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_PAY.getValue(), VehiclePeriodOrder.Status.NOT_USE.getValue(),new Date(), null, null) == 1) {

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
                if(vehiclePeriodOrder != null){
                    PushMetaData metaData = new PushMetaData();
                    metaData.setSourceId(vehiclePeriodOrder.getId());
                    metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
                    metaData.setCreateTime(new Date());
                    pushMetaDataMapper.insert(metaData);
                }

            } else {
                log.error("更新订单VehiclePeriodOrder状态失败, id: {}", vehiclePeriodOrder.getId());
            }

        } else {
            log.error("VehiclePeriodOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
        }

        if (groupOrder.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {

            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
            if (customerForegiftOrder != null) {

                if (customerForegiftOrderMapper.payOk(customerForegiftOrder.getId(), new Date(), new Date(), CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                    //更新用户换电押金状态
                    customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

                    //回写用户运营商id
                    customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());

                    CustomerExchangeInfo customerExchangeInfo = new CustomerExchangeInfo();
                    customerExchangeInfo.setId(customer.getId());
                    customerExchangeInfo.setAgentId(agent.getId());
                    customerExchangeInfo.setBatteryType(null);
                    customerExchangeInfo.setForegift(customerForegiftOrder.getMoney());
                    customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
                    customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
                    customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
                    customerExchangeInfo.setBalanceStationId(customerForegiftOrder.getStationId());
                    customerExchangeInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
                    customerExchangeInfo.setBatteryType(customerForegiftOrder.getBatteryType());
                    customerExchangeInfo.setCreateTime(new Date());

                    customerExchangeInfoMapper.insert(customerExchangeInfo);

                } else {
                    log.error("更新订单CustomerForegiftOrder状态失败, id: {}", customerForegiftOrder.getId());
                }
            } else {
                log.error("CustomerForegiftOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
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
                log.error("PacketPeriodOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
            }

        } else if (groupOrder.getCategory() == ConstEnum.Category.RENT.getValue()) {

            RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
            if (rentForegiftOrder != null) {

                if (rentForegiftOrderMapper.payOk(rentForegiftOrder.getId(), new Date(), new Date(), RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                    //更新客户租电押金状态
                    customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());

                    //回写用户运营商id
                    customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

                    CustomerRentInfo customerRentInfo = new CustomerRentInfo();
                    customerRentInfo.setId(customer.getId());
                    customerRentInfo.setAgentId(agent.getId());
                    customerRentInfo.setBatteryType(null);
                    customerRentInfo.setForegift(rentForegiftOrder.getMoney());
                    customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
                    customerRentInfo.setBalanceShopId(rentForegiftOrder.getShopId());
                    customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
                    customerRentInfo.setBatteryType(rentForegiftOrder.getBatteryType());
                    customerRentInfo.setCreateTime(new Date());

                    customerRentInfoMapper.insert(customerRentInfo);

                } else {
                    log.error("更新订单RentForegiftOrder状态失败, id: {}", rentForegiftOrder.getId());
                }
            } else {
                log.error("RentForegiftOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
            }

            RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
            if (rentPeriodOrder != null) {

                if (rentPeriodOrderMapper.payOk(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_PAY.getValue(), RentPeriodOrder.Status.NOT_USE.getValue(),new Date(), null, null) == 1) {

                    if (log.isDebugEnabled()) {
                        log.debug("RentPeriodOrder id: {}支付成功", rentPeriodOrder.getId());
                    }

                } else {
                    log.error("更新订单RentPeriodOrder状态失败, id: {}", rentPeriodOrder.getId());
                }

            } else {
                log.error("RentPeriodOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
            }

            //清空缓存
            long customerId = customer.getId();
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
            memCachedClient.delete(key);

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
            log.error("VehicleForegiftOrder 订单不存在, {}", "组合订单id"+groupOrderId);
        }

        VehiclePeriodOrder vehiclePeriodOrder = null;

        if (groupOrder != null) {
            vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());
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

                    if (customerForegiftOrderMapper.payOk(customerForegiftOrder.getId(), new Date(), new Date(), CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                        //更新用户换电押金状态
                        customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

                        //回写用户运营商id
                        customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());

                        CustomerExchangeInfo customerExchangeInfo = new CustomerExchangeInfo();
                        customerExchangeInfo.setId(customer.getId());
                        customerExchangeInfo.setAgentId(agent.getId());
                        customerExchangeInfo.setBatteryType(null);
                        customerExchangeInfo.setForegift(customerForegiftOrder.getMoney());
                        customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
                        customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
                        customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
                        customerExchangeInfo.setBalanceStationId(customerForegiftOrder.getStationId());
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

                    if (rentForegiftOrderMapper.payOk(rentForegiftOrder.getId(), new Date(), new Date(), RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                        //更新客户租电押金状态
                        customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());

                        //回写用户运营商id
                        customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

                        CustomerRentInfo customerRentInfo = new CustomerRentInfo();
                        customerRentInfo.setId(customer.getId());
                        customerRentInfo.setAgentId(agent.getId());
                        customerRentInfo.setBatteryType(null);
                        customerRentInfo.setForegift(rentForegiftOrder.getMoney());
                        customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
                        customerRentInfo.setBalanceShopId(rentForegiftOrder.getShopId());
                        customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
                        customerRentInfo.setBatteryType(rentForegiftOrder.getBatteryType());
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

    /**
     * 租车组合续租订单支付
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void vehicleGroupOrderRentPayOk(PayOrder payOrder) {
        GroupOrder groupOrder = groupOrderMapper.find(payOrder.getSourceId());

        Agent agent = agentMapper.find(groupOrder.getAgentId());
        Customer customer = customerMapper.find(groupOrder.getCustomerId());

        //组合订单
        if (groupOrder != null) {
            if (groupOrderMapper.payOk(groupOrder.getId(), new Date(), GroupOrder.Status.WAIT_PAY.getValue(), GroupOrder.Status.PAY_OK.getValue()) == 1) {
                if (groupOrder.getMoney() > 0) {

                    if (groupOrder.getRentCouponTicketId() != null) {
                        //更新租金券状态
                        customerCouponTicketMapper.setUsed(groupOrder.getRentCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                    }

                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(groupOrder.getCustomerId());
                    inOutMoney.setMoney(groupOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(groupOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + groupOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(groupOrder.getCustomerId());
                    inOutMoney.setMoney(-groupOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_VEHICLE_GROUP_PAY.getValue());
                    inOutMoney.setBizId(groupOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(groupOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(groupOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_GROUP_PAY.getValue());
                    partnerInOutMoney.setBizId(groupOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(groupOrder.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname()==null?customer.getMobile():customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

            } else {
                log.error("更新订单GroupOrder状态失败, id: {}", groupOrder.getId());
            }
        } else {
            log.error("GroupOrder订单不存在, {}", payOrder.getSourceId());
        }


        VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());

        if (vehiclePeriodOrder != null) {

            Date beginTime = null;
            Date endTime = null;
            int status = RentPeriodOrder.Status.NOT_USE.getValue();
            //没有使用中电池，为使用变为已经使用
            VehiclePeriodOrder usedOrder = vehiclePeriodOrderMapper.findOneEnabled(vehiclePeriodOrder.getCustomerId(), VehiclePeriodOrder.Status.USED.getValue(), vehiclePeriodOrder.getAgentId(), vehiclePeriodOrder.getModelId());
            if (usedOrder == null) {
                beginTime = new Date();
                endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, vehiclePeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                status = RentPeriodOrder.Status.USED.getValue();
            }

            if (vehiclePeriodOrderMapper.payOk(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_PAY.getValue(), status ,new Date(), beginTime, endTime) == 1) {

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
                if(vehiclePeriodOrder != null){
                    PushMetaData metaData = new PushMetaData();
                    metaData.setSourceId(vehiclePeriodOrder.getId());
                    metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
                    metaData.setCreateTime(new Date());
                    pushMetaDataMapper.insert(metaData);
                }

            } else {
                log.error("更新订单VehiclePeriodOrder状态失败, id: {}", vehiclePeriodOrder.getId());
            }

        } else {
            log.error("VehiclePeriodOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
        }

        if (groupOrder.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {

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
                log.error("PacketPeriodOrder 订单不存在, {}", "组合订单id" + payOrder.getSourceId());
            }

        } else if (groupOrder.getCategory() == ConstEnum.Category.RENT.getValue()) {

            RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
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
                if (rentPeriodOrderMapper.payOk(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_PAY.getValue(), status ,new Date(), beginTime, endTime) == 1) {

                if (log.isDebugEnabled()) {
                    log.debug("RentPeriodOrder id: {}支付成功", rentPeriodOrder.getId());
                }

                } else {
                    log.error("更新订单RentPeriodOrder状态失败, id: {}", rentPeriodOrder.getId());
                }

            } else {
                log.error("RentPeriodOrder 订单不存在, {}","组合订单id" + payOrder.getSourceId());
            }

            //清空缓存
            long customerId = customer.getId();
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
            memCachedClient.delete(key);

        }

        //客户购买租车租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租车续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * groupOrder.getRentPeriodMoney() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

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
                if (customerForegiftOrder.getMoney() > 0) {
                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(customerForegiftOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(customerForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + customerForegiftOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(-customerForegiftOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT_PAY.getValue());
                    inOutMoney.setBizId(customerForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(customerForegiftOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(customerForegiftOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_PAY.getValue());
                    partnerInOutMoney.setBizId(customerForegiftOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(customerForegiftOrder.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname()==null?customer.getMobile():customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

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

                    if (packetPeriodOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(packetPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(packetPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + packetPeriodOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(-packetPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                        inOutMoney.setBizId(packetPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //商户流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(packetPeriodOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(packetPeriodOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                        partnerInOutMoney.setBizId(packetPeriodOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(packetPeriodOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
                    }


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

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftMoney}，租金: ${packetPeriodMoney}。",
                new String[]{"${foregiftMoney}", "${packetPeriodMoney}"},
                new String[]{String.format("%.2f元", 1d * foregiftMoney / 100.0), String.format("%.2f元", 1d * packetPeriodMoney / 100.0)}));
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

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, 0,agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), rentForegiftOrder.getCustomerMobile());
                }

                Customer customer = customerMapper.find(rentForegiftOrder.getCustomerId());
                if (rentForegiftOrder.getMoney() > 0) {
                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(rentForegiftOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(rentForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + rentForegiftOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(-rentForegiftOrder.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_FOREGIFT_PAY.getValue());
                    inOutMoney.setBizId(rentForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentForegiftOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(rentForegiftOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_RENTFOREGIFT_PAY.getValue());
                    partnerInOutMoney.setBizId(rentForegiftOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(rentForegiftOrder.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

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
                log.error("更新订单RentForegiftOrder状态失败, id: {}", foregiftOrderfId);
            }
        } else {
            log.error("RentForegiftOrder 订单不存在, {}", foregiftOrderfId);
        }

        if (rentPeriodOrderId != null) {
            RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(rentPeriodOrderId);
            if (rentPeriodOrder != null) {
                rentPeriodPrice = rentPeriodOrder.getPrice();
                rentPeriodMoney = rentPeriodOrder.getMoney();

                Date beginTime = new Date();
                Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                if (rentPeriodOrderMapper.payOk(rentPeriodOrderId, RentPeriodOrder.Status.NOT_PAY.getValue(), RentPeriodOrder.Status.USED.getValue(),new Date(), beginTime, endTime) == 1) {

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

                    if (rentPeriodOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(rentPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(rentPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + rentPeriodOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(-rentPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                        inOutMoney.setBizId(rentPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //商户流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentPeriodOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(rentPeriodOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                        partnerInOutMoney.setBizId(rentPeriodOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(rentPeriodOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
                    }


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
                        log.debug("RentPeriodOrder id: {}支付成功", rentPeriodOrderId);
                    }

                } else {
                    log.error("更新订单RentPeriodOrder状态失败, id: {}", rentPeriodOrderId);
                }

            } else {
                log.error("RentPeriodOrder 订单不存在, {}", rentPeriodOrderId);
            }
        }

        if (rentInsuranceOrderId != null) {
            RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderMapper.find(rentInsuranceOrderId);
            if (rentInsuranceOrder != null) {
                if (rentInsuranceOrderMapper.payOk(rentInsuranceOrderId, RentInsuranceOrder.Status.NOT_PAY.getValue(), RentInsuranceOrder.Status.PAID.getValue(),new Date()) == 1) {

                    if (rentInsuranceOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(rentInsuranceOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentInsuranceOrder.getCustomerId());
                        inOutMoney.setMoney(rentInsuranceOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(rentInsuranceOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + rentInsuranceOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentInsuranceOrder.getCustomerId());
                        inOutMoney.setMoney(-rentInsuranceOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_INSURANCE.getValue());
                        inOutMoney.setBizId(rentInsuranceOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //平台流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentInsuranceOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(rentInsuranceOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_RENT_INSURANCE_PAY.getValue());
                        partnerInOutMoney.setBizId(rentInsuranceOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(rentInsuranceOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("RentInsuranceOrder id: {}支付成功", rentInsuranceOrderId);
                    }

                } else {
                    log.error("更新订单RentInsuranceOrder状态失败, id: {}", rentInsuranceOrderId);
                }

            } else {
                log.error("RentInsuranceOrder 订单不存在, {}", rentInsuranceOrderId);
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
        customerPayTrack.setMemo(StringUtils.replaceEach("租电池，押金: ${foregiftMoney}，租金: ${rentPeriodMoney}。",
                new String[]{"${foregiftMoney}", "${rentPeriodMoney}"},
                new String[]{String.format("%.2f元", 1d * foregiftMoney / 100.0), String.format("%.2f元", 1d * rentPeriodMoney / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        //清空缓存
        long customerId = customer.getId();
        String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
        memCachedClient.delete(key);
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

                //客户流水（收入）
                Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                inOutMoney.setMoney(packetPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(packetPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(customer.getBalance() + packetPeriodOrder.getMoney());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
                //客户流水（支出）
                inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                inOutMoney.setMoney(-packetPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                inOutMoney.setBizId(packetPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(customer.getBalance());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);

                //平台流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(packetPeriodOrder.getPayType()));
                partnerInOutMoney.setPartnerId(packetPeriodOrder.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                partnerInOutMoney.setBizId(packetPeriodOrder.getId());
                partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(packetPeriodOrder.getMoney());
                partnerInOutMoney.setOperator(customer.getFullname());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);
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
        customerPayTrack.setMemo(StringUtils.replaceEach("换电续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * packetPeriodOrder.getPrice() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

    /**
     * 换电押金首付支付
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void exchangeForegiftOrderFirstMoneyPayOk(PayOrder payOrder) {

        Long recordId = Long.valueOf(payOrder.getSourceId());

        CustomerInstallmentRecordOrderDetail packetPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(recordId,
                OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());

        CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(recordId,
                OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());

        String foregiftOrderfId = customerForegiftOrderDetail.getSourceId(),
               packetPeriodOrderId = packetPeriodOrderDetail.getSourceId();

        int foregiftMoney = 0, packetPeriodMoney = 0;
        int foregiftPrice = 0, packetPeriodPrice = 0;

        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(foregiftOrderfId);
        if (customerForegiftOrder != null) {
            foregiftPrice = customerForegiftOrder.getPrice();
            foregiftMoney = customerForegiftOrder.getMoney();

            if (customerForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), null, CustomerForegiftOrder.Status.WAIT_PAY.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //更新用户换电押金状态
                customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

                //更新分期记录状态
                customerInstallmentRecordMapper.updateOrderStatus(recordId, CustomerInstallmentRecord.Status.PAY_ING.getValue(), CustomerInstallmentRecord.Status.WAIT_PAY.getValue());

                List<CustomerInstallmentRecordPayDetail> details = customerInstallmentRecordPayDetailMapper.findList(recordId, customerForegiftOrder.getCustomerId(), ConstEnum.Category.EXCHANGE.getValue());

                //更新欠款记录付款状态
                customerInstallmentRecordPayDetailMapper.updateApplyDetail(details.get(0).getId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue(),
                        CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), new Date(), details.get(0).getMoney());

                //更新分期记录已支付金额
                customerInstallmentRecordMapper.updatePaidMoney(recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //更新押金订单已支付金额
                customerForegiftOrderMapper.updatePaidMoney(foregiftOrderfId, recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //首付关联customerRentInfo押金金额
                CustomerForegiftOrder customerForegiftOrder1 = customerForegiftOrderMapper.find(foregiftOrderfId);

                //运营商首单赠送押金券
                int count = customerForegiftOrderMapper.findCountByCustomerId(customerForegiftOrder.getId(), customerForegiftOrder.getAgentId(), customerForegiftOrder.getCustomerId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送
                    int agentId = customerForegiftOrder.getAgentId() == null ? 0 : customerForegiftOrder.getAgentId();

                    String sourceId = customerForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, 0,agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customerForegiftOrder.getCustomerMobile());
                }

                Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());
                if (customerForegiftOrder.getMoney() > 0) {
                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(customerForegiftOrder1.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(customerForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + customerForegiftOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(-customerForegiftOrder1.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT_PAY.getValue());
                    inOutMoney.setBizId(customerForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(customerForegiftOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(customerForegiftOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_PAY.getValue());
                    partnerInOutMoney.setBizId(customerForegiftOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(customerForegiftOrder1.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

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
                customerExchangeInfo.setForegift(customerForegiftOrder1.getMoney());
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

                if (packetPeriodOrderMapper.payOk(packetPeriodOrderId, PacketPeriodOrder.Status.NOT_PAY.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue(), null) == 1) {

                    //更新租金订单已支付金额
                    packetPeriodOrderMapper.updatePaidMoney(packetPeriodOrderId, recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());


                    //租金赠送
                    int agentId = packetPeriodOrder.getAgentId() == null ? 0 : packetPeriodOrder.getAgentId();

                    String sourceId = packetPeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, packetPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());

                    if (packetPeriodOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(packetPeriodOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(packetPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(packetPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + packetPeriodOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(-packetPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                        inOutMoney.setBizId(packetPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //商户流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(packetPeriodOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(packetPeriodOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
                        partnerInOutMoney.setBizId(packetPeriodOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(packetPeriodOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
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

     /*   Agent agent = agentMapper.find(customerForegiftOrder.getAgentId());
        Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());
        if (agent != null && customer != null) {
            handleLaxinCustomer(agent, customer, foregiftMoney, packetPeriodMoney);
        }*/

    }


    /**
     * 租电押金首付支付
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rentForegiftOrderFirstMoneyPayOk(PayOrder payOrder) {

        Long recordId = Long.valueOf(payOrder.getSourceId());

        CustomerInstallmentRecordOrderDetail rentPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(recordId,
                OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());

        CustomerInstallmentRecordOrderDetail rentInsuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(recordId,
                OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue());

        CustomerInstallmentRecordOrderDetail rentCustomerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(recordId,
                OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());


        String foregiftOrderfId = rentCustomerForegiftOrderDetail.getSourceId(),
                rentPeriodOrderId = rentPeriodOrderDetail.getSourceId();

        String rentInsuranceOrderId = null;
        if(rentInsuranceOrderDetail != null){
            rentInsuranceOrderId = rentInsuranceOrderDetail.getSourceId();
        }

        int foregiftMoney = 0, rentPeriodMoney = 0;
        int foregiftPrice = 0, rentPeriodPrice = 0;

        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(foregiftOrderfId);
        if (rentForegiftOrder != null) {
            foregiftPrice = rentForegiftOrder.getPrice();
            foregiftMoney = rentForegiftOrder.getMoney();

            if (rentForegiftOrderMapper.payOk(foregiftOrderfId, new Date(), null, RentForegiftOrder.Status.WAIT_PAY.getValue(), RentForegiftOrder.Status.PAY_OK.getValue()) == 1) {

                //更新客户租电押金状态
                customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());

                //更新分期记录状态
                customerInstallmentRecordMapper.updateOrderStatus(recordId, CustomerInstallmentRecord.Status.PAY_ING.getValue(), CustomerInstallmentRecord.Status.WAIT_PAY.getValue());

                List<CustomerInstallmentRecordPayDetail> details = customerInstallmentRecordPayDetailMapper.findList(recordId, rentForegiftOrder.getCustomerId(), ConstEnum.Category.RENT.getValue());

                //更新欠款记录付款状态
                customerInstallmentRecordPayDetailMapper.updateApplyDetail(details.get(0).getId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue(),
                        CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), new Date(), details.get(0).getMoney());

                //更新分期记录已支付金额
                customerInstallmentRecordMapper.updatePaidMoney(recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //更新押金订单已支付金额
                rentForegiftOrderMapper.updatePaidMoney(foregiftOrderfId, recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //首付关联customerRentInfo押金金额
                RentForegiftOrder rentForegiftOrder1 = rentForegiftOrderMapper.find(foregiftOrderfId);

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
                if (rentForegiftOrder.getMoney() > 0) {
                    //客户流水（收入）
                    CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(rentForegiftOrder1.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    inOutMoney.setBizId(rentForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    inOutMoney.setBalance(customer.getBalance() + rentForegiftOrder.getMoney());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);
                    //客户流水（支出）
                    inOutMoney = new CustomerInOutMoney();
                    inOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
                    inOutMoney.setMoney(-rentForegiftOrder1.getMoney());
                    inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_FOREGIFT_PAY.getValue());
                    inOutMoney.setBizId(rentForegiftOrder.getId());
                    inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    inOutMoney.setBalance(customer.getBalance());
                    inOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(inOutMoney);

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentForegiftOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(rentForegiftOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_RENTFOREGIFT_PAY.getValue());
                    partnerInOutMoney.setBizId(rentForegiftOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(rentForegiftOrder1.getMoney());
                    partnerInOutMoney.setOperator(customer.getFullname());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

                //回写用户运营商id
                customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

                if (log.isDebugEnabled()) {
                    log.debug("CustomerForegiftOrder id: {}支付成功", foregiftOrderfId);
                }

                CustomerRentInfo customerRentInfo = new CustomerRentInfo();
                customerRentInfo.setId(rentForegiftOrder.getCustomerId());
                customerRentInfo.setAgentId(rentForegiftOrder.getAgentId());
                customerRentInfo.setBatteryType(rentForegiftOrder.getBatteryType());
                customerRentInfo.setForegift(rentForegiftOrder1.getMoney());
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
                if (shop != null) {
                    rentOrder.setShopId(shop.getId());
                    rentOrder.setShopName(shop.getShopName());
                } else {
                    rentOrder.setShopId(null);
                    rentOrder.setShopName(null);
                }
                rentOrder.setCustomerId(customer.getId());
                rentOrder.setCustomerMobile(customer.getMobile());
                rentOrder.setCustomerFullname(customer.getFullname());
                rentOrder.setBatteryType(rentForegiftOrder.getBatteryType());
                rentOrder.setBatteryId(rentForegiftOrder.getBatteryId());
                rentOrder.setStatus(RentOrder.Status.RENT.getValue());
                if (battery != null) {
                    rentOrder.setCurrentVolume(battery.getVolume());
                } else {
                    rentOrder.setCurrentVolume(0);
                }
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
                if (rentPeriodOrderMapper.payOk(rentPeriodOrderId, RentPeriodOrder.Status.NOT_PAY.getValue(), RentPeriodOrder.Status.USED.getValue(),null, beginTime, endTime) == 1) {

                    //更新租金订单已支付金额
                    rentPeriodOrderMapper.updatePaidMoney(rentPeriodOrderId, recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                    //清空缓存
                    String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
                    memCachedClient.delete(key);

                    //租金赠送
                    int agentId = rentPeriodOrder.getAgentId() == null ? 0 : rentPeriodOrder.getAgentId();

                    String sourceId = rentPeriodOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
                    Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

                    if (rentPeriodOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(rentPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(rentPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + rentPeriodOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                        inOutMoney.setMoney(-rentPeriodOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                        inOutMoney.setBizId(rentPeriodOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //商户流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentPeriodOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(rentPeriodOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                        partnerInOutMoney.setBizId(rentPeriodOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(rentPeriodOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
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
                if (rentInsuranceOrderMapper.payOk(rentInsuranceOrderId, RentInsuranceOrder.Status.NOT_PAY.getValue(), RentInsuranceOrder.Status.PAID.getValue(),null) == 1) {

                    //更新保险订单已支付金额
                    rentInsuranceOrderMapper.updatePaidMoney(rentInsuranceOrderId, recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                    if (rentInsuranceOrder.getMoney() > 0) {
                        //客户流水（收入）
                        Customer customer = customerMapper.find(rentInsuranceOrder.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentInsuranceOrder.getCustomerId());
                        inOutMoney.setMoney(rentInsuranceOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                        inOutMoney.setBizId(rentInsuranceOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setBalance(customer.getBalance() + rentInsuranceOrder.getMoney());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                        //客户流水（支出）
                        inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(rentInsuranceOrder.getCustomerId());
                        inOutMoney.setMoney(-rentInsuranceOrder.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_INSURANCE.getValue());
                        inOutMoney.setBizId(rentInsuranceOrder.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);

                        //平台流水
                        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                        partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentInsuranceOrder.getPayType()));
                        partnerInOutMoney.setPartnerId(rentInsuranceOrder.getPartnerId());
                        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_RENT_INSURANCE_PAY.getValue());
                        partnerInOutMoney.setBizId(rentInsuranceOrder.getId());
                        partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        partnerInOutMoney.setMoney(rentInsuranceOrder.getMoney());
                        partnerInOutMoney.setOperator(customer.getFullname());
                        partnerInOutMoney.setCreateTime(new Date());
                        partnerInOutMoneyMapper.insert(partnerInOutMoney);
                    }

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

    }

    /**
     * 换电租电押金分期支付
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void foregiftInstallmentMoneyPayOk(PayOrder payOrder) {

        String[] sourceIdList = StringUtils.split(payOrder.getSourceId(), ",");
        long recordId = 0;
        for (String id : sourceIdList) {
            long payDetailId = Long.valueOf(id);
            CustomerInstallmentRecordPayDetail payDetail = customerInstallmentRecordPayDetailMapper.find(payDetailId);
            if (payDetail != null) {
                recordId = payDetail.getRecordId();
                Customer customer = customerMapper.find(payDetail.getCustomerId());

                if (customerInstallmentRecordPayDetailMapper.updateApplyDetail(payDetailId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue(),
                        CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), new Date(), payDetail.getMoney()) == 1) {

                    //换电押金分期
                    if (payDetail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
                        CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
                        CustomerInstallmentRecordOrderDetail packetPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
                        CustomerInstallmentRecordOrderDetail insuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.INSURANCE_ORDER.getValue());
                        //更新保险订单已支付金额
                        if(insuranceOrderDetail != null){
                            insuranceOrderMapper.updatePaidMoney(insuranceOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        }
                        //更新租金订单已支付金额
                        packetPeriodOrderMapper.updatePaidMoney(packetPeriodOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        //更新押金订单已支付金额
                        customerForegiftOrderMapper.updatePaidMoney(customerForegiftOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        //更新关联customerRentInfo押金金额
                        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerForegiftOrderDetail.getSourceId());
                        customerExchangeInfoMapper.updateForegift(customerForegiftOrder.getId(), customerForegiftOrder.getMoney());
                    } else {
                        CustomerInstallmentRecordOrderDetail rentForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
                        CustomerInstallmentRecordOrderDetail rentPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
                        CustomerInstallmentRecordOrderDetail rentInsuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                                OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue());
                        //更新保险订单已支付金额
                        if(rentInsuranceOrderDetail != null){
                            rentInsuranceOrderMapper.updatePaidMoney(rentInsuranceOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        }
                        //更新租金订单已支付金额
                        rentPeriodOrderMapper.updatePaidMoney(rentPeriodOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        //更新押金订单已支付金额
                        rentForegiftOrderMapper.updatePaidMoney(rentForegiftOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                        //更新关联customerRentInfo押金金额
                        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(rentForegiftOrderDetail.getSourceId());
                        customerRentInfoMapper.updateForegift(rentForegiftOrder.getId(), rentForegiftOrder.getMoney());
                    }

                    if (payDetail.getMoney() > 0) {
                        //客户流水（支出）
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(customer.getId());
                        inOutMoney.setMoney(-payDetail.getMoney());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_INSTALLMENT_PAY_ORDER.getValue());
                        inOutMoney.setBizId(payDetail.getId().toString());
                        inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                        inOutMoney.setBalance(customer.getBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("CustomerInstallmentRecordPayDetail id: {}分期明细支付成功", payDetailId);
                    }
                } else {
                    log.error("更新分期明细记录CustomerInstallmentRecordPayDetail状态失败, id: {}", payDetailId);
                }

            } else {
                log.error("CustomerInstallmentRecordPayDetail 分期付款明细记录不存在, {}", payDetailId);
            }
        }

        //更新分期记录已支付金额
        customerInstallmentRecordMapper.updatePaidMoney(recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

        CustomerInstallmentRecord record = customerInstallmentRecordMapper.find(recordId);
        //分期完成
        if (record.getTotalMoney() <= record.getPaidMoney()) {

            //更新分期记录状态
            customerInstallmentRecordMapper.updateOrderStatus(record.getId(), CustomerInstallmentRecord.Status.PAY_OK.getValue(), CustomerInstallmentRecord.Status.PAY_ING.getValue());

            List<CustomerInstallmentRecordOrderDetail> orderDetailList = customerInstallmentRecordOrderDetailMapper.findList(recordId);

            for (CustomerInstallmentRecordOrderDetail detail : orderDetailList) {
                if (detail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {

                    if (detail.getSourceType() == OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue()) {
                        packetPeriodOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    } else if (detail.getSourceType() == OrderId.OrderIdType.INSURANCE_ORDER.getValue()) {
                        insuranceOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    } else if (detail.getSourceType() == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue()) {
                        customerForegiftOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    }
                } else {
                    if (detail.getSourceType() == OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue()) {
                        rentPeriodOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    } else if (detail.getSourceType() == OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue()) {
                        rentInsuranceOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    } else if (detail.getSourceType() == OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue()) {
                        rentForegiftOrderMapper.updateCompleteInstallmentTime(detail.getSourceId(), new Date(), new Date());
                    }
                }
            }
        }
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
                //租金赠送
                int agentId = rentPeriodOrder.getAgentId() == null ? 0 : rentPeriodOrder.getAgentId();

                String sourceId = rentPeriodOrder.getId();
                Integer sourceType = OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue();
                Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

                //客户流水（收入）
                Customer customer = customerMapper.find(rentPeriodOrder.getCustomerId());
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                inOutMoney.setMoney(rentPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(rentPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(customer.getBalance() + rentPeriodOrder.getMoney());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
                //客户流水（支出）
                inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(rentPeriodOrder.getCustomerId());
                inOutMoney.setMoney(-rentPeriodOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                inOutMoney.setBizId(rentPeriodOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(customer.getBalance());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);

                //平台流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(rentPeriodOrder.getPayType()));
                partnerInOutMoney.setPartnerId(rentPeriodOrder.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_RENT_PERIOD_ORDER_PAY.getValue());
                partnerInOutMoney.setBizId(rentPeriodOrder.getId());
                partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(rentPeriodOrder.getMoney());
                partnerInOutMoney.setOperator(customer.getFullname());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);
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
        customerPayTrack.setMemo(StringUtils.replaceEach("租电续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * rentPeriodOrder.getPrice() / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        //清空缓存
        long customerId = rentPeriodOrder.getCustomerId();
        String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, customerId);
        memCachedClient.delete(key);
    }


    @Transactional(rollbackFor = Throwable.class)
    public boolean batteryOrderPayOk(PayOrder payOrder) {
        boolean ok = false;
        String batteryOrderId = payOrder.getSourceId();
        BatteryOrder batteryOrder = batteryOrderMapper.find(batteryOrderId);
        if (batteryOrder != null) {
            if (batteryOrderMapper.payOk(payOrder.getSourceId(), new Date(), BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(), BatteryOrder.OrderStatus.PAY.getValue()) == 1) {

                if (batteryOrder.getPayTimeoutFaultLogId() != null) {
                    faultLogMapper.handle(batteryOrder.getPayTimeoutFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, new Date(), null, FaultLog.Status.PROCESSED.getValue());
                }

                //客户流水（收入）
                Customer customer = customerMapper.find(batteryOrder.getCustomerId());
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(batteryOrder.getCustomerId());
                inOutMoney.setMoney(batteryOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                inOutMoney.setBizId(batteryOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                inOutMoney.setBalance(customer.getBalance() + batteryOrder.getMoney());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
                //客户流水（支出）
                inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(batteryOrder.getCustomerId());
                inOutMoney.setMoney(-batteryOrder.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_EXCHANGE_PAY.getValue());
                inOutMoney.setBizId(batteryOrder.getId());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(customer.getBalance());
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);

                //平台流水
                PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(batteryOrder.getPayType()));
                partnerInOutMoney.setPartnerId(batteryOrder.getPartnerId());
                partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_EXCHANGE_PAY.getValue());
                partnerInOutMoney.setBizId(batteryOrder.getId());
                partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                partnerInOutMoney.setMoney(batteryOrder.getMoney());
                partnerInOutMoney.setOperator(customer.getFullname());
                partnerInOutMoney.setCreateTime(new Date());
                partnerInOutMoneyMapper.insert(partnerInOutMoney);


                if (batteryOrder.getCouponTicketId() != null) {
                    customerCouponTicketMapper.setUsed(batteryOrder.getCouponTicketId(), new Date(), CustomerCouponTicket.Status.USED.getValue());
                }
                customerExchangeBatteryMapper.clearBattery(batteryOrder.getCustomerId(), batteryOrder.getBatteryId());
                batteryMapper.clearCustomer(batteryOrder.getBatteryId(), Battery.Status.IN_BOX.getValue());
                //更改柜子格口状态
                cabinetBoxMapper.unlockBox(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), CabinetBox.BoxStatus.FULL.getValue());

                batteryOperateLogMapper.insert(new BatteryOperateLog(batteryOrder.getBatteryId(), BatteryOperateLog.OperateType.CUSTOMER_PAY_OLD.getValue(), batteryOrder.getCustomerId(), batteryOrder.getCustomerMobile(), batteryOrder.getCustomerFullname(), batteryOrder.getPutCabinetId(), batteryOrder.getPutCabinetName(), batteryOrder.getPutBoxNum(), batteryOrder.getCurrentVolume(), new Date()));

                if (log.isDebugEnabled()) {
                    log.debug("BatteryOrder id: {}支付成功", batteryOrderId);
                }
                ok = true;
            } else {
                log.error("更新订单BatteryOrder状态失败, id: {}", batteryOrderId);
            }

        } else {
            log.error("BatteryOrder 订单不存在, {}", batteryOrderId);
        }
        return ok;
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean laxinOrderPayOk(PayOrder payOrder) {
        int payType = 0;
        if (payOrder instanceof WeixinPayOrder) {
            payType = ConstEnum.PayType.WEIXIN.getValue();
        } else if (payOrder instanceof WeixinmpPayOrder) {
            payType = ConstEnum.PayType.WEIXIN_MP.getValue();
        } else if (payOrder instanceof AlipayPayOrder) {
            payType = ConstEnum.PayType.ALIPAY.getValue();
        } else if (payOrder instanceof AlipayfwPayOrder) {
            payType = ConstEnum.PayType.ALIPAY_FW.getValue();
        }

        boolean ok = false;
        String laxinPayOrderId = payOrder.getSourceId();
        LaxinPayOrder laxinPayOrder = laxinPayOrderMapper.find(laxinPayOrderId);
        if (laxinPayOrder != null) {
            if (laxinPayOrderMapper.payOk(laxinPayOrderId, LaxinPayOrder.Status.WAIT.getValue(), LaxinPayOrder.Status.SUCCESS.getValue(), new Date()) == 1) {
                Date now = new Date();
                List<LaxinRecord> laxinRecordList = laxinRecordMapper.findByOrderId(laxinPayOrderId);
                for (LaxinRecord record : laxinRecordList) {
                    laxinRecordMapper.updateStatus(record.getId(), laxinPayOrderId, LaxinRecord.Status.TRANSFER.getValue(), payType, now);
                }

            }
        }

        return true;
    }

    /**
     * 运营商押金充值
     *
     * @param payOrder
     */
    @Transactional(rollbackFor = Throwable.class)
    public void agentForegiftDepositOrderPayOk(PayOrder payOrder) {
        AgentForegiftDepositOrder agentForegiftDepositOrder = agentForegiftDepositOrderMapper.find(payOrder.getSourceId());
        if (agentForegiftDepositOrder != null) {
            int effect = agentForegiftDepositOrderMapper.payOk(payOrder.getSourceId(), new Date(), AgentForegiftDepositOrder.Status.NOT_PAID.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
            if (effect > 0) {
                if(agentForegiftDepositOrder.getCategory() == ConstEnum.Category.EXCHANGE.getValue()){
                    //更新运营商押金余额 预留金额  押金余额比例
                    List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                            CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
                    //押金余额
                    int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(agentForegiftDepositOrder.getAgentId(), statusList);
                    //运营商押金充值
                    int deposit =  agentForegiftDepositOrderMapper.sumMoney(agentForegiftDepositOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
                    //运营商提现
                    int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(agentForegiftDepositOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(),  AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

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
                    if(agentMapper.updateForegift(agentForegiftDepositOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
                        //运营商押金流水
                        AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
                        foregiftInOutMoney.setAgentId(agentForegiftDepositOrder.getAgentId());
                        foregiftInOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
                        foregiftInOutMoney.setMoney(agentForegiftDepositOrder.getMoney());
                        foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                        foregiftInOutMoney.setBizId(agentForegiftDepositOrder.getId());
                        foregiftInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                        foregiftInOutMoney.setBalance(foregiftBalance);
                        foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
                        foregiftInOutMoney.setRatio(foregiftBalanceRatio);
                        foregiftInOutMoney.setOperator(agentForegiftDepositOrder.getOperator());
                        foregiftInOutMoney.setCreateTime(new Date());
                        agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
                    }

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(agentForegiftDepositOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(agentForegiftDepositOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    partnerInOutMoney.setBizId(agentForegiftDepositOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(agentForegiftDepositOrder.getMoney());
                    partnerInOutMoney.setOperator(agentForegiftDepositOrder.getOperator());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }else if(agentForegiftDepositOrder.getCategory() == ConstEnum.Category.RENT.getValue()){
                    //更新运营商押金余额 预留金额  押金余额比例
                    List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                            CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
                    //押金余额
                    int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(agentForegiftDepositOrder.getAgentId(), statusList);
                    //运营商押金充值
                    int deposit =  agentForegiftDepositOrderMapper.sumMoney(agentForegiftDepositOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
                    //运营商提现
                    int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(agentForegiftDepositOrder.getAgentId(), ConstEnum.Category.RENT.getValue(),  AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

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
                    if(agentMapper.updateZdForegift(agentForegiftDepositOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
                        //运营商押金流水
                        AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
                        foregiftInOutMoney.setAgentId(agentForegiftDepositOrder.getAgentId());
                        foregiftInOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
                        foregiftInOutMoney.setMoney(agentForegiftDepositOrder.getMoney());
                        foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                        foregiftInOutMoney.setBizId(agentForegiftDepositOrder.getId());
                        foregiftInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                        foregiftInOutMoney.setBalance(foregiftBalance);
                        foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
                        foregiftInOutMoney.setRatio(foregiftBalanceRatio);
                        foregiftInOutMoney.setOperator(agentForegiftDepositOrder.getOperator());
                        foregiftInOutMoney.setCreateTime(new Date());
                        agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
                    }

                    //商户流水
                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(agentForegiftDepositOrder.getPayType()));
                    partnerInOutMoney.setPartnerId(agentForegiftDepositOrder.getPartnerId());
                    partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.IN_CUSTOMER_DEPOSIT_PAY.getValue());
                    partnerInOutMoney.setBizId(agentForegiftDepositOrder.getId());
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    partnerInOutMoney.setMoney(agentForegiftDepositOrder.getMoney());
                    partnerInOutMoney.setOperator(agentForegiftDepositOrder.getOperator());
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }

            }
        }
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
     * 租车多通道支付回调
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
                String groupOrderId = null;
                for(CustomerMultiOrderDetail orderDetail : customerMultiOrderDetailList){
                    if(orderDetail.getSourceType() == CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue()){
                        groupOrderId = orderDetail.getSourceId();
                    }
                }
                String sourceId;
                if(groupOrderId != null){
                    sourceId = VEHICLE_GROUP_ORDER.getValue() + ":" + groupOrderId;
                    payOrder.setSourceId(sourceId);
                    multiGroupOrderPayOk(payOrder);
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
