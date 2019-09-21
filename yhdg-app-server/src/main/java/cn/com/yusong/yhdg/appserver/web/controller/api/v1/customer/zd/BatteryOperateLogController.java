package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.BatteryOperateLogService;
import cn.com.yusong.yhdg.appserver.service.zd.RentBatteryForegiftService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import org.apache.commons.lang.time.DateFormatUtils;
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

@Controller("api_v1_customer_zd_battery_operate_log")
@RequestMapping(value = "/api/v1/customer/zd/battery_operate_log")
public class BatteryOperateLogController extends ApiController {
    @Autowired
    BatteryOperateLogService batteryOperateLogService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public RestResult list(@Valid @RequestBody ListParam param) {
        List dataList = new ArrayList();

        List<BatteryOperateLog> batteryOperateLogList = batteryOperateLogService.findList(param.offset, param.limit);

        for(BatteryOperateLog batteryOperateLog : batteryOperateLogList){
            Map line = new HashMap();
           // line.put("volume", batteryOperateLog.getVolume());
            line.put("time", DateFormatUtils.format(batteryOperateLog.getCreateTime(), Constant.DATE_TIME_FORMAT));
            dataList.add(line);
        }
        return  RestResult.dataResult(RespCode.CODE_0.getValue(), null, dataList);
    }
}
