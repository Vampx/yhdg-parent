package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.estate.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.EstateService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
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

@Controller("agent_api_v1_estate_basic_user")
@RequestMapping(value = "/agent_api/v1/estate/basic/user")
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
    EstateService estateService;


    /**
     * 1- 查询物业用户个人信息
     *
     */
    @ResponseBody
    @RequestMapping(value = "/estate_user_info.htm")
    public RestResult estateUserInfo() {
        TokenCache.Data tokenData = getTokenData();
        Estate estate = estateService.find(tokenData.estateId);
        if (estate == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "物业不存在");
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
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
        } else {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FindUserListByEstateParam {
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 2-根据骑手查询对应物业的账户列表
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/find_user_list_by_estate.htm")
    public RestResult findUserListByEstate(@Valid @RequestBody FindUserListByEstateParam param) {

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
            userList = userService.findEstateUserListByMobile(customer.getMobile(), User.AccountType.ESTATE.getValue());
            for (User user : userList) {
                Map map = new HashMap();
                map.put("estateName", user.getEstateName());
                map.put("loginName", user.getLoginName());
                map.put("userId", user.getId());
                mapList.add(map);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
    }

    public static class EstateLoginByIdParam {
        public int id;
        @NotBlank(message = "token不能为空")
        public String customerToken;
    }

    /**
     * 3- 物业用户登录(通过id)
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/estate_login_by_id.htm")
    public RestResult estateLoginById(@Valid @RequestBody EstateLoginByIdParam param) {
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
        if(loginUser.getEstateId() == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
        }
        if(loginUser.getAccountType() != User.AccountType.ESTATE.getValue() ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非物业账号");
        }
        if (!StringUtils.equals(loginUser.getMobile(), customer.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "骑手与用户手机号不一致，请检查数据或联系管理人员");
        }
        Estate estate = estateService.find(loginUser.getEstateId());
        if (estate.getIsActive() == Estate.IsActive.DISABLE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "此物业被禁用不能登入");
        }

        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        String token = tokenCache.putUser(loginUser.getId(), loginUser.getAgentId(), "", loginUser.getEstateId(), "", expireIn).token;

        if (log.isDebugEnabled()) {
            log.debug("estate_login_by_id userId ={} estateId={} token={}", loginUser.getId(), loginUser.getEstateId(), token);
        }

        Map map = new HashMap();

        map.put("id", loginUser.getId());
        map.put("token", token);
        map.put("expireIn", expireIn);
        map.put("fullName", loginUser.getFullname());
        map.put("estateId", estate.getId());
        map.put("estateName", estate.getEstateName());
        map.put("isAdmin", loginUser.getIsAdmin());

        userService.updateLoginTime(loginUser.getId(), new Date());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EstateLoginParam {
        @NotBlank(message = "用户名不能为空")
        public String loginName;
        @NotBlank(message = "密码不能为空")
        public String password;
    }

    /**
     * 4-用户登录
     *
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/estate_login.htm")
    public RestResult estateLogin(@Valid @RequestBody EstateLoginParam param) {

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
        if(user.getEstateId() == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
        }
        if(user.getAccountType() != User.AccountType.ESTATE.getValue() ){
            return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非物业账号");
        }
        Estate estate = estateService.find(user.getEstateId());
        if (estate.getIsActive() == Estate.IsActive.DISABLE.getValue()){
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

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


}
