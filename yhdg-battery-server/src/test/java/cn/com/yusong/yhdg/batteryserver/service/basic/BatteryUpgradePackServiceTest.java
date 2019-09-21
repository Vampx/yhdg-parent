package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryUpgradePackServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryUpgradePackService batteryUpgradePackService;

    @Test
    public void find() {
        BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
        insertBatteryUpgradePack(batteryUpgradePack);

        assertNotNull(batteryUpgradePackService.find(batteryUpgradePack.getId()));
    }

    @Test
    public void findByOldVersion() {
        BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
        batteryUpgradePack.setOldVersion("1");
        insertBatteryUpgradePack(batteryUpgradePack);

        assertEquals(1, batteryUpgradePackService.findByOldVersion(null, "1").size());
    }
}