package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BespeakOrderServiceTest extends BaseJunit4Test {
    @Autowired
    BespeakOrderService bespeakOrderService;

    @Test
    public void expire() {
        bespeakOrderService.expire();
    }
}
