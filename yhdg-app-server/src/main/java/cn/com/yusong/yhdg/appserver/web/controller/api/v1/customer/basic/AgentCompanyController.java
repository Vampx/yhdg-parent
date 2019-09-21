package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller("api_v1_customer_basic_agent_company")
@RequestMapping(value = "/api/v1/customer/basic/agent_company")
public class AgentCompanyController extends ApiController {

	@Autowired
	MemCachedClient memCachedClient;
	@Autowired
	CustomerService customerService;
	@Autowired
	AgentCompanyService agentCompanyService;
	@Autowired
	AgentCompanyCustomerService agentCompanyCustomerService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RegisterAgentCompanyParam {
		public int type;
		public String openId;
		public String agentCompanyId;
		public String mobile;
		public String authCode;
	}

	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/register_agent_company")
	public RestResult registerLaxin(@Valid @RequestBody RegisterAgentCompanyParam param) {
		AgentCompany agentCompany = agentCompanyService.find(param.agentCompanyId);
		if (agentCompany == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
		}

		String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
		if (cached == null || !cached.contains(param.authCode)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
		}
		memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));

		List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerService.findByMobile(param.mobile);
		if (agentCompanyCustomerList.size() > 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "手机号绑定过运营公司");
		}

		agentCompanyCustomerService.bindCustomer(agentCompany, param.mobile, param.openId, param.type);

		return RestResult.result(RespCode.CODE_0.getValue(), "成功注册运营公司骑手");
	}
}
