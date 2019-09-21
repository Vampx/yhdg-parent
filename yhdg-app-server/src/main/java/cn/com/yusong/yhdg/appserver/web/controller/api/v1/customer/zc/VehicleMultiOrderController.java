package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleMultiOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * APP\客户版\租车\20-多通道付款
 */
@Controller("api_v1_customer_zc_vehicle_multi_order")
@RequestMapping(value = "/api/v1/customer/zc/vehicle_multi_order")
public class VehicleMultiOrderController extends ApiController {

    static final Logger log = LogManager.getLogger(VehicleMultiOrderController.class);

    @Autowired
    AreaCache areaCache;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;
    @Autowired
    VehicleMultiOrderService vehicleMultiOrderService;
    @Autowired
    CustomerMultiOrderDetailService customerMultiOrderDetailService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    GroupOrderService groupOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByWeixinMpParam {
        public long id;
        public int money;
    }

    /**
     * APP\客户版\租车\20-多通道付款(微信公众号)
     */
    @ResponseBody
    @RequestMapping("/pay_by_weixin_mp.htm")
    public RestResult payByWeixinMp(@Valid @RequestBody PayByWeixinMpParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = vehicleMultiOrderService.payByThird(
                    customer,
                    param.id,
                    param.money,
                    ConstEnum.PayType.WEIXIN_MP);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }

        String openId = customer.getMpOpenId();


        WeixinmpPayOrder weixinmpPayOrder = (WeixinmpPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("多通道支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(openId);
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(weixinmpPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinmpPayOrder.getPartnerId()));
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
                data.put("payAppId", customer.getPartnerId());
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

    /**
     * APP\客户版\租车\20-多通道付款(微信)
     */
    @ResponseBody
    @RequestMapping("/pay_by_weixin.htm")
    public RestResult payByWeixin(@Valid @RequestBody PayByWeixinMpParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = vehicleMultiOrderService.payByThird(
                    customer,
                    param.id,
                    param.money,
                    ConstEnum.PayType.WEIXIN);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }

        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("多通道支付");
        orderRequest.setOutTradeNo(weixinPayOrder.getId());
        orderRequest.setTotalFee( weixinPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setNotifyUrl(config.getStaticUrl() + Constant.WEIXIN_PAY_OK);
        orderRequest.setTradeType(WxPayConstants.TradeType.APP);


        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.get(weixinPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinPayOrder.getPartnerId()));
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

                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("appid", wxPayService.getConfig().getAppId());
                stringMap.put("noncestr", nonceStr);
                stringMap.put("package", "Sign=WXPay");
                stringMap.put("prepayid", result.getPrepayId());
                stringMap.put("partnerid", wxPayService.getConfig().getMchId());
                stringMap.put("timestamp", timeStamp);
                String sign = AppUtils.sign(stringMap, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();
                stringMap.put("sign", sign);

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, stringMap);

            } else {
                log.warn("return_code error: {}", result.toString());
                return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败");
            }

        } catch (Exception e) {
            log.error("统一下单失败", e);
            return RestResult.result(RespCode.CODE_1.getValue(), "统一下单失败");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByAlipayFw {
        public long id;
        public int money;
    }


    /**
     * APP\客户版\租车\20-多通道付款(支付宝当面付)
     */
    @ResponseBody
    @RequestMapping("/pay_by_alipay_fw.htm")
    public RestResult payByAlipayFw(@Valid @RequestBody PayByAlipayFw param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        return vehicleMultiOrderService.payByThird(
                customer,
                param.id,
                param.money,
                ConstEnum.PayType.ALIPAY_FW);
    }

    /**
     * APP\客户版\租车\20-多通道付款(支付宝)
     */
    @ResponseBody
    @RequestMapping("/pay_by_alipay.htm")
    public RestResult payByAlipay(@Valid @RequestBody PayByAlipayFw param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        return vehicleMultiOrderService.payByThird(
                customer,
                param.id,
                param.money,
                ConstEnum.PayType.ALIPAY);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByBalance {
        public long id;
        public int money;
    }


    /**
     * APP\客户版\租车\20-多通道付款(余额)
     */
    @ResponseBody
    @RequestMapping("/pay_by_balance.htm")
    public RestResult payByBalance(@Valid @RequestBody PayByBalance param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        try {
            return vehicleMultiOrderService.payBalance(
                    customer,
                    param.id,
                    param.money
                    );

        } catch (BalanceNotEnoughException e) {
            log.error("batteryCreateByBalance occur error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        } catch (CouponTicketNotAvailableException e) {
            log.error("batteryCreateByBalance occur error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已被使用或已过期");
        }
    }

}
