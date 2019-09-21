package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopDepositOrderMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentappserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDepositOrder;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
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
public class ShopDepositOrderService extends AbstractService {

    final static Logger log = LogManager.getLogger(ShopDepositOrderService.class);
    @Autowired
    AlipayfwClientHolder alipayfwClientHolder;
    @Autowired
    ShopDepositOrderMapper shopDepositOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    AppConfig appConfig;

    public RestResult findList(String shopId, int offset, int limit) {
        List<Map> list = new ArrayList<Map>(limit);
        List<ShopDepositOrder> shopDepositOrders = shopDepositOrderMapper.findList(shopId, offset, limit);
        for (ShopDepositOrder shopDepositOrder : shopDepositOrders) {
            Map customerMap = new HashMap();
            customerMap.put("id", shopDepositOrder.getId());
            customerMap.put("status", shopDepositOrder.getStatus());
            customerMap.put("payType", shopDepositOrder.getPayType());
            customerMap.put("money", shopDepositOrder.getMoney());
            customerMap.put("createTime", DateFormatUtils.format(shopDepositOrder.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(customerMap);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public int insert(ShopDepositOrder shopDepositOrder) {
        return shopDepositOrderMapper.insert(shopDepositOrder);
    }

    public RestResult payByAlipayfw(boolean test, User user, Agent agent, ShopDepositOrder shopDepositOrder) throws IOException {
        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }
        //实例化客户端
        AlipayClient alipayClient = alipayfwClientHolder.getPartner(agent.getPartnerId());
        if(alipayClient == null) {
            throw new IllegalArgumentException(String.format("AlipayClient is null(payAppId=%d)", agent.getPartnerId()));
        }
        AlipayfwPayOrder alipayfwPayOrder = new AlipayfwPayOrder();
        alipayfwPayOrder.setId(newOrderId(OrderId.OrderIdType.ALIPAYFW_PAY_ORDER));
        alipayfwPayOrder.setPartnerId(agent.getPartnerId());
        alipayfwPayOrder.setAgentId(agent.getId());
        alipayfwPayOrder.setCustomerId(null);
        alipayfwPayOrder.setMobile(user.getMobile());
        alipayfwPayOrder.setCustomerName(null);
        alipayfwPayOrder.setMoney(shopDepositOrder.getMoney());
        alipayfwPayOrder.setSourceType(AlipayPayOrder.SourceType.DEPOSIT_ORDER_SHOP_PAY.getValue());
        alipayfwPayOrder.setSourceId(shopDepositOrder.getId());
        alipayfwPayOrder.setOrderStatus(AlipayfwPayOrder.Status.INIT.getValue());
        alipayfwPayOrder.setMemo(null);
        alipayfwPayOrder.setCreateTime(new Date());
        alipayfwPayOrderMapper.insert(alipayfwPayOrder);

        // 从支付宝生活号改成支付宝当面付
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(alipayfwPayOrder.getId());
        model.setTotalAmount(String.format("%.2f", 1.0 * shopDepositOrder.getMoney() / 100));
        model.setSubject("充值订单支付");
        model.setBody("充值订单支付");
        //门店OpenId取值
       // model.setBuyerId(customer.getFwOpenId());
        ExtendParams params = new ExtendParams();
        params.setSysServiceProviderId(Constant.ALIPAY_SYS_SERVICE_PROVIDER_ID);
        model.setExtendParams(params);
        model.setTimeoutExpress("30m");

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(appConfig.getStaticUrl() + Constant.ALIPAYFW_PAY_OK);
        request.setBizModel(model);

        Map map = new HashMap();
        try {
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            map.put("tradeNo", response.getTradeNo());
            map.put("orderId", shopDepositOrder.getId());
            if (log.isDebugEnabled()) {
                log.debug("payByAlipayfw: {}", AppUtils.encodeJson(map));
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    public RestResult payByWeixinMp(boolean test, User user, Agent agent, ShopDepositOrder shopDepositOrder) {

        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
        weixinmpPayOrder.setPartnerId(agent.getPartnerId());
        weixinmpPayOrder.setAgentId(agent.getId());
        weixinmpPayOrder.setMobile(user.getMobile());
        weixinmpPayOrder.setCustomerId(null);
        weixinmpPayOrder.setCustomerName(null);
        weixinmpPayOrder.setMoney(shopDepositOrder.getMoney());
        weixinmpPayOrder.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_SHOP_PAY.getValue());
        weixinmpPayOrder.setSourceId(shopDepositOrder.getId());
        weixinmpPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrderMapper.insert(weixinmpPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("充值订单支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
        orderRequest.setProductId(shopDepositOrder.getId());

        if (test) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(agent.getParentId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", agent.getParentId()));
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
                data.put("payAppId", agent.getPartnerId());
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
        return  RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
    }
}
