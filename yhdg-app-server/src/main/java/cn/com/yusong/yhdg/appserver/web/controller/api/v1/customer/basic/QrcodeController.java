package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zc.*;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentBatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentInfoService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

@Controller("api_v1_customer_basic_qrcode")
@RequestMapping(value = "/api/v1/customer/basic/qrcode")
public class QrcodeController extends ApiController {
    final static Logger log = LogManager.getLogger(QrcodeController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    AppConfig config;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    CabinetDynamicCodeCustomerService cabinetDynamicCodeCustomerService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    ShopStoreVehicleBatteryService ShopStoreVehicleBatteryService;
    @Autowired
    ShopStoreVehicleService shopStoreVehicleService;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerVehicleInfoService customerVehicleInfoService;
    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;
    @Autowired
    VehiclePeriodOrderService vehiclePeriodOrderService;
    @Autowired
    ShopService shopService;
    @Autowired
    StationService stationService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScanParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;
        public int baiduCityId;
        public String districtName;
    }

    @ResponseBody
    @RequestMapping(value = "/scan")
    public RestResult scan(@Valid @RequestBody ScanParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        param.qrcode = AppUtils.decodeUrl(param.qrcode, Constant.ENCODING_UTF_8);
        QrcodeResult result = ConstEnum.Qrcode.parse(param.qrcode);
        if (result == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "不可识别的二维码");
        }

        if (result.type == ConstEnum.Qrcode.QRCODE_CABINET.getType()) {
            return handleCabinetQrcode(customerId, result.value, param.baiduCityId, param.districtName);

        } else if (result.type == ConstEnum.Qrcode.QRCODE_BATTERY.getType()) {
            return handleBatteryQrcode(customerId, result.value);

        } else if (result.type == ConstEnum.Qrcode.QRCODE_SHOP.getType()) {
            return handleShopQrcode(customerId, result.value);

        } else if (result.type == ConstEnum.Qrcode.QRCODE_STATION.getType()) {
            return handleStationQrcode(customerId, result.value);

        } else if (result.type == ConstEnum.Qrcode.QRCODE_VEHICLE.getType()) {
            return handleVehicleQrcode(customerId, result.value);

        } else if (result.type == ConstEnum.Qrcode.QRCODE_CUSTOMER_LOGIN_CABINET.getType()) {
            return handleLoginyQrcode(customerId, result.value);

        }

