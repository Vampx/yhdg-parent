package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerExchangeBatteryService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller("api_v1_independent_customer_hdg_battery_order")
@RequestMapping(value = "/api/v1/independent_customer/hdg/battery_order")
public class BatteryOrderController extends ApiController {
    final static Logger log = LogManager.getLogger(BatteryOrderController.class);

    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;

    /**
     * 查询换电记录
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String customerMobile;
        public String orderId;
        public int offset;
        public int limit;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/query_battery_order.htm")
    public RestResult getList(@RequestBody ListParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        return batteryOrderService.getListByCustomerAndOrderId(customer.getId(), param.orderId, param.offset, param.limit);
    }

    /**
     * 用户取新电(不通过柜子)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public String batteryId;
        public String customerMobile;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/create.htm")
    public RestResult create(@RequestBody CreateParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Battery battery = batteryService.findByCode(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        //用户存在电池时特殊处理
        if(batteryList.size() > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已绑定电池");
        }

        BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, null, null, null);
        Map returnMap = new HashMap();
        returnMap.put("id", order.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", returnMap);
    }


    /**
     * 结束订单
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompleteParam {
        public String customerMobile;
        public String orderId;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/complete.htm")
    public RestResult complete(@RequestBody CompleteParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        return batteryOrderService.complete(param.orderId);
    }


}
