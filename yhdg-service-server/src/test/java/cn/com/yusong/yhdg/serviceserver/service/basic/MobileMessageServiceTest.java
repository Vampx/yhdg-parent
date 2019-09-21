package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.tool.sms.DefaultSmsHttpClientManager;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileMessageServiceTest extends BaseJunit4Test {

    @Autowired
    MobileMessageService mobileMessageService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig appConfig;

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(agent.getId(), MobileMessage.SourceType.DELIVER_ORDER, newOrderId(OrderId.OrderIdType.BATTERY_ORDER), null);
        assertEquals(1, mobileMessageService.insert(mobileMessage));
    }

    @Test
    public void scanMessage() {
        mobileMessageService.scanMessage();
    }

    @Test
    public void cleanMessage() {
        mobileMessageService.cleanMessage();
    }
}
