package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryOrderBatteryReportLogServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOrderBatteryReportLogService service;

    DriverManagerDataSource dataSource;

    Date date = new Date();
    final String suffix = DateFormatUtils.format(date, "yyyyww");
    final String tableName = "hdg_battery_order_battery_report_log_" + suffix;

    @Test
    public void findselectPage() {

        Partner partner = newPartner(); insertPartner(partner);
        Agent agent = newAgent(partner.getId()); insertAgent(agent);
        Customer customer = newCustomer(partner.getId()); insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        reportLog.setSuffix(suffix);
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        assertTrue(1 == service.findselectPage(reportLog).getTotalItems());
        assertTrue(1 == service.findselectPage(reportLog).getResult().size());
    }
    @Test
    public void findAllMap() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        Date startTime = new Date(date.getTime() - 1 * 24 * 60 * 60);
        Date endTime = new Date(date.getTime() + 1 * 24 * 60 * 60);
        assertTrue(service.findAllMap(batteryOrder.getId(), new Date(), startTime, endTime).size() > 0);
    }

    @Test
    public void findAllMapCount() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        reportLog.setSuffix(suffix);
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        assertTrue(service.findAllMapCount(reportLog.getOrderId(), reportLog.getReportTime()).isSuccess());


    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        reportLog.setSuffix(suffix);
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        assertTrue(1 == service.findPage(reportLog).getTotalItems());
        assertTrue(1 == service.findPage(reportLog).getResult().size());
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
        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TABLE if not exists `" + tableName + "` (");
        sb.append("order_id char(32) not null,");
        sb.append("report_time datetime,");
        sb.append("volume smallint(6),");
        sb.append("temp varchar(40),");
        sb.append("coordinate_type varchar(10),");
        sb.append("lng double,");
        sb.append("lat double,");
        sb.append("distance int,");
//        sb.append("power int,");
        sb.append("current_signal smallint,");
        sb.append("address varchar(40),");
        sb.append("primary key(order_id, report_time)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        try {
            jdbcTemplate.update(sb.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Test
    public void updateAddress() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER), agent.getId(), battery.getId(), customer.getId(), suffix);
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        insertBatteryOrder(batteryOrder);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryOrderBatteryReportLog reportLog = newBatteryOrderBatteryReportLog(batteryOrder.getId());
        reportLog.setSuffix(suffix);
        insertBatteryOrderBatteryReportLog(reportLog, suffix);

        assertTrue(service.updateAddress(reportLog).isSuccess());
    }
}
