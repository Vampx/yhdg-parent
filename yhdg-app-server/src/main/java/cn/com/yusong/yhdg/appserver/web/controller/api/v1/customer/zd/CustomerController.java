package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zd.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
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
import java.io.IOException;
import java.util.*;

@Controller("api_v1_customer_zd_customer")
@RequestMapping(value = "/api/v1/customer/zd/customer")
public class CustomerController extends ApiController {

    static final Logger log = LogManager.getLogger(CustomerController.class);
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AgentService agentService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    PartnerMpOpenIdService partnerMpOpenIdService;
    @Autowired
    protected AppConfig appConfig;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    PhoneappService phoneappService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    ShopService shopService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

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

            Integer batteryType = null,foregift = null;
            Long foregiftId = null;
            String foregiftOrderId = null,shopId = null;
            Integer vehicleForegiftFlag = null;
            CustomerRentInfo customerRentInfo = customerRentInfoService.find(customerId);
            if(customerRentInfo != null){
                batteryType = customerRentInfo.getBatteryType();
                foregift = customerRentInfo.getForegift();
                foregiftOrderId = customerRentInfo.getForegiftOrderId();
                shopId =  customerRentInfo.getBalanceShopId();
                vehicleForegiftFlag = customerRentInfo.getVehicleForegiftFlag();
                RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(foregiftOrderId);
                if (rentForegiftOrder != null) {
                    foregiftId = rentForegiftOrder.getForegiftId();
                }
            }
            map.put("shopId", shopId);
            map.put("foregift", foregift);
            map.put("batteryType", batteryType);
            map.put("vehicleForegiftFlag", vehicleForegiftFlag);
            if(batteryType != null ){
                map.put("batteryTypeName", systemBatteryTypeService.find(batteryType).getTypeName());
            }
            map.put("foregiftId",foregiftId);
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
            int waitPayCount = customerInstallmentRecordPayDetailService.findCountByCustomerId(customerId, CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.RENT.getValue(), new Date());
            if (waitPayCount > 0) {
                map.put("authStep", "expire_debt");
            }

