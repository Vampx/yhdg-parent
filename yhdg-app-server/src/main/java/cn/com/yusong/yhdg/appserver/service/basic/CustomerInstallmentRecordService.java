package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.zd.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerInstallmentRecordService extends AbstractService {
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;

    public List<CustomerInstallmentRecord> findList(long customerId, int status, int category) {
        return customerInstallmentRecordMapper.findList(customerId, status, category);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, String batteryId, long id, int agentId, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CustomerInstallmentRecordPayDetail detail = customerInstallmentRecordPayDetailMapper.find(id);
        if (detail == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户分期记录不存在");
        }

        Battery battery = null;
        ShopStoreBattery shopStoreBattery = null;
        Shop shop = null;
        if (StringUtils.isNotEmpty(batteryId)) {
            battery = batteryMapper.find(batteryId);
            if (battery == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
            }
            shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
            if(shopStoreBattery == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
            }
            shop = shopMapper.find(shopStoreBattery.getShopId());
            if(shop == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
            }
        }
        Integer sourceType;
        if (detail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
            sourceType = PayOrder.SourceType.CUSTOMER_EXCHANGE_FIRST_MONEY_PAY.getValue();
        } else {
            sourceType = PayOrder.SourceType.CUSTOMER_RENT_FIRST_MONEY_PAY.getValue();
        }

        String sourceId = String.valueOf(detail.getRecordId());

        String memo = String.format("首付金额:%.2f", detail.getMoney() / 100.0);

        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customer.getId());
            weixinPayOrder.setMoney(detail.getMoney());
            weixinPayOrder.setSourceType(sourceType);
            weixinPayOrder.setSourceId(sourceId);
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
        }else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), sourceId, detail.getMoney(), customer.getId(), sourceType, "客户押金首付支付费用", "客户押金首付支付费用", memo);
            map.put("orderId", detail.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(agent.getId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(detail.getMoney());
            weixinmpPayOrder.setSourceType(sourceType);
            weixinmpPayOrder.setSourceId(sourceId);
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), agent.getId(), sourceId, detail.getMoney(), customerId, sourceType, "客户押金首付支付费用", "客户押金首付支付费用", memo);
            map.put("orderId", detail.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, String batteryId, int agentId, long id) {
        Agent agent = agentMapper.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        Battery battery = null;
        ShopStoreBattery shopStoreBattery = null;
        Shop shop = null;
        if (StringUtils.isNotEmpty(batteryId)) {
            battery = batteryMapper.find(batteryId);
            if (battery == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
            }
            shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
            if(shopStoreBattery == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
            }
            shop = shopMapper.find(shopStoreBattery.getShopId());
            if(shop == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
            }
        }

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        CustomerInstallmentRecordPayDetail detail = customerInstallmentRecordPayDetailMapper.find(id);
        if (detail == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户分期记录不存在");
        }

        int totalMoney = detail.getMoney();

        if ((customer.getBalance() + customer.getGiftBalance()) < totalMoney) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        int balance = customer.getBalance();

        RestResult restResult = updateCustomerBalance(customer, totalMoney, new ArrayList<Integer>());
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        int consumeBalance = 0;
        if (balance >= totalMoney) {
            consumeBalance = totalMoney;
        } else {
            consumeBalance = customer.getBalance();
        }
        balance -= consumeBalance;

        if (totalMoney > 0) {
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(customer.getId());
            inOutMoney.setMoney(-totalMoney);
            if (detail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT_PAY.getValue());
            } else {
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RENT_FOREGIFT_PAY.getValue());
            }
            inOutMoney.setBizId(detail.getId().toString());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(balance);
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        //更新欠款记录付款状态
        customerInstallmentRecordPayDetailMapper.updateApplyDetail(detail.getId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue(),
                CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), new Date(), detail.getMoney(), ConstEnum.PayType.BALANCE.getValue());

        //更新分期记录状态
        customerInstallmentRecordMapper.updateOrderStatus(detail.getRecordId(), CustomerInstallmentRecord.Status.PAY_ING.getValue(), CustomerInstallmentRecord.Status.WAIT_PAY.getValue());

        //更新分期记录已支付金额
        customerInstallmentRecordMapper.updatePaidMoney(detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

        //换电押金首付
        if (detail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {

            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
            }

            CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(detail.getRecordId(),
                    OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());

            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerForegiftOrderDetail.getSourceId());
            if (customerForegiftOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
            }

            CustomerInstallmentRecordOrderDetail packetPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(detail.getRecordId(),
                    OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());

            PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(packetPeriodOrderDetail.getSourceId());
            if (packetPeriodOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金订单不存在");
            }

            if (packetPeriodOrder != null) {

                //更新租金订单状态
                packetPeriodOrderMapper.updateOrderStatus(packetPeriodOrderDetail.getSourceId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.NOT_PAY.getValue());

                //更新租金订单已支付金额
                packetPeriodOrderMapper.updatePaidMoney(packetPeriodOrderDetail.getSourceId(), detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //租金赠送

                String sourceId = packetPeriodOrder.getId();
                Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();
                Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();

                super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, packetPeriodOrder.getDayCount(),agentId, CustomerCouponTicket.TicketType.RENT.getValue(), packetPeriodOrder.getCustomerMobile());

            }

            if (customerForegiftOrder != null) {

                //更新押金订单状态
                customerForegiftOrderMapper.updateOrderStatus(customerForegiftOrderDetail.getSourceId(), CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());

                //更新押金订单已支付金额
                customerForegiftOrderMapper.updatePaidMoney(customerForegiftOrderDetail.getSourceId(), detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                CustomerForegiftOrder customerForegiftOrder1 = customerForegiftOrderMapper.find(customerForegiftOrderDetail.getSourceId());

                //运营商首单赠送押金券
                int count = customerForegiftOrderMapper.findCountByCustomerId(customerForegiftOrder.getId(), agent.getId(), customer.getId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
                    String sourceId = customerForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.EXCHANGE.getValue(), sourceType, type, 0,agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());
                }

                //更新用户押金
                customerExchangeInfo = new CustomerExchangeInfo();
                customerExchangeInfo.setId(customerId);
                customerExchangeInfo.setAgentId(agentId);
                customerExchangeInfo.setBatteryType(customerForegiftOrder.getBatteryType());
                customerExchangeInfo.setForegift(customerForegiftOrder1.getMoney());
                customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
                customerExchangeInfo.setBalanceCabinetId(customerForegiftOrder.getCabinetId());
                customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
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
                        IdCardAuthRecord idCardAuthRecord = new IdCardAuthRecord();
                        idCardAuthRecord.setAgentId(agent.getId());
                        idCardAuthRecord.setAgentName(agent.getAgentName());
                        idCardAuthRecord.setAgentCode(agent.getAgentCode());
                        idCardAuthRecord.setCustomerId(customer.getId());
                        idCardAuthRecord.setMobile(customer.getMobile());
                        idCardAuthRecord.setFullname(customer.getFullname());
                        idCardAuthRecord.setMoney(idCardAuthMoney);
                        idCardAuthRecord.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                        idCardAuthRecord.setCreateTime(new Date());
                        idCardAuthRecordMapper.insert(idCardAuthRecord);
                        customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.CREATED.getValue());
                    } else {
                        customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.AVOID.getValue());
                    }
                }

                //handleLaxinCustomer(agent, customer, customerForegiftOrder.getMoney(), packetPeriodOrder.getMoney());

                //余额支付押金成功后，推送
                PushMetaData metaData = new PushMetaData();
                metaData.setSourceId(customerForegiftOrder.getId());
                metaData.setSourceType(PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
                metaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(metaData);
            }

        } else {

            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
            if (customerRentInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买电池押金");
            }

            CustomerInstallmentRecordOrderDetail rentForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(detail.getRecordId(),
                    OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());

            RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(rentForegiftOrderDetail.getSourceId());
            if (rentForegiftOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
            }

            CustomerInstallmentRecordOrderDetail rentPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(detail.getRecordId(),
                    OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());

            RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(rentPeriodOrderDetail.getSourceId());
            if (rentPeriodOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金订单不存在");
            }

            CustomerInstallmentRecordOrderDetail rentInsuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(detail.getRecordId(),
                    OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue());

            if (rentInsuranceOrderDetail != null) {
                RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderMapper.find(rentInsuranceOrderDetail.getSourceId());
                if (rentInsuranceOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "保险订单不存在");
                }

                if (rentInsuranceOrder != null) {
                    //更新保险订单状态
                    rentInsuranceOrderMapper.updateOrderStatus(rentInsuranceOrder.getId(), RentInsuranceOrder.Status.PAID.getValue(), RentInsuranceOrder.Status.NOT_PAY.getValue());
                    //更新保险订单已支付金额
                    rentInsuranceOrderMapper.updatePaidMoney(rentInsuranceOrder.getId(), detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                }
            }


            if (rentPeriodOrder != null) {

                //更新租金订单状态
                rentPeriodOrderMapper.updateOrderStatus(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_USE.getValue(), RentPeriodOrder.Status.NOT_PAY.getValue());
                //更新租金订单已支付金额
                rentPeriodOrderMapper.updatePaidMoney(rentPeriodOrder.getId(), detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                //租金赠送
                Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
                String sourceId = rentPeriodOrder.getId();
                Integer sourceType = OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue();

                super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, rentPeriodOrder.getDayCount(), agentId, CustomerCouponTicket.TicketType.RENT.getValue(), rentPeriodOrder.getCustomerMobile());

            }

            if (rentForegiftOrder != null) {

                //更新押金订单状态
                rentForegiftOrderMapper.updateOrderStatus(rentForegiftOrder.getId(), RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.WAIT_PAY.getValue());

                //更新押金订单已支付金额
                rentForegiftOrderMapper.updatePaidMoney(rentForegiftOrder.getId(), detail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

                RentForegiftOrder rentForegiftOrder1 = rentForegiftOrderMapper.find(rentForegiftOrder.getId());

                //运营商首单赠送押金券
                int count = rentForegiftOrderMapper.findCountByCustomerId(rentForegiftOrder.getId(), agent.getId(), customer.getId(), CustomerForegiftOrder.Status.WAIT_PAY.getValue());
                if (count == 0) {
                    //押金赠送
                    Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
                    String sourceId = rentForegiftOrder.getId();
                    Integer sourceType = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue();

                    super.giveCouponTicket(sourceId, ConstEnum.Category.RENT.getValue(), sourceType, type, 0 , agentId, CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());
                }

                //更新用户押金
                customerRentInfo = new CustomerRentInfo();
                customerRentInfo.setId(customerId);
                customerRentInfo.setAgentId(agent.getId());
                customerRentInfo.setBatteryType(battery.getType());
                customerRentInfo.setForegift(rentForegiftOrder1.getMoney());
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
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(idCardAuthMoneyValue)) {
                        idCardAuthMoney = (int) (Float.parseFloat(idCardAuthMoneyValue) * 100);
                    }

                    if (idCardAuthMoney > 0) {
                        IdCardAuthRecord idCardAuthRecord = new IdCardAuthRecord();
                        idCardAuthRecord.setAgentId(agent.getId());
                        idCardAuthRecord.setAgentName(agent.getAgentName());
                        idCardAuthRecord.setAgentCode(agent.getAgentCode());
                        idCardAuthRecord.setCustomerId(customer.getId());
                        idCardAuthRecord.setMobile(customer.getMobile());
                        idCardAuthRecord.setFullname(customer.getFullname());
                        idCardAuthRecord.setMoney(idCardAuthMoney);
                        idCardAuthRecord.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                        idCardAuthRecord.setCreateTime(new Date());
                        idCardAuthRecordMapper.insert(idCardAuthRecord);
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
            }

        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
