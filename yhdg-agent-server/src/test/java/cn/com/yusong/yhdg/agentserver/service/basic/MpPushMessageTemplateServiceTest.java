package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MpPushMessageTemplateServiceTest extends BaseJunit4Test {
	@Autowired
	MpPushMessageTemplateService service;

	@Test
	public void find() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		assertNotNull(service.find(mpPushMessageTemplate.getWeixinmpId(), mpPushMessageTemplate.getId()));
	}

	@Test
	public void findPage() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		assertTrue(1 == service.findPage(mpPushMessageTemplate).getTotalItems());
		assertTrue(1 == service.findPage(mpPushMessageTemplate).getResult().size());
	}

	@Test
	public void update() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		mpPushMessageTemplate.setMemo("1dr3");
		assertTrue(service.update(mpPushMessageTemplate.getId(), mpPushMessageTemplate.getWeixinmpId(), mpPushMessageTemplate.getTemplateName(), mpPushMessageTemplate.getVariable(), mpPushMessageTemplate.getMpCode(), mpPushMessageTemplate.getIsActive(), mpPushMessageTemplate.getMemo()).isSuccess());
		assertEquals(mpPushMessageTemplate.getMemo(), service.find(mpPushMessageTemplate.getWeixinmpId(), mpPushMessageTemplate.getId()).getMemo());
	}

	@Test
	public void insert() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		assertTrue(service.insert(mpPushMessageTemplate).isSuccess());
	}

	@Test
	public void findByWeixinmpId() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		insertMpPushMessageTemplate(mpPushMessageTemplate);

		assertNotNull(service.findByWeixinmpId(mpPushMessageTemplate.getWeixinmpId()));

	}

}
