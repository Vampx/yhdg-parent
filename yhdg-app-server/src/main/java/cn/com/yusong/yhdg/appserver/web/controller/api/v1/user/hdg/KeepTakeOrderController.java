package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.hdg.KeepOrderMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.KeepOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.KeepTakeOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
@Controller("api_v1_user_hdg_keep_take_order")
@RequestMapping(value = "/api/v1/user/hdg/keep_take_order")
public class KeepTakeOrderController extends ApiController {


    @Autowired
    KeepTakeOrderService keepTakeOrderService;

    @Autowired
    CabinetService cabinetService;


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
        List<KeepTakeOrder> orderList = keepTakeOrderService.findList(userId,param.offset,param.limit);
        if(!orderList.isEmpty()){
            for (KeepTakeOrder keepTakeOrder:orderList){
                Map mapOrder = new HashMap();
                mapOrder.put("id",keepTakeOrder.getId());
                mapOrder.put("orderCount",keepTakeOrder.getOrderCount());
                mapOrder.put("cabinetId",keepTakeOrder.getCabinetId());
                mapOrder.put("cabinetName",keepTakeOrder.getCabinetName());
                mapOrder.put("takeTime",keepTakeOrder.getCreateTime() != null?
                        DateFormatUtils.format(keepTakeOrder.getCreateTime(), Constant.DATE_TIME_FORMAT):null);
                Cabinet cabinet = cabinetService.find(keepTakeOrder.getCabinetId());
                if(cabinet != null) {
                    mapOrder.put("cabinetAddress",cabinet.getAddress());
                }

                list.add(mapOrder);
            }
        }

        return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",list);
    }

    @Autowired
    KeepOrderService keepOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderParam{
        @NotBlank(message = "订单ID不能为空")
        public String orderId;
    }
    @ResponseBody
    @RequestMapping(value = "detail.htm")
    public RestResult detail(@Valid @RequestBody OrderParam param){
        TokenCache.Data tokenData = getTokenData();
        long userId = tokenData.userId;


        List<Map> list = new ArrayList<Map>();
        KeepTakeOrder order = keepTakeOrderService.find(param.orderId);
        Map map = new HashMap();
        if(order != null){
            map.put("id", order.getId());
            map.put("orderCount", order.getOrderCount());
            map.put("cabinetId", order.getCabinetId());
            map.put("cabinetName", order.getCabinetName());
            map.put("takeTime", order.getCreateTime() != null?
                    DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT):null);
            Cabinet cabinet = cabinetService.find(order.getCabinetId());
            map.put("cabinetAddress",cabinet.getAddress());
        }else {
               return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        List<KeepOrder> keepList = keepOrderService.findTakeList(param.orderId);
        if(!keepList.isEmpty()){
            for (KeepOrder keepOrder:keepList){
                Map map1 = new HashMap();
                map1.put("id",keepOrder.getId());
                map1.put("batteryId",keepOrder.getBatteryId());
                map1.put("boxNum",keepOrder.getTakeBoxNum());
                map1.put("volume",keepOrder.getInitVolume());
                map1.put("takeTime",keepOrder.getTakeTime() != null?
                        DateFormatUtils.format(keepOrder.getTakeTime(), Constant.DATE_TIME_FORMAT):null);
                list.add(map1);
            }
        }
        map.put("orderList",list);
        return  RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }


}
