package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
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

@Controller("agent_api_v1_agent_basic_system_battery")
@RequestMapping(value = "/agent_api/v1/agent/basic/system_battery")
public class SystemBatteryTypeController extends ApiController {

    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String typeName;
    }

    /**
     * 64-查询系统电池列表
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult customerList(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<SystemBatteryType> list = systemBatteryTypeService.findList(param.typeName);

        List<Map> result = new ArrayList<Map>();
        for (SystemBatteryType systemBatteryType : list) {
            Map line = new HashMap();
            line.put("id", systemBatteryType.getId());
            line.put("typeName", systemBatteryType.getTypeName());
            line.put("ratedVoltage", systemBatteryType.getRatedVoltage());
            line.put("ratedCapacity", systemBatteryType.getRatedCapacity());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

}
