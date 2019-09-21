package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.hdg;

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
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
@Controller(value = "api_v1_independent_customer_hdg_back_battery_order")
@RequestMapping(value = "/api/v1/independent_customer/hdg/back_battery_order")
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
        public String customerMobile;
        public String cabinetId;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "create.htm")
    public RestResult createOrder(@RequestBody CreateOrderParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        RestResult result = backBatteryOrderService.createOrder(param.cabinetId,customer.getId());
        if(result.getCode() == RespCode.CODE_0.getValue()){
            BackBatteryOrder backBatteryOrder = backBatteryOrderService.find((String)((Map)result.getData()).get("orderId"));
            Map line = new HashMap();
            line.put("id", backBatteryOrder.getId());
            line.put("subcabinetId", backBatteryOrder.getCabinetId());
            line.put("boxNum", backBatteryOrder.getBoxNum());
            line.put("lockTime", DateFormatUtils.format(backBatteryOrder.getExpireTime(), Constant.DATE_TIME_FORMAT));
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "", line);
        }else{
            return result;
        }
    }

    /**
     * 35-取消退租订单
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelParam {
        public String customerMobile;
        public String cabinetId;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "cancel.htm")
    public RestResult cancel(@RequestBody CancelParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer == null ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        BackBatteryOrder backBatteryOrder = backBatteryOrderService.findBatteryOrder(customer.getId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        if (backBatteryOrder == null ) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户没有申请退租");
        }

        return backBatteryOrderService.cancelOrder(backBatteryOrder.getId(), customer.getId());
    }

}
