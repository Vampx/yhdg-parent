package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.LaxinService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LaxinServiceTest extends BaseJunit4Test {
    @Autowired
    LaxinService laxinService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        assertNotNull(laxinService.find(laxin.getId()));
    }

    @Test
    public void findByAgentMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        assertNotNull(laxinService.findByAgentMobile(agent.getId(), laxin.getMobile()));
    }
}
