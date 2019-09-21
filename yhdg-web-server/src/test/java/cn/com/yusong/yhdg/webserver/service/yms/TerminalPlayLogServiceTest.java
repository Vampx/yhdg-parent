package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalPlayLog;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.entity.Strategy;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalPlayLogMapper;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Calendar;
import java.util.Date;

public class TerminalPlayLogServiceTest extends BaseJunit4Test {
	@Autowired
	TerminalPlayLogService service;
	DriverManagerDataSource dataSource;

	Date date = new Date();
	final String suffix = DateFormatUtils.format(date, "yyyyww");

	@Test
	public void find() {
		JdbcTemplate testJdbcTemplate = jdbcTemplate;
		dropTable(testJdbcTemplate, suffix);
		createTable(testJdbcTemplate, suffix);

		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);
		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);
		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		TerminalPlayLog terminalPlayLog = newTerminalPlayLog(terminal.getId(),agent.getId(),suffix);
		insertTerminalPlayLog(terminalPlayLog,suffix);

		assertNotNull(service.find(terminalPlayLog.getId(), suffix));
	}

	@Test
	public void findPage() {
		JdbcTemplate testJdbcTemplate = jdbcTemplate;
		dropTable(testJdbcTemplate, suffix);
		createTable(testJdbcTemplate, suffix);

		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);
		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);
		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		TerminalPlayLog terminalPlayLog = newTerminalPlayLog(terminal.getId(),agent.getId(),suffix);
		insertTerminalPlayLog(terminalPlayLog,suffix);

		assertTrue(1 == service.findPage(terminalPlayLog).getTotalItems());
		assertTrue(1 == service.findPage(terminalPlayLog).getResult().size());
	}

	@Test
	public void insert() {
		JdbcTemplate testJdbcTemplate = jdbcTemplate;
		dropTable(testJdbcTemplate, suffix);
		createTable(testJdbcTemplate, suffix);

		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);
		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);
		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		TerminalPlayLog terminalPlayLog = newTerminalPlayLog(terminal.getId(), agent.getId(), suffix);
		assertTrue(1 == service.insert(terminalPlayLog));
	}

	@Test
	public void createTables() {
		//方法未被使用
	}

//	public JdbcTemplate getJdbcTemplate() {
//		//设置数据库信息
//		dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClass("com.mysql.jdbc.Driver");
//		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/yhdg_test");
//		dataSource.setUser("root");
//		dataSource.setPassword("root");
//
//		return jdbcTemplate = new JdbcTemplate(dataSource);
//	}

	private void dropTable(JdbcTemplate jdbcTemplate, String suffix) {
		String tableName = "yms_terminal_play_log_" + suffix;
		StringBuffer sb = new StringBuffer("");
		sb.append("DROP TABLE if exists " + tableName);
		jdbcTemplate.update(sb.toString());
	}

	public void createTable(JdbcTemplate jdbcTemplate, String suffix) {
		String tableName = "yms_terminal_play_log_" + suffix;

		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE if not exists `" + tableName + "` (");
		sb.append("id int unsigned not null auto_increment,");
		sb.append("terminal_id char(6),");
		sb.append("area_num int unsigned,");
		sb.append("agent_id int unsigned not null,");
		sb.append("material_name varchar(40) not null,");
		sb.append("duration int unsigned not null,");
		sb.append("begin_time datetime not null,");
		sb.append("end_time datetime not null,");
		sb.append("create_time datetime not null,");
		sb.append("primary key(id)");
		sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		jdbcTemplate.update(sb.toString());
	}
}
