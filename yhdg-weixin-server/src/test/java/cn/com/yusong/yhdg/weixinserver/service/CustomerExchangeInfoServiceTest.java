package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.CustomerExchangeInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerExchangeInfoServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        customerExchangeInfo.setId(customer.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        assertNotNull(customerExchangeInfoService.find(customerExchangeInfo.getId()));
    }
}
