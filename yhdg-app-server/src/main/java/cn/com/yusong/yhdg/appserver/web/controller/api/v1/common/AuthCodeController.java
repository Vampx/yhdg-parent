package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AlipayfwService;
import cn.com.yusong.yhdg.appserver.service.basic.MobileMessageTemplateService;
import cn.com.yusong.yhdg.appserver.service.basic.PhoneappService;
import cn.com.yusong.yhdg.appserver.service.basic.WeixinmpService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.protocol.msg21.Msg212000001;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_common_basic_auth_code")
@RequestMapping(value = "/api/v1/common/basic/auth_code")
public class AuthCodeController extends ApiController {
    final static Logger log = LogManager.getLogger(AuthCodeController.class);

    @Autowired
    PhoneappService phoneappService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    MobileMessageTemplateService mobileMessageTemplateService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
    public final static String CACHE_GET_AUTH_CODE_MOBILE = "get_auth_code_mobile:";

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidateParam {
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @NotBlank(message = "验证码不能为空")
        public String authCode;
    }

    /**
     * 验证短信验证码
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/validate.htm")
    public RestResult validate(@Valid @RequestBody ValidateParam param) {
        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetByMpParam {
        public int appId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        public Integer type;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/get_by_mp")
    public RestResult getByMp(@Valid @RequestBody GetByMpParam param) {
        return get(param.appId, 0, 0, param.mobile, param.type);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetByFwParam {
        public int appId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        public Integer type;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/get_by_fw")
    public RestResult getByFw(@Valid @RequestBody GetByFwParam param) {
        return get(0, 0, param.appId, param.mobile, param.type);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetByAppParam {
        public int appId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        public Integer type;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/get_by_app")
    public RestResult getByApp(@Valid @RequestBody GetByAppParam param) {
        return get(0, param.appId, 0, param.mobile, param.type);
    }


    public RestResult get(int mpId, int appId, int fwId, String param_mobile, Integer param_type) {
        Integer partnerId = null;
        if (appId > 0) {
            Phoneapp phoneapp = phoneappService.find(appId);
            if (phoneapp == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机APP配置不存在");
            }
            partnerId = phoneapp.getPartnerId();

        } else if (mpId > 0) {
            Weixinmp weixinmp = weixinmpService.find(mpId);
            if (weixinmp == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "公众号配置不存在");
            }
            partnerId = weixinmp.getPartnerId();
        } else if (fwId > 0) {
            Alipayfw alipayfw = alipayfwService.find(fwId);
            if (alipayfw == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "生活号配置不存在");
            }
            partnerId = alipayfw.getPartnerId();
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户端类型不能是空");
        }

        if (partnerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户不存在");
        }

        String authCode = IdUtils.randomSixChar();
        MobileMessageTemplate template = null;
        if (param_type != null) {
            //TODO 测试统一先用验证码模板
            if (param_type.equals(MobileMessageTemplate.Type.AUTH_CODE.getValue())) {
                template = mobileMessageTemplateService.find(partnerId, MobileMessageTemplate.Type.AUTH_CODE.getValue());
            } else if (param_type.equals(MobileMessageTemplate.Type.UNTIE.getValue())) {
                template = mobileMessageTemplateService.find(partnerId, MobileMessageTemplate.Type.AUTH_CODE.getValue());
            } else if (param_type.equals(MobileMessageTemplate.Type.BINDING.getValue())) {
                template = mobileMessageTemplateService.find(partnerId, MobileMessageTemplate.Type.AUTH_CODE.getValue());
            }
        } else {
            template = mobileMessageTemplateService.find(partnerId, MobileMessageTemplate.Type.AUTH_CODE.getValue());
        }


        String content = template.replace(template.getVariable(), authCode);
        String[] variable = {template.getVariable(), authCode};
        try {
            Object mobile = memCachedClient.get(CACHE_GET_AUTH_CODE_MOBILE + param_mobile);
            if (mobile != null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "一分钟内只能发送一次短信");
            }
            memCachedClient.set(CACHE_GET_AUTH_CODE_MOBILE + param_mobile, param_mobile, MemCachedConfig.CACHE_ONE_MINUTE - 5);

            ClientBizUtils.Resp<Msg212000001> resp = ClientBizUtils.sendMobileMessage(config,
                    param_mobile,
                    content,
                    partnerId,
                    template.getId().byteValue(),
                    MobileMessage.buildVariableJson(variable),
                    template.getCode());

            if (resp.code == ClientBizUtils.Resp.CODE_OK) {
                String cacheKey = CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param_mobile);
                String cached = (String) memCachedClient.get(cacheKey);
                if (StringUtils.isNotEmpty(cached)) {
                    authCode = cached + "," + authCode;
                }

                memCachedClient.set(cacheKey, authCode, MemCachedConfig.CACHE_TWO_HOUR);
                return RestResult.result(RespCode.CODE_0.getValue(), null);

            } else if (resp.code == ClientBizUtils.Resp.CODE_NO_CLIENT) {
                log.error("No Available ServiceServer");
            } else if (resp.code == ClientBizUtils.Resp.CODE_RESP_TIMEOUT) {
                log.error("Response Timeout");
            }

        } catch (Exception e) {
            log.error("send sms error", e);
        }
        return RestResult.result(RespCode.CODE_1.getValue(), "短信发送失败");
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/get_auth_image_url")
    public RestResult getAuthImageUrl() {
        String uuid = IdUtils.uuid();
        String imgPath = String.format("/api/v1/common/basic/auth_code/auth_image_%s.htm", uuid);
        String key = CacheKey.key(CacheKey.K_UUID_V_IMAGE_AUTH_CODE, uuid);
        memCachedClient.set(key, uuid, MemCachedConfig.CACHE_FIVE_MINUTE);

        Map data = new HashMap();
        data.put("sign", uuid);
        data.put("url", imgPath);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
    }

    @NotLogin
    @RequestMapping(value = "/auth_image_{sign}.htm")
    public void authImageSign(@PathVariable("sign") String sign, HttpServletResponse response) throws IOException {
        String key = CacheKey.key(CacheKey.K_UUID_V_IMAGE_AUTH_CODE, sign);
        String value = (String) memCachedClient.get(key);
        if (sign.equals(value)) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/png");
            ServletOutputStream servletOutputStream = response.getOutputStream();

            //请求校验码图片
            ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

            //示例代码 https://www.cnblogs.com/zxf330301/p/6210964.html
            RandomWordFactory wordFactory = new RandomWordFactory();
            wordFactory.setMaxLength(3);
            wordFactory.setMinLength(3);
            cs.setWordFactory(wordFactory);

            cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
            cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

            String checkCode = EncoderHelper.getChallangeAndWriteImage(cs, "png", servletOutputStream);
            memCachedClient.replace(key, checkCode, MemCachedConfig.CACHE_FIVE_MINUTE);

        } else {
            response.setStatus(403);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignGetParam {
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @NotBlank
        public String imageAuthCode;
        @NotBlank
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/sign_get")
    public RestResult signGet(@Valid @RequestBody SignGetParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        String mobile = param.mobile;
        String imageAuthCode = param.imageAuthCode;
        String sign = param.sign;

        String key = CacheKey.key(CacheKey.K_UUID_V_IMAGE_AUTH_CODE, sign);
        String value = (String) memCachedClient.get(key);

        if (StringUtils.isEmpty(value)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "图片验证码已过期");
        }

        if (!imageAuthCode.equalsIgnoreCase(value)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "图片验证码错误");
        }

        memCachedClient.delete(key);
        String authCode = IdUtils.randomSixChar();

        MobileMessageTemplate template = mobileMessageTemplateService.find(0, MobileMessageTemplate.Type.AUTH_CODE.getValue());
        String content = template.replace(template.getVariable(), authCode);
        String[] variable = {template.getVariable(), authCode};
        try {
            ClientBizUtils.Resp<Msg212000001> resp = ClientBizUtils.sendMobileMessage(config,
                    mobile,
                    content,
                    MobileMessageTemplate.DEFAULT_APP_ID,
                    template.getId().byteValue(),
                    MobileMessage.buildVariableJson(variable),
                    template.getCode());

            if (resp.code == ClientBizUtils.Resp.CODE_OK) {
                String cacheKey = CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, mobile);
                String cached = (String) memCachedClient.get(cacheKey);
                if (StringUtils.isNotEmpty(cached)) {
                    authCode = cached + "," + authCode;
                }

                memCachedClient.set(cacheKey, authCode, MemCachedConfig.CACHE_TWO_HOUR);
                return RestResult.result(RespCode.CODE_0.getValue(), null);

            } else if (resp.code == ClientBizUtils.Resp.CODE_NO_CLIENT) {
                log.error("No Available ServiceServer");
            } else if (resp.code == ClientBizUtils.Resp.CODE_RESP_TIMEOUT) {
                log.error("Response Timeout");
            }

        } catch (Exception e) {
            log.error("send sms error", e);
        }

        return RestResult.result(RespCode.CODE_1.getValue(), "发送短信失败");
    }


}
