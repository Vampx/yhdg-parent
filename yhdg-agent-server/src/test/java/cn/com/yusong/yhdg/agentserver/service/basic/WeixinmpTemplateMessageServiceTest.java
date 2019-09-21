package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinmpTemplateMessageServiceTest extends BaseJunit4Test {
	@Autowired
	WeixinmpTemplateMessageService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Weixinmp weixinmp = newWeixinmp(partner.getId());
		insertWeixinmp(weixinmp);

		WeixinmpTemplateMessage weixinmpTemplateMessage = newWeixinmpTemplateMessage(agent.getId(), weixinmp.getId());
		insertWeixinmpTemplateMessage(weixinmpTemplateMessage);
		assertNotNull(service.find(weixinmpTemplateMessage.getId().intValue()));
	}
	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Weixinmp weixinmp = newWeixinmp(partner.getId());
		insertWeixinmp(weixinmp);

		WeixinmpTemplateMessage weixinmpTemplateMessage = newWeixinmpTemplateMessage(agent.getId(), weixinmp.getId());
		insertWeixinmpTemplateMessage(weixinmpTemplateMessage);
		assertTrue(1 == service.findPage(weixinmpTemplateMessage).getTotalItems());
		assertTrue(1 == service.findPage(weixinmpTemplateMessage).getResult().size());
	}

//	@Test
//	方法没有被使用
//	public void update() {
//		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//		insertAgent(agent);
//		WeixinmpTemplateMessage weixinmpTemplateMessage = newWeixinmpTemplateMessage(agent.getId());
//		insertWeixinmpTemplateMessage(weixinmpTemplateMessage);
//		weixinmpTemplateMessage.setNickname("fd3e");
//		assertTrue(1 == service.update(weixinmpTemplateMessage));
//		assertEquals(weixinmpTemplateMessage.getNickname(), service.find(weixinmpTemplateMessage.getId().intValue()).getNickname());
//	}

}
