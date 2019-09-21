package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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

@Controller("api_v1_common_hdg_battery")
@RequestMapping(value = "/api/v1/common/hdg/battery")
public class BatteryController extends ApiController {
    final static Logger log = LogManager.getLogger(BatteryController.class);
    @Autowired
    BatteryService batteryService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordParam {
        public String qrcode;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "record.htm")
    public RestResult record(@Valid @RequestBody RecordParam param) {
        Battery battery = batteryService.findConditional("qrcode", param.qrcode);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        return batteryService.record(battery);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ControlParam {
        public String batteryId;
        public int controlType;
    }

    @ResponseBody
    @RequestMapping(value = "control.htm")
    public RestResult control(@Valid @RequestBody ControlParam param) {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RescueParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "rescue.htm")
    public RestResult rescue(@Valid @RequestBody RescueParam param) {
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        return batteryService.rescue(battery);
    }
}
