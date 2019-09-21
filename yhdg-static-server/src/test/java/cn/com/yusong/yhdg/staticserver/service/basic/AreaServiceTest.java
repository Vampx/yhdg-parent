package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AreaServiceTest extends BaseJunit4Test {
    @Autowired
    AreaService areaService;

    @Test
    public void findAll() {
        Area area = new Area();
        insertArea(area);

        assertTrue(areaService.findAll().size()>1);
    }
}