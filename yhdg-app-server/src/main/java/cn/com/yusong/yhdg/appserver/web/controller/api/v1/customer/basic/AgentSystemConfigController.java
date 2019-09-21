package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_customer_basic_agent_system_config")
@RequestMapping(value = "/api/v1/customer/basic/agent_system_config")
public class AgentSystemConfigController extends ApiController{
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public Integer agentId;
    }

    @ResponseBody
    @RequestMapping(value = "/detail")
    public RestResult detail(@Valid @RequestBody DetailParam param) {
        String isRequire =  agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_INSURANCE_REQUIER.getValue(), param.agentId);
        Map date = new HashMap();
        date.put("isRequire", isRequire);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, date);
    }
}
