package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_agent_basic_agent_battery_type")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent_battery_type")
public class AgentBatteryTypeController extends ApiController {

    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;
    @Autowired
    RentBatteryTypeService rentBatteryTypeService;


    /**
     * 67-查询运营商电池列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult customerList() {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<AgentBatteryType> list = agentBatteryTypeService.findListByAgentId(agentId);
        List<RentBatteryType> rentBatteryTypeList = rentBatteryTypeService.findListByAgentId(agentId);
        List<Map> result = new ArrayList<Map>();
        for (AgentBatteryType agentBatteryType : list) {
            Map line = new HashMap();
            line.put("agentId", agentBatteryType.getAgentId());
            line.put("batteryType", agentBatteryType.getBatteryType());
            line.put("typeName", agentBatteryType.getTypeName());
            line.put("category", Battery.Category.EXCHANGE.getValue());
            result.add(line);
        }
        for (RentBatteryType rentBatteryType : rentBatteryTypeList) {
            Map line = new HashMap();
            line.put("agentId", rentBatteryType.getAgentId());
            line.put("batteryType", rentBatteryType.getBatteryType());
            line.put("typeName", rentBatteryType.getTypeName());
            line.put("category", Battery.Category.RENT.getValue());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

}
