package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        insertLaxinSetting(laxinSetting);

        assertEquals(1, ((List) laxinSettingService.findList(agent.getId(), laxinSetting.getSettingName(), 0, 1).getData()).size());
    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        laxinSettingService.create(laxinSetting);

    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        insertLaxinSetting(laxinSetting);

        laxinSettingService.update(laxinSetting);
    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        insertLaxinSetting(laxinSetting);

        laxinSettingService.delete(laxinSetting.getId());
    }
}
