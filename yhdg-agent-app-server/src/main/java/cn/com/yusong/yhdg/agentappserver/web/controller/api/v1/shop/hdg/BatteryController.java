package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.*;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import java.text.ParseException;
import java.util.*;


@Controller("agent_api_v1_shop_hdg_battery")
@RequestMapping(value = "/agent_api/v1/shop/hdg/battery")
public class BatteryController extends ApiController {
    final static Logger log = LogManager.getLogger(BatteryController.class);
    @Autowired
    ShopService shopService;
    @Autowired
    AgentService agentService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryInstallRecordService batteryInstallRecordService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    UserService userService;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    RentOrderService rentOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int type;
        public String keyword;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list(@Valid @RequestBody ListParam param) {
        String shopId = getTokenData().shopId;
        List<Battery> batteryList = Collections.emptyList();

        if (param.type == 1) { //客户使用中
            batteryList = batteryService.shopCustomerUseList(shopId, param.keyword, param.offset, param.limit);
        } else if (param.type == 2) { //柜子中
            batteryList = batteryService.shopCabinetList(shopId, param.keyword, param.offset, param.limit);
        } else if (param.type == 3) { //门店中
            batteryList = batteryService.shopStoreList(shopId, param.keyword, param.offset, param.limit);
        }

        List<Map> data = new ArrayList<Map>();
        Map<Integer, String> typeNameMap = new HashMap<Integer, String>();

        for (Battery battery : batteryList) {
            String batteryTypeName = null;
            if (battery.getType() != null) {
                batteryTypeName = typeNameMap.get(battery.getType());
                if (batteryTypeName == null) {
                    SystemBatteryType systemBatteryType = systemBatteryTypeService.find(battery.getType());
                    if (systemBatteryType != null) {
                        batteryTypeName = systemBatteryType.getTypeName();
                        typeNameMap.put(systemBatteryType.getId(), batteryTypeName);
                    }
                }
            }

            NotNullMap line = new NotNullMap();
            line.put("id", battery.getId());
            line.put("batteryTypeName", batteryTypeName);
            line.put("category", battery.getCategory());
            line.put("batteryCode", battery.getCode());
            line.put("shellCode", battery.getShellCode());
            line.put("statusName", battery.getStatusName());
            line.put("volume", battery.getVolume());
            line.put("signalType", battery.getSignalType());
            line.put("currentSignal", battery.getCurrentSignal());
            line.put("fullname", battery.getCustomerFullname());
            line.putMobileMask("mobile", battery.getCustomerMobile());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BindBatteryParam {
        @NotBlank(message = "电池id不能为空")
        public String batteryQrcode;

    }

    @ResponseBody
    @RequestMapping(value = "/bind_battery")
    public RestResult bindBattery(@Valid @RequestBody BindBatteryParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Agent agent = agentService.find(shop.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        List<Battery> batteryList = batteryService.findByCode(trimBatteryUid(param.batteryQrcode));
        if (batteryList.isEmpty()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        if (batteryList.size() > 1) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池编号:%s 查找到多块电池，请联系管理员", trimBatteryUid(param.batteryQrcode)));
        }
        Battery battery = batteryList.get(0);

        if(battery.getUpLineStatus() == null || battery.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue()
                || battery.getCategory() == null | battery.getAgentId() == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池未上线，请联系管理员进行上线操作");
        }

        if (!battery.getAgentId().equals(shop.getAgentId())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不是门店所属运营商的电池");
        }

        if (battery.getCategory() != Battery.Category.RENT.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "只有租电电池可绑定门店");
        }


        if (shopStoreBatteryService.findByBatteryId(battery.getId()) > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池已绑定");
        }

        if(battery.getStatus() != Battery.Status.NOT_USE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前电池不是空闲状态");
        }

        //电池绑定
        return batteryService.updateShopStoreBattery(battery.getId(), shopId, battery, agent, shop);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnBindBatteryParam {
        @NotBlank(message = "电池id不能为空")
        public String batteryId;

    }

    @ResponseBody
    @RequestMapping(value = "/un_bind_battery")
    public RestResult bindBattery(@Valid @RequestBody UnBindBatteryParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Agent agent = agentService.find(shop.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        //电池解绑
        return batteryService.unBindShopStoreBattery(battery.getId(), shopId, battery);
    }

    public static class RefundBatteryParam {
        public String [] batteryId;
        public Integer refundMoney;

    }

    @ResponseBody
    @RequestMapping(value = "/back_battery")
    public RestResult refundBattery(@Valid @RequestBody RefundBatteryParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        long userId = tokenData.userId;
        String shopId = tokenData.shopId;

        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商信息错误");
        }

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        User user = userService.find(userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        return batteryService.backBattery(param.batteryId, param.refundMoney, agent, shop, user);
    }


    /**
     * 4-交换电池
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SwitchBatteryParam {
        public String oldBatteryQrcode;
        public String newBatteryQrcode;
    }

    @ResponseBody
    @RequestMapping(value = "/switch_battery.htm")
    public RestResult switchBattery(@RequestBody SwitchBatteryParam param) {
        TokenCache.Data tokenData = getTokenData();
        if (log.isDebugEnabled()) {
            log.debug("switch_battery userId ={} shopId={} token={}", tokenData.userId, tokenData.shopId, tokenData.token);
        }
        return batteryService.switchShopBattery(tokenData.shopId, param.oldBatteryQrcode, param.newBatteryQrcode);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryDetailParam {
        public String batteryId;
    }

    /**
     * 124-查询门店电池详情
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
        Shop shop = shopService.find(tokenData.shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"电池不存在",null);
        }

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
        result.put("isCustomer", battery.getCustomerId() != null ? 1 : 0);

        result.put("shopId", shop.getId());
        result.put("shopName", shop.getShopName());

        List<BatteryOrder> list = batteryOrderService.findByBatteryList(agentId, param.batteryId, null,0, 10);
        if (list.size() > 0) {
            BatteryOrder batteryOrder = list.get(0);
            result.put("takeCabinetId", batteryOrder.getTakeCabinetId());
            result.put("takeCabinetName", batteryOrder.getTakeCabinetName());
            result.putDateTime("takeTime", batteryOrder.getTakeTime());
            result.put("putCabinetId", batteryOrder.getPutCabinetId());
            result.put("putCabinetName", batteryOrder.getPutCabinetName());
            result.putDateTime("putTime", batteryOrder.getPutTime());
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


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryRentRecordParam {
        public String batteryId;
        public int offset;
        public int limit;
    }

    /**
     * 5-查询门店电池租用记录信息
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/battery_rent_record.htm")
    public RestResult batteryRentRecord(@Valid @RequestBody BatteryRentRecordParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        String shopId = tokenData.shopId;
        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }
        Battery battery = batteryService.find(param.batteryId);
        if(battery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        List<Map> result = new ArrayList<Map>();
        //根据电池类型显示 换租电记录
        if (battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
            List<BatteryOrder> list1 = batteryOrderService.findByBatteryList(agentId, param.batteryId, null, param.offset, param.limit);
            for (BatteryOrder batteryOrder : list1) {
                NotNullMap notNullMap = new NotNullMap();

                notNullMap.putString("id", batteryOrder.getId());
                notNullMap.putString("customerFullname", batteryOrder.getCustomerFullname());
                notNullMap.putMobileMask("customerMobile", batteryOrder.getCustomerMobile());
                notNullMap.putString("statusName", batteryOrder.getOrderStatusName());
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

                result.add(notNullMap);
            }
        } else {
            List<RentOrder> list = rentOrderService.findListByBatteryId(shopId, param.batteryId, param.offset, param.limit);
            for (RentOrder rentOrder : list) {
                NotNullMap line = new NotNullMap();

                line.putString("id", rentOrder.getId());
                line.putString("customerFullname", rentOrder.getCustomerFullname());
                line.putMobileMask("customerMobile", rentOrder.getCustomerMobile());
                line.putInteger("volume", rentOrder.getCurrentVolume());
                line.putString("shopId", rentOrder.getShopId());
                line.putString("shopName", rentOrder.getShopName());
                line.putDateTime("backTime", rentOrder.getBackTime());
                line.putDateTime("createTime", rentOrder.getCreateTime());

                result.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class queryBatteryInfoParam {
        public String batteryQrcode;
    }


    @ResponseBody
    @RequestMapping(value = "/query_battery_info.htm")
    public RestResult queryBatteryInfo(@Valid @RequestBody queryBatteryInfoParam param) {
        TokenCache.Data tokenData = getTokenData();

        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        String shopId = tokenData.shopId;
        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        param.batteryQrcode = trimBatteryUid(param.batteryQrcode);

        List<Battery> batteryCodeList = batteryService.findByCode(param.batteryQrcode);
        if (batteryCodeList.isEmpty()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        if (batteryCodeList.size() > 1) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池编号:%s 查找到多块电池，请联系管理员", param.batteryQrcode));
        }
        Battery battery = batteryCodeList.get(0);



        if (battery.getStatus() != Battery.Status.CUSTOMER_OUT.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池状态错误, 不是客户使用电池");
        }

        Map data = new HashMap();

        if (battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
            CustomerExchangeInfo exchangeInfo = customerExchangeInfoService.find(battery.getCustomerId());
            if (exchangeInfo == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有交押金");
            }
            if (!shop.getId().equals(exchangeInfo.getBalanceShopId())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户电池不是门店电池");
            }

            List<Map> batteryList = new ArrayList<Map>();
            List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryService.findByCustomerId(exchangeInfo.getId());
            for (CustomerExchangeBattery e : exchangeBatteryList) {
                Battery b = batteryService.find(e.getBatteryId());

                Map m = new HashMap();
                m.put("id", b.getId());
                m.put("shellCode", b.getShellCode());
                m.put("code", b.getCode());
                batteryList.add(m);
            }

            int foregiftMoney = 0;
            if (StringUtils.isNotEmpty(exchangeInfo.getForegiftOrderId())) {
                CustomerForegiftOrder foregiftOrder = customerForegiftOrderService.find(exchangeInfo.getForegiftOrderId());
                if (foregiftOrder != null) {
                    foregiftMoney = foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null ? foregiftOrder.getDeductionTicketMoney() : 0);
                }
            }

            Customer customer = customerService.find(exchangeInfo.getId());
            data.put("fullname", customer.getFullname());
            data.put("mobile", customer.getMobile());
            data.put("batteryList", batteryList);
            data.put("foregiftMoney", foregiftMoney);
            data.put("category", Battery.Category.EXCHANGE.getValue());

        } else if (battery.getCategory() == Battery.Category.RENT.getValue()) {
            CustomerRentInfo rentInfo = customerRentInfoService.find(battery.getCustomerId());
            if (rentInfo == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有交押金");
            }
            if (!shop.getId().equals(rentInfo.getBalanceShopId())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户电池不是门店电池");
            }

            List<Map> batteryList = new ArrayList<Map>();
            List<CustomerRentBattery> rentBatteryList = customerRentBatteryService.findByCustomerId(rentInfo.getId());
            for (CustomerRentBattery e : rentBatteryList) {
                Battery b = batteryService.find(e.getBatteryId());

                Map m = new HashMap();
                m.put("id", b.getId());
                m.put("shellCode", b.getShellCode());
                m.put("code", b.getCode());
                batteryList.add(m);
            }

            int foregiftMoney = 0;
            if (StringUtils.isNotEmpty(rentInfo.getForegiftOrderId())) {
                RentForegiftOrder foregiftOrder = rentForegiftOrderService.find(rentInfo.getForegiftOrderId());
                if (foregiftOrder != null) {
                    foregiftMoney = foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null?foregiftOrder.getDeductionTicketMoney():0);
                }
            }

            Customer customer = customerService.find(rentInfo.getId());
            data.put("fullname", customer.getFullname());
            data.put("mobile", customer.getMobile());
            data.put("batteryList", batteryList);
            data.put("foregiftMoney", foregiftMoney);
            data.put("category", Battery.Category.RENT.getValue());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LockParam {
        @NotBlank(message = "电池ID不能为空")
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/lock.htm")
    public RestResult lock(@Valid @RequestBody LockParam param) {
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        batteryService.updateLockSwitch(param.batteryId, ConstEnum.Flag.TRUE.getValue());
        return RestResult.SUCCESS;
    }

    private String trimBatteryUid(String batteryQrcode) {
        if (StringUtils.isNotEmpty(batteryQrcode) && batteryQrcode.contains("zudian.yusong.com.cn")) {
            String v = StringUtils.substringBetween(batteryQrcode, "v=", "&");
            if (StringUtils.isNotEmpty(v) && v.length() > 2) {
                return batteryQrcode = "ZZ" + v.substring(2);
            }
        }
        return batteryQrcode;
    }

}
