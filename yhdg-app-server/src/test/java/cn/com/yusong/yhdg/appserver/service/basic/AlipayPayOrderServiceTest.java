package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public class AlipayPayOrderServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    OrderIdService orderIdService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceId(920);
        agent.setCityId(921);
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        String sourceId = OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue() + ":" + 1;
        AlipayPayOrder alipayPayOrder1 = new AlipayPayOrder();
        alipayPayOrder1.setId(orderIdService.newOrderId(OrderId.OrderIdType.ALIPAY_PAY_ORDER));
        alipayPayOrder1.setAgentId(agent.getId());
        alipayPayOrder1.setCustomerId(customer.getId());
        alipayPayOrder1.setMoney(11);
        alipayPayOrder1.setSourceType(AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        alipayPayOrder1.setSourceId(sourceId);
        alipayPayOrder1.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        alipayPayOrder1.setCreateTime(new Date());
        alipayPayOrder1.setPartnerId(partner.getId());
        assertEquals(1, alipayPayOrderService.insert(alipayPayOrder1));


    }

}
