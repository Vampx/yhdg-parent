package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.*;

@Controller("api_v1_customer_hdg_battery_order")
@RequestMapping(value = "/api/v1/customer/hdg/battery_order")
public class BatteryOrderController extends ApiController {
    final static Logger log = LogManager.getLogger(BatteryOrderController.class);

    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryOrderRefundSerService batteryOrderRefundSerService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryParameterService batteryParameterService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByAlipayParam {
        public String orderId;
        public long couponTicketId;
    }

    /**
     * 客户支付旧电费用(支付宝)
     */
    @ResponseBody
    @RequestMapping(value = "/pay_by_alipay")
    public RestResult createByAlipay(@Valid @RequestBody PayByAlipayParam param) {
        TokenCache.Data tokenData = getTokenData();
        return batteryOrderService.payByAlipay(false, param.orderId, tokenData.customerId, param.couponTicketId);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByAlipayfwParam {
        public String orderId;
        public long couponTicketId;
    }

    /**
     * 客户支付旧电费用(支付宝 生活号)
     */
    @ResponseBody
    @RequestMapping(value = "/pay_by_alipay_fw")
    public RestResult createByAlipay(@Valid @RequestBody PayByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();
        return batteryOrderService.payByAlipayfw(param.orderId, tokenData.customerId, param.couponTicketId);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByWeixinParam {
        public String orderId;
        public long couponTicketId;
    }

    @ResponseBody
    @RequestMapping(value = "/pay_by_weixin")
    public RestResult createByWeixin(@Valid @RequestBody PayByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();

        return batteryOrderService.payByWeixin(param.orderId, tokenData.customerId, param.couponTicketId);
    }

    @ResponseBody
    @RequestMapping(value = "/pay_by_weixin_mp")
    public RestResult createByWeixinMp(@Valid @RequestBody PayByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        return batteryOrderService.payByWeixinMp(param.orderId, tokenData.customerId, param.couponTicketId);
    }

    @ResponseBody
    @RequestMapping(value = "/pay_by_weixin_ma")
    public RestResult createByWeixinMa(@Valid @RequestBody PayByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        return batteryOrderService.payByWeixinMa(param.orderId, tokenData.customerId, param.couponTicketId);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByBalanceParam {
        public String orderId;
        public long couponTicketId;
    }

    @ResponseBody
    @RequestMapping(value = "/pay_by_balance")
    public RestResult createByBalance(@Valid @RequestBody PayByBalanceParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        RestResult restResult = null;
        try {
            restResult = batteryOrderService.payByBalance(param.orderId, tokenData.customerId, param.couponTicketId);
        } catch (OrderStatusExpireException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单状态过期");
        }

        if (restResult.getCode() == 0) { //操作成功后 打开新箱
            openNewFullBox(param.orderId);
        }

        return restResult;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApplyRefundParam {
        public String orderId;
        public String reason;
    }

    @ResponseBody
    @RequestMapping(value = "/apply_refund.htm")
    public RestResult applyRefund(@Valid @RequestBody ApplyRefundParam param) {
        TokenCache.Data token = getTokenData();
        long customerId = token.customerId;

        BatteryOrder batteryOrder = batteryOrderService.find(param.orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        if (batteryOrder.getOrderStatus() != BatteryOrder.OrderStatus.PAY.getValue() ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "只有已付款状态才可退款");
        }

        if(StringUtils.isEmpty(param.reason)){
            return RestResult.result(RespCode.CODE_2.getValue(), "退款原因不能为空");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        batteryOrder.setRefundMoney(batteryOrder.getMoney());
        batteryOrder.setRefundReason(param.reason);

        return batteryOrderService.refund(batteryOrder, customer);

    }

    /**
     * 查询换电记录
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        @NotNull
        public int offset;
        @NotNull
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list_by_customer.htm")
    public RestResult getList(@RequestBody ListParam param) {
        return batteryOrderService.getListByCustomer(getTokenData().customerId, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackListParam {
        public String orderId;
        @NotNull
        public int offset;
        @NotNull
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/track_list.htm")
    public RestResult trackList(@RequestBody TrackListParam param) {
        return batteryOrderService.findBatteryReportLogByOrderId(param.orderId, param.offset, param.limit);
    }

    protected RestResult openNewFullBox(String orderId) throws InterruptedException {
        BatteryOrder batteryOrder = batteryOrderService.find(orderId);
        String cabinetId = batteryOrder.getPutCabinetId();

        Cabinet cabinet = cabinetService.find(cabinetId);
        Customer customer = customerService.find(batteryOrder.getCustomerId());

        Integer batteryType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        if(customerExchangeInfo != null){
            batteryType = customerExchangeInfo.getBatteryType();
        }else{
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }
        }

        if (batteryType == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
        }

        //查询是否有预约订单
        String bespeakBoxNum = null;
        BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());
        if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinetId)){
            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
        }

        CabinetBox cabinetBox = cabinetBoxService.findOneFull(cabinetId, batteryType, bespeakBoxNum);
        if (cabinetBox == null) {
            int fullCount = cabinetBoxService.findFullCount(cabinetId);
            String errorMessage = "没有符合类型的已充满电池";
            if (fullCount != 0) {
                errorMessage = "扫码者与当前柜子不匹配";
            }
            return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
        }

        Battery battery = batteryService.find(cabinetBox.getBatteryId());
        if (battery == null) {
            String errorMessage = "电池不存在: " + cabinetBox.getBatteryId();
            return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
        }

        if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
            int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
            if (effect == 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }
        }

        //发送开箱指令
        ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
        //如果协议发送成功
        if (result.getCode() == RespCode.CODE_0.getValue()) {
            BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

            NewBoxNum newBoxNum = new NewBoxNum("app-server", batteryOrder.getPutCabinetId(), null, batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum(), new Date());
            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
            memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
//            memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);
            if (log.isDebugEnabled()) {
                log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum());
            }

        } else {
            if (result.getSerial() == -1) {
                cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());

            } else {
                CabinetBox box = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                if (box.getIsOpen() != ConstEnum.Flag.TRUE.getValue()) {
                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单打开满箱失败", customer.getFullname(), customer.getMobile()),
                            customer.getFullname());
                }
                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                NewBoxNum newBoxNum = new NewBoxNum("app-server", batteryOrder.getPutCabinetId(), null, batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum(), new Date());
                String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
                memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//                String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
//                memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

                if (log.isDebugEnabled()) {
                    log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), cabinetBox.getBoxNum());
                }
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    /**
     * 75-查询客户电池信息
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/customer_battery_info.htm")
    public RestResult customerBatteryInfo() throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        NotNullMap data = new NotNullMap();

        data.putLong("id", customer.getId());
        data.putString("fullname", customer.getFullname());
        data.putMobileMask("mobile", customer.getMobile());

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
        CustomerForegiftOrder customerForegiftOrder = null;
        if (customerExchangeInfo != null) {
            data.putInteger("foregiftMoney", customerExchangeInfo.getForegift());
            Integer batteryType = customerExchangeInfo.getBatteryType();
            insuranceOrder = insuranceOrderService.findByCustomerId(customer.getId(), batteryType, InsuranceOrder.Status.PAID.getValue());
            data.putInteger("insuranceMoney", insuranceOrder != null ? insuranceOrder.getPrice() : 0);
            data.putInteger("insurancePaid", insuranceOrder != null ? insuranceOrder.getPaid() : 0);
            data.putInteger("monthCount", insuranceOrder != null ? insuranceOrder.getMonthCount() : 0);
            customerForegiftOrder = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
            data.putInteger("deductionTicketMoney",customerForegiftOrder != null &&  customerForegiftOrder.getDeductionTicketMoney() != null ? customerForegiftOrder.getDeductionTicketMoney() : 0);
        } else {
            data.putInteger("foregiftMoney", 0);
            data.putInteger("insuranceMoney", 0);
            data.putInteger("insurancePaid", 0);
            data.putInteger("monthCount", 0);
            data.putInteger("deductionTicketMoney", 0);
        }

        if (packetPeriodOrder!= null) {
            data.putInteger("packetPeriodMoney", packetPeriodOrder.getPrice());
            data.putInteger("dayCount", packetPeriodOrder.getDayCount());
            data.putDate("beginTime", packetPeriodOrder.getBeginTime());
            data.putDate("endTime", packetPeriodOrder.getEndTime());
            data.putString("leavingDay", leavingDay);
        } else {
            data.putInteger("packetPeriodMoney", 0);
            data.putInteger("dayCount", 0);
            data.putDate("beginTime", null);
            data.putDate("endTime", null);
            data.putString("leavingDay", "");
        }

        List<CustomerExchangeBattery> orderList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        List<NotNullMap> batteryList = new ArrayList<NotNullMap>();
        for (CustomerExchangeBattery customerExchangeBattery : orderList) {
            NotNullMap notNullMap = new NotNullMap();

            Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
            if (battery != null) {
                BatteryParameter batteryParameter = batteryParameterService.find(battery.getId());

                notNullMap.put("batteryId", battery.getId());
                notNullMap.put("status", battery.getStatus());
                notNullMap.put("shellCode", battery.getShellCode());
                notNullMap.put("code", battery.getCode());
                notNullMap.put("lng", battery.getLng());
                notNullMap.put("lat", battery.getLat());
                notNullMap.put("volume", battery.getVolume());
                notNullMap.put("voltage", battery.getVoltage());
                notNullMap.put("electricity", battery.getElectricity());
                Integer designMileage;
                if (customer.getAgentId() != null && customer.getAgentId() != 0) {
                    designMileage = Integer.parseInt(agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue(), customer.getAgentId()));
                } else {
                    designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
                }
                notNullMap.put("estimateDistance", designMileage * battery.getVolume() / 100);//预计行驶里程
                String statusName = Battery.Status.getName(battery.getStatus());
                if (battery.getChargeStatus() != null && Battery.ChargeStatus.CHARGING.getValue() == battery.getChargeStatus()) {
                    statusName += "/充电中";
                }
                notNullMap.put("statusName", statusName);

            }
            BatteryOrder batteryOrder = batteryOrderService.find(customerExchangeBattery.getBatteryOrderId());

            notNullMap.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
            notNullMap.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
            notNullMap.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
            notNullMap.putDateTime("takeTime", batteryOrder.getTakeTime());

            notNullMap.putString("putCabinetId", batteryOrder.getPutCabinetId());
            notNullMap.putString("putCabinetName", batteryOrder.getPutCabinetName());
            notNullMap.putString("putBoxNum", batteryOrder.getPutBoxNum());
            notNullMap.putDateTime("putTime", batteryOrder.getPutTime());

            notNullMap.putInteger("beginVolume", batteryOrder.getInitVolume());
            notNullMap.putInteger("endVolume", batteryOrder.getCurrentVolume());

            //包含自救版本并且出现欠压故障才能自救
            String batteryVersions = systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_RESCUE_VERSION.getValue());
            if((battery.getMonomerLowvoltageFaultLogId() != null || battery.getWholeLowvoltageFaultLogId() != null)
                    && (battery.getVersion() != null && batteryVersions.indexOf(battery.getVersion() )> -1) ){
                notNullMap.put("showRescue", ConstEnum.Flag.TRUE.getValue());
            }else{
                notNullMap.put("showRescue", ConstEnum.Flag.FALSE.getValue());
            }

            batteryList.add(notNullMap);
        }
        data.put("batteryList", batteryList);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryLatLngParam {
        public String batteryId;
    }

    /**
     * 52-查询电池经纬度
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/battery_lat_lng.htm")
    public RestResult batteryLatLng(@Valid @RequestBody BatteryLatLngParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        NotNullMap data = new NotNullMap();
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        data.put("lat", battery.getLat());
        data.put("lng", battery.getLng());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


}
