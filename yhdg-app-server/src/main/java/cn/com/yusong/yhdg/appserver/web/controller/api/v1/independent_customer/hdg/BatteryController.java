package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerExchangeBatteryService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
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


@Controller("api_v1_independent_customer_hdg_battery")
@RequestMapping(value = "/api/v1/independent_customer/hdg/battery")
public class BatteryController extends ApiController {
    final static Logger log = LogManager.getLogger(BatteryController.class);

    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;

    /**
     * 电池控制
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ControlParam {
        public String batteryId;
        public Integer controlType;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/control.htm")
    public RestResult control(@RequestBody ControlParam param) {
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        if(battery.getLockSwitch() != null){
            if(battery.getLockSwitch() == ConstEnum.Flag.TRUE.getValue() && param.controlType == 1){
                return RestResult.result(RespCode.CODE_2.getValue(), "电池已处于锁定状态，如有异常请联系管理员");
            }
            if(battery.getLockSwitch() == ConstEnum.Flag.FALSE.getValue() && param.controlType == 2){
                return RestResult.result(RespCode.CODE_2.getValue(), "电池已处于解锁状态，如有异常请联系管理员");
            }

        }
        return batteryService.control(battery, param.controlType);
    }
}
