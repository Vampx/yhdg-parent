package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;


import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftWithdrawOrder;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.apache.commons.lang.StringUtils;
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

@Controller("agent_api_v1_agent_basic_agent_foregift_withdraw_order")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent_foregift_withdraw_order")
public class AgentForegiftWithdrawOrderController extends ApiController {
    @Autowired
    AgentForegiftWithdrawOrderService agentForegiftWithdrawOrderService;
    @Autowired
    AgentService agentService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SystemConfigService systemConfigService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int category;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list(@Valid @RequestBody ListParam param) {

        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        List<Map> list = new ArrayList<Map>();
        List<AgentForegiftWithdrawOrder> orderList = agentForegiftWithdrawOrderService.findList(agentId, param.category, param.offset, param.limit);
        for (AgentForegiftWithdrawOrder order : orderList) {
            Map line = new HashMap();
            line.put("id", order.getId());
            line.put("money", order.getMoney());
            if (order.getHandleTime() != null) {
                line.put("handleTime", DateFormatUtils.format(order.getHandleTime(), Constant.DATE_TIME_FORMAT) );
            }else{
                line.put("handleTime", null);
            }
            line.put("accountType", order.getAccountType());
            line.put("createTime", DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentWithdrawParam {
        public int category;
        public int money;
    }

    @ResponseBody
    @RequestMapping(value = "/agent_withdraw")
    public RestResult agentWithdraw(@Valid @RequestBody AgentWithdrawParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "AgentId错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        User user = userService.find(getTokenData().userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Map result = new HashMap();
        try {
            if(param.category == ConstEnum.Category.EXCHANGE.getValue()){
                String orderId = agentForegiftWithdrawOrderService.agentHdWithdraw(agent, param.money, user.getFullname());
                result.put("orderId", orderId);
            }else if(param.category == ConstEnum.Category.RENT.getValue()){
                String orderId = agentForegiftWithdrawOrderService.agentZdWithdraw(agent, param.money, user.getFullname());
                result.put("orderId", orderId);
            }
        } catch (BalanceNotEnoughException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "押金池余额不足");
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }
}
