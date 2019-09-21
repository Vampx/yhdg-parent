package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SmsConfigServiceTest extends BaseJunit4Test {

    @Autowired
    SmsConfigService service;


    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SmsConfig smsConfig = newSmsConfig(partner.getId());
        insertSmsConfig(smsConfig);

        assertNotNull("smsConfig is null",service.find(smsConfig.getId()));
    }

//    @Test
//    public void findByApp() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        SmsConfig smsConfig = newSmsConfig(partner.getId());
//        insertSmsConfig(smsConfig);
//
//        assertTrue("false", 1 == service.findByApp(agent.getId()).size());
//    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SmsConfig smsConfig = newSmsConfig(partner.getId());
        insertSmsConfig(smsConfig);

        assertTrue("page != 1", 1 == service.findPage(smsConfig).getResult().size());
    }

    @Test
    public void insert() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SmsConfig smsConfig = newSmsConfig(partner.getId());

        assertTrue("insert false", 1 == service.insert(smsConfig));
        assertNotNull("smsConfig is null",service.find(smsConfig.getId()));
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SmsConfig smsConfig = newSmsConfig(partner.getId());
        insertSmsConfig(smsConfig);
        SmsConfig smsConfig1 = new SmsConfig();
        smsConfig1.setId(smsConfig.getId());
        smsConfig1.setPartnerId(partner.getId());
        smsConfig1.setUpdateTime(new Date());
        smsConfig1.setSignPlace(SmsConfig.SignPlace.LEFT.getValue());
        smsConfig1.setSign("sss");
        smsConfig1.setIsActive(ConstEnum.Flag.FALSE.getValue());
        smsConfig1.setBalance("34325");
        smsConfig1.setConfigName("sfsdfs");

        memCachedClient.set(CacheKey.key(CacheKey.K_ID_V_SMS_CONFIG_INFO, smsConfig.getId()), "", MemCachedConfig.CACHE_ONE_WEEK);

        assertTrue("update flase", 1 == service.update(smsConfig1));
//        assertNull(memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_SMS_CONFIG_INFO, smsConfig.getAgentId())));
    }


//    @Test
//    public void balance() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//        SmsConfig smsConfig = newSmsConfig(partner.getId());
//        insertSmsConfig(smsConfig);
//        assertTrue(service.balance(smsConfig.getId()).isSuccess());
//
//    }

    @Test
    public void handleClwSmsStatus() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        MobileMessage mobileMessage = newMobileMessage(partner.getId(), MobileMessage.SourceType.AUTH_CODE, null, agent.getId());
        mobileMessage.setMsgId("xxxxxxxxxxxxxxxxxxxxxxx");
        insertMobileMessage(mobileMessage);

        service.handleClwSmsStatus(null, null, "xxxxxxxxxxxxxxxxxxxxxxx", "1111", mobileMessage.getMobile(), MobileMessage.ClwCallbackStatus.EXPIRED.getValue());

        assertEquals(1, jdbcTemplate.queryForInt("select resend_num from bas_mobile_message where msg_id = ?", mobileMessage.getMsgId()));
        assertEquals(MobileMessage.MessageStatus.NOT.getValue(), jdbcTemplate.queryForInt("select status from bas_mobile_message where msg_id = ?", mobileMessage.getMsgId()));
    }

//    @Test
//    public void handleWndSmsStatus() throws Exception{
//        assertTrue(service.handleWndSmsStatus("asdf").isSuccess());
//    }
}
