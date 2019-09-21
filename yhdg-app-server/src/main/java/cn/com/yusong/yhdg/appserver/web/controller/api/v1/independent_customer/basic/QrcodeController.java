package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.basic;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerExchangeBatteryService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemBatteryTypeService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import net.spy.memcached.internal.OperationFuture;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller("api_v1_independent_customer_basic_qrcode")
@RequestMapping(value = "/api/v1/independent_customer/basic/qrcode")
public class QrcodeController extends ApiController {
    final static Logger log = LogManager.getLogger(QrcodeController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    AppConfig config;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    MemCachedClient memCachedClient;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScanParam {
        @NotBlank(message = "二维码不能为空")
        public String qrcode;
        public String customerMobile;
        public String batteryId;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/scan")
    public RestResult scan(@Valid @RequestBody ScanParam param) throws ExecutionException, InterruptedException {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        QrcodeResult result = ConstEnum.Qrcode.parse(param.qrcode);
        if (result == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "不可识别的二维码");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (result.type == ConstEnum.Qrcode.QRCODE_CABINET.getType()) {
            return handleCabinetQrcode(customer, result.value);
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "不支持的二维码类型");
    }

    private RestResult handleCabinetQrcode(Customer customer, String cabinetId) throws ExecutionException, InterruptedException {
        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "柜子不存在");
        }

        Map data = new HashMap();

        //电池类型要处理
        Integer batteryType = 2;
        if(StringUtils.isNotEmpty(customer.getIcCard())){
            batteryType = Integer.parseInt(customer.getIcCard() );
        }

        //运营商最大电池数
        int maxCount = 1;//默认1块电池



        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        //用户存在电池时特殊处理
        if(batteryList.size() > 0){
            //优先查看是否有申请退电电池，如果有，进行退电处理
            BackBatteryOrder backBatteryOrder = backBatteryOrderService.findBatteryOrder(customer.getId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
            if(backBatteryOrder != null){
                if (!cabinetId.equals(backBatteryOrder.getCabinetId())) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "请到" + backBatteryOrder.getCabinetName() + "扫码退租电池或取消退租");
                }
                data.put("step", "back_battery");
                Battery battery = batteryService.find(batteryList.get(0).getBatteryId());
                return openEmptyBoxForBackBattery(customer.getId().intValue(), backBatteryOrder.getId(), battery );
            }

            //如果用户有未取出电池，如果是相同柜子，提示要先取出电池
            for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                    if (cabinetId.equals(battery.getCabinetId())) {
                        //取新电
                        return batteryOrderOpenFullBox( cabinetId, customer.getId().intValue(), battery.getOrderId(), batteryType);
                    }
                }

