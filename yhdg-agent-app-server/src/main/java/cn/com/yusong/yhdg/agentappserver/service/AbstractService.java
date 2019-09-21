package cn.com.yusong.yhdg.agentappserver.service;

import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    private static final Logger log = LogManager.getLogger(AbstractService.class);

    @Autowired
    WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    AlipayfwClientHolder alipayfwClientHolder;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    AppConfig config;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AreaMapper areaMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    DictItemMapper dictItemMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;


    protected String staticPath(String imagePath) {
        if (StringUtils.isEmpty(imagePath)) {
            return imagePath;
        } else {
            return config.staticUrl + imagePath;
        }
    }

    public String findConfigValue(String id) {
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
        String value = (String) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemConfigMapper.findConfigValue(id);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    public Area findArea(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AREA, id);
        Area area = (Area) memCachedClient.get(key);
        if (area != null) {
            return area;
        }
        area = areaMapper.find(id);

        memCachedClient.set(key, area, MemCachedConfig.CACHE_ONE_WEEK);
        return area;
    }

    protected SystemBatteryType findBatteryType(int batteryType) {
        String key = CacheKey.key(CacheKey.K_BATTERY_TYPE_V_TYPE_NAME, batteryType);
        SystemBatteryType value = (SystemBatteryType) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemBatteryTypeMapper.find(batteryType);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }


    protected Map<String, String> findDictItemMap(int categoryId) {
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, categoryId);
        Map<String, String> v = (Map<String, String>) memCachedClient.get(key);
        if(v != null) {
            return v;
        }

        v = new HashMap<String, String>();

        List<DictItem> itemList = dictItemMapper.findByCategory(categoryId);
        for(DictItem e : itemList) {
            v.put(e.getItemValue(), e.getItemName());
        }
        memCachedClient.set(key, v, MemCachedConfig.CACHE_ONE_WEEK);
        return v;
    }

    public List<? extends AreaEntity> setAreaProperties(AreaCache areaCache, List<? extends AreaEntity> list) {
        for (AreaEntity site : list) {
            setAreaProperties(areaCache, site);
        }

        return list;
    }

    public AreaEntity setAreaProperties(AreaCache areaCache, AreaEntity areaEntity) {
        if (areaEntity == null) {
            return null;
        }

        if (areaEntity.getProvinceId() != null) {
            Area area = areaCache.get(areaEntity.getProvinceId());
            if (area != null) {
                areaEntity.setProvinceName(area.getAreaName());
            }
        }
        if (areaEntity.getCityId() != null) {
            Area area = areaCache.get(areaEntity.getCityId());
            if (area != null) {
                areaEntity.setCityName(area.getAreaName());
            }
        }
        if (areaEntity.getDistrictId() != null) {
            Area area = areaCache.get(areaEntity.getDistrictId());
            if (area != null) {
                areaEntity.setDistrictName(area.getAreaName());
            }
        }

        return areaEntity;
    }

    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    public Map payByWeixinMp(int partnerId, int agentId, String orderId, Integer money, long customerId, String openid, Integer sourceType, String body, String memo) {

        Customer customer = customerMapper.find(customerId);

        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
        weixinmpPayOrder.setAgentId(agentId);
        weixinmpPayOrder.setPartnerId(partnerId);
        weixinmpPayOrder.setCustomerId(customerId);
        weixinmpPayOrder.setMobile(customer.getMobile());
        weixinmpPayOrder.setCustomerName(customer.getFullname());
        weixinmpPayOrder.setMoney(money);
        weixinmpPayOrder.setSourceType(sourceType);
        weixinmpPayOrder.setSourceId(orderId);
        weixinmpPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinmpPayOrder.setMemo(memo);
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrderMapper.insert(weixinmpPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(body);
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setOpenid(openid);
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTimeStart(null);
        orderRequest.setTimeExpire(null);
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(partnerId);
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", partnerId));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult  result = wxPayService.unifiedOrder(orderRequest);
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

                Map root = new HashMap();
                root.put("data", data);
                root.put("payOrder", weixinmpPayOrder);
                return root;
            } else {
                log.error("get prepay id error", ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE));
                return null;
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmpPayOrder.getId());
            log.error("unifiedOrder error", e);
        }
        return null;
    }

    public Map payByAlipayfw(int partnerId, int agentId, String orderId, Integer money, long customerId, Integer sourceType, String subject, String body, String memo) {
        //实例化客户端
        AlipayClient alipayClient = alipayfwClientHolder.getPartner(partnerId);
        if(alipayClient == null) {
            throw new IllegalArgumentException(String.format("AlipayClient is null(payAppId=%d)", partnerId));
        }
        Customer customer = customerMapper.find(customerId);

        AlipayfwPayOrder alipayfwPayOrder = new AlipayfwPayOrder();
        alipayfwPayOrder.setId(newOrderId(OrderId.OrderIdType.ALIPAYFW_PAY_ORDER));
        alipayfwPayOrder.setPartnerId(partnerId);
        alipayfwPayOrder.setAgentId(agentId);
        alipayfwPayOrder.setCustomerId(customerId);
        alipayfwPayOrder.setMobile(customer.getMobile());
        alipayfwPayOrder.setCustomerName(customer.getFullname());
        alipayfwPayOrder.setMoney(money);
        alipayfwPayOrder.setSourceType(sourceType);
        alipayfwPayOrder.setSourceId(orderId);
        alipayfwPayOrder.setOrderStatus(AlipayfwPayOrder.Status.INIT.getValue());
        alipayfwPayOrder.setMemo(memo);
        alipayfwPayOrder.setCreateTime(new Date());
        alipayfwPayOrderMapper.insert(alipayfwPayOrder);

        // 从支付宝生活号改成支付宝当面付
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(alipayfwPayOrder.getId());
        model.setTotalAmount(String.format("%.2f", 1.0 * money / 100));
        model.setSubject(subject);
        model.setBody(body);
        model.setBuyerId(customer.getFwOpenId());
        ExtendParams params = new ExtendParams();
        params.setSysServiceProviderId(Constant.ALIPAY_SYS_SERVICE_PROVIDER_ID);
        model.setExtendParams(params);
        model.setTimeoutExpress("30m");

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(config.getStaticUrl() + Constant.ALIPAYFW_PAY_OK);
        request.setBizModel(model);

        try {
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            Map map = new HashMap();
            map.put("tradeNo", response.getTradeNo());
            return map;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
