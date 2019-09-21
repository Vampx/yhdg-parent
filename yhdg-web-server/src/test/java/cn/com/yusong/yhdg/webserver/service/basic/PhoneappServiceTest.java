package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneappServiceTest extends BaseJunit4Test {
	@Autowired
	PhoneappService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Phoneapp phoneapp = newPhoneapp(partner.getId());
		insertPhoneapp(phoneapp);

		assertNotNull(service.find(phoneapp.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Phoneapp phoneapp = newPhoneapp(partner.getId());
		insertPhoneapp(phoneapp);

		assertTrue(1 == service.findPage(phoneapp).getTotalItems());
		assertTrue(1 == service.findPage(phoneapp).getResult().size());
	}

	@Test
	public void setReferenced() {
		//方法无内容
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Phoneapp phoneapp = newPhoneapp(partner.getId());

		assertTrue(service.insert(phoneapp).isSuccess());
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Phoneapp phoneapp = newPhoneapp(partner.getId());
		insertPhoneapp(phoneapp);

		phoneapp.setAppName("测试的appName");
		assertTrue(service.update(phoneapp).isSuccess());
		assertEquals(service.find(phoneapp.getId()).getAppName(), phoneapp.getAppName());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Phoneapp phoneapp = newPhoneapp(partner.getId());
		insertPhoneapp(phoneapp);

		assertTrue(service.delete(phoneapp.getId()).isSuccess());
		assertNull(service.find(phoneapp.getId()));
	}
}
