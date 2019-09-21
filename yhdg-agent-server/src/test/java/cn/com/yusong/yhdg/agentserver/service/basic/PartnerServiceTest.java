package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PartnerServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerService service;

    Partner partner;

    @Before
    public void setUp() throws Exception {
        partner = newPartner();
    }

    @Test
    public void find() {
        insertPartner(partner);

        assertNotNull(partner);
    }

    @Test
    public void findPage() {
        insertPartner(partner);

        assertTrue(1 == service.findPage(partner).getTotalItems());
        assertTrue(1 == service.findPage(partner).getResult().size());
    }

    @Test
    public void findAll() {
        insertPartner(partner);

        assertTrue(1 == service.findAll().size());
    }

    @Test
    public void findList() {
        insertPartner(partner);

        assertTrue(1 == service.findList(partner).size());
    }

    @Test
    public void create() {
        String sql = "\n" +
                "insert into bas_mobile_message_template (partner_id, id, title, content, receiver, variable)\n" +
                "values(0/*partner_id*/, 1, '验证码', '您的验证码是${authCode}，如非本人操作，请忽略本短信', '客户', 'authCode');";
        List<String> sqlList = new ArrayList<String>();
        sqlList.add(sql);

        assertTrue(service.create(partner, sqlList).isSuccess());
        assertNotNull(service.find(partner.getId()));
    }

    @Test
    public void update() {
        insertPartner(partner);

        partner.setMpAppName("测试的mpAppName");
        assertTrue(service.update(partner).isSuccess());
        assertEquals(partner.getMpAppName(), service.find(partner.getId()).getMpAppName());
    }

    @Test
    public void delete() {
        insertPartner(partner);

        assertTrue(service.delete(partner.getId()).isSuccess());
        assertNull(service.find(partner.getId()));
    }
}