package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;


import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentNoticeMessageService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
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

@Controller("agent_api_v1_agent_basic_agent_notice_message")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent_notice_message")
public class AgentNoticeMessageController extends ApiController {
    @Autowired
    AgentNoticeMessageService agentNoticeMessageService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public Integer type;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list(@Valid @RequestBody ListParam param) {

        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        List<Map> list = new ArrayList<Map>();
        List<AgentNoticeMessage> agentNoticeMessageList = agentNoticeMessageService.findListByCustomerId(agentId, param.type, param.offset, param.limit);
        for (AgentNoticeMessage agentNoticeMessage : agentNoticeMessageList) {
            Map line = new HashMap();
            line.put("id", agentNoticeMessage.getId());
            line.put("type", param.type);
            line.put("title", agentNoticeMessage.getTitle());
            line.put("content", agentNoticeMessage.getContent());
            line.put("createTime", DateFormatUtils.format(agentNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public Integer type;
        public Long id;
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public RestResult info(@Valid @RequestBody InfoParam param) {

        AgentNoticeMessage agentNoticeMessage = agentNoticeMessageService.find(param.type, param.id);
        Map line = new HashMap();
        if(agentNoticeMessage==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "通知消息不存在");
        }
        line.put("title", agentNoticeMessage.getTitle());
        line.put("content", agentNoticeMessage.getContent());
        line.put("createTime", DateFormatUtils.format(agentNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

}
