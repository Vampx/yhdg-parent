package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.zd;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentappserver.service.zd.*;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_shop_zd_rent_order")
@RequestMapping("/agent_api/v1/shop/zd/rent_order")
public class RentOrderController extends ApiController {
    final static Logger log = LogManager.getLogger(RentOrderController.class);
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
        public String keyword;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        String shopId = tokenData.shopId;
        int agentId = getTokenData().agentId;

        List<RentOrder> list = rentOrderService.findListByShop(agentId,shopId, param.keyword, param.offset, param.limit);

        List<NotNullMap> result = new ArrayList<NotNullMap>();
        for (RentOrder order : list) {
            NotNullMap line = new NotNullMap();
            line.putString("id", order.getId());
            line.putString("customerFullname", order.getCustomerFullname());
            line.putString("customerMobile", order.getCustomerMobile());
            line.putString("batteryId", order.getBatteryId());
            line.putString("batteryTypeName", order.getBatteryTypeName());
            line.putInteger("currentVolume", order.getCurrentVolume());
            line.putInteger("currentDistance", order.getCurrentDistance());
            line.putInteger("status", order.getStatus());
            line.putDateTime("createTime", order.getCreateTime());
            result.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        @NotBlank
        public String id;
    }

    /**
     * 16-查询租电订单详情
     */
    @ResponseBody
    @RequestMapping(value = "info.htm")
    public RestResult info(@Valid @RequestBody InfoParam param) {
        RentOrder rentOrder = rentOrderService.find(param.id);
        if(rentOrder == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        CustomerRentInfo customerRentInfo = customerRentInfoService.find(rentOrder.getCustomerId());
        if(customerRentInfo == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "未关联租电信息");
        }

        RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(customerRentInfo.getForegiftOrderId());
        if(rentForegiftOrder == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "押金订单不存在");
        }

        Battery battery = batteryService.find(rentOrder.getBatteryId());
        if(battery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findLastEndTime(rentOrder.getCustomerId());
        RentInsuranceOrder rentInsuranceOrder = null;
        if (rentPeriodOrder != null) {
            rentInsuranceOrder = rentInsuranceOrderService.findByCustomerId(
                    rentOrder.getCustomerId(),
                    rentPeriodOrder.getBatteryType(),
                    RentInsuranceOrder.Status.PAID.getValue());
        }

        NotNullMap data = new NotNullMap();
        data.putString("id", rentOrder.getId());
        data.putString("fullname", rentOrder.getCustomerFullname());
        data.putString("mobile", rentOrder.getCustomerMobile());
        data.putInteger("foregiftMoney", rentForegiftOrder.getMoney());
        data.putInteger("packetPeriodMoney", rentPeriodOrder != null ? rentPeriodOrder.getPrice() : 0);
        data.putDate("packetBeginTime", rentPeriodOrder != null ? rentPeriodOrder.getBeginTime() : null);
        data.putDate("packetEndTime", rentPeriodOrder != null ? rentPeriodOrder.getEndTime() : null);
        data.putLong("packetRestDay", rentPeriodOrder != null ? InstallUtils.getRestDay(rentPeriodOrder.getEndTime()) : 0);

        data.putInteger("insuranceMoney", rentInsuranceOrder != null ? rentInsuranceOrder.getPrice() : 0);
        data.putInteger("insurancePaid", rentInsuranceOrder != null ? rentInsuranceOrder.getPaid() : 0);
        data.putInteger("monthCount", rentInsuranceOrder != null ? rentInsuranceOrder.getMonthCount() : 0);
        data.putInteger("volume", rentOrder.getCurrentVolume());
        data.putInteger("distance", rentOrder.getCurrentDistance());
        data.putInteger("status", rentOrder.getStatus());
        data.putString("batteryId", rentOrder.getBatteryId());
        data.putInteger("batteryType", rentOrder.getBatteryType());
        data.putString("batteryCode", battery.getCode());
        data.putString("shellCode", battery.getShellCode());
        data.putString("shopId", rentOrder.getShopId());
        data.putString("shopName", rentOrder.getShopName());
        data.putDateTime("backTime", rentOrder.getBackTime());
        data.putDateTime("createTime", rentOrder.getCreateTime());

        return RestResult.dataResult(0, null, data);
    }


}
