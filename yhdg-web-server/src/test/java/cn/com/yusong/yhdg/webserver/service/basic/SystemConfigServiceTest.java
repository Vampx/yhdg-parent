package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {

    @Autowired
    SystemConfigService service;

    @Test
    public void find() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(service.find(systemConfig.getId()));
    }

    @Test
    public void findConfigValue() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertNotNull(service.findConfigValue(systemConfig.getId()));
    }

    @Test
    public void findAll() {
        int count = jdbcTemplate.queryForInt("select count(*) from bas_system_config");
        assertEquals(count, service.findAll().size());
    }

    @Test
    public void findPage() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        assertTrue(1 == service.findPage(systemConfig).getResult().size());
    }

    @Test
    public void update() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        SystemConfig systemConfig1 = new SystemConfig();
        systemConfig1.setConfigName("sfd");
        systemConfig1.setConfigValue("tbfdfdg");
        systemConfig1.setValueType((int) SystemConfig.ValueType.INT.getValue());
        systemConfig1.setIsShow(ConstEnum.Flag.TRUE.getValue());
        systemConfig1.setIsReadOnly(ConstEnum.Flag.TRUE.getValue());
        systemConfig1.setCategoryName("dsfsd");
        systemConfig1.setCategoryType(2);
        systemConfig1.setId(systemConfig.getId());

        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, systemConfig.getId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        service.update(systemConfig1);
        assertNull("memcached is not null", memCachedClient.get(key));
    }
    @Test
    public void initConfig() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SmsConfig smsConfig = newSmsConfig(agent.getId());
        smsConfig.setPartnerId(partner.getId());
        insertSmsConfig(smsConfig);
        service.initConfig();

    }

}
