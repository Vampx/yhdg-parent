package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RentInsuranceOrderServiceTest extends BaseJunit4Test {
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;

    @Test
    public void findByCustomerId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertRentForegiftOrder(rentForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setBatteryType(systemBatteryType.getId());
        rentInsuranceOrder.setStatus(RentInsuranceOrder.Status.PAID.getValue());
        insertRentInsuranceOrder(rentInsuranceOrder);

        assertNotNull(rentInsuranceOrderService.findByCustomerId(customer.getId(), systemBatteryType.getId(), RentInsuranceOrder.Status.PAID.getValue()));
    }
}