package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

@Controller("api_v1_cabinet_app_hdg_cabinet")
@RequestMapping(value = "/api/v1/cabinet_app/hdg/cabinet")
public class CabinetController extends ApiController {

    private static final Logger log = LogManager.getLogger(CabinetController.class);

    @Autowired
    DictItemService dictItemService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CabinetOperateLogService cabinetOperateLogService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    MemCachedClient memCachedClient;


    @ResponseBody
    @RequestMapping(value = "/info")
    public RestResult info() {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        List<String> cabinetIdList = cabinetService.findIdByCabinetId(tokenData.cabinetId);
        Map map = new HashMap();
        map.put("cabinetId", cabinet.getId());
        map.put("tel", cabinet.getTel());
        map.put("address", cabinet.getAddress());
        map.put("batteryCount", cabinetBoxService.findFullCount(cabinet.getId()));
        map.put("cabinetIdList", cabinetIdList);

        return RestResult.mapResult(RespCode.CODE_0.getValue()).putAll(map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenEmptyBoxParam {
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/open_empty_box")
    public RestResult openEmptyBox(@Valid @RequestBody OpenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();

        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customer.getId()) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

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
        if(batteryType == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
        }

        //查询合适的空箱号
        CabinetBox cabinetBox = cabinetBoxService.findOneEmptyBoxNum(tokenData.cabinetId, batteryType);
        if (cabinetBox == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "没有可用空箱");
        }
        int subType = cabinet.getSubtype();
        String boxNum = cabinetBox.getBoxNum();

        int effect = cabinetBoxService.lockBox(cabinet.getId(), boxNum, CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
        if (effect == 0) {
            log.error("lockBox fail, cabinetId:{}, boxNum:{}", cabinet.getId(), boxNum);
            return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
        }

        int openSuccess = 0;
        Map data = new HashMap();
        data.put("cabinetId", cabinet.getId());
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, cabinet.getId(), boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", cabinet.getId(), boxNum);
            }
            openSuccess = 1;
        } else {
            CabinetBox box = cabinetBoxService.find(cabinet.getId(), boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(cabinet.getId(), boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", cabinet.getId(), boxNum);
        }

        if(openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());
        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());
        }


        data.put("openSuccess", openSuccess);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenNewBattery {
        public String orderId;
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/open_new_battery")
    public RestResult openNewBattery(@Valid @RequestBody OpenNewBattery param) throws InterruptedException {
        String cabinetId = getTokenData().cabinetId;
        long customerId = param.customerId;
        if (StringUtils.isEmpty(param.orderId)) {
            Cabinet cabinet = cabinetService.find(cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }

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
            if(batteryType == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
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
                    map.put("customerFullname", customer.getFullname());
                    map.put("cabinetId", cabinetId);
                    map.put("cabinetName", cabinet.getCabinetName());
                    map.put("boxNum", cabinetBox.getBoxNum());
                    map.put("batteryTypeName", batteryOrder.getBatteryTypeName());
                    map.put("batteryId", battery.getId());
                    map.put("volume", battery.getVolume());
                    map.put("orderId", batteryOrder.getId());
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
                                String.format("客户%s %s, 换电订单%s打开满箱成功", customer.getFullname(), customer.getMobile(), param.orderId),
                                customer.getFullname());

                    } else {
                        openSuccess = 0;
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单%s打开满箱失败", customer.getFullname(), customer.getMobile(), param.orderId),
                                customer.getFullname());

                    }

                    map.put("openSuccess", openSuccess);
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }
            }

