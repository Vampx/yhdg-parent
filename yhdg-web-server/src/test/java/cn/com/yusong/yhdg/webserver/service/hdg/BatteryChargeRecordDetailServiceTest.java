package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class BatteryChargeRecordDetailServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryChargeRecordDetailService service;
    DriverManagerDataSource dataSource;

    Date date = new Date();
    final String suffix = DateFormatUtils.format(date, "yyyyww");
    final String tableName = "hdg_battery_charge_record_detail_" + suffix;

    //没有此表
    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryChargeRecord chargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(chargeRecord);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryChargeRecordDetail chargeRecordDetail = newBatteryChargeRecordDetail(chargeRecord.getId(), suffix);
        insertBatteryChargeRecordDetail(chargeRecordDetail);

        assertTrue(1 == service.findPage(chargeRecordDetail).getTotalItems());
        assertTrue(1 == service.findPage(chargeRecordDetail).getResult().size());
    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

          SystemBatteryType systemBatteryType = newSystemBatteryType();
          insertSystemBatteryType(systemBatteryType);
          Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
          insertBattery(battery);

        BatteryChargeRecord chargeRecord = newBatteryChargeRecord(agent.getId(), battery.getId());
        insertBatteryChargeRecord(chargeRecord);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate);
        createTable(testJdbcTemplate);

        BatteryChargeRecordDetail chargeRecordDetail = newBatteryChargeRecordDetail(chargeRecord.getId(), suffix);
        insertBatteryChargeRecordDetail(chargeRecordDetail);

        assertNotNull(service.find(chargeRecordDetail.getId()));
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
        sb.append("id bigint unsigned not null,");
        sb.append("report_time datetime not null,");
        sb.append("current_volume int,");
        sb.append("current_power int");
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
