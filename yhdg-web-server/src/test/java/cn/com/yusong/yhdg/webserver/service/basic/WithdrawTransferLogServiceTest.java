package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WithdrawTransferLogServiceTest extends BaseJunit4Test {
	@Autowired
	WithdrawTransferLogService service;

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

		WithdrawTransferLog withdrawTransferLog = newWithdrawTransferLog(withdraw.getId());
		insertWithdrawTransferLog(withdrawTransferLog);

		assertNotNull(service.find(withdrawTransferLog.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
		insertWithdraw(withdraw);

		WithdrawTransferLog withdrawTransferLog = newWithdrawTransferLog(withdraw.getId());
		insertWithdrawTransferLog(withdrawTransferLog);

		assertTrue(1 == service.findPage(withdrawTransferLog).getTotalItems());
		assertTrue(1 == service.findPage(withdrawTransferLog).getResult().size());
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
		insertWithdraw(withdraw);

		WithdrawTransferLog withdrawTransferLog = newWithdrawTransferLog(withdraw.getId());

		assertTrue(service.insert(withdrawTransferLog).isSuccess());
		assertNotNull(service.find(withdrawTransferLog.getId()));
	}
}
