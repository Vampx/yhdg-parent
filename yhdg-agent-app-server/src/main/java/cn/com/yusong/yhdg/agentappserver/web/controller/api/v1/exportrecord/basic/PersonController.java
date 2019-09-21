package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.exportrecord.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.PartService;
import cn.com.yusong.yhdg.agentappserver.service.basic.PersonService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ExportRecordService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
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

@Controller("agent_api_v1_exportrecord_basic_person")
@RequestMapping(value = "/agent_api/v1/export_record/basic/person")
public class PersonController extends ApiController {

	private static final Logger log = LogManager.getLogger(PersonController.class);

	@Autowired
	MemCachedClient memCachedClient;
	@Autowired
	AgentService agentService;
	@Autowired
	ExportRecordService exportRecordService;
	@Autowired
	PersonService personService;
	@Autowired
	PartService partService;
	@Autowired
	CustomerService customerService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ExportRecordLoginParam {
		@NotBlank(message = "手机号不能为空")
		public String mobile;
		@NotBlank(message = "密码不能为空")
		public String password;
	}
	/**
	 * 1-发货人用户登录
	 *
	 * @return
	 */
	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/export_record_login.htm")
	public RestResult shopLogin(@Valid @RequestBody ExportRecordLoginParam param) {
		Map map = new HashMap();

		Person person = personService.findByMobile(param.mobile);
		if (person != null) {
			if (StringUtils.isEmpty(person.getPassword())) {
				return RestResult.result(RespCode.CODE_6.getValue(), "密码未设置");
			}
			if (!person.getPassword().equals(param.password)) {
				return RestResult.result(RespCode.CODE_2.getValue(), "密码错误");
			}
		} else {
			return RestResult.result(RespCode.CODE_2.getValue(), "手机号错误");
		}

		if (person.getIsActive() == null || person.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此账户没有激活");
		}

		//存在发货员角色
		List<Part> exportList = partService.findList(param.mobile, Part.PartType.EXPORT.getValue());
		if (exportList.size() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此账号没有发货员角色");
		}

		int expireIn = MemCachedConfig.CACHE_THREE_DAY;
		String token = tokenCache.putUser(person.getId(), 0, "", 0, "", expireIn).token;
		personService.updateLoginTime(person.getId(), new Date());

		map.put("id", person.getId());
		map.put("token", token);
		map.put("expireIn", expireIn);
		map.put("isAdmin", person.getIsAdmin());

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
	}

	public static class ExportRecordLoginByIdParam {
		public long id;
		@NotBlank(message = "token不能为空")
		public String customerToken;
	}

	/**
	 * 2-发货员用户登录(通过id)
	 */
	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/export_record_login_by_id.htm")
	public RestResult exportRecordLoginById(@Valid @RequestBody ExportRecordLoginByIdParam param) {
		String key = "customerId:" + param.customerToken;
		Long customerId = (Long) memCachedClient.get(key);
		if (customerId == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "请求参数错误");
		}
		Customer customer = customerService.find(customerId);
		if (customer == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该骑手不存在");
		}
		Person loginPerson = personService.find(param.id);
		if (loginPerson == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
		}
		if (loginPerson.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户被禁用");
		}
		List<Part> partList = partService.findList(loginPerson.getMobile(), Part.PartType.EXPORT.getValue());
		if (partList.size() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "此帐号不存在发货员角色");
		}
		if (!StringUtils.equals(loginPerson.getMobile(), customer.getMobile())) {
			return RestResult.result(RespCode.CODE_2.getValue(), "骑手与用户手机号不一致，请检查数据或联系管理人员");
		}
		int expireIn = MemCachedConfig.CACHE_THREE_DAY;
		String token = tokenCache.putUser(loginPerson.getId(), 0, "", 0, "", expireIn).token;
		if (log.isDebugEnabled()) {
			log.debug("export_record_login_by_id userId ={} token={}", loginPerson.getId(), "", token);
		}

		Map map = new HashMap();

		map.put("id", loginPerson.getId());
		map.put("fullname", loginPerson.getFullname());
		map.put("token", token);
		map.put("expireIn", expireIn);
		map.put("fullName", loginPerson.getFullname());
		map.put("isAdmin", loginPerson.getIsAdmin());

		personService.updateLoginTime(loginPerson.getId(), new Date());

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ExportRecordPersonUpdatePasswordParam {
		public Long id;
		@NotBlank(message = "新密码不能为空")
		public String newPassword;
	}

	/**
	 * 3-发货员用户账号修改密码
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/export_record_person_update_password.htm")
	public RestResult exportRecordPersonUpdatePassword(@Valid @RequestBody ExportRecordPersonUpdatePasswordParam param) {
		TokenCache.Data tokenData = getTokenData();
		Person person = personService.find(tokenData.userId);
		if (person == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
		}
		if (personService.updatePassword(getTokenData().userId, person.getPassword(), param.newPassword) == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "修改失败");
		}
		return RestResult.SUCCESS;
	}

	/**
	 * 5- 查询发货员用户个人信息
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/export_record_person_info.htm")
	public RestResult exportRecordPersonInfo() {
		TokenCache.Data tokenData = getTokenData();
		long userId = getTokenData().userId;
		Map map = new HashMap(20);
		Person person = personService.find(userId);
		if (person != null) {
			map.put("id", person.getId());
			map.put("photoPath", staticImagePath(person.getPhotoPath()));
			map.put("fullname", person.getFullname());
			map.put("mobile", person.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SendToAgentParam {
		public Integer agentId;
		public List<Map> list;
	}

	/**
	 * 7-提交发货
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/send_to_agent.htm")
	public RestResult sendToAgent(@Valid @RequestBody SendToAgentParam param) {
		TokenCache.Data tokenData = getTokenData();
		Long personId = tokenData.userId;
		Person person = personService.find(personId);
		if (person == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
		}
		if (param.agentId == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
		}
		if (param.list == null || param.list.size() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "请输入发货信息");
		}
		return exportRecordService.sendToAgent(param.agentId, param.list, personId.intValue(), person.getFullname());
	}
}
