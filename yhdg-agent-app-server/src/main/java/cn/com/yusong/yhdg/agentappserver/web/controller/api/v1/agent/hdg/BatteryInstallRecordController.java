package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryInstallRecordService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller("agent_api_v1_agent_hdg_battery_install_record")
@RequestMapping("/agent_api/v1/agent/hdg/battery_install_record")
public class BatteryInstallRecordController extends ApiController{
    @Autowired
    BatteryInstallRecordService batteryInstallRecordService;
    @Autowired
    BatteryService batteryService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOnlineParam {
        public String code;
        public Integer batteryType;
        public Integer category;
    }

    /*
    * 65-电池上线
    * */

    @ResponseBody
    @RequestMapping(value = "/battery_online")
    public RestResult CabinetOnline(@Valid @RequestBody BatteryOnlineParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        List<Battery> batteryList = batteryService.findByCode(param.code);
        if (batteryList.isEmpty()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        if (batteryList.size() > 1) {
            return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池编号:%s 查找到多块电池，请联系管理员",param.code));
        }
        Battery battery = batteryList.get(0);

        if (battery == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }
        if (battery.getUpLineStatus() == BatteryInstallRecord.Status.YESONLINE.getValue()  && agentId != Constant.TEST_AGENT_ID) {
            return RestResult.result(RespCode.CODE_2.getValue(), "电池已上线");
        }
        BatteryInstallRecord record = new BatteryInstallRecord();
        record.setAgentId(agentId);
        record.setBatteryId(battery.getId());
        record.setBatteryType(param.batteryType);
        batteryInstallRecordService.insert(record, param.category);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
