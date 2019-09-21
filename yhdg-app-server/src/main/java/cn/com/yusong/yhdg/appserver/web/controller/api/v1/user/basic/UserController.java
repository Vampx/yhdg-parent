
package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.basic;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.UserService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.FaultLogService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/9.
 */
@Controller("api_v1_user_basic_user")
@RequestMapping(value = "/api/v1/user/basic/user")
public class UserController extends ApiController {

    final static Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    UserService userService;
    @Autowired
    CabinetService  cabinetService;
    @Autowired
    FaultLogService faultLogService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginParam {
        @NotBlank(message = "帐号不能为空")
        public String loginName;
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
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        Map map = new HashMap();

        User user = userService.findByLoginName(param.loginName);
        if (user != null) {
            if (StringUtils.isEmpty(user.getPassword())) {
                return RestResult.result(RespCode.CODE_6.getValue(), "密码未设置");
            }
            if (!user.getPassword().equals(param.password)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
            }
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "登录帐号错误");
        }

        if (user.getIsActive() == null || user.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户没有激活");
        }

        if(user.getAgentId() == null ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
        }

        //是否调度人员
        List<Cabinet> list = cabinetService.findList(user.getId());
        if(list.isEmpty()){
            return RestResult.result(RespCode.CODE_2.getValue(), "调度员未绑定终端，无法登录！");
        }


        int expireIn = MemCachedConfig.CACHE_TWO_HOUR;
        String token = tokenCache.putUser(user.getId(), user.getAgentId(), expireIn).token;

        map.put("id", user.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginByAuthCodeParam {
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



        Map map = new HashMap();
        User user = userService.findByLoginName(param.mobile);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户不存在");
        }
        if (user.getIsActive() == null || user.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }

        int expireIn = MemCachedConfig.CACHE_TWO_HOUR;
        String token = tokenCache.putUser(user.getId(), user.getAgentId(), expireIn).token;


        map.put("id", user.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PasswordParam {
        @NotBlank(message = "旧密码不能为空")
        public String oldPassword;
        @NotBlank(message = "新密码不能为空")
        public String newPassword;

    }
    @ResponseBody
    @RequestMapping(value = "/update_password")
    public RestResult password(@Valid @RequestBody PasswordParam param) {

        if (StringUtils.isEmpty(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码不能为空");
        }

        if (StringUtils.isEmpty(param.newPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "新密码不能为空");
        }

        if (userService.updatePassword(getTokenData().userId,
                                        param.oldPassword,
                                        param.newPassword) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        return RestResult.SUCCESS;
    }

    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult getInfo() {
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;
        Map map = new HashMap(20);
        User user = userService.find(userId);
        if(user != null) {
            map.put("id", user.getId());
            map.put("roleName", "调度员");
            map.put("fullname", user.getFullname());
            map.put("mobile", user.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            map.put("photoPath", staticImagePath(user.getPhotoPath()));
            map.put("areaName", "");
            map.put("permission", "运维调度");
            return RestResult.dataResult(RespCode.CODE_0.getValue(),"", map);
        } else {
            return RestResult.dataResult(RespCode.CODE_1.getValue(),"此用户不存在！",null);
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateInfoParam {
        public String photoPath;
    }

    @ResponseBody
    @RequestMapping(value = "/update_info.htm")
    public RestResult updateInfo(@Valid @RequestBody UpdateInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }

        userService.updateInfo(tokenData.userId, param.photoPath);
        return RestResult.SUCCESS;
    }


    /**
     * 以下 是 进入  调度首页 预览
     */
    @ResponseBody
    @RequestMapping(value = "/index_data.htm")
    public RestResult getIndex(){
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;
        return  userService.getIndex(userId);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReportPushTokenParam {
        public Integer pushType;
        public String pushToken;
    }

    @ResponseBody
    @RequestMapping(value = "/report_push_token.htm")
    public RestResult reportPushToken(@Valid @RequestBody ReportPushTokenParam param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }
        return userService.updatePushToken(tokenData.userId, param.pushType, param.pushToken);
    }
}
