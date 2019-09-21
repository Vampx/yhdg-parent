package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwTemplateMessage;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwTemplateMessageServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwTemplateMessageService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Alipayfw alipayfw = newAlipayfw(partner.getId()); insertAlipayfw(alipayfw);

		AlipayfwTemplateMessage alipayfwTemplateMessage = newAlipayfwTemplateMessage(agent.getId(), alipayfw.getId());
		insertAlipayfwTemplateMessage(alipayfwTemplateMessage);
		assertNotNull(service.find(alipayfwTemplateMessage.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Alipayfw alipayfw = newAlipayfw(partner.getId()); insertAlipayfw(alipayfw);

		AlipayfwTemplateMessage alipayfwTemplateMessage = newAlipayfwTemplateMessage(agent.getId(), alipayfw.getId());
		insertAlipayfwTemplateMessage(alipayfwTemplateMessage);
		assertTrue(1 == service.findPage(alipayfwTemplateMessage).getTotalItems());
		assertTrue(1 == service.findPage(alipayfwTemplateMessage).getResult().size());

	}

}
