package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyRolePerm;
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

@Controller("agent_api_v1_agentcompany_basic_user")
@RequestMapping(value = "/agent_api/v1/agentcompany/basic/user")
public class UserController extends ApiController {

	private static final Logger log = LogManager.getLogger(cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.basic.UserController.class);

	@Autowired
	MemCachedClient memCachedClient;
	@Autowired
	UserService userService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AgentService agentService;
	@Autowired
	AgentCompanyService agentCompanyService;
	@Autowired
	AgentRolePermService agentRolePermService;
	@Autowired
	AgentCompanyRolePermService agentCompanyRolePermService;

	/**
	 * 5- 查询运营公司用户个人信息
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/agent_company_user_info.htm")
	public RestResult shopUserInfo() {
		TokenCache.Data tokenData = getTokenData();
		AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
		if (agentCompany == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
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
			if (agentCompany.getPayPeopleMobile() != null && agentCompany.getPayPeopleMobile().equals(user.getMobile())) {
				map.put("isPay", ConstEnum.Flag.TRUE.getValue());
			} else {
				map.put("isPay", ConstEnum.Flag.FALSE.getValue());
			}
			if (agentCompany.getPayPeopleMobile() != null) {
				map.put("isSetPay", agentCompany.getPayPassword() != null ? 1 : 0);
			} else {
				map.put("isSetPay", ConstEnum.Flag.FALSE.getValue());
			}
			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FindUserListByAgentCompanyParam {
		@NotBlank(message = "token不能为空")
		public String customerToken;
	}

	/**
	 * 22-根据骑手查询对应运营公司的账户列表
	 *
	 */
	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/find_user_list_by_agent_company.htm")
	public RestResult findUserListByAgentCompany(@Valid @RequestBody FindUserListByAgentCompanyParam param) {

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
			userList = userService.findAgentCompanyUserListByMobile(customer.getMobile(), User.AccountType.AGENT_COMPANY.getValue());
			for (User user : userList) {
				Map map = new HashMap();
				map.put("agentCompanyName", user.getAgentCompanyName());
				map.put("loginName", user.getLoginName());
				map.put("userId", user.getId());
				mapList.add(map);
			}
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
	}

	public static class AgentCompanyLoginByIdParam {
		public int id;
		@NotBlank(message = "token不能为空")
		public String customerToken;
	}

	/**
	 * 2-运营公司用户登录(通过id)
	 */
	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/agent_company_login_by_id.htm")
	public RestResult agentCompanyLoginById(@Valid @RequestBody AgentCompanyLoginByIdParam param) {
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
		if (loginUser.getAgentCompanyId() == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
		}
		if (loginUser.getAccountType() != User.AccountType.AGENT_COMPANY.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非运营公司账号");
		}
		if (!StringUtils.equals(loginUser.getMobile(), customer.getMobile())) {
			return RestResult.result(RespCode.CODE_2.getValue(), "骑手与用户手机号不一致，请检查数据或联系管理人员");
		}
		AgentCompany agentCompany = agentCompanyService.find(loginUser.getAgentCompanyId());
		if (agentCompany.getActiveStatus() == AgentCompany.ActiveStatus.DISABLE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此运营公司被禁用不能登入");
		}

		int expireIn = MemCachedConfig.CACHE_THREE_DAY;
		String token = tokenCache.putUser(loginUser.getId(), loginUser.getAgentId(), "", 0, loginUser.getAgentCompanyId(), expireIn).token;
		if (log.isDebugEnabled()) {
			log.debug("agent_company_login_by_id userId ={} agentCompanyId={} token={}", loginUser.getId(), loginUser.getAgentCompanyId(), token);
		}

		Map map = new HashMap();

		map.put("id", loginUser.getId());
		map.put("token", token);
		map.put("expireIn", expireIn);
		map.put("fullName", loginUser.getFullname());
		map.put("agentCompanyId", agentCompany.getId());
		map.put("agentCompanyName", agentCompany.getCompanyName());
		map.put("isAdmin", loginUser.getIsAdmin());

		userService.updateLoginTime(loginUser.getId(), new Date());

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AgentCompanyLoginParam {
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
	@RequestMapping(value = "/agent_company_login.htm")
	public RestResult shopLogin(@Valid @RequestBody AgentCompanyLoginParam param) {

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
		if(user.getAgentCompanyId() == null){
			return RestResult.result(RespCode.CODE_2.getValue(), "此帐号禁止登录");
		}
		if(user.getAccountType() != User.AccountType.AGENT_COMPANY.getValue() ){
			return RestResult.result(RespCode.CODE_2.getValue(), "此帐号非运营公司账号");
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

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShopUserUpdatePasswordParam {
		public Long id;
		@NotBlank(message = "新密码不能为空")
		public String newPassword;
	}

	/**
	 * 3-运营公司用户账号修改密码
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/agent_company_user_update_password.htm")
	public RestResult agentCompanyUserUpdatePassword(@Valid @RequestBody ShopUserUpdatePasswordParam param) {
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
	public static class FindRolePerm {
		public Integer roleId;
	}

	/**
	 * 4-查询权限
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/find_role_perm.htm")
	public RestResult findRolePerm(@Valid @RequestBody FindRolePerm param) {
		List<String> permList = agentCompanyRolePermService.findAgentCompanyRoleAll(param.roleId);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, permList);
	}

}