package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
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
    public void findByMobile() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        assertNotNull(laxinService.findByMobile(partner.getId(), laxin.getMobile()));
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

    @Test
    public void updatePassword() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        laxinService.updatePassword(laxin.getId(), "123456");
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        LaxinSetting laxinSetting = newLaxinSetting(agent.getId());
        insertLaxinSetting(laxinSetting);

        PartnerMpOpenId partnerMpOpenId = newPartnerMpOpenId(partner.getId(), null);
        partnerMpOpenId.setOpenId("111");
        insertPartnerMpOpenId(partnerMpOpenId);

        PartnerFwOpenId partnerFwOpenId = newPartnerFwOpenId(partner.getId(), null);
        partnerFwOpenId.setOpenId("111");
        insertPartnerFwOpenId(partnerFwOpenId);

        laxinService.insert(laxinSetting, "aaa", "111", "1:111:222", 1);
        laxinService.insert(laxinSetting, "bbb", "111", "1:111:222", 2);

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where partner_id = ? and mobile = ?", partner.getId(), "aaa"));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer where partner_id = ? and mobile = ?", partner.getId(), "bbb"));
    }
}
