package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryCellFormatServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryCellFormatService service;

    private BatteryCellFormat batteryCellFormat;

    @Before
    public void setUp() throws Exception {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        batteryCellFormat = newBatteryCellFormat(batteryCellModel.getId());
    }

    @Test
    public void find() {
        insertBatteryCellFormat(batteryCellFormat);

        assertNotNull(service.find(batteryCellFormat.getId()));
    }

    @Test
    public void findPage() {
        insertBatteryCellFormat(batteryCellFormat);

        assertTrue(1 == service.findPage(batteryCellFormat).getTotalItems());
        assertTrue(1 == service.findPage(batteryCellFormat).getResult().size());
    }

    @Test
    public void create() {
        assertTrue(service.create(batteryCellFormat).isSuccess());
        assertNotNull(service.find(batteryCellFormat.getId()));
    }

    @Test
    public void update() {
        insertBatteryCellFormat(batteryCellFormat);

        batteryCellFormat.setCellMfr("新的厂家");
        assertTrue(service.update(batteryCellFormat).isSuccess());
        assertEquals(batteryCellFormat.getCellMfr(), service.find(batteryCellFormat.getId()).getCellMfr());
    }

    @Test
    public void delete() {
        insertBatteryCellFormat(batteryCellFormat);

        assertTrue(service.delete(batteryCellFormat.getId()).isSuccess());
        assertNull(service.find(batteryCellFormat.getId()));
    }
}