package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.appserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleVipPriceService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zc_group_order")
@RequestMapping(value = "/api/v1/customer/zc/group_order")
public class GroupOrderController extends ApiController {
	static final Logger log = LogManager.getLogger(GroupOrderController.class);

	@Autowired
	CustomerService customerService;
	@Autowired
	GroupOrderService groupOrderService;
	@Autowired
	RentPriceService rentPriceService;
	@Autowired
	VehicleVipPriceService vehicleVipPriceService;
	@Autowired
	VehiclePeriodOrderService vehiclePeriodOrderService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public int status;
		public int offset;
		public int limit;
	}

	/**
	 * 10-查询租车组合订单列表
	 * <p>
	 *
	 */

	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		Customer customer = customerService.find(customerId);
		if (customer == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
		}
		List<GroupOrder> groupOrderList = new ArrayList<GroupOrder>();
		if (param.status == 1) {
			groupOrderList = groupOrderService.findList(customerId, param.offset, param.limit, null);
		} else {
			groupOrderList = groupOrderService.findList(customerId, param.offset, param.limit, param.status);
		}

		List<Map> list = new ArrayList<Map>();
		for (GroupOrder groupOrder : groupOrderList) {
			if (groupOrder.getStatus() == GroupOrder.Status.WAIT_PAY.getValue()) {
				continue;
			}
			NotNullMap orderMap = new NotNullMap();
			orderMap.put("id", groupOrder.getId());
			orderMap.put("vehicleName", groupOrder.getVehicleName());
			orderMap.put("priceName", null);
			RentPrice rentPrice = rentPriceService.find(groupOrder.getRentPriceId());
			if (rentPrice != null) {
				orderMap.put("priceName", rentPrice.getPriceName());
			}
			orderMap.put("money", groupOrder.getMoney());
			orderMap.putDateTime("createTime", groupOrder.getCreateTime());
			orderMap.put("status", groupOrder.getStatus());
			orderMap.put("vehicleDayCount", groupOrder.getVehicleDayCount());
			orderMap.put("batteryDayCount", groupOrder.getBatteryDayCount());
			orderMap.put("customerName", groupOrder.getCustomerFullname());
			orderMap.put("customerMobile", groupOrder.getCustomerMobile());
			orderMap.put("payTypeName", groupOrder.getPayTypeName());
			orderMap.putDateTime("payTime", groupOrder.getPayTime());
			list.add(orderMap);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public String id;
	}

	/**
	 * 19-查询租车租金订单详情
	 * <p>
	 * coupon_ticket_list
	 */

	@ResponseBody
	@RequestMapping(value = "/detail.htm")
	public RestResult detail(@Valid @RequestBody DetailParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		Customer customer = customerService.find(customerId);
		if (customer == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
		}
		GroupOrder groupOrder = groupOrderService.find(param.id);
		if (groupOrder == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
		}

		NotNullMap data = new NotNullMap();
		data.put("id", groupOrder.getId());
		data.put("vehicleName", groupOrder.getVehicleName());
		data.put("priceName", null);
		VehicleVipPrice vehicleVipPrice = null;
		vehicleVipPrice = vehicleVipPriceService.findByRentPriceId(groupOrder.getRentPriceId());
		if (vehicleVipPrice != null) {
			data.put("priceName", vehicleVipPrice.getPriceName());
		} else {
			RentPrice rentPrice = rentPriceService.find(groupOrder.getRentPriceId());
			if (rentPrice != null) {
				data.put("priceName", rentPrice.getPriceName());
			}
		}
		data.putDateTime("createTime", groupOrder.getCreateTime());
		data.putDateTime("payTime", groupOrder.getPayTime());
		data.put("shopName", groupOrder.getShopName());
		data.put("vehicleDayCount", groupOrder.getVehicleDayCount());
		data.put("batteryDayCount", groupOrder.getBatteryDayCount());
		data.put("foregiftMoney", groupOrder.getForegiftMoney());
		data.put("packetPeriodMoney", groupOrder.getRentPeriodMoney());
		data.put("category", 0);
		if (groupOrder.getBatteryForegiftId() != null) {
			data.put("category", groupOrder.getCategory());
		}
		data.put("deductionMoney", groupOrder.getDeductionTicketMoney());
		data.put("payType", groupOrder.getPayType());
		data.put("status", groupOrder.getStatus());
		data.put("money", (groupOrder.getMoney()));
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}
}
