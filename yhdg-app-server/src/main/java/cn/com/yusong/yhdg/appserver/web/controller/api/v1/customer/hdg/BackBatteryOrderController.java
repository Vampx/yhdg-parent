package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Controller(value = "api_v1_customer_hdg_back_battery_order")
@RequestMapping(value = "/api/v1/customer/hdg/back_battery_order")
public class BackBatteryOrderController extends ApiController {
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    CustomerService customerService;

    /**
     * 33-申请退还电池押金
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateOrderParam {
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public RestResult createOrder(@RequestBody CreateOrderParam param) {
        return backBatteryOrderService.createOrder(param.cabinetId, getTokenData().customerId);
    }

    @ResponseBody
    @RequestMapping(value = "create_fast.htm")
    public RestResult createFastOrder(@RequestBody CreateOrderParam param) {
        return backBatteryOrderService.createFastOrder(param.cabinetId, getTokenData().customerId);
    }
    /**
     * 34-查询退租电池订单明细
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "detail.htm")
    public RestResult getDetail(@RequestBody DetailParam param) {
        Customer customer = customerService.find(getTokenData().customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不出存在");
        }

        if (StringUtils.isEmpty(param.orderId)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单号不存在");
        }

        BackBatteryOrder backBatteryOrder = backBatteryOrderService.find(param.orderId);
        if (backBatteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        Cabinet cabinet = cabinetService.find(backBatteryOrder.getCabinetId());
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        List<Map> batteries = batteryService.findBoxBattery(backBatteryOrder.getCabinetId());
        List<Map> fullList = new ArrayList<Map>();
//        Map<String, String> dictItemMap = dictItemService.findMapByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue());

        for (Map map : batteries) {
            Map full = new HashMap();
  //          String type = dictItemMap.get(map.get("type").toString());
    //        full.put("type", type != null ? type : map.get("type").toString());
            full.put("count", Integer.parseInt(map.get("count").toString()));
            fullList.add(full);
        }

        Map line = new HashMap();
        line.put("id", param.orderId);

        line.put("cabinetId", backBatteryOrder.getCabinetId());
        line.put("cabinetName", backBatteryOrder.getCabinetName());
        line.put("boxNum", backBatteryOrder.getBoxNum());
        line.put("address", cabinet.getAddress());
        line.put("lng", cabinet.getLng());
        line.put("lat", cabinet.getLat());
        line.put("orderStatus", backBatteryOrder.getOrderStatus());
        line.put("emptyCount", cabinetBoxService.findEmptyCount(backBatteryOrder.getCabinetId()));
        line.put("fullCount", cabinetBoxService.findFullCount(backBatteryOrder.getCabinetId()));
        line.put("fullList", fullList);

        //分钟
        String mins;
        if (backBatteryOrder.getAgentId() != null && backBatteryOrder.getAgentId() != 0) {
            mins = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BACK_BESPEAK_TIME.getValue(), backBatteryOrder.getAgentId());
        } else {
            mins = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.BACK_BESPEAK_TIME.getValue());
        }
        long t1 = System.currentTimeMillis();
        long t2 = backBatteryOrder.getCreateTime().getTime();
        line.put("restTime", Long.parseLong(mins) * 60 - TimeUnit.MILLISECONDS.toSeconds(t1 - t2));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", line);
    }

    /**
     * 35-取消退租订单
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelParam {
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "cancel.htm")
    public RestResult cancel(@RequestBody CancelParam param) {
        return backBatteryOrderService.cancelOrder(param.orderId, getTokenData().customerId);
    }

    /**
     * 36-退租订单开空箱
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenEmptyBoxParam {
        @NotBlank
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "open_empty_box.htm")
    public RestResult openEmptyBox(@RequestBody OpenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();

        BackBatteryOrder order = backBatteryOrderService.find(param.orderId);
        if (order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        if (order.getCustomerId() != tokenData.customerId) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户无权操作");
        }

        if (order.getOrderStatus() != BackBatteryOrder.OrderStatus.SUCCESS.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单的状态是 " + BackBatteryOrder.OrderStatus.getName(order.getOrderStatus()));
        }

        Customer customer = customerService.find(order.getCustomerId());
        Cabinet cabinet = cabinetService.find(order.getCabinetId());

        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        CabinetBox cabinetBox = cabinetBoxService.find(order.getCabinetId(), order.getBoxNum());
        if (cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "箱子状态不是退租锁定");
        }

        Map data = new HashMap();
        data.put("cabinetId", cabinet.getId());
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", cabinetBox.getBoxNum());


        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, order.getCabinetId(), order.getBoxNum(), cabinet.getSubtype());
        //如果协议发送成功
        if (result.getCode() == RespCode.CODE_0.getValue()) {
            data.put("openSuccess", 1);

            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 退租订单%s, 开空箱, 开箱成功", customer.getFullname(), customer.getMobile(), order.getId()),
                    customer.getFullname());

        } else {
            data.put("openSuccess", 0);

            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 退租订单%s, 开空箱, 开箱失败", customer.getFullname(), customer.getMobile(), order.getId()),
                    customer.getFullname());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

}
