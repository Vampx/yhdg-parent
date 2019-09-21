package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PhoneappServiceTest extends BaseJunit4Test {
    @Autowired
    private PhoneappService service;
    Phoneapp phoneapp;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        phoneapp = newPhoneapp(partner.getId());
    }

    @Test
    public void find() {
        insertPhoneapp(phoneapp);

        assertNotNull(service.find(phoneapp.getId()));
    }

    @Test
    public void findPage() {
        insertPhoneapp(phoneapp);

        assertTrue(1 == service.findPage(phoneapp).getTotalItems());
        assertTrue(1 == service.findPage(phoneapp).getResult().size());
    }

    @Test
    public void insert() {
        assertTrue(service.insert(phoneapp).isSuccess());
    }

    @Test
    public void update() {
        insertPhoneapp(phoneapp);

        phoneapp.setAppName("测试的appName");
        assertTrue(service.update(phoneapp).isSuccess());
        assertEquals(service.find(phoneapp.getId()).getAppName(), phoneapp.getAppName());
    }

    @Test
    public void delete() {
        insertPhoneapp(phoneapp);

        assertTrue(service.delete(phoneapp.getId()).isSuccess());
        assertNull(service.find(phoneapp.getId()));
    }
}