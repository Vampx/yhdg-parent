package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentOrderService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
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
import java.io.IOException;
import java.util.*;

@Controller("agent_api_v1_shop_hdg_shop")
@RequestMapping("/agent_api/v1/shop/hdg/shop")
public class ShopController extends ApiController {

	static final Logger log = LogManager.getLogger(ShopController.class);
	@Autowired
	MemCachedClient memCachedClient;
	@Autowired
	ShopService shopService;
	@Autowired
    UserService userService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	BatteryParameterService batteryParameterService;
	@Autowired
	BatteryOrderService batteryOrderService;
	@Autowired
	RentOrderService rentOrderService;
	@Autowired
	PacketPeriodOrderService packetPeriodOrderService;
	@Autowired
	RentPeriodOrderService rentPeriodOrderService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	CabinetBoxService cabinetBoxService;
	@Autowired
	CabinetOperateLogService cabinetOperateLogService;
	@Autowired
	CabinetDayStatsService cabinetDayStatsService;
	@Autowired
	CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
	@Autowired
	CabinetCodeService cabinetCodeService;
	@Autowired
	FaultLogService faultLogService;
	@Autowired
	CabinetMonthStatsService cabinetMonthStatsService;
	@Autowired
	AgentMonthStatsService agentMonthStatsService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	CustomerExchangeInfoService customerExchangeInfoService;
	@Autowired
	CustomerService customerService;
	@Autowired
	InsuranceOrderService insuranceOrderService;
	@Autowired
	ShopStoreBatteryService shopStoreBatteryService;
	@Autowired
	CustomerForegiftOrderService customerForegiftOrderService;
	@Autowired
	RentForegiftOrderService rentForegiftOrderService;

	/**
	 * 96-查询门店首页数据
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/info.htm")
	public RestResult getInfo() {
		TokenCache.Data tokenData = getTokenData();
		long userId = tokenData.userId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
		}
		Map map = new HashMap(20);
		User user = userService.find(userId);
		if (user != null) {
			map.put("id", user.getId());
			map.put("nickname", user.getNickname());
			map.put("photoPath", staticImagePath(user.getPhotoPath()));
			map.put("mobile", user.getMobile());
			map.put("loginName", user.getLoginName());
			map.put("balance", shop.getBalance());
			map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
			//核心管理员才能提现 显示提现按钮
			if (shop.getPayPeopleMobile() != null
					&& shop.getPayPeopleMobile().equals(user.getMobile())) {
				map.put("isSelf", ConstEnum.Flag.TRUE.getValue());
			} else {
				map.put("isSelf", ConstEnum.Flag.FALSE.getValue());
			}
			if (user.getIsAdmin() == ConstEnum.Flag.TRUE.getValue()) {
				map.put("isShowPay", ConstEnum.Flag.TRUE.getValue());
			} else {
				map.put("isShowPay", ConstEnum.Flag.FALSE.getValue());
			}
			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}
	/**
	 * 96-查询门店首页数据（管理权限）
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/admin_first_info.htm")
	public RestResult adminFirstInfo() {
		TokenCache.Data tokenData = getTokenData();
		long userId = tokenData.userId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
		}
		NotNullMap map = new NotNullMap();
		User user = userService.find(userId);
		if (user != null) {
			map.put("id", user.getId());
			map.put("nickname", user.getNickname());
			map.put("photoPath", staticImagePath(user.getPhotoPath()));
			map.put("mobile", user.getMobile());
			map.put("loginName", user.getLoginName());
			map.put("balance", shop.getBalance());
			map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
			map.put("shopRoleId", user.getShopRoleId());

			Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
			Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

			int packetMoney = packetPeriodOrderService.countShopTodayOrderMoney(shop.getId(), startTime, endTime);
			int rentMoney = rentPeriodOrderService.countShopTodayOrderMoney(shop.getId(), startTime, endTime);
			map.putInteger("todayPacketMoney", packetMoney + rentMoney);

			int shopStoreBattery = shopStoreBatteryService.findCount(shop.getId());
			map.putInteger("shopStoreBattery", shopStoreBattery);

			int cabinetBatteryCount = batteryService.countShopCabinetBattery(shop.getId());
			map.putInteger("cabinetBatteryCount", cabinetBatteryCount);

			int customerUseBatteryCount = batteryService.countShopCustomerUseNum(shop.getId());
			map.putInteger("shopBatteryCount", shopStoreBattery + cabinetBatteryCount + customerUseBatteryCount);

			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}
	/**
	 * 96-查询门店首页数据（资金权限）
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/money_first_info.htm")
	public RestResult moneyFirstInfo() {
		TokenCache.Data tokenData = getTokenData();
		long userId = tokenData.userId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
		}
		NotNullMap map = new NotNullMap();
		User user = userService.find(userId);
		if (user != null) {
			map.putLong("id", user.getId());
			map.putString("nickname", user.getNickname());
			map.putString("photoPath", staticImagePath(user.getPhotoPath()));
			map.putString("mobile", user.getMobile());
			map.putString("loginName", user.getLoginName());
			map.putInteger("balance", shop.getBalance());
			map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
			map.putInteger("shopRoleId", user.getShopRoleId());

			Date startTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
			Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(startTime, 1), -1);

			int packetMoney = packetPeriodOrderService.countShopTodayOrderMoney(shop.getId(), startTime, endTime);
			int rentMoney = rentPeriodOrderService.countShopTodayOrderMoney(shop.getId(), startTime, endTime);
			map.putInteger("todayPacketMoney", packetMoney + rentMoney);

			int customerForegiftCount = customerForegiftOrderService.countShopTodayOrderNum(shop.getId(), startTime, endTime);
			int rentForegiftOrderCount = rentForegiftOrderService.countShopTodayOrderNum(shop.getId(), startTime, endTime);
			map.putInteger("registerCount", customerForegiftCount + rentForegiftOrderCount);

			int batteryOrderCount = batteryOrderService.countShopTodayOrderNum(shop.getId(), startTime, endTime);
			int rentOrderCount = rentOrderService.countShopTodayOrderNum(shop.getId(), startTime, endTime);
			map.putInteger("peopleCount", batteryOrderCount + rentOrderCount);

			int cabinetBatteryCount = batteryService.countShopCabinetBattery(shop.getId());
			map.putInteger("cabinetBatteryCount", cabinetBatteryCount);

			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "用户不存在", null);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 34-查询门店列表
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Shop> list = shopService.findList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Shop shop : list) {
			Map line = new HashMap();
			line.put("id", shop.getId());
			line.put("shopName", shop.getShopName());
			line.put("address", shop.getAddress());
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	/**
	 * 97-查询我的门店
	 *
	 */
	@ResponseBody
	@RequestMapping("/my_shop.htm")
	public RestResult detailList() {

		TokenCache.Data tokenData = getTokenData();

		Map result = shopService.findDetail(tokenData.shopId);
		if (result == null) {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "门店不存在！", result);
		}
		String[] imageFilePaths = (String[]) result.get("imagePath");
		List<Map> imagePathListMap = new ArrayList<Map>();
		//图片路径为空的就不存到数组
		for (String s : imageFilePaths) {
			if (s != null) {
				Map map = new HashMap();
				map.put("url", staticImagePath(s));
				imagePathListMap.add(map);
			}
		}
		result.put("imagePath", imagePathListMap);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class VehicleListParam {
		public String shopId;
		public int offset;
		public int limit;
	}

