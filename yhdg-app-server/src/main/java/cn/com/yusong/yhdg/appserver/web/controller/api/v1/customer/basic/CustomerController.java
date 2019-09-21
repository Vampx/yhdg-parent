package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.config.WxMaServiceHolder;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zd.*;
import cn.com.yusong.yhdg.appserver.utils.DownloadFileUtils;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.NotVerifyAccountOtherPhoneLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.LoginQrcode;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.entity.UnionOpenId;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20180916.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller("api_v1_customer_basic_customer")
@RequestMapping(value = "/api/v1/customer/basic/customer")
public class CustomerController extends ApiController {

    static final Logger log = LogManager.getLogger(CustomerController.class);
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    PartnerMpOpenIdService partnerMpOpenIdService;
    @Autowired
    protected AppConfig appConfig;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired

    InsuranceOrderService insuranceOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    WeixinmaService weixinmaService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    PhoneappService phoneappService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    PartnerMaOpenIdService partnerMaOpenIdService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    ExchangeInstallmentSettingService exchangeInstallmentSettingService;
    @Autowired
    ExchangeInstallmentDetailService exchangeInstallmentDetailService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    RentInstallmentDetailService rentInstallmentDetailService;
    @Autowired
    RentInstallmentSettingService rentInstallmentSettingService;
    @Autowired
    RentBatteryForegiftService rentBatteryForegiftService;
    @Autowired
    CustomerManualAuthRecordService customerManualAuthRecordService;
    @Autowired
    InsuranceService insuranceService;
    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
    @Autowired
    RentInsuranceService rentInsuranceService;
    @Autowired
    RentPeriodPriceService rentPeriodPriceService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    CustomerOfflineBatteryService customerOfflineBatteryService;
    @Autowired
    CustomerOfflineExchangeRecordService customerOfflineExchangeRecordService;
    @Autowired
    WxMaServiceHolder wxMaServiceHolder;

    //新加四个
    @Autowired
    ExchangeInstallmentCabinetService exchangeInstallmentCabinetService;
    @Autowired
    ExchangeInstallmentCountDetailService exchangeInstallmentCountDetailService;
    @Autowired
    ExchangeInstallmentCountService exchangeInstallmentCountService;
    @Autowired
    ExchangeInstallmentCustomerService exchangeInstallmentCustomerService;
    @Autowired
    ExchangeInstallmentStationService exchangeInstallmentStationService;

    public static class GetOpenIdParam {
        public int appId;
        public String code;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping("get_open_id.htm")
    public RestResult getOpenId(@RequestBody GetOpenIdParam param) {
        if (log.isDebugEnabled()) {
            log.debug("code2Session: code={}", param.code);
        }


        WxMaService wxMaService = wxMaServiceHolder.get(param.appId);
        if (wxMaService == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "appId不存在");
        }

        Weixinma weixinma = weixinmaService.find(param.appId);
        if (weixinma == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "小程序appId不存在");
        }



