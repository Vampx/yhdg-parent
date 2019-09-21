package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SystemBatteryTypeServiceTest extends BaseJunit4Test {
    @Autowired
    private SystemBatteryTypeService service;

    @Test
    public void find() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        assertNotNull(service.find(systemBatteryType.getId()));
    }

    @Test
    public void findPage() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        assertTrue(1 == service.findPage(systemBatteryType).getTotalItems());
        assertTrue(1 == service.findPage(systemBatteryType).getResult().size());
    }

    @Test
    public void create() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();

        assertTrue(service.create(systemBatteryType).isSuccess());
        assertNotNull(service.find(systemBatteryType.getId()));
    }

    @Test
    public void update() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        systemBatteryType.setTypeName("测试的类型名称");
        assertTrue(service.update(systemBatteryType).isSuccess());
        assertEquals(systemBatteryType.getTypeName(), service.find(systemBatteryType.getId()).getTypeName());

    }

    @Test
    public void delete() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        assertTrue(service.delete(systemBatteryType.getId()).isSuccess());
        assertNull(service.find(systemBatteryType.getId()));
    }
}