package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BalanceTransferOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BalanceTransferOrderService service;
/*
    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
        BalanceTransferOrder order = newBalanceTransferOrder(agent.getId(), orderId);
        insertBalanceTransferOrder(order);

        assertNotNull(service.find(orderId));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
        BalanceTransferOrder order = newBalanceTransferOrder(agent.getId(), orderId);
        insertBalanceTransferOrder(order);

        assertTrue(1 == service.findPage(order).getTotalItems());
        assertTrue(1 == service.findPage(order).getResult().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
        BalanceTransferOrder order = newBalanceTransferOrder(agent.getId(), orderId);

//       DayBalanceRecord dayBalanceRecord = newDayBalanceRecord(orderId, agent.getId());
//        insertDayBalanceRecord(dayBalanceRecord);
        AgentDayBalanceRecord agentDayBalanceRecord = newAgentDayBalanceRecord(agent.getId());
        insertAgentDayBalanceRecord(agentDayBalanceRecord);
        Long[] dayBalanceRecordids = {agentDayBalanceRecord.getId()};
        assertEquals(true, service.insert(order, dayBalanceRecordids).isSuccess());
    }

    @Test
    public void reset() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
        BalanceTransferOrder order = newBalanceTransferOrder(agent.getId(), orderId);
        order.setStatus(BalanceTransferOrder.Status.FAILURE.getValue());
        insertBalanceTransferOrder(order);

        assertEquals(true, service.reset(orderId, order.getOpenId(), order.getFullName(), order.getHandleUser()).isSuccess());
    }*/

}
