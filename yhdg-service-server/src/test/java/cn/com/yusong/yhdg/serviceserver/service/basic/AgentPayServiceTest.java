package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.service.hdg.CabinetService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;


public class AgentPayServiceTest extends BaseJunit4Test {
    @Autowired
    AgentPayService agentPayService;
    @Autowired
    CabinetService cabinetService;

    /**
     * 柜子押金统计
     */
    @Test
    public void stats_1() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setUpLineStatus(2);//已上线
        cabinet1.setForegiftMoney(100);//押金100
        cabinet1.setUpLineTime(DateUtils.addDays(new Date(), -1));
        insertCabinet(cabinet1);

        Cabinet cabinet2 = newCabinet(agent.getId(), null);
        cabinet2.setId("00012");
        cabinet2.setUpLineStatus(2);//已上线
        cabinet2.setForegiftMoney(300);//押金200
        cabinet2.setUpLineTime(DateUtils.addDays(new Date(), -1));
        insertCabinet(cabinet2);

        Cabinet cabinet3 = newCabinet(agent.getId(), null);
        cabinet3.setId("00013");
        cabinet3.setUpLineStatus(0);//未上线
        cabinet3.setForegiftMoney(100);//押金200
        cabinet3.setUpLineTime(DateUtils.addDays(new Date(), -1));
        insertCabinet(cabinet3);

        Cabinet cabinet4 = newCabinet(agent.getId(), null);
        cabinet4.setId("00014");
        cabinet4.setUpLineStatus(2);//已上线
        cabinet4.setForegiftMoney(0);//押金0
        cabinet4.setUpLineTime(DateUtils.addDays(new Date(), -1));
        insertCabinet(cabinet4);

        //今天统计昨天数据
        agentPayService.stats(DateUtils.addDays(new Date(), -1));


        //设备押金数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_foregift_count from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //设备押金金额
        assertEquals(400, jdbcTemplate.queryForInt("select cabinet_foregift_money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //总金额  押金金额不计入统计
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));

