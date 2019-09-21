package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class AgentForegiftInOutMoneyServiceTest extends BaseJunit4Test {
    @Autowired
    AgentForegiftInOutMoneyService agentForegiftInOutMoneyService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentForegiftInOutMoney agentForegiftInOutMoney = newAgentForegiftInOutMoney(agent.getId());
        agentForegiftInOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertAgentForegiftInOutMoney(agentForegiftInOutMoney);

        AgentForegiftInOutMoney agentForegiftInOutMoney1 = newAgentForegiftInOutMoney(agent.getId());
        agentForegiftInOutMoney1.setCategory(ConstEnum.Category.RENT.getValue());
        insertAgentForegiftInOutMoney(agentForegiftInOutMoney1);

        assertNotNull(agentForegiftInOutMoneyService.findList(agent.getId(), ConstEnum.Category.EXCHANGE.getValue(), null, 0, 1));

        assertNotNull(agentForegiftInOutMoneyService.findList(agent.getId(), ConstEnum.Category.RENT.getValue(), null, 0, 1));

    }


    @Test
    public void agentHandleList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentForegiftDepositOrder agentForegiftDepositOrder = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForegiftDepositOrder.setCategory(ConstEnum.Category.EXCHANGE.getValue());

        insertAgentForegiftDepositOrder(agentForegiftDepositOrder);

        AgentForegiftInOutMoney agentForegiftInOutMoney = newAgentForegiftInOutMoney(agent.getId());
        agentForegiftInOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        agentForegiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
        agentForegiftInOutMoney.setBizId(agentForegiftInOutMoney.getBizId());
        insertAgentForegiftInOutMoney(agentForegiftInOutMoney);


        AgentForegiftDepositOrder agentForegiftDepositOrder1 = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForegiftDepositOrder1.setId("121323213");
        agentForegiftDepositOrder1.setCategory(ConstEnum.Category.RENT.getValue());

        insertAgentForegiftDepositOrder(agentForegiftDepositOrder1);

        AgentForegiftInOutMoney agentForegiftInOutMoney1 = newAgentForegiftInOutMoney(agent.getId());
        agentForegiftInOutMoney1.setCategory(ConstEnum.Category.RENT.getValue());
        agentForegiftInOutMoney1.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
        agentForegiftInOutMoney1.setBizId(agentForegiftInOutMoney1.getBizId());
        insertAgentForegiftInOutMoney(agentForegiftInOutMoney1);

        List<Integer> bizTypeList = Arrays.asList(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue(),
                AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());

        assertNotNull(agentForegiftInOutMoneyService.findList(agent.getId(), ConstEnum.Category.EXCHANGE.getValue(), bizTypeList,  0, 1));
        assertNotNull(agentForegiftInOutMoneyService.findList(agent.getId(), ConstEnum.Category.RENT.getValue(), bizTypeList,  0, 1));

    }

}
