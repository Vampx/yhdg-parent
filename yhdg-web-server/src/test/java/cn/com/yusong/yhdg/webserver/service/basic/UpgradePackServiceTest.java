package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UpgradePackServiceTest extends BaseJunit4Test {

    @Autowired
    UpgradePackService service;

    @Test
    public void find() {

        UpgradePack upgradePack = newUpgradePack();
        insertUpgradePack(upgradePack);

        assertNotNull("UpgradePack is null", service.find(upgradePack.getId()));
    }

    @Test
    public void findPage() {
        UpgradePack upgradePack = newUpgradePack();
        insertUpgradePack(upgradePack);

        assertEquals(1, service.findPage(upgradePack).getResult().size());
    }

    @Test
    public void findScreenPage() {
        UpgradePack upgradePack = newUpgradePack();
        insertUpgradePack(upgradePack);

        assertTrue(1 == service.findScreenPage(upgradePack).getTotalItems());
        assertTrue(1 == service.findScreenPage(upgradePack).getResult().size());
    }

    @Test
    public void update() throws Exception{
        UpgradePack upgradePack = newUpgradePack();
        upgradePack.setDescFile("E:\\my_workspace\\test_file_class\\Customer.xls");
        insertUpgradePack(upgradePack);
        upgradePack.setFileName("as3213df");

        assertTrue(service.update(upgradePack).isSuccess());
        assertEquals(service.find(upgradePack.getId()).getFileName(), upgradePack.getFileName());
    }
}
