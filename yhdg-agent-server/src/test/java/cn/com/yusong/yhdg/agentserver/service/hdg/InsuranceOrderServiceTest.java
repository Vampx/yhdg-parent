package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InsuranceOrderServiceTest extends BaseJunit4Test {
    @Autowired
    private InsuranceOrderService service;

    private InsuranceOrder insuranceOrder;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setAgentId(agent.getId());
        insuranceOrder.setBatteryType(agentBatteryType.getBatteryType());
    }

    @Test
    public void find() {
        insertInsuranceOrder(insuranceOrder);

        assertNotNull(service.find(insuranceOrder.getId()));
    }

    @Test
    public void findPage() {
        insertInsuranceOrder(insuranceOrder);

        assertTrue(1 == service.findPage(insuranceOrder).getTotalItems());
        assertTrue(1 == service.findPage(insuranceOrder).getResult().size());
    }

    @Test
    public void findCanRefundByCustomerId() {
        insertInsuranceOrder(insuranceOrder);

        assertTrue(1 == service.findCanRefundByCustomerId(insuranceOrder.getCustomerId()).size());
    }
}