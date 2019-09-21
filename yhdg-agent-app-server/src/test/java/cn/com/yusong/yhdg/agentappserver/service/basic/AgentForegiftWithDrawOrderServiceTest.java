package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class AgentForegiftWithDrawOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AgentForegiftWithdrawOrderService agentForegiftWithdrawOrderService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        AgentForegiftWithdrawOrder agentForegiftWithdrawOrder = newAgentForegiftWithdrawOrder(partner.getId(), agent.getId());
        agentForegiftWithdrawOrder.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertAgentForegiftWithdrawOrder(agentForegiftWithdrawOrder);

        AgentForegiftWithdrawOrder agentForegiftWithdrawOrder1 = newAgentForegiftWithdrawOrder(partner.getId(), agent.getId());
        agentForegiftWithdrawOrder1.setId("312312321");
        agentForegiftWithdrawOrder1.setCategory(ConstEnum.Category.RENT.getValue());
        insertAgentForegiftWithdrawOrder(agentForegiftWithdrawOrder1);

        assertNotNull(agentForegiftWithdrawOrderService.findList(agent.getId(), ConstEnum.Category.EXCHANGE.getValue(),0, 1));
        assertNotNull(agentForegiftWithdrawOrderService.findList(agent.getId(), ConstEnum.Category.RENT.getValue(),0, 1));
    }

    @Test
    public void agentHdWithdraw() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        agent.setBalance(0);
        agent.setForegiftBalance(100);
        agent.setForegiftRemainMoney(100);
        agent.setForegiftBalanceRatio(100);
        insertAgent(agent);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setMoney(100);
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        insertCustomerForegiftOrder(customerForegiftOrder);


        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue());
        agentSystemConfig.setConfigValue("20");
        insertAgentSystemConfig(agentSystemConfig);


        agentForegiftWithdrawOrderService.agentHdWithdraw(agent, 80, "zzzz");

        assertEquals(80, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(20, jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(20, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ? and category = ? ", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }

    @Test
    public void agentZdWithdraw() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        agent.setBalance(0);
        agent.setZdForegiftBalance(100);
        agent.setZdForegiftRemainMoney(100);
        agent.setZdForegiftBalanceRatio(100);
        insertAgent(agent);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        rentForegiftOrder.setMoney(100);
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(rentForegiftOrder);


        AgentSystemConfig agentSystemConfig = newAgentSystemConfig(agent.getId());
        agentSystemConfig.setId(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue());
        agentSystemConfig.setConfigValue("20");
        insertAgentSystemConfig(agentSystemConfig);


        agentForegiftWithdrawOrderService.agentZdWithdraw(agent, 80, "zzzz");

        assertEquals(80, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(20, jdbcTemplate.queryForInt("select zd_foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(20, jdbcTemplate.queryForInt("select zd_foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ? and category = ? ", agent.getId(), ConstEnum.Category.RENT.getValue()));


        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }

}
