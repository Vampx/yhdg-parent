package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryOrderHistoryServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryOrderHistoryService service;
	DriverManagerDataSource dataSource;

	Date date = new Date();
	final String suffix = DateFormatUtils.format(date, "yyyyww");
	final String tableName = "hdg_battery_order_" + suffix;
	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		//这里删除了一个该表，然后又生成了这个表//这三个特殊的方法都写在下面了。
		//配置数据源信息
		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		//如果存在该表，就删除该表
		dropTable(testJdbcTemplate);
		//创建该表
		createTable(testJdbcTemplate);
		//这里要多传一个参数suffix
		BatteryOrder batteryOrder = newBatteryOrder("asdf", agent.getId(), battery.getId(), customer.getId(),suffix);
		insertBatteryOrder(batteryOrder,suffix);

		assertTrue(1 == service.findPage(batteryOrder).getTotalItems());
		assertTrue(1 == service.findPage(batteryOrder).getResult().size());
	}

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		JdbcTemplate testJdbcTemplate = getJdbcTemplate();
		dropTable(testJdbcTemplate);
		createTable(testJdbcTemplate);
		BatteryOrder batteryOrder = newBatteryOrder("asdf", agent.getId(), battery.getId(), customer.getId(),suffix);
		insertBatteryOrder(batteryOrder,suffix);

		assertNotNull(service.find(batteryOrder.getId(),suffix));
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

	private int dropTable(JdbcTemplate jdbcTemplate) {
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

	public int createTable(JdbcTemplate jdbcTemplate) {
		//这里新建表，每个表的结构是不一样的，数据库不存在这个表，则不能根据db.sql来写，所以要根据各自实体类的属性来写。
		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE if not exists `" + tableName + "` (");
		sb.append("id char(32) not null,");
		sb.append("agent_id int unsigned not null,");
		sb.append("province_id int unsigned,");
		sb.append("city_id int unsigned,");
		sb.append("district_id int unsigned,");
		sb.append("battery_id char(12) not null,");
		sb.append("take_cabinet_id char(12),");
		sb.append("take_cabinet_name varchar(40),");
		sb.append("take_box_num varchar(20),");
		sb.append("take_time datetime,");
		sb.append("init_volume tinyint,");
		sb.append("put_cabinet_id char(12),");
		sb.append("put_cabinet_name varchar(40),");
		sb.append("put_cabinet_id char(6),");
		sb.append("put_cabinet_name char(40),");
		sb.append("put_box_num varchar(20),");
		sb.append("put_time datetime,");
		sb.append("pay_time datetime,");
		sb.append("pay_type tinyint,");
		sb.append("price int,");
		sb.append("money int,");
		sb.append("current_volume tinyint,");
		sb.append("current_distance int not null,");
		sb.append("customer_id bigint unsigned not null,");
		sb.append("customer_mobile char(11) not null,");
		sb.append("customer_fullname varchar(40),");
		sb.append("order_status tinyint(4) not null,");
		sb.append("refund_status tinyint,");
		sb.append("refund_time datetime,");
		sb.append("refund_money int unsigned,");
		sb.append("refund_reason varchar(40),");
		sb.append("address varchar(120),");
		sb.append("ticket_name varchar(40),");
		sb.append("ticket_money int unsigned,");
		sb.append("coupon_ticket_id bigint unsigned,");
		sb.append("packet_period_order_id char(32),");
		sb.append("error_message varchar(40),");
		sb.append("error_time datetime,");
		sb.append("pay_timeout_fault_log_id bigint unsigned,");
		sb.append("not_take_timeout_fault_log_id bigint unsigned,");
		sb.append("create_time datetime not null");
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
