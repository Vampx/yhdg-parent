package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class BatteryOrderAllotServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryOrderAllotService batteryOrderAllotService;
    DriverManagerDataSource dataSource;

    final String suffix = DateFormatUtils.format(new Date(), "yyyyww");

    @Test
    public void exist() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate, suffix);
        createTable(testJdbcTemplate, suffix);

        BatteryOrderAllot batteryOrderAllot = newBatteryOrderAllot(partner.getId(), agent.getId(), cabinet.getId(), shop.getId());
        insertBatteryOrderAllot(batteryOrderAllot, suffix);

        String exist = batteryOrderAllotService.exist(suffix);
        assertNotNull(exist);
    }

    @Test
    public void findShopMonthIncome() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate, suffix);
        createTable(testJdbcTemplate, suffix);

        BatteryOrderAllot batteryOrderAllot = newBatteryOrderAllot(partner.getId(), agent.getId(), cabinet.getId(), shop.getId());
        batteryOrderAllot.setOrgType(IncomeRatioHistory.OrgType.SHOP.getValue());
        batteryOrderAllot.setServiceType(BatteryOrderAllot.ServiceType.INCOME.getValue());
        batteryOrderAllot.setStatsDate("2019-06-23");
        insertBatteryOrderAllot(batteryOrderAllot, suffix);

        Date beginDate = DateUtils.parseDate("2019-06", new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        List<BatteryOrderAllot> shopDayIncome = batteryOrderAllotService.findShopMonthIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shop.getId(), BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix, beginDate, endDate);

        assertEquals(1, shopDayIncome.size());
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
        String tableName = "hdg_battery_order_allot_" + suffix;
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
        String tableName = "hdg_battery_order_allot_" + suffix;

        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TABLE if not exists `" + tableName + "` (");
        sb.append("id bigint not null auto_increment,");
        sb.append("partner_id int unsigned not null,");
        sb.append("agent_id int unsigned not null,");
        sb.append("order_id char(24) not null,");
        sb.append("customer_name varchar(40),");
        sb.append("customer_mobile char(11),");
        sb.append("cabinet_id char(40),");
        sb.append("cabinet_name varchar(40),");
        sb.append("order_money int not null,");
        sb.append("service_type tinyint not null,");
        sb.append("ratio tinyint not null,");
        sb.append("org_type tinyint not null,");
        sb.append("org_id int unsigned,");
        sb.append("shop_id char(40),");
        sb.append("org_name varchar(40) not null,");
        sb.append("money decimal(11, 2) not null,");
        sb.append("stats_date char(10) not null,");
        sb.append("create_time datetime not null,");
        sb.append("index org_id (org_type, org_id, stats_date, money),");
        sb.append("index cabinet_id (org_type, org_id, stats_date, cabinet_id, customer_mobile),");
        sb.append("index customer_mobile (org_type, org_id, stats_date, customer_mobile),");
        sb.append("primary key (id)");
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