package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SmsConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SmsConfigService smsConfigService;

    @Test
    public void findInfoByApp() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertNotNull(smsConfigService.findInfoByPartner(agent.getId()));
    }
}
