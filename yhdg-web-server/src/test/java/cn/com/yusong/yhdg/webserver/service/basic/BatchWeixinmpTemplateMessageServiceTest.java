package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessage;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchWeixinmpTemplateMessageServiceTest extends BaseJunit4Test {
	@Autowired
	BatchWeixinmpTemplateMessageService service;

	@Test
	public void find() {
		BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage = newBatchWeixinmpTemplateMessage();
		insertBatchWeixinmpTemplateMessage(batchWeixinmpTemplateMessage);
		assertNotNull(service.find(batchWeixinmpTemplateMessage.getId()));
	}

	@Test
	public void findPage() {
		BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage = newBatchWeixinmpTemplateMessage();
		insertBatchWeixinmpTemplateMessage(batchWeixinmpTemplateMessage);
		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessage).getTotalItems());
		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessage).getResult().size());
	}

	@Test
	public void insert() throws Exception{
		BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage = newBatchWeixinmpTemplateMessage();
		insertBatchWeixinmpTemplateMessage(batchWeixinmpTemplateMessage);
		String[] variables = {"asdf","asdf"};
		String[] contents = {"asdf","asdf"};
		Integer checked = 1;

		assertTrue(service.insert(batchWeixinmpTemplateMessage,variables,contents,checked).isSuccess());
	}
}
