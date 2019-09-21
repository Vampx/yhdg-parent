package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PushMessageServiceTest extends BaseJunit4Test {

    @Autowired
    PushMessageService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        PushMessage pushMessage = newPushMessage(agent.getId(),
                PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        insertPushMessage(pushMessage);

        PushMessageContent pushMessageContent = newPushMessageContent(pushMessage.getId());
        insertPushMessageContent(pushMessageContent);

        assertNotNull(service.find(pushMessage.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        PushMessage pushMessage = newPushMessage(agent.getId(),
                PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        insertPushMessage(pushMessage);

        PushMessageContent pushMessageContent = newPushMessageContent(pushMessage.getId());
        insertPushMessageContent(pushMessageContent);

        assertTrue(1 == service.findPage(pushMessage).getTotalItems());
        assertTrue(1 == service.findPage(pushMessage).getResult().size());
    }

}
