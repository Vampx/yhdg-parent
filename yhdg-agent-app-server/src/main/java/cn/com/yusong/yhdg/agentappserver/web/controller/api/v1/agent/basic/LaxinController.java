package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.LaxinService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller("agent_api_v1_agent_basic_laxin")
@RequestMapping(value = "/agent_api/v1/agent/basic/laxin")
public class LaxinController extends ApiController {

    @Autowired
    AgentService agentService;
    @Autowired
    LaxinService laxinService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        @NotBlank(message = "手机号不能为空")
        public String mobile;
        public String password;
        public int laxinMoney;
        public int ticketMoney;
        public int ticketDayCount;
        public int packetPeriodMoney;
        public int packetPacketMonth;
        public int isActive;
        public int incomeType;
        public int intervalDay;
    }

    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult create(@Valid @RequestBody CreateParam param) {
        TokenCache.Data tokenData = getTokenData();

        Agent agent = agentService.find(tokenData.agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Laxin laxin = new Laxin();
        laxin.setPartnerId(agent.getPartnerId());
        laxin.setAgentId(tokenData.agentId);
        laxin.setMobile(param.mobile);
        laxin.setPassword(param.password);
        laxin.setLaxinMoney(param.laxinMoney);
        laxin.setTicketMoney(param.ticketMoney);
        laxin.setTicketDayCount(param.ticketDayCount);
        laxin.setPacketPeriodMoney(param.packetPeriodMoney);
        laxin.setPacketPeriodMonth(param.packetPacketMonth);
        laxin.setIsActive(param.isActive);
        laxin.setIncomeType(param.incomeType);
        laxin.setIntervalDay(param.intervalDay);
        laxin.setCreateTime(new Date());
        return laxinService.create(laxin);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateParam {
        public long id;
        public String password;
        public int laxinMoney;
        public int ticketMoney;
        public int ticketDayCount;
        public int packetPeriodMoney;
        public int packetPacketMonth;
        public int isActive;
        public int incomeType;
        public int intervalDay;
    }

    @ResponseBody
    @RequestMapping("/update.htm")
    public RestResult update(@Valid @RequestBody UpdateParam param) {
        Laxin laxin = new Laxin();
        laxin.setId(param.id);
        laxin.setPassword(param.password);
        laxin.setLaxinMoney(param.laxinMoney);
        laxin.setTicketMoney(param.ticketMoney);
        laxin.setTicketDayCount(param.ticketDayCount);
        laxin.setPacketPeriodMoney(param.packetPeriodMoney);
        laxin.setPacketPeriodMonth(param.packetPacketMonth);
        laxin.setIsActive(param.isActive);
        laxin.setIncomeType(param.incomeType);
        laxin.setIntervalDay(param.intervalDay);
        return laxinService.update(laxin);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        return laxinService.findList(tokenData.agentId, param.mobile, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteParam {
        public long id;
    }

    @ResponseBody
    @RequestMapping("/delete.htm")
    public RestResult delete(@Valid @RequestBody DeleteParam param) {
        return laxinService.delete(param.id);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public long id;
    }

    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult detail(@Valid @RequestBody DetailParam param) {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(param.id);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新人员不存在");
        }

        Map data = new HashMap();
        data.put("id", laxin.getId());
        data.put("mobile", laxin.getMobile());
        data.put("laxinMoney", laxin.getLaxinMoney());
        data.put("ticketMoney", laxin.getTicketMoney());
        data.put("ticketDayCount", laxin.getTicketDayCount());
        data.put("packetPeriodMoney", laxin.getPacketPeriodMoney());
        data.put("packetPacketMonth", laxin.getPacketPeriodMonth());
        data.put("isActive", laxin.getIsActive());
        data.put("incomeType", laxin.getIncomeType());
        data.put("intervalDay", laxin.getIntervalDay());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
