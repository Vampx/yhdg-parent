package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerExchangeInfoServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;

    @Test
    public void updateErrorMessage() {
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

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        assertEquals(1, customerExchangeInfoService.updateErrorMessage(customerExchangeInfo.getId(), customerExchangeInfo.getErrorTime(), customerExchangeInfo.getErrorMessage()));
    }

    @Test
    public void clearErrorMessage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        assertEquals(1, customerExchangeInfoService.clearErrorMessage(customerExchangeInfo.getId()));
    }
}
