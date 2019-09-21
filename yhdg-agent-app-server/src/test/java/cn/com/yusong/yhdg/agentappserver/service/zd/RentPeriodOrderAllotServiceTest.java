package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class RentPeriodOrderAllotServiceTest extends BaseJunit4Test {
    @Autowired
    private RentPeriodOrderAllotService service;

    RentPeriodOrderAllot rentPeriodOrderAllot;
    Shop shop;

    String suffix= DateFormatUtils.format(new Date(), "yyyyww");

    @Before
    public void setUp() throws Exception {
        String tableName = "zd_rent_period_order_allot_" + suffix;

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        shop = newShop(agent.getId());
        insertShop(shop);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(),terminal.getId());
        insertCabinet(cabinet);

        JdbcTemplate testJdbcTemplate = getJdbcTemplate();
        dropTable(testJdbcTemplate,tableName);
        createTable(testJdbcTemplate,tableName);

        rentPeriodOrderAllot = newRentPeriodOrderAllot(partner.getId(), agent.getId(), shop.getId(), suffix);
    }

    @Test
    public void exist() throws ParseException {
        insertRentPeriodOrderAllot(rentPeriodOrderAllot);

        String exist = service.exist(suffix);
        assertNotNull(exist);
    }

//    @Test
//    public void findShopDayIncome() throws ParseException {
//        rentPeriodOrderAllot.setOrgType(IncomeRatioHistory.OrgType.SHOP.getValue());
//        rentPeriodOrderAllot.setServiceType(RentPeriodOrderAllot.ServiceType.INCOME.getValue());
//        rentPeriodOrderAllot.setStatsDate("2019-06-23");
//        insertRentPeriodOrderAllot(rentPeriodOrderAllot);
//
//        List<RentPeriodOrderAllot> shopDayIncome = service.findShopDayIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shop.getId(), RentPeriodOrderAllot.ServiceType.INCOME.getValue(), "2019-06-23", suffix);
//
//        assertEquals(1, shopDayIncome.size());
//    }

    @Test
    public void findShopMonthIncome() throws ParseException {
        rentPeriodOrderAllot.setOrgType(IncomeRatioHistory.OrgType.SHOP.getValue());
        rentPeriodOrderAllot.setServiceType(RentPeriodOrderAllot.ServiceType.INCOME.getValue());
        rentPeriodOrderAllot.setStatsDate("2019-06-23");
        insertRentPeriodOrderAllot(rentPeriodOrderAllot);

        Date beginDate = DateUtils.parseDate("2019-06", new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        List<RentPeriodOrderAllot> shopDayIncome = service.findShopMonthIncome(IncomeRatioHistory.OrgType.SHOP.getValue(), shop.getId(), RentPeriodOrderAllot.ServiceType.INCOME.getValue(), suffix, beginDate, endDate);

        assertEquals(1, shopDayIncome.size());
    }

    public JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
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