package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import com.alipay.api.AlipayApiException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/5/20.
 */
@Service
public class CustomerDepositOrderService  extends AbstractService {

    private static final Logger log = LogManager.getLogger(CustomerDepositOrderService.class);


    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerDepositGiftMapper customerDepositGiftMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public CustomerDepositOrder find(String id) {
        return customerDepositOrderMapper.find(id);
    }


    public int sumMoney(int status) {
        return customerDepositOrderMapper.sumMoney(status);
    }

    public Page findPage(CustomerDepositOrder customerDepositOrder) {
        Page page = customerDepositOrder.buildPage();
        page.setTotalItems(customerDepositOrderMapper.findPageCount(customerDepositOrder));
        customerDepositOrder.setBeginIndex(page.getOffset());
        List<CustomerDepositOrder> customerDepositOrders = customerDepositOrderMapper.findPageResult(customerDepositOrder);
        for (CustomerDepositOrder cd: customerDepositOrders) {
            if (cd.getPartnerId() != null) {
                Partner partner = findPartner(cd.getPartnerId());
                if (partner != null) {
                    cd.setPartnerName(partner.getPartnerName());
                }
            }
            cd.setRealMoney(cd.getGift() + cd.getMoney());
            if (cd.getCustomerId() != null) {
                if (customerMapper.find(cd.getCustomerId()) != null) {
                    int currentBalance = customerMapper.find(cd.getCustomerId()).getBalance();
                    cd.setCurrentBalance(currentBalance);
                }
            }
        }
        page.setResult(customerDepositOrders);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create (CustomerDepositOrder customerDepositOrder) {
//        Customer customer = customerMapper.find(customerDepositOrder.getCustomerId());
//
///*        List<CustomerDepositGift> customerDepositGiftList = customerDepositGiftMapper.findAll();
//        int gift = CustomerDepositGift.gift(customerDepositGiftList, customerDepositOrder.getMoney());*/
//        int gift = customerDepositOrder.getMoney();
//        customerDepositOrder.setGift(gift);
//        customerDepositOrder.setPartnerId(customer.getPartnerId());
//        customerDepositOrder.setStatus(CustomerDepositOrder.Status.OK.getValue());
//        customerDepositOrder.setCustomerId(customerDepositOrder.getCustomerId());
//        customerDepositOrder.setCustomerMobile(customerDepositOrder.getCustomerMobile());
//        customerDepositOrder.setCreateTime(new Date());
//        customerDepositOrder.setMoney(0);
//        customerDepositOrder.setPayType(ConstEnum.PayType.PLATFORM.getValue());
//        customerDepositOrder.setClientType(ConstEnum.ClientType.WEB.getValue());
//        customerDepositOrder.setCreateTime(new Date());
//
//        CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
//        customerInOutMoney.setMoney(gift);
//        customerInOutMoney.setCustomerId(customerDepositOrder.getCustomerId());
//        customerInOutMoney.setBizId(customerDepositOrder.getId());
//        customerInOutMoney.setCreateTime(new Date());
//        customerInOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_WEB_DEPOSIT.getValue());
//        customerInOutMoneyMapper.insert(customerInOutMoney);
//        int total = customerDepositOrderMapper.insert(customerDepositOrder);
//        if(total == 1) {
//            customerMapper.updateBalance(customerDepositOrder.getCustomerId(), 0,gift);
            return ExtResult.successResult();
//        }else {
//            return ExtResult.failResult("创建失败！");
//        }

    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult confirmRefund(String id, float refundMoney, String photoPath, String refundOperator, String refundReason) throws AlipayApiException {
//        if (refundMoney <= 0) {
//            return ExtResult.failResult("退款金额必须大于0");
//        }
//        if (StringUtils.isEmpty(photoPath)) {
//            return ExtResult.failResult("请上传退款凭证");
//        }
//
//        CustomerDepositOrder customerDepositOrder = customerDepositOrderMapper.find(id);
//        if (customerDepositOrder == null) {
//            return ExtResult.failResult("订单不存在");
//        }
//        if (customerDepositOrder.getStatus() != CustomerDepositOrder.Status.OK.getValue()) {
//            return ExtResult.failResult("只有充值成功才可退款！");
//        }
//
//        if (refundMoney > customerDepositOrder.getMoney()) {
//            return ExtResult.failResult("退款金额不能大于充值金额");
//        }
//
//
//        Long customerId = customerDepositOrder.getCustomerId();
//        int money = Math.round(refundMoney * 100); //真实退款金额
//
//        Customer customer = customerMapper.find(customerId);
//
//        if (money > customer.getBalance()) {
//            return ExtResult.failResult("余额不足");
//        }
//
//        int giftMoney = customerDepositOrder.getGift();
//        if (giftMoney > customer.getGiftBalance()) {
//            giftMoney = customer.getGiftBalance();
//        }
//
//        Partner partner = partnerMapper.find(customerDepositOrder.getPartnerId());
//
//        if (customerDepositOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
//            AlipayPayOrder alipayPayOrder = alipayPayOrderMapper.findBySourceId(customerDepositOrder.getId());
//            if (alipayPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "订单不存在");
//            }
//            String appId = partner.getAlipayAppId();
//            String alipayPublic = partner.getAlipayAliKey();
//            String alipayAppRsaPrivate = partner.getAlipayPriKey();
//
//            double refundAmount = ((double) money) / 100;
//            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayAppRsaPrivate, "json", "GBK", alipayPublic, "RSA2");
//            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//            request.setBizContent("{" +
//                    "\"out_trade_no\":\"" + alipayPayOrder.getId() + "\"," +
//                    "\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
//                    "\"refund_amount\":" + refundAmount + "," +
//                    "\"refund_reason\":\"客户充值退款\"" +
//                    "  }");
//            try {
//                AlipayTradeRefundResponse response = alipayClient.execute(request);
//                if (response != null && response.isSuccess()) {
//                    alipayPayOrderMapper.refundOk(alipayPayOrder.getId(), customerDepositOrder.getRefundMoney(), new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//                }else{
//                    log.error(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg());
//                }
//            } catch (Exception e) {
//                log.error(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + e.getMessage());
//            }
//        } else if (customerDepositOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
//            WeixinPayOrder weixinPayOrder = weixinPayOrderMapper.findBySourceId(customerDepositOrder.getId());
//            if (weixinPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "订单不存在");
//            }
//
//            String appId = partner.getWeixinAppId();
//            String mchId = partner.getWeixinPartnerCode();
//            String partnerKey = partner.getWeixinPartnerKey();
//            String outTradeNo = weixinPayOrder.getId();
//            int totalFee = weixinPayOrder.getMoney();
//            int refundFee = money;
//
//            try {
//
//                File certFile = AppUtils.getWeixinCertFile(appConfig.appDir, weixinPayOrder.getPartnerId());
//                if(!certFile.exists()) {
//                    return ExtResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "证书不存在");
//                }
//
//                WxMpPrepayIdResult result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, "客户充值退款", certFile);
//                if (result != null && result.getReturn_code().equals("SUCCESS")) {
//                    weixinPayOrderMapper.refundOk(weixinPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//                }else{
//                    log.error(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + result.getReturn_code()  + result.getErr_code_des());
//                }
//            } catch (Exception e) {
//                log.error("Weixin退款失败", e);
//                log.error(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + e.getMessage());
//            }
//
//        } else if (customerDepositOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
//            WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderMapper.findBySourceId(customerDepositOrder.getId());
//            if (weixinmpPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "订单不存在");
//            }
//
//
//            String appId = partner.getMpAppId();
//            String mchId = partner.getMpPartnerCode();
//            String partnerKey = partner.getWeixinPartnerKey();
//
//            String outTradeNo = weixinmpPayOrder.getId();
//            int totalFee = weixinmpPayOrder.getMoney();
//            int refundFee = money;
//
//            try {
//
//                File certFile = AppUtils.getWeixinmpCertFile(appConfig.appDir, weixinmpPayOrder.getPartnerId());
//                if(!certFile.exists()) {
//                    return ExtResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "证书不存在");
//                }
//
//                WxMpPrepayIdResult result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, "客户充值退款", certFile);
//                if (result != null && result.getReturn_code().equals("SUCCESS")) {
//                    weixinmpPayOrderMapper.refundOk(weixinmpPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//                }else{
//                    log.error(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + result.getReturn_code()  + result.getErr_code_des());
//                }
//            } catch (Exception e) {
//                log.error("WeixinMp退款失败", e);
//                log.error(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + e.getMessage());
//            }
//
//        } else if (customerDepositOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
//            AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderMapper.findBySourceId(customerDepositOrder.getId());
//            if (alipayfwPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "订单不存在");
//            }
//            String appId = partner.getFwAppId();
//            String alipayfwPublic = partner.getFwAliKey();
//            String alipayfwAppRsaPrivate = partner.getFwPriKey();
//
//            double refundAmount = ((double) money) / 100;
//            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayfwAppRsaPrivate, "json", "GBK", alipayfwPublic, "RSA2");
//            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//            request.setBizContent("{" +
//                    "\"out_trade_no\":\"" + alipayfwPayOrder.getId() + "\"," +
//                    "\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
//                    "\"refund_amount\":" + refundAmount + "," +
//                    "\"refund_reason\":\"客户充值退款\"" +
//                    "  }");
//            try {
//                AlipayTradeRefundResponse response = alipayClient.execute(request);
//                if (response != null && response.isSuccess()) {
//                    alipayPayOrderMapper.refundOk(alipayfwPayOrder.getId(), customerDepositOrder.getRefundMoney(), new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//                }else{
//                    log.error(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg());
//                }
//
//            } catch (Exception e) {
//                log.error(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + e.getMessage());
//            }
//
//        }
//        int total = 0;
//        if (customerAgentBalanceMapper.findByCustomerId(customerId) != null) {
//            total += customerAgentBalanceMapper.updateBalance(customerId, 0);
//        }
//        if(customerMapper.updateBalance(customerId, -(money), -giftMoney) == 1 && total == 1) {
//
//            CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
//            customerInOutMoney.setCustomerId(customerId);
//            customerInOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_DEPOSIT_REFUND.getValue());
//            customerInOutMoney.setBizId(customerDepositOrder.getId());
//            customerInOutMoney.setMoney(money);
//            customerInOutMoney.setCreateTime(new Date());
//            customerInOutMoneyMapper.insert(customerInOutMoney);
//
//            //更新充值订单退款信息
//            customerDepositOrder.setRefundTime(new Date());
//            customerDepositOrder.setRefundMoney(money);
//            customerDepositOrder.setRefundPhoto(photoPath);
//            customerDepositOrder.setRefundOperator(refundOperator);
//            customerDepositOrder.setRefundReason(refundReason);
//            customerDepositOrder.setStatus(CustomerDepositOrder.Status.REFUND.getValue());
//            customerDepositOrderMapper.refund(customerDepositOrder);
//
//            return ExtResult.successResult();
//        }

        return ExtResult.failResult("余额不足");
    }

}
