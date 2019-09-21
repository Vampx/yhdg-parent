package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessageDetail;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchWeixinmpTemplateMessageDetailServiceTest extends BaseJunit4Test {
	@Autowired
	BatchWeixinmpTemplateMessageDetailService service;

	@Test
	public void findPage() {
		BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage = newBatchWeixinmpTemplateMessage();
		insertBatchWeixinmpTemplateMessage(batchWeixinmpTemplateMessage);

		BatchWeixinmpTemplateMessageDetail batchWeixinmpTemplateMessageDetail = newBatchWeixinmpTemplateMessageDetail(batchWeixinmpTemplateMessage.getId());
		insertBatchWeixinmpTemplateMessageDetail(batchWeixinmpTemplateMessageDetail);

		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessageDetail).getTotalItems());
		assertTrue(1 == service.findPage(batchWeixinmpTemplateMessageDetail).getResult().size());
	}

}
