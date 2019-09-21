package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void findByWeixinmpId() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		mpPushMessageTemplate.setWeixinmpId(1024);
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		assertNotNull(service.findByWeixinmpId(mpPushMessageTemplate.getWeixinmpId()));
	}
//	@Test
//	public void findAll() {
//		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//		insertAgent(agent);
//		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate(agent.getId());
//		insertMpPushMessageTemplate(mpPushMessageTemplate);
//		int count = jdbcTemplate.queryForInt("select count(*) from bas_mp_push_message_template");
//		assertEquals(count, service.findAll().size());
//	}
	@Test
	public void findPage() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		assertTrue(1 == service.findPage(mpPushMessageTemplate).getTotalItems());
		assertTrue(1 == service.findPage(mpPushMessageTemplate).getResult().size());
	}

	@Test
	public void update() throws IOException, ParseException {
		Partner partner = newPartner();
		insertPartner(partner);

		Weixinmp weixinmp = newWeixinmp(partner.getId());
		weixinmp.setAuthType(1);
		insertWeixinmp(weixinmp);

		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		mpPushMessageTemplate.setWeixinmpId(weixinmp.getId());
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		mpPushMessageTemplate.setMemo("qwer1234");
		Map map = new HashMap<String, Object>();
		map.put("id", mpPushMessageTemplate.getId());
		map.put("weixinmpId", mpPushMessageTemplate.getWeixinmpId());
		map.put("variable", mpPushMessageTemplate.getVariable());
		map.put("mpCode", mpPushMessageTemplate.getMpCode());
		map.put("isActive", mpPushMessageTemplate.getIsActive());
		List<Map> detailList = new ArrayList<Map>();
		Map detail = new HashMap<String, Object>();
		MpPushMessageTemplateDetail mpPushMessageTemplateDetail = newMpPushMessageTemplateDetail();
		mpPushMessageTemplateDetail.setWeixinmpId(mpPushMessageTemplateDetail.getWeixinmpId());
		mpPushMessageTemplateDetail.setKeywordValue("asdf");
		mpPushMessageTemplateDetail.setColor("asdf");
		mpPushMessageTemplateDetail.setTemplateId(mpPushMessageTemplate.getId());
		detail.put("weixinmpId", mpPushMessageTemplateDetail.getWeixinmpId());
		detail.put("keywordValue", mpPushMessageTemplateDetail.getKeywordValue());
		detail.put("color", mpPushMessageTemplateDetail.getColor());
		detail.put("templateId", mpPushMessageTemplateDetail.getTemplateId());
		detail.put("detailId", mpPushMessageTemplateDetail.getId());
		detailList.add(detail);
		map.put("detailList", detailList);
		String data = AppUtils.encodeJson(map);
		ExtResult extResult = service.update(data);
		assertTrue(extResult.isSuccess());
	}

	@Test
	public void insert() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		assertTrue(service.insert(mpPushMessageTemplate).isSuccess());
	}

	@Test
	public void findByUserPage() {
		MpPushMessageTemplate mpPushMessageTemplate = newMpPushMessageTemplate();
		mpPushMessageTemplate.setReceiver("运营商");
		insertMpPushMessageTemplate(mpPushMessageTemplate);
		assertNotNull(service.findByUserPage(mpPushMessageTemplate));
	}

}
