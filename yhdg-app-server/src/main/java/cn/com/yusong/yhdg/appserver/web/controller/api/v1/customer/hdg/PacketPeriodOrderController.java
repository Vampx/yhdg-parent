package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
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
import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Controller("api_v1_customer_hdg_packet_period_order")
@RequestMapping(value = "/api/v1/customer/hdg/packet_period_order")
public class PacketPeriodOrderController extends ApiController {
    static final Logger log = LogManager.getLogger(PacketPeriodOrderController.class);

    @Autowired
    AppConfig appConfig;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentService agentService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    WxPayServiceHolder wxPayServiceHolder;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryCreateByMultiParam {
        public int agentId;
        public String cabinetId;
        public int batteryType;
        public long activityId;
        public long vipId;
        public long priceId;
        public int price;
        public long couponTicketId;
    }

    /**
     * APP\客户版\换电柜\5-电池押金支付(多通道)
     */
    @ResponseBody
    @RequestMapping("/battery_create_by_multi")
    public RestResult batteryCreateByMulti(@Valid @RequestBody BatteryCreateByMultiParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return packetPeriodOrderService.payByMulti(
                customerId,
                param.agentId,
                param.cabinetId,
                param.batteryType,
                param.activityId,
                param.vipId,
                param.priceId,
                param.price,
                param.couponTicketId,
                ConstEnum.PayType.MULTI_CHANNEL);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinParam {
        public int agentId;
        public String cabinetId;
        public int batteryType;
        public long activityId;
        public long vipId;
        public long priceId;
        public int price;
        public long couponTicketId;
    }


    @ResponseBody
    @RequestMapping("/create_by_weixin")
    public RestResult createByWeixin(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        RestResult restResult = null;
        try {
            restResult = packetPeriodOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.cabinetId,
                    param.batteryType,
                    param.activityId,
                    param.vipId,
                    param.priceId,
                    param.price,
                    param.couponTicketId,
                    ConstEnum.PayType.WEIXIN);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            log.error("packetPeriodOrderService.payByThird error", e);
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }
        WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("套餐购买支付");
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

    @ResponseBody
    @RequestMapping("/create_by_weixin_mp")
    public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = packetPeriodOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.cabinetId,
                    param.batteryType,
                    param.activityId,
                    param.vipId,
                    param.priceId,
                    param.price,
                    param.couponTicketId,
                    ConstEnum.PayType.WEIXIN_MP);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            log.error("packetPeriodOrderService.payByThird error", e);
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }
        WeixinmpPayOrder weixinmpPayOrder = (WeixinmpPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("套餐购买支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(customer.getMpOpenId());
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

    @ResponseBody
    @RequestMapping("/create_by_weixin_ma")
    public RestResult createByWeixinMa(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = packetPeriodOrderService.payByThird(
                    customerId,
                    param.agentId,
                    param.cabinetId,
                    param.batteryType,
                    param.activityId,
                    param.vipId,
                    param.priceId,
                    param.price,
                    param.couponTicketId,
                    ConstEnum.PayType.WEIXIN_MA);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (IllegalArgumentException e) {
            log.error("packetPeriodOrderService.payByThird error", e);
            e.printStackTrace();
            return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
        }
        WeixinmaPayOrder weixinmaPayOrder = (WeixinmaPayOrder) restResult.getData();

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("套餐购买支付");
        orderRequest.setOutTradeNo(weixinmaPayOrder.getId());
        orderRequest.setTotalFee( weixinmaPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(customer.getMaOpenId());
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
    public static class createByAlipayParam {
        public int agentId;
        public String cabinetId;
        public int batteryType;
        public long activityId;
        public long vipId;
        public long priceId;
        public int price;
        public long couponTicketId;
    }

    /**
     * 客户购买包时段套餐(支付宝)
     */
    @ResponseBody
    @RequestMapping("/create_by_alipay.htm")
    public RestResult createByAlipay(@Valid @RequestBody createByAlipayParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return packetPeriodOrderService.payByThird(
                customerId,
                param.agentId,
                param.cabinetId,
                param.batteryType,
                param.activityId,
                param.vipId,
                param.priceId,
                param.price,
                param.couponTicketId,
                ConstEnum.PayType.ALIPAY);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class createByAlipayfwParam {
        public int agentId;
        public String cabinetId;
        public int batteryType;
        public long activityId;
        public long vipId;
        public long priceId;
        public int price;
        public long couponTicketId;
        
        
    }

    /**
     * 客户购买包时段套餐(支付宝 生活号)
     */
    @ResponseBody
    @RequestMapping("/create_by_alipay_fw.htm")
    public RestResult createByAlipayFw(@Valid @RequestBody createByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return packetPeriodOrderService.payByThird(
                customerId,
                param.agentId,
                param.cabinetId,
                param.batteryType,
                param.activityId,
                param.vipId,
                param.priceId,
                param.price,
                param.couponTicketId,
                ConstEnum.PayType.ALIPAY_FW);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class createByBalanceParam {
        public int agentId;
        public String cabinetId;
        public int batteryType;
        public long activityId;
        public long vipId;
        public int priceId;
        public int price;
        public long couponTicketId;
        
        
    }

    @ResponseBody
    @RequestMapping("/create_by_balance.htm")
    public RestResult createByBalance(@Valid @RequestBody createByBalanceParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        RestResult restResult = null;
        try {
            restResult = packetPeriodOrderService.payBalance(
                    customerId,
                    param.agentId,
                    param.cabinetId,
                    param.batteryType,
                    param.activityId,
                    param.vipId,
                    param.priceId,
                    param.price,
                    param.couponTicketId);
            if (restResult.getCode() != 0) {
                return restResult;
            }
        } catch (CouponTicketNotAvailableException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "优惠券不可用");
        } catch (BalanceNotEnoughException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return packetPeriodOrderService.getList(customerId, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderListParam {
        public String orderId;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/battery_order_list.htm")
    public RestResult batteryOrderList(@Valid @RequestBody BatteryOrderListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        List<Map> list = new ArrayList<Map>();
        List<BatteryOrder> listOrder =
                batteryOrderService.findByPacketOrderId(param.orderId, customerId, param.offset, param.limit);
        if (listOrder != null) {
            for (BatteryOrder batteryOrder : listOrder) {

                NotNullMap line = new NotNullMap();
                line.putString("id", batteryOrder.getId());
                line.putString("batteryId", batteryOrder.getBatteryId());
                line.putInteger("money", batteryOrder.getMoney());
                line.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
                line.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
                line.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
                line.putDateTime("takeTime", batteryOrder.getTakeTime());
                line.putString("putCabinetId", batteryOrder.getPutCabinetId());
                line.putString("putCabinetName", batteryOrder.getPutCabinetName());
                line.putString("putBoxNum", batteryOrder.getPutBoxNum());
                line.putDateTime("putTime", batteryOrder.getPutTime());
                line.putInteger("orderStatus", batteryOrder.getOrderStatus());
                line.putDateTime("createTime", batteryOrder.getCreateTime());

                long duration = 0;
                if (batteryOrder.getPutTime() != null && batteryOrder.getTakeTime() != null) {
                    duration = batteryOrder.getPutTime().getTime() - batteryOrder.getTakeTime().getTime();
                } else if (batteryOrder.getPutTime() == null && batteryOrder.getTakeTime() != null) {
                    duration = System.currentTimeMillis() - batteryOrder.getTakeTime().getTime();
                }
                line.putString("duration", AppUtils.formatHhmmss(duration / 1000));

                line.putInteger("distance", batteryOrder.getCurrentDistance());
                line.putInteger("beginVolume", batteryOrder.getInitVolume());
                line.putInteger("endVolume", batteryOrder.getCurrentVolume());


                line.putString("payTypeName", "");
                if (batteryOrder.getPayType() != null) {
                    line.putString("payTypeName", ConstEnum.PayType.getName(batteryOrder.getPayType()));
                }
                line.putString("ticketName", "");
                Integer ticketMoney = batteryOrder.getTicketMoney();
                if (ticketMoney != null) {
                    String ticketName = batteryOrder.getTicketName();
                    if (ticketName == null) {
                        ticketName = "";
                    }

                    line.putString("ticketName", String.format("%.2f", (1d * ticketMoney) / 100) + "元" + ticketName);
                }

                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApplyrefundParam {
        public String orderId;
        public String reason;
    }

    /**
     * 订单申请退款
     * apply_refund.htm
     */
    @ResponseBody
    @RequestMapping("/apply_refund.htm")
    public RestResult applyRefund(@Valid @RequestBody ApplyrefundParam param) {

        try {
            return packetPeriodOrderService.refundOrder(param.orderId, param.reason, getTokenData().customerId);
        } catch (IllegalArgumentException e) {
            log.error("packetPeriodOrderService.refundOrder 申请退款异常 error", e);
            log.error("申请退款异常:{}", e.getMessage());
            return RestResult.result(RespCode.CODE_1.getValue(), e.getMessage());
        }
    }

}
