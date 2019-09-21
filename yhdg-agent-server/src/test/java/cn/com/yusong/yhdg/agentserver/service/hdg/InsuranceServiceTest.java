package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InsuranceServiceTest extends BaseJunit4Test {
    @Autowired
    private InsuranceService service;

    Insurance insurance;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        insurance = newInsurance(agent.getId(), systemBatteryType.getId());
    }

    @Test
    public void find() {
        insertInsurance(insurance);

        assertNotNull(service.find(insurance.getId()));
    }

    @Test
    public void findListByBatteryType() {
        insertInsurance(insurance);
        assertTrue(1 == service.findListByBatteryType(insurance.getBatteryType(), insurance.getAgentId()).size());
    }

    @Test
    public void findPage() {
        insertInsurance(insurance);

        assertTrue(1 == service.findPage(insurance).getTotalItems());
        assertTrue(1 == service.findPage(insurance).getResult().size());
    }

    @Test
    public void create() {
        assertTrue(service.create(insurance).isSuccess());
        assertNotNull(service.find(insurance.getId()));
    }

    @Test
    public void update() {
        insertInsurance(insurance);

        insurance.setInsuranceName("测试的insuranceName");
        assertTrue(service.update(insurance).isSuccess());
        assertEquals(insurance.getInsuranceName(), service.find(insurance.getId()).getInsuranceName());
    }

    @Test
    public void delete() {
        insertInsurance(insurance);

        assertTrue(service.delete(insurance.getId()).isSuccess());
        assertNull(service.find(insurance.getId()));
    }
}