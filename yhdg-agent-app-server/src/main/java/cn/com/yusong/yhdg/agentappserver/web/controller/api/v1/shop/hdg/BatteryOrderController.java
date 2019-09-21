package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_shop_hdg_battery_order")
@RequestMapping("/agent_api/v1/shop/hdg/battery_order")
public class BatteryOrderController extends ApiController {
    @Autowired
    private BatteryOrderService batteryOrderService;
    @Autowired
    private CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    private PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    private InsuranceOrderService insuranceOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String keyword;
        public int offset;
        public int limit;
    }

    /**
     * 7-查询电池订单
     *
     */
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        List<BatteryOrder> list = batteryOrderService.findListByShop(shopId, param.keyword, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (BatteryOrder order : list) {
            NotNullMap line = new NotNullMap();
            line.putString("id", order.getId());
            line.putString("batteryTypeName", order.getBatteryTypeName());
            line.putString("customerFullname", order.getCustomerFullname());
            line.putString("customerMobile", order.getCustomerMobile());
            line.putInteger("currentVolume", order.getCurrentVolume());
            line.putInteger("status", order.getOrderStatus());
            line.putInteger("currentDistance", order.getCurrentDistance());
            line.putDateTime("takeTime", order.getTakeTime());
            line.putDateTime("createTime", order.getCreateTime());
            result.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        @NotBlank(message = "订单ID不能为空")
        public String id;
    }

    @ResponseBody
    @RequestMapping(value = "/info.htm")
    public RestResult info(@Valid @RequestBody InfoParam param) {
        BatteryOrder batteryOrder = batteryOrderService.find(param.id);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(batteryOrder.getCustomerId());
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findLastEndTime(batteryOrder.getCustomerId());
        InsuranceOrder insuranceOrder = null;
        if (packetPeriodOrder != null) {
            insuranceOrder = insuranceOrderService.findByCustomerId(
                    batteryOrder.getCustomerId(),
                    packetPeriodOrder.getBatteryType(),
                    InsuranceOrder.Status.PAID.getValue());
        }

        NotNullMap data = new NotNullMap();
        data.putString("id", batteryOrder.getId());
        data.putString("fullname", batteryOrder.getCustomerFullname());
        data.putString("mobile", batteryOrder.getCustomerMobile());
        data.putInteger("foregiftMoney", customerExchangeInfo != null ? customerExchangeInfo.getForegift() : 0);
        data.putInteger("packetPeriodMoney", packetPeriodOrder != null ? packetPeriodOrder.getPrice() : 0);
        data.putDateTime("packetBeginTime", packetPeriodOrder != null ? packetPeriodOrder.getBeginTime() : null);
        data.putDateTime("packetEndTime", packetPeriodOrder != null ? packetPeriodOrder.getEndTime() : null);
        data.putLong("packetRestDay", packetPeriodOrder != null ? getRestDay(packetPeriodOrder.getEndTime()) : 0);
        data.putInteger("insuranceMoney", insuranceOrder != null ? insuranceOrder.getPrice() : 0);
        data.putInteger("insurancePaid", insuranceOrder != null ? insuranceOrder.getPaid() : 0);
        data.putInteger("monthCount", insuranceOrder != null ? insuranceOrder.getMonthCount() : 0);
        data.putInteger("distance", batteryOrder.getCurrentDistance());
        data.putDateTime("takeTime", batteryOrder.getTakeTime());
        data.putDateTime("putTime", batteryOrder.getPayTime());
        data.putString("payType", batteryOrder.getPayTypeName());//支付类型
        data.putInteger("status", batteryOrder.getOrderStatus());//状态
        data.putString("batteryId", batteryOrder.getBatteryId());//电池编号
        data.putString("batteryType", batteryOrder.getBatteryTypeName());
        data.putInteger("initVolume", batteryOrder.getInitVolume());//取出电量
        data.putInteger("currentVolume", batteryOrder.getCurrentVolume());//当前电量
        data.putString("takeCabinetName", batteryOrder.getTakeCabinetName());//取出站点名称
        data.putString("putCabinetName", batteryOrder.getPutCabinetName());//归还站点名称
        data.putString("putBoxNum", batteryOrder.getPutBoxNum());//放电柜箱号
        data.putString("takeBoxNum", batteryOrder.getTakeBoxNum());//取电柜箱号

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 计算剩余天数
     * @param date 结束日期
     * @return
     */
    private static long getRestDay(Date date) {
        long restDay = 0;
        Date now = new Date();
        if (now.getTime() < date.getTime()) {
            restDay = (date.getTime() - now.getTime()) / (24 * 60 * 60 * 1000);
        }

        return restDay;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompleteParam {
        // 订单id
        @NotBlank(message = "订单ID不能为空")
        public String id;
    }

    @ResponseBody
    @RequestMapping(value = "/complete.htm")
    public RestResult complete(@Valid @RequestBody CompleteParam param) {
        return batteryOrderService.complete(param.id);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BackBatteryParam {
        // 订单id
        @NotBlank(message = "订单ID不能为空")
        public String id;
    }

    @ResponseBody
    @RequestMapping(value = "/back_battery.htm")
    public RestResult backBattery(@Valid @RequestBody BackBatteryParam param) {
        return batteryOrderService.toBackBatteryOrder(param.id);
    }

}
