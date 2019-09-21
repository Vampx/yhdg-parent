package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FwPushMessageTemplateServiceTest extends BaseJunit4Test {
	@Autowired
	FwPushMessageTemplateService service;

	@Test
	public void find() {
		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		insertFwPushMessageTemplate(fwPushMessageTemplate);
		assertNotNull(service.find(fwPushMessageTemplate.getAlipayfwId(), fwPushMessageTemplate.getId()));
	}

	@Test
	public void findPage() {
		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		insertFwPushMessageTemplate(fwPushMessageTemplate);
		assertTrue(1 == service.findPage(fwPushMessageTemplate).getTotalItems());
		assertTrue(1 == service.findPage(fwPushMessageTemplate).getResult().size());
	}

	@Test
	public void update() {
		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		insertFwPushMessageTemplate(fwPushMessageTemplate);
		fwPushMessageTemplate.setMemo("qwer1234");
		assertTrue(service.update(fwPushMessageTemplate.getId(), fwPushMessageTemplate.getAlipayfwId(), fwPushMessageTemplate.getTemplateName(), fwPushMessageTemplate.getVariable(), fwPushMessageTemplate.getFwCode(), fwPushMessageTemplate.getIsActive(), fwPushMessageTemplate.getMemo()).isSuccess());
		assertEquals(service.find(fwPushMessageTemplate.getAlipayfwId(), fwPushMessageTemplate.getId()).getMemo(), fwPushMessageTemplate.getMemo());
	}

	@Test
	public void insert() {
		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		assertTrue(service.insert(fwPushMessageTemplate).isSuccess());
	}
}
