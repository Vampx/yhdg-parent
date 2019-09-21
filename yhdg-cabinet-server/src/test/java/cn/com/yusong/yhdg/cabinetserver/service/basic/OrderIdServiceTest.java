package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderIdServiceTest extends BaseJunit4Test {
    static Logger log = LogManager.getLogger(OrderIdServiceTest.class);

    @Autowired
    OrderIdService orderIdService;

    @Test
    public void newOrderId() {
        for(OrderId.OrderIdType e : OrderId.OrderIdType.values()) {
            log.debug("====={}",e);
            orderIdService.newOrderId(e);
        }
    }
}
