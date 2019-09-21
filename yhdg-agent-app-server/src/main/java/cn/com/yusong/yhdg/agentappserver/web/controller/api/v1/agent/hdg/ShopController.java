package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.ShopInOutMoneyService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.*;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_hdg_shop")
@RequestMapping("/agent_api/v1/agent/hdg/shop")
public class ShopController extends ApiController {
	@Autowired
    ShopService shopService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	BatteryParameterService batteryParameterService;
	@Autowired
	BatteryOrderService batteryOrderService;
	@Autowired
	PacketPeriodOrderService packetPeriodOrderService;
	@Autowired
	CustomerService customerService;
	@Autowired
	CustomerExchangeInfoService customerExchangeInfoService;
	@Autowired
	InsuranceOrderService insuranceOrderService;
	@Autowired
	ShopStoreBatteryService shopStoreBatteryService;
	@Autowired
	ShopInOutMoneyService shopInOutMoneyService;
	@Autowired
	BatteryOrderAllotService batteryOrderAllotService;
	@Autowired
	PacketPeriodOrderAllotService packetPeriodOrderAllotService;
	@Autowired
	RentPeriodOrderAllotService rentPeriodOrderAllotService;
	@Autowired
	CustomerRentInfoService customerRentInfoService;
	@Autowired
	RentForegiftOrderService rentForegiftOrderService;
	@Autowired
	RentPeriodOrderService rentPeriodOrderService;
	@Autowired
	RentInsuranceOrderService rentInsuranceOrderService;
	@Autowired
	CabinetBoxService cabinetBoxService;

