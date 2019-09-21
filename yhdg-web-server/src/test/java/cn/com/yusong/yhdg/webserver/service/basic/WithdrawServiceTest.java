package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WithdrawServiceTest extends BaseJunit4Test {

    @Autowired
    WithdrawService withdrawService;

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

        assertNotNull(withdrawService.find(withdraw.getId()));
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

        assertTrue(1 == withdrawService.findPage(withdraw).getTotalItems());
        assertTrue(1 == withdrawService.findPage(withdraw).getResult().size());
    }

    /**
     * 审核通过
     */
    @Test
    public void audit_1() {
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

        withdrawService.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_OK.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
    }

    /**
     * 审核不通过，客户提现
     */
    @Test
    public void audit_2_1() {
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
        withdraw.setType(Withdraw.Type.CUSTOMER.getValue());
        insertWithdraw(withdraw);

        withdrawService.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_NO.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_NO.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(customer.getBalance() + withdraw.getMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));
    }

    /**
     * 审核不通过，运营商提现
     */
    @Test
    public void audit_2_2() {
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
        withdraw.setType(Withdraw.Type.AGENT.getValue());
        insertWithdraw(withdraw);

        withdrawService.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_NO.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_NO.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(agent.getBalance() + withdraw.getMoney(), jdbcTemplate.queryForInt("select balance from bas_agent where id = ?", agent.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_agent_in_out_money where agent_id = ?", agent.getId()));
    }

    /**
     * 审核不通过，门店提现
     */
    @Test
    public void audit_2_3() {
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
        withdraw.setType(Withdraw.Type.SHOP.getValue());
        insertWithdraw(withdraw);

        withdrawService.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_NO.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_NO.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(shop.getBalance() + withdraw.getMoney(), jdbcTemplate.queryForInt("select balance from hdg_shop where id = ?", shop.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_shop_in_out_money where shop_id = ?", shop.getId()));
    }

    /**
     * 审核不通过，系统商户提现
     */
    @Test
    public void audit_2_4() {
        Partner partner = newPartner();
        insertPartner(partner);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        insertPlatformAccount(platformAccount);

        Withdraw withdraw = newWithdraw(partner.getId(), partner.getId(), null, null, null);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setType(Withdraw.Type.SYSTEM.getValue());
        insertWithdraw(withdraw);

        withdrawService.audit(withdraw.getId(), Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.AUDIT_NO.getValue(), "", "");

        assertEquals(Withdraw.Status.AUDIT_NO.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(platformAccount.getBalance() + withdraw.getMoney(), jdbcTemplate.queryForInt("select balance from bas_platform_account where id = ?", partner.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_platform_account_in_out_money where platform_account_id = ?", partner.getId()));
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

        withdrawService.reset(withdraw.getId(), "abc", "123", "456", "aaa", "aaa");

        assertEquals(Withdraw.Status.AUDIT_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_withdraw_transfer_log where withdraw_id = ?", withdraw.getId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        platformAccount.setBalance(1000);
        insertPlatformAccount(platformAccount);

        withdrawService.insert(Withdraw.AccountType.WEIXIN_MP.getValue(), 100, 10, platformAccount, "aaa");

        assertEquals(platformAccount.getBalance() - 100, jdbcTemplate.queryForInt("select balance from bas_platform_account where id = ?", partner.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_withdraw"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_platform_account_in_out_money"));
    }

    @Test
    public void offline() {
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

        assertTrue(withdrawService.offline(withdraw.getId()).isSuccess());
    }
}