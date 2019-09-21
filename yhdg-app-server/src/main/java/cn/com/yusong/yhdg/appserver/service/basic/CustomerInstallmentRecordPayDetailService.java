package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentInsuranceOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerInstallmentRecordPayDetailService extends AbstractService {
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
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public List<CustomerInstallmentRecordPayDetail> findList(long recordId, long customerId, int category) {
        return customerInstallmentRecordPayDetailMapper.findList(recordId, customerId, category);
    }

    public int findCountByCustomerId(Long customerId, int status, int category, Date newTime) {
        return customerInstallmentRecordPayDetailMapper.findCountByCustomerId(customerId, status, category, newTime);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, long[] id, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        StringBuilder sb = new StringBuilder();
        int payMoney = 0;
        long recordId = 0;
        for (long detailId : id) {
            CustomerInstallmentRecordPayDetail payDetail = customerInstallmentRecordPayDetailMapper.find(detailId);
            if (payDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "分期已支付");
            }
            payMoney = payMoney + payDetail.getMoney();

            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(detailId);

            recordId = payDetail.getRecordId();
        }

        String sourceId = sb.toString();

        String memo = String.format("分期支付金额:%.2f", payMoney / 100.0);

        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(payMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue());
            weixinPayOrder.setSourceId(sourceId);
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrderMapper.insert(weixinPayOrder);
            customerInstallmentRecordPayDetailMapper.updatePayType(recordId, ConstEnum.PayType.WEIXIN.getValue());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), sourceId, payMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue(), "客户押金首付支付费用", "客户押金首付支付费用", memo);
            map.put("orderId", recordId);
            customerInstallmentRecordPayDetailMapper.updatePayType(recordId, ConstEnum.PayType.ALIPAY.getValue());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(customer.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(payMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue());
            weixinmpPayOrder.setSourceId(sourceId);
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            //这里更新支付方式 回调不再更新
            customerInstallmentRecordPayDetailMapper.updatePayType(recordId, ConstEnum.PayType.WEIXIN_MP.getValue());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            //这里更新支付方式 回调不再更新
            customerInstallmentRecordPayDetailMapper.updatePayType(recordId, ConstEnum.PayType.ALIPAY_FW.getValue());
            Map map = super.payByAlipayfw(customer.getPartnerId(), customer.getAgentId(), sourceId, payMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_INSTALLMENT_MONEY_PAY.getValue(), "客户押金首付支付费用", "客户押金首付支付费用", memo);
            map.put("orderId", recordId);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, long[] id) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        List<CustomerInstallmentRecordPayDetail> detailList = new ArrayList<CustomerInstallmentRecordPayDetail>();
        int payMoney = 0;
        for (long detailId : id) {
            CustomerInstallmentRecordPayDetail payDetail = customerInstallmentRecordPayDetailMapper.find(detailId);
            if (payDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "分期已支付");
            }
            payMoney = payMoney + payDetail.getMoney();
            detailList.add(payDetail);
        }

        if ((customer.getBalance() + customer.getGiftBalance()) < payMoney) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        long recordId = 0;
        for (CustomerInstallmentRecordPayDetail payDetail : detailList) {
            recordId = payDetail.getRecordId();

            int balance = customer.getBalance();

            RestResult restResult = updateCustomerBalance(customer, payDetail.getMoney(), new ArrayList<Integer>());
            if (restResult.getCode() != RespCode.CODE_0.getValue()) {
                throw new BalanceNotEnoughException();
            }

            int consumeBalance = 0;
            if (balance >= payDetail.getMoney()) {
                consumeBalance = payDetail.getMoney();
            } else {
                consumeBalance = customer.getBalance();
            }
            balance -= consumeBalance;

            if (payDetail.getMoney() > 0) {
                CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                inOutMoney.setCustomerId(customer.getId());
                inOutMoney.setMoney(-payDetail.getMoney());
                inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_INSTALLMENT_PAY_ORDER.getValue());
                inOutMoney.setBizId(payDetail.getId().toString());
                inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
                inOutMoney.setBalance(balance);
                inOutMoney.setCreateTime(new Date());
                customerInOutMoneyMapper.insert(inOutMoney);
            }

            //更新欠款记录付款状态
            customerInstallmentRecordPayDetailMapper.updateApplyDetail(payDetail.getId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue(),
                    CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), new Date(), payDetail.getMoney(), ConstEnum.PayType.BALANCE.getValue());

            //更新分期记录已支付金额
            customerInstallmentRecordMapper.updatePaidMoney(recordId, CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());

            //换电押金分期
            if (payDetail.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
                CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
                CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerForegiftOrderDetail.getSourceId());
                if (customerForegiftOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
                }
                CustomerInstallmentRecordOrderDetail packetPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(packetPeriodOrderDetail.getSourceId());
                if (packetPeriodOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金订单不存在");
                }
                CustomerInstallmentRecordOrderDetail insuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.INSURANCE_ORDER.getValue());
                if (insuranceOrderDetail != null) {
                    InsuranceOrder insuranceOrder = insuranceOrderMapper.find(insuranceOrderDetail.getSourceId());
                    if (insuranceOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "保险订单不存在");
                    }
                    //更新保险订单已支付金额
                    insuranceOrderMapper.updatePaidMoney(insuranceOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                }

                //更新租金订单已支付金额
                packetPeriodOrderMapper.updatePaidMoney(packetPeriodOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                //更新押金订单已支付金额
                customerForegiftOrderMapper.updatePaidMoney(customerForegiftOrderDetail.getSourceId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
               //更新关联customerRentInfo押金金额
                CustomerForegiftOrder customerForegiftOrder1 = customerForegiftOrderMapper.find(customerForegiftOrderDetail.getSourceId());
                customerExchangeInfoMapper.updateForegift(customerForegiftOrder1.getId(), customerForegiftOrder1.getMoney());
            } else {
                CustomerInstallmentRecordOrderDetail rentForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
                RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(rentForegiftOrderDetail.getSourceId());
                if (rentForegiftOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
                }
                CustomerInstallmentRecordOrderDetail rentPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(rentPeriodOrderDetail.getSourceId());
                if (rentPeriodOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金订单不存在");
                }
                CustomerInstallmentRecordOrderDetail rentInsuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderDetail(payDetail.getRecordId(),
                        OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue());
                if (rentInsuranceOrderDetail != null) {
                    RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderMapper.find(rentInsuranceOrderDetail.getSourceId());
                    if (rentInsuranceOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "保险订单不存在");
                    }
                    //更新保险订单已支付金额
                    rentInsuranceOrderMapper.updatePaidMoney(rentInsuranceOrder.getId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                }

               //更新租金订单已支付金额
                rentPeriodOrderMapper.updatePaidMoney(rentPeriodOrder.getId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                //更新押金订单已支付金额
                rentForegiftOrderMapper.updatePaidMoney(rentForegiftOrder.getId(), payDetail.getRecordId(), CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue());
                //更新关联customerRentInfo押金金额
                RentForegiftOrder rentForegiftOrder1 = rentForegiftOrderMapper.find(rentForegiftOrder.getId());
                customerRentInfoMapper.updateForegift(rentForegiftOrder1.getId(), rentForegiftOrder1.getMoney());
            }
        }

        CustomerInstallmentRecord record = customerInstallmentRecordMapper.find(recordId);
        if (record == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户分期记录不存在");
        }
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

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