	/**
	 * 136-查询电池列表
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShopBatteryListParam {
		public String shopId;
		public String keyword;
		public int type;
		public int category;
		public int offset;
		public int limit;
	}

	@ResponseBody
	@RequestMapping(value = "/shop_battery_list.htm")
	public RestResult batteryList(@Valid @RequestBody ShopBatteryListParam param ) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		List<Battery> list = new ArrayList<Battery>();
		//查询门店电池
		if (param.shopId != null) {
			if (param.type == 1) { //客户使用中
				list = batteryService.shopCustomerUseList(param.shopId, param.keyword, param.offset, param.limit);
			} else if (param.type == 2) { //柜子中
				list = batteryService.shopCabinetList(param.shopId, param.keyword, param.offset, param.limit);
			} else if (param.type == 3) { //门店中
				list = batteryService.shopStoreList(param.shopId, param.keyword, param.offset, param.limit);
			}
		}

		List<Map> data = new ArrayList<Map>();
		for (Battery battery : list) {
			Map line = new HashMap();
			line.put("id", battery.getId());
			String batteryTypeName = batteryService.getBatteryTypeName(battery.getType());
			line.put("typeName", batteryTypeName);
			line.put("code", battery.getCode());
			line.put("status", battery.getStatus());
			line.put("volume", battery.getVolume());
			line.put("customerFullname", battery.getCustomerFullname());
			line.put("customerMobile", battery.getCustomerMobile() == null ? null : battery.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
			line.put("isOnline", battery.getIsOnline());
			line.put("isNormal", battery.getIsNormal());
			line.put("chargeStatus", battery.getChargeStatus());
			line.put("upLineStatus", battery.getUpLineStatus());
			line.put("shellCode", battery.getShellCode());
			line.put("signalType", battery.getSignalType());
			line.put("currentSignal", battery.getCurrentSignal());
			line.put("category", battery.getCategory());
			data.add(line);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryDetailParam {
		public String shopId;
		public String batteryId;
	}

	/**
	 * 137-查询门店电池详情
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/battery_detail.htm")
	public RestResult batteryDetail(@Valid @RequestBody BatteryDetailParam param ) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}

		Shop shop = null;
		if (param.shopId != null) {
			shop = shopService.find(param.shopId);
			if (shop == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
			}
		}
		Battery battery = batteryService.find(param.batteryId);
		if (battery == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"电池不存在",null);
		}
		BatteryParameter batteryParameter = batteryParameterService.find(param.batteryId);

		NotNullMap result = new NotNullMap();
		result.put("id",battery.getId());
		result.put("category",battery.getCategory());
		result.put("code", battery.getCode());
		result.put("volume",battery.getVolume() == null ? 0 : battery.getVolume());
		result.put("status", battery.getStatus());
		result.put("shellCode", battery.getShellCode());
		result.put("signalType", battery.getSignalType());
		result.put("currentSignal",battery.getCurrentSignal());
		result.put("category", battery.getCategory());
		result.put("isOnline", battery.getIsOnline());
		result.put("isNormal", battery.getIsNormal());
		result.put("boxNum", battery.getBoxNum());
		result.put("cabinetId", battery.getCabinetId());
		result.put("abnormalCause", battery.getAbnormalCause());//异常标识原因
		result.put("cabinetId", battery.getCabinetId());
		result.put("operatorTime", battery.getOperatorTime() == null ? "" :DateFormatUtils.format(battery.getOperatorTime(), Constant.DATE_TIME_FORMAT));
		if (battery.getCabinetId() != null) {
			result.put("cabinetName", cabinetService.find(battery.getCabinetId()).getCabinetName());
		} else {
			result.put("cabinetName", "");
		}
		result.put("boxNum", battery.getBoxNum()!= null? battery.getBoxNum() : "");

		if (battery.getUpLineTime() != null) {
			result.put("upLineTime", DateFormatUtils.format(battery.getUpLineTime(), Constant.DATE_TIME_FORMAT));
		} else {
			result.put("upLineTime", "");
		}
		if (battery.getUpLineTime() != null) {
			result.put("useDay", InstallUtils.getUseDay(battery.getUpLineTime()));
		} else {
			result.put("useDay", 0);
		}
		if (StringUtils.isNotEmpty(battery.getBoxNum()) && StringUtils.isNotEmpty(battery.getCabinetId())) {
			CabinetBox cabinetBox = cabinetBoxService.find(battery.getCabinetId(), battery.getBoxNum());
			if (cabinetBox != null) {
				result.put("isActive", cabinetBox.getIsActive());
				result.put("forbiddenCause", cabinetBox.getForbiddenCause()); //禁用原因
			}
		} else {
			result.put("isActive", null);
			result.put("forbiddenCause", null);
		}
		result.put("isCustomer", battery.getCustomerId() != null ? 1 : 0);

		if (param.shopId != null) {
			result.put("shopId", shop.getId());
			result.put("shopName", shop.getShopName());
		}
		List<BatteryOrder> list = batteryOrderService.findBatteryListByCustomer(agentId, param.batteryId, battery.getCustomerId());
		if (list.size() > 0) {
			BatteryOrder batteryOrder = list.get(0);
			result.put("takeCabinetId", batteryOrder.getTakeCabinetId());
			result.put("takeCabinetName", batteryOrder.getTakeCabinetName());
			result.put("takeBoxNum", batteryOrder.getTakeBoxNum());
			result.putDateTime("takeTime", batteryOrder.getTakeTime());
			result.put("putCabinetId", batteryOrder.getPutCabinetId());
			result.put("putCabinetName", batteryOrder.getPutCabinetName());
			result.putDateTime("putTime", batteryOrder.getPutTime());
		}else {
			result.put("takeCabinetId", null);
			result.put("takeCabinetName", null);
			result.put("takeBoxNum", null);
			result.putDateTime("takeTime", null);
			result.put("putCabinetId", null);
			result.put("putCabinetName", null);
			result.putDateTime("putTime", null);
		}
		String batteryTypeName = batteryService.getBatteryTypeName(battery.getType());
		result.put("totalCapacity", (battery.getTotalCapacity() != null && battery.getTotalCapacity() > 0) ? battery.getTotalCapacity(): batteryParameter.getNominalCapacity());//额定容量
		result.put("circle", battery.getCircle() != null ? battery.getCircle(): batteryParameter.getCircle());//循环次数
		result.put("batteryTypeName", batteryTypeName);//电池类型
		result.put("voltage", battery.getVoltage());//电压V
		result.put("electricity", battery.getElectricity());//电流A
		result.put("nominalCapacity", battery.getCurrentCapacity() != null ? battery.getCurrentCapacity() : batteryParameter.getCurrentCapacity());//剩余容量
		result.put("volume", battery.getVolume()!= null ? battery.getVolume() : batteryParameter.getVolume());//soc %
		List<String> tempList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(battery.getTemp())) {
			String[] temps = battery.getTemp().split(",");
			for (int i = 0; i < temps.length; i++) {
				tempList.add(temps[i]);
			}
		}

		int maxVoltage = 0;
		int minVoltage = 0;
		int voltageRange = 0;

		List<Map> mapList = new ArrayList<Map>();
		if (StringUtils.isNotEmpty(battery.getSingleVoltage())) {
			String[] singleVoltages = battery.getSingleVoltage().split(",");
			List<Integer> voltageList = new ArrayList<Integer>();
			for (int i = 0; i < singleVoltages.length; i++) {
				voltageList.add(Integer.parseInt(singleVoltages[i]));
			}
			minVoltage = (int) Collections.min(voltageList);
			maxVoltage = (int) Collections.max(voltageList);
			voltageRange = maxVoltage - minVoltage;
			int i = 1;
			for (Integer voltage : voltageList) {
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put("no", i);
				map.put("voltage", voltage);
				map.put("maxVoltageRange", maxVoltage - voltage);
				map.put("minVoltageRange", voltage - minVoltage);
				mapList.add(map);
				i++;
			}
		}
		if (mapList.size() == 0) {
			if (batteryParameter != null && StringUtils.isNotEmpty(batteryParameter.getSingleVoltage())) {
				String[] singleVoltages = batteryParameter.getSingleVoltage().split(",");
				List<Integer> voltageList = new ArrayList<Integer>();
				for (int i = 0; i < singleVoltages.length; i++) {
					voltageList.add(Integer.parseInt(singleVoltages[i]));
				}
				minVoltage = (int) Collections.min(voltageList);
				maxVoltage = (int) Collections.max(voltageList);
				voltageRange = maxVoltage - minVoltage;
			}
		}


		result.put("tempList", tempList);//温度

		result.put("realVoltageDiff", voltageRange); //当前压差 单位mV
		result.put("maxVoltage", maxVoltage);//最高电压 V
		result.put("minVoltage", minVoltage);//最低电压 V

		result.put("signalType", Battery.SignalType.getName(battery.getSignalType()));//网络类型
		result.put("currentSignal", battery.getCurrentSignal() != null ? battery.getCurrentSignal() : batteryParameter.getCurrentSignal());//信号
		result.put("lngLat", battery.getLng()+"/"+battery.getLat());//经纬度
		if (battery.getLng() == null || battery.getLat() == null) {
			if (batteryParameter != null) {
				result.put("lngLat", batteryParameter.getLng()+"/"+batteryParameter.getLat());//经纬度
			}
		}
		if (battery != null && battery.getFet() != null) {
			result.put("fetStatus", battery.getFetName());//充电状态
		}else{
			if (batteryParameter != null) {
				result.put("fetStatus", batteryParameter.getMosName());
			}
		}


		if (battery.getCustomerId() != null) {
			Customer customer = customerService.find(battery.getCustomerId());
			if (customer == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
			}
			result.put("customerFullname", customer.getFullname());
			result.put("customerMobile", customer.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));

			if (battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
				String leavingDay = null;
				long now = System.currentTimeMillis();
				PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findLastEndTime(customer.getId());
				if (packetPeriodOrder != null) {
					if (now < packetPeriodOrder.getEndTime().getTime()) {
						leavingDay = AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - now) / 1000);
					}
				}
				CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
				InsuranceOrder insuranceOrder = null;
				if (customerExchangeInfo != null) {
					result.put("foregiftMoney", customerExchangeInfo.getForegift());
					Integer batteryType = customerExchangeInfo.getBatteryType();
					insuranceOrder = insuranceOrderService.findByCustomerId(customer.getId(), batteryType, InsuranceOrder.Status.PAID.getValue());
					result.put("insuranceMoney", insuranceOrder != null ? insuranceOrder.getPrice() : 0);
					result.put("insurancePaid", insuranceOrder != null ? insuranceOrder.getPaid() : 0);
					result.put("monthCount", insuranceOrder != null ? insuranceOrder.getMonthCount() : 0);
				} else {
					result.put("foregiftMoney", 0);
					result.put("insuranceMoney", 0);
					result.put("insurancePaid", 0);
					result.put("monthCount", 0);
				}
				if (packetPeriodOrder!= null) {
					result.put("packetPeriodMoney", packetPeriodOrder.getPrice());
					result.put("dayCount", packetPeriodOrder.getDayCount());
					result.putDate("beginTime", packetPeriodOrder.getBeginTime());
					result.putDate("endTime", packetPeriodOrder.getEndTime());
					result.put("leavingDay", leavingDay);
				} else {
					result.put("packetPeriodMoney", 0);
					result.put("dayCount", 0);
					result.put("beginTime", null);
					result.put("endTime", null);
					result.put("leavingDay", "");
				}
			} else {
				CustomerRentInfo customerRentInfo = customerRentInfoService.find(customer.getId());
				if(customerRentInfo == null){
					return RestResult.result(RespCode.CODE_2.getValue(), "未关联租电信息");
				}

				RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(customerRentInfo.getForegiftOrderId());
				if(rentForegiftOrder == null){
					return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
				}

				RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findLastEndTime(customer.getId());
				RentInsuranceOrder rentInsuranceOrder = null;
				if (rentPeriodOrder != null) {
					rentInsuranceOrder = rentInsuranceOrderService.findByCustomerId(
							customer.getId(),
							rentPeriodOrder.getBatteryType(),
							RentInsuranceOrder.Status.PAID.getValue());
				}

				result.put("foregiftMoney", rentForegiftOrder.getMoney());
				result.put("packetPeriodMoney", rentPeriodOrder != null ? rentPeriodOrder.getPrice() : 0);
				result.putDate("packetBeginTime", rentPeriodOrder != null ? rentPeriodOrder.getBeginTime() : null);
				result.putDate("packetEndTime", rentPeriodOrder != null ? rentPeriodOrder.getEndTime() : null);
				result.put("packetRestDay", rentPeriodOrder != null ? InstallUtils.getRestDay(rentPeriodOrder.getEndTime()) : 0);

				result.put("insuranceMoney", rentInsuranceOrder != null ? rentInsuranceOrder.getPrice() : 0);
				result.put("insurancePaid", rentInsuranceOrder != null ? rentInsuranceOrder.getPaid() : 0);
				result.put("monthCount", rentInsuranceOrder != null ? rentInsuranceOrder.getMonthCount() : 0);
			}
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	/**
	 * 138-查询门店列表
	 * @return
	 */

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShopListParam {
		public Integer offset;
		public Integer limit;
		public String keyword;
		public double lat;
		public double lng;
	}

