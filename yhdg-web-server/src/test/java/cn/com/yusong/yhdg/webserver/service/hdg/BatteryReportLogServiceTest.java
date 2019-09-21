package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.domain.hdg.VehicleReportLog;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryReportLogServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryReportLogService service;
	DriverManagerDataSource dataSource;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Date date = new Date();
		String suffix= DateFormatUtils.format(date, "yyyyww");
		String tableName = "hdg_battery_report_log_" + suffix;
		BatteryReportLog batteryReportLog = newBatteryReportLog(battery.getId(),date,suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReportLog(batteryReportLog,suffix);
		assertNotNull(service.find(battery.getId(),batteryReportLog.getReportTime()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Date date = new Date();
		String suffix= DateFormatUtils.format(date, "yyyyww");
		String tableName = "hdg_battery_report_log_" + suffix;
		BatteryReportLog batteryReportLog = newBatteryReportLog(battery.getId(),date,suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReportLog(batteryReportLog,suffix);

		assertTrue(1 == service.findPage(batteryReportLog).getTotalItems());
		assertTrue(1 == service.findPage(batteryReportLog).getResult().size());
	}

	@Test
	public void findForExcel() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Date date = new Date();
		String suffix= DateFormatUtils.format(date, "yyyyww");
		String tableName = "hdg_battery_report_log_" + suffix;
		BatteryReportLog batteryReportLog = newBatteryReportLog(battery.getId(),date,suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReportLog(batteryReportLog,suffix);

		assertTrue(1 == service.findForExcel(batteryReportLog).size());
	}

	@Test
	public void findList() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Date date = new Date();
		String suffix= DateFormatUtils.format(date, "yyyyww");
		String tableName = "hdg_battery_report_log_" + suffix;
		BatteryReportLog batteryReportLog = newBatteryReportLog(battery.getId(),date,suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReportLog(batteryReportLog,suffix);

		assertTrue(1 == service.findList(batteryReportLog.getBatteryId(), batteryReportLog.getReportTime()).size());
	}

	public JdbcTemplate getJdbcTemplate() {
		dataSource = new DriverManagerDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/yhdg_history_test");
		dataSource.setUser("root");
		dataSource.setPassword("root");

		return jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private int dropTable(JdbcTemplate jdbcTemplate,String tableName) {
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

	public int createTable(JdbcTemplate jdbcTemplate,String tableName) {
		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE if not exists `" + tableName + "` (");
		sb.append("battery_id varchar(12) not null,");
		sb.append("report_time datetime,");
		sb.append("voltage smallint,");
		sb.append("electricity smallint,");
		sb.append("current_capacity smallint(6),");
		sb.append("protect_state varchar(40),");
		sb.append("fet varchar(40),");
		sb.append("strand tinyint,");
		sb.append("temp varchar(40),");
		sb.append("lng double,");
		sb.append("lat double,");
		sb.append("coordinate_type varchar(10),");
		sb.append("distance int,");
		sb.append("power int,");
		sb.append("fet_status tinyint,");
		sb.append("current_signal smallint,");
		sb.append("charge_status tinyint,");
		sb.append("is_electrify tinyint,");
		sb.append("position_state tinyint,");
		sb.append("address varchar(40),");
		sb.append("sim_code varchar(40),");
		sb.append("single_voltage varchar(100),");

		sb.append("primary key (battery_id, report_time)");
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
