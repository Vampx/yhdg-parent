package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AgentForegiftWithdrawOrderService {
    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;


    public List<AgentForegiftWithdrawOrder> findList(int agentId, int category, Integer offset, Integer limit) {
        return agentForegiftWithdrawOrderMapper.findList(agentId, category, offset, limit);
    }

    @Transactional(rollbackFor = Throwable.class)
    public String agentHdWithdraw(Agent agent, int money, String operator) {
        Date now = new Date();

        //计算出不可提金额
        int ratio = 100;
        String systemRatio =  agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId() );
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        int floorBalance =  agent.getForegiftBalance() * ratio / 100;
        int allowBalane = agent.getForegiftRemainMoney() - floorBalance - money;
        if(allowBalane < 0 ){
            throw new BalanceNotEnoughException();
        }
        agentMapper.updateBalance(agent.getId(), money);


        //生成提现订单
        AgentForegiftWithdrawOrder order = new AgentForegiftWithdrawOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_WITHDRAW_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setAccountType(AgentForegiftWithdrawOrder.AccountType.BALANCE.getValue());
        order.setMoney(money);
        order.setRealMoney(money);
        order.setServiceMoney(0);
        order.setStatus(AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());
        order.setHandleTime(new Date());
        order.setOperator(operator);
        order.setCreateTime(new Date());
        agentForegiftWithdrawOrderMapper.insert(order);

        //运营商流水
        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_FOREGIFT_WITHDRAW.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        inOutMoney.setBizId(order.getId());
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);


        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;

        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateForegift(order.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
            foregiftInOutMoney.setAgentId(order.getAgentId());
            foregiftInOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            foregiftInOutMoney.setMoney(-order.getMoney());
            foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
            foregiftInOutMoney.setBizId(order.getId());
            foregiftInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
            foregiftInOutMoney.setBalance(foregiftBalance);
            foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
            foregiftInOutMoney.setRatio(foregiftBalanceRatio);
            foregiftInOutMoney.setOperator(operator);
            foregiftInOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
        }

        return order.getId();
    }


    @Transactional(rollbackFor = Throwable.class)
    public String agentZdWithdraw(Agent agent, int money, String operator) {
        Date now = new Date();

        //计算出不可提金额
        int ratio = 100;
        String systemRatio =  agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId() );
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        int floorBalance =  agent.getZdForegiftBalance() * ratio / 100;
        int allowBalane = agent.getZdForegiftRemainMoney() - floorBalance - money;
        if(allowBalane < 0 ){
            throw new BalanceNotEnoughException();
        }
        agentMapper.updateBalance(agent.getId(), money);


        //生成提现订单
        AgentForegiftWithdrawOrder order = new AgentForegiftWithdrawOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_WITHDRAW_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(ConstEnum.Category.RENT.getValue());
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setAccountType(AgentForegiftWithdrawOrder.AccountType.BALANCE.getValue());
        order.setMoney(money);
        order.setRealMoney(money);
        order.setServiceMoney(0);
        order.setStatus(AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());
        order.setHandleTime(new Date());
        order.setOperator(operator);
        order.setCreateTime(new Date());
        agentForegiftWithdrawOrderMapper.insert(order);

        //运营商流水
        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_FOREGIFT_WITHDRAW.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        inOutMoney.setBizId(order.getId());
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);


        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;

        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateZdForegift(order.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
            foregiftInOutMoney.setAgentId(order.getAgentId());
            foregiftInOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
            foregiftInOutMoney.setMoney(-order.getMoney());
            foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
            foregiftInOutMoney.setBizId(order.getId());
            foregiftInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
            foregiftInOutMoney.setBalance(foregiftBalance);
            foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
            foregiftInOutMoney.setRatio(foregiftBalanceRatio);
            foregiftInOutMoney.setOperator(operator);
            foregiftInOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
        }

        return order.getId();
    }
}
