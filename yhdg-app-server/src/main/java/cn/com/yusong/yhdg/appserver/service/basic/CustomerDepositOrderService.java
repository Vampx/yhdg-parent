package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.alipay.api.AlipayClient;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CustomerDepositOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(CustomerDepositOrderService.class);

    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerDepositOrderMapper customerDepositOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;
    @Autowired
    CustomerDepositGiftMapper customerDepositGiftMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public RestResult findList(Long customerId, int offset, int limit) {
        List<Map> list = new ArrayList<Map>(limit);
        List<CustomerDepositOrder> customerDepositOrders = customerDepositOrderMapper.findList(customerId, offset, limit);
        for (CustomerDepositOrder customerDepositOrder : customerDepositOrders) {
            Map customerMap = new HashMap();
            customerMap.put("id", customerDepositOrder.getId());
            customerMap.put("status", customerDepositOrder.getStatus());
            customerMap.put("payType", customerDepositOrder.getPayType());
            customerMap.put("money", customerDepositOrder.getMoney());
            customerMap.put("createTime", DateFormatUtils.format(customerDepositOrder.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(customerMap);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public int insert(CustomerDepositOrder customerDepositOrder) {
        return customerDepositOrderMapper.insert(customerDepositOrder);
    }

    public RestResult payByAlipay(Integer money, long customerId, boolean test) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        customerDepositOrder.setPartnerId(customer.getPartnerId());
        customerDepositOrder.setMoney(money);
        customerDepositOrder.setGift(0);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile(customer.getMobile());
        customerDepositOrder.setCustomerFullname(customer.getFullname());
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.APP.getValue());
        customerDepositOrderMapper.insert(customerDepositOrder);

        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else {

            Map map = super.payByAlipay(customer.getPartnerId(), customerDepositOrder.getId(), money, customerId, AlipayPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue(), "充值订单支付", "充值订单支付");
            map.put("orderId", customerDepositOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
    }

    public RestResult payByAlipayfw(Integer money, long customerId, boolean test) throws IOException {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        customerDepositOrder.setPartnerId(customer.getPartnerId());
        customerDepositOrder.setMoney(money);
        customerDepositOrder.setGift(0);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile(customer.getMobile());
        customerDepositOrder.setCustomerFullname(customer.getFullname());
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.FW.getValue());
        customerDepositOrderMapper.insert(customerDepositOrder);

        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else {
            Map map = super.payByAlipayfw(customer.getPartnerId(),
                    null,
                    customerDepositOrder.getId(),
                    money, customerId,
                    AlipayPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue(),
                    "充值订单支付",
                    "充值订单支付");
            map.put("orderId", customerDepositOrder.getId());
            if (log.isDebugEnabled()) {
                log.debug("payByAlipayfw: {}", AppUtils.encodeJson(map));
            }

            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
    }

    public RestResult payByWeixin(int partnerId, String orderId, Integer money, long customerId) {
        WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
        weixinPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
        weixinPayOrder.setPartnerId(partnerId);
        weixinPayOrder.setCustomerId(customerId);
        weixinPayOrder.setMoney(money);
        weixinPayOrder.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        weixinPayOrder.setSourceId(orderId);
        weixinPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinPayOrder.setCreateTime(new Date());
        weixinPayOrderMapper.insert(weixinPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("充值订单支付");
        orderRequest.setOutTradeNo(weixinPayOrder.getId());
        orderRequest.setTotalFee( weixinPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setNotifyUrl(config.getStaticUrl() + Constant.WEIXIN_PAY_OK);
        orderRequest.setTradeType(WxPayConstants.TradeType.APP);

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.get(partnerId);
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxService is null (partnerId=%d)", partnerId));
            }
            log.debug("getWxConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult  result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);

                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("appid", wxPayService.getConfig().getAppId());
                stringMap.put("noncestr", nonceStr);
                stringMap.put("package", "Sign=WXPay");
                stringMap.put("prepayid", result.getPrepayId());
                stringMap.put("partnerid", wxPayService.getConfig().getMchId());
                stringMap.put("timestamp", timeStamp);
                String sign = AppUtils.sign(stringMap, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();
                stringMap.put("sign", sign);
                stringMap.put("orderId", orderId);

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, stringMap);

            } else {
                return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败:"+result + result.getErrCodeDes());
            }

        } catch (Exception e) {
            return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败: " + e);
        }
    }

    public RestResult payByWeixinMp(boolean test, int partnerId, String orderId, Integer money, long customerId, String openid) {
        if (StringUtils.isEmpty(openid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败 openid是空");
        }

        Customer customer = customerMapper.find(customerId);

        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
        weixinmpPayOrder.setPartnerId(partnerId);
        weixinmpPayOrder.setAgentId(null);
        weixinmpPayOrder.setCustomerId(customerId);
        weixinmpPayOrder.setMobile(customer.getMobile());
        weixinmpPayOrder.setCustomerName(customer.getFullname());
        weixinmpPayOrder.setMoney(money);
        weixinmpPayOrder.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        weixinmpPayOrder.setSourceId(orderId);
        weixinmpPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrderMapper.insert(weixinmpPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("充值订单支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(openid);
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(partnerId);
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", partnerId));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMpPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);
                String signType = "MD5";

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("appId", wxPayService.getConfig().getAppId());
                map1.put("timeStamp", timeStamp);
                map1.put("nonceStr", nonceStr);
                map1.put("package", "prepay_id=" + result.getPrepayId());
                map1.put("signType", signType);
                String paySign = AppUtils.sign(map1, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();

                Map data = new HashMap<String, String>();
                data.put("appId", wxPayService.getConfig().getAppId());
                data.put("payAppId", partnerId);
                data.put("prepayId", result.getPrepayId());
                data.put("package", "prepay_id=" + result.getPrepayId());
                data.put("timeStamp", timeStamp);
                data.put("nonceStr", nonceStr);
                data.put("signType", signType);
                data.put("paySign", paySign);

                if (log.isDebugEnabled()) {
                    log.debug("data: {}", data);
                }

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmpPayOrder.getId());
            log.error("unifiedOrder error", e);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }

    public RestResult payByWeixinMa(boolean test, int partnerId, String orderId, Integer money, long customerId, String openid) {
        if (StringUtils.isEmpty(openid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败 openid是空");
        }

        Customer customer = customerMapper.find(customerId);

        WeixinmaPayOrder weixinmaPayOrder = new WeixinmaPayOrder();
        weixinmaPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMA_PAY_ORDER));
        weixinmaPayOrder.setPartnerId(partnerId);
        weixinmaPayOrder.setAgentId(null);
        weixinmaPayOrder.setCustomerId(customerId);
        weixinmaPayOrder.setMobile(customer.getMobile());
        weixinmaPayOrder.setCustomerName(customer.getFullname());
        weixinmaPayOrder.setMoney(money);
        weixinmaPayOrder.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        weixinmaPayOrder.setSourceId(orderId);
        weixinmaPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinmaPayOrder.setCreateTime(new Date());
        weixinmaPayOrderMapper.insert(weixinmaPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("充值订单支付");
        orderRequest.setOutTradeNo(weixinmaPayOrder.getId());
        orderRequest.setTotalFee( weixinmaPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(openid);
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMa(partnerId);
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMaService is null (partnerId=%d)", partnerId));
            }
            log.debug("getWxMaConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMaPrepayIdResult: {}", string);
            }

            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                long now = System.currentTimeMillis();
                String timeStamp = String.format("%d", now / 1000);
                String nonceStr = String.format("%d", now);
                String signType = "MD5";

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("appId", wxPayService.getConfig().getAppId());
                map1.put("timeStamp", timeStamp);
                map1.put("nonceStr", nonceStr);
                map1.put("package", "prepay_id=" + result.getPrepayId());
                map1.put("signType", signType);
                String paySign = AppUtils.sign(map1, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();

                Map data = new HashMap<String, String>();
                data.put("appId", wxPayService.getConfig().getAppId());
                data.put("payAppId", partnerId);
                data.put("prepayId", result.getPrepayId());
                data.put("package", "prepay_id=" + result.getPrepayId());
                data.put("timeStamp", timeStamp);
                data.put("nonceStr", nonceStr);
                data.put("signType", signType);
                data.put("paySign", paySign);

                if (log.isDebugEnabled()) {
                    log.debug("data: {}", data);
                }

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmaPayOrder.getId());
            log.error("unifiedOrder error", e);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }
}
