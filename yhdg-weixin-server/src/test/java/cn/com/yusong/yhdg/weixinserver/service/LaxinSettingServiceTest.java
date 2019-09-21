package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.LaxinSettingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinSettingServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinSettingService laxinSettingService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        insertLaxinSetting(laxinSetting);

        assertNotNull(laxinSettingService.find(laxinSetting.getId()));
    }
}
