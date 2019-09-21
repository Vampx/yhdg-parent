package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.StationService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.VipPriceService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_hdg_vip_price")
@RequestMapping("/agent_api/v1/agent/hdg/vip_price")
public class VipPriceController extends ApiController {

	@Autowired
	VipPriceService vipPriceService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AgentService agentService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	AgentCompanyService agentCompanyService;
	@Autowired
	ShopService shopService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;
	@Autowired
	StationService stationService;

	/**
	 * 117-查询vip电池类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_type_list.htm")
	public RestResult customerList() {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}

		List<AgentBatteryType> list = agentBatteryTypeService.findListByAgentId(agentId);
		List<Map> result = new ArrayList<Map>();
		for (AgentBatteryType agentBatteryType : list) {
			Map line = new HashMap();
			line.put("agentId", agentBatteryType.getAgentId());
			line.put("batteryType", agentBatteryType.getBatteryType());
			line.put("typeName", agentBatteryType.getTypeName());
			line.put("category", Battery.Category.EXCHANGE.getValue());
			result.add(line);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 98-查询VIP套餐列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return vipPriceService.list(agentId, param.keyword, param.offset, param.limit);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public Long id;
	}

	/**
	 * 99-查询VIP套餐详情
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detail.htm")
	public RestResult detail(@RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return vipPriceService.detail(param.id, agentId);
	}

	public static class CreateParam {

		public String priceName;
		public Integer batteryType;
		public Date beginTime;
		public Date endTime;
		public Integer isActive;

		public VipPacketPeriodPrice[] packetPeriodList;

		public static class VipPacketPeriodPrice {
			public Long foregiftId;
			public Integer reduceMoney;/*减免金额*/

			public GroupBizaPrice[] priceList;

			public static class GroupBizaPrice {
				public Integer dayCount;
				public Integer price;
				public Integer limitCount;
				public Integer isTicket;
				public Integer dayLimitCount;
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

		public String[] customerMobileList;
		public String[] cabinetIdList;
		public String[] stationIdList;
	}

	/**
	 * 96-新建vip套餐
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

		VipPrice vipPrice = new VipPrice();
		vipPrice.setAgentId(agentId);
		vipPrice.setPriceName(param.priceName);
		vipPrice.setBatteryType(param.batteryType);
		vipPrice.setBeginTime(param.beginTime);
		vipPrice.setEndTime(param.endTime);
		vipPrice.setIsActive(param.isActive);
		vipPrice.setCustomerCount(ConstEnum.Flag.FALSE.getValue());
		vipPrice.setCabinetCount(ConstEnum.Flag.FALSE.getValue());
		vipPrice.setCreateTime(new Date());

		return vipPriceService.create(vipPrice, agentId, param.packetPeriodList,
				param.customerMobileList,
				param.cabinetIdList,
				param.stationIdList);
	}

	public static class UpdateParam {

		public Long id;
		public String priceName;
		public Integer batteryType;
		public Date beginTime;
		public Date endTime;
		public Integer isActive;

		public VipPacketPeriodPrice[] packetPeriodList;

		public static class VipPacketPeriodPrice {
			public Long foregiftId;
			public Integer reduceMoney;/*减免金额*/

			public GroupBizaPrice[] priceList;

			public static class GroupBizaPrice {
				public Integer dayCount;
				public Integer price;
				public Integer isTicket;
				public Integer limitCount;
				public Integer dayLimitCount;
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

		public String[] customerMobileList;
		public String[] cabinetIdList;
		public String[] stationIdList;
	}

	/**
	 * 97-修改vip套餐
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
		if (vipPriceService.find(param.id) == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该套餐不存在");
		}

		VipPrice vipPrice = new VipPrice();
		vipPrice.setId(param.id);
		vipPrice.setAgentId(agentId);
		vipPrice.setPriceName(param.priceName);
		vipPrice.setBatteryType(param.batteryType);
		vipPrice.setBeginTime(param.beginTime);
		vipPrice.setEndTime(param.endTime);
		vipPrice.setIsActive(param.isActive);

		return vipPriceService.update(vipPrice, agentId, param.packetPeriodList,
				param.customerMobileList,
				param.cabinetIdList,
				param.stationIdList);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		public Long id;
	}


	/**
	 * 100-删除VIP套餐
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.htm")
	public RestResult delete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.delete(param.id);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class VipCabinetListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 102-查询VIP套餐设备列表
	 * <p>
	 */
	@ResponseBody
	@RequestMapping("/vip_cabinet_list.htm")
	public RestResult vipCabinetList(@Valid @RequestBody VipCabinetListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Cabinet> list = cabinetService.findVipCabinetList(agentId, param.keyword, param.offset, param.limit);

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
	public static class VipStationListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 185-查询VIP套餐站点列表
	 * <p>
	 */
	@ResponseBody
	@RequestMapping("/vip_station_list.htm")
	public RestResult vipStationList(@Valid @RequestBody VipStationListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Station> list = stationService.findVipStationList(agentId, param.keyword, param.offset, param.limit);

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
	public static class VipAgentCompanyListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 173-查询VIP套餐运营公司列表
	 * <p>
	 */
	@ResponseBody
	@RequestMapping("/vip_agent_company_list.htm")
	public RestResult vipCabinetList(@Valid @RequestBody VipAgentCompanyListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<AgentCompany> list = agentCompanyService.findVipAgentCompanyList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (AgentCompany agentCompany : list) {
			Map line = new HashMap();
			line.put("id", agentCompany.getId());
			line.put("companyName", agentCompany.getCompanyName());
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	/**
	 * 115-删除VIP柜子
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_cabinet_delete.htm")
	public RestResult vipCabinetDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.vipCabinetDelete(param.id);
	}

	/**
	 * 116-删除VI客户
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_customer_delete.htm")
	public RestResult vipCustomerDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.vipCustomerDelete(param.id);
	}

	/**
	 * 119-删除VI门店
	 */
/*	@ResponseBody
	@RequestMapping(value = "/vip_shop_delete.htm")
	public RestResult vipShopDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.vipShopDelete(param.id);
	}*/


	/**
	 * 184-删除VIP站点
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_station_delete.htm")
	public RestResult vipStationDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.vipStationDelete(param.id);
	}


	/**
	 * 174-删除VIP运营公司
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_agent_company_delete.htm")
	public RestResult vipAgentCompanyDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipPriceService.vipAgentCompanyDelete(param.id);
	}

}
