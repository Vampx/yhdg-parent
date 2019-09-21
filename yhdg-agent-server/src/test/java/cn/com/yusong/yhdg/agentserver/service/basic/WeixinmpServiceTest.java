package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WeixinmpServiceTest extends BaseJunit4Test {
    @Autowired
    private WeixinmpService service;

    private Weixinmp weixinmp;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        weixinmp = newWeixinmp(partner.getId());
    }

    @Test
    public void find() {
        insertWeixinmp(weixinmp);

        assertNotNull(service.find(weixinmp.getId()));
    }

    @Test
    public void findPage() {
        insertWeixinmp(weixinmp);

        assertTrue(1 == service.findPage(weixinmp).getTotalItems());
        assertTrue(1 == service.findPage(weixinmp).getResult().size());
    }

    @Test
    public void findList() {
        insertWeixinmp(weixinmp);

        assertTrue(1 == service.findList(weixinmp).size());
    }

    @Test
    public void insert() {
        String sql = "insert into bas_mp_push_message_template_detail (weixinmp_id, id, template_id, keyword_name, keyword_value, color, order_num) values (0/*weixinmp_id*/,'remark', '12', '备注', '请予关注处理。', '#000000', '5')";
        List<String> sqlList = new ArrayList<String>();
        sqlList.add(sql);
        assertTrue(service.insert(weixinmp, sqlList).isSuccess());
        assertNotNull(service.find(weixinmp.getId()));
    }

    @Test
    public void update() {
        insertWeixinmp(weixinmp);

        weixinmp.setAppName("测试的appName");
        assertTrue(service.update(weixinmp).isSuccess());
        assertEquals(service.find(weixinmp.getId()).getAppName(), weixinmp.getAppName());
    }

    @Test
    public void delete() {
        insertWeixinmp(weixinmp);

        assertTrue(service.delete(weixinmp.getId()).isSuccess());
        assertNull(service.find(weixinmp.getId()));
    }
}