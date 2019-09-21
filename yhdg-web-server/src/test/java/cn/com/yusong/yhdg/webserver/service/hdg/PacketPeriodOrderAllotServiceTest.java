package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.util.Date;

public class PacketPeriodOrderAllotServiceTest extends BaseJunit4Test {
	@Autowired
	PacketPeriodOrderAllotService service;
	DriverManagerDataSource dataSource;

	@Test
	public void findPageByRecord() throws ParseException {

		Date date = new Date();
		String suffix= DateFormatUtils.format(date, "yyyyww");
		String tableName = "hdg_packet_period_order_allot_" + suffix;

		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		Playlist playlist = newPlaylist(agent.getId());
		insertPlaylist(playlist);

		TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
		insertTerminalStrategy(terminalStrategy);

		Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
		insertTerminal(terminal);

		Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
		insertCabinet(cabinet);

		BalanceRecord balanceRecord = newBalanceRecord(partner.getId(), agent.getId(), shop.getId(), "adsf");
		Date now = new Date();
		String today = DateFormatUtils.format(now, Constant.DATE_FORMAT);
		balanceRecord.setBalanceDate(today);
		balanceRecord.setBizType(BalanceRecord.BizType.AGENT.getValue());
		insertBalanceRecord(balanceRecord);


		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate,tableName);
		createTable(testJdbcTemplate,tableName);

		PacketPeriodOrderAllot packetPeriodOrderAllot = newPacketPeriodOrderAllot(partner.getId(), agent.getId(), cabinet.getId(), shop.getId(), suffix);

		packetPeriodOrderAllot.setOrgId(balanceRecord.getAgentId());
		packetPeriodOrderAllot.setSuffix(suffix);
		packetPeriodOrderAllot.setStatsDate(today);
		insertPacketPeriodOrderAllot(packetPeriodOrderAllot);

		assertTrue(1 == service.findPageByRecord(balanceRecord).getTotalItems());
		assertTrue(1 == service.findPageByRecord(balanceRecord).getResult().size());
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
		sb.append("id bigint not null auto_increment,");
		sb.append("partner_id int unsigned not null,");
		sb.append("agent_id int unsigned not null,");
		sb.append("cabinet_id char(40),");
		sb.append("order_id char(24) not null,");
		sb.append("customer_name varchar(40),");
		sb.append("customer_mobile char(11),");
		sb.append("begin_time datetime,");
		sb.append("end_time datetime,");
		sb.append("day_count int,");
		sb.append("order_money int not null,");
		sb.append("service_type tinyint not null,");
		sb.append("ratio tinyint not null,");
		sb.append("org_type tinyint not null,");
		sb.append("org_id int unsigned,");
		sb.append("shop_id char(40),");
		sb.append("org_name varchar(40) not null,");
		sb.append("platform_deduct_money int,");
		sb.append("money int not null,");
		sb.append("stats_date char(10) not null,");
		sb.append("pay_time datetime not null,");
		sb.append("create_time datetime not null,");
		sb.append("index order_id (order_id),");
		sb.append("index org_id (org_type, org_id, stats_date, money),");
		sb.append("index customer_mobile (org_type, org_id, stats_date, customer_mobile),");
		sb.append("PRIMARY KEY (`id`)");
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
