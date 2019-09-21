package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpgradePackServiceTest extends BaseJunit4Test {
    @Autowired
    UpgradePackService upgradePackService;

    @Test
    public void find() {
        upgradePackService.find(1);
    }
}
