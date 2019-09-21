package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;


import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleForegiftOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
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
@Controller("api_v1_customer_zc_vehicle_foregift_order")
@RequestMapping(value = "/api/v1/customer/zc/vehicle_foregift_order")
public class VehicleForegiftOrderController extends ApiController {


    static final Logger log = LogManager.getLogger(VehicleForegiftOrderController.class);

    @Autowired
    AreaCache areaCache;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;
    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;
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
        public String shopId;
        public long priceId;
        public int vipPriceId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        @Range(min = 0, message = "租金金额不能小于0")
        public int rentPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
    }

    /**
     * 客户押金支付（公众号）
     *
     * @Valid @RequestBody RegisterParam param
     */
    @ResponseBody
    @RequestMapping("/create_by_weixin_mp.htm")
    public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);

        RestResult restResult = null;
        try {
            restResult = vehicleForegiftOrderService.payByThird(
                    customerId,
                    param.shopId,
                    param.priceId,
                    param.vipPriceId,
                    param.foregiftMoney,
                    param.rentPrice,
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByAlipayfwParam {
        public String shopId;
        public long priceId;
        public int vipPriceId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        @Range(min = 0, message = "租金金额不能小于0")
        public int rentPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
    }

    /**
     * 客户押金支付（支付宝生活号）
     */
    @ResponseBody
    @RequestMapping("/create_by_alipay_fw.htm")
    public RestResult batteryCreateByAlipayFw(@Valid @RequestBody CreateByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return vehicleForegiftOrderService.payByThird(
                customerId,
                param.shopId,
                param.priceId,
                param.vipPriceId,
                param.foregiftMoney,
                param.rentPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId,
                ConstEnum.PayType.ALIPAY_FW);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByBalanceParam {
        public String shopId;
        public long priceId;
        public int vipPriceId;
        @Range(min = 0, message = "押金金额不能小于0")
        public int foregiftMoney;
        @Range(min = 0, message = "租金金额不能小于0")
        public int rentPrice;
        public long foregiftTicketId;
        public long packetTicketId;
        public long deductionTicketId;
    }

    /**
     * 客户租车押金支付(余额)
     */
    @ResponseBody
    @RequestMapping("/create_by_balance.htm")
    public RestResult batteryCreateByBalance(@Valid @RequestBody CreateByBalanceParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        try {
            return vehicleForegiftOrderService.payBalance(
                    customerId,
                    param.shopId,
                    param.priceId,
                    param.vipPriceId,
                    param.foregiftMoney,
                    param.rentPrice,
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

    /**
     * 客户租车押金支付(多通道)
     */
    @ResponseBody
    @RequestMapping("/create_by_multi.htm")
    public RestResult createByMulti(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        return vehicleForegiftOrderService.payByMulti(
                customerId,
                param.shopId,
                param.priceId,
                param.vipPriceId,
                param.foregiftMoney,
                param.rentPrice,
                param.foregiftTicketId,
                param.packetTicketId,
                param.deductionTicketId
        );
    }
}
