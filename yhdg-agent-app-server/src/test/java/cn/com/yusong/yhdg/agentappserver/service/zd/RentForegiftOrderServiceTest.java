package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RentForegiftOrderServiceTest extends BaseJunit4Test {
    @Autowired
    RentForegiftOrderService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        assertNotNull(service.find(rentForegiftOrder.getId()));
    }
}