        //柜子的押金记录数量
        assertEquals(2, jdbcTemplate.queryForInt("select count(1) from hdg_agent_cabinet_foregift_record where agent_id = ?", agent.getId()));
       //柜子的押金记录金额
        assertEquals(400, jdbcTemplate.queryForInt("select sum(money) from hdg_agent_cabinet_foregift_record where agent_id = ?", agent.getId()));
    }

    /**
     * 柜子租金统计
     */
    @Test
    public void stats_2() throws ParseException {
        //固定当前时间
        Date newDate = DateUtils.parseDate("2019-05-29 10:13:00", new String[]{Constant.DATE_TIME_FORMAT});

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        //上线时间(两个月前)
        Date upLineTime = DateUtils.addMonths(newDate, -2);

        //参数：上线时间2个月前，周期1个月，租金100，截止时间：下个月
        //期望生成数据：
        //记录1 03-29 00:00:00 至 04-28 23:59:49  租金100
        //记录1 04-29 00:00:00 至 05-28 23:59:49  租金100
        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setUpLineStatus(2);//已上线
        cabinet1.setUpLineTime(upLineTime);
        cabinet1.setRentPeriodType(1);//周期1个月
        cabinet1.setRentMoney(100);
        cabinet1.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet1);

        //上线时间(两个月前再前一天)
        upLineTime = DateUtils.addDays(upLineTime , -1);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：下个月
        //期望生成数据：
        //记录1 03-28 00:00:00 至 04-27 23:59:49  租金100
        //记录1 04-28 00:00:00 至 05-27 23:59:49  租金100
        //记录1 05-28 00:00:00 至 06-27 23:59:49  租金100
        Cabinet cabinet2 = newCabinet(agent.getId(), null);
        cabinet2.setId("00012");
        cabinet2.setUpLineStatus(2);//已上线
        cabinet2.setUpLineTime(upLineTime);
        cabinet2.setRentPeriodType(1);//周期1个月
        cabinet2.setRentMoney(100);
        cabinet2.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet2);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：'05-27'
        //期望生成数据：
        //记录1 03-28 00:00:00 至 04-27 23:59:49  租金100
        //记录1 04-28 00:00:00 至 05-27 23:59:49  租金100
        Cabinet cabinet6 = newCabinet(agent.getId(), null);
        cabinet6.setId("00016");
        cabinet6.setUpLineStatus(2);//已上线
        cabinet6.setUpLineTime(upLineTime);
        cabinet6.setRentPeriodType(1);//周期1个月
        cabinet6.setRentMoney(100);
        cabinet6.setRentExpireTime(DateUtils.addDays(newDate, -2));
        insertCabinet(cabinet6);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：'05-20'
        //期望生成数据：
        //记录1 03-28 00:00:00 至 04-27 23:59:49  租金100
        //记录1 04-28 00:00:00 至 05-20 23:59:49  租金  76 ( 100 / 30 *  23   (截止时间 - 开始时间))
        Cabinet cabinet7 = newCabinet(agent.getId(), null);
        cabinet7.setId("00017");
        cabinet7.setUpLineStatus(2);//已上线
        cabinet7.setUpLineTime(upLineTime);
        cabinet7.setRentPeriodType(1);//周期1个月
        cabinet7.setRentMoney(100);
        cabinet7.setRentExpireTime(DateUtils.addDays(newDate, -9));
        insertCabinet(cabinet7);

        //参数：上线时间2个月前再前一天，周期3个月，租金100，截止时间：'05-20'
        //期望生成数据：
        //记录1 03-28 00:00:00  至 05-20 23:59:49  租金  60
        Cabinet cabinet8 = newCabinet(agent.getId(), null);
        cabinet8.setId("00018");
        cabinet8.setUpLineStatus(2);//已上线
        cabinet8.setUpLineTime(upLineTime);
        cabinet8.setRentPeriodType(3);//周期3个月
        cabinet8.setRentMoney(100);
        cabinet8.setRentExpireTime(DateUtils.addDays(newDate, -9));
        insertCabinet(cabinet8);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：下个月 已经生成一个月数据
        //期望生成数据：
        //记录1 04-28 00:00:00 至 05-27 23:59:49  租金100
        //记录1 05-28 00:00:00 至 06-27 23:59:49  租金100
        Cabinet cabinet9 = newCabinet(agent.getId(), null);
        cabinet9.setId("00019");
        cabinet9.setUpLineStatus(2);//已上线
        cabinet9.setUpLineTime(upLineTime);
        cabinet9.setRentPeriodType(1);//周期1个月
        cabinet9.setRentRecordTime( DateUtils.addMonths(upLineTime, 1));
        cabinet9.setRentMoney(100);
        cabinet9.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet9);

        /*以下为不统计终端*/
        Cabinet cabinet3 = newCabinet(agent.getId(), null);
        cabinet3.setId("00013");
        cabinet3.setUpLineStatus(0);//未上线
        cabinet3.setUpLineTime(upLineTime);
        cabinet3.setRentPeriodType(1);//周期1个月
        cabinet3.setRentMoney(100);
        cabinet3.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet3);

        Cabinet cabinet4 = newCabinet(agent.getId(), null);
        cabinet4.setId("00014");
        cabinet4.setUpLineStatus(2);//已上线
        cabinet4.setUpLineTime(upLineTime);
        cabinet4.setRentPeriodType(0);//周期为0
        cabinet4.setRentMoney(100);
        cabinet4.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet4);

        Cabinet cabinet5 = newCabinet(agent.getId(), null);
        cabinet5.setId("00015");
        cabinet5.setUpLineStatus(2);//已上线
        cabinet5.setUpLineTime(upLineTime);
        cabinet5.setRentPeriodType(1);//周期为0
        cabinet5.setRentMoney(0);//租金为空0
        cabinet5.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertCabinet(cabinet5);

        //今天统计昨天数据
        agentPayService.stats(DateUtils.addDays(newDate, -1));


        //设备租金数量
        assertEquals(12, jdbcTemplate.queryForInt("select cabinet_rent_count from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //设备租金金额
        assertEquals(1136, jdbcTemplate.queryForInt("select cabinet_rent_money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //总金额
        assertEquals(1136, jdbcTemplate.queryForInt("select money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));

        //柜子的租金记录数量
        assertEquals(12, jdbcTemplate.queryForInt("select count(1) from hdg_agent_cabinet_rent_record where agent_id = ?", agent.getId()));
        //柜子的租金记录金额
        assertEquals(1136, jdbcTemplate.queryForInt("select sum(money) from hdg_agent_cabinet_rent_record where agent_id = ?", agent.getId()));
    }

    /**
     * 电池租金统计
     */
    @Test
    public void stats_3() throws ParseException {
        //固定当前时间
        Date newDate = DateUtils.parseDate("2019-05-29 10:13:00", new String[]{Constant.DATE_TIME_FORMAT});

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        SystemBatteryType systemBatteryType1 = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType1);

        SystemBatteryType systemBatteryType2 = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType2);

        SystemBatteryType systemBatteryType3 = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType3);

        //参数：周期1个月，租金100，截止时间：下个月
        BatteryTypeIncomeRatio ratio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
        ratio.setRentPeriodMoney(100);
        ratio.setRentPeriodType(1);
        ratio.setRentExpireTime(DateUtils.addMonths(newDate, 1));
        insertBatteryTypeIncomeRatio(ratio);

        //参数：周期1个月，租金100，截止时间：05-27'
        BatteryTypeIncomeRatio ratio1 = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType1.getId());
        ratio1.setRentPeriodMoney(100);
        ratio1.setRentPeriodType(1);
        ratio1.setRentExpireTime(DateUtils.addDays(newDate, -2));
        insertBatteryTypeIncomeRatio(ratio1);

        //参数：周期1个月，租金100，截止时间：05-20'
        BatteryTypeIncomeRatio ratio2 = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType2.getId());
        ratio2.setRentPeriodMoney(100);
        ratio2.setRentPeriodType(1);
        ratio2.setRentExpireTime(DateUtils.addDays(newDate, -9));
        insertBatteryTypeIncomeRatio(ratio2);

        //参数：周期3个月，租金100，截止时间：05-20'
        BatteryTypeIncomeRatio ratio3 = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType3.getId());
        ratio3.setRentPeriodMoney(100);
        ratio3.setRentPeriodType(3);
        ratio3.setRentExpireTime(DateUtils.addDays(newDate, -9));
        insertBatteryTypeIncomeRatio(ratio3);

        //上线时间(两个月前)
        Date upLineTime = DateUtils.addMonths(newDate, -2);

        //参数：上线时间2个月前，周期1个月，租金100，截止时间：下个月
        //期望生成数据：
        //记录1 03-29 00:00:00 至 04-28 23:59:49  租金100
        //记录1 04-29 00:00:00 至 05-28 23:59:49  租金100
        Battery battery1 = newBattery(agent.getId(), systemBatteryType.getId());
        battery1.setId("1");
        battery1.setUpLineStatus(1);//已上线
        battery1.setUpLineTime(upLineTime);
        battery1.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery1);

        //上线时间(两个月前再前一天)
        upLineTime = DateUtils.addDays(upLineTime , -1);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：'05-27'
        //期望生成数据：
        //记录1 03-28 00:00:00 至 04-27 23:59:49  租金100
        //记录1 04-28 00:00:00 至 05-27 23:59:49  租金100
        Battery battery2 = newBattery(agent.getId(), systemBatteryType1.getId());
        battery2.setId("2");
        battery2.setUpLineStatus(1);//已上线
        battery2.setUpLineTime(upLineTime);
        battery2.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery2);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：'05-20'
        //期望生成数据：
        //记录1 03-28 00:00:00 至 04-27 23:59:49  租金100
        //记录1 04-28 00:00:00 至 05-20 23:59:49  租金  76 ( 100 / 30 *  23   (截止时间 - 开始时间))
        Battery battery3 = newBattery(agent.getId(), systemBatteryType2.getId());
        battery3.setId("3");
        battery3.setUpLineStatus(1);//已上线
        battery3.setUpLineTime(upLineTime);
        battery3.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery3);

        //参数：上线时间2个月前再前一天，周期3个月，租金100，截止时间：'05-20'
        //期望生成数据：
        //记录1 03-28 00:00:00  至 05-20 23:59:49  租金  60
        Battery battery4 = newBattery(agent.getId(), systemBatteryType3.getId());
        battery4.setId("4");
        battery4.setUpLineStatus(1);//已上线
        battery4.setUpLineTime(upLineTime);
        battery4.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery4);

        //参数：上线时间2个月前再前一天，周期1个月，租金100，截止时间：下个月 已经生成一个月数据
        //期望生成数据：
        //记录1 04-28 00:00:00 至 05-27 23:59:49  租金100
        //记录1 05-28 00:00:00 至 06-27 23:59:49  租金100
        Battery battery5 = newBattery(agent.getId(), systemBatteryType.getId());
        battery5.setId("5");
        battery5.setUpLineStatus(1);//已上线
        battery5.setUpLineTime(upLineTime);
        battery5.setRentRecordTime( DateUtils.addMonths(upLineTime, 1));
        battery5.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery5);


        /*以下为不统计终端*/
        Battery battery6 = newBattery(agent.getId(), systemBatteryType.getId());
        battery6.setId("6");
        battery6.setUpLineStatus(0);//未上线
        battery6.setUpLineTime(upLineTime);
        battery6.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        insertBattery(battery6);


        //上线时间(两个月前)
        upLineTime = DateUtils.addMonths(newDate, -2);

        //参数：上线时间2个月前，周期1个月，租金100，截止时间：下个月  (租电电池)
        //期望生成数据：
        //记录1 03-29 00:00:00 至 04-28 23:59:49  租金100
        //记录1 04-29 00:00:00 至 05-28 23:59:49  租金100
        Battery battery7 = newBattery(agent.getId(), systemBatteryType.getId());
        battery7.setId("7");
        battery7.setUpLineStatus(1);//已上线
        battery7.setUpLineTime(upLineTime);
        battery7.setCategory(ConstEnum.Category.RENT.getValue());
        insertBattery(battery7);

        //今天统计昨天数据
        agentPayService.stats(DateUtils.addDays(newDate, -1));


        //电池租金数量
        assertEquals(9, jdbcTemplate.queryForInt("select battery_rent_count from hdg_agent_material_day_stats where agent_id = ?  and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //电池租金金额
        assertEquals(836, jdbcTemplate.queryForInt("select battery_rent_money from hdg_agent_material_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //总金额
        assertEquals(836, jdbcTemplate.queryForInt("select money from hdg_agent_material_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //电池的租金记录数量
        assertEquals(11, jdbcTemplate.queryForInt("select count(1) from hdg_agent_battery_rent_record where agent_id = ? ", agent.getId()));
        //电池的租金记录金额
        assertEquals(1036, jdbcTemplate.queryForInt("select sum(money) from hdg_agent_battery_rent_record where agent_id = ?", agent.getId()));

        //租电相关
        //电池租金数量
        assertEquals(2, jdbcTemplate.queryForInt("select battery_rent_count from hdg_agent_material_day_stats where agent_id = ?  and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));
        //电池租金金额
        assertEquals(200, jdbcTemplate.queryForInt("select battery_rent_money from hdg_agent_material_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));
        //总金额
        assertEquals(200, jdbcTemplate.queryForInt("select money from hdg_agent_material_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

    }

    /**
     * 用户实名认证统计
     */
    @Test
    public void stats_4() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        IdCardAuthRecord idCardAuthRecord = newIdCardAuthRecord(agent.getId(), customer.getId());
        idCardAuthRecord.setCreateTime(DateUtils.addDays(new Date(), -1));
        insertIdCardAuthRecord(idCardAuthRecord);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setMobile("111");
        insertCustomer(customer1);
        IdCardAuthRecord idCardAuthRecord1 = newIdCardAuthRecord(agent.getId(), customer1.getId());
        idCardAuthRecord1.setCreateTime(DateUtils.addDays(new Date(), -1));
        insertIdCardAuthRecord(idCardAuthRecord1);

        //今天统计昨天数据
        agentPayService.stats(DateUtils.addDays(new Date(), -1));

        //认证用户数量
        assertEquals(2, jdbcTemplate.queryForInt("select id_card_auth_count from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //认证用户金额
        assertEquals(200, jdbcTemplate.queryForInt("select id_card_auth_money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));
        //总金额
        assertEquals(200, jdbcTemplate.queryForInt("select money from hdg_agent_material_day_stats where agent_id = ?", agent.getId()));

    }


}
