package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class BatteryTypeIncomeRatioServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryTypeIncomeRatioService service;

    private BatteryTypeIncomeRatio ratio;

    @Before
    public void before() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        ratio = newBatteryTypeIncomeRatio(agent.getId(), systemBatteryType.getId());
        ratio.setRentPeriodMoney(100);
        ratio.setRentPeriodType(1);
        Date newDate = DateUtils.parseDate("2019-05-29 10:13:00", new String[]{Constant.DATE_TIME_FORMAT});
        ratio.setRentExpireTime(DateUtils.addDays(newDate, -2));
    }

    @Test
    public void find() {
        insertBatteryTypeIncomeRatio(ratio);

        assertNotNull(service.find(ratio.getId()));
    }

    @Test
    public void findByBatteryType() {
        insertBatteryTypeIncomeRatio(ratio);

        assertNotNull(service.findByBatteryType(ratio.getBatteryType(), ratio.getAgentId()));
    }

    @Test
    public void findPage() {
        insertBatteryTypeIncomeRatio(ratio);

        assertTrue(1 == service.findPage(ratio).getTotalItems());
        assertTrue(1 == service.findPage(ratio).getResult().size());
    }

    @Test
    public void create() {
        assertTrue(service.create(ratio).isSuccess());
        assertEquals(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hdg_battery_type_income_ratio", Integer.class).intValue(), 1);
    }

    @Test
    public void update() {
        insertBatteryTypeIncomeRatio(ratio);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        ratio.setBatteryType(systemBatteryType.getId());
        assertTrue(service.update(ratio).isSuccess());
        int type = systemBatteryType.getId();
        assertEquals(jdbcTemplate.queryForObject("select battery_type from hdg_battery_type_income_ratio", Integer.class).intValue(), type);
    }

    @Test
    public void delete() {
        insertBatteryTypeIncomeRatio(ratio);

        assertEquals(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hdg_battery_type_income_ratio", Integer.class).intValue(), 1);

        assertTrue(service.delete(ratio.getId()).isSuccess());

        assertEquals(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hdg_battery_type_income_ratio", Integer.class).intValue(), 0);
    }
}