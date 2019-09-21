package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UnregisterBatteryServiceTest extends BaseJunit4Test {
    @Autowired
    UnregisterBatteryService service;

    @Test
    public void findPage() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertTrue(1 == service.findPage(unregisterBattery).getTotalItems());
        assertTrue(1 == service.findPage(unregisterBattery).getResult().size());
    }

    @Test
    public void find() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertNotNull(service.find(unregisterBattery.getId()));
    }

    @Test
    public void findByShellCode() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        unregisterBattery.setShellCode("shellCode");
        insertUnregisterBattery(unregisterBattery);

        DataResult dataResult = (DataResult) service.findByShellCode(unregisterBattery.getShellCode());
        assertTrue(dataResult.isSuccess());
        UnregisterBattery unregisterBattery1 = (UnregisterBattery) dataResult.getData();
        assertEquals(unregisterBattery1.getId(), unregisterBattery.getId());
    }

    @Test
    public void checkBindParam() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        unregisterBattery.setShellCode("shellCode");
        insertUnregisterBattery(unregisterBattery);

        DataResult dataResult = (DataResult) service.checkBindParam(unregisterBattery.getCode(), unregisterBattery.getShellCode());
        assertTrue(dataResult.isSuccess());
        UnregisterBattery unregisterBattery1 = (UnregisterBattery) dataResult.getData();
        assertEquals(unregisterBattery.getId(), unregisterBattery1.getId());
    }

    @Test
    public void createCheckedBattery() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        unregisterBattery.setShellCode("shellCode");
        insertUnregisterBattery(unregisterBattery);

        DataResult dataResult = (DataResult) service.createCheckedBattery(unregisterBattery);
        assertTrue(dataResult.isSuccess());
        UnregisterBattery unregisterBattery1 = (UnregisterBattery) dataResult.getData();
        assertEquals(unregisterBattery1.getId(), unregisterBattery.getId());
    }

    @Test
    public void delete() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertTrue(service.delete(unregisterBattery.getId()).isSuccess());
        assertNull(service.find(unregisterBattery.getId()));
    }

    @Test
    public void updateCellBind() {
        BatteryCellModel batteryCellModel = newBatteryCellModel();
        insertBatteryCellModel(batteryCellModel);

        BatteryFormat batteryFormat = newBatteryFormat(batteryCellModel.getId());
        insertBatteryFormat(batteryFormat);

        BatteryBarcode batteryBarcode = newBatteryBarcode(batteryFormat.getId());
        batteryBarcode.setBarcode("testBarcode1");
        insertBatteryBarcode(batteryBarcode);

        BatteryBarcode batteryBarcode2 = newBatteryBarcode(batteryFormat.getId());
        batteryBarcode2.setBarcode("testBarcode2");
        insertBatteryBarcode(batteryBarcode2);

        BatteryCell batteryCell = newBatteryCell();
        insertBatteryCell(batteryCell);

        UnregisterBattery unregisterBattery1 = newUnregisterBattery();
        unregisterBattery1.setShellCode(batteryBarcode.getBarcode());
        unregisterBattery1.setCellCount(1);
        insertUnregisterBattery(unregisterBattery1);

        UnregisterBattery unregisterBattery2 = newUnregisterBattery();
        unregisterBattery2.setId("id2");
        unregisterBattery2.setCellCount(0);
        unregisterBattery2.setShellCode(batteryBarcode2.getBarcode());
        insertUnregisterBattery(unregisterBattery2);

        assertTrue(service.updateCellBind(unregisterBattery1.getId(), unregisterBattery1.getShellCode(), unregisterBattery2.getShellCode()).isSuccess());

    }

    @Test
    public void clearCode() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        assertTrue(service.clearCode(unregisterBattery.getId()).isSuccess());
        UnregisterBattery newUnregisterBattery = new UnregisterBattery();
        UnregisterBattery dbUnregisterBattery = (UnregisterBattery) service.findPage(newUnregisterBattery).getResult().get(0);
        assertEquals(dbUnregisterBattery.getCode(), "");
    }

    @Test
    public void updateQrcode() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setCode(unregisterBattery.getCode());
        insertBattery(battery);

        unregisterBattery.setQrcode("测试的qrcode");
        assertTrue(service.updateQrcode(unregisterBattery.getId(), unregisterBattery.getQrcode()).isSuccess());
        assertEquals(service.find(unregisterBattery.getId()).getQrcode(), unregisterBattery.getQrcode());
    }

    @Test
    public void checkBattery() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        unregisterBattery.setCellMfr("测试的cellMfr");
        unregisterBattery.setCellModel("测试的cellModel");
        assertTrue(service.checkBattery(unregisterBattery).isSuccess());
        assertEquals(service.find(unregisterBattery.getId()).getCellMfr(), unregisterBattery.getCellMfr());
        assertEquals(service.find(unregisterBattery.getId()).getCellModel(), unregisterBattery.getCellModel());
    }

    @Test
    public void updateCode() {
        UnregisterBattery unregisterBattery = newUnregisterBattery();
        insertUnregisterBattery(unregisterBattery);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        systemBatteryType.setId(2);
        insertSystemBatteryType(systemBatteryType);

        unregisterBattery.setCode("测试的code");
        assertTrue(service.updateCode(unregisterBattery.getId(), unregisterBattery.getCode()).isSuccess());
        assertEquals(service.find(unregisterBattery.getId()).getCode(), unregisterBattery.getCode());
    }



}
