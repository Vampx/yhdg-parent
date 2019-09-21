package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerGuideServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerGuideService service;

	@Test
	public void findPage() {
		CustomerGuide entity = newCustomerGuide();
		insertCustomerGuide(entity);
		assertTrue(1 == service.findPage(entity).getTotalItems());
		assertTrue(1 == service.findPage(entity).getResult().size());
	}

	@Test
	public void insert() {
		CustomerGuide entity = newCustomerGuide();
		insertCustomerGuide(entity);
		assertTrue(service.insert(entity).isSuccess());
	}

	@Test
	public void find() {
		CustomerGuide entity = newCustomerGuide();
		insertCustomerGuide(entity);
		assertNotNull(service.find(entity.getId()));
	}

	@Test
	public void update() {
		CustomerGuide entity = newCustomerGuide();
		insertCustomerGuide(entity);
		entity.setName("d343r");
		assertTrue(service.update(entity).isSuccess());
		assertEquals(entity.getName(), service.find(entity.getId()).getName());
	}

	@Test
	public void delete() {
		CustomerGuide entity = newCustomerGuide();
		insertCustomerGuide(entity);
		assertTrue(service.delete(entity.getId()).isSuccess());
		assertNull(service.find(entity.getId()));
	}
}
