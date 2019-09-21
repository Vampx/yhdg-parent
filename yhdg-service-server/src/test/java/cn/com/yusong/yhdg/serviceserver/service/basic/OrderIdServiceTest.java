package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderIdServiceTest extends BaseJunit4Test {
    @Autowired
    OrderIdService orderIdService;

    @Test
    public void clean() {
        orderIdService.clean();
    }
}
