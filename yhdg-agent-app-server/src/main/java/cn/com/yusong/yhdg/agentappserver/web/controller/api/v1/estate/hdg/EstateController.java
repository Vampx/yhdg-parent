package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.estate.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.basic.CustomerController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.zd.RentOrderController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_estate_hdg_estate")
@RequestMapping("/agent_api/v1/estate/hdg/estate")
public class EstateController extends ApiController {

	static final Logger log = LogManager.getLogger(EstateController.class);
	@Autowired
	MemCachedClient memCachedClient;
	@Autowired
	EstateService estateService;
	@Autowired
    UserService userService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	CabinetOperateLogService cabinetOperateLogService;
	@Autowired
	CabinetDayStatsService cabinetDayStatsService;
	@Autowired
	CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
	@Autowired
	CabinetCodeService cabinetCodeService;
	@Autowired
	CustomerService customerService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	AgentService agentService;
	@Autowired
	CabinetDegreeInputService cabinetDegreeInputService;
	/**
	 * 5-查询物业首页数据
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/first_info.htm")
	public RestResult getInfo() {
		TokenCache.Data tokenData = getTokenData();
		long userId = tokenData.userId;
		Estate estate = estateService.find(tokenData.estateId);
		if (estate == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "物业不存在");
		}
		Map map = new HashMap(20);
		User user = userService.find(userId);
		if (user != null) {
			map.put("id", user.getId());
			map.put("nickname", user.getNickname());
			map.put("photoPath", staticImagePath(user.getPhotoPath()));
			map.put("mobile", user.getMobile());
			map.put("loginName", user.getLoginName());
			map.put("balance", estate.getBalance());
			map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));

			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}

	/**
	 * 8-查询物业余额支付密码
	 */
	@ResponseBody
	@RequestMapping(value = "/info_balance.htm")
	public RestResult infoBalance() {
		TokenCache.Data tokenData = getTokenData();
		Estate estate = estateService.find(tokenData.estateId);
		if (estate == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "物业不存在");
		}
		User loginUser = userService.find(tokenData.userId);
		if (loginUser == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
		}
		if (loginUser.getIsAdmin() != ConstEnum.Flag.TRUE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该用户非物业核心管理员");
		}
		Agent agent = agentService.find(loginUser.getAgentId());
		if (agent == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
		}
		Customer customer = customerService.findByMobile(agent.getPartnerId(), estate.getPayPeopleMobile());

		int hasPayPassword = 1;
		if (StringUtils.isEmpty(estate.getPayPassword())) {
			hasPayPassword = 0;
		}
		Map map = new HashMap();
		map.put("balance", estate.getBalance());//物业余额
		map.put("hasPayPassword", hasPayPassword);
		map.put("alipayAccount", estate.getPayPeopleFwOpenId());
		map.put("nickname", customer == null ? "" : customer.getNickname());
		map.put("fullname", customer == null ? "" : customer.getFullname());
		map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
	}


	/**
	 * 10-设置物业提现支付密码
	 */

	public static class SetPayPassword {
		public String payPassword;
	}

	@ResponseBody
	@RequestMapping(value = "/set_pay_password.htm")
	public RestResult setPayPassword(@Valid @RequestBody SetPayPassword param) {
		TokenCache.Data tokenData = getTokenData();
		User loginUser = userService.find(tokenData.userId);
		if (loginUser == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
		}
		Estate estate = estateService.find(tokenData.estateId);
		if (estate == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"物业不存在",null);
		}
		estateService.setPayPassword(estate.getId(), param.payPassword);
		return RestResult.result(RespCode.CODE_0.getValue(), null);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class QueryEstateDegreeRecordParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	@ResponseBody
	@RequestMapping(value = "/query_estate_degree_record.htm")
	public RestResult query_estate_degree_record(@Valid @RequestBody QueryEstateDegreeRecordParam param) {
		TokenCache.Data tokenData = getTokenData();
		Estate estate = estateService.find(tokenData.estateId);
		if (estate == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "物业不存在");
		}
		int agentId = getTokenData().agentId;

		List<CabinetDegreeInput> list = cabinetDegreeInputService.findListByEstate(agentId, estate.getId(), param.offset, param.limit);

		List<NotNullMap> result = new ArrayList<NotNullMap>();
		for (CabinetDegreeInput cabinetDegreeInput : list) {
			NotNullMap line = new NotNullMap();
			line.put("statsDate", DateFormatUtils.format(cabinetDegreeInput.getCreateTime(), Constant.DATE_FORMAT));
			line.put("beginTime", DateFormatUtils.format(cabinetDegreeInput.getBeginTime(), Constant.DATE_TIME_FORMAT));
			line.put("endTime", DateFormatUtils.format(cabinetDegreeInput.getEndTime(), Constant.DATE_TIME_FORMAT));
			line.put("beginNum", cabinetDegreeInput.getBeginNum());
			line.put("endNum", cabinetDegreeInput.getEndNum());
			line.put("num", cabinetDegreeInput.getDegree());
			line.put("price", cabinetDegreeInput.getDegreeMoney()*100);//电费
			line.put("unitPrice", cabinetDegreeInput.getDegreePrice()*100);//单价
			line.put("cabinetId", cabinetDegreeInput.getCabinetId());
			line.put("cabinetName", cabinetDegreeInput.getCabinetName());
			line.put("status", cabinetDegreeInput.getStatus());

			result.add(line);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}
}
