package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
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
	public void update() throws IOException, ParseException {
		Partner partner = newPartner();
		insertPartner(partner);

		Alipayfw alipayfw = newAlipayfw(partner.getId());
		alipayfw.setAuthType(1);
		insertAlipayfw(alipayfw);

		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		fwPushMessageTemplate.setAlipayfwId(alipayfw.getId());
		insertFwPushMessageTemplate(fwPushMessageTemplate);
		fwPushMessageTemplate.setMemo("qwer1234");
		Map map = new HashMap<String, Object>();
		map.put("id", fwPushMessageTemplate.getId());
		map.put("alipayfwId", fwPushMessageTemplate.getAlipayfwId());
		map.put("variable", fwPushMessageTemplate.getVariable());
		map.put("fwCode", fwPushMessageTemplate.getFwCode());
		map.put("isActive", fwPushMessageTemplate.getIsActive());
		List<Map> detailList = new ArrayList<Map>();
		Map detail = new HashMap<String, Object>();
		FwPushMessageTemplateDetail fwPushMessageTemplateDetail = newFwPushMessageTemplateDetail(fwPushMessageTemplate.getId(), fwPushMessageTemplate.getAlipayfwId());
		fwPushMessageTemplateDetail.setAlipayfwId(fwPushMessageTemplate.getAlipayfwId());
		fwPushMessageTemplateDetail.setKeywordValue("asdf");
		fwPushMessageTemplateDetail.setColor("asdf");
		fwPushMessageTemplateDetail.setTemplateId(fwPushMessageTemplate.getId());
		detail.put("alipayfwId", fwPushMessageTemplateDetail.getAlipayfwId());
		detail.put("keywordValue", fwPushMessageTemplateDetail.getKeywordValue());
		detail.put("color", fwPushMessageTemplateDetail.getColor());
		detail.put("templateId", fwPushMessageTemplateDetail.getTemplateId());
		detail.put("detailId", fwPushMessageTemplateDetail.getId());
		detailList.add(detail);
		map.put("detailList", detailList);
		String data = AppUtils.encodeJson(map);
		ExtResult extResult = service.update(data);
		assertTrue(extResult.isSuccess());
	}

	@Test
	public void insert() {
		FwPushMessageTemplate fwPushMessageTemplate = newFwPushMessageTemplate();
		assertTrue(service.insert(fwPushMessageTemplate).isSuccess());
	}
}
