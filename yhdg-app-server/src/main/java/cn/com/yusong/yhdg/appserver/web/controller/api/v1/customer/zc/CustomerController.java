package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zc.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller("api_v1_customer_zc_customer")
@RequestMapping(value = "/api/v1/customer/zc/customer")
public class CustomerController extends ApiController {
	static final Logger log = LogManager.getLogger(CustomerController.class);

	@Autowired
	AgentService agentService;
	@Autowired
	CustomerService customerService;
	@Autowired
	CustomerCouponTicketService customerCouponTicketService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	SystemBatteryTypeService systemBatteryTypeService;
	@Autowired
	WeixinmpService weixinmpService;
	@Autowired
	AlipayfwService alipayfwService;
	@Autowired
	PhoneappService phoneappService;
	@Autowired
	CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;
	@Autowired
	CustomerMultiOrderService customerMultiOrderService;
	@Autowired
	CustomerVehicleInfoService customerVehicleInfoService;
	@Autowired
	ShopStoreVehicleService shopStoreVehicleService;
	@Autowired
	VehiclePeriodOrderService vehiclePeriodOrderService;
	@Autowired
	PriceSettingService priceSettingService;
	@Autowired
	RentPriceService rentPriceService;
	@Autowired
	VehicleVipPriceService vehicleVipPriceService;
	@Autowired
	VehicleService vehicleService;
	@Autowired
	ShopService shopService;
	@Autowired
	PacketPeriodOrderService packetPeriodOrderService;
	@Autowired
	GroupOrderService groupOrderService;
	@Autowired
	VehicleForegiftOrderService vehicleForegiftOrderService;

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

