package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.basic.SystemBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PriceGroupService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.StationService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_agent_hdg_price_group")
@RequestMapping("/agent_api/v1/agent/hdg/price_group")
public class PriceGroupController extends ApiController {

	@Autowired
	SystemBatteryTypeService systemBatteryTypeService;
	@Autowired
	PriceGroupService priceGroupService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	StationService stationService;

	/**
	 * 27-查询资费组列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list() {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return priceGroupService.list(agentId);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public Integer batteryType;
	}


	/**
	 * 41-查询资费组详情
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detail.htm")
	public RestResult detail(@RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return priceGroupService.detail(param.batteryType, agentId);
	}

	public static class CreateParam {

		public Integer id;
		public Integer isActive;
		public Integer timesPrice;

		public GroupBiza[] packetPeriodList;

		public static class GroupBiza {
			public Integer foregift;
			public String memo;
			public GroupBizaPrice[] priceList;

			public static class GroupBizaPrice {
				public Integer dayCount;
				public Integer price;
				public Integer limitCount;
				public Integer dayLimitCount;
				public Integer isTicket;
				public String memo;

				public GroupBizaPriceRenew[] priceListRenew;

				public static class GroupBizaPriceRenew {
					public Integer dayCount;
					public Integer price;
					public Integer limitCount;
					public Integer isTicket;
					public Integer dayLimitCount;
					public String memo;
				}
			}
		}

		public String[] cabinetIdList;
		public String[] stationIdList;
	}

	/**
	 * 42-新建资费组
	 *
	 * @param param
	 * @return
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
		AgentBatteryType batteryType = agentBatteryTypeService.find(systemBatteryType.getId(), agentId);
		if (batteryType != null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池类型已存在");
		}
		//运营商电池
		AgentBatteryType agentBatteryType = new AgentBatteryType();
		agentBatteryType.setAgentId(agentId);
		agentBatteryType.setBatteryType(param.id);
		agentBatteryType.setTypeName(systemBatteryType.getTypeName());

		return priceGroupService.create(agentBatteryType, agentId, param.isActive, param.timesPrice, param.packetPeriodList, param.cabinetIdList, param.stationIdList);
	}

	public static class UpdateParam {
		public Integer batteryType;
		public Integer isActive;
		public Integer timesPrice;

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
				public Integer isTicket;
				public Integer limitCount;
				public Integer dayLimitCount;
				public String memo;

				public GroupBizaPriceRenew[] priceListRenew;

				public static class GroupBizaPriceRenew {
					public Long renewPriceId;
					public Integer dayCount;
					public Integer price;
					public Integer limitCount;
					public Integer isTicket;
					public Integer dayLimitCount;
					public String memo;
				}
			}
		}
		public String[] cabinetIdList;
		public String[] stationIdList;
	}

	/**
	 * 43-修改资费组
	 *
	 * @param param
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/update.htm")
	public RestResult update(@RequestBody UpdateParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		if (agentBatteryTypeService.find(param.batteryType, agentId) == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "资费不存在");
		}
		SystemBatteryType systemBatteryType = systemBatteryTypeService.find(param.batteryType);
		if (systemBatteryType == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "系统电池不存在");
		}

		//运营商电池
		AgentBatteryType agentBatteryType = new AgentBatteryType();
		agentBatteryType.setAgentId(agentId);
		agentBatteryType.setBatteryType(param.batteryType);
		agentBatteryType.setTypeName(systemBatteryType.getTypeName());

		return priceGroupService.update(agentBatteryType, agentId, param.isActive, param.timesPrice,param.packetPeriodList, param.cabinetIdList, param.stationIdList);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		public Integer batteryType;
	}

	/**
	 * 38-删除电池类型
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_type_delete.htm")
	public RestResult batteryTypeDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return priceGroupService.batteryTypeDelete(param.batteryType, agentId);
	}

	/**
	 * 44-删除资费组
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.htm")
	public RestResult delete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return priceGroupService.delete(param.batteryType, agentId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryForegiftDeleteParam {
		public long foregiftId;
	}


	/**
	 * 38-删除电池押金
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_foregift_delete.htm")
	public RestResult batteryForegiftDelete(@Valid @RequestBody BatteryForegiftDeleteParam param) {
		return priceGroupService.batteryForegiftDelete(param.foregiftId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryPeriodPriceDeleteParam {
		public Integer priceId;
	}


	/**
	 * 45-删除电池租金
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_period_price_delete.htm")
	public RestResult batteryPeriodPriceDelete(@Valid @RequestBody BatteryPeriodPriceDeleteParam param) {
		return priceGroupService.batteryPeriodPriceDelete(param.priceId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 180-查询(电池型号)套餐设备列表
	 */
	@ResponseBody
	@RequestMapping("/cabinet_list.htm")
	public RestResult cabinetList(@Valid @RequestBody CabinetListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Cabinet> list = cabinetService.findBatteryCabinetList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Cabinet cabinet : list) {
			Map line = new HashMap();
			line.put("id", cabinet.getId());
			line.put("cabinetName", cabinet.getCabinetName());
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetDeleteParam {
		public Integer batteryType;
		public String cabinetId;
	}

	/**
	 * 181-删除电池型号(套餐)设备
	 */
	@ResponseBody
	@RequestMapping(value = "/cabinet_delete.htm")
	public RestResult cabinetDelete(@Valid @RequestBody CabinetDeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return priceGroupService.cabinetDelete(param.cabinetId, param.batteryType);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StationListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 182-查询电池型号(套餐)站点列表
	 */
	@ResponseBody
	@RequestMapping("/station_list.htm")
	public RestResult stationList(@Valid @RequestBody StationListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Station> list = stationService.findBatteryStationList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Station station : list) {
			Map line = new HashMap();
			line.put("id", station.getId());
			line.put("stationName", station.getStationName());
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StationDeleteParam {
		public Integer batteryType;
		public String stationId;
	}

	/**
	 * 183-删除电池型号(套餐)站点
	 */
	@ResponseBody
	@RequestMapping(value = "/station_delete.htm")
	public RestResult stationDelete(@Valid @RequestBody StationDeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return priceGroupService.stationDelete(param.stationId, param.batteryType);
	}
}
