package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg.ShopController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;

@Controller("agent_api_v1_agentcompany_basic_agent_company")
@RequestMapping("/agent_api/v1/agentcompany/basic/agent_company")
public class AgentCompanyController extends ApiController {

	static final Logger log = LogManager.getLogger(AgentCompanyController.class);
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
	AgentCompanyService agentCompanyService;
	@Autowired
	AgentCompanyDayStatsService agentCompanyDayStatsService;
	@Autowired
	PacketPeriodOrderAllotService packetPeriodOrderAllotService;
	@Autowired
	PacketPeriodOrderMapper packetPeriodOrderMapper;

	/**
	 * 6-查询运营公司首页数据
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/first_info.htm")
	public RestResult adminFirstInfo() throws ParseException {
		TokenCache.Data tokenData = getTokenData();
		long userId = tokenData.userId;
		AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
		if (agentCompany == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
		}
		NotNullMap map = new NotNullMap();
		User user = userService.find(userId);
		if (user != null) {
			map.put("id", user.getId());
			map.put("nickname", user.getNickname());
			map.put("photoPath", staticImagePath(user.getPhotoPath()));
			map.put("mobile", user.getMobile());
			map.put("loginName", user.getLoginName());
			map.put("balance", agentCompany.getBalance());
			map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
			map.put("agentCompanyRoleId", user.getAgentCompanyRoleId());

//			List<Map> statsMapList = new ArrayList<Map>();
//			List<AgentCompanyDayStats> statsList = agentCompanyDayStatsService.findByCompanyId(agentCompany.getId());
//			for (AgentCompanyDayStats agentCompanyDayStats : statsList) {
//				Map statsMap = new HashMap();
//				statsMap.put("money", agentCompanyDayStats.getMoney());
//				statsMap.put("packetPeriodCount", agentCompanyDayStats.getPacketPeriodCount());
//				String statsDate = agentCompanyDayStats.getStatsDate();
//				statsMap.put("statsDate", statsDate);
//				int ratio = agentCompanyDayStatsService.findRatio(statsDate, agentCompany.getAgentId(), agentCompany.getId(), AgentCompanyIncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
//				statsMap.put("ratio", ratio);
//				List<PacketPeriodOrderAllot> packetPeriodOrderAllotList = packetPeriodOrderAllotService.findByAgentCompany(statsDate, agentCompany.getAgentId(), agentCompany.getId(), AgentCompanyIncomeRatioHistory.OrgType.AGENT_COMPANY.getValue());
//				statsMapList.add(statsMap);
//
//				List<Map> orderMapList = new ArrayList<Map>();
//				Map orderMap = new HashMap();
//				//订单
//				for (PacketPeriodOrderAllot packetPeriodOrderAllot : packetPeriodOrderAllotList) {
//					orderMap.put("customerFullname", packetPeriodOrderAllot.getCustomerName());
//					orderMap.put("periodMoney", packetPeriodOrderAllot.getMoney());
//					orderMap.put("serviceType", packetPeriodOrderAllot.getServiceType());
//					orderMap.put("createTime", packetPeriodOrderAllot.getPayTime());
//					String customerMobile = packetPeriodOrderAllot.getCustomerMobile();
//					List<Integer> statusList = Arrays.asList(PacketPeriodOrder.Status.NOT_USE.getValue(),
//							PacketPeriodOrder.Status.USED.getValue(),
//							PacketPeriodOrder.Status.EXPIRED.getValue(),
//							PacketPeriodOrder.Status.APPLY_REFUND.getValue(),
//							PacketPeriodOrder.Status.REFUND.getValue());
//					int customerOrderCount = packetPeriodOrderMapper.findCustomerOrderCount(customerMobile, agentCompany.getId(), statusList);
//					orderMap.put("orderCount", customerOrderCount);
//					orderMapList.add(orderMap);
//				}
//				statsMap.put("orderList", orderMapList);
//			}
//			map.put("statsList", statsList);

			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}

	/**
	 * 8-查询我的运营公司
	 *
	 */
	@ResponseBody
	@RequestMapping("/my_agentcompany.htm")
	public RestResult detailList() {

		TokenCache.Data tokenData = getTokenData();

		Map result = agentCompanyService.findDetail(tokenData.agentCompanyId);
		if (result == null) {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "运营公司不存在！", result);
		}
		String[] imageFilePaths = (String[]) result.get("imagePath");
		List<Map> imagePathListMap = new ArrayList<Map>();
		//图片路径为空的就不存到数组
		for (String s : imageFilePaths) {
			if (s != null) {
				Map map = new HashMap();
				map.put("url", (staticImagePath(s)));
				imagePathListMap.add(map);
			}
		}
		result.put("imagePath", imagePathListMap);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	public static class UpdateParam {
		public String id;
		@NotBlank(message = "运营公司名称不能为空")
		public String agentCompanyName;
		public String linkname;
		public String tel;
		public String openTime;
	}

	/**
	 * 10-修改运营公司信息
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update.htm")
	public RestResult updateShop(@RequestBody UpdateParam param) {
		return agentCompanyService.update(param.id, param.agentCompanyName, param.linkname, param.tel, param.openTime);
	}

	public static class UpdatePayPeopleParam {
		public String id;
		public String authCode;
		@NotBlank(message = "收款人手机不能为空")
		public String payPeopleMobile;
		public String payPeopleName;
		public String payPeopleMpOpenId;
		public String payPeopleFwOpenId;
	}

	/**
	 * 12-修改运营公司收款人信息
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update_pay_people.htm")
	public RestResult updatePayPeople(@RequestBody UpdatePayPeopleParam param) {
		String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.payPeopleMobile));
		if (cached == null || !cached.contains(param.authCode)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
		}
		memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.payPeopleMobile));

		return agentCompanyService.updatePayPeople(param.id, param.payPeopleMobile, param.payPeopleName, param.payPeopleMpOpenId, param.payPeopleFwOpenId);
	}



}
