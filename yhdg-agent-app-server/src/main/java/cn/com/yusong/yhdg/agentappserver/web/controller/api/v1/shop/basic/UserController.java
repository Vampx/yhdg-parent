package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.ShopRolePermService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.User;
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

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_shop_basic_user")
@RequestMapping(value = "/agent_api/v1/shop/basic/user")
public class UserController extends ApiController {

    private static final Logger log = LogManager.getLogger(UserController.class);

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
    ShopRolePermService shopRolePermService;

    /**
     * 92- 查询门店用户个人信息
     *
     */
    @ResponseBody
    @RequestMapping(value = "/shop_user_info.htm")
    public RestResult shopUserInfo() {
        TokenCache.Data tokenData = getTokenData();
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        long userId = getTokenData().userId;
        Map map = new HashMap(20);
        User user = userService.find(userId);
        if (user != null) {
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("photoPath", staticImagePath(user.getPhotoPath()));
            map.put("mobile", user.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            map.put("loginName", user.getLoginName());
            if (shop.getPayPeopleMobile() != null && shop.getPayPeopleMobile().equals(user.getMobile())) {
                map.put("isPay", ConstEnum.Flag.TRUE.getValue());
            } else {
                map.put("isPay", ConstEnum.Flag.FALSE.getValue());
            }
            if (shop.getPayPeopleMobile() != null) {
                map.put("isSetPay", shop.getPayPassword() != null ? 1 : 0);
            } else {
                map.put("isSetPay", ConstEnum.Flag.FALSE.getValue());
            }
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
        } else {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FindUserListByShopParam {
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 93-根据骑手查询对应门店的账户列表
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/find_user_list_by_shop.htm")
    public RestResult findUserListByShop(@Valid @RequestBody FindUserListByShopParam param) {

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
            userList = userService.findShopUserListByMobile(customer.getMobile(), User.AccountType.SHOP.getValue());
            for (User user : userList) {
                Map map = new HashMap();
                map.put("shopName", user.getShopName());
                map.put("loginName", user.getLoginName());
                map.put("userId", user.getId());
                mapList.add(map);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
    }

    public static class ShopLoginByIdParam {
        public int id;
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 94- 门店用户登录(通过id)
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/shop_login_by_id.htm")
    public RestResult shopLoginById(@Valid @RequestBody ShopLoginByIdParam param) {
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
        if(loginUser.getShopId() == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
        }
        if(loginUser.getAccountType() != User.AccountType.SHOP.getValue() ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非门店账号");
        }
        if (!StringUtils.equals(loginUser.getMobile(), customer.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "骑手与用户手机号不一致，请检查数据或联系管理人员");
        }
        Shop shop = shopService.find(loginUser.getShopId());
        if (shop.getActiveStatus() == Shop.ActiveStatus.DISABLE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "此门店被禁用不能登入");
        }

        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        String token = tokenCache.putUser(loginUser.getId(), loginUser.getAgentId(), loginUser.getShopId(), 0, "", expireIn).token;

        if (log.isDebugEnabled()) {
            log.debug("shop_login_by_id userId ={} shopId={} token={}", loginUser.getId(), loginUser.getShopId(), token);
        }

        Map map = new HashMap();

        map.put("id", loginUser.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        map.put("fullName", loginUser.getFullname());
        map.put("shopId", shop.getId());
        map.put("shopName", shop.getShopName());
        map.put("isAllowOpenBox", shop.getIsAllowOpenBox() != null && shop.getIsAllowOpenBox() == 1 ? 1 : 0);
        map.put("isAdmin", loginUser.getIsAdmin());

        userService.updateLoginTime(loginUser.getId(), new Date());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopLoginParam {
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
    @RequestMapping(value = "/shop_login.htm")
    public RestResult shopLogin(@Valid @RequestBody ShopLoginParam param) {

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
        if(user.getShopId() == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
        }
        if(user.getAccountType() != User.AccountType.SHOP.getValue() ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非门店账号");
        }
        Shop shop = shopService.find(user.getShopId());
        if (shop.getActiveStatus() == Shop.ActiveStatus.DISABLE.getValue()){
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

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopUserUpdatePasswordParam {
        public Long id;
        @NotBlank(message = "新密码不能为空")
        public String newPassword;
    }

    /**
     * 99-门店用户账号修改密码
     *
     */
    @ResponseBody
    @RequestMapping(value = "/shop_user_update_password.htm")
    public RestResult shopUserUpdatePassword(@Valid @RequestBody ShopUserUpdatePasswordParam param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (userService.updatePassword(getTokenData().userId, user.getPassword(), param.newPassword) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "修改失败");
        }
        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FindShopRolePerm {
        public Integer roleId;
    }

    /**
     * 132-查询权限
     *
     */
    @ResponseBody
    @RequestMapping(value = "/find_role_perm.htm")
    public RestResult findRolePerm(@Valid @RequestBody FindShopRolePerm param) {
        List<String> permList = shopRolePermService.findShopRoleAll(param.roleId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, permList);
    }
}
