package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class WithdrawTransferLogServiceTest extends BaseJunit4Test {
    @Autowired
    WithdrawTransferLogService service;
    WithdrawTransferLog withdrawTransferLog;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
        insertWithdraw(withdraw);

        withdrawTransferLog = newWithdrawTransferLog(withdraw.getId());

    }

    @Test
    public void find() {
        insertWithdrawTransferLog(withdrawTransferLog);

        assertNotNull(service.find(withdrawTransferLog.getId()));
    }

    @Test
    public void findPage() {
        insertWithdrawTransferLog(withdrawTransferLog);

        assertTrue(1 == service.findPage(withdrawTransferLog).getTotalItems());
        assertTrue(1 == service.findPage(withdrawTransferLog).getResult().size());
    }

    @Test
    public void insert() {
        assertTrue(service.insert(withdrawTransferLog).isSuccess());
        assertNotNull(service.find(withdrawTransferLog.getId()));
    }
}