	@ResponseBody
	@RequestMapping(value = "/shop_list.htm")
	public RestResult shopList(@Valid @RequestBody ShopListParam param) {
		TokenCache.Data tokenData = getTokenData();
		List<Shop> list = shopService.findByAgentId(tokenData.agentId, param.keyword, param.lat, param.lng, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		if (list != null){
			for (Shop shop : list){
				Map line = new HashMap();
				line.put("id", shop.getId());
				line.put("workTime", shop.getWorkTime());
				int shopStoreBattery = shopStoreBatteryService.findCount(shop.getId());
				line.put("shopStoreBattery", shopStoreBattery);
				line.put("distance", shop.getDistance());
				line.put("shopName", shop.getShopName());
				line.put("lat", shop.getLat());
				line.put("lng", shop.getLng());
				line.put("address", shop.getAddress());
				result.add(line);
			}
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	/*
	* 139-查询门店收入
	* */

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShopIncomeParam {
		public String date;
		public String shopId;
	}
	@ResponseBody
	@RequestMapping(value = "/shop_income.htm")
	public RestResult list(@Valid @RequestBody ShopIncomeParam param) throws Exception {
		String shopId = param.shopId;
		Shop shop = shopService.find(shopId);
		Date createTime = DateUtils.addMonths(shop.getCreateTime(), -1);
		int isFinish = 0;
		List list = new ArrayList();

		Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
		Date date1 = DateUtils.addMonths(beginDate, 1);
		Date endDate = DateUtils.addDays(date1, -1);
		Date tempDate;
		if(beginDate.getTime() > createTime.getTime()){
			tempDate = beginDate;
		}else {
			tempDate = createTime;
			isFinish = 1;
		}
		while(tempDate.getTime() < endDate.getTime()) {
			tempDate = DateUtils.addWeeks(tempDate, 1);

			String suffix1 = BatteryOrderAllot.getSuffixByDate(tempDate);
			String exist1 = batteryOrderAllotService.exist(suffix1);
			if (StringUtils.isNotEmpty(exist1)) {
				List<BatteryOrderAllot> allotList = batteryOrderAllotService.findShopMonthIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix1, beginDate, endDate);
				for (BatteryOrderAllot allot : allotList) {
					NotNullMap map = new NotNullMap();
					map.putLong("id", allot.getId());
					map.putString("fullname", allot.getCustomerName());
					map.putMobileMask("mobile", allot.getCustomerMobile());
					map.putDateTime("payTime", allot.getPayTime());
					map.putInteger("orderMoney", allot.getOrderMoney());
					map.putDouble("money", allot.getMoney());
					map.putInteger("type", Shop.Type.HDG_BATTERY_ORDER.getValue());
					map.putString("statsDate", allot.getStatsDate());
					list.add(map);
				}
			}

			String suffix2 = PacketPeriodOrderAllot.getSuffixByDate(tempDate);
			String exist2 = packetPeriodOrderAllotService.exist(suffix2);
			if (StringUtils.isNotEmpty(exist2)) {
				List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotService.findShopMonthIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix2, beginDate, endDate);
				for (PacketPeriodOrderAllot allot : allotList) {
					NotNullMap map = new NotNullMap();
					map.putLong("id", allot.getId());
					map.putString("fullname", allot.getCustomerName());
					map.putMobileMask("mobile", allot.getCustomerMobile());
					map.putDateTime("payTime", allot.getPayTime());
					map.putInteger("orderMoney", allot.getOrderMoney());
					map.putDouble("money", allot.getMoney());
					map.putInteger("type", Shop.Type.HDG_PACKET_PERIOD.getValue());
					map.putString("statsDate", allot.getStatsDate());
					list.add(map);
				}
			}

			String suffix3 = RentPeriodOrderAllot.getSuffixByDate(tempDate);
			String exist3 = rentPeriodOrderAllotService.exist(suffix3);
			if (StringUtils.isNotEmpty(exist3)) {
				List<RentPeriodOrderAllot> allotList = rentPeriodOrderAllotService.findShopMonthIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shopId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix3, beginDate, endDate);
				for (RentPeriodOrderAllot allot : allotList) {
					NotNullMap map = new NotNullMap();
					map.putLong("id", allot.getId());
					map.putString("fullname", allot.getCustomerName());
					map.putMobileMask("mobile", allot.getCustomerMobile());
					map.putDateTime("payTime", allot.getPayTime());
					map.putInteger("orderMoney", allot.getOrderMoney());
					map.putDouble("money", allot.getMoney());
					map.putInteger("type", Shop.Type.ZD_RENT_PERIOD.getValue());
					map.putString("statsDate", allot.getStatsDate());
					list.add(map);
				}
			}
		}

		List listGroupByDay = groupByDay(list, param.date);
		if(!listGroupByDay.isEmpty() && listGroupByDay.size()>1) {
			Collections.sort(listGroupByDay, new Comparator<Map>() {
				public int compare(Map p1, Map p2) {
					return -((Map)p1.get("stats")).get("statsDate").toString().compareTo(((Map)p2.get("stats")).get("statsDate").toString());
				}
			});
		}

		Map map = new HashMap();
		map.put("isFinish", isFinish);
		map.put("listMonth", listGroupByDay);

		return RestResult.dataResult(0, null, map);
	}

	private List groupByDay(List<NotNullMap> monthList, String date) throws Exception {
		Date beginDate = DateUtils.parseDate(date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
		Date date1 = DateUtils.addMonths(beginDate, 1);
		Date endDate = DateUtils.addDays(date1, -1);

		List listGroupByDay = new ArrayList();
		while (beginDate.getTime() <= endDate.getTime()) {
			List list = new ArrayList();
			int totalCount = 0;
			double totalMoney = 0;

			for (NotNullMap map : monthList) {
				Date statsDate = DateUtils.parseDate((String)map.get("statsDate"), new String[]{Constant.DATE_FORMAT});
				if (statsDate.getTime() == beginDate.getTime()) {
					totalCount += 1;
					totalMoney += (Double) map.get("money");
					list.add(map);
				}
			}

			if(totalCount > 0) {
				NotNullMap stats = new NotNullMap();
				stats.putDate("statsDate", beginDate);
				stats.put("totalCount", totalCount);
				stats.put("totalMoney", totalMoney);

				Map data = new HashMap();
				data.put("list", list);
				data.put("stats", stats);

				listGroupByDay.add(data);
			}

			beginDate = DateUtils.addDays(beginDate, 1);
		}
		return listGroupByDay;
	}

}
