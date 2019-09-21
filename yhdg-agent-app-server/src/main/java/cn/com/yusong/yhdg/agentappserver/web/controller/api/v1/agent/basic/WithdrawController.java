package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.AgentMaterialDayStatsService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.apache.commons.lang.StringUtils;
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

@Controller("agent_api_v1_agent_basic_withdraw")
@RequestMapping(value = "/agent_api/v1/agent/basic/withdraw")
public class WithdrawController extends ApiController {
    @Autowired
    WithdrawService withdrawService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentService agentService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    AgentMaterialDayStatsService agentMaterialDayStatsService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public Integer accountType;
        public String payPassword;
        public Integer money;
        public String alipayAccount;
    }

    /**
     * 109-运营商提现提交
     * */
    @ResponseBody
    @RequestMapping(value = "/agent_withdraw.htm")
    public RestResult agentWithdraw(@Valid @RequestBody CreateParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if (StringUtils.isEmpty(agent.getPayPeopleMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "收款人不存在");
        }

        if (!param.payPassword.equals(agent.getPayPassword())) {
            return RestResult.result(RespCode.CODE_9.getValue(), "密码错误");
        }

        if (agent.getBalance() < param.money) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
        double ratio = 0;
        String withdrawRatio = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue());
        if (StringUtils.isNotEmpty(withdrawRatio)) {
            ratio = Double.parseDouble(withdrawRatio);
        }
        int serviceMoney = (int) Math.round(param.money * ratio);
        if (param.money - serviceMoney < 100) {
            return RestResult.result(RespCode.CODE_2.getValue(), "扣除手续费后金额不能小于1元");
        }

        //押金池判断
        String systemRatio = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId() );
        if(StringUtils.isNotEmpty(systemRatio)){
           int  foregiftRatio = Integer.parseInt(systemRatio);
           if((agent.getForegiftBalanceRatio() !=null && agent.getForegiftBalanceRatio() != 0 && agent.getForegiftBalanceRatio() < foregiftRatio)
                   || (agent.getZdForegiftBalanceRatio() != null && agent.getZdForegiftBalanceRatio() != 0 && agent.getZdForegiftBalanceRatio() < foregiftRatio)){
               return RestResult.result(RespCode.CODE_2.getValue(), "押金池剩余金额过低，无法提现。请先进行押金池充值");
           }
        }

        // 运营商支出判断
        /*int count = agentMaterialDayStatsService.findCountByStatus(agentId, AgentMaterialDayStats.Status.NOT_PAY.getValue());
        if (count > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "存在未支付的运营商支出，无法提现。请先进行支付");
        }*/
        try {
            String id = withdrawService.agentWithdraw(param.accountType, param.alipayAccount, param.money, serviceMoney, agent);
            Map data = new HashMap();
            data.put("id", id);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        } catch (BalanceNotEnoughException exception) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    /**
     * 110-查询账单
     * */
    @ResponseBody
    @RequestMapping(value = "/agent_withdraw_list.htm")
    public RestResult agentWithdrawList(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        List<Map> data = new ArrayList<Map>();
        List<Withdraw> withdrawList = withdrawService.findByAgent(Withdraw.Type.AGENT.getValue(), tokenData.agentId, param.offset, param.limit);
        for (Withdraw withdraw : withdrawList) {
            NotNullMap line = new NotNullMap();
            line.put("id", withdraw.getId());
            line.put("accountType", withdraw.getAccountType());
            line.put("money", withdraw.getMoney());
            line.put("realMoney", withdraw.getRealMoney());
            line.put("serviceMoney", withdraw.getServiceMoney());
            line.put("status", withdraw.getStatus());
            line.putDateTime("createTime", withdraw.getCreateTime());
            line.putDateTime("auditTime", withdraw.getAuditTime());
            line.putDateTime("handleTime", withdraw.getHandleTime());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public String id;
    }


    /**
     * 111-查询运营商账单详情
     * */
    @ResponseBody
    @RequestMapping(value = "/agent_withdraw_info.htm")
    public RestResult agentWithdrawInfo(@Valid @RequestBody InfoParam param) {
        Withdraw withdraw = withdrawService.find(param.id);
        NotNullMap map = new NotNullMap();
        if (withdraw != null) {
            map.put("money", withdraw.getMoney());
            map.put("accountType", withdraw.getAccountType());
            map.put("serviceMoney", withdraw.getServiceMoney());
            map.put("id", withdraw.getId());
            map.put("operator", withdraw.getAuditUser());
            map.putDateTime("handleTime", withdraw.getHandleTime());
            map.put("operatorMemo", withdraw.getAuditMemo());
            map.put("status", withdraw.getStatus());
            map.putDateTime("createTime", withdraw.getCreateTime());
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

}
