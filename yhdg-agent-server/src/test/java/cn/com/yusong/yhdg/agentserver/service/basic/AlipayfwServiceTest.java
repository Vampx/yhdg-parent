package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AlipayfwServiceTest extends BaseJunit4Test {
    @Autowired
    private AlipayfwService service;
    Alipayfw alipayfw;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        alipayfw = newAlipayfw(partner.getId());
    }

    @Test
    public void find() {
        insertAlipayfw(alipayfw);

        assertNotNull(service.find(alipayfw.getId()));
    }

    @Test
    public void findPage() {
        insertAlipayfw(alipayfw);

        assertTrue(1 == service.findPage(alipayfw).getTotalItems());
        assertTrue(1 == service.findPage(alipayfw).getResult().size());
    }

    @Test
    public void findList() {
        insertAlipayfw(alipayfw);

        assertTrue(1 == service.findList(alipayfw).size());
    }

    @Test
    public void insert() {
        String sql = "insert into bas_fw_push_message_template_detail (alipayfw_id, id, template_id, keyword_name, keyword_value, color, order_num) values (0/*alipayfw_id*/,'keyword3', '1', '赠送金额', '${gift}', '#000000', '4');";
        List<String> sqlList = new ArrayList<String>();
        sqlList.add(sql);

        assertTrue(service.insert(alipayfw, sqlList).isSuccess());
        assertNotNull(service.find(alipayfw.getId()));
    }

    @Test
    public void update() {
        insertAlipayfw(alipayfw);

        alipayfw.setAppName("测试的appName");
        assertTrue(service.update(alipayfw).isSuccess());
        assertEquals(alipayfw.getAppName(), service.find(alipayfw.getId()).getAppName());
    }

    @Test
    public void delete() {
        insertAlipayfw(alipayfw);

        assertTrue(service.delete(alipayfw.getId()).isSuccess());
        assertNull(service.find(alipayfw.getId()));
    }
}