package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MpPushMessageTemplateDetailServiceTest extends BaseJunit4Test {
	@Autowired
	MpPushMessageTemplateDetailService service;

	@Test
	public void find() {
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		insertMpPushMessageTemplateDetail(mpPushMessageTemplateDetail);

		assertNotNull(service.find(mpPushMessageTemplateDetail.getId(), mpPushMessageTemplateDetail.getWeixinmpId(), mpPushMessageTemplateDetail.getTemplateId()));
	}

	@Test
	public void findByTemplateId() {
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		insertMpPushMessageTemplateDetail(mpPushMessageTemplateDetail);
		assertNotNull(service.findByTemplateId(mpPushMessageTemplateDetail.getWeixinmpId(), mpPushMessageTemplateDetail.getTemplateId()));
	}

	@Test
	public void findPage() {
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		insertMpPushMessageTemplateDetail(mpPushMessageTemplateDetail);
		assertTrue(1 == service.findPage(mpPushMessageTemplateDetail).getTotalItems());
		assertTrue(1 == service.findPage(mpPushMessageTemplateDetail).getResult().size());
	}

	@Test
	public void update() {
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		insertMpPushMessageTemplateDetail(mpPushMessageTemplateDetail);
		mpPushMessageTemplateDetail.setColor("23er");
		assertTrue(service.update(mpPushMessageTemplateDetail).isSuccess());
		assertEquals(service.find(mpPushMessageTemplateDetail.getNewId(), mpPushMessageTemplateDetail.getWeixinmpId(), mpPushMessageTemplateDetail.getTemplateId()).getColor(), mpPushMessageTemplateDetail.getColor());
	}

	@Test
	public void insert() {
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		assertTrue(service.insert(mpPushMessageTemplateDetail).isSuccess());
	}
}
