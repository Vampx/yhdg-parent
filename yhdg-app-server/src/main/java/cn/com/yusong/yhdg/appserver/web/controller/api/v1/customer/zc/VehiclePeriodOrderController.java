package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.appserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleVipPriceService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zc_vehicle_period_order")
@RequestMapping(value = "/api/v1/customer/zc/vehicle_period_order")
public class VehiclePeriodOrderController extends ApiController {
	static final Logger log = LogManager.getLogger(VehiclePeriodOrderController.class);

	@Autowired
	CustomerCouponTicketService customerCouponTicketService;
	@Autowired
	AppConfig appConfig;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	AgentSystemConfigService agentSystemConfigService;
	@Autowired
	VehiclePeriodOrderService vehiclePeriodOrderService;
	@Autowired
	BatteryOrderService batteryOrderService;
	@Autowired
	CustomerService customerService;
	@Autowired
	GroupOrderService groupOrderService;
	@Autowired
	RentPriceService rentPriceService;
	@Autowired
	VehicleVipPriceService vehicleVipPriceService;
	@Autowired
	AgentService agentService;
	@Autowired
	PartnerService partnerService;
	@Autowired
	WxPayServiceHolder wxPayServiceHolder;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CreateByWeixinParam {
		public long priceId;
		public int vipPriceId;
		public int rentPrice;
		public long couponTicketId;
	}

	/**
	 * APP\客户版\租车\8-租车套餐支付(多通道)
	 */
	@ResponseBody
	@RequestMapping("/create_by_multi")
	public RestResult createByMulti(@Valid @RequestBody CreateByWeixinParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		return vehiclePeriodOrderService.payByMulti(
				customerId,
				param.vipPriceId,
				param.priceId,
				param.rentPrice,
				param.couponTicketId);

	}

	@ResponseBody
	@RequestMapping("/create_by_weixin_mp")
	public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		Customer customer = customerService.find(customerId);

		RestResult restResult = null;
		try {
			restResult = vehiclePeriodOrderService.payByThird(
					customerId,
					param.vipPriceId,
					param.priceId,
					param.rentPrice,
					param.couponTicketId,
					ConstEnum.PayType.WEIXIN_MP);
			if (restResult.getCode() != 0) {
				return restResult;
			}
		} catch (IllegalArgumentException e) {
			log.error("packetPeriodOrderService.payByThird error", e);
			e.printStackTrace();
			return RestResult.result(RespCode.CODE_2.getValue(), e.getMessage());
		}
		WeixinmpPayOrder weixinmpPayOrder = (WeixinmpPayOrder) restResult.getData();

		WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
		orderRequest.setBody("套餐购买支付");
		orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
		orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
		orderRequest.setOpenid(customer.getMpOpenId());
		orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
		orderRequest.setTimeStart(null);
		orderRequest.setTimeExpire(null);
		orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);


		try {
			//实例化客户端
			WxPayService wxPayService = wxPayServiceHolder.getMp(weixinmpPayOrder.getPartnerId());
			if(wxPayService == null) {
				throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", weixinmpPayOrder.getPartnerId()));
			}
			log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
			WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
			if (log.isDebugEnabled()) {
				String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
				log.debug("WxMpPrepayIdResult: {}", string);
			}

			if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
				long now = System.currentTimeMillis();
				String timeStamp = String.format("%d", now / 1000);
				String nonceStr = String.format("%d", now);
				String signType = "MD5";

				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("appId", wxPayService.getConfig().getAppId());
				map1.put("timeStamp", timeStamp);
				map1.put("nonceStr", nonceStr);
				map1.put("package", "prepay_id=" + result.getPrepayId());
				map1.put("signType", signType);
				String paySign = AppUtils.sign(map1, wxPayService.getConfig().getMchKey(), AppUtils.SignType.MD5).toUpperCase();

				Map data = new HashMap<String, String>();
				data.put("appId", wxPayService.getConfig().getAppId());
				data.put("payAppId", customer.getPartnerId());
				data.put("prepayId", result.getPrepayId());
				data.put("package", "prepay_id=" + result.getPrepayId());
				data.put("timeStamp", timeStamp);
				data.put("nonceStr", nonceStr);
				data.put("signType", signType);
				data.put("paySign", paySign);

				if (log.isDebugEnabled()) {
					log.debug("data: {}", data);
				}
				return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
			}
		} catch (WxPayException e) {
			log.error("unifiedOrder 统一下单失败, {}", weixinmpPayOrder.getId());
			log.error("unifiedOrder error", e);
		}

		return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");

	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class createByAlipayfwParam {
		public int vipPriceId;
		public long priceId;
		public int rentPrice;
		public long couponTicketId;
	}

	/**
	 * 客户购买包时段套餐(支付宝 生活号)
	 */
	@ResponseBody
	@RequestMapping("/create_by_alipay_fw.htm")
	public RestResult createByAlipayFw(@Valid @RequestBody createByAlipayfwParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;
		return vehiclePeriodOrderService.payByThird(
				customerId,
				param.vipPriceId,
				param.priceId,
				param.rentPrice,
				param.couponTicketId,
				ConstEnum.PayType.ALIPAY_FW);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class createByBalanceParam {
		public int vipPriceId;
		public long priceId;
		public int rentPrice;
		public long couponTicketId;
	}

	@ResponseBody
	@RequestMapping("/create_by_balance.htm")
	public RestResult createByBalance(@Valid @RequestBody createByBalanceParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		RestResult restResult = null;
		try {
			restResult = vehiclePeriodOrderService.payBalance(
					customerId,
					param.vipPriceId,
					param.priceId,
					param.rentPrice,
					param.couponTicketId);
			if (restResult.getCode() != 0) {
				return restResult;
			}
		} catch (CouponTicketNotAvailableException e) {
			return RestResult.result(RespCode.CODE_2.getValue(), "优惠券不可用");
		} catch (BalanceNotEnoughException e) {
			return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
		}
		return RestResult.SUCCESS;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CouponTicketListParam {
		public int status;
		public int offset;
		public int limit;
	}

	/**
	 * 16-查询我的优惠券
	 * <p>
	 * coupon_ticket_list
	 */

	@ResponseBody
	@RequestMapping(value = "/coupon_ticket_list.htm")
	public RestResult couponTicketList(@Valid @RequestBody CouponTicketListParam param) {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		Customer customer = customerService.find(customerId);
		if (customer.getMobile() == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "无可用优惠券");
		}
		List<Map> list = new ArrayList<Map>(20);
		List<CustomerCouponTicket> ticketList = customerCouponTicketService.findList(null, customer.getMobile(),null,null, param.status, CustomerCouponTicket.Category.VEHICLE.getValue(), param.offset, param.limit);
		if (ticketList != null) {
			for (CustomerCouponTicket ticket : ticketList) {

				Map mapTicket = new HashMap();
				mapTicket.put("id", ticket.getId());
				if (ticket.getAgentId() != null) {
					Agent agent = agentService.find(ticket.getAgentId());
					mapTicket.put("agentName", agent.getAgentName());
				} else {
					mapTicket.put("agentName", "");
				}

				mapTicket.put("ticketTypeName", ticket.getTicketTypeName());
				mapTicket.put("ticketName", ticket.getTicketName() == null ? "" : ticket.getTicketName());
				mapTicket.put("money", ticket.getMoney() == null ? 0 : ticket.getMoney());
				mapTicket.put("expireTime", ticket.getExpireTime() != null ? DateFormatUtils.format(ticket.getExpireTime(), Constant.DATE_FORMAT) : "");
				list.add(mapTicket);
			}
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
	}

}
