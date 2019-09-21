package cn.com.yusong.yhdg.batteryserver.service.hdg;

import cn.com.yusong.yhdg.batteryserver.BaseJunit4Test;
import cn.com.yusong.yhdg.batteryserver.entity.Param;
import cn.com.yusong.yhdg.batteryserver.entity.Result;
import cn.com.yusong.yhdg.batteryserver.web.controller.security.hdg.BatteryController;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public class BatteryServiceTest extends BaseJunit4Test {

    @Autowired
    BatteryService batteryService;

    //无关联任何订单的电池上报
    @Test
    public void report1() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setId("12311");
        insertBattery(battery);

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "23", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联充电订单的电池上报
    @Test
    public void report2() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("1232");
        insertBattery(battery);

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "1", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联充电订单开始充电
    @Test
    public void report3() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("1233");
        insertBattery(battery);

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联充电订单充电时长结束充电
    @Test
    public void report4() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("1234");
        insertBattery(battery);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);

        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        batteryChargeRecord.setBeginTime(calendar.getTime());
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "1", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联维护订单的开始充电
    @Test
    public void report5() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery.setStatus(Battery.Status.KEEPER_OUT.getValue());
        battery.setId("1235");
        insertBattery(battery);


        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联维护订单的结束充电
    @Test
    public void report6() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.KEEPER_OUT.getValue());
        battery.setId("1236");
        insertBattery(battery);

        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //在柜子中的开始充电
    @Test
    public void report7() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setId("1237");
        insertBattery(battery);


        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //在柜子中的结束充电
    @Test
    public void report8() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setId("1238");
        insertBattery(battery);


        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //客户骑行中电量低
    @Test
    public void report9() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("1239");
        insertBattery(battery);


        Param param = new Param(battery.getCode(), "1.0", "50", "50", "10", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //客户骑行中电量低后 开始充电清空 电量低通知时间
    @Test
    public void report10() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("12310");
        battery.setLowVolumeNoticeTime(new Date());
        insertBattery(battery);

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //关联充电订单手动结束充电
    @Test
    public void report11() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.CUSTOMER_OUT.getValue());
        battery.setId("12311");
        insertBattery(battery);


        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        jdbcTemplate.update("UPDATE hdg_battery SET charge_record_id = ?   WHERE id = ?", new Object[]{battery.getChargeRecordId(), battery.getId()});

        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "0", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //柜子充电时长结束充电
    @Test
    public void report12() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.IN_BOX.getValue());
        battery.setId("12312");
        insertBattery(battery);


        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        batteryChargeRecord.setBeginTime(calendar.getTime());
        batteryChargeRecord.setDuration(1);
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        jdbcTemplate.update("UPDATE hdg_battery SET charge_record_id = ?   WHERE id = ?", new Object[]{battery.getChargeRecordId(), battery.getId()});


        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "1", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //维护充电时长结束充电
    @Test
    public void report13() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
        battery.setStatus(Battery.Status.KEEPER_OUT.getValue());
        battery.setId("12313");
        insertBattery(battery);

        KeepOrder keepOrder = newKeepOrder("KP1122", agent.getId(), battery.getId());
        insertKeepOrder(keepOrder);

        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        batteryChargeRecord.setBeginTime(calendar.getTime());
        batteryChargeRecord.setDuration(1);
        insertBatteryChargeRecord(batteryChargeRecord);
        battery.setChargeRecordId(batteryChargeRecord.getId());

        jdbcTemplate.update("UPDATE hdg_battery SET charge_record_id = ?   WHERE id = ?", new Object[]{battery.getChargeRecordId(), battery.getId()});


        Param param = new Param(battery.getCode(), "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "1", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
        assertEquals(0, (int) result.rtnCode);
    }

    //维护充电时长结束充电
    @Test
    public void report14() {
//        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        Customer customer = newCustomer(partner.getId());
//        insertCustomer(customer);
//
//        Battery battery = newBattery(agent.getId());
//        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
//        battery.setStatus(Battery.Status.KEEPER_OUT.getValue());
//        battery.setId("12313");
//        insertBattery(battery);
//
//        KeepOrder keepOrder = newKeepOrder("KP1122",agent.getId(),battery.getId());
//        insertKeepOrder(keepOrder);
//
//        BatteryChargeRecord batteryChargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR,-2);
//        batteryChargeRecord.setBeginTime(calendar.getTime());
//        batteryChargeRecord.setDuration(1);
//        batteryChargeRecord.setKeepOrderId(keepOrder.getId());
//        insertBatteryChargeRecord(batteryChargeRecord);
//        battery.setChargeRecordId(batteryChargeRecord.getId());
//
//        jdbcTemplate.update("UPDATE hdg_battery SET charge_record_id = ?   WHERE id = ?", new Object[]{battery.getChargeRecordId(), battery.getId()});


        Param param = new Param("123", "1.0", "50", "50", "1000", "2000", "8", "6845", "11", "1", "22", "15", "12", "116.303659", "40.022211", "2", "0", "31", "1", "365164568465454", "1A,82,66,54", "", 1, 1);
        Result result = new Result();
        result = batteryService.report(param);
        System.out.print(result.toString());
    }

}
