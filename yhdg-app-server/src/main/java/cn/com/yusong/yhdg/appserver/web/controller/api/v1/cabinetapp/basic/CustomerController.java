package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.LoginQrcode;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_cabinet_app_basic_customer")
@RequestMapping(value = "/api/v1/cabinet_app/basic/customer")
public class CustomerController extends ApiController {
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CustomerOfflineBatteryService customerOfflineBatteryService;
    @Autowired
    CustomerOfflineExchangeRecordService customerOfflineExchangeRecordService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginParam {
        public String mobile;
        public String password;
    }

    @ResponseBody
    @RequestMapping(value = "/login.htm")
    public RestResult login(@Valid @RequestBody LoginParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        TokenCache.Data tokenData = getTokenData();
        String cabinetId = tokenData.cabinetId;

        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "设备不存在");
        }

        Agent agent = agentService.find(cabinet.getAgentId());

        Customer customer = customerService.findByMobile(agent.getPartnerId(), param.mobile);
        if (customer != null) {
            if (StringUtils.isEmpty(customer.getPassword())) {
                return RestResult.result(RespCode.CODE_6.getValue(), "密码未设置，请先设置密码");
            }
            if (!customer.getPassword().equals(param.password)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机号或密码错误");
            }
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号或密码错误");
        }

        if (customer.getIsActive() == null || customer.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此账户禁止登录");
        }

        Map map = new HashMap();
        map.put("customerId", customer.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginByBatteryParam {
        @NotBlank(message = "电池编号不能为空")
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/login_by_battery.htm")
    public RestResult loginByBattery(@Valid @RequestBody LoginByBatteryParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        TokenCache.Data tokenData = getTokenData();

        Battery battery = batteryService.find(param.batteryId);

        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此电池不存在");
        }

        if (battery.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此电池未被启用");
        }

        if (battery.getCustomerId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此电池没有对应客户");
        }

        if (battery.getStatus() != Battery.Status.CUSTOMER_OUT.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前电池不是客户取出电池");
        }

        Map map = new HashMap();
        map.put("customerId", battery.getCustomerId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class faceListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/face_list.htm")
    public RestResult faceList(@Valid @RequestBody faceListParam param) {
        return customerService.findFaceList(param.offset, param.limit);
    }

    @ResponseBody
    @RequestMapping(value = "/get_qrcode_for_login.htm")
    public RestResult getQrcodeForLogin() {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        TokenCache.Data tokenData = getTokenData();
        String uuid = tokenData.cabinetId + (System.currentTimeMillis() / 1000);
        String qrcode = String.format(ConstEnum.Qrcode.QRCODE_CUSTOMER_LOGIN_CABINET.getFormat(), config.getStaticUrl(), uuid);
        String key = CacheKey.key(CacheKey.K_LOGIN_QRCODE_V_CUSTOMER_ID, qrcode);
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }
        LoginQrcode loginQrcode = new LoginQrcode(cabinet.getId(), cabinet.getCabinetName(), Constant.LOGIN_QRCODE_STATUS_NOT_SCAN);
        memCachedClient.set(key, loginQrcode, MemCachedConfig.CACHE_THREE_MINUTE);
        Map map = new HashMap();
        map.put("qrcode", qrcode);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryQrcodeIsLoginSuccessParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;
    }

    @ResponseBody
    @RequestMapping(value = "/query_qrcode_is_login_success.htm")
    public RestResult queryQrcodeIsLoginSuccess(@Valid @RequestBody QueryQrcodeIsLoginSuccessParam param) {
        String key = CacheKey.key(CacheKey.K_LOGIN_QRCODE_V_CUSTOMER_ID, param.qrcode);
        LoginQrcode loginQrcode = (LoginQrcode) memCachedClient.get(key);
        if (loginQrcode != null) {
            if (Constant.LOGIN_QRCODE_STATUS_SCAN_SUCCESS == loginQrcode.status) {
                Map map = new HashMap();
                map.put("status", Constant.LOGIN_QRCODE_STATUS_SCAN_SUCCESS);
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            } else if (Constant.LOGIN_QRCODE_STATUS_LOGIN_SUCCESS == loginQrcode.status) {
                memCachedClient.delete(key);
                if (loginQrcode.customerId != null) {
                    Customer customer = customerService.find(loginQrcode.customerId);
                    if (customer != null) {
                        Map map = new HashMap();
                        map.put("customerId", loginQrcode.customerId);
                        map.put("status", Constant.LOGIN_QRCODE_STATUS_LOGIN_SUCCESS);
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                    }
                }
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");

            } else {
                Map map = new HashMap();
                map.put("status", Constant.LOGIN_QRCODE_STATUS_NOT_SCAN);
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            }
        } else {
            return RestResult.result(RespCode.CODE_4.getValue(), "二维码已过期");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult info(@Valid @RequestBody InfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        Customer customer = customerService.find(param.customerId);
        if (customer != null) {
            Map map = new HashMap();
            map.put("id", customer.getId());
            map.put("idCard", customer.getIdCard());
            map.put("nickname", customer.getNickname());
            map.put("fullname", customer.getFullname());
            map.put("mobile", customer.getMobile());
            map.put("photoPath", staticImagePath(customer.getPhotoPath()));
            map.put("balance", customer.getBalance() + customer.getGiftBalance());

            Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
            int agentId = cabinet.getAgentId();
            String cabinetId = cabinet.getId();
            long customerId = customer.getId();
            Integer batteryType = null;
            Integer batteryForegift = null;

            //查询白名单
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(agentId, customerId);
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }else{
                if(customerExchangeInfo != null){
                    batteryType = customerExchangeInfo.getBatteryType();
                    batteryForegift = customerExchangeInfo.getBatteryType();
                }
            }
            map.put("foregift", batteryForegift);
            map.put("batteryType", batteryType);

            if (StringUtils.isEmpty(customer.getMobile())) {
                map.put("step", "not_mobile");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            }

            if (StringUtils.isEmpty(customer.getIdCard())) {
                map.put("step", "not_authentication");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            }

            if(exchangeWhiteList == null){
                if(customerExchangeInfo == null ) {
                    map.put("step", "not_pay_foregift");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }else {
                    CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
                    if (order == null) {
                        return RestResult.dataResult(RespCode.CODE_1.getValue(), "押金订单不存在", null);
                    }
                    if (order.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                        map.put("step", "apply_back_foregift");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "您已申请退押金，欢迎再次使用", map);
                    }
                }
            }


            //运营商最大电池数
            int maxCount = 1;//默认1块电池
            String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), cabinet.getAgentId());
            if(!StringUtils.isEmpty(maxCountStr)){
                maxCount = Integer.parseInt(maxCountStr);
            }
            //用户电池数
            List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customerId);
            
            //用户存在电池时特殊处理
            if(batteryList.size() > 0){
                //优先查看是否有申请退电电池，如果有，进行退电处理
                BackBatteryOrder backBatteryOrder = backBatteryOrderService.findBatteryOrder(customerId, BackBatteryOrder.OrderStatus.SUCCESS.getValue());
                if(backBatteryOrder != null){
                    if (!cabinetId.equals(backBatteryOrder.getCabinetId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "请到" + backBatteryOrder.getCabinetName() + "扫码退租电池或取消退租");
                    }
                    map.put("step", "back_battery");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }

                //如果有电池在箱中未付款，需先进行退款处理
                for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                    Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                    if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                        if (!cabinetId.equals(battery.getCabinetId())) {
                            return RestResult.result(RespCode.CODE_2.getValue(), "请到" + battery.getCabinetName() + "扫码换电支付");
                        }
                        map.put("boxNum", battery.getBoxNum());
                        map.put("step", "old_battery_in_box");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                    }
                }
                //如果用户有未取出电池，如果是相同柜子，提示要先取出电池
                for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                    Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                    if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                        if (cabinetId.equals(battery.getCabinetId())) {
                            map.put("step", "take_new_battery");
                            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                        }
                    }
                }
            }

            //没开启分时分段，需要交押金
            if (exchangeWhiteList == null){
                ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(cabinet.getAgentId(), batteryType);
                if(exchangePriceTime == null || exchangePriceTime.getActiveSingleExchange() == null || exchangePriceTime.getActiveSingleExchange() == ConstEnum.Flag.FALSE.getValue()){
                    PacketPeriodOrder order = packetPeriodOrderService.findOneEnabled(customerId, agentId, batteryType);
                    if (order == null) {
                        map.put("step", "not_buy_packet_period_order");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                    }
                }
            }

            //运营商只有一块电池的情况下，需判断电池电量是否可以换电
            if(maxCount == 1 && batteryList.size() == 1){
                Battery battery = batteryService.find(batteryList.get(0).getBatteryId());
                if (battery.getVolume() != null && battery.getVolume() > cabinet.getPermitExchangeVolume()) {
                    map.put("step", "battery_volume_not_allow_exchange");
                    map.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }
            }

           String packetRemainTime = "";
            long now = System.currentTimeMillis();
            PacketPeriodOrder lastEndTime = packetPeriodOrderService.findLastEndTime(customerId);
            if (lastEndTime != null) {
                if (lastEndTime.getStatus() == PacketPeriodOrder.Status.USED.getValue() && now < lastEndTime.getEndTime().getTime()) {
                    packetRemainTime = AppUtils.formatTimeUnit((lastEndTime.getEndTime().getTime() - now) / 1000);
                } else {
                    packetRemainTime = lastEndTime.getDayCount() + "天";
                }
            }

            map.put("packetRemainTime", packetRemainTime);
            map.put("vehicleRemainTime", null);
            map.put("exchangeCount", 0);

            //查询是否有预约订单
            String bespeakBoxNum = null;
            BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(param.customerId);
            if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(tokenData.cabinetId)){
                bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
            }

            CabinetBox cabinetBox = cabinetBoxService.findOneFull(tokenData.cabinetId, batteryType, bespeakBoxNum);
            if (cabinetBox == null) {
                map.put("step", "not_full_battery");
                int fullCount = cabinetBoxService.findFullCount(tokenData.cabinetId);
                String errorMessage = "没有符合类型的已充满电池";
                if (fullCount != 0) {
                    errorMessage = "扫码者与当前柜子不匹配";
                }
                return RestResult.dataResult(RespCode.CODE_0.getValue(), errorMessage, map);
            }

            //如果电池小于最大电池数，取新电
            if(batteryList.size()  < maxCount){
                map.put("step", "take_new_battery");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            }else{//电池已经全部取出，放旧电
                map.put("step", "put_old_battery");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
            }
        } else {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerOfflineBatteryParam {
        public long customerId;
        public String takeBoxNum;
        public String batteryCode;
        public long exchangeTime;
    }

    @ResponseBody
    @RequestMapping(value = "/customer_offline_battery.htm")
    public RestResult customerOfflineBattery(@Valid @RequestBody CustomerOfflineBatteryParam param) {
        TokenCache.Data tokenData = getTokenData();
        String cabinetId = tokenData.cabinetId;
        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "终端不存在");
        }
        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerOfflineBattery customerOfflineBattery = new CustomerOfflineBattery();
        customerOfflineBattery.setCustomerId(customer.getId());
        customerOfflineBattery.setCustomerMobile(customer.getMobile());
        customerOfflineBattery.setCustomerFullname(customer.getFullname());
        customerOfflineBattery.setCabinetId(cabinet.getId());
        customerOfflineBattery.setCabinetCode(cabinet.getMac());
        customerOfflineBattery.setBoxNum(param.takeBoxNum);
        customerOfflineBattery.setBatteryCode(param.batteryCode);
        customerOfflineBattery.setExchangeTime(new Date(param.exchangeTime));
        int id = customerOfflineBatteryService.save(customerOfflineBattery);

        Map data = new HashMap();
        data.put("id", id);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerExchangeBatteryRecordParam {
        public long customerId;
        public String takeBoxNum;
        public String takeBatteryCode;
        public String putBoxNum;
        public String putBatteryCode;
        public long exchangeTime;
    }

    @ResponseBody
    @RequestMapping(value = "/customer_exchange_battery_record.htm")
    public RestResult customerExchangeBatteryRecord(@Valid @RequestBody CustomerExchangeBatteryRecordParam param) {
        TokenCache.Data tokenData = getTokenData();
        String cabinetId = tokenData.cabinetId;
        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "终端不存在");
        }
        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        CustomerOfflineExchangeRecord customerOfflineExchangeRecord = new CustomerOfflineExchangeRecord();
        customerOfflineExchangeRecord.setCustomerId(customer.getId());
        customerOfflineExchangeRecord.setCustomerMobile(customer.getMobile());
        customerOfflineExchangeRecord.setCustomerFullname(customer.getFullname());

        customerOfflineExchangeRecord.setPutCabinetId(cabinet.getId());
        customerOfflineExchangeRecord.setPutCabinetCode(cabinet.getMac());
        customerOfflineExchangeRecord.setPutBoxNum(param.putBoxNum);
        customerOfflineExchangeRecord.setPutBatteryCode(param.putBatteryCode);

        customerOfflineExchangeRecord.setTakeCabinetId(cabinet.getId());
        customerOfflineExchangeRecord.setTakeCabinetCode(cabinet.getMac());
        customerOfflineExchangeRecord.setTakeBoxNum(param.takeBoxNum);
        customerOfflineExchangeRecord.setTakeBatteryCode(param.takeBatteryCode);

        customerOfflineExchangeRecord.setExchangeTime(new Date(param.exchangeTime));
        customerOfflineExchangeRecordService.save(customerOfflineExchangeRecord);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
