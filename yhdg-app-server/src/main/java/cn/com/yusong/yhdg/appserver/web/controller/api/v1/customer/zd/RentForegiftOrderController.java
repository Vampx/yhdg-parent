package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentBatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentInfoService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
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
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/6.
 * <p/>
 * 客户 押金
 */
@Controller("api_v1_customer_zd_rent_foregift_order")
@RequestMapping(value = "/api/v1/customer/zd/rent_foregift_order")
public class RentForegiftOrderController extends ApiController {


    static final Logger log = LogManager.getLogger(RentForegiftOrderController.class);

    @Autowired
    AreaCache areaCache;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    CustomerRentBatteryService customerRenteBatteryService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;
    @Autowired
    PartnerService partnerService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByMultiParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }

    /**
     * APP\客户版\换电柜\5-电池押金支付(多通道)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_multi")
    public RestResult batteryCreateByMulti(@Valid @RequestBody BatteryCreateByMultiParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return rentForegiftOrderService.payByMulti(
                customerId,
                param.batteryId,
                param.foregiftId,
                param.vipForegiftId,
                param.foregiftMoney,
                param.activityId,
                param.rentPeriodPriceId,
                param.vipRentPeriodPriceId,
                param.rentPeriodPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                param.insuranceId,
                param.insurancePrice,
                ConstEnum.PayType.MULTI_CHANNEL);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByWeixinParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }

    /**
     * 客户押金支付（微信）
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_weixin")
    public RestResult batteryCreateByWeixin(@Valid @RequestBody BatteryCreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        RestResult restResult = null;
        try {
            restResult = rentForegiftOrderService.payByThird(
                    customerId,
                    param.batteryId,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.rentPeriodPriceId,
                    param.vipRentPeriodPriceId,
                    param.rentPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
                    param.insuranceId,
                    param.insurancePrice,
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByWeixinMpParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }

    /**
     * 客户押金支付（公众号）
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_weixin_mp")
    public RestResult batteryCreateByWeixinMp(@Valid @RequestBody BatteryCreateByWeixinMpParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = rentForegiftOrderService.payByThird(
                    customerId,
                    param.batteryId,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.rentPeriodPriceId,
                    param.vipRentPeriodPriceId,
                    param.rentPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
                    param.insuranceId,
                    param.insurancePrice,
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


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByAlipayParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }

    /**
     * 客户押金支付（支付宝）
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_alipay.htm")
    public RestResult batteryCreateByAlipay(@Valid @RequestBody BatteryCreateByAlipayParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return rentForegiftOrderService.payByThird(
                customerId,
                param.batteryId,
                param.foregiftId,
                param.vipForegiftId,
                param.foregiftMoney,
                param.activityId,
                param.rentPeriodPriceId,
                param.vipRentPeriodPriceId,
                param.rentPeriodPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                param.insuranceId,
                param.insurancePrice,
                ConstEnum.PayType.ALIPAY);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByAlipayfwParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }

    /**
     * 客户押金支付（支付宝生活号）
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_alipay_fw.htm")
    public RestResult batteryCreateByAlipayFw(@Valid @RequestBody BatteryCreateByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return rentForegiftOrderService.payByThird(
                customerId,
                param.batteryId,
                param.foregiftId,
                param.vipForegiftId,
                param.foregiftMoney,
                param.activityId,
                param.rentPeriodPriceId,
                param.vipRentPeriodPriceId,
                param.rentPeriodPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                param.insuranceId,
                param.insurancePrice,
                ConstEnum.PayType.ALIPAY_FW);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByInstallmentParam {
        public String batteryId;
        public long installmentId;
        public long foregiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long rentPeriodPriceId;
        public int rentPeriodPrice;
        public long insuranceId;
        public int insurancePrice;
    }

    /**
     * 押金支付(分期)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_installment.htm")
    public RestResult batteryCreateByInstallment(@Valid @RequestBody BatteryCreateByInstallmentParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return rentForegiftOrderService.payInstallment(
                customerId,
                param.batteryId,
                param.installmentId,
                param.foregiftId,
                param.foregiftMoney,
                param.rentPeriodPriceId,
                param.rentPeriodPrice,
                param.insuranceId,
                param.insurancePrice
        );
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByBalanceParam {
        public String batteryId;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long rentPeriodPriceId;
        public long vipRentPeriodPriceId;
        public int rentPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int insuranceId;
        public int insurancePrice;
    }


    /**
     * 客户电池押金支付(余额)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_balance.htm")
    public RestResult batteryCreateByBalance(@Valid @RequestBody BatteryCreateByBalanceParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        try {
            return rentForegiftOrderService.payBalance(
                    customerId,
                    param.batteryId,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.rentPeriodPriceId,
                    param.vipRentPeriodPriceId,
                    param.rentPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
                    param.insuranceId,
                    param.insurancePrice
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
