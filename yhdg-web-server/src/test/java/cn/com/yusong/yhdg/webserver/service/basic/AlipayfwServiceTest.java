package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AlipayfwServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		assertNotNull(service.find(alipayfw.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		assertTrue(1 == service.findPage(alipayfw).getTotalItems());
		assertTrue(1 == service.findPage(alipayfw).getResult().size());
	}

	@Test
	public void findList() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		assertTrue(1 == service.findList(alipayfw).size());
	}

	@Test
	public void setReferenced() {
		//方法无内容
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		String sql = "insert into bas_fw_push_message_template_detail (alipayfw_id, id, template_id, keyword_name, keyword_value, color, order_num) values (0/*alipayfw_id*/,'keyword3', '1', '赠送金额', '${gift}', '#000000', '4');";
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(sql);

		assertTrue(service.insert(alipayfw, sqlList).isSuccess());
		assertNotNull(service.find(alipayfw.getId()));
	}

	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		alipayfw.setAppName("测试的appName");
		assertTrue(service.update(alipayfw).isSuccess());
		assertEquals(alipayfw.getAppName(), service.find(alipayfw.getId()).getAppName());
	}

	@Test
	public void delete() {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		assertTrue(service.delete(alipayfw.getId()).isSuccess());
		assertNull(service.find(alipayfw.getId()));
	}
}
