package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PacketPeriodOrderServiceTest  extends BaseJunit4Test {
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;

    @Test
    public void expire() {
        packetPeriodOrderService.expire();
    }
}
