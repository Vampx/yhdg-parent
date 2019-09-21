package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.LaxinRecordService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_basic_laxin_record")
@RequestMapping(value = "/agent_api/v1/agent/basic/laxin_record")
public class LaxinRecordController extends ApiController {

    @Autowired
    LaxinRecordService laxinRecordService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
        public String keyword;
        public int stats;
    }

    @ResponseBody
    @RequestMapping("/wait_pay_list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();

        List<Map> lines = new ArrayList<Map>();
        List<LaxinRecord> laxinRecordList = laxinRecordService.findByStatus(tokenData.agentId, LaxinRecord.Status.WAIT.getValue(), param.keyword, param.offset, param.limit);
        for (LaxinRecord e : laxinRecordList) {
            Map line = new HashMap();
            line.put("id", e.getId());
            line.put("laxinMobile", e.getLaxinMobile());
            line.put("money", e.getLaxinMoney());
            line.put("targetFullname", e.getTargetFullname());
            line.put("targetMobile", e.getTargetMobile());
            line.put("foregiftMoney", e.getForegiftMoney());
            line.put("packetPeriodMoney", e.getPacketPeriodMoney());
            line.put("status", e.getStatus());
            lines.add(line);
        }

        int todayPayMoney = 0;
        int todayCount = 0;
        int totalPayMoney = 0;
        int totalCount = 0;

        if (param.stats == 1) {
            Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
            Date endTime = DateUtils.addDays(beginTime, 1);
            todayPayMoney = laxinRecordService.totalMoneyByPayTime(tokenData.agentId, beginTime, endTime);
            todayCount = laxinRecordService.totalCountByCreateTime(tokenData.agentId, beginTime, endTime);
            totalPayMoney = laxinRecordService.totalMoneyByPayTime(tokenData.agentId, null, null);
            totalCount = laxinRecordService.totalCountByCreateTime(tokenData.agentId, null, null);
        }

        Map data = new HashMap();
        data.put("totalPayMoney", totalPayMoney);
        data.put("totalCount", totalCount);
        data.put("todayPayMoney", todayPayMoney);
        data.put("todayCount", todayCount);
        data.put("recordList", lines);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelLaxinRecord {
        public String id;
        public String cancelCanuse;
    }

    @ResponseBody
    @RequestMapping("cancel_laxin_record.htm")
    public RestResult cancelLaxinRecord(@Valid @RequestBody CancelLaxinRecord perm) {
        laxinRecordService.updateCancel(perm.id, perm.cancelCanuse);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
