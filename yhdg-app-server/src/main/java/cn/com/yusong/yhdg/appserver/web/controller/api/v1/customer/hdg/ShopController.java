package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopStoreBatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zd_shop")
@RequestMapping(value = "/api/v1/customer/zd/shop")
public class ShopController extends ApiController {

	static final Logger log = LogManager.getLogger(ShopController.class);

	@Autowired
	ShopService shopService;
	@Autowired
	CustomerService customerService;
	@Autowired
	RentBatteryForegiftService rentBatteryForegiftService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	RentPeriodPriceService rentPeriodPriceService;
	@Autowired
	SystemBatteryTypeService systemBatteryTypeService;
	@Autowired
	ShopStoreBatteryService shopStoreBatteryService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NearestParam {
		public double lng;
		public double lat;
		public Integer areaId;
		public String keyword;
		public Integer offset;
		public Integer limit;
	}

	@NotLogin
	@ResponseBody
	@RequestMapping("nearest.htm")
	public RestResult searchByKeyword(@RequestBody NearestParam param) {
		if (param == null || param.lng == 0 || param.lat == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
		}

		Area province = null, city = null;
		if (param.areaId != null) {
			Area area = areaCache.get(param.areaId);
			if (area == null) {
				return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
			}
			if (area.getAreaLevel() == 3) {
				city = areaCache.get(area.getParentId());
			} else if (area.getAreaLevel() == 2) {
				city = area;
			} else if (area.getAreaLevel() == 1) {
				province = area;
			}
		}
		if (city != null) {
			province = areaCache.get(city.getParentId());
			if (province == null) {
				return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
			}
		}
		if (province == null) {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
		}
		String geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat);
		List<Shop> list = null;

		list = shopService.findNearest(geoHash, param.lng, param.lat, city.getId(), param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Shop shop : list) {
			Map line = new HashMap();
			line.put("id", shop.getId());
			line.put("shopName", shop.getShopName());
			line.put("address", shop.getAddress());
			line.put("lng", shop.getLng());
			line.put("lat", shop.getLat());
			line.put("tel", shop.getTel());
			line.put("distance", shop.getDistance());

			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NearestInfoParam {
		public Integer customerId;
		public Integer areaId;
		public double lng;
		public double lat;
	}

	@NotLogin
	@ResponseBody
	@RequestMapping("/nearest_info.htm")
	public RestResult nearestInfo(@RequestBody NearestInfoParam param) {
		TokenCache.Data tokenData = getTokenData();
		Area area = areaCache.get(param.areaId);
		if (area == null) {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
		}

		Area province = null, city = null;
		if (area.getAreaLevel() == 3) {
			city = areaCache.get(area.getParentId());
		} else if (area.getAreaLevel() == 2) {
			city = area;
		}

		province = areaCache.get(city.getParentId());
		if (province == null) {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
		}

		List<Integer> agentId = null;
		String geoHash = null;
		if (getTokenData() != null) {
			Customer customer = null;
			if (param.customerId != null) {
				customer = customerService.find(param.customerId);
			} else {
				customer = customerService.find(getTokenData().customerId);
			}

			if (customer.getIsWhiteList() != ConstEnum.Flag.TRUE.getValue()) {
				geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
			}
		} else {
			geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
		}
		//根据城市查找最近的地址，如果没有，查询城市外最近的地址
		List<Shop> list = shopService.findNearestInfo(
				geoHash,
				param.lng,
				param.lat,
				city.getId(),
				null,
				0,
				1);

		if (list.size() == 0) {
			list = shopService.findNearestInfo(
					geoHash,
					param.lng,
					param.lat,
					null,
					null,
					0,
					1);
		}

		Map line = new HashMap();

		if (list.size() > 0) {
			Shop shop = list.get(0);
			line.put("id", shop.getId());
			line.put("shopName", shop.getShopName());
			line.put("address", shop.getAddress());
			line.put("lng", shop.getLng());
			line.put("lat", shop.getLat());
			line.put("tel", shop.getTel());
			line.put("distance", shop.getDistance());

			RentBatteryForegift rentBatteryForegift = rentBatteryForegiftService.findOneByAgentId(shop.getAgentId());
			if (rentBatteryForegift == null) {
				line.put("foregift", null);
				line.put("batteryTypeName", null);
			} else {
				line.put("foregift", rentBatteryForegift.getMoney());
				SystemBatteryType systemBatteryType = systemBatteryTypeService.find(rentBatteryForegift.getBatteryType());
				if (systemBatteryType != null) {
					line.put("batteryTypeName", systemBatteryType.getTypeName());
				} else {
					return RestResult.dataResult(RespCode.CODE_1.getValue(), "电池类型不存在", null);
				}
			}

			List<ShopStoreBattery> shopStoreBatteryList = shopStoreBatteryService.findByShopId(shop.getId(), ShopStoreBattery.Category.RENT.getValue());
			line.put("rentCount", shopStoreBatteryList.size());

			List priceList = new ArrayList();
			Long foregiftId = null;
			if (rentBatteryForegift != null) {
				foregiftId = rentBatteryForegift.getId();
			}
			List<RentPeriodPrice> packetPeriodPriceList = rentPeriodPriceService.findList(shop.getAgentId(), null, foregiftId);
			for (RentPeriodPrice rentPeriodPrice : packetPeriodPriceList) {
				Map map = new HashMap();
				map.put("dayCount", rentPeriodPrice.getDayCount());
				map.put("price", rentPeriodPrice.getPrice());
				priceList.add(map);
			}
			line.put("packetPeriodList", priceList);
		}else{
			//附近无库存电池门店，直接返回最近的门店信息，资费信息设置为null
			//根据城市查找最近的地址，如果没有，查询城市外最近的地址
			List<Shop> shopList = shopService.findNearestInfo(
					geoHash,
					param.lng,
					param.lat,
					city.getId(),
					null,
					0,
					1);

			if (shopList.size() == 0) {
				shopList = shopService.findNearestInfo(
						geoHash,
						param.lng,
						param.lat,
						null,
						null,
						0,
						1);
			}
			if (shopList.size() > 0) {
				Shop shop = list.get(0);
				line.put("id", shop.getId());
				line.put("shopName", shop.getShopName());
				line.put("address", shop.getAddress());
				line.put("lng", shop.getLng());
				line.put("lat", shop.getLat());
				line.put("tel", shop.getTel());
				line.put("distance", shop.getDistance());
				line.put("batteryTypeName", null);
				line.put("foregift", null);
				line.put("rentCount", null);
				List priceList = new ArrayList();
				Map map = new HashMap();
				map.put("dayCount", null);
				map.put("price", null);
				priceList.add(map);
				line.put("packetPeriodList", priceList);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
	}

	@ResponseBody
	@RequestMapping(value = "/province_city_list.htm")
	public RestResult provinceCityList() {
		return shopService.findAddressList();
	}

}
