package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.utils.*;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_customer_basic_weixin")
@RequestMapping(value = "/api/v1/customer/basic/weixin")
public class WeixinController extends ApiController {

    static final Logger log = LogManager.getLogger(WeixinController.class);

    @Autowired
    WxMpServiceHolder wxMpServiceHolder;
    @Autowired
    SystemConfigService systemConfigService;

    public static class RefreshJsApiTicketParam {
        public int appId;
        @NotBlank(message = "webPath不能为空")
        public String webPath;
    }

    @ResponseBody
    @RequestMapping(value = "/refresh_js_api_ticket.htm")
    public RestResult refreshJsApiTicket(@Valid @RequestBody RefreshJsApiTicketParam param) throws UnsupportedEncodingException, WxErrorException, WxErrorException {
        WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(param.appId);

        long now = System.currentTimeMillis();
        String timestamp = String.format("%d", now / 1000);
        String noncestr = String.format("%d", now);
        String jsapi_ticket = wxMpService.getJsapiTicket();
        Map<String, String> map = new HashMap<String, String>();
        map.put("timestamp", timestamp);
        map.put("noncestr", noncestr);
        map.put("jsapi_ticket", jsapi_ticket);
        map.put("url", param.webPath);
        String signature = ChargerUtils.sign(map, null, ChargerUtils.SignType.SHA1);
        map.put("signature", signature);

        if (log.isDebugEnabled()) {
            String string = ReflectionToStringBuilder.toString(map, ToStringStyle.MULTI_LINE_STYLE);
            log.debug("refresh_js_api_ticket: {}", string);
        }


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    public static class AppJsTicketParam {
        public int appId;
        @NotBlank(message = "webPath不能为空")
        public String webPath;
        @NotBlank(message = "signature不能为空")
        public String signature;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/app_js_ticket.htm")
    public RestResult refreshJsApiTicket(@Valid @RequestBody AppJsTicketParam param) throws WxErrorException, IOException {
//        int formal_platform = Integer.valueOf(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FORMAL_PLATFORM.getValue()));
//        if (formal_platform == ConstEnum.Flag.FALSE.getValue()) {
//
//            Map<String, String> map = new HashMap<String, String>();
//            Map<String, String> header = new HashMap<String, String>();
//            header.put("Content-Type", "application/json");
//            Map<String, String> hashMap = new HashMap<String, String>();
//            hashMap.put("appId", String.format("%d", param.appId));
//            hashMap.put("webPath", param.webPath);
//            Date date = new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String dateString = formatter.format(date);
//            String appSignature = CodecUtils.md5(hashMap.get("appId") + "|" + param.webPath + "|" + dateString + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK");
//            hashMap.put("signature", appSignature);
//
//            OkHttpClientUtils.HttpResp httpResp = OkHttpClientUtils.post("https://hdyz.yusong.com.cn/api/v1/weixin/app_js_ticket.htm", YhdgUtils.encodeJson(hashMap), header);
//            if (httpResp != null && httpResp.status == 200) {
//                Map resp = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
//                if (resp != null && "0".equals(resp.get("code").toString())) {
//                    Map dataMap = (Map) resp.get("data");
//                    map.put("timestamp", dataMap.get("timestamp").toString());
//                    map.put("noncestr", dataMap.get("noncestr").toString());
//                    map.put("jsapi_ticket", dataMap.get("jsapi_ticket").toString());
//                    map.put("url", dataMap.get("url").toString());
//                    map.put("signature", dataMap.get("signature").toString());
//                    map.put("app_id", dataMap.get("app_id").toString());
//                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
//                }
//            }
//            return RestResult.dataResult(RespCode.CODE_2.getValue(), null, map);
//        } else {
//            int payAppId = Constant.SYSTEM_PARTNER_ID;
//            if(param.appId != Constant.SYSTEM_PARTNER_ID) {
//                Agent agent = agentService.find(param.appId);
//                if(agent.getIsSelfBalance() == ConstEnum.Flag.TRUE.getValue()) {
//                    payAppId = agent.getId();
//                }
//            }
//            param.appId = payAppId;

            if (!CodecUtils.md5(param.appId + "|" + param.webPath + "|" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK").equals(param.signature)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "signature无效");
            }

            WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(param.appId);
            if (wxMpService == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), String.format("appId=%d没有配置", param.appId));
            }

            long now = System.currentTimeMillis();
            String timestamp = String.format("%d", now / 1000);
            String noncestr = String.format("%d", now);
            String jsapi_ticket = wxMpService.getJsapiTicket();
            Map<String, String> map = new HashMap<String, String>();
            map.put("timestamp", timestamp);
            map.put("noncestr", noncestr);
            map.put("jsapi_ticket", jsapi_ticket);
            map.put("url", param.webPath);
            String signature = ChargerUtils.sign(map, null, ChargerUtils.SignType.SHA1);
            map.put("signature", signature);
            map.put("app_id", wxMpService.getWxMpConfigStorage().getAppId());

            if (log.isDebugEnabled()) {
                String string = AppUtils.encodeJson(map);
                log.debug("refresh_js_api_ticket: {}", string);
            }


            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
//    }

    public static class PayAppJsTicketParam {
        public int payAppId;
        @NotBlank(message = "webPath不能为空")
        public String webPath;
        @NotBlank(message = "signature不能为空")
        public String signature;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/pay_app_js_ticket.htm")
    public RestResult refreshPayJsApiTicket(@Valid @RequestBody PayAppJsTicketParam param) throws WxErrorException, IOException {
        if (!CodecUtils.md5(param.payAppId + "|" + param.webPath + "|" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK").equals(param.signature)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "signature无效");
        }

        WxMpService wxMpService = wxMpServiceHolder.getPartner(param.payAppId);
        if (wxMpService == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("appId=%d没有配置", param.payAppId));
        }

        long now = System.currentTimeMillis();
        String timestamp = String.format("%d", now / 1000);
        String noncestr = String.format("%d", now);
        String jsapi_ticket = wxMpService.getJsapiTicket();
        Map<String, String> map = new HashMap<String, String>();
        map.put("timestamp", timestamp);
        map.put("noncestr", noncestr);
        map.put("jsapi_ticket", jsapi_ticket);
        map.put("url", param.webPath);
        String signature = ChargerUtils.sign(map, null, ChargerUtils.SignType.SHA1);
        map.put("signature", signature);
        map.put("app_id", wxMpService.getWxMpConfigStorage().getAppId());

        if (log.isDebugEnabled()) {
            String string = AppUtils.encodeJson(map);
            log.debug("refresh_js_api_ticket: {}", string);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    public static class AppAccessTokenParam {
        public int appId;
        @NotBlank(message = "signature不能为空")
        public String signature;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/app_access_token.htm")
    public RestResult refreshJsApiTicket(@Valid @RequestBody AppAccessTokenParam param) throws UnsupportedEncodingException, WxErrorException {
        if (!CodecUtils.md5(param.appId + "|" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK").equals(param.signature)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "signature无效");
        }

        WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(param.appId);
        if (wxMpService == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("appId=%d没有配置", param.appId));
        }

        Map map = new HashMap();
        map.put("accessToken", wxMpService.getAccessToken());
        map.put("appId", wxMpService.getWxMpConfigStorage().getAppId());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

}