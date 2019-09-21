package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.persistence.zc.*;
import cn.com.yusong.yhdg.webserver.persistence.zd.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import cn.com.yusong.yhdg.webserver.weixin.WxPayUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class GroupOrderService extends AbstractService {

    static Logger log = LoggerFactory.getLogger(GroupOrderService.class);

    @Autowired
    GroupOrderMapper groupOrderMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;
    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    AgentForegiftRefundMapper agentForegiftRefundMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    AlipayPayOrderRefundMapper alipayPayOrderRefundMapper;
    @Autowired
    AlipayfwPayOrderRefundMapper alipayfwPayOrderRefundMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    WeixinPayOrderRefundMapper weixinPayOrderRefundMapper;
    @Autowired
    WeixinmpPayOrderRefundMapper weixinmpPayOrderRefundMapper;
    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    CustomerRefundRecordMapper customerRefundRecordMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    VehicleOrderMapper vehicleOrderMapper;
    @Autowired
    ShopStoreVehicleMapper shopStoreVehicleMapper;
    @Autowired
    ShopStoreVehicleBatteryMapper shopStoreVehicleBatteryMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;

    public GroupOrder find(String id) {
        GroupOrder groupOrder = groupOrderMapper.find(id);
        if (groupOrder.getModelId() != null) {
            VehicleModel vehicleModel = vehicleModelMapper.find(groupOrder.getModelId());
            if (vehicleModel != null) {
                groupOrder.setModelName(vehicleModel.getModelName());
            }
        }
        return groupOrder;
    }

    public Page findPage(GroupOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(groupOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<GroupOrder> list = groupOrderMapper.findPageResult(search);
        for (GroupOrder groupOrder : list) {
            if (groupOrder.getModelId() != null) {
                VehicleModel vehicleModel = vehicleModelMapper.find(groupOrder.getModelId());
                if (vehicleModel != null) {
                    groupOrder.setModelName(vehicleModel.getModelName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    /**
     * 退款
     *
     * @param userName    操作人
     * @param refundType  退款方式
     * @param sourceType  来源类型
     * @param sourceId    来源id
     * @param refundMoney 退款金额
     * @return
     * @throws AlipayApiException
     */
    @Transactional(rollbackFor = Throwable.class)
    public DataResult refund(String userName, int refundType, int sourceType, String sourceId, int refundMoney, int refundableMoney, Long refundRecordId, String orderId, boolean test) throws AlipayApiException {

        //先还车再退款
        //如果已经换车成功，就不再还车
        GroupOrder groupOrder = groupOrderMapper.find(sourceId);
        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(groupOrder.getCustomerId());
        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null) {
            VehicleOrder vehicleOrder = vehicleOrderMapper.find(customerVehicleInfo.getVehicleOrderId());
            if (vehicleOrder != null && vehicleOrder.getStatus() != VehicleOrder.Status.BACK.getValue()) {
                backVehicle(userName, sourceId);
            }
        }

        Map<String, String> data = new HashMap();
        //租车组合支付退款
        if (sourceType == CustomerRefundRecord.SourceType.ZCGROUP.getValue()) {
            if (groupOrder == null) {
                return DataResult.failResult("订单不存在", data);
            }
            if (StringUtils.isEmpty(orderId) && groupOrder.getStatus() != GroupOrder.Status.APPLY_REFUND.getValue()) {
                return DataResult.failResult("申请退款状态不对", data);
            }
            if (refundMoney > refundableMoney) {
                return DataResult.failResult("退款金额不能大于可退金额", data);
            }

            String memo = null;
            //判断用户退款金额是否大于支付金额，如果大于支付金额，剩下的钱加入资金账户
            int payMoney = refundMoney;
            int balanceMoney = 0;

            if (refundMoney > groupOrder.getMoney()) {
                payMoney = groupOrder.getMoney();
                balanceMoney = refundMoney - groupOrder.getMoney();
            }

            int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_VEHICLE_GROUP_REFUND.getValue();
            //退款
            DataResult result = doRefund(sourceType, refundType, bizType, groupOrder.getPayType(), groupOrder.getId(), data, payMoney, refundMoney, groupOrder.getCustomerId(), groupOrder.getPartnerId(), userName, "租车组合订单退款", test);
            if (!result.isSuccess()) {
                return result;
            }

            //抵扣券退款
            if (balanceMoney > 0) {
                if (customerMapper.updateBalance(groupOrder.getCustomerId(), balanceMoney, 0) > 0) {
                    //余额流水
                    Customer customer = customerMapper.find(groupOrder.getCustomerId());
                    CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
                    customerInOutMoney.setCustomerId(groupOrder.getCustomerId());
                    customerInOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEDUCTION_TICKET.getValue());
                    customerInOutMoney.setBizId(groupOrder.getId());
                    customerInOutMoney.setMoney(balanceMoney);
                    customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    customerInOutMoney.setBalance(customer.getBalance());
                    customerInOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(customerInOutMoney);
                } else {
                    memo = "退款抵扣券金额失败";
                }
            }
            //更新组合订单
            groupOrderMapper.updateStatus(groupOrder.getId(),
                    GroupOrder.Status.REFUND_SUCCESS.getValue(),
                    refundMoney, userName, new Date(),
                    null, memo, new Date());

            //更新租车押金订单状态
            if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
                VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.find(groupOrder.getVehicleForegiftId());
                if (vehicleForegiftOrder != null) {
                    vehicleForegiftOrderMapper.updateStatus(vehicleForegiftOrder.getId(),
                            VehicleForegiftOrder.Status.REFUND_SUCCESS.getValue(),
                            0, userName, new Date(), "组合订单退款", new Date()
                    );
                }
            }

            //更新租车租金订单状态
            if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
                VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());
                if (vehiclePeriodOrder != null) {
                    vehiclePeriodOrderMapper.updateStatus(vehiclePeriodOrder.getId(),
                            VehiclePeriodOrder.Status.REFUND.getValue(),
                            0, userName, new Date()
                    );
                }
            }

            //更新电池押金订单状态
            if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                    if (customerForegiftOrder != null) {
                        customerForegiftOrderMapper.updateRefund(customerForegiftOrder.getId(),
                                customerForegiftOrder.getApplyRefundTime(), "组合订单退款",
                                CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), customerForegiftOrder.getStatus());
                    }
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                    if (rentForegiftOrder != null) {
                        rentForegiftOrderMapper.updateRefund(rentForegiftOrder.getId(),
                                rentForegiftOrder.getApplyRefundTime(), "组合订单退款",
                                RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), rentForegiftOrder.getStatus());
                    }
                }
            }

            //更新电池租金订单状态
            if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                    if (packetPeriodOrder != null) {
                        packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(),
                                0, new Date(), PacketPeriodOrder.Status.REFUND.getValue());
                    }
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                    if (rentPeriodOrder != null) {
                        rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), 0, new Date(), RentPeriodOrder.Status.REFUND.getValue());
                    }
                }
            }

            //清空客户绑定租车押金信息
            customerVehicleInfoMapper.delete(groupOrder.getCustomerId());
            //清空客户绑定的电池押金信息
            if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                customerExchangeInfoMapper.delete(groupOrder.getCustomerId());
            } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                customerRentInfoMapper.delete(groupOrder.getCustomerId());
            }
            //存在未使用的优惠券 将优惠券状态置为失效
            customerCouponTicketMapper.findBySource(groupOrder.getId(), OrderId.OrderIdType.VEHICLE_GROUP_ORDER.getValue());

            if (groupOrder.getPayType() == ConstEnum.PayType.MULTI_CHANNEL.getValue()) {
                CustomerMultiOrderDetail customerMultiOrderDetail = customerMultiOrderDetailMapper.findBySource(groupOrder.getId(), CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue());
                if (customerMultiOrderDetail == null) {
                    return DataResult.failResult("多通道订单明细不存在", data);
                }
                CustomerMultiOrder customerMultiOrder = customerMultiOrderMapper.find(customerMultiOrderDetail.getOrderId());
                if (customerMultiOrder == null) {
                    return DataResult.failResult("多通道订单不存在", data);
                }
                if (StringUtils.isEmpty(orderId) &&  customerMultiOrder.getStatus() != CustomerMultiOrder.Status.APPLY_REFUND.getValue()) {
                    return DataResult.failResult("申请退款状态不对", data);
                }
                if (refundMoney > customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney()) {
                    return DataResult.failResult("退款金额不能大于支付金额", data);
                }
                if(refundType != CustomerRefundRecord.RefundType.BALANCE.getValue()){
                    return DataResult.failResult("请选择退到余额", data);
                }
                customerMultiOrderMapper.updateRefund(customerMultiOrder.getId(), refundMoney, new Date(), CustomerMultiOrder.Status.REFUND_SUCCESS.getValue());
            }
