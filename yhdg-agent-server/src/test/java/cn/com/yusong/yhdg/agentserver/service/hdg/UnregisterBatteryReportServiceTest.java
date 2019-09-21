package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReport;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

import static org.junit.Assert.*;

public class UnregisterBatteryReportServiceTest extends BaseJunit4Test {
    @Autowired
    private UnregisterBatteryReportService service;
    UnregisterBatteryReport report;

    @Before
    public void setUp() throws Exception {
        Date date = new Date();
        String suffix = DateFormatUtils.format(date, "yyyyww");
        String tableName = "hdg_unregister_battery_report_" + suffix;

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        dropTable(historyTemplate, tableName);
        createTable(historyTemplate, tableName);
        report = newUnregisterBatteryReport(battery.getId());
    }

    @Test
    public void findPage() {
        insertUnregisterBatteryReport(report);

        assertTrue(1 == service.findPage(report).getTotalItems());
        assertTrue(1 == service.findPage(report).getResult().size());
    }

    @Test
    public void find() {
        assertNotNull(service.find(report.getCode(), new Date()));
    }

    private int dropTable(JdbcTemplate jdbcTemplate, String tableName) {
        StringBuffer sb = new StringBuffer("");
        sb.append("DROP TABLE if exists " + tableName);
        try {
            jdbcTemplate.update(sb.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int createTable(JdbcTemplate jdbcTemplate, String tableName) {
        String sql = "create table if not exists " + tableName + " (\n" +
                "            code varchar(40) not null,\n" +
                "            heart_type tinyint,/*心跳类型*/\n" +
                "            sim_code varchar(40), /*sim卡号*/\n" +
                "            version varchar(40),/*版本*/\n" +
                "            loc_type tinyint,/*位置类型（1.GPS 2.CELL*/\n" +
                "            current_signal smallint, /*当前信号*/\n" +
                "            signal_type tinyint, /*信号类型 0:2g 1:3G 2:4G 3:NBIot*/\n" +
                "            lng double, /*经度*/\n" +
                "            lat double, /*纬度*/\n" +
                "            voltage int, /*电压*/\n" +
                "            electricity int, /*电流*/\n" +
                "            serials smallint,\n" +
                "            single_voltage varchar(100), /*电池每串单体电压*/\n" +
                "            balance tinyint, /*电芯的均衡状态0关闭,1开启*/\n" +
                "            temp varchar(100), /*温度*/\n" +
                "            current_capacity int, /*当前容量*/\n" +
                "            volume tinyint,/*当前电量*/\n" +
                "            circle int,/*循环次数*/\n" +
                "            mos tinyint,\n" +
                "            fault int,\n" +
                "            heart_interval smallint, /*心跳间隔*/\n" +
                "            is_motion tinyint,\n" +
                "            uncap_state tinyint,/*开盖状态*/\n" +
                "            energy_state tinyint,/*工作状态*/\n" +
                "            protect varchar(100), /*保护次数*/\n" +
                "            cell_model varchar(40),/*电池型号*/\n" +
                "            cell_mfr varchar(40),/*电芯厂家*/\n" +
                "            batt_mfr varchar(40),/*电池厂家*/\n" +
                "            mfd varchar(40),/*生产日期*/\n" +
                "            bms_model varchar(40),/*BMS型号*/\n" +
                "            material tinyint, /*材质1.三元,2.磷酸铁锂,3.铅酸*/\n" +
                "            batt_type tinyint,/*电池类型*/\n" +
                "            nominal_capacity int, /*标准容量*/\n" +
                "            circle_capacity int,/*循环容量*/\n" +
                "            cell_full_vol int,\n" +
                "            cell_cut_vol int,\n" +
                "            self_dsg_rate int,\n" +
                "            ocv_table varchar(150), /*路电压值*/\n" +
                "            cell_ov_trip int,\n" +
                "            cell_ov_resume int,\n" +
                "            cell_ov_delay int,\n" +
                "            cell_uv_trip int,\n" +
                "            cell_uv_resume int,\n" +
                "            cell_uv_delay int,\n" +
                "            pack_ov_trip int,\n" +
                "            pack_ov_resume int,\n" +
                "            pack_ov_delay int,\n" +
                "            pack_uv_trip int,\n" +
                "            pack_uv_resume int,\n" +
                "            pack_uv_delay int,\n" +
                "            bas_ot_trip int,\n" +
                "            bas_ot_resume int,\n" +
                "            bas_ot_delay int,\n" +
                "            bas_ut_trip int,\n" +
                "            bas_ut_resume int,\n" +
                "            bas_ut_delay int,\n" +
                "            dsg_ot_trip int,\n" +
                "            dsg_ot_resume int,\n" +
                "            dsg_ot_delay int,\n" +
                "            dsg_ut_trip int,\n" +
                "            dsg_ut_resume int,\n" +
                "            dsg_ut_delay int,\n" +
                "            bas_oc_trip int,\n" +
                "            bas_oc_delay int,\n" +
                "            bas_oc_release int,\n" +
                "            dsg_oc_trip int,\n" +
                "            dsg_oc_delay int,\n" +
                "            dsg_oc_release int,\n" +
                "            rsns int,\n" +
                "            hard_oc_trip int,\n" +
                "            hard_oc_delay int,\n" +
                "            sc_trip int,\n" +
                "            sc_delay int,\n" +
                "            hard_ov_trip int,\n" +
                "            hard_ov_delay int,\n" +
                "            hard_uv_trip int,\n" +
                "            hard_uv_delay int,\n" +
                "            sd_release int,\n" +
                "            function int,\n" +
                "            ntc_config int,\n" +
                "            sample_r int,\n" +
                "            std_interval smallint, /*待机时心跳间隔*/\n" +
                "            total_capacity int, /*总容量*/\n" +
                "            create_time datetime not null,/*创建时间*/\n" +
                "            primary key(code, create_time)\n" +
                "        ) engine=innodb default charset=utf8;";
        try {
            jdbcTemplate.update(sql);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}