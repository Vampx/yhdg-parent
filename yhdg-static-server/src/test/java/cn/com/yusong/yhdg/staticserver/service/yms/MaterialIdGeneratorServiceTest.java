package cn.com.yusong.yhdg.staticserver.service.yms;

import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class MaterialIdGeneratorServiceTest extends BaseJunit4Test {
    @Autowired
    MaterialIdGeneratorService materialIdGeneratorService;

    @Test
    public void newId() {
        assertTrue(materialIdGeneratorService.newId()>0);
    }
}