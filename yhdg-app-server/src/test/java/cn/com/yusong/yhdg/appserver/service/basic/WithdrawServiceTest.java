package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WithdrawServiceTest extends BaseJunit4Test {
    @Autowired
    WithdrawService withdrawService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
        insertWithdraw(withdraw);

        assertNotNull(withdrawService.find(withdraw.getId()));
    }

    //测试公众号提现
    @Test
    public void insert_1() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertNotNull(withdrawService.insert(Withdraw.AccountType.WEIXIN_MP.getValue(), "zz", "", 100, 1, customer));
    }

    //测试支付宝提现
    @Test
    public void insert_2() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(1000);
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertNotNull(withdrawService.insert(Withdraw.AccountType.ALIPAY.getValue(), "zz", "", 100, 1, customer));
    }


    @Test
    public void findByCustomer() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
        withdraw.setAccountType(Withdraw.Type.CUSTOMER.getValue());
        insertWithdraw(withdraw);

        assertEquals(1, withdrawService.findByCustomer(Withdraw.Type.CUSTOMER.getValue(), customer.getId(), 0, 100).size());
    }
}