                if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                    //结束订单
                    batteryOrderService.complete(battery.getOrderId());
                }
            }
        }

        if (cabinetBoxService.findOneFull(cabinetId, batteryType, null) == null) {
            data.put("step", "not_full_battery");
            int fullCount = cabinetBoxService.findFullCount(cabinetId);
            String errorMessage = "没有符合类型的已充满电池";
            if (fullCount != 0) {
                errorMessage = "扫码者与当前柜子不匹配";
            }
            return RestResult.dataResult(RespCode.CODE_2.getValue(), errorMessage, data);
        }

        //如果电池小于最大电池数，取新电
        batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        if(batteryList.size()  < maxCount){
            //取新电
            return batteryOrderOpenFullBox( cabinetId, customer.getId().intValue(), null, batteryType);
        }else{//电池已经全部取出，放旧电
            //放旧电
            return batteryOrderOpenEmptyBox( cabinetId, customer.getId(), batteryType);
        }
    }

    private RestResult batteryOrderOpenFullBox(String cabinetId, Integer customerId, String orderId, Integer batteryType) throws InterruptedException, ExecutionException {
        String cacheKey = CacheKey.key(CacheKey.K_CABINET_ID_CUSTOMER_ID_V_ZERO, cabinetId, customerId);

        OperationFuture<Boolean> future = memCachedClient.add(cacheKey, 0, MemCachedConfig.CACHE_TEN_SECOND);
        if (future == null || future.get() == false) {
            return RestResult.result(RespCode.CODE_2.getValue(), "操作过于频繁,请稍后重试");
        }

        if (StringUtils.isEmpty(orderId)) {
            Cabinet cabinet = cabinetService.find(cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }

            List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());

            //判断用户是否存在电池，如果用户在柜子中存在电池并且是客户未取出状态，取出自己的电池
            for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                //新电未取出的情况
                if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                    BatteryOrder batteryOrder = batteryOrderService.find(battery.getOrderId());
                    if (batteryOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "换电订单不存在");
                    }
                    CabinetBox cabinetBox = cabinetBoxService.find(batteryOrder.getTakeCabinetId(), batteryOrder.getTakeBoxNum());
                    if (cabinetBox == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
                    }
                    if (StringUtils.isEmpty(cabinetBox.getBatteryId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱中没有电池");
                    }
                    if (!cabinetBox.getBatteryId().equals(battery.getId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱中电池编号错误");
                    }
                    Map map = new HashMap();
                    map.put("subcabinetId", cabinetBox.getCabinetId());
                    map.put("subcabinetName", cabinet.getCabinetName());
                    map.put("boxNum", cabinetBox.getBoxNum());
                    map.put("batteryId", battery.getCode());
                    map.put("volume", battery.getVolume());

                    int openSuccess = 0;

                    //发送开箱指令
                    ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                    //如果协议发送成功
                    if (result.getCode() == RespCode.CODE_0.getValue()) {
                        openSuccess = 1;
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单%s打开满箱成功", customer.getFullname(), customer.getMobile(), orderId),
                                customer.getFullname());

                    } else {
                        openSuccess = 0;
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单%s打开满箱失败", customer.getFullname(), customer.getMobile(), orderId),
                                customer.getFullname());

                    }

                    map.put("openSuccess", openSuccess);
                    map.put("step", "take_new_battery");
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }
            }

            //取新电
            int maxCount = 1;//默认1块电池

            if(batteryList.size() >= maxCount) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已拥有最大数目的电池");
            }

            CabinetBox cabinetBox = cabinetBoxService.findOneFull(cabinetId, batteryType, null);
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
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
            }

            int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
            if (effect == 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }

            Map map = new HashMap();
            map.put("subcabinetId", cabinetBox.getCabinetId());
            map.put("subcabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryId", battery.getCode());
            map.put("volume", battery.getVolume());

            int openSuccess = 0;

            //发送开箱指令
            ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
            //如果协议发送成功
            if (result.getCode() == RespCode.CODE_0.getValue()) {
                openSuccess = 1;
                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, null);
                map.put("orderId", order.getId());

            } else {
                openSuccess = 0;
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
                    }else{
                        openSuccess = 1;
                    }
                    BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, null);
                    map.put("orderId", order.getId());
                }
            }
            map.put("openSuccess", openSuccess);
            map.put("step", "take_new_battery");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else { //重新开箱
            BatteryOrder batteryOrder = batteryOrderService.find(orderId);
            if (batteryOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
            }
            if (batteryOrder.getOrderStatus() == BatteryOrder.OrderStatus.TAKE_OUT.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池已取出");
            }
            Cabinet cabinet = cabinetService.find(cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }
            CabinetBox cabinetBox = cabinetBoxService.find(batteryOrder.getTakeCabinetId(), batteryOrder.getTakeBoxNum());
            if (cabinetBox == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "没有符合类型的已充满电池");
            }
            Battery battery = batteryService.find(cabinetBox.getBatteryId());
            if (battery == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
            }

            Map map = new HashMap();
            map.put("subcabinetId", cabinetBox.getCabinetId());
            map.put("subcabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryId", battery.getCode());
            map.put("volume", battery.getVolume());
            int openSuccess = 1;
            if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                // 箱门已打开
            } else {
                if (StringUtils.isEmpty(cabinetBox.getBatteryId())) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "电池已取出");
                }
                //发送开箱指令
                ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                //如果协议发送成功
                if (result.getCode() == RespCode.CODE_0.getValue()) {
                    openSuccess = 1;
                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单%s重新打开满箱成功", customer.getFullname(), customer.getMobile(), orderId),
                            customer.getFullname());
                } else {
                    openSuccess = 0;

                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单%s重新打开满箱失败", customer.getFullname(), customer.getMobile(), orderId),
                            customer.getFullname());

                }
            }

            map.put("openSuccess", openSuccess);
            map.put("step", "take_new_battery");
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }
    }

    private RestResult openEmptyBoxForBackBattery(Integer customerId, String orderId, Battery battery) throws InterruptedException {
        BackBatteryOrder order = backBatteryOrderService.find(orderId);
        if (order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        if (order.getCustomerId().intValue() != customerId) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户无权操作");
        }

        if (order.getOrderStatus() != BackBatteryOrder.OrderStatus.SUCCESS.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单的状态是 " + BackBatteryOrder.OrderStatus.getName(order.getOrderStatus()));
        }

        Customer customer = customerService.find(order.getCustomerId());
        Cabinet cabinet = cabinetService.find(order.getCabinetId());

        Cabinet subcabinet = cabinetService.find(order.getCabinetId());
        if (subcabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        CabinetBox subcabinetBox = cabinetBoxService.find(order.getCabinetId(), order.getBoxNum());
        if (subcabinetBox.getBoxStatus() != CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "箱子状态不是退租锁定");
        }

        Map data = new HashMap();
        data.put("subcabinetId", subcabinet.getId());
        data.put("subcabinetName", subcabinet.getCabinetName());
        data.put("boxNum", subcabinetBox.getBoxNum());
        data.put("batteryId", battery.getCode());
        data.put("volume", "");
        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, order.getCabinetId(), order.getBoxNum(), subcabinet.getSubtype());
        //如果协议发送成功
        if (result.getCode() == RespCode.CODE_0.getValue()) {
            data.put("openSuccess", 1);

            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    subcabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 退租订单%s, 开空箱, 开箱成功", customer.getFullname(), customer.getMobile(), order.getId()),
                    customer.getFullname());

        } else {
            data.put("openSuccess", 0);

            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    subcabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 退租订单%s, 开空箱, 开箱失败", customer.getFullname(), customer.getMobile(), order.getId()),
                    customer.getFullname());
        }
        data.put("step", "back_battery");
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    private RestResult batteryOrderOpenEmptyBox(String cabinetId, Long customerId, Integer batteryType) throws InterruptedException {

        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customerId) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

        CabinetBox subcabinetBox = null;
        String cacheKey = CacheKey.key(CacheKey.K_CUSTOMER_ID_CABINET_ID_V_BOX_NUM, customer.getId(), cabinet.getId());
        String emptyBoxNum = (String) memCachedClient.get(cacheKey);
        if (StringUtils.isNotEmpty(emptyBoxNum)) {
            CabinetBox box = cabinetBoxService.find(cabinetId, emptyBoxNum);
            if (box != null && (box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue() || box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()) && StringUtils.isEmpty(box.getBatteryId())) {
                subcabinetBox = box;
            }
        }

        //查询合适的空箱号
        if (subcabinetBox == null) {
            subcabinetBox = cabinetBoxService.findOneEmptyBoxNum(cabinetId,batteryType);
            if (subcabinetBox == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "没有可用空箱");
            }
        }

        int subType = cabinet.getSubtype();
        String subcabinetId = subcabinetBox.getCabinetId();
        String boxNum = subcabinetBox.getBoxNum();

        if (subcabinetBox.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()) {
            int effect = cabinetBoxService.lockBox(subcabinetId, boxNum, CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
            if (effect == 0) {
                log.error("lockBox fail, subcabinetId:{}, boxNum:{}", subcabinetId, boxNum);
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }
        }

        memCachedClient.set(cacheKey, boxNum, MemCachedConfig.CACHE_THREE_MINUTE);

        int openSuccess = 0;
        Map data = new HashMap();
        data.put("subcabinetId", subcabinetId);
        data.put("subcabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, subcabinetId, boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", subcabinetId, boxNum);
            }
            openSuccess = 1;

        } else {
            CabinetBox box = cabinetBoxService.find(subcabinetId, boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(subcabinetId, boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", subcabinetId, boxNum);
        }

        if (openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    subcabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

            //保存开箱客户进缓存，如果放入非开箱客户电池，给出提示
            memCachedClient.set(CacheKey.key(CacheKey.K_CABINET_BOX_V_CUSTMOER_ID, cabinet.getId(), subcabinetBox.getBoxNum()), customerId, MemCachedConfig.CACHE_THREE_MINUTE);


        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    subcabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        }

        data.put("openSuccess", openSuccess);
        data.put("step", "put_old_batter");
        String subcabinetIdBox = cabinet.getId() + ":" + subcabinetBox.getBoxNum();
        memCachedClient.set(CacheKey.key(CacheKey.K_CUSTOMER_ID_V_SUBCABINET_ID_BOX, customerId), subcabinetIdBox, MemCachedConfig.CACHE_FIVE_MINUTE);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

}
