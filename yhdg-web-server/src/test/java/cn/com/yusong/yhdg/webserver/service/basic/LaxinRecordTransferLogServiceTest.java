package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinRecordTransferLogServiceTest extends BaseJunit4Test {
	@Autowired
	LaxinRecordTransferLogService service;

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

		LaxinRecordTransferLog laxinRecordTransferLog = newLaxinRecordTransferLog(laxinRecord.getId());
		insertLaxinRecordTransferLog(laxinRecordTransferLog);

		assertNotNull(service.find(laxinRecordTransferLog.getId()));
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

		LaxinRecordTransferLog laxinRecordTransferLog = newLaxinRecordTransferLog(laxinRecord.getId());
		insertLaxinRecordTransferLog(laxinRecordTransferLog);

		assertTrue(1 == service.findPage(laxinRecordTransferLog).getTotalItems());
		assertTrue(1 == service.findPage(laxinRecordTransferLog).getResult().size());
	}
}
