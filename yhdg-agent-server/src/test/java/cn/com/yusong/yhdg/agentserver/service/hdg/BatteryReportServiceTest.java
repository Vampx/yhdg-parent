package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryReportServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryReportService service;
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
		String tableName = "hdg_battery_report_" + suffix;

		BatteryReport batteryReport = newBatteryReport(battery.getId(), suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReport(batteryReport, suffix);
		assertNotNull(service.find(batteryReport.getBatteryId(), date));
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
		String tableName = "hdg_battery_report_" + suffix;

		BatteryReport batteryReport = newBatteryReport(battery.getId(), suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReport(batteryReport, suffix);
		assertTrue(1 == service.findPage(batteryReport).getTotalItems());
		assertTrue(1 == service.findPage(batteryReport).getResult().size());
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
		String tableName = "hdg_battery_report_" + suffix;

		BatteryReport batteryReport = newBatteryReport(battery.getId(), suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReport(batteryReport, suffix);
		assertTrue(1 == service.findForExcel(batteryReport).size());
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
		String tableName = "hdg_battery_report_" + suffix;

		BatteryReport batteryReport = newBatteryReport(battery.getId(), suffix);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		insertBatteryReport(batteryReport, suffix);
		assertTrue(1 == service.findList(batteryReport.getBatteryId(), batteryReport.getCreateTime()).size());

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
		sb.append("battery_id char(12) not null,");
		sb.append("code varchar(40) not null,");
		sb.append("heart_type tinyint(4),");
		sb.append("version varchar(40),");
		sb.append("loc_type tinyint(4),");
		sb.append("current_signal smallint(6),");
		sb.append("signal_type tinyint(4),");
		sb.append("lng double,");
		sb.append("lat double,");
		sb.append("voltage int(11),");
		sb.append("electricity int(11),");
		sb.append("serials smallint(6),");
		sb.append("single_voltage varchar(100),");
		sb.append("balance tinyint(4),");
		sb.append("temp varchar(100),");
		sb.append("current_capacity int(11),");
		sb.append("volume tinyint(4),");
		sb.append("circle int(11),");
		sb.append("mos tinyint(4),");
		sb.append("fault int(11),");
		sb.append("heart_interval smallint(6),");
		sb.append("is_motion tinyint(4),");
		sb.append("uncap_state tinyint(4),");
		sb.append("protect varchar(100),");
		sb.append("cell_model varchar(40),");
		sb.append("cell_mfr varchar(40),");
		sb.append("batt_mfr varchar(40),");
		sb.append("mfd varchar(40),");
		sb.append("bms_model varchar(40),");
		sb.append("material tinyint(4),");
		sb.append("batt_type tinyint(4),");
		sb.append("nominal_capacity int(11),");
		sb.append("circle_capacity int(11),");
		sb.append("cell_full_vol int(11),");
		sb.append("cell_cut_vol int(11),");
		sb.append("self_dsg_rate int(11),");
		sb.append("ocv_table varchar(150),");
		sb.append("cell_ov_trip int(11),");
		sb.append("cell_ov_resume int(11),");
		sb.append("cell_ov_delay int(11),");
		sb.append("cell_uv_trip int(11),");
		sb.append("cell_uv_resume int(11),");
		sb.append("cell_uv_delay int(11),");
		sb.append("pack_ov_trip int(11),");
		sb.append("pack_ov_resume int(11),");
		sb.append("pack_ov_delay int(11),");
		sb.append("pack_uv_trip int(11),");
		sb.append("pack_uv_resume int(11),");
		sb.append("pack_uv_delay int(11),");
		sb.append("dsg_ot_trip int(11),");
		sb.append("dsg_ot_resume int(11),");
		sb.append("dsg_ot_delay int(11),");
		sb.append("dsg_ut_trip int(11),");
		sb.append("dsg_ut_resume int(11),");
		sb.append("dsg_ut_delay int(11),");
		sb.append("dsg_oc_trip int(11),");
		sb.append("dsg_oc_delay int(11),");
		sb.append("dsg_oc_release int(11),");
		sb.append("rsns int(11),");
		sb.append("hard_oc_trip int(11),");
		sb.append("hard_oc_delay int(11),");
		sb.append("sc_trip int(11),");
		sb.append("sc_delay int(11),");
		sb.append("hard_ov_trip int(11),");
		sb.append("hard_ov_delay int(11),");
		sb.append("hard_uv_trip int(11),");
		sb.append("hard_uv_delay int(11),");
		sb.append("sd_release int(11),");
		sb.append("function int(11),");
		sb.append("ntc_config int(11),");
		sb.append("sample_r int(11),");
		sb.append("total_capacity int(11),");
		sb.append("create_time datetime NOT NULL,");
		sb.append("PRIMARY KEY (`battery_id`, `create_time`)");
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
