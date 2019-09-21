package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.*;

public class BatteryUpgradePackServiceTest extends BaseJunit4Test {
    @Autowired
    private BatteryUpgradePackService service;

    private BatteryUpgradePack batteryUpgradePack;

    @Before
    public void setUp() throws Exception {
        batteryUpgradePack = newBatteryUpgradePack();

    }

    @Test
    public void find() {
        insertBatteryUpgradePack(batteryUpgradePack);

        assertNotNull(service.find(batteryUpgradePack.getId()));
    }

    @Test
    public void findPage() {
        insertBatteryUpgradePack(batteryUpgradePack);

        assertTrue(1 == service.findPage(batteryUpgradePack).getTotalItems());
        assertTrue(1 == service.findPage(batteryUpgradePack).getResult().size());
    }

    @Test
    public void insert() {
        assertTrue(service.insert(batteryUpgradePack).isSuccess());

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_battery_upgrade_pack where id = ?", batteryUpgradePack.getId()));
    }

    @Test
    public void update() throws IOException {
        insertBatteryUpgradePack(batteryUpgradePack);

        batteryUpgradePack.setMemo("updateMemo");
        assertTrue(service.update(batteryUpgradePack).isSuccess());

        assertEquals("updateMemo", jdbcTemplate.queryForObject("select memo from bas_battery_upgrade_pack where id = " + batteryUpgradePack.getId(), String.class));
    }

    @Test
    public void delete() {
        insertBatteryUpgradePack(batteryUpgradePack);

        assertTrue(service.delete(batteryUpgradePack.getId()).isSuccess());

        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_battery_upgrade_pack where id = ?", batteryUpgradePack.getId()));
    }
}