            //取新电
            int maxCount = 1;//默认1块电池
            String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), cabinet.getAgentId());
            if(!StringUtils.isEmpty(maxCountStr)){
                maxCount = Integer.parseInt(maxCountStr);
            }
            if(batteryList.size() >= maxCount) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已拥有最大数目的电池");
            }

            //查询是否有预约订单
            String bespeakBoxNum = null;
            BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(param.customerId);
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
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
            }

            if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
                int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
                if (effect == 0) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
                }
            }

            Map map = new HashMap();
            map.put("customerFullname", customer.getFullname());
            map.put("cabinetId", cabinetId);
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
            map.put("batteryId", battery.getId());
            map.put("volume", battery.getVolume());
            int openSuccess = 0;

            //发送开箱指令
            ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
            //如果协议发送成功
            if (result.getCode() == RespCode.CODE_0.getValue()) {
                openSuccess = 1;
                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
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
                    BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
                    map.put("orderId", order.getId());
                }
            }
            map.put("openSuccess", openSuccess);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else { //重新开箱
            BatteryOrder batteryOrder = batteryOrderService.find(param.orderId);
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
            map.put("orderId", batteryOrder.getId());
            map.put("customerFullname", customer.getFullname());
            map.put("cabinetId", cabinetId);
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
            map.put("batteryId", battery.getId());
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
                            String.format("客户%s %s, 换电订单%s重新打开满箱成功", customer.getFullname(), customer.getMobile(), param.orderId),
                            customer.getFullname());
                } else {
                    openSuccess = 0;

                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单%s重新打开满箱失败", customer.getFullname(), customer.getMobile(), param.orderId),
                            customer.getFullname());

                }
            }

            map.put("openSuccess", openSuccess);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReopenEmptyBoxParam {
        public long customerId;
        public String cabinetId;
        public String boxNum;
    }

    @ResponseBody
    @RequestMapping(value = "/reopen_empty_box.htm")
    public RestResult reopenEmptyBox(@Valid @RequestBody ReopenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        String cabinetId = tokenData.cabinetId;

        Cabinet cabinet = cabinetService.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customer.getId()) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

        //查询合适的空箱号
        CabinetBox cabinetBox = cabinetBoxService.find(cabinetId, param.boxNum);
        if (cabinetBox == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "没有可用空箱");
        }

        int subType = cabinet.getSubtype();
        String boxNum = cabinetBox.getBoxNum();

        int openSuccess = 1;
        Map data = new HashMap();
        data.put("cabinetId", cabinetId);
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);

        if (cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.EMPTY.getValue() && cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.EMPTY_LOCK.getValue()) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "箱号不是空箱", data);
        }

        if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        }

        if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()) {
            int effect = cabinetBoxService.lockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
            if (effect == 0) {
                log.error("lockBox fail, cabinetId:{}, boxNum:{}", cabinetId, boxNum);
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }
        }

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, cabinetId, boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", cabinetId, boxNum);
            }
            openSuccess = 1;

        } else {
            CabinetBox box = cabinetBoxService.find(cabinetId, boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", cabinetId, boxNum);
        }

        if (openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打空箱成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打开空箱失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        }

        data.put("openSuccess", openSuccess);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryBoxBatteryParam {
        public long customerId;
        public String cabinetId;
        public String boxNum;
    }

    @ResponseBody
    @RequestMapping(value = "/query_box_battery.htm")
    public RestResult queryBoxBattery(@Valid @RequestBody QueryBoxBatteryParam param) {
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "分柜编号不存在");
        }

        CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, param.boxNum);
        if (cabinetBox == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
        }

        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Map map = new HashMap();
        map.put("batteryId", cabinetBox.getBatteryId());
        map.put("isOpen", cabinetBox.getIsOpen());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    public static class LoopExchangeBatteryParam {
        public long customerId;
        public String oldBoxNum;
        public String newBoxNum;
        public int type;
    }


    @ResponseBody
    @RequestMapping(value = "/loop_exchange_battery.htm")
    public RestResult loopExchangeBattery(@RequestBody LoopExchangeBatteryParam param) throws InterruptedException {
        long customerId = param.customerId;
        String cabinetId = getTokenData().cabinetId;
        RestResult result = cabinetService.loopExchangeBattery(customerId, cabinetId, param.oldBoxNum, param.newBoxNum, param.type, true);
        if(result.getCode() == 0) {
            Map root = (Map) result.getData();
            Map data = (Map) root.get("data");

            if("opening_new_battery".equals(root.get("step"))) {
                if("yes".equals(data.get("need_open_battery"))) {
                    data.remove("need_open_battery");

                    Cabinet cabinet = cabinetService.find(cabinetId);
                    Customer customer = customerService.find(customerId);

                    Integer batteryType = null;
                    CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
                    if(customerExchangeInfo != null){
                        batteryType = customerExchangeInfo.getBatteryType();
                    }else{
                        ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customerId);
                        if(exchangeWhiteList != null){
                            batteryType = exchangeWhiteList.getBatteryType();
                        }
                    }
                    if(batteryType == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
                    }

                    String errorMessage = null;

                    //查询是否有预约订单
                    String bespeakBoxNum = null;
                    BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(param.customerId);
                    if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinetId)){
                        bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
                    }

                    CabinetBox cabinetBox  = cabinetBoxService.findOneFull(cabinetId, batteryType, bespeakBoxNum);
                    if (cabinetBox == null) {
                        int fullCount = cabinetBoxService.findFullCount(cabinetId);
                        errorMessage = "没有符合类型的已充满电池";
                        if (fullCount != 0) {
                            errorMessage = "扫码者与当前柜子不匹配";
                        }
                        return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
                    }


                    Battery battery = batteryService.find(cabinetBox.getBatteryId());
                    if(battery == null){
                        return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
                    }

                    if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
                        int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
                        if(effect == 0) {
                            return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
                        }
                    }

                    //发送开箱指令
                    ClientBizUtils.SerialResult serialResult = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                    //如果协议发送成功
                    if (serialResult.getCode() == RespCode.CODE_0.getValue()) {
                        batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                        NewBoxNum newBoxNum = new NewBoxNum("app-server", data.get("cabinetId").toString(), null, data.get("boxNum").toString(), cabinetBox.getBoxNum(), new Date());
                        String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, newBoxNum.cabinetId, newBoxNum.oldBoxNum);
                        memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//                        String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
