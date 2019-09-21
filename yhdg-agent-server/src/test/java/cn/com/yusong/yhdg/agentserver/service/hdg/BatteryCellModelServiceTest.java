package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryCellModelServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryCellModelService service;

    public void find() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        assertNotNull(service.find(batteryCellModel.getId()));
    }

    @Test
    public void findPage() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        assertTrue(1 == service.findPage(batteryCellModel).getTotalItems());
        assertTrue(1 == service.findPage(batteryCellModel).getResult().size());
    }

    @Test
    public void create() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();

        assertTrue(service.create(batteryCellModel).isSuccess());
        assertNotNull(service.find(batteryCellModel.getId()));
    }

    @Test
    public void update() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        batteryCellModel.setMemo("测试的memo");
        assertTrue(service.update(batteryCellModel).isSuccess());
        assertEquals(batteryCellModel.getMemo(), service.find(batteryCellModel.getId()).getMemo());
    }

    @Test
    public void delete() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        assertTrue(service.delete(batteryCellModel.getId()).isSuccess());
        assertNull(service.find(batteryCellModel.getId()));
    }
}