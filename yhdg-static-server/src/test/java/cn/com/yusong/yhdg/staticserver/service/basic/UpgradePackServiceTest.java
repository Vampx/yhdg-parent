package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UpgradePackServiceTest extends BaseJunit4Test {
    @Autowired
    UpgradePackService upgradePackService;

    @Test
    public void find() {
        UpgradePack upgradePack = newUpgradePack();
        insertUpgradePack(upgradePack);

        assertNotNull(upgradePackService.find(upgradePack.getId()));
    }
}