        return RestResult.result(RespCode.CODE_2.getValue(), "不支持的二维码类型");
    }

    private RestResult handleCabinetQrcode(long customerId, String cabinetId, int baiduCityId, String districtName) {
        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "柜子不存在");
        }

        if (cabinet.getUpLineStatus() != null) {
            if (cabinet.getUpLineStatus() == Cabinet.UpLineStatus.NOT_ONLINE.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "柜子未上线，请联系管理员进行上线操作");
            } else if (cabinet.getUpLineStatus() == Cabinet.UpLineStatus.APPLY_FOR_ONLINE.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "柜子上线审核中，请稍后再试");
            }
        }

        int agentId = cabinet.getAgentId();

        int inputDynamicCode = 0; //默认不输入
        if (StringUtils.isNotEmpty(cabinet.getDynamicCode()) && cabinetDynamicCodeCustomerService.find(cabinetId, customerId) == null) {
            inputDynamicCode = 1;
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        Agent agent = agentService.find(cabinet.getAgentId());
        if (agent.getPartnerId() != customer.getPartnerId().intValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("商户不一致，请使用%s对应公众号", agent.getAgentName()));
        }

        Map root = new HashMap();
        Map data = new HashMap();

        root.put("type", ConstEnum.Qrcode.QRCODE_CABINET.getType());
        root.put("cabinetQrcode", data);

        data.put("cabinetId", cabinet.getId());
        data.put("agentId", agentId);
        data.put("shopId", cabinet.getShopId());
        data.put("priceGroupId", "");
        data.put("inputDynamicCode", inputDynamicCode);
        data.put("minExchangeVolume", cabinet.getMinExchangeVolume());
        data.put("chargeFullVolume", cabinet.getChargeFullVolume());

        if (StringUtils.isEmpty(customer.getMobile())) {
            data.put("step", "not_mobile");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        if (customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUTO_FAIL.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUDIT_REFUSE.getValue()) {
            data.put("step", "not_authentication");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        //存在多通道待支付订单
        if (customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.HD.getValue()) > 0) {
            data.put("step", "multi_wait_pay");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        //查询用户白名单
        ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(agentId, customerId);
        //查询用户押金
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
        if (exchangeWhiteList != null && customerExchangeInfo != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "您为白名单用户，请先退押金后再进行换电操作");
        }


        if (customerExchangeInfo != null) {
            //如果柜子状态为独享，无法操作
            if (cabinet.getViewType() == Cabinet.ViewType.UNSHARED.getValue()) {
                if (customerExchangeInfo.getBalanceCabinetId() == null
                        || !customerExchangeInfo.getBalanceCabinetId().equals(cabinet.getId())) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "该换电柜为独享柜，无法使用");
                }
            }
            //如果柜子状态为共享，用户属于独享用户，无法操作
            else {
                if (customerExchangeInfo.getBalanceCabinetId() != null
                        && !customerExchangeInfo.getBalanceCabinetId().equals(cabinet.getId())) {
                    Cabinet customerCabinet = cabinetService.find(customerExchangeInfo.getBalanceCabinetId());
                    if (customerCabinet.getViewType() == Cabinet.ViewType.UNSHARED.getValue()) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "您为独享柜用户，请使用" + customerCabinet.getCabinetName() + "换电柜");
                    }
                }
            }

            if(customerExchangeInfo.getAgentId() != agent.getId().intValue()){
                return RestResult.result(RespCode.CODE_2.getValue(), "未知换电站，无法换电");
            }
        }

        Integer batteryType = null;
        if (customerExchangeInfo != null) {
            batteryType = customerExchangeInfo.getBatteryType();
        } else if (exchangeWhiteList != null) {
            batteryType = exchangeWhiteList.getBatteryType();
        }

        if (exchangeWhiteList == null) {
            if (customerExchangeInfo == null) {
                data.put("step", "not_pay_foregift");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            } else {
                CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
                if (order == null) {
                    return RestResult.dataResult(RespCode.CODE_1.getValue(), "押金订单不存在", null);
                }
                if (order.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                    data.put("step", "apply_back_foregift");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), "您已申请退押金，欢迎再次使用", root);
                }
            }
        }

        //有分期欠款记录
        int waitPayCount = customerInstallmentRecordPayDetailService.findCountByCustomerId(customerId, CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), ConstEnum.Category.EXCHANGE.getValue(), new Date());
        if (waitPayCount > 0) {
            data.put("step", "expire_debt");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        //运营商最大电池数
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), cabinet.getAgentId());
        if (!StringUtils.isEmpty(maxCountStr)) {
            maxCount = Integer.parseInt(maxCountStr);
        }

        //用户电池数
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customerId);

        //用户存在电池时特殊处理
        if (batteryList.size() > 0) {
            //优先查看是否有申请退电电池，如果有，进行退电处理
            BackBatteryOrder backBatteryOrder = backBatteryOrderService.findBatteryOrder(customerId, BackBatteryOrder.OrderStatus.SUCCESS.getValue());
            if (backBatteryOrder != null) {
                if (!cabinetId.equals(backBatteryOrder.getCabinetId())) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "请到" + backBatteryOrder.getCabinetName() + "扫码退租电池或取消退租");
                }
                data.put("step", "back_battery");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }

            //如果有电池在箱中未付款，需先进行退款处理
            for (CustomerExchangeBattery customerExchangeBattery : batteryList) {
                Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                    if (!cabinetId.equals(battery.getCabinetId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "请到" + battery.getCabinetName() + "扫码换电支付");
                    }
                    data.put("boxNum", battery.getBoxNum());
                    data.put("step", "old_battery_in_box");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
                }
            }
            //如果用户有未取出电池，如果是相同柜子，提示要先取出电池
            for (CustomerExchangeBattery customerExchangeBattery : batteryList) {
                Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                    if (cabinetId.equals(battery.getCabinetId())) {
                        data.put("step", "take_new_battery");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
                    }
                }
            }
        }

        //没开启分时分段，需要交押金
        if (exchangeWhiteList == null) {
            ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(cabinet.getAgentId(), batteryType);
            if (exchangePriceTime == null || exchangePriceTime.getActiveSingleExchange() == null || exchangePriceTime.getActiveSingleExchange() == ConstEnum.Flag.FALSE.getValue()) {
                PacketPeriodOrder order = packetPeriodOrderService.findOneEnabled(customerId, agentId, batteryType);
                if (order == null) {
                    data.put("step", "not_buy_packet_period_order");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
                }
            }
        }


        if (cabinet.getIsOnline() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.dataResult(RespCode.CODE_10.getValue(), "换电柜已离线，无法进行换电操作", root);
        }


        //运营商只有一块电池的情况下，需判断电池电量是否可以换电
        if (maxCount == 1 && batteryList.size() == 1) {
            Battery battery = batteryService.find(batteryList.get(0).getBatteryId());
            if (battery.getVolume() != null && battery.getVolume() > cabinet.getPermitExchangeVolume()) {
                data.put("step", "battery_volume_not_allow_exchange");
                data.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }
        }

        //查询是否有预约订单
        String bespeakBoxNum = null;
        BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customerId);
        if (bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinetId)) {
            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
        }

        CabinetBox cabinetBox = cabinetBoxService.findOneFull(cabinetId, batteryType, bespeakBoxNum);
        if (cabinetBox == null) {
            data.put("step", "not_full_battery");
            int fullCount = cabinetBoxService.findFullCount(cabinetId);
            String errorMessage = "没有符合类型的已充满电池";
            if (fullCount != 0) {
                errorMessage = "扫码者与当前柜子不匹配";
            }
            return RestResult.dataResult(RespCode.CODE_0.getValue(), errorMessage, root);
        }


        String message = null;
        if (cabinetBox.getVolume() < cabinet.getChargeFullVolume()) {
            Integer designMileage;
            if (customer.getAgentId() != null && customer.getAgentId() != 0) {
                designMileage = Integer.parseInt(agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue(), customer.getAgentId()));
            } else {
                designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
            }
            message = "可换电池电量为" + cabinetBox.getVolume() + "%，是否确认换电?";
        }

        //如果电池小于最大电池数，取新电
        if (batteryList.size() < maxCount) {
            data.put("step", "take_new_battery");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        } else {//电池已经全部取出，放旧电
            data.put("step", "put_old_battery");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }
    }

    private RestResult handleShopQrcode(long customerId, String shopId) {
        Map root = new HashMap();
        Map data = new HashMap();

        Shop shop = shopService.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        Agent agent = agentService.find(shop.getAgentId());
        if (agent.getPartnerId() != customer.getPartnerId().intValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("商户不一致，请使用%s对应公众号", agent.getAgentName()));
        }

        root.put("type", ConstEnum.Qrcode.QRCODE_SHOP.getType());
        root.put("shopQrcode", data);
        data.put("shopId", shop.getId());
        data.put("agentId", shop.getAgentId());

        String message = null;
        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
    }

    private RestResult handleStationQrcode(long customerId, String stationId) {
        Map root = new HashMap();
        Map data = new HashMap();

        Station station = stationService.find(stationId);
        if (station == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "站点不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        Agent agent = agentService.find(station.getAgentId());
        if (agent.getPartnerId() != customer.getPartnerId().intValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("商户不一致，请使用%s对应公众号", agent.getAgentName()));
        }

        root.put("type", ConstEnum.Qrcode.QRCODE_STATION.getType());
        root.put("stationQrcode", data);
        data.put("stationId", station.getId());
        data.put("agentId", station.getAgentId());

        String message = null;
        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
    }

    private RestResult handleVehicleQrcode(long customerId, String vinNo) {
        Map root = new HashMap();
        Map data = new HashMap();

        Vehicle vehicle = vehicleService.findByVinNo(vinNo);
        if (vehicle == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "车辆不存在");
        }

        if (vehicle.getUpLineStatus() == null || vehicle.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue() || vehicle.getAgentId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "车辆未上线，请联系管理员进行上线操作");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        Agent agent = agentService.find(vehicle.getAgentId());
        if (agent.getPartnerId() != customer.getPartnerId().intValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("商户不一致，请使用%s对应公众号", agent.getAgentName()));
        }


        root.put("type", ConstEnum.Qrcode.QRCODE_VEHICLE.getType());
        root.put("vehicleQrcode", data);

        data.put("vehicleId", vehicle.getId());
        data.put("vinNo", vehicle.getVinNo());
        data.put("vehicleName", vehicle.getVehicleName());
        data.put("modelId", vehicle.getModelId());
        data.put("agentId", vehicle.getAgentId());

        if (StringUtils.isEmpty(customer.getMobile())) {
            data.put("step", "not_mobile");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        if (customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUTO_FAIL.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUDIT_REFUSE.getValue()) {
            data.put("step", "not_authentication");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        String message = null;

        if (vehicle.getCustomerId() != null) {
            message = "车辆已绑定用户";
            data.put("step", "show_vehicle_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        ShopStoreVehicle shopStoreVehicle = shopStoreVehicleService.findByVehicleId(vehicle.getId());
        if (shopStoreVehicle == null) {
            message = "非门店库存车辆无法绑定";
            data.put("step", "show_vehicle_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }
        data.put("shopId", shopStoreVehicle.getShopId());
        data.put("priceSettingId", shopStoreVehicle.getPriceSettingId());

        //存在租电多通道待支付订单
        if (customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.ZC.getValue()) > 0) {
            data.put("step", "multi_wait_pay");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoService.findByCustomerId(customerId);
        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null) {
            message = "您已经拥有可租车辆";
            data.put("step", "show_vehicle_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        //押金
        Integer modelId = null;
        if (customerVehicleInfo == null) {
            data.put("step", "not_pay_foregift");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        } else {
            VehicleForegiftOrder order = vehicleForegiftOrderService.find(customerVehicleInfo.getForegiftOrderId());
            if (order == null) {
                message = "押金订单不存在";
                data.put("step", "show_vehicle_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }
            if (order.getStatus() == VehicleForegiftOrder.Status.APPLY_REFUND.getValue()) {
                data.put("step", "apply_back_foregift");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "您已申请退押金，欢迎再次使用", root);
            }
            modelId = order.getModelId();
        }

        if (modelId != null && modelId != vehicle.getModelId()) {
            message = "车辆型号不匹配无法绑定车辆";
            data.put("step", "show_vehicle_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        //租金
        VehiclePeriodOrder order = vehiclePeriodOrderService.findOneEnabled(customerId, vehicle.getAgentId(), vehicle.getModelId());
        if (order == null) {
            data.put("step", "not_buy_packet_period_order");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        //绑定车辆
        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() == null) {
            data.put("step", "bind_vehicle");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        data.put("step", "show_vehicle_info");
        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
    }

    private RestResult handleBatteryQrcode(long customerId, String code) {
        Map root = new HashMap();
        Map data = new HashMap();

        Battery battery = batteryService.findByShellCode(code);
        if (battery == null) {
            battery = batteryService.findByCode(code);
            if (battery == null) {
                battery = batteryService.find(code);
                if (battery == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
                }
            }
        }

        if (battery.getUpLineStatus() == null || battery.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue()
                || battery.getCategory() == null | battery.getAgentId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池未上线，请联系管理员进行上线操作");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        Agent agent = agentService.find(battery.getAgentId());
        if (agent.getPartnerId() != customer.getPartnerId().intValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("商户不一致，请使用%s对应公众号", agent.getAgentName()));
        }

        String batteryTypeName = "";
        SystemBatteryType systemBatteryType = systemBatteryTypeService.find(battery.getType());
        if (systemBatteryType != null) {
            batteryTypeName = systemBatteryType.getTypeName();
        }

        root.put("type", ConstEnum.Qrcode.QRCODE_BATTERY.getType());
        root.put("batteryQrcode", data);

        data.put("batteryId", battery.getId());
        data.put("volumn", battery.getVolume());
        data.put("code", battery.getCode());
        data.put("shellCode", battery.getShellCode());

        data.put("batteryType", battery.getType());
        data.put("batteryTypeName", batteryTypeName);
        data.put("category", battery.getCategory());
        data.put("agentId", battery.getAgentId());

        if (StringUtils.isEmpty(customer.getMobile())) {
            data.put("step", "not_mobile");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        if (customer.getAuthStatus() == Customer.AuthStatus.NOT.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUTO_FAIL.getValue()
                || customer.getAuthStatus() == Customer.AuthStatus.AUDIT_REFUSE.getValue()) {
            data.put("step", "not_authentication");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
        }

        String message = null;

        //如果电池类型为未使用并且在库存中有值，才能继续走下去
        if (battery.getStatus() != Battery.Status.NOT_USE.getValue()) {
            message = "非空闲电池无法绑定";
            data.put("step", "show_battery_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        if (battery.getCustomerId() != null) {
            message = "电池已绑定用户";
            data.put("step", "show_battery_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        if(ShopStoreVehicleBatteryService.existBattery(battery.getId()) > 0 ){
            message = "电池归属门店租车库存电池";
            data.put("step", "show_battery_info");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
        }

        //换电业务
        if (battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
            //存在多通道待支付订单
            if (customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.HD.getValue()) > 0) {
                data.put("step", "multi_wait_pay");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }

            int agentId = battery.getAgentId();
            //查询用户白名单
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(agentId, customerId);
            //查询用户押金
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
            if (exchangeWhiteList != null && customerExchangeInfo != null) {
                message = "您为白名单用户，请先退押金后再进行换电操作";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }
            Integer batteryType = null;
            if (customerExchangeInfo != null) {
                batteryType = customerExchangeInfo.getBatteryType();
            } else if (exchangeWhiteList != null) {
                batteryType = exchangeWhiteList.getBatteryType();
            }

            if (batteryType != null && batteryType != battery.getType()) {
                message = "电池类型不匹配无法绑定电池";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }

            if (exchangeWhiteList == null) {
                if (customerExchangeInfo == null) {
                    ShopStoreBattery shopStoreBattery = shopStoreBatteryService.findByBattery(battery.getId());
                    if (shopStoreBattery == null) {
                        message = "非门店库存电池无法不支持扫码交押金";
                        data.put("step", "show_battery_info");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
                    } else {
                        data.put("shopId", shopStoreBattery.getShopId());
                    }

                    data.put("step", "not_pay_foregift");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
                } else {
                    CustomerForegiftOrder order = customerForegiftOrderService.find(customerExchangeInfo.getForegiftOrderId());
                    if (order == null) {
                        message = "押金订单不存在";
                        data.put("step", "show_battery_info");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
                    }
                    if (order.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                        data.put("step", "apply_back_foregift");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "您已申请退押金，欢迎再次使用", root);
                    }
                }
            }

            //没开启分时分段，需要交租金
            if (exchangeWhiteList == null) {
                ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(battery.getAgentId(), batteryType);
                if (exchangePriceTime == null || exchangePriceTime.getActiveSingleExchange() == null || exchangePriceTime.getActiveSingleExchange() == ConstEnum.Flag.FALSE.getValue()) {
                    PacketPeriodOrder order = packetPeriodOrderService.findOneEnabled(customerId, agentId, batteryType);
                    if (order == null) {
                        //data.put("step", "not_buy_packet_period_order");
                        data.put("step", "show_battery_info");
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "请扫描换电柜的二维码来续费", root);
                    }
                }
            }

            //如果电池为运营商电池，运营商用户可以绑定。不然只能该门店骑手绑定。
//            ShopStoreBattery shopStoreBattery = shopStoreBatteryService.findByBattery(battery.getId());
//            if (shopStoreBattery != null) {
//                if (customerExchangeInfo == null) {
//                    data.put("step", "show_battery_info");
//                    return RestResult.dataResult(RespCode.CODE_0.getValue(), "白名单用户不能绑定门店电池", root);
//                }
//                if (customerExchangeInfo.getBalanceShopId() == null
//                        || !shopStoreBattery.getShopId().equals(customerExchangeInfo.getBalanceShopId())) {
//                    data.put("step", "show_battery_info");
//                    return RestResult.dataResult(RespCode.CODE_0.getValue(), "非本门店用户无法绑定门店电池", root);
//                }
//            } else {
//                if (customerExchangeInfo != null && customerExchangeInfo.getBalanceShopId() != null) {
//                    data.put("step", "show_battery_info");
//                    return RestResult.dataResult(RespCode.CODE_0.getValue(), "门店用户无法绑定非门店电池", root);
//                }
//            }

            //运营商最大电池数
            int maxCount = 1;//默认1块电池
            String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), battery.getAgentId());
            if (!StringUtils.isEmpty(maxCountStr)) {
                maxCount = Integer.parseInt(maxCountStr);
            }
            //用户电池数
            List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customerId);
            //如果电池小于最大电池数，取新电
            if (batteryList.size() < maxCount) {
                data.put("step", "bind_battery");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            } else {
                message = "您拥有的电池已达到最多可租电池数";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }
        }
        //租电
        else if (battery.getCategory() == Battery.Category.RENT.getValue()) {
            ShopStoreBattery shopStoreBattery = shopStoreBatteryService.findByBattery(battery.getId());
            if (shopStoreBattery == null) {
                message = "非门店库存电池无法绑定";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            } else {
                data.put("shopId", shopStoreBattery.getShopId());
            }

            //存在租电多通道待支付订单
            if (customerMultiOrderService.countMultiWaitPay(customerId, CustomerMultiOrder.Type.ZD.getValue()) > 0) {
                data.put("step", "multi_wait_pay");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }

            //运营商最大电池数
            int maxCount = 1;//默认1块电池
            String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.RENT_BATTERY_COUNT.getValue(), battery.getAgentId());
            if (!StringUtils.isEmpty(maxCountStr)) {
                maxCount = Integer.parseInt(maxCountStr);
            }
            List<CustomerRentBattery> batteryList = customerRentBatteryService.findListByCustomer(customerId);
            if (batteryList.size() >= maxCount) {
                message = "您拥有的电池已达到最多可租电池数";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }

            //押金
            Integer batteryType = null;
            CustomerRentInfo customerRentInfo = customerRentInfoService.find(customer.getId());
            if (customerRentInfo == null) {
                data.put("step", "not_pay_foregift");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            } else {
                RentForegiftOrder order = rentForegiftOrderService.find(customerRentInfo.getForegiftOrderId());
                if (order == null) {
                    message = "押金订单不存在";
                    data.put("step", "show_battery_info");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
                }
                if (order.getStatus() == RentForegiftOrder.Status.APPLY_REFUND.getValue()) {
                    data.put("step", "apply_back_foregift");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), "您已申请退押金，欢迎再次使用", root);
                }
                batteryType = order.getBatteryType();
            }

            if (batteryType != null && batteryType != null && batteryType != battery.getType()) {
                message = "电池类型不匹配无法绑定电池";
                data.put("step", "show_battery_info");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
            }

            //租金
            RentPeriodOrder order = rentPeriodOrderService.findOneEnabled(customerId, battery.getAgentId(), battery.getType());
            if (order == null) {
                data.put("step", "not_buy_packet_period_order");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }

            //绑定电池
            if (batteryList.size() < maxCount) {
                data.put("step", "bind_battery");
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
            }
        }

        data.put("step", "show_battery_info");
        return RestResult.dataResult(RespCode.CODE_0.getValue(), message, root);
    }

    private RestResult handleLoginyQrcode(long customerId, String value) {
        Map root = new HashMap();
        Map data = new HashMap();

        root.put("type", ConstEnum.Qrcode.QRCODE_CUSTOMER_LOGIN_CABINET.getType());
        root.put("loginQrcode", data);

        data.put("value", value);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root);
    }

}
