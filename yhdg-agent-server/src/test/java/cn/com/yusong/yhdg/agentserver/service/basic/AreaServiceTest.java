package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaServiceTest extends BaseJunit4Test {
    @Autowired
    AreaService service;

    @Test
    public void findChildren() {
        Area area = newArea(null);
        insertArea(area);
        assertTrue(service.findChildren(area.getId()).isEmpty());
        Area area1 = newArea(area.getId());
        area1.setId(4001);
        insertArea(area1);
        assertNotNull(service.findChildren(area.getId()));
    }

    @Test
    public void find() {
        Area area = newArea(null);
        insertArea(area);
        assertNotNull(service.find(area.getId()));
    }

    @Test
    public void findAll() {
        assertTrue(service.findAll().size() == 3220);
    }

    @Test
    public void findAllCity() {
        assertNotNull(service.findAllCity().size());
    }


}
