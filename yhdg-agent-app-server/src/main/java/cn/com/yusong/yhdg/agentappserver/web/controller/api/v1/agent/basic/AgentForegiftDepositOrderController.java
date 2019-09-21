package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;


import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_basic_agent_foregift_deposit_order")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent_foregift_deposit_order")
public class AgentForegiftDepositOrderController extends ApiController {
    @Autowired
    AgentForegiftDepositOrderService agentForegiftDepositOrderService;
    @Autowired
    AgentService agentService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderIdService orderIdService;

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
        List<AgentForegiftDepositOrder> orderList = agentForegiftDepositOrderService.findList(agentId, param.category, param.offset, param.limit);
        for (AgentForegiftDepositOrder order : orderList) {
            Map line = new HashMap();
            line.put("id", order.getId());
            line.put("money", order.getMoney());
            if (order.getHandleTime() != null) {
                line.put("handleTime", DateFormatUtils.format(order.getHandleTime(), Constant.DATE_TIME_FORMAT) );
            }else{
                line.put("handleTime", null);
            }
            line.put("payType", order.getPayType());
            line.put("operator", order.getOperator());
            line.put("createTime", DateFormatUtils.format(order.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByBalanceParam {
        public int category;
        public int money;
    }

    @ResponseBody
    @RequestMapping(value = "/create_by_balance")
    public RestResult createByBalance(@Valid @RequestBody CreateByBalanceParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "AgentId错误");
        }
        User user = userService.find(getTokenData().userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Map result = new HashMap();

        if (agent.getBalance() < param.money) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        try {
            String orderId = agentForegiftDepositOrderService.payByBalance(agent, param.category, param.money, user.getFullname());
            result.put("orderId", orderId);
        } catch (BalanceNotEnoughException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinMpParam {
        public int category;
        public int money;
    }

    @ResponseBody
    @RequestMapping(value = "/create_by_weixin_mp")
    public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinMpParam param) {
        TokenCache.Data tokenData = getTokenData();

        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (StringUtils.isEmpty(user.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户手机号是空");
        }
        Agent agent = agentService.find(user.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Customer customer = customerService.findByMobile(agent.getPartnerId(), user.getMobile());
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户对应的客户手机号未注册");
        }
        if (StringUtils.isEmpty(customer.getMpOpenId())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号对应客户未绑定公众号");
        }

        //生成充值订单
        AgentForegiftDepositOrder order = new AgentForegiftDepositOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_DEPOSIT_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(param.category);
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setMoney(param.money);
        order.setStatus(AgentDepositOrder.Status.NOT_PAID.getValue());
        order.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        order.setOperator(user.getFullname());
        order.setCreateTime(new Date());
        agentForegiftDepositOrderService.insert(order);

        //这里需要把钱负到这里换电对应的公众号
        Map root = agentForegiftDepositOrderService.payByWeixinMp(agent.getPartnerId(), user.getAgentId(), order.getId(), order.getMoney(), customer.getId(), customer.getMpOpenId(), PayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue(), "运营商押金充值订单支付", null);
        if (root == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
        } else {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root.get("data"));
        }
    }

    public static class CreateByAlipayfwParam{
        public int category;
        public int money;
    }

    @ResponseBody
    @RequestMapping(value = "/create_by_alipay_fw")
    public RestResult createAlipayFw(@Valid @RequestBody CreateByAlipayfwParam param) {
        TokenCache.Data tokenData = getTokenData();

        User user = userService.find(tokenData.userId);
        if (user == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        if (StringUtils.isEmpty(user.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户手机号是空");
        }

        Agent agent = agentService.find(user.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Customer customer = customerService.findByMobile(agent.getPartnerId(), user.getMobile());
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户对应的客户手机号未注册");
        }
        if (StringUtils.isEmpty(customer.getFwOpenId())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号对应客户未绑定生活号");
        }

        //生成充值订单
        AgentForegiftDepositOrder order = new AgentForegiftDepositOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_DEPOSIT_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(param.category);
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setMoney(param.money);
        order.setStatus(AgentDepositOrder.Status.NOT_PAID.getValue());
        order.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        order.setOperator(user.getFullname());
        order.setCreateTime(new Date());
        agentForegiftDepositOrderService.insert(order);

        try {
            Map root = agentForegiftDepositOrderService.payByAlipayfw(agent.getPartnerId(), user.getAgentId(), order.getId(), order.getMoney(), customer.getId(), PayOrder.SourceType.FOREGIFT_DEPOSIT_ORDER_AGENT_PAY.getValue(), "运营商押金充值订单支付", "运营商押金充值订单支付", null);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root.get("tradeNo"));
        } catch (Exception e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "下单失败");
        }

    }
}
