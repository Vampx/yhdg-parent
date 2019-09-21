package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collections;

public class AgentForegiftDepositOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AgentForegiftDepositOrderService agentForegiftDepositOrderService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentForegiftDepositOrder agentForegiftDepositOrder = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForegiftDepositOrder.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertAgentForegiftDepositOrder(agentForegiftDepositOrder);

        AgentForegiftDepositOrder agentForegiftDepositOrder1 = newAgentForegiftDepositOrder(partner.getId(), agent.getId());
        agentForegiftDepositOrder1.setId("12121");
        agentForegiftDepositOrder1.setCategory(ConstEnum.Category.RENT.getValue());
        insertAgentForegiftDepositOrder(agentForegiftDepositOrder1);

        assertNotNull(agentForegiftDepositOrderService.findList(agent.getId(), ConstEnum.Category.EXCHANGE.getValue(),0, 1));
        assertNotNull(agentForegiftDepositOrderService.findList(agent.getId(), ConstEnum.Category.RENT.getValue(),0, 1));
    }

    @Test
    public void payByBalance_exchange() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        agent.setBalance(100);
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        agentForegiftDepositOrderService.payByBalance(agent, ConstEnum.Category.EXCHANGE.getValue(), 100, "zzzz");

        assertEquals(0, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }

    @Test
    public void payByBalance_rent() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        agent.setBalance(100);
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        agentForegiftDepositOrderService.payByBalance(agent, ConstEnum.Category.RENT.getValue(), 100, "zzzz");

        assertEquals(0, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select zd_foregift_balance from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_remain_money from bas_agent where id = ?", agent.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select zd_foregift_balance_ratio from bas_agent where id = ?", agent.getId()));

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_foregift_in_out_money where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }
}
