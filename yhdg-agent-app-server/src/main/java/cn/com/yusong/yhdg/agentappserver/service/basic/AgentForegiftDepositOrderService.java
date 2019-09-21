package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AgentForegiftDepositOrderService  extends AbstractService {
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;

    public AgentForegiftDepositOrder find(String id) {
        return agentForegiftDepositOrderMapper.find(id);
    }

    public List<AgentForegiftDepositOrder> findList(int agentId, int category, Integer offset, Integer limit) {
        return agentForegiftDepositOrderMapper.findList(agentId, category, offset, limit);
    }

    public int insert(AgentForegiftDepositOrder order) {
        return agentForegiftDepositOrderMapper.insert(order);
    }

    @Transactional(rollbackFor = Throwable.class)
    public String payByBalance(Agent agent, int category, int money, String operator) {
        Date now = new Date();

        if (agentMapper.updateBalance(agent.getId(), -money) == 0) {
            throw new BalanceNotEnoughException();
        }

        //生成充值订单
        AgentForegiftDepositOrder order = new AgentForegiftDepositOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_DEPOSIT_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(category);
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setMoney(money);
        order.setStatus(AgentDepositOrder.Status.HAVE_PAID.getValue());
        order.setPayType(ConstEnum.PayType.BALANCE.getValue());
        order.setOperator(operator);
        order.setHandleTime(new Date());
        order.setCreateTime(new Date());
        agentForegiftDepositOrderMapper.insert(order);

        //运营商流水
        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(-money);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_FOREGIFT_DEPOSIT_PAY.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
        inOutMoney.setBizId(order.getId());
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);

        if(category == ConstEnum.Category.EXCHANGE.getValue()){
            //更新运营商押金余额 预留金额  押金余额比例
            List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                    CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
            //押金余额
            int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
            //运营商押金充值
            int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), category, AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
            //运营商提现
            int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), category, AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

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
                foregiftInOutMoney.setCategory(category);
                foregiftInOutMoney.setMoney(order.getMoney());
                foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                foregiftInOutMoney.setBizId(order.getId());
                foregiftInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                foregiftInOutMoney.setBalance(foregiftBalance);
                foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
                foregiftInOutMoney.setRatio(foregiftBalanceRatio);
                foregiftInOutMoney.setOperator(operator);
                foregiftInOutMoney.setCreateTime(new Date());
                agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
            }
        }else if(category == ConstEnum.Category.RENT.getValue()){
            //更新运营商押金余额 预留金额  押金余额比例
            List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                    CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
            //押金余额
            int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
            //运营商押金充值
            int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), category, AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
            //运营商提现
            int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), category, AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

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
                foregiftInOutMoney.setCategory(category);
                foregiftInOutMoney.setMoney(order.getMoney());
                foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
                foregiftInOutMoney.setBizId(order.getId());
                foregiftInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                foregiftInOutMoney.setBalance(foregiftBalance);
                foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
                foregiftInOutMoney.setRatio(foregiftBalanceRatio);
                foregiftInOutMoney.setOperator(operator);
                foregiftInOutMoney.setCreateTime(new Date());
                agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
            }

        }



        return order.getId();
    }
}
