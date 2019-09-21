package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileMessageServiceTest extends BaseJunit4Test {

    @Autowired
    MobileMessageService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(agent.getId(),
                MobileMessage.SourceType.DELIVER_ORDER,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER), null);
        insertMobileMessage(mobileMessage);

        assertNotNull(service.find(mobileMessage.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(agent.getId(),
                MobileMessage.SourceType.DELIVER_ORDER,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER), null);
        insertMobileMessage(mobileMessage);

        assertTrue(1 == service.findPage(mobileMessage).getTotalItems());
        assertTrue(1 == service.findPage(mobileMessage).getResult().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(agent.getId(),
                MobileMessage.SourceType.DELIVER_ORDER,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER), null);

        assertTrue(1 == service.insert(mobileMessage));
        assertNotNull(service.find(mobileMessage.getId()));
    }

    @Test
    public void updateStatus() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(agent.getId(),
                MobileMessage.SourceType.DELIVER_ORDER,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER), null);
        insertMobileMessage(mobileMessage);

        assertTrue(1 == service.updateStatus(mobileMessage.getId(), MobileMessage.MessageStatus.OK.getValue()));
        MobileMessage result = service.find(mobileMessage.getId());
        assertFalse(mobileMessage.getStatus().equals(result.getStatus()));
    }

}
