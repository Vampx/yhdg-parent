package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class WithdrawServiceTest extends BaseJunit4Test {
    @Autowired
    private WithdrawService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Withdraw withdraw = newWithdraw(partner.getId(), partner.getId(), agent.getId(), shop.getId(), customer.getId());
        insertWithdraw(withdraw);

        assertNotNull(service.find(withdraw.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Withdraw withdraw = newWithdraw(partner.getId(), partner.getId(), agent.getId(), shop.getId(), customer.getId());
        insertWithdraw(withdraw);

        assertTrue(1 == service.findPage(withdraw).getTotalItems());
        assertTrue(1 == service.findPage(withdraw).getResult().size());
    }

    @Test
    public void audit() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Withdraw withdraw = newWithdraw(partner.getId(), partner.getId(), agent.getId(), shop.getId(), customer.getId());
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        insertWithdraw(withdraw);

        service.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_OK.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
    }

    @Test
    public void reset() {
        Partner partner = newPartner();
        insertPartner(partner);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        insertPlatformAccount(platformAccount);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Withdraw withdraw = newWithdraw(partner.getId(), partner.getId(), agent.getId(), shop.getId(), customer.getId());
        withdraw.setStatus(Withdraw.Status.WITHDRAW_NO.getValue());
        insertWithdraw(withdraw);

        service.reset(withdraw.getId(), "abc", "123", "456", "aaa", "bbb");

        assertEquals(Withdraw.Status.AUDIT_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_withdraw_transfer_log where withdraw_id = ?", withdraw.getId()));
    }
}