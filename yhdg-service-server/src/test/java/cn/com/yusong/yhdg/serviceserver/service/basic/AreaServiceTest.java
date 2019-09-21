package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaServiceTest extends BaseJunit4Test {

    @Autowired
    AreaService areaService;

    @Test
    public void findAll() {
        assertNotNull(areaService.findAll());
    }
}