            //存在租电多通道待支付订单
            if(customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.ZD.getValue()) > 0){
                map.put("authStep", "multi_wait_pay");
            }

            map.put("isBindMp", StringUtils.isNotEmpty(customer.getMpOpenId()) ? 1 : 0);
            map.put("isBindFw", StringUtils.isNotEmpty(customer.getFwOpenId()) ? 1 : 0);


            String rentRemainTime = null;
            int rentMoney = 0;
            long now = System.currentTimeMillis();
            Map lastEndTimeMap = rentPeriodOrderService.getRentExpireTime(customerRentInfo);
            if (lastEndTimeMap != null) {
                Date endTime = (Date)lastEndTimeMap.get("endTime");
                if (endTime != null && now < endTime.getTime()) {
                    rentRemainTime = AppUtils.formatTimeUnit((endTime.getTime() - now) / 1000);
                    rentMoney = (Integer) lastEndTimeMap.get("money");
                }
            }
            map.put("rentRemainTime", rentRemainTime);
            map.put("rentMoney", rentMoney);
            map.put("idCardFace", staticImagePath(customer.getIdCardFace()));
            map.put("idCardRear", staticImagePath(customer.getIdCardRear()));

            if (customer.getPartnerId() != null) {
                map.put("couponTicketCount", customerCouponTicketService.findCount(customer.getPartnerId(), customer.getMobile(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Category.RENT.getValue()));
            }else{
                map.put("couponTicketCount", 0);
            }

            String insuranceRemainTime = null;
            RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderService.findByCustomerId(customerId, batteryType, RentInsuranceOrder.Status.PAID.getValue());
            if(rentInsuranceOrder != null) {
                if (now < rentInsuranceOrder.getEndTime().getTime()) {
                    insuranceRemainTime = AppUtils.formatTimeUnit((rentInsuranceOrder.getEndTime().getTime() - now) / 1000);
                }
            }
            map.put("insuranceRemainTime", insuranceRemainTime);

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

    /**
     * 查询租赁电池信息
     */
    @ResponseBody
    @RequestMapping(value = "/battery_info.htm")
    public RestResult getBatteryInfo() {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "该用户不存在");
        }

        List<CustomerRentBattery> batteryList = customerRentBatteryMapper.findListByCustomer(customer.getId());

        List<Map> lines = new ArrayList<Map>(batteryList.size());
        for (CustomerRentBattery customerRentBattery : batteryList) {
            Map map = new HashMap();
            Battery battery = batteryService.find(customerRentBattery.getBatteryId());
            if (battery != null) {

                Integer voltage = battery.getVoltage();
                map.put("voltage", voltage);
                map.put("electricity",battery.getElectricity());

                String[] split = battery.getTemp().split(",");
                if(split.length > 0){
                    map.put("batteryTemp", split[0]);
                }else{
                    map.put("batteryTemp", null);
                }

                map.put("batteryId", battery.getId());
                map.put("batteryType", battery.getBatteryType());
                if(battery.getType() != null ){
                    map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
                }
                map.put("batteryOrderId", battery.getOrderId());
                map.put("Status", battery.getStatus());
                map.put("shellCode", battery.getShellCode());
                map.put("code", battery.getCode());
                map.put("lng", battery.getLng());
                map.put("lat", battery.getLat());
                map.put("volume", battery.getVolume());
                Integer designMileage;
                if (customer.getAgentId() != null && customer.getAgentId() != 0) {
                    designMileage = Integer.parseInt(agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue(), customer.getAgentId()));
                } else {
                    designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
                }
                map.put("estimateDistance", designMileage * battery.getVolume() / 100);//预计行驶里程

                if(battery.getFetStatus() != null && battery.getFetStatus() == Battery.FetStatus.CHARGE.getValue()){
                    if(battery.getVolume()  < 60 ){
                        map.put("chargeStatus", 1);//快速充电
                    }else if(battery.getVolume()  >= 60 && battery.getVolume() < 90){
                        map.put("chargeStatus", 2);// 连续性充电
                    }else {
                        map.put("chargeStatus", 3);// 涓流充电
                    }
                    map.put("chargeFullTime",  (int) Math.ceil(((100 - battery.getVolume()) * 0.7)));//预计充满时间
                }else{
                    map.put("chargeStatus", 0);//不充电
                    map.put("chargeFullTime", null);
                }

                String statusName = Battery.Status.getName(battery.getStatus());
                if (battery.getChargeStatus() != null && Battery.ChargeStatus.CHARGING.getValue() == battery.getChargeStatus()) {
                    statusName += "/充电中";
                }
                map.put("statusName", statusName);
                map.put("isOnLine", battery.getIsOnline());

                //包含自救版本并且出现欠压故障才能自救
                String batteryVersions = systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_RESCUE_VERSION.getValue());
                if((battery.getMonomerLowvoltageFaultLogId() != null || battery.getWholeLowvoltageFaultLogId() != null)
                        && (battery.getVersion() != null && batteryVersions.indexOf(battery.getVersion() )> -1) ){
                    map.put("showRescue", ConstEnum.Flag.TRUE.getValue());
                }else{
                    map.put("showRescue", ConstEnum.Flag.FALSE.getValue());
                }
            }

            lines.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", lines);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindBatteryParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/bind_battery.htm")
    public RestResult bindBattery(@Valid @RequestBody BindBatteryParam param) throws IOException {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        Agent agent = agentService.find(battery.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoService.find(customerId);
        if (customerRentInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有押金");
        }

        //取新电
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.RENT_BATTERY_COUNT.getValue(), battery.getAgentId());
        if(!StringUtils.isEmpty(maxCountStr)){
            maxCount = Integer.parseInt(maxCountStr);
        }
        List<CustomerRentBattery> batteryList = customerRentBatteryService.findListByCustomer(customer.getId());
        if(batteryList.size() >= maxCount) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已拥有最大数目的电池");
        }

        ShopStoreBattery shopStoreBattery = shopStoreBatteryService.findByBattery(battery.getId());
        if(shopStoreBattery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "非门店库存电池无法绑定");
        }

        Shop shop = shopService.find(shopStoreBattery.getShopId());
        if(shop == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        //用户绑定电池
        customerRentBatteryService.bind(agent, customer, battery, shop, customerRentInfo);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CouponTicketListParam {
        public int status;
        public int offset;
        public int limit;
    }

    /**
     * 查询我的优惠券
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
        List<CustomerCouponTicket> ticketList = customerCouponTicketService.findList(null, customer.getMobile(),null,null, param.status, CustomerCouponTicket.Category.RENT.getValue(), param.offset, param.limit);
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
