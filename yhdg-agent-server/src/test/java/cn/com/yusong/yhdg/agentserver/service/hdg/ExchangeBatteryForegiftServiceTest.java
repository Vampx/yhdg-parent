package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ExchangeBatteryForegiftServiceTest extends BaseJunit4Test {
    @Autowired
    private ExchangeBatteryForegiftService service;

    private ExchangeBatteryForegift exchangeBatteryForegift;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        exchangeBatteryForegift = newExchangeBatteryForegift(agent.getId(), systemBatteryType.getId());
    }

    @Test
    public void find() {
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        assertNotNull(service.find(exchangeBatteryForegift.getId()));
    }

    @Test
    public void findByBatteryType() {
        insertExchangeBatteryForegift(exchangeBatteryForegift);

        assertTrue(1 == service.findListByBatteryType(exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId()).size());
    }

    @Test
    public void findListByBatteryType() {
        insertExchangeBatteryForegift(exchangeBatteryForegift);
    }

    @Test
    public void create() {
        assertTrue(service.create(exchangeBatteryForegift).isSuccess());
        assertNotNull(service.find(exchangeBatteryForegift.getId()));
    }

    @Test
    public void update() {
        insertExchangeBatteryForegift(exchangeBatteryForegift);
        exchangeBatteryForegift.setMoney(345435);
        assertTrue(service.update(exchangeBatteryForegift).isSuccess());
        assertEquals(service.find(exchangeBatteryForegift.getId()).getMoney(), exchangeBatteryForegift.getMoney());
    }

    @Test
    public void delete() {
        insertExchangeBatteryForegift(exchangeBatteryForegift);
        assertTrue(service.delete(exchangeBatteryForegift.getId()).isSuccess());
        assertNull(service.find(exchangeBatteryForegift.getId()));
    }
}