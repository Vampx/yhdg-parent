package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerMultiOrderService extends AbstractService {

    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;
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
            weixinPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue());
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
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue());
            weixinmpPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        }else if(payType == ConstEnum.PayType.WEIXIN_MA) {
            WeixinmaPayOrder weixinmaPayOrder = new WeixinmaPayOrder();
            weixinmaPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMA_PAY_ORDER));
            weixinmaPayOrder.setPartnerId(customer.getPartnerId());
            weixinmaPayOrder.setAgentId(customer.getAgentId());
            weixinmaPayOrder.setMoney(money);
            weixinmaPayOrder.setCustomerId(customer.getId());
            weixinmaPayOrder.setMobile(customer.getMobile());
            weixinmaPayOrder.setCustomerName(customer.getFullname());
            weixinmaPayOrder.setSourceType(PayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue());
            weixinmaPayOrder.setSourceId(customerMultiPayDetail.getId().toString());
            weixinmaPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmaPayOrder.setMemo(memo);
            weixinmaPayOrder.setCreateTime(new Date());
            weixinmaPayOrderMapper.insert(weixinmaPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmaPayOrder);
        }else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(
                    customer.getPartnerId(),
                    customer.getAgentId(),
                    customerMultiPayDetail.getId().toString(),
                    money,
                    customer.getId(),
                    AlipayPayOrder.SourceType.MULTI_ORDER_CUSTOMER_PAY.getValue(),
                    "换电多通道订单支付",
                    "换电多通道订单支付",
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

            WeixinPayOrder payOrder = new WeixinPayOrder();
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
                multiPayOrderService.foregiftOrderPayOk(payOrder);
            }else if(rentForegiftOrderId != null){
                sourceId = OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue() + ":" + rentForegiftOrderId;
                if (rentPeriodOrderId != null) {
                    sourceId += "," + OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue() + ":" + rentPeriodOrderId;
                }
                if (rentInsuranceOrderId != null) {
                    sourceId += "," + OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue() + ":" + rentInsuranceOrderId;
                }
                payOrder.setSourceId(sourceId);
                multiPayOrderService.rentForegiftOrderPayOk(payOrder);
            }else if(packetPeriodOrderId != null){
                payOrder.setSourceId(packetPeriodOrderId);
                multiPayOrderService.packetPeriodOrderPayOk(payOrder);
            }else if(rentPeriodOrderId != null){
                payOrder.setSourceId(rentPeriodOrderId);
                multiPayOrderService.rentPeriodOrderPayOk(payOrder);
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
}
