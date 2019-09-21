package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryFormatServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryFormatService service;

    @Test
    public void find() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        assertNotNull(service.find(batteryFormat.getId()));
    }

    @Test
    public void findPage() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        assertTrue(1 == service.findPage(batteryFormat).getTotalItems());
        assertTrue(1 == service.findPage(batteryFormat).getResult().size());
    }

    @Test
    public void create() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        assertTrue(service.create(batteryFormat).isSuccess());
        assertNotNull(service.find(batteryFormat.getId()));
    }

    @Test
    public void update() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        batteryFormat.setMemo("测试的memo");
        assertTrue(service.update(batteryFormat).isSuccess());
        assertEquals(batteryFormat.getMemo(), service.find(batteryFormat.getId()).getMemo());
    }

    @Test
    public void delete() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        assertTrue(service.delete(batteryFormat.getId()).isSuccess());
        assertNull(service.find(batteryFormat.getId()));
    }
}