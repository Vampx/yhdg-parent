package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;


import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyNoticeMessageService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentNoticeMessageService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyNoticeMessage;
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

@Controller("agent_api_v1_agentcompany_basic_agent_company_qrcode")
@RequestMapping(value = "/agent_api/v1/agentcompany/basic/agent_company_qrcode")
public class AgentCompanyQrcodeController extends ApiController {
    @Autowired
    AgentNoticeMessageService agentNoticeMessageService;
    @Autowired
    AgentCompanyNoticeMessageService agentCompanyNoticeMessageService;
    @Autowired
    AgentCompanyService agentCompanyService;

    @ResponseBody
    @RequestMapping(value = "/get_url")
    public RestResult list() {
        TokenCache.Data tokenData = getTokenData();
        String agentCompanyId = tokenData.agentCompanyId;

        Map line = new HashMap();
        line.put("url", String.format("%s/agent_company/join.htm?v=%s", config.weixinUrl, agentCompanyId));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }



}
