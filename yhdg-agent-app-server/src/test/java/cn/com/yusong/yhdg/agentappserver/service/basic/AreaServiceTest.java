package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaServiceTest extends BaseJunit4Test {

    @Autowired
    AreaService areaService;

    @Test
    public void findAll() {
        Area area = newArea(1);
        insertArea(area);
        assertNotNull(areaService.findAll());
    }
}
