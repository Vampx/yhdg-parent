package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BatteryUpgradePackDetailServiceTest extends BaseJunit4Test {
    @Autowired
    BatteryUpgradePackDetailService batteryUpgradePackDetailService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryUpgradePack batteryUpgradePack = newBatteryUpgradePack();
        insertBatteryUpgradePack(batteryUpgradePack);

        UpgradePack upgradePack = newUpgradePack();
        insertUpgradePack(upgradePack);

        BatteryUpgradePackDetail batteryUpgradePackDetail = newBatteryUpgradePackDetail(batteryUpgradePack.getId(), battery.getId());
        insertBatteryUpgradePackDetail(batteryUpgradePackDetail);

        assertNotNull(batteryUpgradePackDetailService.find(batteryUpgradePack.getId(), battery.getId()));
    }
}