package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.zd.VipRentPriceService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.CabinetController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.VipPriceController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_zd_vip_price")
@RequestMapping("/agent_api/v1/agent/zd/vip_price")
public class VipRentPriceController extends ApiController {

	@Autowired
	VipRentPriceService vipRentPriceService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AgentService agentService;
	@Autowired
	ShopService shopService;
	@Autowired
	RentBatteryTypeService rentBatteryTypeService;
	@Autowired
	AgentCompanyService agentCompanyService;
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

		List<RentBatteryType> list = rentBatteryTypeService.findListByAgentId(agentId);
		List<Map> result = new ArrayList<Map>();
		for (RentBatteryType rentBatteryType : list) {
			Map line = new HashMap();
			line.put("agentId", rentBatteryType.getAgentId());
			line.put("batteryType", rentBatteryType.getBatteryType());
			line.put("typeName", rentBatteryType.getTypeName());
			line.put("category", Battery.Category.RENT.getValue());
			result.add(line);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ForegiftListParam {
		public Integer batteryType;
	}

	/**
	 * 120-查询VIP电池押金
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "foregift_list.htm")
	public RestResult cabinetForegiftList(@RequestBody ForegiftListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return vipRentPriceService.foregiftList(param.batteryType,agentId);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 27-查询资费组列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return vipRentPriceService.list(agentId, param.keyword, param.offset, param.limit);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public Long id;
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
		return vipRentPriceService.detail(param.id, agentId);
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
				public String memo;
			}
		}

		public String[] customerMobileList;
		public String[] shopIdList;
		public String[] agentCompanyIdList;
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

		VipRentPrice vipRentPrice = new VipRentPrice();
		vipRentPrice.setAgentId(agentId);
		vipRentPrice.setPriceName(param.priceName);
		vipRentPrice.setBatteryType(param.batteryType);
		vipRentPrice.setBeginTime(param.beginTime);
		vipRentPrice.setEndTime(param.endTime);
		vipRentPrice.setIsActive(param.isActive);
		vipRentPrice.setCustomerCount(ConstEnum.Flag.FALSE.getValue());
		vipRentPrice.setCreateTime(new Date());

		return vipRentPriceService.create(vipRentPrice, agentId, param.packetPeriodList,
				param.customerMobileList,
				param.shopIdList,
				param.agentCompanyIdList);
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
				public Integer limitCount;
				public Integer dayLimitCount;
				public String memo;
			}
		}

		public String[] customerMobileList;
		public String[] shopIdList;
		public String[] agentCompanyIdList;
	}

	/**
	 * 43-修改vip套餐
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
		if (vipRentPriceService.find(param.id) == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该套餐不存在");
		}

		VipRentPrice vipRentPrice = new VipRentPrice();
		vipRentPrice.setId(param.id);
		vipRentPrice.setAgentId(agentId);
		vipRentPrice.setPriceName(param.priceName);
		vipRentPrice.setBatteryType(param.batteryType);
		vipRentPrice.setBeginTime(param.beginTime);
		vipRentPrice.setEndTime(param.endTime);
		vipRentPrice.setIsActive(param.isActive);

		return vipRentPriceService.update(vipRentPrice, agentId, param.packetPeriodList,
				param.customerMobileList,
				param.shopIdList,
				param.agentCompanyIdList);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		public Long id;
	}


	/**
	 * 44-删除资费组
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.htm")
	public RestResult delete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipRentPriceService.delete(param.id);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class VipShopListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 103-查询门店
	 * <p>
	 */
	@ResponseBody
	@RequestMapping("/vip_shop_list.htm")
	public RestResult vipShopList(@Valid @RequestBody VipShopListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Shop> list = shopService.findZdVipShopList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Shop shop : list) {
			Map line = new HashMap();
			line.put("id", shop.getId());
			line.put("shopName", shop.getShopName());
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
	 * 103-查询运营公司
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
		return vipRentPriceService.vipCustomerDelete(param.id);
	}

	/**
	 * 117-删除VI门店
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_shop_delete.htm")
	public RestResult vipShopDelete(@Valid @RequestBody DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipRentPriceService.vipShopDelete(param.id);
	}

	/**
	 * 118-删除VIP运营公司
	 */
	@ResponseBody
	@RequestMapping(value = "/vip_agent_company_delete.htm")
	public RestResult vipAgentCompanyDelete(@Valid @RequestBody VipPriceController.DeleteParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return vipRentPriceService.vipAgentCompanyDelete(param.id);
	}
}
