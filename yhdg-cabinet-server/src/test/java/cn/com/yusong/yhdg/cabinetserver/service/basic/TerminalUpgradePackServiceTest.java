package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chen on 2017/5/17.
 */
public class TerminalUpgradePackServiceTest extends BaseJunit4Test {
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;

    @Test
    public void findByOldVersion() {
        TerminalUpgradePack terminalUpgradePack = newTerminalUpgradePack();
        insertTerminalUpgradePack(terminalUpgradePack);
        assertNotNull(terminalUpgradePackService.findByOldVersion(terminalUpgradePack.getPackType(), terminalUpgradePack.getOldVersion()));
    }
}
