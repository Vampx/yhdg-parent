package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.basic.SystemBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PriceGroupService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentPriceGroupService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller("agent_api_v1_agent_zd_price_group")
@RequestMapping("/agent_api/v1/agent/zd/price_group")
public class RentPriceGroupController extends ApiController {

	@Autowired
	SystemBatteryTypeService systemBatteryTypeService;
	@Autowired
	RentPriceGroupService rentPriceGroupService;
	@Autowired
	RentBatteryTypeService rentBatteryTypeService;

	/**
	 * 152-租电查询资费组列表
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list() {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return rentPriceGroupService.list(agentId);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public Integer batteryType;
	}


	/**
	 * 153-租电查询资费组详情
	 */
	@ResponseBody
	@RequestMapping(value = "detail.htm")
	public RestResult detail(@RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return rentPriceGroupService.detail(param.batteryType, agentId);
	}

	public static class CreateParam {

		public Integer id;
		public Insurance[] insuranceList;

		public static class Insurance {
			public String insuranceName;
			public Integer price;
			public Integer isActive;
			public Integer money;
			public Integer monthCount;
			public String memo;
		}

		public GroupBiza[] packetPeriodList;

		public static class GroupBiza {
			public Integer foregift;
			public String memo;
			public GroupBizaPrice[] priceList;

			public static class GroupBizaPrice {
				public Integer dayCount;
				public Integer price;
				public String memo;
			}


		}
	}

	/**
	 * 154-租电新建资费组
	 *
	 */
	@ResponseBody
	@RequestMapping("/create.htm")
	public RestResult create(@Valid @RequestBody CreateParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}

		SystemBatteryType systemBatteryType = systemBatteryTypeService.find(param.id);
		if (systemBatteryType == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "系统电池不存在");
		}
		RentBatteryType batteryType = rentBatteryTypeService.find(systemBatteryType.getId(), agentId);
		if (batteryType != null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池类型已存在");
		}
		//运营商电池
		RentBatteryType rentBatteryType = new RentBatteryType();
		rentBatteryType.setAgentId(agentId);
		rentBatteryType.setBatteryType(param.id);
		rentBatteryType.setTypeName(systemBatteryType.getTypeName());

		return rentPriceGroupService.create(rentBatteryType, agentId, param.insuranceList, param.packetPeriodList);
	}

	public static class UpdateParam {
		public Integer batteryType;

		public Insurance[] insuranceList;

		public static class Insurance {
			public String insuranceName;
			public Integer price;
			public Integer isActive;
			public Integer money;
			public Integer monthCount;
			public String memo;
		}

		public GroupBiza[] packetPeriodList;

		public static class GroupBiza {
			public Long foregiftId;
			public Integer foregift;
			public String memo;
			public GroupBizaPrice[] priceList;

			public static class GroupBizaPrice {
				public Long priceId;
				public Integer dayCount;
				public Integer price;
				public String memo;
			}
		}

	}

	/**
	 * 155-租电修改资费组
	 *
	 */
	@ResponseBody
	@RequestMapping("/update.htm")
	public RestResult update(@RequestBody UpdateParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		if (rentBatteryTypeService.find(param.batteryType, agentId) == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "资费不存在");
		}
		SystemBatteryType systemBatteryType = systemBatteryTypeService.find(param.batteryType);
		if (systemBatteryType == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "系统电池不存在");
		}

		//运营商电池
		RentBatteryType rentBatteryType = new RentBatteryType();
		rentBatteryType.setAgentId(agentId);
		rentBatteryType.setBatteryType(param.batteryType);
		rentBatteryType.setTypeName(systemBatteryType.getTypeName());

		return rentPriceGroupService.update(rentBatteryType, agentId, param.insuranceList,param.packetPeriodList);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		public Integer batteryType;
	}

	/**
	 * 150-租电资费组删除电池类型
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_type_delete.htm")
	public RestResult batteryTypeDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return rentPriceGroupService.batteryTypeDelete(param.batteryType, agentId);
	}

	/**
	 * 156-租电删除资费组
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.htm")
	public RestResult delete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return rentPriceGroupService.delete(param.batteryType, agentId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class batteryForegiftDeleteParam {
		public Long foregiftId;
	}


	/**
	 * 151-租电资费组删除电池押金
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_foregift_delete.htm")
	public RestResult batteryForegiftDelete(@Valid @RequestBody batteryForegiftDeleteParam param) {
		return rentPriceGroupService.batteryForegiftDelete(param.foregiftId);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class batteryPeriodPriceDeleteParam {
		public Integer priceId;
	}

	/**
	 *157-租电资费组删除电池租金
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_period_price_delete.htm")
	public RestResult batteryPeriodPriceDelete(@Valid @RequestBody batteryPeriodPriceDeleteParam param) {
		return rentPriceGroupService.batteryPeriodPriceDelete(param.priceId);
	}

}
