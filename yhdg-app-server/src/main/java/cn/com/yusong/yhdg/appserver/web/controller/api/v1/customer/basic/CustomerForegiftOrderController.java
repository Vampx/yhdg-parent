package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
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
import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/6.
 * <p/>
 * 客户 押金
 */
@Controller("api_v1_customer_basic_customer_foregift_order")
@RequestMapping(value = "/api/v1/customer/basic/customer_foregift_order")
public class CustomerForegiftOrderController extends ApiController {


    static final Logger log = LogManager.getLogger(CustomerForegiftOrderController.class);

    @Autowired
    AreaCache areaCache;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
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
    public static class BatteryCreateByMultiParam {
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long vipForegiftId;
        public long foregiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long packetPeriodPriceId;
        public long vipPacketPeriodPriceId;
        public int packetPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int type;
    }

    /**
     * APP\客户版\换电柜\5-电池押金支付(多通道)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_multi")
    public RestResult batteryCreateByMulti(@Valid @RequestBody BatteryCreateByMultiParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerForegiftOrderService.payByMulti(
                    customerId,
                    param.agentId,
                    param.stationId,
                    param.cabinetId,
                    param.batteryType,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.packetPeriodPriceId,
                    param.vipPacketPeriodPriceId,
                    param.packetPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
                    ConstEnum.PayType.MULTI_CHANNEL);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByWeixinParam {
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long vipForegiftId;
        public long foregiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long packetPeriodPriceId;
        public long vipPacketPeriodPriceId;
        public int packetPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
        public int type;
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

        RestResult restResult = null;
        try {
            restResult = customerForegiftOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.stationId,
                    param.cabinetId,
                    param.batteryType,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.packetPeriodPriceId,
                    param.vipPacketPeriodPriceId,
                    param.packetPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
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
     * 客户押金支付（公众号）
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_weixin_mp")
    public RestResult batteryCreateByWeixinMp(@Valid @RequestBody BatteryCreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = customerForegiftOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.stationId,
                    param.cabinetId,
                    param.batteryType,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.packetPeriodPriceId,
                    param.vipPacketPeriodPriceId,
                    param.packetPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
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
     * 客户押金支付（小程序）
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_weixin_ma")
    public RestResult batteryCreateByWeixinMa(@Valid @RequestBody BatteryCreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = customerForegiftOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.stationId,
                    param.cabinetId,
                    param.batteryType,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.packetPeriodPriceId,
                    param.vipPacketPeriodPriceId,
                    param.packetPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId,
                    ConstEnum.PayType.WEIXIN_MA);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }

        String openId = customer.getMaOpenId();

        WeixinmaPayOrder weixinmaPayOrder = (WeixinmaPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("押金支付");
        orderRequest.setOutTradeNo(weixinmaPayOrder.getId());
        orderRequest.setTotalFee( weixinmaPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(openId);
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMa(weixinmaPayOrder.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMaService is null (partnerId=%d)", weixinmaPayOrder.getPartnerId()));
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
            log.error("unifiedOrder 统一下单失败, {}", weixinmaPayOrder.getId());
            log.error("unifiedOrder error", e);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByAlipayParam {
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long packetPeriodPriceId;
        public long vipPacketPeriodPriceId;
        public int packetPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
    }

    /**
     * 客户押金支付（支付宝）
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_alipay.htm")
    public RestResult batteryCreateByAlipay(@Valid @RequestBody BatteryCreateByAlipayParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerForegiftOrderService.payByThird(
                customerId,
                param.agentId,
                param.stationId,
                param.cabinetId,
                param.batteryType,
                param.foregiftId,
                param.vipForegiftId,
                param.foregiftMoney,
                param.activityId,
                param.packetPeriodPriceId,
                param.vipPacketPeriodPriceId,
                param.packetPeriodPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                ConstEnum.PayType.ALIPAY);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByAlipayfwParam {
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long foregiftId;
        public long vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long packetPeriodPriceId;
        public long vipPacketPeriodPriceId;
        public int packetPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
    }

    /**
     * 客户押金支付（支付宝生活号）
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_alipay_fw.htm")
    public RestResult batteryCreateByAlipayFw(@Valid @RequestBody BatteryCreateByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerForegiftOrderService.payByThird(
                customerId,
                param.agentId,
                param.stationId,
                param.cabinetId,
                param.batteryType,
                param.foregiftId,
                param.vipForegiftId,
                param.foregiftMoney,
                param.activityId,
                param.packetPeriodPriceId,
                param.vipPacketPeriodPriceId,
                param.packetPeriodPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                ConstEnum.PayType.ALIPAY_FW);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByBalanceParam {
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long foregiftId;
        public int vipForegiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long activityId;
        public long packetPeriodPriceId;
        public long vipPacketPeriodPriceId;
        public int packetPeriodPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
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
            return customerForegiftOrderService.payBalance(
                    customerId,
                    param.agentId,
                    param.stationId,
                    param.cabinetId,
                    param.batteryType,
                    param.foregiftId,
                    param.vipForegiftId,
                    param.foregiftMoney,
                    param.activityId,
                    param.packetPeriodPriceId,
                    param.vipPacketPeriodPriceId,
                    param.packetPeriodPrice,
                    param.foregiftTicketId,
                    param.packetTicketId,
                    param.deductionTicketId
                    );

        } catch (BalanceNotEnoughException e) {
            log.error("batteryCreateByBalance occur error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        } catch (CouponTicketNotAvailableException e) {
            log.error("batteryCreateByBalance occur error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已被使用或已过期");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByInstallmentParam {
        public long installmentId;
        public int settingType;
        public int agentId;
        public String stationId;
        public String cabinetId;
        public int batteryType;
        public long foregiftId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        public long packetPeriodPriceId;
        public int packetPeriodPrice;
        public long deductionTicketId;
        public long foregiftTicketId;
        public long rentPeriodTicketId;
        public int deductionTicketMoney;
        public int foregiftTicketMoney;
        public int rentPeriodTicketMoney;
        public int countId;
        public ExchangeInstallmentCountDetail[] detailList;
        public static class ExchangeInstallmentCountDetail {
            public Integer id;
            public Integer num;
            public Integer foregiftMoney;
            public Integer packetPeriodMoney;
        }
    }

    /**
     * 押金支付(分期)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_installment.htm")
    public RestResult batteryCreateByInstallment(@Valid @RequestBody BatteryCreateByInstallmentParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return customerForegiftOrderService.payInstallment(
                customerId,
                param.installmentId,
                param.settingType,
                param.agentId,
                param.stationId,
                param.cabinetId,
                param.batteryType,
                param.foregiftId,
                param.foregiftMoney,
                param.packetPeriodPriceId,
                param.packetPeriodPrice,
                param.deductionTicketId,
                param.foregiftTicketId,
                param.rentPeriodTicketId,
                param.deductionTicketMoney,
                param.foregiftTicketMoney,
                param.rentPeriodTicketMoney,
                param.countId,
                param.detailList

        );
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundOrderParam {
        public String reason;
    }

    /**
     * 申请退还电池押金
     */
    @ResponseBody
    @RequestMapping("/apply_refund.htm")
    public RestResult refundOrder(@Valid @RequestBody RefundOrderParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if (customerExchangeBatteryService.exists(customer.getId()) > 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "此客户已租电池，不能申请退还电池押金");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
        if (customerExchangeInfo == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户没有电池押金，不能申请退还电池押金");
        }

        if (customerExchangeInfo != null && customerExchangeInfo.getVehicleForegiftFlag() != null && customerExchangeInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户已交纳租车押金，不能申请退还电池押金");
        }

        CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
        if (order != null) {
            if (order.getStatus() != CustomerForegiftOrder.Status.PAY_OK.getValue()) {
                return RestResult.result(RespCode.CODE_1.getValue(), "该客户押金订单状态为" + order.getStatusName() + "，操作失败");
            }
        }

        if (customerForegiftOrderService.updateRefund(customerExchangeInfo.getForegiftOrderId(),
                new Date(),
                param.reason,
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue()) == 0) {
            return RestResult.dataResult(RespCode.CODE_1.getValue(), "申请失败", null);
        } else {
            //提示退款成功
            Map map = new HashMap();
            map.put("text",String.format("您押金%s退款申请成功，请等待系统审核",String.format("%.2f", 1.0d * order.getMoney() / 100.0)));
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "退款申请成功", map);
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancleRefundOrderParam {
        public int type;
    }

    /**
     * cancle apply 取消申请退还电池押金
     * cancel_apply_refund
     */
    @ResponseBody
    @RequestMapping("/cancel_apply_refund.htm")
    public RestResult cancleRefundOrder(@Valid @RequestBody CancleRefundOrderParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户不存在");
        }
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find( customerId);
        if (customerExchangeInfo == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户没有电池押金，不能取消申请退还电池押金");
        }

        CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
        if (order != null) {
            if (order.getStatus() != CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                return RestResult.result(RespCode.CODE_1.getValue(), "该客户押金订单状态不对，不能取消申请押金退还");
            }
        }

        Map map = new HashMap();
        if (param.type == 1) {
            if (customerForegiftOrderService.updateRefund(customerExchangeInfo.getForegiftOrderId(),
                    null
                    ,
                    null,
                    CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) == 0) {
                return RestResult.dataResult(RespCode.CODE_1.getValue(), "取消失败", null);
            } else {
                map.put("text", "取消电池押金退还成功");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "取消成功", map);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryStatusParam {
        public int agentId;
    }

    /**
     * 39查询电池押金明细 status.htm
     */
    @ResponseBody
    @RequestMapping("/battery_status.htm")
    public RestResult batteryStatus(@Valid @RequestBody BatteryStatusParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户不存在");
        }
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
        if (customerExchangeInfo == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户没有电池押金");
        }

        CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
        if (order == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户电池押金订单不存在");
        }

        Map map = new HashMap();
        map.put("status", order.getStatus());
        map.put("price", order.getMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }

}