//                        memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

                        if (log.isDebugEnabled()) {
                            log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", data.get("cabinetId").toString(), data.get("boxNum").toString(), cabinetBox.getBoxNum());
                        }

                    } else {
                        if (serialResult.getSerial() == -1) {
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

                            NewBoxNum newBoxNum = new NewBoxNum("app-server", data.get("cabinetId").toString(), null, data.get("boxNum").toString(), cabinetBox.getBoxNum(), new Date());
                            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, newBoxNum.cabinetId, newBoxNum.oldBoxNum);
                            memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//                            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
//                            memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

                            if (log.isDebugEnabled()) {
                                log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", data.get("cabinetId").toString(), data.get("boxNum").toString(), cabinetBox.getBoxNum());
                            }
                        }
                    }
                }

            }
        }

        return result;
    }

    public static class ZhizuLoopExchangeBatteryParam {
        public String customerMobile;
        public String oldBoxNum;
        public String newBoxNum;
        public int type;
    }

    @ResponseBody
    @RequestMapping(value = "/zhizu_loop_exchange_battery.htm")
    public RestResult zhizuLoopExchangeBattery(@RequestBody ZhizuLoopExchangeBatteryParam param) throws InterruptedException {
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }


        String cabinetId = getTokenData().cabinetId;
        RestResult result = cabinetService.loopExchangeBattery(customer.getId(), cabinetId, param.oldBoxNum, param.newBoxNum, param.type, false);
        if(result.getCode() == 0) {
            Map root = (Map) result.getData();
            Map data = (Map) root.get("data");
        }
        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TakeOldBatteryParam {
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/take_old_battery.htm")
    public RestResult batteryOrderTakeOldBattery(@RequestBody TakeOldBatteryParam param) throws InterruptedException {
        String cabinetId = getTokenData().cabinetId;
        long customerId = param.customerId;

        Customer customer = customerService.find(customerId);
        if(customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        if (batteryList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有电池");
        }


        for(CustomerExchangeBattery customerExchangeBattery : batteryList){
            Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
            if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                Cabinet cabinet = cabinetService.find(battery.getCabinetId());
                if (cabinet == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "换电柜[" + battery.getCabinetId() + "]不存在");
                }

                int openSuccess = 0;
                //发送开箱指令
                ClientBizUtils.SerialResult serialResult = ClientBizUtils.openStandardBox(config, battery.getCabinetId(), battery.getBoxNum(), cabinet.getSubtype());
                //如果协议发送成功
                if (serialResult.getCode() == RespCode.CODE_0.getValue()) {
                    openSuccess = 1;
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());

                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(battery.getBoxNum());
                    operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出旧电开箱成功", battery.getOrderId()));
                    operateLog.setOperator(customer.getFullname());
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogService.insert(operateLog);
                } else {
                    openSuccess = 0;
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());

                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(battery.getBoxNum());
                    operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出旧电开箱失败", battery.getOrderId()));
                    operateLog.setOperator(customer.getFullname());
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogService.insert(operateLog);
                }

                Map data = new HashMap();
                data.put("customerFullname", customer.getFullname());
                data.put("boxNum", battery.getBoxNum());
                data.put("batteryId", "");
                data.put("openSuccess", openSuccess);

                return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
            }
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在待付款电池");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetBattery {
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping(value = "/cabinet_battery.htm")
    public RestResult cabinetBattery(@RequestBody CabinetBattery param) {
        return cabinetService.subcabinetBattery(param.cabinetId);
    }
}
