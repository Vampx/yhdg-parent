package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderIdServiceTest extends BaseJunit4Test {

    @Autowired
    OrderIdService orderIdService;

    @Test
    public void newOrderId() {
        orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.KEEP_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER);
        orderIdService.newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER);
    }
}
