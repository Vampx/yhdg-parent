package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.LaxinSettingService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
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

@Controller("agent_api_v1_agent_basic_laxin_setting")
@RequestMapping(value = "/agent_api/v1/agent/basic/laxin_setting")
public class LaxinSettingController extends ApiController {

    @Autowired
    AgentService agentService;
    @Autowired
    LaxinSettingService laxinSettingService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        @NotBlank(message = "规则名称不能为空")
        public String settingName;
        public int laxinMoney;
        public int ticketMoney;
        public int ticketDayCount;
        public int packetPeriodMoney;
        public int packetPacketMonth;
        public int isActive;
        public int incomeType;
        public int intervalDay;
        public int type;
    }

    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult create(@Valid @RequestBody CreateParam param) {
        TokenCache.Data tokenData = getTokenData();

        Agent agent = agentService.find(tokenData.agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        LaxinSetting laxinSetting = new LaxinSetting();
        laxinSetting.setAgentId(tokenData.agentId);
        laxinSetting.setSettingName(param.settingName);
        laxinSetting.setLaxinMoney(param.laxinMoney);
        laxinSetting.setTicketMoney(param.ticketMoney);
        laxinSetting.setTicketDayCount(param.ticketDayCount);
        laxinSetting.setPacketPeriodMoney(param.packetPeriodMoney);
        laxinSetting.setPacketPeriodMonth(param.packetPacketMonth);
        laxinSetting.setIsActive(param.isActive);
        laxinSetting.setIncomeType(param.incomeType);
        laxinSetting.setIntervalDay(param.intervalDay);
        laxinSetting.setType(param.type);
        laxinSetting.setCreateTime(new Date());
        return laxinSettingService.create(laxinSetting);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateParam {
        public long id;
        public String settingName;
        public int laxinMoney;
        public int ticketMoney;
        public int ticketDayCount;
        public int packetPeriodMoney;
        public int packetPacketMonth;
        public int isActive;
        public int incomeType;
        public int intervalDay;
        public int type;
    }

    @ResponseBody
    @RequestMapping("/update.htm")
    public RestResult update(@Valid @RequestBody UpdateParam param) {
        LaxinSetting laxinSetting = new LaxinSetting();
        laxinSetting.setId(param.id);
        laxinSetting.setSettingName(param.settingName);
        laxinSetting.setLaxinMoney(param.laxinMoney);
        laxinSetting.setTicketMoney(param.ticketMoney);
        laxinSetting.setTicketDayCount(param.ticketDayCount);
        laxinSetting.setPacketPeriodMoney(param.packetPeriodMoney);
        laxinSetting.setPacketPeriodMonth(param.packetPacketMonth);
        laxinSetting.setIsActive(param.isActive);
        laxinSetting.setIncomeType(param.incomeType);
        laxinSetting.setIntervalDay(param.intervalDay);
        laxinSetting.setType(param.type);
        return laxinSettingService.update(laxinSetting);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String settingName;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        return laxinSettingService.findList(tokenData.agentId, param.settingName, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteParam {
        public long id;
    }

    @ResponseBody
    @RequestMapping("/delete.htm")
    public RestResult delete(@Valid @RequestBody DeleteParam param) {
        return laxinSettingService.delete(param.id);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public long id;
    }

    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult detail(@Valid @RequestBody DetailParam param) {
        LaxinSetting laxinSetting = laxinSettingService.find(param.id);
        if (laxinSetting == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新规则不存在");
        }

        Map data = new HashMap();
        data.put("id", laxinSetting.getId());
        data.put("settingName", laxinSetting.getSettingName());
        data.put("laxinMoney", laxinSetting.getLaxinMoney());
        data.put("ticketMoney", laxinSetting.getTicketMoney());
        data.put("ticketDayCount", laxinSetting.getTicketDayCount());
        data.put("packetPeriodMoney", laxinSetting.getPacketPeriodMoney());
        data.put("packetPacketMonth", laxinSetting.getPacketPeriodMonth());
        data.put("isActive", laxinSetting.getIsActive());
        data.put("incomeType", laxinSetting.getIncomeType());
        data.put("intervalDay", laxinSetting.getIntervalDay());
        data.put("type", laxinSetting.getType());
        data.put("url", String.format("%s/laxin_setting/join.htm?v=%d", config.weixinUrl, laxinSetting.getId()));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
