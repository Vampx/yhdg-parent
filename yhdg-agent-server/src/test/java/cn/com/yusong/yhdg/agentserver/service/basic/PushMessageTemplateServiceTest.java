package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PushMessageTemplateServiceTest extends BaseJunit4Test {

    @Autowired
    PushMessageTemplateService service;

    @Test
    public void find() {
        PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
        insertPushMessageTemplate(pushMessageTemplate);

        assertNotNull(service.find(pushMessageTemplate.getId()));
    }

    @Test
    public void findPage() {
        PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
        insertPushMessageTemplate(pushMessageTemplate);

        assertTrue(1 == service.findPage(pushMessageTemplate).getTotalItems());
        assertTrue(1 == service.findPage(pushMessageTemplate).getResult().size());    }

    @Test
    public void update() {
        PushMessageTemplate pushMessageTemplate = newPushMessageTemplate();
        pushMessageTemplate.setContent("修该内容");
        insertPushMessageTemplate(pushMessageTemplate);
        assertTrue(service.update(pushMessageTemplate).isSuccess());
    }

}