        Map data = new HashMap();
        try {
            WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(param.code);
            data.put("openId", result.getOpenid());

            if (log.isDebugEnabled()) {
                log.debug("result: {}", result.toString());
            }

            PartnerMaOpenId openId = partnerMaOpenIdService.findByOpenId(weixinma.getPartnerId(), result.getOpenid());
            if (openId == null) {
                openId = new PartnerMaOpenId();
                openId.setPartnerId(weixinma.getPartnerId());
                openId.setOpenId(result.getOpenid());
                openId.setSessionKey(result.getSessionKey());
                openId.setCreateTime(new Date());
                partnerMaOpenIdService.insert(openId);

            } else {
                partnerMaOpenIdService.updateSessionKey(openId.getId(), result.getSessionKey());
            }


        } catch (WxErrorException e) {
            log.error("getOpenId error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "获取openId失败");
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    public static class GetPhoneNumberParam {
        public int appId;
        public String openId;
        public String encryptedData;
        public String iv;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping("get_phone_number.htm")
    public RestResult getPhoneNumber(@RequestBody GetPhoneNumberParam param) {
        Weixinma weixinma = weixinmaService.find(param.appId);
        if (weixinma == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "小程序appId不存在");
        }

        Partner partner = partnerService.find(weixinma.getPartnerId());
        if (partner == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户不存在");
        }

        WxMaService wxMaService = wxMaServiceHolder.get(param.appId);
        if (wxMaService == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "appId不存在");
        }

        PartnerMaOpenId openId = partnerMaOpenIdService.findByOpenId(weixinma.getPartnerId(), param.openId);
        if (openId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "OpenId不存在");
        }
        try {
            WxMaPhoneNumberInfo info = wxMaService.getUserService().getPhoneNoInfo(openId.getSessionKey(), param.encryptedData, param.iv);

            RestResult result = customerService.maBindMobile(partner.getId(),  param.openId, info.getPurePhoneNumber());
            if (result.getCode() != 0) {
                return result;
            }
            Customer customer = (Customer) result.getData();
            int expireIn = MemCachedConfig.CACHE_THREE_DAY;

            String token = tokenCache.putOpenCustomer(customer.getId(), partner.getId(), TokenCache.Data.CLIENT_TYPE_MA, param.openId, param.openId, param.appId, expireIn).token;

            customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.MP.getValue());

            customerService.updateMpLoginToken(customer.getId(), token);

            Map data = new HashMap();
            data.put("id", customer.getId());
            data.put("token", token);
            data.put("expireIn", expireIn);

            memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, customer.getMobile()));
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

        } catch (Exception e) {
            log.error("getPhoneNoInfo error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "获取手机号失败");
        }
    }

    public static class GetUserInfoParam {
        public int appId;
        public String openId;
        public String encryptedData;
        public String iv;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping("get_user_info.htm")
    public RestResult getUserInfo(@RequestBody GetUserInfoParam param) {
        WxMaService wxMaService = wxMaServiceHolder.get(param.appId);
        if (wxMaService == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "appId不存在");
        }

        Weixinma weixinma = weixinmaService.find(param.appId);
        if (weixinma == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "小程序appId不存在");
        }

        Partner partner = partnerService.find(weixinma.getPartnerId());
        if (partner == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户不存在");
        }

        PartnerMaOpenId openId = partnerMaOpenIdService.findByOpenId(weixinma.getPartnerId(), param.openId);
        if (openId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "OpenId不存在");
        }

        try {
            WxMaUserInfo info = wxMaService.getUserService().getUserInfo(openId.getSessionKey(), param.encryptedData, param.iv);

            String photoPath = null;

            if (org.apache.commons.lang3.StringUtils.isNotEmpty(info.getAvatarUrl())) {
                File tempFile = new File(config.tempDir, IdUtils.uuid() + ".jpg");
                DownloadFileUtils.download(info.getAvatarUrl(), tempFile);
                if (tempFile.exists()) {
                    Map<String, File> fileMap = new HashMap<String, File>();
                    fileMap.put(tempFile.getName(), tempFile);

                    HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(config.staticUrl + "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_PHOTO_PATH.getValue(), fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
                    if (httpResp.status / 100 == 2) {
                        List<Map> list = (List<Map>) ((Map) AppUtils.decodeJson(httpResp.content, Map.class)).get("data");
                        photoPath = (String) list.get(0).get("filePath");
                    } else {
                        log.error("上传附件错误, {}", httpResp.toString());
                    }
                }
            }

            partnerMaOpenIdService.update(weixinma.getPartnerId(), param.openId, AppUtils.filterEmoji(info.getNickName()), photoPath);

            return RestResult.result(RespCode.CODE_0.getValue(), null);
        } catch (Exception e) {
            log.error("getUserInfo error", e);
            return RestResult.result(RespCode.CODE_2.getValue(), "获取个人信息失败");
        }
    }

    public static class MaLoginParam {
        public int appId;
        @NotBlank(message = "openId不能为空")
        public String openId;
        @NotBlank(message = "password不能为空")
        public String password;
    }

    /**
     * 小程序授权
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/ma_login.htm")
    public RestResult mpLogin(@Valid @RequestBody MaLoginParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        String pw = param.password;
        String makeOpenId = CodecUtils.password(param.openId);

        if (!pw.equals(makeOpenId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
        }

        Weixinma weixinma = weixinmaService.find(param.appId);
        if (weixinma == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "小程序appId不存在");
        }

        Customer customer = customerService.findByMaOpenId(weixinma.getPartnerId(), param.openId);

        if (customer == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "客户未注册");
        }
        if (customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }
        if (customer.getAgentId() != null) {
            Agent agent = agentService.find(customer.getAgentId());
            if (agent != null) {
                if (agent.getWeixinmaId() == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置小程序号");
                }
                if (!weixinma.getId().equals(agent.getWeixinmaId())) {//当前运营商不在此公众号下
                    Weixinma ma = weixinmaService.find(agent.getWeixinmaId());
                    if (ma != null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), String.format("请使用%s小程序号", ma.getAppName()));
                    } else {
                        return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置小程序号");
                    }

                }
            }
        }
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), weixinma.getPartnerId(), TokenCache.Data.CLIENT_TYPE_MP, param.openId, param.openId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.MA.getValue());

        customerService.updateMpLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckMobileByMpParam {
        public int appId;
        @NotBlank(message = "手机号不能为空")
        public String mobile;
    }


    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/check_mobile_by_mp")
    public RestResult checkMobileByMp(@Valid @RequestBody CheckMobileByMpParam param) {
        String type = "ok";
        Integer partnerId = null;

        if (param.appId > 0) {
            Weixinmp weixinmp = weixinmpService.find(param.appId);
            if (weixinmp != null) {
                partnerId = weixinmp.getPartnerId();
            }
        }
        if (partnerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户配置错误");
        }

        Customer customer = customerService.findByMobile(partnerId, param.mobile);
        if (customer == null) {
            type = "mobile_not_exist";
        } else if (StringUtils.isEmpty(customer.getPassword())) {
            type = "password_not_set";
        }

        Map data = new HashMap();
        data.put("type", type);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckMobileByFwParam {
        public int appId;
        @NotBlank(message = "手机号不能为空")
        public String mobile;
    }


    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/check_mobile_by_fw")
    public RestResult checkMobile(@Valid @RequestBody CheckMobileByFwParam param) {
        String type = "ok";
        Integer partnerId = null;

        if (param.appId > 0) {
            Alipayfw alipayfw = alipayfwService.find(param.appId);
            if (alipayfw != null) {
                partnerId = alipayfw.getPartnerId();
            }
        }

        if (partnerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户配置错误");
        }

        Customer customer = customerService.findByMobile(partnerId, param.mobile);
        if (customer == null) {
            type = "mobile_not_exist";
        } else if (StringUtils.isEmpty(customer.getPassword())) {
            type = "password_not_set";
        }

        Map data = new HashMap();
        data.put("type", type);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckMobileByAppParam {
        public int appId;
        @NotBlank(message = "手机号不能为空")
        public String mobile;
    }


    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/check_mobile_by_app")
    public RestResult checkMobileByApp(@Valid @RequestBody CheckMobileByAppParam param) {
        String type = "ok";
        Integer partnerId = null;

        if (param.appId > 0) {
            Phoneapp phoneapp = phoneappService.find(param.appId);
            if (phoneapp != null) {
                partnerId = phoneapp.getPartnerId();
            }
        }
        if (partnerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "商户配置错误");
        }

        Customer customer = customerService.findByMobile(partnerId, param.mobile);
        if (customer == null) {
            type = "mobile_not_exist";
        } else if (StringUtils.isEmpty(customer.getPassword())) {
            type = "password_not_set";
        }

        Map data = new HashMap();
        data.put("type", type);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePasswordParam {
        @NotBlank(message = "旧密码不能为空")
        public String oldPassword;
        @NotBlank(message = "新密码不能为空")
        public String newPassword;
    }


    @ResponseBody
    @RequestMapping(value = "/update_password")
    public RestResult updatePassword(@Valid @RequestBody UpdatePasswordParam param) {

        if (customerService.updatePassword(getTokenData().customerId, param.oldPassword, param.newPassword) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePasswordParam2 {
        @NotBlank(message = "新密码不能为空")
        public String password;
    }

    @ResponseBody
    @RequestMapping(value = "/update_password2")
    public RestResult updatePassword2(@Valid @RequestBody UpdatePasswordParam2 param) {
        customerService.updatePassword2(getTokenData().customerId, param.password);
        return RestResult.SUCCESS;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginParam {
        public int appId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @NotBlank(message = "密码不能为空")
        public String password;
    }

    /**
     * 登录
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/login.htm")
    public RestResult login(@Valid @RequestBody LoginParam param) {
        Phoneapp phoneapp = phoneappService.find(param.appId);
        if (phoneapp == null) {
            return RestResult.result(RespCode.CODE_6.getValue(), "手机端配置不存在");
        }

        Map map = new HashMap();

        Customer customer = customerService.findByMobile(phoneapp.getPartnerId(), param.mobile);
        if (customer != null) {
            if (StringUtils.isEmpty(customer.getPassword())) {
                return RestResult.result(RespCode.CODE_6.getValue(), "密码未设置");
            }
            if (!customer.getPassword().equals(param.password)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
            }
        } else {
            return RestResult.result(RespCode.CODE_5.getValue(), "账号不存在");
        }

        if (customer.getIsActive() == null || customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }

        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        String token = tokenCache.putCustomer(customer.getId(), customer.getPartnerId(), customer.getMobile(), TokenCache.Data.CLIENT_TYPE_APP, param.appId, customer.getPushType(), customer.getPushToken(), expireIn).token;

        customerService.updateLoginToken(customer.getId(), token, new Date(), ConstEnum.ClientType.APP.getValue());

        map.put("id", customer.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        map.put("passwordStatus", customer.getPassword() != null ? 1 : 0);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindingParam {
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String newMobile;
        @NotBlank(message = "验证码不能为空")
        public String authCode;
    }

    /**
     * 换绑
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/binding.htm")
    public RestResult binding(@Valid @RequestBody BindingParam param) {
        long customerId = getTokenData().customerId;
        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.newMobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        RestResult result = customerService.updateMobile(getTokenData().customerId, customer.getPartnerId(), param.newMobile);
        if (result.getCode() == 0) {
            memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.newMobile));
        }
        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginByAuthCodeParam {
        public int appId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @NotBlank(message = "验证码不能为空")
        public String authCode;
    }

    /**
     * 登录 验证码 手机号
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/login_by_auth_code.htm")
    public RestResult loginByAuthCode(@Valid @RequestBody LoginByAuthCodeParam param) {

        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }

        Phoneapp phoneapp = phoneappService.find(param.appId);
        if (phoneapp == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机APP没有配置");
        }

        Map map = new HashMap();
        Customer customer = customerService.findByMobile(phoneapp.getPartnerId(), param.mobile);
        if (customer == null) {
            customer = new Customer();
            customer.setPartnerId(phoneapp.getPartnerId());
            customer.setBalance(0);    //余额默认为0
            customer.setGiftBalance(0);
            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
            customer.setMobile(param.mobile);
            customer.setRegisterType(Customer.RegisterType.APP.getValue());
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
            customer.setCreateTime(new Date());
            if (customerService.insert(customer) == 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "账号异常");
            }


        } else {
            if (customer.getIsActive() == null || customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
            }
        }

        int expireIn = MemCachedConfig.CACHE_ONE_DAY;
        String token = tokenCache.putCustomer(customer.getId(), customer.getPartnerId(), customer.getMobile(), TokenCache.Data.CLIENT_TYPE_APP, param.appId, customer.getPushType(), customer.getPushToken(), expireIn).token;
        customerService.updateLoginToken(customer.getId(), token, new Date(), ConstEnum.ClientType.APP.getValue());

        map.put("id", customer.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        map.put("passwordStatus", customer.getPassword() != null ? 1 : 0);

        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponTicketListParam {
        public int status;
        public int offset;
        public int limit;
    }

    /**
     * 查询我的优惠券
     * <p>
     * coupon_ticket_list
     */

    @ResponseBody
    @RequestMapping(value = "/coupon_ticket_list.htm")
    public RestResult couponTicketList(@Valid @RequestBody CouponTicketListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        if (customer.getMobile() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "无可用优惠券");
        }
        List<Map> list = new ArrayList<Map>(20);
        List<CustomerCouponTicket> ticketList = customerCouponTicketService.findList(null, customer.getMobile(),null,null, param.status, CustomerCouponTicket.Category.EXCHANGE.getValue(), param.offset, param.limit);
        if (ticketList != null) {
            for (CustomerCouponTicket ticket : ticketList) {

                Map mapTicket = new HashMap();
                mapTicket.put("id", ticket.getId());
                if (ticket.getAgentId() != null) {
                    Agent agent = agentService.find(ticket.getAgentId());
                    mapTicket.put("agentName", agent.getAgentName());
                } else {
                    mapTicket.put("agentName", "");
                }

                mapTicket.put("ticketTypeName", ticket.getTicketTypeName());
                mapTicket.put("ticketName", ticket.getTicketName() == null ? "" : ticket.getTicketName());
                mapTicket.put("money", ticket.getMoney() == null ? 0 : ticket.getMoney());
                mapTicket.put("expireTime", ticket.getExpireTime() != null ? DateFormatUtils.format(ticket.getExpireTime(), Constant.DATE_FORMAT) : "");
                list.add(mapTicket);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }



    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult getInfo() {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Map map = new HashMap(20);
        Customer customer = customerService.find(customerId);
        if (customer != null) {
            map.put("id", customer.getId());
            map.put("idCard", customer.getIdCard());
            map.put("nickname", customer.getNickname());
            map.put("fullname", customer.getFullname());
            map.put("mobile", customer.getMobile());
            map.put("photoPath", staticImagePath(customer.getPhotoPath()));
            map.put("balance", customer.getBalance());
            map.put("deposit", customer.getBalance());

            map.put("agentId", customer.getAgentId());
            Integer batteryType = null;
            Integer foregiftPrice = 0;
            Integer foregift = 0;
            Integer foregiftDeductionTicketMoney = 0;
            Integer foregiftTicketMoney = 0;

            String foregiftOrderId = null;
            Long foregiftId = null;
            String balanceCabinetId = null;
            String balanceShopId = null;
            String balanceStationId = null;
            Integer vehicleForegiftFlag = null;
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(customer.getAgentId(), customerId);
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
                map.put("isWhite", 1);
            } else {
                CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
                if(customerExchangeInfo != null){
                    batteryType = customerExchangeInfo.getBatteryType();
                    balanceCabinetId = customerExchangeInfo.getBalanceCabinetId();
                    balanceShopId = customerExchangeInfo.getBalanceShopId();
                    balanceStationId = customerExchangeInfo.getBalanceStationId();
                    foregiftOrderId = customerExchangeInfo.getForegiftOrderId();
                    vehicleForegiftFlag = customerExchangeInfo.getVehicleForegiftFlag();
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(foregiftOrderId);
                    if (customerForegiftOrder != null) {
                        foregiftPrice = customerForegiftOrder.getPrice();
                        foregift = customerForegiftOrder.getMoney();
                        foregiftDeductionTicketMoney = customerForegiftOrder.getDeductionTicketMoney();
                        foregiftTicketMoney = customerForegiftOrder.getTicketMoney();
                        foregiftId = customerForegiftOrder.getForegiftId();
                    }
                }
                map.put("isWhite", 0);
            }
            map.put("foregiftId", foregiftId);
            map.put("cabinetId", balanceCabinetId);
            map.put("shopId", balanceShopId);
            map.put("stationId", balanceStationId);
            map.put("batteryType", batteryType);
            map.put("vehicleForegiftFlag", vehicleForegiftFlag);
            if(batteryType != null ){
                map.put("batteryTypeName", systemBatteryTypeService.find(batteryType).getTypeName());
            }
            map.put("foregiftPrice",foregiftPrice);
            map.put("foregift",foregift);
            map.put("foregiftDeductionTicketMoney",foregiftDeductionTicketMoney);
            map.put("foregiftTicketMoney",foregiftTicketMoney);
            map.put("foregiftOrderId",foregiftOrderId);

            map.put("authStep", "ok");
            if (StringUtils.isEmpty(customer.getMobile())) {
                map.put("authStep", "not_mobile");
            } else if (customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()
                    || customer.getAuthStatus() == Customer.AuthStatus.AUTO_FAIL.getValue()
                    || customer.getAuthStatus() == Customer.AuthStatus.AUDIT_REFUSE.getValue()) {
                map.put("authStep", "not_authentication");
            } else if (StringUtils.isEmpty(customer.getPassword())) {
                if (StringUtils.isEmpty(tokenData.openid)) {
                    map.put("authStep", "not_password");
                }
            }
            //有分期欠款记录
            int waitPayCount = customerInstallmentRecordPayDetailService.findCountByCustomerId(customerId, CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.EXCHANGE.getValue(), new Date());
            if (waitPayCount > 0) {
                map.put("authStep", "expire_debt");
            }

            //存在多通道待支付订单
            if(customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.HD.getValue()) > 0){
                map.put("authStep", "multi_wait_pay");
            }

            BackBatteryOrder backBatteryOrder = backBatteryOrderService.findBatteryOrder(customerId, BackBatteryOrder.OrderStatus.SUCCESS.getValue());
            if (backBatteryOrder != null) {
                map.put("backBatteryOrderId", backBatteryOrder.getId());
            } else {
                map.put("backBatteryOrderId", "");
            }

            map.put("city", "");
            if (customer.getCityId() != null) {
                Area area = areaCache.get(customer.getCityId());
                map.put("city", area.getAreaName());
            }
            map.put("isBindMp", StringUtils.isNotEmpty(customer.getMpOpenId()) ? 1 : 0);
            map.put("isBindFw", StringUtils.isNotEmpty(customer.getFwOpenId()) ? 1 : 0);

            //租金处理
            Long packetRemainTime = null;
            Integer rentMoney = null;
            Date beginTime = null;
            Date endTime = null;

//            String packetRemainTime = null;
//            String beginTime = null;
//            String endTime = null;
            long now = System.currentTimeMillis();
            PacketPeriodOrder lastEndTime = packetPeriodOrderService.findLastEndTime(customerId);
            List<PacketPeriodOrder> noUsedList = packetPeriodOrderService.findListByNoUsed(customerId);
            if (lastEndTime != null) {
                if (now < lastEndTime.getEndTime().getTime()) {
                    //packetRemainTime = AppUtils.formatTimeUnit((lastEndTime.getEndTime().getTime() - now) / 1000);
                    packetRemainTime = (lastEndTime.getEndTime().getTime() - now) / 1000;
                    rentMoney = lastEndTime.getMoney();
                    beginTime = lastEndTime.getBeginTime();
                    endTime = lastEndTime.getEndTime();
                }
            }
            if(noUsedList.size() > 0){
                for(PacketPeriodOrder order : noUsedList){
                    if(packetRemainTime != null){
                        packetRemainTime += order.getDayCount() * 24 * 3600 * 1l;
                        rentMoney += order.getMoney();
                        endTime = DateUtils.addDays(endTime,order.getDayCount());
                    }else{
                        packetRemainTime = order.getDayCount() * 24 * 3600 * 1l;
                        rentMoney = order.getMoney();
                        beginTime = new Date();
                        endTime = DateUtils.addDays(new Date(),order.getDayCount());
                    }
                }
            }
            if(packetRemainTime != null){
                map.put("beginTime", DateFormatUtils.format(beginTime, Constant.DATE_FORMAT));
                map.put("endTime", DateFormatUtils.format(endTime, Constant.DATE_FORMAT));
                map.put("packetRemainTime",  AppUtils.formatTimeUnit(packetRemainTime));
                map.put("rentMoney", rentMoney);
                map.put("isNotUse", 1);
            }else{
                map.put("beginTime", null);
                map.put("endTime", null);
                map.put("packetRemainTime", null);
                map.put("rentMoney", null);
                map.put("isNotUse", 0);
            }


            map.put("idCardFace", staticImagePath(customer.getIdCardFace()));
            map.put("idCardRear", staticImagePath(customer.getIdCardRear()));

            if (customer.getPartnerId() != null) {
                map.put("couponTicketCount", customerCouponTicketService.findCount(customer.getPartnerId(), customer.getMobile(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Category.EXCHANGE.getValue()));
            }else{
                map.put("couponTicketCount", 0);
            }
            map.put("exchangeCount", batteryOrderService.findTodayOrderCount(customer.getId()));

            String insuranceRemainTime = null;
            InsuranceOrder insuranceOrder = insuranceOrderService.findByCustomerId(customerId, batteryType, InsuranceOrder.Status.PAID.getValue());
            if(insuranceOrder != null) {
                if (now < insuranceOrder.getEndTime().getTime()) {
                    insuranceRemainTime = AppUtils.formatTimeUnit((insuranceOrder.getEndTime().getTime() - now) / 1000);
                }
            }
            map.put("insuranceRemainTime", insuranceRemainTime);

            int systemAuthType = Weixinmp.AuthType.AUTO.getValue();
            if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_MP) {
                Weixinmp weixinmp = weixinmpService.find(tokenData.appId);
                if (weixinmp == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "公众号配置不存在", null);
                }
                systemAuthType = weixinmp.getAuthType();

            } else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_FW) {
                Alipayfw alipayfw = alipayfwService.find(tokenData.appId);
                if (alipayfw == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "生活号配置不存在", null);
                }
                systemAuthType = alipayfw.getAuthType();

            } else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_APP) {
                Phoneapp phoneapp = phoneappService.find(tokenData.appId);
                if (phoneapp == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "手机端配置不存在", null);
                }
                systemAuthType = phoneapp.getAuthType();
            }else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_MA) {
                Weixinma weixinma = weixinmaService.find(tokenData.appId);
                if (weixinma == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "小程序配置不存在", null);
                }
                systemAuthType = weixinma.getAuthType();

            }

            map.put("systemAuthType", systemAuthType);
            map.put("authStatus", customer.getAuthStatus());
            map.put("authMessage", customer.getAuthMessage());

            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
        } else {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthenticationParam {
        public String fullname;
        public String idCard;
        public String idCardFace;
        public String idCardRear;
        public String authFace;
    }

    /**
     * 实名认证
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/authentication.htm")
    public RestResult authentication(@Valid @RequestBody AuthenticationParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }
        customerService.updateFullname(customerId, param.fullname);

        CustomerManualAuthRecord customerManualAuthRecord = new CustomerManualAuthRecord();
        customerManualAuthRecord.setCustomerId(customerId);
        customerManualAuthRecord.setPartnerId(customer.getPartnerId());
        customerManualAuthRecord.setFullname(param.fullname);
        customerManualAuthRecord.setMobile(customer.getMobile());
        customerManualAuthRecord.setIdCard(param.idCard);
        customerManualAuthRecord.setIdCardFace(param.idCardFace);
        customerManualAuthRecord.setIdCardRear(param.idCardRear);
        customerManualAuthRecord.setAuthFacePath(param.authFace);
        customerManualAuthRecord.setStatus(CustomerManualAuthRecord.Status.NOT.getValue());
        customerManualAuthRecord.setCreateTime(new Date());
        customerManualAuthRecordService.insert(customerManualAuthRecord);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MaAuthenticationParam {
        public String fullname;
        public String idCard;
    }

    /**
     * 小程序实名认证
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ma_authentication.htm")
    public RestResult maAuthentication(@Valid @RequestBody MaAuthenticationParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }
        customerService.updateFullname(customerId, param.fullname);


//        int systemAuthType = Weixinma.AuthType.AUTO.getValue();
//         if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_MA) {
//            Weixinma weixinma = weixinmaService.find(tokenData.appId);
//            if (weixinma == null) {
//                return RestResult.dataResult(RespCode.CODE_2.getValue(), "小程序配置不存在", null);
//            }
//            systemAuthType = weixinma.getAuthType();
//        }

        //审核记录
        CustomerManualAuthRecord customerManualAuthRecord = new CustomerManualAuthRecord();
        customerManualAuthRecord.setCustomerId(customerId);
        customerManualAuthRecord.setPartnerId(customer.getPartnerId());
        customerManualAuthRecord.setFullname(param.fullname);
        customerManualAuthRecord.setMobile(customer.getMobile());
        customerManualAuthRecord.setIdCard(param.idCard);
//        if(systemAuthType ==  Weixinmp.AuthType.AUTO.getValue()){
//            customerManualAuthRecord.setStatus(CustomerManualAuthRecord.Status.APPROVAL.getValue());
//        }else{
            customerManualAuthRecord.setStatus(CustomerManualAuthRecord.Status.NOT.getValue());
//        }
        customerManualAuthRecord.setCreateTime(new Date());

        customerManualAuthRecordService.insertForMa(customerManualAuthRecord);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 查询帐户余额
     */
    @ResponseBody
    @RequestMapping(value = "/balance.htm")
    public RestResult getBalance() {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }

        Map map = new HashMap();
        map.put("balance", customer.getBalance());
        map.put("foregift", customer.getGiftBalance());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class scanLoginQrcodeParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;
    }

    @ResponseBody
    @RequestMapping(value = "/scan_login_qrcode.htm")
    public RestResult scanLoginQrcode(@Valid @RequestBody scanLoginQrcodeParam param) {
        String key = CacheKey.key(CacheKey.K_LOGIN_QRCODE_V_CUSTOMER_ID, param.qrcode);
        LoginQrcode loginQrcode = (LoginQrcode) memCachedClient.get(key);
        if (loginQrcode != null) {
            loginQrcode.status = Constant.LOGIN_QRCODE_STATUS_SCAN_SUCCESS;
            memCachedClient.replace(key, loginQrcode, MemCachedConfig.CACHE_THREE_MINUTE);
            Map map = new HashMap();
            map.put("cabinetId", loginQrcode.cabinetId);
            map.put("cabinetName", loginQrcode.cabinetName);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            return RestResult.result(RespCode.CODE_4.getValue(), "二维码已过期");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConfirmLoginQrcodeParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;
    }

    @ResponseBody
    @RequestMapping(value = "/confirm_login_qrcode.htm")
    public RestResult confirmLoginQrcode(@Valid @RequestBody ConfirmLoginQrcodeParam param) {
        TokenCache.Data tokenData = getTokenData();
        String key = CacheKey.key(CacheKey.K_LOGIN_QRCODE_V_CUSTOMER_ID, param.qrcode);
        LoginQrcode loginQrcode = (LoginQrcode) memCachedClient.get(key);
        if (loginQrcode != null) {
            loginQrcode.customerId = tokenData.customerId;
            loginQrcode.status = Constant.LOGIN_QRCODE_STATUS_LOGIN_SUCCESS;
            memCachedClient.replace(key, loginQrcode, MemCachedConfig.CACHE_THREE_MINUTE);

            return RestResult.result(RespCode.CODE_0.getValue(), null);
        } else {
            return RestResult.result(RespCode.CODE_4.getValue(), "二维码已过期");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReportPushTokenParam {
        public Integer pushType;
        public String pushToken;
    }

    @NotVerifyAccountOtherPhoneLogin
    @ResponseBody
    @RequestMapping(value = "/report_push_token.htm")
    public RestResult reportPushToken(@Valid @RequestBody ReportPushTokenParam param) {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }
        tokenData.pushType = param.pushType;
        tokenData.pushToken = param.pushToken;
        tokenCache.putCopy(tokenData, MemCachedConfig.CACHE_THREE_DAY);

        return customerService.updatePushToken(tokenData.customerId, param.pushType, param.pushToken);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateInfoParam {
        public String photoPath;
        public String facePath1;
        public String facePath2;
        public String facePath3;
    }

    @ResponseBody
    @RequestMapping(value = "/update_info.htm")
    public RestResult updateInfo(@Valid @RequestBody UpdateInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }

        customerService.updateInfo(tokenData.customerId, param.photoPath, param.facePath1, param.facePath2, param.facePath3);
        return RestResult.SUCCESS;
    }


    /**
     * 查询租赁电池信息
     */
    @ResponseBody
    @RequestMapping(value = "/battery_info.htm")
    public RestResult getBatteryInfo() {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }

        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryMapper.findListByCustomer(customer.getId());

        List<Map> lines = new ArrayList<Map>(batteryList.size());
        for (CustomerExchangeBattery customerExchangeBattery : batteryList) {
            Map map = new HashMap();
            Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
            if (battery != null) {

                Integer voltage = battery.getVoltage();
                map.put("voltage", voltage);

                if(battery.getType() != null){
                    String[] split = battery.getTemp().split(",");
                    if(split.length > 0){
                        map.put("batteryTemp", split[0]);
                    }
                }else{
                    map.put("batteryTemp", null);
                }


                map.put("batteryId", battery.getId());
                map.put("batteryType", battery.getBatteryType());
                if(battery.getType() != null ){
                    map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
                }
                map.put("batteryOrderId", battery.getOrderId());
                map.put("Status", battery.getStatus());
                map.put("shellCode", battery.getShellCode());
                map.put("code", battery.getCode());
                map.put("lng", battery.getLng());
                map.put("lat", battery.getLat());
                map.put("volume", battery.getVolume());
                Integer designMileage;
                if (customer.getAgentId() != null && customer.getAgentId() != 0) {
                    designMileage = Integer.parseInt(agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue(), customer.getAgentId()));
                } else {
                    designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
                }
                map.put("estimateDistance", designMileage * battery.getVolume() / 100);//预计行驶里程
                String statusName = Battery.Status.getName(battery.getStatus());
                if (battery.getChargeStatus() != null && Battery.ChargeStatus.CHARGING.getValue() == battery.getChargeStatus()) {
                    statusName += "/充电中";
                }
                map.put("statusName", statusName);
                map.put("isOnLine", battery.getIsOnline());

                //包含自救版本并且出现欠压故障才能自救
                String batteryVersions = systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_RESCUE_VERSION.getValue());
                if((battery.getMonomerLowvoltageFaultLogId() != null || battery.getWholeLowvoltageFaultLogId() != null)
                        && (battery.getVersion() != null && batteryVersions.indexOf(battery.getVersion() )> -1) ){
                    map.put("showRescue", ConstEnum.Flag.TRUE.getValue());
                }else{
                    map.put("showRescue", ConstEnum.Flag.FALSE.getValue());
                }
            }

            lines.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", lines);
    }

    public static class MpLoginParam {
        public int appId;
        @NotBlank(message = "openId不能为空")
        public String openId;
        @NotBlank(message = "password不能为空")
        public String password;
    }

    /**
     * 公众号授权
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/mp_login.htm")
    public RestResult mpLogin(@Valid @RequestBody MpLoginParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        String pw = param.password;
        String makeOpenId = CodecUtils.password(param.openId);

        if (!pw.equals(makeOpenId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
        }

        Weixinmp weixinmp = weixinmpService.find(param.appId);
        if (weixinmp == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "公众号不存在");
        }

        UnionOpenId unionOpenId = UnionOpenId.newInstance(param.openId);

        Customer customer = customerService.findByMpOpenId(weixinmp.getPartnerId(), unionOpenId.openId);

        if (customer == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "客户未注册");
        }
        if (customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }
        if (customer.getAgentId() != null) {
            Agent agent = agentService.find(customer.getAgentId());
            if (agent != null) {
                if (agent.getWeixinmpId() == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置公众号");
                }
                if (!weixinmp.getId().equals(agent.getWeixinmpId())) {//当前运营商不在此公众号下
                    Weixinmp mp = weixinmpService.find(agent.getWeixinmpId());
                    if (mp != null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), String.format("请使用%s公众号", mp.getAppName()));
                    } else {
                        return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置公众号");
                    }

                }
            }
        }
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), weixinmp.getPartnerId(), TokenCache.Data.CLIENT_TYPE_MP, unionOpenId.openId, unionOpenId.secondOpenId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.MP.getValue());

        customerService.updateMpLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 生活号授权
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/fw_login.htm")
    public RestResult fwLogin(@Valid @RequestBody MpLoginParam param) {

        String pw = param.password;
        String makeOpenId = CodecUtils.password(param.openId);
        if(!pw.equals(makeOpenId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
        }

        Alipayfw alipayfw = alipayfwService.find(param.appId);
        if (alipayfw == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "生活号不存在");
        }

        UnionOpenId unionOpenId = UnionOpenId.newInstance(param.openId);
        Customer customer = customerService.findByFwOpenId(alipayfw.getPartnerId(), unionOpenId.openId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_5.getValue(), "客户未注册");
        }

        if (customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }
        if (customer.getAgentId() != null) {
            Agent agent = agentService.find(customer.getAgentId());
            if (agent != null) {
                if (agent.getAlipayfwId() == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置生活号");
                }
                if (!alipayfw.getId().equals(agent.getAlipayfwId())) {//当前运营商不在此生活号下
                    Alipayfw fw = alipayfwService.find(agent.getAlipayfwId());
                    if (fw != null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), String.format("请使用%s生活号", fw.getAppName()));
                    } else {
                        return RestResult.result(RespCode.CODE_2.getValue(), "所属运营商没有设置公众号");
                    }

                }
            }
        }

        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), alipayfw.getPartnerId(), TokenCache.Data.CLIENT_TYPE_FW, unionOpenId.openId, unionOpenId.secondOpenId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.FW.getValue());

        customerService.updateFwLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public static class BindMobileParam {
        public int appId;
        @NotBlank(message = "openId不能为空")
        public String openId;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_AUTH_CODE, message = "验证码格式错误")
        public String authCode;
    }

    /**
     * 公众号绑定手机号
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/mp_bind_mobile.htm")
    public RestResult mpBindMobile(@Valid @RequestBody BindMobileParam param) {

        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        Weixinmp weixinmp = weixinmpService.find(param.appId);
        if (weixinmp == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "公众号配置信息不存在");
        }

        UnionOpenId unionOpenId = UnionOpenId.newInstance(param.openId);
        RestResult result = customerService.mpBindMobile(weixinmp.getPartnerId(), unionOpenId.openId, param.mobile);
        if (result.getCode() != 0) {
            return result;
        }
        Customer customer = (Customer) result.getData();
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), weixinmp.getPartnerId(), TokenCache.Data.CLIENT_TYPE_MP, unionOpenId.openId, unionOpenId.secondOpenId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.MP.getValue());

        customerService.updateMpLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);

        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 小程序绑定手机号
     *
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/ma_bind_mobile.htm")
    public RestResult maBindMobile(@Valid @RequestBody BindMobileParam param) {

        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        Weixinma weixinma = weixinmaService.find(param.appId);
        if (weixinma == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "小程序配置信息不存在");
        }

        RestResult result = customerService.maBindMobile(weixinma.getPartnerId(), param.openId, param.mobile);
        if (result.getCode() != 0) {
            return result;
        }
        Customer customer = (Customer) result.getData();
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), weixinma.getPartnerId(), TokenCache.Data.CLIENT_TYPE_MA, param.openId, param.openId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.MP.getValue());

        customerService.updateMpLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);

        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 生活号绑定手机号
     * @param param
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/fw_bind_mobile.htm")
    public RestResult fwBindMobile(@Valid @RequestBody BindMobileParam param) throws IOException {


        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            log.error("fw_bind_mobile 验证码错误: {}, {}", cached, AppUtils.encodeJson(param));
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }

        Alipayfw alipayfw = alipayfwService.find(param.appId);
        if (alipayfw == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "生活号配置不存在");
        }

        UnionOpenId unionOpenId = UnionOpenId.newInstance(param.openId);
        RestResult result = customerService.fwBindMobile(alipayfw.getPartnerId(), unionOpenId.openId, param.mobile);
        if(result.getCode() != 0) {
            return result;
        }
        Customer customer = (Customer) result.getData();
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;

        String token = tokenCache.putOpenCustomer(customer.getId(), alipayfw.getPartnerId(), TokenCache.Data.CLIENT_TYPE_FW, unionOpenId.openId, unionOpenId.secondOpenId, param.appId, expireIn).token;

        customerService.updateLoginTime(customer.getId(), new Date(), ConstEnum.ClientType.FW.getValue());

        customerService.updateFwLoginToken(customer.getId(), token);

        Map data = new HashMap();
        data.put("id", customer.getId());
        data.put("token", token);
        data.put("expireIn", expireIn);

        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public static class MpUnbindMobileParam {
        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
        public String mobile;
        public Integer type;
    }

    /**
     * 公众号解绑手机号
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mp_unbind_mobile.htm")
    public RestResult mpUnbindMobile(@Valid @RequestBody MpUnbindMobileParam param) {
        TokenCache.Data tokenData = getTokenData();

        long customerId = tokenData.customerId;
        if (customerId == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户信息错误");
        }
        Customer customer = customerService.find(customerId);
        if (null == customer) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户不存在");
        } else {
            if (!param.mobile.equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_1.getValue(), "原手机号不正确");
            }
        }
        int effect = customerService.mpUnbindMobile(tokenData.customerId, customer.getPartnerId(), customer.getMpOpenId());
        if (effect == 1 && param.type == null) {
            tokenCache.remove(tokenData.token);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }else if (effect == 1 && param.type == 1) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else {
            return RestResult.result(RespCode.CODE_1.getValue(), "修改失败");
        }
    }

    /**
     * 小程序解绑手机号
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ma_unbind_mobile.htm")
    public RestResult maUnbindMobile(@Valid @RequestBody MpUnbindMobileParam param) {
        TokenCache.Data tokenData = getTokenData();

        long customerId = tokenData.customerId;
        if (customerId == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户信息错误");
        }
        Customer customer = customerService.find(customerId);
        if (null == customer) {
            return RestResult.result(RespCode.CODE_1.getValue(), "客户不存在");
        } else {
            if (!param.mobile.equals(customer.getMobile())) {
                return RestResult.result(RespCode.CODE_1.getValue(), "原手机号不正确");
            }
        }
        int effect = customerService.maUnbindMobile(tokenData.customerId, customer.getPartnerId(), customer.getMaOpenId());
        if (effect == 1) {
            tokenCache.remove(tokenData.token);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else {
            return RestResult.result(RespCode.CODE_1.getValue(), "修改失败");
        }
    }

    /**
     * 生活号解绑手机号
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fw_unbind_mobile.htm")
    public RestResult fwUnbindMobile(@Valid @RequestBody MpUnbindMobileParam param) {
        TokenCache.Data tokenData = getTokenData();

        long customerId = tokenData.customerId;
        if (customerId == 0){
            return RestResult.result(RespCode.CODE_1.getValue(), "客户信息错误");
        }
        Customer customer = customerService.find(customerId);
        if (null == customer){
            return RestResult.result(RespCode.CODE_1.getValue(), "客户不存在");
        } else {
            if (!param.mobile.equals(customer.getMobile())){
                return RestResult.result(RespCode.CODE_1.getValue(), "原手机号不正确");
            }
        }
        int effect = customerService.fwUnbindMobile(tokenData.customerId, customer.getPartnerId(), customer.getFwOpenId());
        if (effect == 1 && param.type == null) {
            tokenCache.remove(tokenData.token);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else if (effect == 1 && param.type == 1) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        } else {
            return RestResult.result(RespCode.CODE_1.getValue(), "修改失败");
        }
    }


//    public static class AllowBindMpFwParam {
//        public int appId;
//        @Pattern(regexp = Constant.VALIDATOR_PATTERN_MOBILE, message = "手机号格式错误")
//        public String mobile;
//    }
//
//    @NotLogin
//    @ResponseBody
//    @RequestMapping(value = "/allow_bind_mp_fw.htm")
//    public RestResult allowBindMpFw(@Valid @RequestBody AllowBindMpFwParam param) {
//        Customer customer = customerService.findByMobile(param.mobile);
//        if(customer == null) {
//            return RestResult.result(RespCode.CODE_0.getValue(), null);
//        } else {
//            return RestResult.result(RespCode.CODE_2.getValue(), "手机号已经在其他公众号或生活号绑定,请先解绑");
//        }
//    }


    public static class SyncCustomerInfoParam {
        public String mpOpenId;
        public String fwOpenId;
        public String nickname;
        public String photoPath;
        public String mobile;
        public String fullname;
        public String idCard;
        public Integer company;
        public Integer batteryType;
        public String idCardFace;
        public String idCardRear;
    }

    public static class QueryAuthenticationUrlParam {
        @NotBlank(message = "姓名不能为空")
        public String fullname;
        @NotBlank(message = "身份证号码不能为空")
        public String idCard;
    }

    @ResponseBody
    @RequestMapping(value = "/query_authentication_url.htm")
    public RestResult queryAuthenticationUrl(@Valid @RequestBody QueryAuthenticationUrlParam param) {
        long customerId = getTokenData().customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if (StringUtils.isNotEmpty(customer.getIcCard())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户已认证");
        }

        Customer idCard = customerService.findByIdCard(customer.getPartnerId(), param.idCard);
        if (idCard != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "身份证号已存在");
        }

        String token = null, url = null;
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "h5-customer-register"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html
        String ticketId = UUID.randomUUID().toString(); //认证ID, 由使用方指定, 发起不同的认证任务需要更换不同的认证ID
//1. 服务端发起认证请求, 获取到token
//GetVerifyToken接口文档：https://help.aliyun.com/document_detail/57050.html

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Name", param.fullname);
        binding.put("IdentificationNumber", param.idCard);

        GetVerifyTokenRequest getVerifyTokenRequest = new GetVerifyTokenRequest();
        getVerifyTokenRequest.setBiz(biz);
        getVerifyTokenRequest.setTicketId(ticketId);
        getVerifyTokenRequest.setMethod(MethodType.POST);
        getVerifyTokenRequest.setBinding(AppUtils.encodeJson2(binding));

        if (log.isDebugEnabled()) {
            log.debug("GetVerifyTokenRequest: {}", AppUtils.encodeJson2(getVerifyTokenRequest));
        }
        try {
            GetVerifyTokenResponse response = client.getAcsResponse(getVerifyTokenRequest);
            if (log.isDebugEnabled()) {
                log.debug("GetVerifyTokenResponse: {}", AppUtils.encodeJson2(response));
            }

            if (response != null && response.getSuccess()) {
                token = response.getData().getVerifyToken().getToken(); //token默认30分钟时效，每次发起认证时都必须实时获取
                url = response.getData().getCloudauthPageUrl();
                String successUrl = AppUtils.encodeUrl(String.format("%s/authentication/ok.htm?customerId=%d&ticketId=%s", appConfig.getWeixinUrl(), customerId, ticketId), Constant.ENCODING_UTF_8);
                String failUrl = AppUtils.encodeUrl(String.format("%s/authentication/ok.htm?customerId=%d&ticketId=%s", appConfig.getWeixinUrl(), customerId, ticketId), Constant.ENCODING_UTF_8);

                String redirect = String.format("&successRedirect=%s&failRedirect=%s", successUrl, failUrl);

                Map<String, String> data = new HashMap<String, String>();
                data.put("token", token);
                data.put("ticketId", ticketId);
                data.put("url", url + redirect);
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

            } else {
                log.error("GetVerifyTokenResponse: fail {}", AppUtils.encodeJson2(response));
            }

            System.out.println("GetVerifyTokenResponse: " + AppUtils.encodeJson2(response));
        } catch (ServerException e) {
            log.error("GetVerifyTokenRequest ServerException", e);
        } catch (ClientException e) {
            log.error("GetVerifyTokenRequest ClientException", e);
        }

        return RestResult.dataResult(RespCode.CODE_2.getValue(), "获取实名认证服务失败", null);
    }

    public static class QueryAuthenticationTokenParam {
        @NotBlank(message = "姓名不能为空")
        public String fullname;
        @NotBlank(message = "身份证号码不能为空")
        public String idCard;
    }

    @ResponseBody
    @RequestMapping(value = "/query_authentication_token.htm")
    public RestResult queryAuthenticationToken(@Valid @RequestBody QueryAuthenticationTokenParam param) {
        long customerId = getTokenData().customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if (StringUtils.isNotEmpty(customer.getIcCard())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户已认证");
        }

        Customer idCard = customerService.findByIdCard(customer.getPartnerId(), param.idCard);
        if (idCard != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "身份证号已存在");
        }

        String token = null, url = null;
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "app-customer-reg"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html
        String ticketId = UUID.randomUUID().toString(); //认证ID, 由使用方指定, 发起不同的认证任务需要更换不同的认证ID
//1. 服务端发起认证请求, 获取到token
//GetVerifyToken接口文档：https://help.aliyun.com/document_detail/57050.html

        Map<String, String> binding = new HashMap<String, String>();
        binding.put("Name", param.fullname);
        binding.put("IdentificationNumber", param.idCard);

        GetVerifyTokenRequest getVerifyTokenRequest = new GetVerifyTokenRequest();
        getVerifyTokenRequest.setBiz(biz);
        getVerifyTokenRequest.setTicketId(ticketId);
        getVerifyTokenRequest.setMethod(MethodType.POST);
        getVerifyTokenRequest.setBinding(AppUtils.encodeJson2(binding));

        if (log.isDebugEnabled()) {
            log.debug("GetVerifyTokenRequest: {}", AppUtils.encodeJson2(getVerifyTokenRequest));
        }
        try {
            GetVerifyTokenResponse response = client.getAcsResponse(getVerifyTokenRequest);
            if (log.isDebugEnabled()) {
                log.debug("GetVerifyTokenResponse: {}", AppUtils.encodeJson2(response));
            }

            if (response != null && response.getSuccess()) {
                token = response.getData().getVerifyToken().getToken(); //token默认30分钟时效，每次发起认证时都必须实时获取

                Map<String, String> data = new HashMap<String, String>();
                data.put("token", token);
                data.put("ticketId", ticketId);
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

            } else {
                log.error("GetVerifyTokenResponse: fail {}", AppUtils.encodeJson2(response));
            }

            System.out.println("GetVerifyTokenResponse: " + AppUtils.encodeJson2(response));
        } catch (ServerException e) {
            log.error("GetVerifyTokenRequest ServerException", e);
        } catch (ClientException e) {
            log.error("GetVerifyTokenRequest ClientException", e);
        }

        return RestResult.dataResult(RespCode.CODE_2.getValue(), "获取实名认证服务失败", null);
    }

    /*
    * 47- 查询余额
    * */
    @ResponseBody
    @RequestMapping(value = "/info_balance.htm")
    public RestResult infoBalance() {
        Long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        } else {
            int hasPayPassword = 1;
            if (StringUtils.isEmpty(customer.getPayPassword())) {
                hasPayPassword = 0;
            }
            Map map = new HashMap();
            map.put("balance", customer.getBalance());
            map.put("hasPayPassword", hasPayPassword);
            map.put("alipayAccount", customer.getAlipayAccount());
            map.put("wxOpenId", customer.getWxOpenId());
            map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
            map.put("nickname", customer.getNickname());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
    }

    /**
     * 49-设置支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SetPayPasswordParam {
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/set_pay_password.htm")
    public RestResult setPayPassword(@Valid @RequestBody SetPayPasswordParam param) {
        customerService.setPayPassword(getTokenData().customerId, param.payPassword);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 52-修改支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPasswordParam {
        @NotBlank(message = "Key不能为空")
        public String key;
        @NotBlank(message = "密码不能为空")
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password.htm")
    public RestResult UpdatePayPassword(@Valid @RequestBody UpdatePayPasswordParam param) {
        long customerId = getTokenData().customerId;

        String uuid = (String) memCachedClient.get(CacheKey.key(CacheKey.K_CUSTOMER_ID_V_UUID, customerId));
        if (!param.key.equals(uuid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密钥验证失败");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        customerService.setPayPassword(customerId, param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 52-修改支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPassword2Param {
        public String oldPassword;
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password2.htm")
    public RestResult UpdatePayPassword2(@Valid @RequestBody UpdatePayPassword2Param param) {
        long customerId = getTokenData().customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (StringUtils.isEmpty(customer.getPayPassword())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码没有设置");
        }
        if (!customer.getPayPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        customerService.setPayPassword(customerId, param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 52-检验支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckPayPasswordParam {
        public String oldPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/check_pay_password.htm")
    public RestResult checkPayPassword(@Valid @RequestBody CheckPayPasswordParam param) {
        long customerId = getTokenData().customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        NotNullMap data = new NotNullMap();
        if (!customer.getPayPassword().equals(param.oldPassword)) {
            data.putInteger("isRight", 0);
        }else{
            data.putInteger("isRight", 1);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryAuthenticationResultParam {
        public String ticketId;
    }

    @ResponseBody
    @RequestMapping(value = "/query_authentication_result.htm")
    public RestResult queryAuthenticationResult(@Valid @RequestBody QueryAuthenticationResultParam param) throws IOException {
        long customerId = getTokenData().customerId;

        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "app-customer-reg"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html

        //2. 服务端将认证URL(带token)传递给H5前端
        //3. H5前端跳转认证URL
        //4. 用户按照认证H5流程页面的指引，提交认证资料
        //5. 认证流程结束跳转指定的重定向URL(指定方法参看：https://help.aliyun.com/document_detail/58644.html?#H5Server)
        //6. 服务端查询认证状态(建议以服务端调接口确认的为准)
        //GetStatus接口文档：https://help.aliyun.com/document_detail/57049.html
        String facePic = null;
        String fullname = null;
        String idCard = null;
        String msg = null;
        int statusCode = 0; //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setBiz(biz);
        getStatusRequest.setTicketId(param.ticketId);

        if (log.isDebugEnabled()) {
            log.debug("GetStatusRequest: {}", AppUtils.encodeJson2(getStatusRequest));
        }

        try {
            GetStatusResponse response = client.getAcsResponse(getStatusRequest);
            if (log.isDebugEnabled()) {
                log.debug("GetStatusResponse: {}", AppUtils.encodeJson2(response));
            }

            if (response != null && response.getSuccess()) {
                statusCode = response.getData().getStatusCode();
                if (statusCode == 2) {
                    msg = response.getData().getAuditConclusions();
                }

            } else {
                log.debug("GetStatusResponse error: {}", AppUtils.encodeJson2(response));
            }

        } catch (ServerException e) {
            log.error("GetStatusRequest ServerException", e);
        } catch (ClientException e) {
            log.error("GetStatusRequest ClientException", e);
        }

        //7. 服务端获取认证资料
        //GetMaterials接口文档：https://help.aliyun.com/document_detail/57641.html
        GetMaterialsRequest getMaterialsRequest = new GetMaterialsRequest();
        getMaterialsRequest.setBiz(biz);
        getMaterialsRequest.setTicketId(param.ticketId);
        if(1 == statusCode || 2 == statusCode ) { //认证通过or认证不通过
            if (log.isDebugEnabled()) {
                log.debug("GetMaterialsRequest: {}", AppUtils.encodeJson2(getMaterialsRequest));
            }

            try {
                GetMaterialsResponse response = client.getAcsResponse(getMaterialsRequest);
                if (log.isDebugEnabled()) {
                    log.debug("GetMaterialsResponse: {}", AppUtils.encodeJson2(response));
                }

                if (response != null && response.getSuccess()) {
                    facePic = response.getData().getFacePic();
                    idCard = response.getData().getIdentificationNumber();
                    fullname = response.getData().getName();

                } else {
                    log.debug("GetMaterialsResponse error: {}", AppUtils.encodeJson2(response));
                }

                //后续业务处理
            } catch (ServerException e) {
                log.error("GetMaterialsResponse ServerException", e);
            } catch (ClientException e) {
                log.error("GetMaterialsResponse ClientException", e);
            }
        }

        if (statusCode == 1) { //认证通过
            customerService.updateCertification2(customerId, idCard, fullname, Customer.AuthStatus.AUDIT_PASS.getValue());

            try {
                if (StringUtils.isNotEmpty(facePic)) { //图片有
                    File tempFile = new File(appConfig.tempDir, IdUtils.uuid() + ".jpg");
                    DownloadFileUtils.download(facePic, tempFile);
                    if (tempFile.exists()) {
                        Map<String, File> fileMap = new HashMap<String, File>();
                        fileMap.put(tempFile.getName(), tempFile);

                        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_AUTH_FACE.getValue(), fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
                        if (httpResp.status / 100 == 2) {
                            List<Map> list = (List<Map>) ((Map) AppUtils.decodeJson2(httpResp.content, Map.class)).get("data");
                            String authFacePath = (String) list.get(0).get("filePath");
                            customerService.updateAuthFacePath(customerId, authFacePath);

                        } else {
                            log.error("上传附件错误1, {}", httpResp.toString());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("上传附件错误2", e);
            }

        }

        //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
        if (statusCode == -1) {
            return RestResult.result(RespCode.CODE_2.getValue(), "未认证");

        } else if (statusCode == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "认证中, 请稍后");

        } else if (statusCode == 1) {
            return RestResult.SUCCESS;

        } else if (statusCode == 2) {
            customerService.updateCertification2(customerId, null, null, Customer.AuthStatus.AUTO_FAIL.getValue());
            return RestResult.result(RespCode.CODE_2.getValue(), "认证失败");
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("未知状态, statusCode=%d", statusCode));
        }
        //常见问题：https://help.aliyun.com/document_detail/57640.html
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindBatteryParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/bind_battery.htm")
    public RestResult bindBattery(@Valid @RequestBody BindBatteryParam param) throws IOException {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
           return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        if(battery.getStatus() != Battery.Status.NOT_USE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "非空闲电池无法绑定");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        //取新电
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), battery.getAgentId());
        if(!StringUtils.isEmpty(maxCountStr)){
            maxCount = Integer.parseInt(maxCountStr);
        }
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        if(batteryList.size() >= maxCount) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已拥有最大数目的电池");
        }

/*        ShopStoreBattery shopStoreBattery = shopStoreBatteryService.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }*/

        BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, null, null, null);

        Map map = new HashMap();
        map.put("orderId", order.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AllowInstallmentParam {
        public Integer type; // 1 换电 2 租电
    }

    @ResponseBody
    @RequestMapping(value = "/allow_installment.htm")
    public RestResult allowInstallment(@Valid @RequestBody AllowInstallmentParam param){
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List list = new ArrayList();

        if (param.type == 1) {

            ExchangeInstallmentSetting exchangeSetting = exchangeInstallmentSettingService.findByMobile(customer.getMobile());
            if (exchangeSetting == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "押金分期设置不存在", list);
            }
            if (exchangeSetting.getDeadlineTime().compareTo(new Date()) < 0) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "", list);
            }

            ExchangeBatteryForegift batteryForegift = exchangeBatteryForegiftService.find(exchangeSetting.getForegiftId());
            if (batteryForegift == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "押金设置不存在", list);
            }
            if (!exchangeSetting.getForegiftMoney().equals(batteryForegift.getMoney())) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "分期押金金额不正确，请确认。", list);
            }

            PacketPeriodPrice packetPeriodPrice = packetPeriodPriceService.find(exchangeSetting.getPacketId());
            if (packetPeriodPrice == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "租金设置不存在", list);
            }
            if (!exchangeSetting.getPacketMoney().equals(packetPeriodPrice.getPrice())) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "租金金额不正确，请确认。", list);
            }

            Insurance insurance = null;
            if(exchangeSetting.getInsuranceId() != null){
                insurance = insuranceService.find(exchangeSetting.getInsuranceId());
                if (insurance == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "保险设置不存在", list);
                }
                if (!exchangeSetting.getInsuranceMoney().equals(insurance.getPrice())) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "保险金额不正确，请确认。", list);
                }
            }

            NotNullMap line = new NotNullMap();
            line.putLong("id", exchangeSetting.getId());
            line.putInteger("batteryType", exchangeSetting.getBatteryType());
            line.putString("batteryTypeName", systemBatteryTypeService.find(exchangeSetting.getBatteryType()).getTypeName());
            line.putLong("foregiftId", exchangeSetting.getForegiftId());
            line.putInteger("foregiftMoney", exchangeSetting.getForegiftMoney());
            line.putLong("packetId", exchangeSetting.getPacketId());
            line.putInteger("packetMoney", exchangeSetting.getPacketMoney());
            line.putInteger("totalMoney", exchangeSetting.getTotalMoney());
            line.putString("memo", batteryForegift.getMemo());

            if(insurance != null){
                line.putLong("insuranceId", exchangeSetting.getInsuranceId());
                line.putInteger("insurancePrice", exchangeSetting.getInsuranceMoney());
                line.putInteger("insurancePaid", insurance.getPaid());
                line.putInteger("monthCount", insurance.getMonthCount());
            }else{
                line.putLong("insuranceId", 0L);
                line.putInteger("insurancePrice", 0);
                line.putInteger("insurancePaid", 0);
                line.putInteger("monthCount", 0);
            }

            List<ExchangeInstallmentDetail> installmentDetailList =  exchangeInstallmentDetailService.findListBySettingId(exchangeSetting.getId());
            List<NotNullMap> detailList = new ArrayList<NotNullMap>();
            for (ExchangeInstallmentDetail exchangeInstallmentDetail : installmentDetailList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.putInteger("money", exchangeInstallmentDetail.getMoney());
                notNullMap.putDateTime("expireTime", exchangeInstallmentDetail.getExpireTime());
                detailList.add(notNullMap);
            }
            line.put("list", detailList);

            list.add(line);

        } else if (param.type == 2) {

            RentInstallmentSetting rentSetting = rentInstallmentSettingService.findByMobile(customer.getMobile());
            if (rentSetting == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "押金分期设置不存在", list);
            }
            if (rentSetting.getDeadlineTime().compareTo(new Date()) < 0) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "", list);
            }

            RentBatteryForegift batteryForegift = rentBatteryForegiftService.find(rentSetting.getForegiftId());
            if (batteryForegift == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "押金设置不存在", list);
            }
            if (!rentSetting.getForegiftMoney().equals(batteryForegift.getMoney())) {
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "分期押金金额不正确，请确认。", list);
            }
            RentPeriodPrice rentPeriodPrice = rentPeriodPriceService.find(rentSetting.getPacketId());
            if (rentPeriodPrice == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "租金设置不存在", list);
            }
            if (!rentSetting.getPacketMoney().equals(rentPeriodPrice.getPrice())) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "租金金额不正确，请确认。", list);
            }

            RentInsurance rentInsurance = null;
            if(rentSetting.getInsuranceId() != null ){
                rentInsurance = rentInsuranceService.find(rentSetting.getInsuranceId());
                if (rentInsurance == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "保险设置不存在", list);
                }
                if (!rentSetting.getInsuranceMoney().equals(rentInsurance.getPrice())) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "保险金额不正确，请确认。", list);
                }
            }

            NotNullMap line = new NotNullMap();
            line.putLong("id", rentSetting.getId());
            line.putInteger("batteryType", rentSetting.getBatteryType());
            line.putString("batteryTypeName", systemBatteryTypeService.find(rentSetting.getBatteryType()).getTypeName());
            line.putLong("foregiftId", rentSetting.getForegiftId());
            line.putInteger("foregiftMoney", rentSetting.getForegiftMoney());
            line.putLong("packetId", rentSetting.getPacketId());
            line.putInteger("packetMoney", rentSetting.getPacketMoney());

            line.putInteger("totalMoney", rentSetting.getTotalMoney());
            line.putString("memo", batteryForegift.getMemo());
            if(rentInsurance != null){
                line.putLong("insuranceId", rentSetting.getInsuranceId());
                line.putInteger("insurancePrice", rentSetting.getInsuranceMoney());
                line.putInteger("insurancePaid", rentInsurance.getPaid());
                line.putInteger("monthCount", rentInsurance.getMonthCount());
            }else{
                line.putLong("insuranceId", 0L);
                line.putInteger("insurancePrice", 0);
                line.putInteger("insurancePaid", 0);
                line.putInteger("monthCount", 0);
            }


            List<RentInstallmentDetail> installmentDetailList = rentInstallmentDetailService.findListBySettingId(rentSetting.getId());
            List<NotNullMap> detailList = new ArrayList<NotNullMap>();
            for (RentInstallmentDetail rentInstallmentDetail : installmentDetailList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.putInteger("money", rentInstallmentDetail.getMoney());
                notNullMap.putDateTime("expireTime", rentInstallmentDetail.getExpireTime());
                detailList.add(notNullMap);
            }
            line.put("list", detailList);

            list.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    /**
     * 67-查询客户换电租电信息
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/find_exchange_rent_info.htm")
    public RestResult bindBattery() {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Map data = new HashMap();

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        CustomerRentInfo customerRentInfo = customerRentInfoService.find(customer.getId());
        if (customerExchangeInfo != null) {
            data.put("exchangeInfo", 1);
        } else {
            data.put("exchangeInfo", 0);
        }
        if (customerRentInfo != null) {
            data.put("rentInfo", 1);
        } else {
            data.put("rentInfo", 0);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnbindAccountParam {
        public Integer type; // 1 微信 2 支付宝
    }

    @ResponseBody
    @RequestMapping(value = "/unbind_account.htm")
    public RestResult unbindAccount(@Valid @RequestBody UnbindAccountParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (param.type == 1) {
            customerService.clearWxOpenId(customerId);
        }else if(param.type == 2) {
            customerService.clearAlipayAccount(customerId);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindingAccountParam {
        public Integer type; // 1 微信 2 支付宝
        public String account;
    }

    @ResponseBody
    @RequestMapping(value = "/binding_account.htm")
    public RestResult bindingAccount(@Valid @RequestBody BindingAccountParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (param.type == 1) {
            customerService.bindingWxOpenId(customerId, param.account);
        }else if(param.type == 2) {
            customerService.bindingAlipayAccount(customerId, param.account);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryAllowInstallmentParam {
        public Integer type;
        public String cabinetId;
        public String stationId;
    }

    //59 - 查询是否允许分期
    @ResponseBody
    @RequestMapping(value = "/query_allow_installment.htm")
    public RestResult queryAllowInstallment(@Valid @RequestBody QueryAllowInstallmentParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        Map map = new HashMap();//分期设置记录
        List list = new ArrayList();
        if (param.type == 1) {
            ExchangeInstallmentSetting exchangeSetting = null;
            ExchangeInstallmentCustomer customerMobile = exchangeInstallmentCustomerService.findCustomerMobile(customer.getMobile());
            if (customerMobile == null) {
                if (StringUtils.isNotBlank(param.cabinetId)) {
                    ExchangeInstallmentCabinet cabinetId = exchangeInstallmentCabinetService.findCabinetId(param.cabinetId);
                    if (cabinetId == null) {
                        return RestResult.dataResult(RespCode.CODE_2.getValue(), "未找到此换电柜分期信息", list);
                    } else {
                        exchangeSetting = exchangeInstallmentSettingService.find(cabinetId.getSettingId());
                    }
                }else if(StringUtils.isNotBlank(param.stationId)){
                    ExchangeInstallmentStation stationId = exchangeInstallmentStationService.findStationId(param.stationId);
                    if (stationId == null) {
                        return RestResult.dataResult(RespCode.CODE_2.getValue(), "未找到此站点分期信息", list);
                    } else {
                        exchangeSetting = exchangeInstallmentSettingService.find(stationId.getSettingId());
                    }
                }else {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "未找到用户绑定分期信息", list);
                }
            } else {
                exchangeSetting = exchangeInstallmentSettingService.find(customerMobile.getSettingId());
            }
            if (exchangeSetting == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "没有分期设置", list);

            }
            if (exchangeSetting.getIsActive() ==0 || exchangeSetting.getIsActive() ==null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "分期设置未启用", list);

            }
            if (exchangeSetting.getDeadlineTime().compareTo(new Date()) < 0) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "此分期设置时间过期", list);
            }
            map.put("id",exchangeSetting.getId());
            map.put("settingType",exchangeSetting.getSettingType());
            List<ExchangeInstallmentCount> settingId = exchangeInstallmentCountService.findSettingId(exchangeSetting.getId());

            if(exchangeSetting.getSettingType()==ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue()){
                List<Map> countList = new ArrayList<Map>();//分期总表记录
                for (ExchangeInstallmentCount exchangeInstallmentCount: settingId) {
                    Map mapCount =new HashMap();
                    List<Map> countDetailList = new ArrayList<Map>();//分期记录详情
                    mapCount.put("id",exchangeInstallmentCount.getId());
                    mapCount.put("count",exchangeInstallmentCount.getCount());
                    mapCount.put("feeType",exchangeInstallmentCount.getFeeType());
                    mapCount.put("feeMoney",exchangeInstallmentCount.getFeeMoney()==0?0:exchangeInstallmentCount.getFeeMoney());
                    mapCount.put("feePercentage",exchangeInstallmentCount.getFeePercentage()==0?0:exchangeInstallmentCount.getFeePercentage());
                    mapCount.put("detailList",countDetailList);
                    countList.add(mapCount);
                }
                map.put("countList",countList);
            }else if(exchangeSetting.getSettingType()==ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue()){
                List<Map> countList = new ArrayList<Map>();//分期总表记录
                for (ExchangeInstallmentCount exchangeInstallmentCount: settingId) {
                    Map mapCount =new HashMap();
                    mapCount.put("id",exchangeInstallmentCount.getId());
                    mapCount.put("count",exchangeInstallmentCount.getCount());
                    mapCount.put("feeType",exchangeInstallmentCount.getFeeType());
                    mapCount.put("feeMoney",exchangeInstallmentCount.getFeeMoney()==0?0:exchangeInstallmentCount.getFeeMoney());
                    mapCount.put("feePercentage",exchangeInstallmentCount.getFeePercentage()==0?0:exchangeInstallmentCount.getFeePercentage());
                    List<ExchangeInstallmentCountDetail> countId = exchangeInstallmentCountDetailService.findCountId(exchangeInstallmentCount.getId());
                    List<Map> countDetailList = new ArrayList<Map>();//分期记录详情
                    if(countId.size() ==0 ){
                        Map mapCountDetait =new HashMap();
                        mapCountDetait.put("id","");
                        mapCountDetait.put("num","");
                        mapCountDetait.put("feeType","");
                        mapCountDetait.put("feeMoney","");
                        mapCountDetait.put("feePercentage","");
                        mapCountDetait.put("minForegiftPercentage","");
                        mapCountDetait.put("minPacketPeriodPercentage","");
                        countDetailList.add(mapCountDetait);
                    }
                    for (ExchangeInstallmentCountDetail exchangeInstallmentCountDetail:countId){
                        Map mapCountDetait =new HashMap();
                        mapCountDetait.put("id",exchangeInstallmentCountDetail.getId());
                        mapCountDetait.put("num",exchangeInstallmentCountDetail.getNum());
                        mapCountDetait.put("feeType",exchangeInstallmentCountDetail.getFeeType());
                        mapCountDetait.put("feeMoney",exchangeInstallmentCountDetail.getFeeMoney()==0?0:exchangeInstallmentCountDetail.getFeeMoney());
                        mapCountDetait.put("feePercentage",exchangeInstallmentCountDetail.getFeePercentage()==0?0:exchangeInstallmentCountDetail.getFeePercentage());
                        mapCountDetait.put("minForegiftPercentage",exchangeInstallmentCountDetail.getMinForegiftPercentage()==0?0:exchangeInstallmentCountDetail.getMinForegiftPercentage());
                        mapCountDetait.put("minPacketPeriodPercentage",exchangeInstallmentCountDetail.getMinPacketPeriodPercentage()==0?0:exchangeInstallmentCountDetail.getMinPacketPeriodPercentage());
                        countDetailList.add(mapCountDetait);
                    }
                    mapCount.put("detailList",countDetailList);
                    countList.add(mapCount);
                }
                map.put("countList",countList);
            }
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
        }
        return RestResult.dataResult(RespCode.CODE_2.getValue(), "未找到换电分期设置信息", list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerOfflineBatteryParam {
        public String cabinetCode;
        public String takeBoxNum;
        public String batteryCode;
        public long exchangeTime;
    }

    @ResponseBody
    @RequestMapping(value = "/customer_offline_battery.htm")
    public RestResult customerOfflineBattery(@Valid @RequestBody CustomerOfflineBatteryParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerOfflineBattery customerOfflineBattery = new CustomerOfflineBattery();
        customerOfflineBattery.setCustomerId(customer.getId());
        customerOfflineBattery.setCustomerMobile(customer.getMobile());
        customerOfflineBattery.setCustomerFullname(customer.getFullname());
        customerOfflineBattery.setCabinetCode(param.cabinetCode);
        customerOfflineBattery.setBoxNum(param.takeBoxNum);
        customerOfflineBattery.setBatteryCode(param.batteryCode);
        customerOfflineBattery.setExchangeTime(new Date(param.exchangeTime));
        int id = customerOfflineBatteryService.save(customerOfflineBattery);

        Map data = new HashMap();
        data.put("id", id);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerExchangeBatteryRecordParam {
        public String cabinetCode;
        public String takeBoxNum;
        public String takeBatteryCode;
        public String putBoxNum;
        public String putBatteryCode;
        public long exchangeTime;
    }

    @ResponseBody
    @RequestMapping(value = "/customer_exchange_battery_record.htm")
    public RestResult customerExchangeBatteryRecord(@Valid @RequestBody CustomerExchangeBatteryRecordParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerOfflineExchangeRecord customerOfflineExchangeRecord = new CustomerOfflineExchangeRecord();
        customerOfflineExchangeRecord.setCustomerId(customer.getId());
        customerOfflineExchangeRecord.setCustomerMobile(customer.getMobile());
        customerOfflineExchangeRecord.setCustomerFullname(customer.getFullname());

        customerOfflineExchangeRecord.setPutCabinetCode(param.cabinetCode);
        customerOfflineExchangeRecord.setPutBoxNum(param.putBoxNum);
        customerOfflineExchangeRecord.setPutBatteryCode(param.putBatteryCode);

        customerOfflineExchangeRecord.setTakeCabinetCode(param.cabinetCode);
        customerOfflineExchangeRecord.setTakeBoxNum(param.takeBoxNum);
        customerOfflineExchangeRecord.setTakeBatteryCode(param.takeBatteryCode);

        customerOfflineExchangeRecord.setExchangeTime(new Date(param.exchangeTime));
        customerOfflineExchangeRecordService.save(customerOfflineExchangeRecord);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
