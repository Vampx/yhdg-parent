package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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

@Controller("agent_api_v1_agent_hdg_battery_order")
@RequestMapping("/agent_api/v1/agent/hdg/battery_order")
public class BatteryOrderController extends ApiController {

	@Autowired
	BatteryOrderService batteryOrderService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	PacketPeriodOrderService packetPeriodOrderService;
	@Autowired
	CustomerExchangeInfoService customerExchangeInfoService;
	@Autowired
	InsuranceOrderService insuranceOrderService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String keyword;
		public int offset;
		public int limit;
	}

	/**
	 * 32-查询换电订单列表
	 *
	 * @param param
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<BatteryOrder> list = batteryOrderService.findList(agentId, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (BatteryOrder batteryOrder : list) {
			Map line = new HashMap();
			line.put("id", batteryOrder.getId());
			line.put("distance", batteryOrder.getCurrentDistance());
			line.put("takeCabinetName", batteryOrder.getTakeCabinetName());
			line.put("takeBoxNum", batteryOrder.getTakeBoxNum());
			line.put("takeTime", batteryOrder.getTakeTime() != null ? DateFormatUtils.format(batteryOrder.getTakeTime(), Constant.DATE_TIME_FORMAT) : "");
			line.put("putCabinetName", batteryOrder.getPutCabinetName());
			line.put("putBoxNum", batteryOrder.getPutBoxNum());
			line.put("putTime", batteryOrder.getPutTime() != null ? DateFormatUtils.format(batteryOrder.getPutTime(), Constant.DATE_TIME_FORMAT) : "");
			line.put("status", batteryOrder.getOrderStatus());
			line.put("batteryId", batteryOrder.getBatteryId());
			line.put("batteryType", batteryOrder.getBatteryTypeName());
			line.put("initVolume", batteryOrder.getInitVolume());
			line.put("customerMobile", batteryOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
			line.put("customerFullName", batteryOrder.getCustomerFullname());
			line.put("createTime", DateFormatUtils.format(batteryOrder.getCreateTime(), Constant.DATE_TIME_FORMAT));
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BatteryOrderListParam {
		public String idAndName;
		public String batteryId;
		public int offset;
		public int limit;
	}

	/**
	* 73-查询电池订单
	* */
	@ResponseBody
	@RequestMapping(value = "/battery_order_list.htm")
	public RestResult BatteryOrderList(@Valid @RequestBody BatteryOrderListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<BatteryOrder> list = batteryOrderService.findByBatteryList(agentId, param.batteryId, param.idAndName, param.offset, param.limit);
		List<Map> result = new ArrayList<Map>();
		if (list != null) {
			for (BatteryOrder batteryOrder : list) {
				Map line = new HashMap();
				line.put("id", batteryOrder.getId());
				line.put("customerFullname", batteryOrder.getCustomerFullname());
				line.put("customerMobile", batteryOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
				line.put("createTime", batteryOrder.getCreateTime());
				result.add(line);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class InfoParam {
		public String batteryOrderId;
	}

	/**
	 * 74-查询换电订单详情
	 *
	 * @param param
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/info.htm")
	public RestResult info(@Valid @RequestBody InfoParam param) {
		BatteryOrder batteryOrder = batteryOrderService.find(param.batteryOrderId);
		if (batteryOrder == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
		}
		Map line = new HashMap();
		line.put("id", batteryOrder.getId());//订单编号
		line.put("distance", batteryOrder.getCurrentDistance());//骑行距离
		line.put("putBoxNum", batteryOrder.getPutBoxNum());//放电柜箱号
		line.put("takeBoxNum", batteryOrder.getTakeBoxNum());//取电柜箱号
		line.put("takeTime", batteryOrder.getTakeTime() != null ? DateFormatUtils.format(batteryOrder.getTakeTime(), Constant.DATE_TIME_FORMAT) : "");
		line.put("putTime", batteryOrder.getPutTime() != null ? DateFormatUtils.format(batteryOrder.getPutTime(), Constant.DATE_TIME_FORMAT) : "");
		line.put("payType", batteryOrder.getPayTypeName());//支付类型
		line.put("status", batteryOrder.getOrderStatus());//状态
		line.put("batteryId", batteryOrder.getBatteryId());//电池编号
		line.put("currentVolume", batteryOrder.getCurrentVolume());//当前电量
		line.put("initVolume", batteryOrder.getInitVolume());//取出电量
		line.put("takeCabinetName", batteryOrder.getTakeCabinetName());//取出站点名称
		line.put("putCabinetName", batteryOrder.getPutCabinetName());//归还站点名称
		String hoursOfUse = null;
		if (batteryOrder.getPutTime() != null && batteryOrder.getTakeTime() != null) {
			hoursOfUse = InstallUtils.getDatePoor(batteryOrder.getPutTime(), batteryOrder.getTakeTime());
		}
		line.put("hoursOfUse", hoursOfUse);//使用时间

		line.put("fullname", batteryOrder.getCustomerFullname());
		line.put("mobile", batteryOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));

		String leavingDay = null;
		long now = System.currentTimeMillis();
		PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findLastEndTime(batteryOrder.getCustomerId());
		if (packetPeriodOrder != null) {
			if (now < packetPeriodOrder.getEndTime().getTime()) {
				leavingDay = AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - now) / 1000);
			}
		}

		CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(batteryOrder.getCustomerId());
		InsuranceOrder insuranceOrder = null;
		if (customerExchangeInfo != null) {
			line.put("foregiftMoney", customerExchangeInfo.getForegift());
			Integer batteryType = customerExchangeInfo.getBatteryType();
			insuranceOrder = insuranceOrderService.findByCustomerId(batteryOrder.getCustomerId(), batteryType, InsuranceOrder.Status.PAID.getValue());
			line.put("insuranceMoney", insuranceOrder != null ? insuranceOrder.getPrice() : 0);
			line.put("insurancePaid", insuranceOrder != null ? insuranceOrder.getPaid() : 0);
			line.put("monthCount", insuranceOrder != null ? insuranceOrder.getMonthCount() : 0);
		} else {
			line.put("foregiftMoney", 0);
			line.put("insuranceMoney", 0);
			line.put("insurancePaid", 0);
			line.put("monthCount", 0);
		}
		if (packetPeriodOrder!= null) {
			line.put("packetPeriodMoney", packetPeriodOrder.getPrice());
			line.put("dayCount", packetPeriodOrder.getDayCount());
			line.put("beginTime", packetPeriodOrder.getBeginTime() != null ? DateFormatUtils.format(packetPeriodOrder.getBeginTime(), Constant.DATE_TIME_FORMAT) : "");
			line.put("endTime", packetPeriodOrder.getEndTime() != null ? DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_TIME_FORMAT) : "");
			line.put("leavingDay", leavingDay);
		} else {
			line.put("packetPeriodMoney", 0);
			line.put("dayCount", 0);
			line.put("beginTime", null);
			line.put("endTime", null);
			line.put("leavingDay", "");
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
	}


	/**
	 * 34-交换电池
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ExchangeBatteryParam {
		public String batteryOrderId;
		public String batteryId;
	}

	@ResponseBody
	@RequestMapping(value = "/exchange_battery.htm")
	public RestResult ExchangeBattery(@RequestBody ExchangeBatteryParam param) {

		Battery battery = batteryService.find(param.batteryId);
		if (battery == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
		}
		batteryOrderService.exchangeBattery(param.batteryOrderId, param.batteryId);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	/**
	 * 35- 转电池退租
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ToBackBatteryOrderParam {
		public String batteryOrderId;
	}

	@ResponseBody
	@RequestMapping(value = "/to_back_battery_order.htm")
	public RestResult ToBackBatteryOrder(@RequestBody ToBackBatteryOrderParam param) {

		batteryOrderService.toBackBatteryOrder(param.batteryOrderId);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CustomerOverTimeListParam {
		public int offset;
		public int limit;
	}

	@ResponseBody
	@RequestMapping("/customer_over_time_list.htm")
	public RestResult customerOverTimeList(@RequestBody CustomerOverTimeListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}
		return batteryOrderService.customerOverTimeList(agentId, param.offset, param.limit);
	}

}
