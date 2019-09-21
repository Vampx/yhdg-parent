package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhoub on 2017/11/17.
 */
public class BatchMobileMessageServiceTest extends BaseJunit4Test {

    @Autowired
    BatchMobileMessageService service;

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate template = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(template);

        BatchMobileMessage batchMobileMessage = newBatchMobileMessage(template.getId());
        insertBatchMobileMessage(batchMobileMessage);

        assertTrue(1 == service.findPage(batchMobileMessage).getTotalItems());
        assertTrue(1 == service.findPage(batchMobileMessage).getResult().size());

    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate template = newMobileMessageTemplate(0);
        insertMobileMessageTemplate(template);

        BatchMobileMessage batchMobileMessage = newBatchMobileMessage(template.getId());
        String[] variables = {"3245", "1234"};
        String[] contents = {"您的验证码是${authCode}，如非本人操作，请忽略本短信【浙江宇松】"
        ,"您的验证码是${authCode}，如非本人操作，请忽略本短信【浙江宇松】"};
        service.insert(batchMobileMessage, variables, contents);

        assertNotNull(service.find(batchMobileMessage.getId()));
    }

}
