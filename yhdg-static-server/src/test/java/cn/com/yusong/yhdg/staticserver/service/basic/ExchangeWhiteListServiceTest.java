package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ExchangeWhiteListServiceTest extends BaseJunit4Test {
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;

    @Test
    public void findByCustomer() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        ExchangeWhiteList exchangeWhiteList = newExchangeWhiteList(agent.getId(), customer.getId(), systemBatteryType.getId());
        insertExchangeWhiteList(exchangeWhiteList);

        assertNotNull(exchangeWhiteListService.findByCustomer(agent.getId(), customer.getId()));
    }
}