	/**
	 * 40-查询门店车辆列表
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/vehicle_list.htm")
	public RestResult list(@Valid @RequestBody VehicleListParam param) {
		List<Vehicle> list = new ArrayList<Vehicle>();

		List<Map> result = new ArrayList<Map>();
		for (Vehicle vehicle : list) {
			Map line = new HashMap();
			line.put("id", vehicle.getId());
			line.put("vehicleModelName", vehicle.getModelName());
			line.put("status", vehicle.getStatus());
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	public static class CreateParam {
		@NotBlank(message = "门店名称不能为空")
		public String shopName;
		public Integer areaId;
		public String street;
		public String openTime;
		public Long[] userList;
		public Double lng;
		public Double lat;
		public String[] imagePath;
	}

	/**
	 * 35-新建门店
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/create.htm")
	public RestResult createShop(@RequestBody CreateParam param) {
		ExtResult extResult = shopService.create(param.shopName, param.areaId, param.street, param.openTime, param.userList, param.lng, param.lat, param.imagePath, getTokenData().agentId);
		if (!extResult.isSuccess()) {
			return RestResult.result(RespCode.CODE_2.getValue(), extResult.getMessage());
		}
		return RestResult.result(RespCode.CODE_0.getValue(), "门店新建成功！");

	}

	public static class UpdateParam {
		public String id;
		@NotBlank(message = "门店名称不能为空")
		public String shopName;
		public String linkname;
		public String tel;
		public String openTime;
	}

	/**
	 * 100-修改门店信息
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update.htm")
	public RestResult updateShop(@RequestBody UpdateParam param) {
	  return shopService.update(param.id, param.shopName, param.linkname, param.tel, param.openTime);
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
	 * 104-修改门店收款人信息
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

		return shopService.updatePayPeople(param.id, param.payPeopleMobile, param.payPeopleName, param.payPeopleMpOpenId, param.payPeopleFwOpenId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		@NotBlank(message = "门店id不能为空")
		public String shopId;
	}

	/**
	 * 47-删除门店
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.htm")
	public RestResult delete(@Valid @RequestBody DeleteParam param) {
		ExtResult extResult = shopService.delete(param.shopId);
		if (!extResult.isSuccess()) {
			return RestResult.result(RespCode.CODE_2.getValue(), extResult.getMessage());
		}
		return RestResult.result(RespCode.CODE_0.getValue(), null);
	}


	/**
	 * 98-查询门店店员账号列表
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/shop_user_list.htm")
	public RestResult shopUserList() {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Shop shop = shopService.find(getTokenData().shopId);
		if (shop == null){
			return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
		}
		List<User> list = userService.findListByAgentId(agentId, User.AccountType.SHOP.getValue(), shop.getId(), null);

		List<Map> result = new ArrayList<Map>();
		for (User user : list) {
			NotNullMap line = new NotNullMap();
			line.put("id", user.getId());
			line.put("loginName", user.getLoginName());
			line.put("fullname", user.getFullname());
			line.putMobileMask("mobile", user.getMobile());
			line.put("shopId", user.getShopId());
			if (user.getShopId() != null) {
				line.put("shopName", shopService.find(user.getShopId()).getShopName());
			} else {
				line.put("shopName", "");
			}
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShopCabinetListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 40-查询门店设备列表
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/shop_cabinet_list.htm")
	public RestResult shopCabinetList(@Valid @RequestBody ShopCabinetListParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		int agentId = tokenData.agentId;
		List<Cabinet> list = cabinetService.findList(agentId, tokenData.shopId, param.keyword, param.offset, param.limit);

		String  statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);

		List<Map> result = new ArrayList<Map>();
		for (Cabinet cabinet : list) {
			Map line = new HashMap();
			line.put("id", cabinet.getId());
			line.put("cabinetName", cabinet.getCabinetName());
			line.put("address", cabinet.getAddressName());
			line.put("chargeFullVolume", cabinet.getChargeFullVolume());
			line.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
			line.put("upLineStatus", cabinet.getUpLineStatus());
			line.put("isOnline", cabinet.getIsOnline());

			if(cabinet.getUpLineTime() != null){
				line.put("upLineTime",DateFormatUtils.format( cabinet.getUpLineTime(), Constant.DATE_TIME_FORMAT));
			}else{
				line.put("upLineTime","暂无时间");
			}

			//版本
			line.put("version", cabinet.getVersion());

			//查询日统计表
			CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), statsDate);
			if(cabinetDayStats != null){
				//换电订单
				line.put("orderCount", cabinetDayStats.getOrderCount());
				//押金数-骑手
				line.put("foregiftCount", cabinetDayStats.getForegiftCount());
				//押金金额
				line.put("foregiftMoney", cabinetDayStats.getForegiftMoney() - cabinetDayStats.getRefundForegiftMoney());
				//套餐金额
				line.put("packetPeriodMoney", cabinetDayStats.getAgentPacketPeriodMoney() - cabinetDayStats.getAgentRefundPacketPeriodMoney());

				line.put("activeCount", cabinetDayStats.getActiveCustomerCount());
			}else{
				//换电订单
				line.put("orderCount", 0);
				//押金数-骑手
				line.put("foregiftCount", 0);
				//押金金额
				line.put("foregiftMoney", 0);
				//套餐金额
				line.put("packetPeriodMoney", 0);

				line.put("activeCount",0);
			}
			//电价
			line.put("unitPrice", cabinet.getPrice() * 100);
			//电费
			CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
			if(cabinetDayDegreeStats != null){
				line.put("useVolume", cabinetDayDegreeStats.getNum());
				line.put("price", cabinetDayDegreeStats.getNum() * cabinet.getPrice() * 100 );
				if(cabinetDayStats != null && cabinetDayStats.getActiveCustomerCount() != 0){
					line.put("referencePrice",  (cabinetDayDegreeStats.getNum() * cabinet.getPrice() * 100)/cabinetDayStats.getActiveCustomerCount());
				} else {
					line.put("referencePrice", 0);
				}
			} else {
				line.put("useVolume",0);
				line.put("price",0);
				line.put("referencePrice", 0);
			}
			CabinetMonthStats cabinetMonthStats = cabinetMonthStatsService.findTotal(cabinet.getId());
			if(cabinetMonthStats != null){
				//换电订单
				line.put("totalOrderCount", cabinetMonthStats.getOrderCount());
				//当前注册数
				line.put("currentForegiftCount", customerExchangeInfoService.findByBalanceCabinetId(cabinet.getId()));
				//押金数-骑手
				line.put("totalForegiftCount", cabinetMonthStats.getForegiftCount());
				//押金金额
				line.put("totalForegiftMoney", cabinetMonthStats.getForegiftMoney() - cabinetMonthStats.getRefundForegiftMoney());
				//套餐金额
				line.put("totalPacketPeriodMoney", cabinetMonthStats.getAgentPacketPeriodMoney() - cabinetMonthStats.getAgentRefundPacketPeriodMoney());
			} else {
				//换电订单
				line.put("totalOrderCount", 0);
				//当前注册数
				line.put("currentForegiftCount", 0);
				//押金数-骑手
				line.put("totalForegiftCount", 0);
				//押金金额
				line.put("totalForegiftMoney", 0);
				//套餐金额
				line.put("totalPacketPeriodMoney", 0);
			}

			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	public static class DetailParam {
		public String id;
	}
	/**
	 * 112-查询门店换电站详情
	 *
	 */
	@ResponseBody
	@RequestMapping("/cabinet_info.htm")
	public RestResult info(@RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		Cabinet cabinet = cabinetService.find(param.id);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		Map data = new HashMap();
		data.put("id", cabinet.getId());
		data.put("cabinetName", cabinet.getCabinetName());
		data.put("address", cabinet.getAddressName());
		data.put("street", cabinet.getStreet());
		data.put("chargeFullVolume", cabinet.getChargeFullVolume());
		data.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
		data.put("terminalId", cabinet.getTerminalId());
		data.put("lng", cabinet.getLng());
		data.put("lat", cabinet.getLat());
		data.put("isOnline", cabinet.getIsOnline());
		data.put("temp1", cabinet.getTemp1() == null ? 0 : cabinet.getTemp1() / 100);
		data.put("temp2", cabinet.getTemp2() == null ? 0 : cabinet.getTemp2() / 100);
		data.put("boxMinPower", cabinet.getBoxMinPower());
		data.put("boxMaxPower", cabinet.getBoxMaxPower());
		data.put("activeFanTemp", cabinet.getActiveFanTemp());
		data.put("power", cabinet.getPower());
		CabinetCode cabinetCode = cabinetCodeService.find(cabinet.getId());
		if (cabinetCode != null) {
			data.put("cabinetSn", cabinetCode.getId());
		} else {
			data.put("cabinetSn", "");
		}

		data.put("workTime", cabinet.getWorkTime() != null ? cabinet.getWorkTime() : "");
		data.put("isAllowOpenBox", cabinet.getIsAllowOpenBox());

		data.put("price", cabinet.getPrice() * 100);
		//用电量统计中查找
		int useNum = 0;
		CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
		if(cabinetDayDegreeStats != null){
			useNum = cabinetDayDegreeStats.getEndNum();
		}
		data.put("useVolume", useNum);
		if(cabinet.getPrice() != null ){
			data.put("powerPrice", useNum * cabinet.getPrice() * 100 );
		}else{
			data.put("powerPrice", 0);
		}


		data.put("foregiftMoney", cabinet.getForegiftMoney());
		data.put("rentMoney", cabinet.getRentMoney());
		data.put("rentPeriodType", cabinet.getRentPeriodType());
		if(cabinet.getRentExpireTime() != null){
			data.put("rentExpireTime",DateFormatUtils.format(cabinet.getRentExpireTime(), Constant.DATE_FORMAT));
		}else{
			data.put("rentExpireTime", null);
		}

		if(cabinet.getUpLineTime() != null){
			data.put("upLineTime",DateFormatUtils.format(cabinet.getUpLineTime(), Constant.DATE_TIME_FORMAT));
		}else{
			data.put("upLineTime","暂无时间");
		}

		String  statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
		CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), statsDate);
		if(cabinetDayStats != null){
			//换电订单
			data.put("orderCount", cabinetDayStats.getOrderCount());
			//押金数-骑手
			data.put("foregiftCount", cabinetDayStats.getForegiftCount());
			//押金金额
			data.put("foregiftMoney", cabinetDayStats.getForegiftMoney() - cabinetDayStats.getRefundForegiftMoney());
			//套餐金额
			data.put("packetPeriodMoney",  cabinetDayStats.getAgentPacketPeriodMoney() - cabinetDayStats.getAgentRefundPacketPeriodMoney());
		}else{
			//换电订单
			data.put("orderCount", 0);
			//押金数-骑手
			data.put("foregiftCount", 0);
			//押金金额
			data.put("foregiftMoney", 0);
			//套餐金额
			data.put("packetPeriodMoney", 0);
		}

		CabinetMonthStats cabinetMonthStats = cabinetMonthStatsService.findTotal(cabinet.getId());
		if(cabinetMonthStats != null){
			//换电订单
			data.put("totalOrderCount", cabinetMonthStats.getOrderCount());
			//押金数-骑手
			data.put("totalForegiftCount", cabinetMonthStats.getForegiftCount());
			//押金金额
			data.put("totalForegiftMoney", cabinetMonthStats.getForegiftMoney() - cabinetMonthStats.getRefundForegiftMoney());
			//套餐金额
			data.put("totalPacketPeriodMoney", cabinetMonthStats.getAgentPacketPeriodMoney() - cabinetMonthStats.getAgentRefundPacketPeriodMoney());
		}else{
			//换电订单
			data.put("totalOrderCount", 0);
			//押金数-骑手
			data.put("totalForegiftCount", 0);
			//押金金额
			data.put("totalForegiftMoney", 0);
			//套餐金额
			data.put("totalPacketPeriodMoney", 0);
		}

		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

	/**
	 * 113-查询门店设备格口详情
	 *
	 */
	@ResponseBody
	@RequestMapping("/box_info.htm")
	public RestResult boxInfo(@RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		Cabinet cabinet = cabinetService.find(param.id);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		List<Map> boxList = new ArrayList<Map>();
		List<CabinetBox> cabinetBoxList = cabinetBoxService.findByCabinetId(cabinet.getId());
		final int EMPTY_BOX = 1, NOT_OUT = 2, ORANGE = 3, FULL_POWER = 4, PROHIBIT = 5 , NOT_ONLINE = 6, BESPEAK = 7;    //1 空箱 2 不可取出(未满和客户的电池) 3 橙色 4 满电 5 禁用 6 电池未上线
		for (CabinetBox box : cabinetBoxList) {
			Map boxMap = new HashMap();
			boxMap.put("boxNum", box.getBoxNum());
			boxMap.put("batteryId", box.getBatteryId());
			boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());
			boxMap.put("power", box.getPower());
			boxMap.put("isOpen", box.getIsOpen());
			boxMap.put("shellCode", box.getShellCode());
			boxMap.put("imei", box.getImei());
			boxMap.put("chargeStatus", box.getChargeStatus());
			boxMap.put("forbiddenCause", box.getForbiddenCause());
			int status = EMPTY_BOX; //1 空箱  空闲
			int expectFullTime = 0;
			if (box.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
				status = PROHIBIT;// 5 禁用
			} else if (box.getBatteryId() != null) {
				if ((box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue() || box.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()) &&
						(box.getBatteryStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()
								|| box.getBatteryStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue())) {
					status = NOT_OUT; // 2 不可取出(未满和客户的电池)
				}  else if (box.getBoxStatus() == CabinetBox.BoxStatus.BESPEAK.getValue()) {
					status = BESPEAK; // 预约
				} else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
						&& box.getVolume() < box.getBatteryFullVolume()) {
					status = ORANGE; // 3 未充满 橙色
				} else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
						&& box.getVolume() >= box.getBatteryFullVolume()) {
					status = FULL_POWER; //4 满电(可换)
				}
				if (box.getVolume() != null && box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
					expectFullTime = (int) Math.ceil(((100 - box.getVolume()) * 0.7));
				}
				//上线状态 0 未上线 1 已上线*/
				if (box.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue()) {
					status = NOT_ONLINE;
				}
			} else if(box.getBoxStatus() == CabinetBox.BoxStatus.BESPEAK.getValue()
					|| box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
					|| box.getBoxStatus() == CabinetBox.BoxStatus.FULL_LOCK.getValue()
					|| box.getBoxStatus() == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
				status = NOT_OUT; // 2 不可取出(占箱的格口)
			}
			boxMap.put("status", status);
			boxMap.put("expectFullTime", expectFullTime);
			boxList.add(boxMap);
		}

		Map data = new HashMap();
		data.put("id", cabinet.getId());
		data.put("cabinetName", cabinet.getCabinetName());
		data.put("boxList", boxList);

		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetBatteryListParam {
		public String cabinetId;
	}

	/**
	 * 114-查询设备电池类型
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_battery_list.htm")
	public RestResult cabinetBatteryList(@RequestBody CabinetBatteryListParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		return cabinetService.cabinetBatteryList(param.cabinetId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetForegiftListParam {
		public Integer batteryType;
	}

	/**
	 * 115-查询门店设备押金
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_foregift_list.htm")
	public RestResult cabinetForegiftList(@RequestBody CabinetForegiftListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		return cabinetService.cabinetForegiftList(param.batteryType,agentId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetPriceListParam {
		public Integer batteryType;
		public Long foregiftId;
	}

	/**
	 * 116-查询门店设备租金
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_price_list.htm")
	public RestResult cabinetPriceList(@RequestBody CabinetPriceListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		return cabinetService.cabinetPriceList(param.batteryType,agentId, param.foregiftId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetInsuranceListParam {
		public Integer batteryType;
	}

	/**
	 * 117-查询设备保险
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_insurance_list.htm")
	public RestResult cabinetInsuranceList(@RequestBody CabinetInsuranceListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		return cabinetService.cabinetInsuranceList(param.batteryType,agentId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DegreeStatsListParam {
		public String cabinetId;
		public int offset;
		public int limit;
	}

	/**
	 * 118-查询门店换电柜电费
	 *
	 **/
	@ResponseBody
	@RequestMapping(value = "/degree_stats_list")
	public RestResult degreeStatsList(@Valid @RequestBody DegreeStatsListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}

		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该换电柜不存在");
		}

		List<CabinetDayDegreeStats> list = cabinetDayDegreeStatsService.findList(agentId, param.cabinetId, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (CabinetDayDegreeStats stats : list) {
			Map line = new HashMap();
			line.put("statsDate", stats.getStatsDate());
			line.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_TIME_FORMAT));
			line.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_TIME_FORMAT));
			line.put("beginNum", stats.getBeginNum());
			line.put("endNum", stats.getEndNum());
			line.put("num", stats.getNum());
			line.put("unitPrice", cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 );
			line.put("price", cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum());
			CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), stats.getStatsDate());
			if (cabinetDayStats != null) {
				line.put("orderCount", cabinetDayStats.getOrderCount());
				if(cabinetDayStats.getActiveCustomerCount() != 0){
					line.put("referencePrice", (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum())/cabinetDayStats.getActiveCustomerCount());
				}else{
					line.put("referencePrice", 0);
				}
			} else {
				line.put("orderCount", 0);
				line.put("referencePrice", 0);

			}

			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetStatsListParam {
		public String cabinetId;
		public int offset;
		public int limit;
	}

	/**
	 * 119-查询门店设备收入
	 *
	 */
	@ResponseBody
	@RequestMapping("/cabinet_stats_list.htm")
	public RestResult cabinetStatsList(@RequestBody CabinetStatsListParam param) throws IOException {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		int agentId = getTokenData().agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		NotNullMap data = new NotNullMap();

		List<CabinetDayStats> statsList = cabinetDayStatsService.findListByCabinetId(agentId, param.cabinetId);

		double foregiftMoney = 0;
		double foregiftRefundMoney = 0;
		double packetPeriodMoney = 0;
		double refundPacketPeriodMoney = 0;
		double orderCount = 0;
		double activeCount = 0;

		for (CabinetDayStats e : statsList){
			foregiftMoney = foregiftMoney + e.getForegiftMoney();
			foregiftRefundMoney = foregiftRefundMoney + e.getRefundForegiftMoney();
			packetPeriodMoney = packetPeriodMoney + e.getAgentPacketPeriodMoney();
			refundPacketPeriodMoney = refundPacketPeriodMoney + e.getAgentRefundPacketPeriodMoney();
			orderCount = orderCount + e.getOrderCount();
			activeCount = activeCount + e.getActiveCustomerCount();
		}

		CabinetDayDegreeStats totalElectricPrice = cabinetDayDegreeStatsService.findForAgent(agentId, null, param.cabinetId);

		data.put("totalForegiftMoney", (int)foregiftMoney - foregiftRefundMoney);
		data.put("totalPacketPeriodMoney", (int)packetPeriodMoney - refundPacketPeriodMoney);
		if (totalElectricPrice != null) {
			data.put("totalElectricPrice", totalElectricPrice.getPrice() * 100);
		} else {
			data.put("totalElectricPrice", 0);
		}
		data.put("totalOrderCount", orderCount);
		data.put("totalActiveCount", activeCount);

		List<CabinetDayStats> cabinetDayStats = cabinetDayStatsService.findByCabinetList(agentId, param.cabinetId, null, null,null, param.offset, param.limit);

		List<NotNullMap> list = new ArrayList<NotNullMap>();
		for (CabinetDayStats stats : cabinetDayStats) {
			NotNullMap notNullMap = new NotNullMap();

			notNullMap.put("statsDate", stats.getStatsDate());
			notNullMap.put("cabinetId", stats.getCabinetId());
			notNullMap.put("cabinetName", stats.getCabinetName());
			notNullMap.put("foregiftMoney", stats.getForegiftMoney() - stats.getRefundForegiftMoney());
			notNullMap.put("packetPeriodMoney", stats.getAgentPacketPeriodMoney() - stats.getAgentRefundPacketPeriodMoney());
			notNullMap.put("price", stats.getUnitPrice());
			notNullMap.put("useVolume", stats.getElectricDegree());
			notNullMap.put("powerPrice", stats.getElectricPrice());
			notNullMap.put("activeCustomerCount", stats.getActiveCustomerCount());
			notNullMap.put("foregiftCount", stats.getForegiftCount());
			notNullMap.put("orderCount", stats.getOrderCount());

			list.add(notNullMap);
		}
		data.put("cabinetList", list);


		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

	}

	public static class CabinetUpdateParam {
		public String id;
		@NotBlank(message = "站点名称不能为空")
		public String cabinetName;
		public Integer chargeFullVolume;
		public Integer permitExchangeVolume;
		public String terminalId;
		public Integer boxMinPower;
		public Integer boxMaxPower;
		public Double price;
		public Integer power;
		public String workTime;

		public Integer areaId;
		public String street;
		public Double lng;
		public Double lat;


	}

	/**
	 * 120-修改门店设备换电柜
	 *
	 */
	@ResponseBody
	@RequestMapping("/cabinet_update.htm")
	public RestResult cabinetUpdate(@Valid @RequestBody CabinetUpdateParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		int agentId = tokenData.agentId;
		Cabinet cabinet = cabinetService.find(param.id);

		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		if (StringUtils.isEmpty(param.cabinetName)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "站点名称不能为空");
		}

		cabinet.setAgentId(agentId);
		cabinet.setCabinetName(param.cabinetName);
		cabinet.setChargeFullVolume(param.chargeFullVolume);
		cabinet.setPermitExchangeVolume(param.permitExchangeVolume);
		cabinet.setTerminalId(param.terminalId);
		cabinet.setBoxMinPower(param.boxMinPower);
		cabinet.setBoxMaxPower(param.boxMaxPower);
		cabinet.setPrice(param.price);
		cabinet.setPower(param.power);
		cabinet.setWorkTime(param.workTime);

		//修改地址
		if(param.areaId != null && param.lng != null && param.lat != null){
			cabinetService.updateAddress(param.id,
					param.areaId,
					param.street,
					param.lng,
					param.lat);
		}

		return cabinetService.update(cabinet);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OpenBoxParam {
		public String cabinetId;
		public String boxNum;
	}

	/**
	 * 121-门店设备开箱
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/open_box.htm")
	public RestResult batteryOrderOldBatteryOpenBox(@RequestBody OpenBoxParam param) throws InterruptedException {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		User user = userService.find(tokenData.userId);
		if (StringUtils.isEmpty(param.cabinetId)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}
		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		CabinetOperateLog operateLog = new CabinetOperateLog();
		operateLog.setAgentId(cabinet.getAgentId() != null ? cabinet.getAgentId() : 0);
		operateLog.setCabinetId(cabinet.getId());
		operateLog.setCabinetName(cabinet.getCabinetName());
		operateLog.setBoxNum(param.boxNum);
		operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
		operateLog.setOperatorType(CabinetOperateLog.OperatorType.SHOP_CABINET.getValue());
		operateLog.setOperator(user.getFullname());

		//发送开箱指令
		RestResult result = ClientBizUtils.openStandardBox(config, param.cabinetId, param.boxNum, cabinet.getSubtype());

		int openSuccess = 0;
		Map data = new HashMap();

		if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
			if (log.isDebugEnabled()) {
				log.debug("open {}, {} box success", param.cabinetId, param.boxNum);
			}
			openSuccess = 1;
			operateLog.setContent("远程开箱成功, 箱门状态: " + (result.getCode() == 0 ? "开门" : "关门"));
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		} else {
			openSuccess = 0;
			if (log.isDebugEnabled()) {
				log.error("open {}, {} box fail", param.cabinetId, param.boxNum);
			}
			operateLog.setContent("远程开箱失败, " + result.getMessage());
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		data.put("openSuccess", openSuccess);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CompleteOrderParam {
		public String batteryId;
	}

	/**
	 * 122-结束订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/complete_order.htm")
	public RestResult completeOrderParam(@RequestBody CompleteOrderParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		Battery battery = batteryService.find(param.batteryId);
		if (battery == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
		}
		if (battery.getOrderId() != null) {
			batteryOrderService.complete(battery.getOrderId());
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetBoxParam {
		public String cabinetId;
		public String[] boxNumList;
		public int isActive;
		public String forbiddenCause;
	}

	/**
	 * 123-门店设备格口启用禁用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/box_active.htm")
	public RestResult CabinetBoxActive(@RequestBody CabinetBoxParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		User user = userService.find(tokenData.userId);
		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		String[] boxList = new String[param.boxNumList.length];
		if (param.boxNumList != null && param.boxNumList.length > 0) {
			boxList = param.boxNumList;
		}

		for (String boxNum : boxList) {
			CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, boxNum);
			if (cabinetBox == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
			}
			//保存日志
			CabinetOperateLog operateLog = new CabinetOperateLog();
			operateLog.setAgentId(cabinet.getAgentId());
			operateLog.setCabinetId(cabinet.getId());
			operateLog.setCabinetName(cabinet.getCabinetName());
			operateLog.setBoxNum(boxNum);
			operateLog.setOperatorType(CabinetOperateLog.OperatorType.AGENT_CABINET.getValue());
			operateLog.setOperator(user.getFullname());
			String operator = user.getFullname();
			if (param.isActive == ConstEnum.Flag.TRUE.getValue()){
				cabinetBoxService.updateBoxActive(param.cabinetId, boxNum, ConstEnum.Flag.TRUE.getValue(),param.forbiddenCause, null, null);
				operateLog.setOperateType(CabinetOperateLog.OperateType.ACTIVE.getValue());
				operateLog.setContent("箱门激活成功");
			} else {
				cabinetBoxService.updateBoxActive(param.cabinetId, boxNum, ConstEnum.Flag.FALSE.getValue(),param.forbiddenCause,operator,new Date());
				operateLog.setOperateType(CabinetOperateLog.OperateType.NO_ACTIVE.getValue());
				operateLog.setContent("箱门禁用成功");
			}

			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TrackListParam {
		public String orderId;
		public String trackTime;
		public int offset;
		public int limit;
	}
	/**
	 * 125-查询门店电池轨迹
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/track_list.htm")
	public RestResult trackList(@RequestBody TrackListParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		return batteryOrderService.findBatteryReportLogByOrderId(param.orderId, param.trackTime, param.offset, param.limit);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryFaultLogListParam {
		public String batteryId;
		public int offset;
		public int limit;
	}

	/**
     * 126-查询门店电池告警信息
     **/
	@ResponseBody
	@RequestMapping(value = "/fault_log_list.htm")
	public RestResult batteryFaultLogList(@Valid @RequestBody BatteryFaultLogListParam param ) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		int agentId = tokenData.agentId;
		List<FaultLog> list = faultLogService.findByBatteryId(agentId, param.batteryId, param.offset, param.limit);
		List<Map> result = new ArrayList<Map>();
		if (list != null) {
			for (FaultLog faultLog : list){
				Map line = new HashMap();
				line.put("faultType", faultLog.getFaultTypeName());
				line.put("boxNum", faultLog.getBoxNum());
				line.put("faultContent", faultLog.getFaultContent());
				line.put("createTime", faultLog.getCreateTime());
				result.add(line);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UpdateLockSwitchParam {
		public String batteryId;
		public int lockSwitch;
	}
	/**
	 * 127-门店电池电池控制
	 * */
	@ResponseBody
	@RequestMapping(value = "/update_lock_switch.htm")
	public RestResult updateLockSwitch(@Valid @RequestBody UpdateLockSwitchParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		batteryService.updateLockSwitch(param.batteryId, param.lockSwitch);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryOrderListParam {
		public String idAndName;
		public String batteryId;
		public int offset;
		public int limit;
	}


	/**
	 * 128-查询门店电池订单
	 * */
	@ResponseBody
	@RequestMapping(value = "/battery_order_list.htm")
	public RestResult BatteryOrderList(@Valid @RequestBody BatteryOrderListParam param) {
		TokenCache.Data tokenData = getTokenData();
		Shop shop = shopService.find(tokenData.shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}
		int agentId = tokenData.agentId;
		List<BatteryOrder> list = batteryOrderService.findByBatteryList(agentId, param.batteryId, param.idAndName, param.offset, param.limit);
		List<Map> result = new ArrayList<Map>();
		if (list != null) {
			for (BatteryOrder batteryOrder : list) {
				Map line = new HashMap();
				line.put("id", batteryOrder.getId());
				line.put("customerFullname", batteryOrder.getCustomerFullname());
				line.put("customerMobile", batteryOrder.getCustomerMobile());
				line.put("createTime", batteryOrder.getCreateTime());
				result.add(line);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


}
