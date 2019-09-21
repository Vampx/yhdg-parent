package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FwPushMessageTemplateDetailServiceTest extends BaseJunit4Test {
	@Autowired
	FwPushMessageTemplateDetailService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
		insertPushMessageTemplate(pushMessageTemplate);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(pushMessageTemplate.getId(), alipayfw.getId());
		insertFwPushMessageTemplateDetail(fwPushMessageTemplateDetail);

		assertNotNull(service.find(fwPushMessageTemplateDetail.getId(), alipayfw.getId(), pushMessageTemplate.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
		insertPushMessageTemplate(pushMessageTemplate);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(pushMessageTemplate.getId(), alipayfw.getId());
		insertFwPushMessageTemplateDetail(fwPushMessageTemplateDetail);

		assertNotNull(1 == service.findPage(fwPushMessageTemplateDetail).getTotalItems());
	}
	@Test
	public void findByTemplateId() {
		Partner partner = newPartner();
		insertPartner(partner);

		PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
		insertPushMessageTemplate(pushMessageTemplate);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(pushMessageTemplate.getId(), alipayfw.getId());
		insertFwPushMessageTemplateDetail(fwPushMessageTemplateDetail);

		assertNotNull(service.findByTemplateId(fwPushMessageTemplateDetail.getAlipayfwId(),fwPushMessageTemplateDetail.getTemplateId()));
		assertNotNull(service.findByTemplateId(fwPushMessageTemplateDetail.getAlipayfwId(),fwPushMessageTemplateDetail.getTemplateId()).size());
	}
	@Test
	public void update() {
		Partner partner = newPartner();
		insertPartner(partner);

		PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
		insertPushMessageTemplate(pushMessageTemplate);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(pushMessageTemplate.getId(), alipayfw.getId());
		insertFwPushMessageTemplateDetail(fwPushMessageTemplateDetail);

		fwPushMessageTemplateDetail.setNewId("asdf");
		fwPushMessageTemplateDetail.setKeywordName("测试的keywordName");
		assertTrue(service.update(fwPushMessageTemplateDetail).isSuccess());
		assertEquals(service.find(fwPushMessageTemplateDetail.getNewId(), alipayfw.getId(), pushMessageTemplate.getId()).getKeywordName(), fwPushMessageTemplateDetail.getKeywordName());
	}

	@Test
	public void insert() {
		Partner partner = newPartner();
		insertPartner(partner);

		PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
		insertPushMessageTemplate(pushMessageTemplate);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		insertAlipayfw(alipayfw);

		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(pushMessageTemplate.getId(), alipayfw.getId());

		assertTrue(service.insert(fwPushMessageTemplateDetail).isSuccess());
		assertNotNull(service.find(fwPushMessageTemplateDetail.getId(), alipayfw.getId(), pushMessageTemplate.getId()));
	}
}
