package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinRecordServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinRecordService service;
	@Autowired
	LaxinRecordTransferLogService laxinRecordTransferLogService;

	@Test
	public void find() {

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

		assertNotNull(service.find(laxinRecord.getId()));
	}

	@Test
	public void findPage() {
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

		assertTrue(1 == service.findPage(laxinRecord).getTotalItems());
		assertTrue(1 == service.findPage(laxinRecord).getResult().size());
	}

	@Test
	public void resetAccount() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Laxin laxin = newLaxin(partner.getId(), agent.getId());
		insertLaxin(laxin);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		LaxinRecord laxinRecord = newLaxinRecord(agent.getId(), laxin.getId(), customer.getId());
		laxinRecord.setStatus(LaxinRecord.Status.FAIL.getValue());
		insertLaxinRecord(laxinRecord);

		assertTrue(1 == service.resetAccount(laxinRecord.getId(), "mpOpenId",
				"accountName", "admin"));

		LaxinRecordTransferLog laxinRecordTransferLog = new LaxinRecordTransferLog();
		laxinRecordTransferLog.setRecordId(laxinRecord.getId());
		assertTrue(1 == laxinRecordTransferLogService.findPage(laxinRecordTransferLog).getTotalItems());
		assertTrue(1 == laxinRecordTransferLogService.findPage(laxinRecordTransferLog).getResult().size());

	}
}
