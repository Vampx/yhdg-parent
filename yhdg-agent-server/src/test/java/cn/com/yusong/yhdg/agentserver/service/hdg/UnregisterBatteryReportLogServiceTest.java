package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class UnregisterBatteryReportLogServiceTest extends BaseJunit4Test {
    @Autowired
    UnregisterBatteryReportLogService service;
    DriverManagerDataSource dataSource;

    Date date = new Date();
    final String suffix = DateFormatUtils.format(date, "yyyyww");

    @Test
    public void findPage() {
        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate, suffix);
        createTable(testJdbcTemplate, suffix);

        UnregisterBatteryReportLog reportLog = newUnregisterBatteryReportLog();
        reportLog.setQueryLogTime(date);
        insertUnregisterBatteryReportLog(reportLog, suffix);

        assertTrue(1 == service.findPage(reportLog).getTotalItems());
        assertTrue(1 == service.findPage(reportLog).getResult().size());
    }

    @Test
    public void find() {
        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate, suffix);
        createTable(testJdbcTemplate, suffix);

        UnregisterBatteryReportLog reportLog = newUnregisterBatteryReportLog();
        reportLog.setQueryLogTime(date);
        insertUnregisterBatteryReportLog(reportLog, suffix);

        assertNotNull(service.find(reportLog.getCode(), date));
    }

    public JdbcTemplate getJdbcTemplate() {
        //设置数据库信息
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/yhdg_history_test");
        dataSource.setUser("root");
        dataSource.setPassword("root");

        return jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private int dropTable(JdbcTemplate jdbcTemplate, String suffix) {
        String tableName = "hdg_unregister_battery_report_log_" + suffix;
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

    public int createTable(JdbcTemplate jdbcTemplate, String suffix) {
        String tableName = "hdg_unregister_battery_report_log_" + suffix;

        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TABLE if not exists `" + tableName + "` (");
        sb.append("code char(24) not null,");
        sb.append("version char(4),");
        sb.append("voltage smallint,");
        sb.append("electricity smallint,");
        sb.append("current_capacity smallint(6),");
        sb.append("total_capacity smallint,");
        sb.append("use_count int,");
        sb.append("produce_date datetime,");
        sb.append("protect_state tinyint,");
        sb.append("fet_status tinyint,");
        sb.append("strand tinyint,");
        sb.append("temp varchar(40),");
        sb.append("lng double,");
        sb.append("lat double,");
        sb.append("fet tinyint,");
        sb.append("position_state tinyint,");
        sb.append("current_signal smallint,");
        sb.append("sim_code varchar(40),");
        sb.append("single_voltage varchar(100),");
        sb.append("volume tinyint,");
        sb.append("create_time datetime not null,");
        sb.append("primary key(code, create_time)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        try {
            jdbcTemplate.update(sb.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
