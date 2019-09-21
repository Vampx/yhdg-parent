package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.EstateService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
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
import sun.management.resources.agent;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_basic_user")
@RequestMapping(value = "/agent_api/v1/agent/basic/user")
public class UserController extends ApiController {

    static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentService agentService;
    @Autowired
    ShopService shopService;
    @Autowired
    AgentCompanyService agentCompanyService;
    @Autowired
    EstateService estateService;
    @Autowired
    AgentRolePermService agentRolePermService;
    @Autowired
    AgentPermService agentPermService;
    @Autowired
    LaxinService laxinService;
    @Autowired
    PersonService personService;
    @Autowired
    PartService partService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class findUserListParam {
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 0-根据骑手查询对应运营商的账户列表
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/find_user_list_by_customer.htm")
    public RestResult findUserList(@Valid @RequestBody findUserListParam param) {

        String key = "customerId:" + param.customerToken;
        Long customerId = (Long) memCachedClient.get(key);
        if (customerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "请求参数错误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该骑手不存在");
        }
        List<User> userList;
        List<Map> mapList = new ArrayList<Map>();
        if (customer.getMobile() != null) {
            userList = userService.findListByMobile(customer.getMobile(), User.AccountType.AGENT.getValue());
            for (User user : userList) {
                Map map = new HashMap();
                map.put("agentName", user.getAgentName());
                map.put("loginName", user.getLoginName());
                map.put("userId", user.getId());
                mapList.add(map);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class findTypeUserListParam {
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 0-根据骑手查询对应的账户类型列表
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/find_type_user_list.htm")
    public RestResult findTypeUserList(@Valid @RequestBody findTypeUserListParam param) {

        String key = "customerId:" + param.customerToken;
        Long customerId = (Long) memCachedClient.get(key);
        if (customerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "请求参数错误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该骑手不存在");
        }
        List<User> userList;
        List<Map> mapList = new ArrayList<Map>();
        Person person = personService.findByMobile(customer.getMobile());
        if (person != null) {
            List<Part> partList = partService.findList(person.getMobile(), Part.PartType.EXPORT.getValue());
            if (partList.size() > 0) {
                Map map = new HashMap();
                map.put("accountType", Part.PartType.EXPORT.getValue());
                map.put("personId", person.getId());
                map.put("mobile", person.getMobile());
                map.put("partName", partList.get(0).getPartName());
                mapList.add(map);
            }
        }

        if (customer.getMobile() != null) {
            userList = userService.findTypeUserList(customer.getMobile());
            for (User user : userList) {
                Map map = new HashMap();
                map.put("agentId", user.getAgentId());
                map.put("agentName", user.getAgentName());
                map.put("shopId", user.getShopId());
                map.put("shopName", user.getShopName());
                map.put("agentCompanyId", user.getAgentCompanyId());
                map.put("agentCompanyName", user.getAgentCompanyName());
                map.put("estateId", user.getEstateId());
                map.put("estateName", user.getEstateName());
                map.put("accountType", user.getAccountType());
                map.put("loginName", user.getLoginName());
                map.put("userId", user.getId());
                mapList.add(map);
            }
            List<Laxin> laxinList = laxinService.findByMobilePartner(customer.getPartnerId(), customer.getMobile());
            for (Laxin laxin : laxinList) {
                Map line = new HashMap();
                line.put("accountType", User.AccountType.LAXIN.getValue());
                line.put("laxinId", laxin.getId());
                line.put("agentName", laxin.getAgentName());
                line.put("agentCode", laxin.getAgentCode());
                mapList.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
    }

    public static class LoginByIdParam {
        public int id;
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 1- 用户登录(通过id)
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/login_by_id.htm")
    public RestResult loginById(@Valid @RequestBody LoginByIdParam param) {


        String key = "customerId:" + param.customerToken;
        Long customerId = (Long) memCachedClient.get(key);
        if (customerId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "请求参数错误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该骑手不存在");
        }

        User loginUser = userService.find(param.id);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (loginUser.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户被禁用");
        }
        if(loginUser.getAccountType() != User.AccountType.AGENT.getValue() ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非运营商账号");
        }
        if (!StringUtils.equals(loginUser.getMobile(), customer.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "骑手与用户手机号不相等，请检查数据或联系管理人员");
        }
        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent.getIsActive() == ConstEnum.Flag.FALSE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "此运营商被禁用不能登入");
        }

        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        String token = tokenCache.putUser(loginUser.getId(), loginUser.getAgentId(), "", 0, "", expireIn).token;

        Map map = new HashMap();

        map.put("id", loginUser.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        map.put("fullName", loginUser.getFullname());
        map.put("agentId", agent.getId());
        map.put("agentName", agent.getAgentName());
        map.put("roleId", (loginUser.getRoleId() == null) ? null : loginUser.getRoleId());

        userService.updateLoginTime(loginUser.getId(), new Date());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginParam {
        @NotBlank(message = "用户名不能为空")
        public String loginName;
        @NotBlank(message = "密码不能为空")
        public String password;
    }

    /**
     * 1-用户登录
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/login.htm")
    public RestResult login(@Valid @RequestBody LoginParam param) {
        User user = null;
        Map map = new HashMap();

        Person person = personService.findByMobile(param.loginName);
        if (person != null) {
            List<Part> partList = partService.findList(person.getMobile(), Part.PartType.EXPORT.getValue());
            if (partList.size() > 0) {
                if(person.getIsActive() == ConstEnum.Flag.FALSE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
                }
                int expireIn = MemCachedConfig.CACHE_THREE_DAY;
                String token = tokenCache.putUser(person.getId(), 0, "", 0, "", expireIn).token;
                personService.updateLoginTime(person.getId(), new Date());
                map.put("id", person.getId());
                map.put("fullname", person.getFullname());
                map.put("token", token);
                map.put("expireIn", expireIn);
                map.put("isAdmin", person.getIsAdmin());
                map.put("accountType", Part.PartType.EXPORT.getValue());
            }
        }else{
            user = userService.findByLoginName(param.loginName);
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

            if (user.getAccountType() == User.AccountType.AGENT.getValue()) {
                if(user.getAgentId() == null){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
                }

                Agent agent = agentService.find(user.getAgentId());
                if (agent.getIsActive() == ConstEnum.Flag.FALSE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此运营商被禁用不能登入");
                }

                int expireIn = MemCachedConfig.CACHE_THREE_DAY;
                String token = tokenCache.putUser(user.getId(), user.getAgentId(), "", 0, "", expireIn).token;

                userService.updateLoginTime(user.getId(), new Date());

                map.put("id", user.getId());
                map.put("token", token);
                map.put("expireIn", expireIn);
                map.put("agentId", agent.getId());
                map.put("agentName", agent.getAgentName());
                map.put("roleId", (user.getRoleId() == null) ? null : user.getRoleId());

            } else if (user.getAccountType() == User.AccountType.SHOP.getValue()) {
                if(user.getShopId() == null){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
                }
                Shop shop = shopService.find(user.getShopId());
                if (shop.getActiveStatus() == Shop.ActiveStatus.DISABLE.getValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "此门店被禁用不能登入");
                }
                int expireIn = MemCachedConfig.CACHE_THREE_DAY;
                String token = tokenCache.putUser(user.getId(), user.getAgentId(), user.getShopId(), 0, "", expireIn).token;

                userService.updateLoginTime(user.getId(), new Date());

                map.put("id", user.getId());
                map.put("token", token);
                map.put("expireIn", expireIn);
                map.put("shopId", shop.getId());
                map.put("shopName", shop.getShopName());
                map.put("isAllowOpenBox", shop.getIsAllowOpenBox() != null && shop.getIsAllowOpenBox() == 1 ? 1 : 0);
                map.put("isAdmin", user.getIsAdmin());

            } else if (user.getAccountType() == User.AccountType.AGENT_COMPANY.getValue()) {
                if(user.getAgentCompanyId() == null){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
                }
                AgentCompany agentCompany = agentCompanyService.find(user.getAgentCompanyId());
                if (agentCompany.getActiveStatus() == AgentCompany.ActiveStatus.DISABLE.getValue()){
                    return RestResult.result(RespCode.CODE_2.getValue(), "此运营公司被禁用不能登入");
                }

                int expireIn = MemCachedConfig.CACHE_THREE_DAY;
                String token = tokenCache.putUser(user.getId(), user.getAgentId(), "", 0, user.getAgentCompanyId(), expireIn).token;
                userService.updateLoginTime(user.getId(), new Date());

                map.put("id", user.getId());
                map.put("token", token);
                map.put("expireIn", expireIn);
                map.put("agentCompanyId", agentCompany.getId());
                map.put("agentCompanyName", agentCompany.getCompanyName());
                map.put("isAdmin", user.getIsAdmin());
            } else if (user.getAccountType() == User.AccountType.ESTATE.getValue()) {
                if (user.getEstateId() == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
                }
                Estate estate = estateService.find(user.getEstateId());
                if (estate.getIsActive() == Estate.IsActive.DISABLE.getValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "此物业被禁用不能登入");
                }

                int expireIn = MemCachedConfig.CACHE_THREE_DAY;
                String token = tokenCache.putUser(user.getId(), user.getAgentId(), "", user.getEstateId(), "", expireIn).token;

                userService.updateLoginTime(user.getId(), new Date());

                map.put("id", user.getId());
                map.put("token", token);
                map.put("fullName", user.getFullname());
                map.put("expireIn", expireIn);
                map.put("estateId", estate.getId());
                map.put("estateName", estate.getEstateName());
                map.put("isAdmin", user.getIsAdmin());

            } else {
                return RestResult.result(RespCode.CODE_2.getValue(), "账号类型错误");
            }
        }
        if (user != null) {
            map.put("accountType", user.getAccountType());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    public static class ForgetPasswordParam {
        public String mobile;
        public String password;
        public String authCode;
    }

    /**
     * 2-用户忘记密码
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/forget_password.htm")
    public RestResult forgetPassword(@RequestBody ForgetPasswordParam param) {

        String mobile = param.mobile;
        String password = param.password;
        String authCode = param.authCode;

        if (StringUtils.isEmpty(mobile)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密码不能为空");
        }

        if (StringUtils.isEmpty(authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码不能为空");
        }

        User user  = userService.findByMobile(mobile);
        if (user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "没有绑定手机号");
        }

        String cacheKey = CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, mobile);
        String cached = (String) memCachedClient.get(cacheKey);

        if (cached == null || !cached.contains(authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        userService.updatePassword2(user.getId(), password);
        memCachedClient.delete(cacheKey);
        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateInfoParam {
        public String mobile;
        public String authCode;
    }

    /**
     * 3- 修改手机号
     *
     */
    @ResponseBody
    @RequestMapping(value = "/update_mobile.htm")
    public RestResult updateInfo(@Valid @RequestBody UpdateInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }
        if (StringUtils.isEmpty(param.mobile)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号不能为空");
        }
        if (StringUtils.isEmpty(user.getMobile())) {
            return RestResult.result(RespCode.CODE_1.getValue(), "没有绑定手机号");
        }

        String cacheKey = CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile);
        String cached = (String) memCachedClient.get(cacheKey);
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }

        if (userService.updateMobile(user.getId(), param.mobile) == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "修改失败");
        }
        memCachedClient.delete(cacheKey);
        return RestResult.result(RespCode.CODE_0.getValue(), "修改成功");
    }

    /**
     * 4- 查询运营商用户个人信息
     *
     */
    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult getInfo() {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        long userId = getTokenData().userId;
        Map map = new HashMap(20);
        User user = userService.find(userId);
        if (user != null) {
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("photoPath", staticImagePath(user.getPhotoPath()));
            map.put("mobile", user.getMobile());
            map.put("loginName", user.getLoginName());
            if (agent.getPayPeopleMobile() != null && agent.getPayPeopleMobile().equals(user.getMobile())) {
                map.put("isPay", ConstEnum.Flag.TRUE.getValue());
            } else {
                map.put("isPay", ConstEnum.Flag.FALSE.getValue());
            }
            if (agent.getPayPeopleMobile() != null) {
                map.put("isSetPay", agent.getPayPassword() != null ? 1 : 0);
            } else {
                map.put("isSetPay", ConstEnum.Flag.FALSE.getValue());
            }
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
        } else {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePasswordParam {
        @NotBlank(message = "旧密码不能为空")
        public String oldPassword;
        @NotBlank(message = "新密码不能为空")
        public String newPassword;
    }

    /**
     * 5-用户修改密码
     *
     */
    @ResponseBody
    @RequestMapping(value = "/update_password")
    public RestResult updatePassword(@Valid @RequestBody UpdatePasswordParam param) {

        if (userService.updatePassword(getTokenData().userId, param.oldPassword, param.newPassword) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    /**
     * 19-查询人员列表
     *
     */
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<User> userList = userService.findList(agentId, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (User user : userList) {
            Map line = new HashMap();
            line.put("id", user.getId());
            line.put("loginName", user.getLoginName());
            line.put("mobile", user.getMobile());
            line.put("fullname", user.getFullname());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FindRolePerm {
        public Integer roleId;
    }

    /**
     * 134-查询权限
     *
     */
    @ResponseBody
    @RequestMapping(value = "/find_role_perm.htm")
    public RestResult findRolePerm(@Valid @RequestBody FindRolePerm param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        List<String> permList;
        if (user.getIsProtected() == ConstEnum.Flag.TRUE.getValue()) {
            permList = agentPermService.findAllByClientType();
        }else {
            permList = agentRolePermService.findAll(param.roleId);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, permList);
    }

}
