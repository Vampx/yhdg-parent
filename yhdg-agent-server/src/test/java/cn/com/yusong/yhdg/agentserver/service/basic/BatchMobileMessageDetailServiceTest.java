package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchMobileMessageDetailServiceTest extends BaseJunit4Test {

    @Autowired
    BatchMobileMessageDetailService service;

    @Test
    public void findPage() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate template = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(template);

        BatchMobileMessage batchMobileMessage = newBatchMobileMessage(template.getId());
        insertBatchMobileMessage(batchMobileMessage);

        BatchMobileMessageDetail detail = newBatchMobileMessageDetail(batchMobileMessage.getId());
        insertBatchMobileMessageDetail(detail);

        assertTrue(1 == service.findPage(detail).getTotalItems());
        assertTrue(1 == service.findPage(detail).getResult().size());

    }

}
