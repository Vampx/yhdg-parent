package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrderLog;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BalanceTransferOrderLogServiceTest extends BaseJunit4Test {
    @Autowired
    BalanceTransferOrderLogService service;

    @Test
    public void findByOrderId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
        BalanceTransferOrder order = newBalanceTransferOrder(agent.getId(), orderId);
        insertBalanceTransferOrder(order);

        BalanceTransferOrderLog orderLog = newBalanceTransferOrderLog(order.getId());
        insertBalanceTransferOrderLog(orderLog);

       assertNotNull(service.findByOrderId(orderId));
    }
}
