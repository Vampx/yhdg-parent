package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessageDetail;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchWeixinmpTemplateMessageDetailServiceTest extends BaseJunit4Test {
	@Autowired
	BatchWeixinmpTemplateMessageDetailService service;

	@Test
	public void findPage() {
		BatchWeixinmpTemplateMessageDetail batchWeixinmpTemplateMessageDetail = newBatchWeixinmpTemplateMessageDetail(123);
		insertBatchWeixinmpTemplateMessageDetail(batchWeixinmpTemplateMessageDetail);
		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessageDetail).getTotalItems());
		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessageDetail).getResult().size());
	}

}