//            String foregiftOrderId = null;
//            //押金加入押金池
//            if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
//                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
//                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
//                    if (customerForegiftOrder != null) {
//                        foregiftOrderId = customerForegiftOrder.getId();
//                        handleAgentForegift(customerForegiftOrder, null);
//                    }
//                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
//                    RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
//                    if (rentForegiftOrder != null) {
//                        foregiftOrderId = rentForegiftOrder.getId();
//                        handleAgentForegift(null, rentForegiftOrder);
//                    }
//                }
//            }

            //分期付订单处理
            if (groupOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()) {
                //如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
                if (groupOrder.getPayTime() == null) {
                    groupOrderMapper.updatePayTime(groupOrder.getId(), new Date());
                }
                handleCustomerInstallment(sourceId, OrderId.OrderIdType.VEHICLE_GROUP_ORDER.getValue(), ConstEnum.Category.VEHICLE.getValue());
            }

            //保存退款记录
            CustomerRefundRecord customerRefundRecord = insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), groupOrder.getPartnerId(), groupOrder.getAgentId(), groupOrder.getAgentName(), groupOrder.getCustomerId(), groupOrder.getCustomerMobile(), groupOrder.getCustomerFullname(), groupOrder.getId());

//            //生成运营商押金退款订单
//            if (refundMoney < refundableMoney) {
//                AgentForegiftRefund agentForegiftRefund = new AgentForegiftRefund();
//                agentForegiftRefund.setAgentCode(groupOrder.getAgentCode());
//                agentForegiftRefund.setAgentId(groupOrder.getAgentId());
//                agentForegiftRefund.setAgentName(groupOrder.getAgentName());
//                agentForegiftRefund.setCustomerId(groupOrder.getCustomerId());
//                agentForegiftRefund.setDeductionTicketMoney(groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());
//                agentForegiftRefund.setForegiftOrderId(foregiftOrderId);
//                agentForegiftRefund.setFullname(groupOrder.getCustomerFullname() != null ? groupOrder.getCustomerFullname() : groupOrder.getCustomerMobile());
//                agentForegiftRefund.setMobile(groupOrder.getCustomerMobile());
//                agentForegiftRefund.setOperatorName(groupOrder.getRefundOperator());
//                agentForegiftRefund.setPrice(groupOrder.getPrice());
//                agentForegiftRefund.setRefundMoney(groupOrder.getRefundMoney() == null ? 0 : groupOrder.getRefundMoney());
//                agentForegiftRefund.setRefundRecordId(customerRefundRecord.getId() == null ? refundRecordId : customerRefundRecord.getId());
//                agentForegiftRefund.setPayMoney(groupOrder.getMoney());
//                agentForegiftRefund.setTicketMoney(0);
//                agentForegiftRefund.setCreateTime(new Date());
//                agentForegiftRefund.setRemainMoney(refundableMoney - refundMoney);
//                agentForegiftRefundMapper.insert(agentForegiftRefund);
//            }
            //客户退租车组合订单消费轨迹
            addCustomerPayTrack(groupOrder.getAgentId(), groupOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_VEHICLE_GROUP.getValue(),
                    StringUtils.replaceEach("退租车组合订单，金额：${refundMoney}。",
                            new String[]{"${refundMoney}"},
                            new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));



        }
        return DataResult.successResult(data);
    }

    public DataResult doRefund(int sourceType, int refundType, int bizType, int payType,
                               String sourceId, Map data, Integer payMoney, int refundMoney, long customerId, int partnerId,
                               String userName, String refundReason, boolean test){
        if(payMoney == null){
            payMoney = refundMoney;
        }
        String ptPayOrderId;
        boolean thirdPay = false;
        //根据退款方式进行退款
        if(refundType == CustomerRefundRecord.RefundType.BALANCE.getValue()){ //余额退款
            if(customerMapper.updateBalance(customerId, payMoney, 0) > 0){
                //余额流水
                Customer customer = customerMapper.find(customerId);
                CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
                customerInOutMoney.setCustomerId(customerId);
                customerInOutMoney.setMoney(payMoney);
                customerInOutMoney.setBizType(bizType);
                customerInOutMoney.setBizId(sourceId);
                customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                customerInOutMoney.setBalance(customer.getBalance());
                customerInOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(customerInOutMoney);
            }else{
                return DataResult.failResult("更新用户余额失败", data);
            }
        }else{ //原路返回
            if(payMoney > 0){
                if (payType == ConstEnum.PayType.ALIPAY.getValue()) {
                    thirdPay = true;
                    AlipayPayOrder alipayPayOrder = alipayPayOrderMapper.findBySourceId(sourceId);
                    if (alipayPayOrder == null) {
                        return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "订单不存在", data);
                    }
                    ptPayOrderId = alipayPayOrder.getId();
                    data.put("ptPayOrderId", ptPayOrderId);

                    Partner partner = partnerMapper.find(alipayPayOrder.getPartnerId());
                    String appId = partner.getAlipayAppId();
                    String alipayPublic = partner.getAlipayAliKey();
                    String alipayAppRsaPrivate = partner.getAlipayPriKey();

                    double refundAmount = ((double) payMoney) / 100;
                    AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayAppRsaPrivate, "json", "GBK", alipayPublic, "RSA2");
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    request.setBizContent("{" +
                            "\"out_trade_no\":\"" + alipayPayOrder.getId() + "\"," +
                            "\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
                            "\"refund_amount\":" + refundAmount + "," +
                            "\"refund_reason\":\"" + refundReason + "\"" +
                            "  }");
                    try {
                        AlipayTradeRefundResponse response;
                        if(test){
                            response = new AlipayTradeRefundResponse();
                            response.setSubCode(null);
                        }else {
                            response = alipayClient.execute(request);
                        }
                        if (response != null && response.isSuccess()) {
                            alipayPayOrderMapper.refundOk(alipayPayOrder.getId(), payMoney, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
                            //加入退款订单
                            addPayOrderRefund(alipayPayOrder, ConstEnum.PayType.ALIPAY.getValue(), sourceType, payMoney);
                        }else{
                            return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg(), data);
                        }
                    } catch (Exception e) {
                        log.error("Alipay退款失败", e);
                        return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + e.getMessage(), data);
                    }

                } else if (payType == ConstEnum.PayType.WEIXIN.getValue()) {
                    thirdPay = true;
                    WeixinPayOrder weixinPayOrder = weixinPayOrderMapper.findBySourceId(sourceId);
                    if (weixinPayOrder == null) {
                        return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "订单不存在", data);
                    }
                    ptPayOrderId = weixinPayOrder.getId();
                    data.put("ptPayOrderId", ptPayOrderId);

                    Partner partner = partnerMapper.find(weixinPayOrder.getPartnerId());
                    String appId = partner.getWeixinAppId();
                    String mchId = partner.getWeixinPartnerCode();
                    String partnerKey = partner.getWeixinPartnerKey();

                    String outTradeNo = weixinPayOrder.getId();
                    int totalFee = weixinPayOrder.getMoney();
                    int refundFee = payMoney;

                    try {
                        WxPayUnifiedOrderResult result;
                        if(test){
                            result = new WxPayUnifiedOrderResult();
                            result.setReturnCode("SUCCESS");
                        }else {
                            File certFile = AppUtils.getWeixinCertFile(appConfig.appDir, weixinPayOrder.getPartnerId());
                            if(!certFile.exists()) {
                                return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "证书不存在", data);
                            }
                            result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, refundReason, certFile);
                        }

                        if (result != null && result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                            weixinPayOrderMapper.refundOk(weixinPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
                            //加入退款订单
                            addPayOrderRefund(weixinPayOrder, ConstEnum.PayType.WEIXIN.getValue(), sourceType, payMoney);
                        }else{
                            return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + result.getReturnCode()  + result.getErrCodeDes(), data);
                        }
                    } catch (Exception e) {
                        log.error("Weixin退款失败", e);
                        return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + e.getMessage(), data);
                    }

                } else if (payType == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                    thirdPay = true;
                    WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderMapper.findBySourceId(sourceId);
                    if (weixinmpPayOrder == null) {
                        return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "订单不存在", data);
                    }
                    ptPayOrderId = weixinmpPayOrder.getId();
                    data.put("ptPayOrderId", ptPayOrderId);

                    Partner partner = partnerMapper.find(weixinmpPayOrder.getPartnerId());
                    String appId = partner.getMpAppId();
                    String mchId = partner.getMpPartnerCode();
                    String partnerKey = partner.getMpPartnerKey();

                    String outTradeNo = weixinmpPayOrder.getId();
                    int totalFee = weixinmpPayOrder.getMoney();
                    int refundFee = payMoney;

                    try {
                        WxPayUnifiedOrderResult result;
                        if(test){
                            result = new WxPayUnifiedOrderResult();
                            result.setReturnCode("SUCCESS");
                        }else {
                            File certFile = AppUtils.getWeixinmpCertFile(appConfig.appDir, weixinmpPayOrder.getPartnerId());
                            if(!certFile.exists()) {
                                return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "证书不存在", data);
                            }
                            result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, refundReason, certFile);
                        }

                        if (result != null && result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                            weixinmpPayOrderMapper.refundOk(weixinmpPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
                            //加入退款订单
                            addPayOrderRefund(weixinmpPayOrder, ConstEnum.PayType.WEIXIN_MP.getValue(), sourceType, payMoney);
                        }else{
                            return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + result.getReturnCode()  + result.getErrCodeDes(), data);
                        }
                    } catch (Exception e) {
                        log.error("WeixinMp退款失败", e);
                        return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + e.getMessage(), data);
                    }

                } else if (payType == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                    thirdPay = true;
                    AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderMapper.findBySourceId(sourceId);
                    if (alipayfwPayOrder == null) {
                        return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "订单不存在", data);
                    }
                    ptPayOrderId = alipayfwPayOrder.getId();
                    data.put("ptPayOrderId", ptPayOrderId);

                    Partner partner = partnerMapper.find(alipayfwPayOrder.getPartnerId());
                    String appId = partner.getFwAppId();
                    String alipayfwPublic = partner.getFwAliKey();
                    String alipayfwAppRsaPrivate = partner.getFwPriKey();

                    double refundAmount = ((double) payMoney) / 100;
                    AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayfwAppRsaPrivate, "json", "GBK", alipayfwPublic, "RSA2");
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    request.setBizContent("{" +
                            "\"out_trade_no\":\"" + alipayfwPayOrder.getId() + "\"," +
                            "\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
                            "\"refund_amount\":" + refundAmount + "," +
                            "\"refund_reason\":\"" + refundReason + "\"" +
                            "  }");
                    try {
                        AlipayTradeRefundResponse response;
                        if(test){
                            response = new AlipayTradeRefundResponse();
                            response.setSubCode(null);
                        }else {
                            response = alipayClient.execute(request);
                        }
                        if (response != null && response.isSuccess()) {
                            alipayfwPayOrderMapper.refundOk(alipayfwPayOrder.getId(), payMoney, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
                            //加入退款订单
                            addPayOrderRefund(alipayfwPayOrder, ConstEnum.PayType.ALIPAY_FW.getValue(), sourceType, payMoney);
                        }else{
                            return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg(), data);
                        }

                    } catch (Exception e) {
                        log.error("AlipayFw退款失败", e);
                        return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + e.getMessage(), data);
                    }

                } else {
                    if(customerMapper.updateBalance(customerId, payMoney, 0) > 0){
                        //余额流水
                        Customer customer = customerMapper.find(customerId);
                        CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
                        customerInOutMoney.setCustomerId(customerId);
                        customerInOutMoney.setMoney(payMoney);
                        customerInOutMoney.setBizType(bizType);
                        customerInOutMoney.setBizId(sourceId);
                        customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        customerInOutMoney.setBalance(customer.getBalance());
                        customerInOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(customerInOutMoney);
                    }else{
                        return DataResult.failResult("更新用户余额失败", data);
                    }
                }

                //商户流水
                if(thirdPay) {
                    //客户收入流水
                    Customer customer = customerMapper.find(customerId);
                    CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
                    customerInOutMoney.setCustomerId(customerId);
                    customerInOutMoney.setMoney(refundMoney);
                    customerInOutMoney.setBizType(bizType);
                    customerInOutMoney.setBizId(sourceId);
                    customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                    customerInOutMoney.setBalance(customer.getBalance() + refundMoney);
                    customerInOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(customerInOutMoney);
                    //客户支出流水
                    customerInOutMoney = new CustomerInOutMoney();
                    customerInOutMoney.setCustomerId(customerId);
                    customerInOutMoney.setMoney(-refundMoney);
                    customerInOutMoney.setBizType(bizType);
                    customerInOutMoney.setBizId(sourceId);
                    customerInOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    customerInOutMoney.setBalance(customer.getBalance());
                    customerInOutMoney.setCreateTime(new Date());
                    customerInOutMoneyMapper.insert(customerInOutMoney);

                    PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
                    partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(payType));
                    partnerInOutMoney.setPartnerId(partnerId);
                    partnerInOutMoney.setBizType(bizType);
                    partnerInOutMoney.setBizId(sourceId);
                    partnerInOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                    partnerInOutMoney.setMoney(-refundMoney);
                    partnerInOutMoney.setOperator(userName);
                    partnerInOutMoney.setCreateTime(new Date());
                    partnerInOutMoneyMapper.insert(partnerInOutMoney);
                }
            }
        }
        data.put("thirdPay", thirdPay);
        return DataResult.successResult(data);
    }

    /**
     * 保存退款支付订单
     * @param payOrder
     * @param payType
     * @param sourceType
     * @param payMoney
     */
    public void addPayOrderRefund(PayOrder payOrder, int payType, int sourceType, int payMoney){
        Integer bizType = null;
        if(sourceType == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()){
            bizType = PayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue();
        }else if(sourceType == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()){
            bizType = PayOrderRefund.BizType.PACKET_PERIOD_ORDER_REFUND.getValue();
        }else if(sourceType == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()){
            bizType = PayOrderRefund.BizType.INSURANCE_ORDER_REFUND.getValue();
        } else if (sourceType == CustomerRefundRecord.SourceType.ZCGROUP.getValue()) {
            bizType = PayOrderRefund.BizType.VEHICLE_GROUP_ORDER_REFUND.getValue();
        }

        if(payType == ConstEnum.PayType.ALIPAY.getValue()){
            AlipayPayOrderRefund payOrderRefund = new AlipayPayOrderRefund();
            payOrderRefund.setOrderId(payOrder.getId());
            payOrderRefund.setPartnerId(payOrder.getPartnerId());
            payOrderRefund.setAgentId(payOrder.getAgentId());
            payOrderRefund.setAgentName(payOrder.getAgentName());
            payOrderRefund.setRefundMoney(payMoney);
            payOrderRefund.setBizType(bizType);
            payOrderRefund.setBizId(payOrder.getSourceId());
            payOrderRefund.setCustomerId(payOrder.getCustomerId());
            payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
            payOrderRefund.setCustomerMobile(payOrder.getMobile());
            payOrderRefund.setCreateTime(new Date());
            alipayPayOrderRefundMapper.insert(payOrderRefund);
        }else if(payType == ConstEnum.PayType.WEIXIN.getValue()){
            WeixinPayOrderRefund payOrderRefund = new WeixinPayOrderRefund();
            payOrderRefund.setOrderId(payOrder.getId());
            payOrderRefund.setPartnerId(payOrder.getPartnerId());
            payOrderRefund.setAgentId(payOrder.getAgentId());
            payOrderRefund.setAgentName(payOrder.getAgentName());
            payOrderRefund.setRefundMoney(payMoney);
            payOrderRefund.setBizType(bizType);
            payOrderRefund.setBizId(payOrder.getSourceId());
            payOrderRefund.setCustomerId(payOrder.getCustomerId());
            payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
            payOrderRefund.setCustomerMobile(payOrder.getMobile());
            payOrderRefund.setCreateTime(new Date());
            weixinPayOrderRefundMapper.insert(payOrderRefund);
        }else if(payType == ConstEnum.PayType.ALIPAY_FW.getValue()){
            AlipayfwPayOrderRefund payOrderRefund = new AlipayfwPayOrderRefund();
            payOrderRefund.setOrderId(payOrder.getId());
            payOrderRefund.setPartnerId(payOrder.getPartnerId());
            payOrderRefund.setAgentId(payOrder.getAgentId());
            payOrderRefund.setAgentName(payOrder.getAgentName());
            payOrderRefund.setRefundMoney(payMoney);
            payOrderRefund.setBizType(bizType);
            payOrderRefund.setBizId(payOrder.getSourceId());
            payOrderRefund.setCustomerId(payOrder.getCustomerId());
            payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
            payOrderRefund.setCustomerMobile(payOrder.getMobile());
            payOrderRefund.setCreateTime(new Date());
            alipayfwPayOrderRefundMapper.insert(payOrderRefund);
        }else if(payType == ConstEnum.PayType.WEIXIN_MP.getValue()){
            WeixinmpPayOrderRefund payOrderRefund = new WeixinmpPayOrderRefund();
            payOrderRefund.setOrderId(payOrder.getId());
            payOrderRefund.setPartnerId(payOrder.getPartnerId());
            payOrderRefund.setAgentId(payOrder.getAgentId());
            payOrderRefund.setAgentName(payOrder.getAgentName());
            payOrderRefund.setRefundMoney(payMoney);
            payOrderRefund.setBizType(bizType);
            payOrderRefund.setBizId(payOrder.getSourceId());
            payOrderRefund.setCustomerId(payOrder.getCustomerId());
            payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
            payOrderRefund.setCustomerMobile(payOrder.getMobile());
            payOrderRefund.setCreateTime(new Date());
            weixinmpPayOrderRefundMapper.insert(payOrderRefund);
        }

    }

    /**
     * 运营商押金池
     *
     * @param customerForegiftOrder
     * @param rentForegiftOrder
     */
    private void handleAgentForegift(CustomerForegiftOrder customerForegiftOrder, RentForegiftOrder rentForegiftOrder) {
        if (customerForegiftOrder != null && rentForegiftOrder == null) {
            //更新运营商押金余额 预留金额  押金余额比例
            List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                    CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
            //押金余额
            int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(customerForegiftOrder.getAgentId(), statusList);
            //运营商押金充值
            int deposit = agentForegiftDepositOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
            //运营商提现
            int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

            //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
            int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
            int foregiftBalanceRatio = 100;
            if (foregiftBalance != 0) {
                foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
            }
            if (foregiftRemainMoney < 0) {
                foregiftBalanceRatio = 0;
            }

            //更新运营商押金
            if (agentMapper.updateForegift(customerForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0) {
                //运营商押金流水
                AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
                inOutMoney.setAgentId(customerForegiftOrder.getAgentId());
                inOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
                inOutMoney.setMoney(-customerForegiftOrder.getMoney());
                inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT.getValue());
                inOutMoney.setBizId(customerForegiftOrder.getId());
                inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(foregiftBalance);
                inOutMoney.setRemainMoney(foregiftRemainMoney);
                inOutMoney.setRatio(foregiftBalanceRatio);
                inOutMoney.setOperator(customerForegiftOrder.getCustomerFullname() != null ? customerForegiftOrder.getCustomerFullname() : customerForegiftOrder.getCustomerMobile());
                inOutMoney.setCreateTime(new Date());
                agentForegiftInOutMoneyMapper.insert(inOutMoney);
            }
        } else if (customerForegiftOrder == null && rentForegiftOrder != null) {
            //更新运营商押金余额 预留金额  押金余额比例
            List<Integer> statusList = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(),
                    RentForegiftOrder.Status.APPLY_REFUND.getValue());
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
                inOutMoney.setMoney(-rentForegiftOrder.getMoney());
                inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT.getValue());
                inOutMoney.setBizId(rentForegiftOrder.getId());
                inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(foregiftBalance);
                inOutMoney.setRemainMoney(foregiftRemainMoney);
                inOutMoney.setRatio(foregiftBalanceRatio);
                inOutMoney.setOperator(rentForegiftOrder.getCustomerFullname());
                inOutMoney.setCreateTime(new Date());
                agentForegiftInOutMoneyMapper.insert(inOutMoney);
            }
        }

    }

    /**
     * 分期付订单处理
     */
    private void handleCustomerInstallment(String sourceId, int sourceType, int category) {
        CustomerInstallmentRecordOrderDetail detail = customerInstallmentRecordOrderDetailMapper.find(sourceId, sourceType, category);
        if(detail != null){
            CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.find(detail.getRecordId());
            if (customerInstallmentRecord != null){
                //更新状态
                if(customerInstallmentRecord.getStatus() != CustomerInstallmentRecord.Status.WAIT_PAY.getValue()){
                    customerInstallmentRecordMapper.updateStatus(customerInstallmentRecord.getId(), CustomerInstallmentRecord.Status.REFUND_SUCCESS.getValue());
                    List<CustomerInstallmentRecordPayDetail>  payDetailList = customerInstallmentRecordPayDetailMapper.findListByRecordId(customerInstallmentRecord.getId());
                    for(CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail : payDetailList){
                        if(customerInstallmentRecordPayDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue()){
                            customerInstallmentRecordPayDetailMapper.updateStatus(customerInstallmentRecordPayDetail.getId(), CustomerInstallmentRecordPayDetail.Status.CANCEL.getValue());
                        }
                        if(customerInstallmentRecordPayDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue()){
                            customerInstallmentRecordPayDetailMapper.updateStatus(customerInstallmentRecordPayDetail.getId(), CustomerInstallmentRecordPayDetail.Status.REFUND.getValue());
                        }
                    }
                }
            }
        }
    }

    private CustomerRefundRecord insertRefundRecord(int refundType, int sourceType, int refundMoney, String orderId, String ptPayOrderId, Integer partnerId, Integer agentId, String agentName, Long customerId, String customerMobile, String customerFullname, String id) {
        CustomerRefundRecord refundRecord = new CustomerRefundRecord();
        if(StringUtils.isNotEmpty(orderId)){
            refundRecord.setPartnerId(partnerId);
            refundRecord.setAgentId(agentId);
            refundRecord.setAgentName(agentName);
            refundRecord.setOrderId(orderId);

            refundRecord.setCustomerId(customerId);
            refundRecord.setMobile(customerMobile);
            refundRecord.setFullname(customerFullname != null ? customerFullname : customerMobile);
            refundRecord.setSourceType(sourceType);
            refundRecord.setSourceId(id);

            refundRecord.setRefundMoney(refundMoney);
            refundRecord.setCreateTime(new Date());
            refundRecord.setRefundTime(new Date());
            refundRecord.setStatus(CustomerRefundRecord.Status.FINISH.getValue());
            refundRecord.setRefundType(refundType);
            refundRecord.setPtPayOrderId(ptPayOrderId);
            customerRefundRecordMapper.insert(refundRecord);
        }
        return refundRecord;
    }


    public String getPtPayOrderId(DataResult result) {
        String ptPayOrderId = "";
        if (result.getData() != null) {
            Map m = (Map) result.getData();
            Object objId = m.get("ptPayOrderId");
            if (objId != null) {
                ptPayOrderId = (String) objId;
            }
        }
        return ptPayOrderId;
    }

    public void addCustomerPayTrack(Integer agentId, Long customerId, int trackType, String memo){
        Agent agent = agentMapper.find(agentId);
        Customer customer = customerMapper.find(customerId);

        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname() != null ? customer.getFullname() : customer.getMobile());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(trackType);
        customerPayTrack.setMemo(memo);
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

    public void backVehicle(String userName, String sourceId) {
        GroupOrder groupOrder = groupOrderMapper.find(sourceId);

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(groupOrder.getCustomerId());

        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.find(customerVehicleInfo.getVehicleId());
            Customer customer = customerMapper.find(groupOrder.getCustomerId());
            Shop shop = null;
            if (groupOrder.getShopId() != null) {
                shop = shopMapper.find(groupOrder.getShopId());
            }

            Agent agent = agentMapper.find(groupOrder.getAgentId());

            vehicleOrderMapper.complete(customerVehicleInfo.getVehicleOrderId(), VehicleOrder.Status.BACK.getValue(), VehicleOrder.Status.RENT.getValue(), new Date(), userName);

            vehicleMapper.clearCustomer(vehicle.getId(), Vehicle.Status.NOT_USE.getValue());

            //库存车辆入库
            ShopStoreVehicle shopStoreVehicle = new ShopStoreVehicle();

            Long priceSettingId = null;
            VehicleVipPrice vehicleVipPrice = vehicleVipPriceMapper.find(customerVehicleInfo.getVipPriceId());
            if (vehicleVipPrice != null) {
                priceSettingId = vehicleVipPrice.getPriceSettingId().longValue();
            } else {
                RentPrice rentPrice = rentPriceMapper.find(customerVehicleInfo.getRentPriceId());
                priceSettingId = rentPrice.getPriceSettingId();
            }

            shopStoreVehicle.setCategory(customerVehicleInfo.getCategory());
            shopStoreVehicle.setAgentId(agent.getId());
            shopStoreVehicle.setAgentName(agent.getAgentName());
            shopStoreVehicle.setAgentCode(agent.getAgentCode());
            shopStoreVehicle.setShopId(shop != null ? shop.getId() : null);
            shopStoreVehicle.setShopName(shop != null ? shop.getShopName() : null);
            shopStoreVehicle.setPriceSettingId(priceSettingId);
            shopStoreVehicle.setVehicleId(vehicle.getId());
            shopStoreVehicle.setVinNo(vehicle.getVinNo());
            shopStoreVehicle.setBatteryCount(0);
            shopStoreVehicle.setCreateTime(new Date());
            shopStoreVehicleMapper.insert(shopStoreVehicle);

            List<String> batteryIdList = new ArrayList<String>();

            if (customerVehicleInfo.getCategory() == Battery.Category.EXCHANGE.getValue()) {

                Set<String> exchangeBatterySet = new HashSet<String>();
                List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(customer.getId());
                for (CustomerExchangeBattery e : exchangeBatteryList) {
                    exchangeBatterySet.add(e.getBatteryId());
                }

                for (CustomerExchangeBattery e : exchangeBatteryList) {
                    customerExchangeBatteryMapper.clearBattery(e.getCustomerId(), e.getBatteryId());
                    if (StringUtils.isNotEmpty(e.getBatteryOrderId())) {
                        batteryOrderMapper.complete(e.getBatteryOrderId(), new Date(), ConstEnum.PayType.BALANCE.getValue(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue());
                    }
                    batteryMapper.clearCustomer(e.getBatteryId(), Battery.Status.NOT_USE.getValue());
                    batteryIdList.add(e.getBatteryId());
                }

            } else if (customerVehicleInfo.getCategory() == Battery.Category.RENT.getValue()) {
                Set<String> rentBatterySet = new HashSet<String>();
                List<CustomerRentBattery> rentBatteryList = customerRentBatteryMapper.findByCustomerId(customer.getId());
                for (CustomerRentBattery e : rentBatteryList) {
                    rentBatterySet.add(e.getBatteryId());
                }

                for (CustomerRentBattery e : rentBatteryList) {
                    customerRentBatteryMapper.clearBattery(e.getCustomerId(), e.getBatteryId());
                    if (StringUtils.isNotEmpty(e.getRentOrderId())) {
                        rentOrderMapper.complete(e.getRentOrderId(), RentOrder.Status.BACK.getValue(), RentOrder.Status.RENT.getValue(), new Date(), userName);
                    }
                    batteryMapper.clearCustomer(e.getBatteryId(), Battery.Status.NOT_USE.getValue());
                    batteryIdList.add(e.getBatteryId());
                }
            }

            if (batteryIdList.size() > 0) {
                for (String e : batteryIdList) {
                    ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
                    shopStoreBattery.setCategory(customerVehicleInfo.getCategory());
                    shopStoreBattery.setAgentId(agent.getId());
                    shopStoreBattery.setAgentName(agent.getAgentName());
                    shopStoreBattery.setAgentCode(agent.getAgentCode());
                    shopStoreBattery.setShopId(shop.getId());
                    shopStoreBattery.setShopName(shop.getShopName());
                    shopStoreBattery.setBatteryId(e);
                    shopStoreBattery.setCreateTime(new Date());
                    shopStoreBatteryMapper.insert(shopStoreBattery);
                }

                for (String e : batteryIdList) {
                    ShopStoreVehicleBattery shopStoreVehicleBattery = new ShopStoreVehicleBattery();
                    shopStoreVehicleBattery.setStoreVehicleId(shopStoreVehicle.getId());
                    shopStoreVehicleBattery.setBatteryId(e);
                    shopStoreVehicleBatteryMapper.insert(shopStoreVehicleBattery);
                }

                int batteryCount = shopStoreVehicleBatteryMapper.findByStoreVehicle(shopStoreVehicle.getId()).size();
                shopStoreVehicleMapper.updateBatteryCount(shopStoreVehicle.getId(), batteryCount);
            }
        }

    }
}
