package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agent_basic_laxin_pay_order")
@RequestMapping(value = "/agent_api/v1/agent/basic/laxin_pay_order")
public class LaxinPayOrderController extends ApiController {

    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
    @Autowired
    LaxinRecordService laxinRecordService;
    @Autowired
    LaxinPayOrderService laxinPayOrderService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AgentService agentService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public String id;
    }

    @ResponseBody
    @RequestMapping(value = "/detail")
    public RestResult detail(@Valid @RequestBody DetailParam param) {
        TokenCache.Data tokenData = getTokenData();

        LaxinPayOrder order = laxinPayOrderService.find(param.id);
        if (order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if (order.getAgentId() != tokenData.agentId) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户没有权限");
        }

        Map data = new HashMap();
        data.put("totalMoney", order.getMoney());
        data.put("recordCount", order.getRecordCount());

        List<Map> recordList = new ArrayList<Map>();
        data.put("recordList", recordList);

        List<LaxinRecord> laxinRecordList = laxinRecordService.findByOrderId(param.id);
        for (LaxinRecord record : laxinRecordList) {
            NotNullMap line = new NotNullMap();
            line.put("id", record.getId());
            line.put("laxinMobile", record.getLaxinMobile());
            line.put("money", record.getLaxinMoney());
            line.put("targetFullname", record.getTargetFullname());
            line.put("targetMobile", record.getTargetMobile());
            line.put("foregiftMoney", record.getForegiftMoney());
            line.put("packetPeriodMoney", record.getPacketPeriodMoney());
            line.put("status", record.getStatus());
            recordList.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayedListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/payed_list")
    public RestResult detail(@Valid @RequestBody PayedListParam param) {
        TokenCache.Data tokenData = getTokenData();
        List<LaxinPayOrder> orderList = laxinPayOrderService.findByStatus(tokenData.agentId, LaxinPayOrder.Status.SUCCESS.getValue(), param.offset, param.limit);

        List<Map> data = new ArrayList<Map>();
        for (LaxinPayOrder order : orderList) {
            NotNullMap line = new NotNullMap();
            line.put("id", order.getId());
            line.put("recordCount", order.getRecordCount());
            line.put("money", order.getMoney());
            line.putDateTime("payTime", order.getPayTime());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByBalanceParam {
        public String[] id;
    }

    @ResponseBody
    @RequestMapping(value = "/create_by_balance")
    public RestResult createByBalance(@Valid @RequestBody CreateByBalanceParam param) {
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

        List<LaxinRecord> recordList = new ArrayList<LaxinRecord>();
        int totalMoney = 0;
        for (String e : param.id) {
            LaxinRecord laxinRecord = laxinRecordService.find(e);
            if (laxinRecord != null && laxinRecord.getStatus() == LaxinRecord.Status.WAIT.getValue()) {
                totalMoney += laxinRecord.getLaxinMoney();
                recordList.add(laxinRecord);
            }
        }

        Map result = new HashMap();

        if (agent.getBalance() < totalMoney) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        try {
            String orderId = laxinPayOrderService.payByBalance(agent, totalMoney, recordList, user.getFullname());
            result.put("orderId", orderId);
        } catch (BalanceNotEnoughException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }catch (OrderStatusExpireException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新记录状态过期");
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinMpParam {
        public String[] id;
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

        List<LaxinRecord> recordList = new ArrayList<LaxinRecord>();
        int totalMoney = 0;
        for (String e : param.id) {
            LaxinRecord laxinRecord = laxinRecordService.find(e);
            if (laxinRecord != null && laxinRecord.getStatus() == LaxinRecord.Status.WAIT.getValue()) {
                totalMoney += laxinRecord.getLaxinMoney();
                recordList.add(laxinRecord);
            }
        }

        LaxinPayOrder laxinPayOrder = new LaxinPayOrder();
        laxinPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.LAXIN_PAY_ORDER));
        laxinPayOrder.setAgentId(agent.getId());
        laxinPayOrder.setAgentName(agent.getAgentName());
        laxinPayOrder.setAgentCode(agent.getAgentCode());
        laxinPayOrder.setMoney(totalMoney);
        laxinPayOrder.setRecordCount(recordList.size());
        laxinPayOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        laxinPayOrder.setStatus(LaxinPayOrder.Status.WAIT.getValue());
        laxinPayOrder.setCreateTime(new Date());
        laxinPayOrderService.insert(laxinPayOrder, recordList);


        //这里需要把钱负到这里换电对应的公众号
        Map root = laxinPayOrderService.payByWeixinMp(agent.getPartnerId(), user.getAgentId(), laxinPayOrder.getId(), totalMoney, customer.getId(), customer.getMpOpenId(), PayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue(), "拉新订单支付", null);
        if (root == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "统一下单失败");
        } else {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root.get("data"));
        }
    }

    public static class CreateByAlipayfwParam{
        public String[] id;
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

        List<LaxinRecord> recordList = new ArrayList<LaxinRecord>();
        int totalMoney = 0;
        for (String e : param.id) {
            LaxinRecord laxinRecord = laxinRecordService.find(e);
            if (laxinRecord != null && laxinRecord.getStatus() == LaxinRecord.Status.WAIT.getValue()) {
                totalMoney += laxinRecord.getLaxinMoney();
                recordList.add(laxinRecord);
            }
        }

        LaxinPayOrder laxinPayOrder = new LaxinPayOrder();
        laxinPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.LAXIN_PAY_ORDER));
        laxinPayOrder.setAgentId(agent.getId());
        laxinPayOrder.setAgentName(agent.getAgentName());
        laxinPayOrder.setAgentCode(agent.getAgentCode());
        laxinPayOrder.setMoney(totalMoney);
        laxinPayOrder.setRecordCount(recordList.size());
        laxinPayOrder.setPayType(ConstEnum.PayType.ALIPAY_FW.getValue());
        laxinPayOrder.setStatus(LaxinPayOrder.Status.WAIT.getValue());
        laxinPayOrder.setCreateTime(new Date());
        laxinPayOrderService.insert(laxinPayOrder, recordList);

        try {
            Map root = laxinPayOrderService.payByAlipayfw(agent.getPartnerId(), user.getAgentId(), laxinPayOrder.getId(), totalMoney, customer.getId(), PayOrder.SourceType.LAXIN_PAY_ORDER_PAY.getValue(), "拉新订单支付", "拉新订单支付", null);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, root.get("tradeNo"));
        } catch (Exception e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "下单失败");
        }

    }

}
