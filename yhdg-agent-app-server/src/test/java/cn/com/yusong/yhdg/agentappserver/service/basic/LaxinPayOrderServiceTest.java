package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

public class LaxinPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinPayOrderService laxinPayOrderService;


    @Test
    public void findByStatus() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        insertLaxinRecord(laxinRecord);

        LaxinPayOrder laxinPayOrder = newLaxinPayOrder(agent.getId());
        laxinPayOrder.setStatus(LaxinPayOrder.Status.SUCCESS.getValue());
        insertLaxinPayOrder(laxinPayOrder);

        assertEquals(1, laxinPayOrderService.findByStatus(agent.getId(), LaxinPayOrder.Status.SUCCESS.getValue(), 0, 100).size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        insertLaxinRecord(laxinRecord);

        LaxinPayOrder laxinPayOrder = newLaxinPayOrder(agent.getId());

        laxinPayOrderService.insert(laxinPayOrder, Collections.singletonList(laxinRecord));
    }

    @Test
    public void payByBalance() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        agent.setBalance(100);
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
        laxinRecord.setLaxinMoney(100);
        laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
        insertLaxinRecord(laxinRecord);

        laxinPayOrderService.payByBalance(agent, 100, Collections.singletonList(laxinRecord), "zzzz");

        assertEquals(0, jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }
}
