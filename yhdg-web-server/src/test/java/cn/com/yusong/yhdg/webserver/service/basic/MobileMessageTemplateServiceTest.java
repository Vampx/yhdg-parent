package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileMessageTemplateServiceTest extends BaseJunit4Test {

    @Autowired
    MobileMessageTemplateService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate mobileMessageTemplate = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(mobileMessageTemplate);

        assertNotNull(service.find(agent.getId(), mobileMessageTemplate.getId()));
    }

    @Test
    public void findAll() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate mobileMessageTemplate = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(mobileMessageTemplate);

        assertNotNull(service.findAll());
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate mobileMessageTemplate = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(mobileMessageTemplate);

        assertNotNull("分页 is null", service.findPage(mobileMessageTemplate).getTotalItems());
        assertNotNull("分页 is null", service.findPage(mobileMessageTemplate).getResult().size());
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessageTemplate mobileMessageTemplate = newMobileMessageTemplate(agent.getId());
        insertMobileMessageTemplate(mobileMessageTemplate);

        String key = CacheKey.key(CacheKey.K_ID_V_MOBILE_MESSAGE_TEMPLATE, mobileMessageTemplate.getPartnerId(), mobileMessageTemplate.getId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        assertTrue("update false", service.update(mobileMessageTemplate).isSuccess());
        assertNull("memcached is not null", memCachedClient.get(key));
    }


}
