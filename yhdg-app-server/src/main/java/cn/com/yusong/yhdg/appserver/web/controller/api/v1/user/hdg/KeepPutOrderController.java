package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.BackBatteryOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.KeepOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.KeepPutOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.domain.hdg.KeepPutOrder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/11.
 */

@Controller("api_v1_user_hdg_keep_put_order")
@RequestMapping(value = "/api/v1/user/hdg/keep_put_order")
public class KeepPutOrderController extends ApiController {

    @Autowired
    KeepPutOrderService keepPutOrderService;

    @Autowired
    BackBatteryOrderService backBatteryOrderService;
    @Autowired
    KeepOrderService keepOrderService;

    @Autowired
    CabinetService cabinetService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam{
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "list.htm")
    public RestResult list(@RequestBody ListParam param ){
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;


        List<Map> list = new ArrayList<Map>();
        List<KeepPutOrder> orderList = keepPutOrderService.findList(userId,param.offset,param.limit);
        if(!orderList.isEmpty()){
            for (KeepPutOrder keepPutOrder:orderList){
                Map mapOrder = new HashMap();
                mapOrder.put("id", keepPutOrder.getId());
                mapOrder.put("orderCount", keepPutOrder.getOrderCount());
                mapOrder.put("cabinetId", keepPutOrder.getCabinetId());
                mapOrder.put("cabinetName", keepPutOrder.getCabinetName());
                mapOrder.put("putTime", keepPutOrder.getCreateTime() != null?
                        DateFormatUtils.format(keepPutOrder.getCreateTime(), Constant.DATE_TIME_FORMAT):null);
                Cabinet cabinet = cabinetService.find(keepPutOrder.getCabinetId());
                if(cabinet != null) {
                    mapOrder.put("cabinetAddress",cabinet.getAddress());
                }

                list.add(mapOrder);
            }
        }
        return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",list);
    }




    public static class OrderParam{
        @NotBlank(message = "订单ID不能为空")
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "detail.htm")
    public RestResult detail(@Valid @RequestBody OrderParam param){
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;

        Map map = new HashMap();
        List<Map> list = new ArrayList<Map>();
        KeepPutOrder order = keepPutOrderService.find(param.orderId);

        if(order != null){
            map.put("id", order.getId());
            map.put("orderCount", order.getOrderCount());
            map.put("cabinetId", order.getCabinetId());
            map.put("cabinetName", order.getCabinetName());
            map.put("putTime", order.getCreateTime() != null ?
                    DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT) : null);
            Cabinet cabinet = cabinetService.find(order.getCabinetId());
            map.put("cabinetAddress",cabinet.getAddress());
        }else {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        List<KeepOrder> keepList = keepOrderService.findPutList(param.orderId);
        if(!keepList.isEmpty()){
            for (KeepOrder keepOrder:keepList){
                Map map1 = new HashMap();
                map1.put("id",keepOrder.getId());
                map1.put("batteryId",keepOrder.getBatteryId());
                map1.put("boxNum",keepOrder.getPutBoxNum());
                map1.put("volume",keepOrder.getCurrentVolume());
                map1.put("putTime",keepOrder.getPutTime() != null?
                        DateFormatUtils.format(keepOrder.getPutTime(), Constant.DATE_TIME_FORMAT):null);
                list.add(map1);
            }
        }
        map.put("orderList",list);
        return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }
}
