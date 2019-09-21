package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
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

@Controller("api_v1_customer_basic_customer_installment_record")
@RequestMapping(value = "/api/v1/customer/basic/customer_installment_record")
public class CustomerInstallmentRecordController extends ApiController {


    static final Logger log = LogManager.getLogger(CustomerInstallmentRecord.class);

    @Autowired
    AreaCache areaCache;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerInstallmentRecordService customerInstallmentRecordService;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinParam {
        public long id;
        public int agentId;
        public String batteryId;
    }

     /**
     * 缴纳分期付首付(微信公众号)
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/first_money_by_weixin_mp")
    public RestResult batteryCreateByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = customerInstallmentRecordService.payByThird(
                    customerId,
                    param.batteryId,
                    param.id,
                    param.agentId,
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
        orderRequest.setBody("押金支付");
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
     * 缴纳分期付首付(微信)
     */

    @ResponseBody
    @RequestMapping("/first_money_by_weixin")
    public RestResult batteryCreateByWeixin(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = customerInstallmentRecordService.payByThird(
                    customerId,
                    param.batteryId,
                    param.id,
                    param.agentId,
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
        orderRequest.setBody("押金支付");
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


    /**
     * 缴纳分期付首付（支付宝生活号）
     */
    @ResponseBody
    @RequestMapping("/first_money_by_alipay_fw.htm")
    public RestResult batteryCreateByAlipayFw(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerInstallmentRecordService.payByThird(
                customerId,
                param.batteryId,
                param.id,
                param.agentId,
                ConstEnum.PayType.ALIPAY_FW);
    }

    /**
     * 缴纳分期付首付（支付宝）
     */
    @ResponseBody
    @RequestMapping("/first_money_by_alipay.htm")
    public RestResult batteryCreateByAlipay(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerInstallmentRecordService.payByThird(
                customerId,
                param.batteryId,
                param.id,
                param.agentId,
                ConstEnum.PayType.ALIPAY);
    }

    /**
     * 缴纳分期付首付(余额)
     */
    @ResponseBody
    @RequestMapping("/first_money_by_balance.htm")
    public RestResult batteryCreateByBalance(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        try {
            return customerInstallmentRecordService.payBalance(
                    customerId,
                    param.batteryId,
                    param.agentId,
                    param.id);

        } catch (BalanceNotEnoughException e) {
            log.error("batteryCreateByBalance occur error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
    }
}
