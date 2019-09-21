package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;


import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentForegiftDepositOrderService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentForegiftInOutMoneyService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_basic_agent_foregift_in_out_money")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent_foregift_in_out_money")
public class AgentForegiftInOutMoneyController extends ApiController {
    @Autowired
    AgentForegiftInOutMoneyService agentForegiftInOutMoneyService;
    @Autowired
    AgentForegiftDepositOrderService agentForegiftDepositOrderService;

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
        List<AgentForegiftInOutMoney> orderList = agentForegiftInOutMoneyService.findList(agentId, param.category, null, param.offset, param.limit);
        for (AgentForegiftInOutMoney order : orderList) {
            Map line = new HashMap();
            line.put("bizType", order.getBizType());
            line.put("bizId", order.getBizId());
            line.put("type", order.getType());
            line.put("money", order.getMoney());
            line.put("balance", order.getBalance());
            line.put("remainMoney", order.getRemainMoney());
            line.put("ratio", order.getRatio());
            line.put("createTime", DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentHandleListParam {
        public int category;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/agent_handle_list")
    public RestResult agentHandleList(@Valid @RequestBody AgentHandleListParam param) {

        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        List<Map> list = new ArrayList<Map>();
        List<Integer> bizTypeList = Arrays.asList(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue(),
                AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
        List<AgentForegiftInOutMoney> orderList = agentForegiftInOutMoneyService.findList(agentId, param.category,  bizTypeList, param.offset, param.limit);
        for (AgentForegiftInOutMoney order : orderList) {
            Map line = new HashMap();
            line.put("bizType", order.getBizType());
            line.put("bizId", order.getBizId());
            line.put("type", order.getType());

            Integer payType = null;
            if(order.getBizType() == AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue()){
                AgentForegiftDepositOrder agentForegiftDepositOrder = agentForegiftDepositOrderService.find(order.getBizId());
                if(agentForegiftDepositOrder != null){
                    payType = agentForegiftDepositOrder.getPayType();
                }
            }
            line.put("payType", payType);

            line.put("money", order.getMoney());
            line.put("balance", order.getBalance());
            line.put("remainMoney", order.getRemainMoney());
            line.put("ratio", order.getRatio());
            line.put("createTime", DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }
}