	@ResponseBody
	@RequestMapping(value = "/info.htm")
	public RestResult getInfo() {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;
		Map map = new HashMap(20);
		Customer customer = customerService.find(customerId);
		if (customer != null) {
			map.put("id", customer.getId());
			map.put("idCard", customer.getIdCard());
			map.put("nickname", customer.getNickname());
			map.put("fullname", customer.getFullname());
			map.put("mobile", customer.getMobile());
			map.put("photoPath", staticImagePath(customer.getPhotoPath()));
			map.put("balance", customer.getBalance());
			map.put("deposit", customer.getBalance());
			map.put("agentId", customer.getAgentId());
			Integer vehicleForegift = null;
			String foregiftOrderId = null;
			String balanceShopId = null;
			String vinNo =null;
			Integer foregift = null;
			Long priceSettingId = null;
			Integer priceId = null;
			Integer vipPriceId = null;
			Integer category =null;
			Date beginTime =null;
			Integer modeId = null;
			String	vehicleName = null;
			//正在使用套餐开始时间结束时间
			Date beingBeginTime = null;
			Date beingEndTime = null;
			Long beingPacketRemainTime = null;
			Date rentTime =null;
			Integer foregiftMoney = null;
			CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoService.findByCustomerId(customerId);
			if(customerVehicleInfo != null){
				balanceShopId = customerVehicleInfo.getBalanceShopId();
				foregiftOrderId = customerVehicleInfo.getForegiftOrderId();
				foregift=customerVehicleInfo.getForegift();
				category = customerVehicleInfo.getCategory();
				modeId = customerVehicleInfo.getModelId();
				rentTime = customerVehicleInfo.getCreateTime();
				if (customerVehicleInfo.getVipPriceId() != null && customerVehicleInfo.getVipPriceId() != 0) {
					VehicleVipPrice vehicleVipPrice = vehicleVipPriceService.find(customerVehicleInfo.getVipPriceId());
					if (vehicleVipPrice != null) {
						vipPriceId = customerVehicleInfo.getVipPriceId();
						priceId = customerVehicleInfo.getRentPriceId().intValue();
						priceSettingId = vehicleVipPrice.getPriceSettingId().longValue();
						vehicleForegift = vehicleVipPrice.getVehicleForegiftPrice();
						PriceSetting priceSetting = priceSettingService.find(vehicleVipPrice.getPriceSettingId().longValue());
						if (priceSetting != null) {
							vehicleName = priceSetting.getVehicleName();
						}
					}
				} else if (customerVehicleInfo.getRentPriceId() != null && customerVehicleInfo.getRentPriceId() != 0) {
					RentPrice rentPrice = rentPriceService.find(customerVehicleInfo.getRentPriceId());
					if (rentPrice != null) {
						vipPriceId = customerVehicleInfo.getVipPriceId();
						priceId = customerVehicleInfo.getRentPriceId().intValue();
						priceSettingId = rentPrice.getPriceSettingId();
						vehicleForegift = rentPrice.getVehicleForegiftPrice();
						PriceSetting priceSetting = priceSettingService.find(rentPrice.getPriceSettingId());
						if (priceSetting != null) {
							vehicleName = priceSetting.getVehicleName();
						}
					}
				}
				//支付押金金额
				if(customerVehicleInfo.getForegiftOrderId()!=null){
					VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderService.find(customerVehicleInfo.getForegiftOrderId());
					if(vehicleForegiftOrder != null ){
						GroupOrder byVehicleForegiftId = groupOrderService.findByVehicleForegiftId(vehicleForegiftOrder.getId());
						if(byVehicleForegiftId !=null){
							foregiftMoney = byVehicleForegiftId.getForegiftMoney();
						}
					}
				}
				if(customerVehicleInfo.getVehicleId()!=null){
					Vehicle vehicle = vehicleService.find(customerVehicleInfo.getVehicleId());
					if(vehicle!=null && StringUtils.isNotEmpty(vehicle.getVinNo())){
						vinNo = vehicle.getVinNo();
					}
				}
			}
			if(rentTime!=null){
				map.put("rentTime",DateFormatUtils.format(rentTime, Constant.DATE_FORMAT));
			}else {
				map.put("rentTime",null);
			}
			map.put("foregiftMoney",foregiftMoney);
			map.put("modeId",modeId);
			map.put("vehicleName",vehicleName);
			map.put("category",category);
			map.put("priceSettingId",priceSettingId);
			map.put("priceId",priceId);
			map.put("vipPriceId", vipPriceId);
			map.put("vehicleForegift",vehicleForegift);
			map.put("vinNo",vinNo);
			map.put("shopId", balanceShopId);
			map.put("foregift",foregift);
			map.put("foregiftOrderId",foregiftOrderId);
			map.put("authStep", "ok");
			if (StringUtils.isEmpty(customer.getMobile())) {
				map.put("authStep", "not_mobile");
			} else if (customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()
					|| customer.getAuthStatus() == Customer.AuthStatus.AUTO_FAIL.getValue()
					|| customer.getAuthStatus() == Customer.AuthStatus.AUDIT_REFUSE.getValue()) {
				map.put("authStep", "not_authentication");
			} else if (StringUtils.isEmpty(customer.getPassword())) {
				if (StringUtils.isEmpty(tokenData.openid)) {
					map.put("authStep", "not_password");
				}
			}
			//有分期欠款记录
			int waitPayCount = customerInstallmentRecordPayDetailService.findCountByCustomerId(customerId, CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.VEHICLE.getValue(), new Date());
			if (waitPayCount > 0) {
				map.put("authStep", "expire_debt");
			}

			//存在多通道待支付订单
			if(customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.ZC.getValue()) > 0){
				map.put("authStep", "multi_wait_pay");
			}
			map.put("isBindMp", StringUtils.isNotEmpty(customer.getMpOpenId()) ? 1 : 0);
			map.put("isBindFw", StringUtils.isNotEmpty(customer.getFwOpenId()) ? 1 : 0);
			//租金处理
			Long packetRemainTime = null;
			Integer rentMoney = null;
			Date endTime = null;
			long now = System.currentTimeMillis();
			VehiclePeriodOrder lastEndTime = vehiclePeriodOrderService.findLastEndTime(customerId);
			List<VehiclePeriodOrder> list = vehiclePeriodOrderService.findListByNoUsed(customerId);
			if (lastEndTime != null) {
				if (now < lastEndTime.getEndTime().getTime()) {
					GroupOrder byVehiclePeriodId = groupOrderService.findByVehiclePeriodId(lastEndTime.getId());
					if(byVehiclePeriodId!=null){
						rentMoney = byVehiclePeriodId.getRentPeriodMoney();
					}else{
						rentMoney = lastEndTime.getMoney();
					}
					packetRemainTime = (lastEndTime.getEndTime().getTime() - now) / 1000;
					beginTime=lastEndTime.getBeginTime();
					endTime = lastEndTime.getEndTime();
					beingBeginTime = lastEndTime.getBeginTime();
					beingEndTime = lastEndTime.getEndTime();
					beingPacketRemainTime = (lastEndTime.getEndTime().getTime() - now) / 1000;
				}
			}
			if(list.size() > 0){
				for(VehiclePeriodOrder order : list){
					GroupOrder byVehiclePeriodId = groupOrderService.findByVehiclePeriodId(order.getId());
					if(packetRemainTime != null){
						packetRemainTime += order.getDayCount() * 24 * 3600 * 1l;
						if(byVehiclePeriodId!=null){
							rentMoney += byVehiclePeriodId.getRentPeriodMoney();
						}else{
							rentMoney += lastEndTime.getMoney();
						}
						endTime = DateUtils.addDays(endTime,order.getDayCount());
					}else{
						packetRemainTime = order.getDayCount() * 24 * 3600 * 1l;
						if(byVehiclePeriodId!=null){
							rentMoney = byVehiclePeriodId.getRentPeriodMoney();
						}else{
							rentMoney = lastEndTime.getMoney();
						}
						endTime = DateUtils.addDays(new Date(),order.getDayCount());
					}
				}
			}
			if(packetRemainTime != null){
				map.put("endTime", DateFormatUtils.format(endTime, Constant.DATE_FORMAT));
				map.put("rentRemainTime",  AppUtils.formatTimeUnit(packetRemainTime));
				map.put("rentMoney", rentMoney);
				if(beingPacketRemainTime != null){
					map.put("beingPacketRemainTime",  AppUtils.formatTimeUnit(beingPacketRemainTime));
				}else {
					map.put("beingPacketRemainTime", null);
				}
				if(beingEndTime !=null){
					map.put("beingEndTime", DateFormatUtils.format(beingEndTime, Constant.DATE_FORMAT));
				}else{
					map.put("beingEndTime",null);
				}
				if(beginTime!=null){
					map.put("beginTime", DateFormatUtils.format(beginTime, Constant.DATE_FORMAT));
					map.put("beingBeginTime", DateFormatUtils.format(beingBeginTime, Constant.DATE_FORMAT));
				}else {
					map.put("beginTime",null);
					map.put("beingBeginTime",null);
				}
			}else{
				map.put("beingEndTime",null);
				map.put("beginTime",null);
				map.put("endTime", null);
				map.put("rentRemainTime", null);
				map.put("rentMoney", null);
				map.put("beginTime",null);
				map.put("beingPacketRemainTime", null);
			}
			map.put("idCardFace", staticImagePath(customer.getIdCardFace()));
			map.put("idCardRear", staticImagePath(customer.getIdCardRear()));

			if (customer.getPartnerId() != null) {
				map.put("couponTicketCount", customerCouponTicketService.findCount(customer.getPartnerId(), customer.getMobile(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Category.VEHICLE.getValue()));
			}else{
				map.put("couponTicketCount", 0);
			}
			map.put("exchangeCount", vehicleService.findTodayOrderCount(customer.getId()));
			int systemAuthType = Weixinmp.AuthType.AUTO.getValue();
			if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_MP) {
				Weixinmp weixinmp = weixinmpService.find(tokenData.appId);
				if (weixinmp == null) {
					return RestResult.dataResult(RespCode.CODE_2.getValue(), "公众号配置不存在", null);
				}
				systemAuthType = weixinmp.getAuthType();

			} else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_FW) {
				Alipayfw alipayfw = alipayfwService.find(tokenData.appId);
				if (alipayfw == null) {
					return RestResult.dataResult(RespCode.CODE_2.getValue(), "生活号配置不存在", null);
				}
				systemAuthType = alipayfw.getAuthType();

			} else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_APP) {
				Phoneapp phoneapp = phoneappService.find(tokenData.appId);
				if (phoneapp == null) {
					return RestResult.dataResult(RespCode.CODE_2.getValue(), "手机端配置不存在", null);
				}
				systemAuthType = phoneapp.getAuthType();
			}

			map.put("systemAuthType", systemAuthType);
			map.put("authStatus", customer.getAuthStatus());
			map.put("authMessage", customer.getAuthMessage());

			return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
		} else {
			return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BindVehicleParam {
		public String vinNo;
	}

	@ResponseBody
	@RequestMapping(value = "/bind_vehicle.htm")
	public RestResult bindBattery(@Valid @RequestBody BindVehicleParam param) throws IOException {
		TokenCache.Data tokenData = getTokenData();
		long customerId = tokenData.customerId;

		Vehicle vehicle = vehicleService.findByVinNo(param.vinNo);
		if (vehicle == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "车辆不存在");
		}

		Agent agent = agentService.find(vehicle.getAgentId());
		if (agent == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
		}

		Customer customer = customerService.find(customerId);
		if (customer == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
		}

		CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoService.findByCustomerId(customerId);
		if (customerVehicleInfo == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴纳租车押金");
		}

		if(customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null){
			return RestResult.result(RespCode.CODE_2.getValue(), "您已经拥有可租车辆");
		}

		ShopStoreVehicle shopStoreVehicle = shopStoreVehicleService.findByVehicleId(vehicle.getId());
		if(shopStoreVehicle == null){
			return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存车辆无法绑定");
		}

		Shop shop = shopService.find(shopStoreVehicle.getShopId());
		if(shop == null){
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}

		return vehicleService.bindVehicle(agent, customer, shop, vehicle, customerVehicleInfo, shopStoreVehicle);
	}

}
