package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.IdCardAuthRecordService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
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

@Controller("agent_api_v1_agent_basic_id_card_auth_record")
@RequestMapping(value = "/agent_api/v1/agent/basic/id_card_auth_record")
public class IdCardAuthRecordController extends ApiController{

    @Autowired
    IdCardAuthRecordService idCardAuthRecordService;
    @Autowired
    AgentService agentService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryByDateParam {
        public String keyword;
        public String date;
        public Integer stats;
        public Integer offset;
        public Integer limit;
    }

    /*
    * 140-按日查询实名认证记录
    * */

    @ResponseBody
    @RequestMapping(value = "query_by_date.htm")
    public RestResult queryByDate(@Valid @RequestBody QueryByDateParam param) {
        TokenCache.Data tokenData = getTokenData();
        Integer agentId = tokenData.agentId;

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        List<IdCardAuthRecord> result = idCardAuthRecordService.queryByDate(agentId,param.keyword, param.date, param.offset, param.limit);

        List<Map> list = new ArrayList<Map>();
        Map map = new HashMap();
        int totalMoney = 0;
        for (IdCardAuthRecord idCardAuthRecord : result) {
            Map line = new HashMap();
            totalMoney += idCardAuthRecord.getMoney();
            line.put("id", idCardAuthRecord.getId());
            line.put("fullname", idCardAuthRecord.getFullname());
            line.put("mobile", idCardAuthRecord.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            line.put("money", idCardAuthRecord.getMoney());
            line.put("createTime", DateFormatUtils.format(idCardAuthRecord.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        map.put("list", list);
        if (param.stats == 1) {
            Map line = new HashMap();
            line.put("totalMoney", totalMoney);
            map.put("stats", line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }
}
