package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemBatteryTypeServiceTest extends BaseJunit4Test {


    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;

    @Test
    public void find() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        systemBatteryType.setId(1);
        insertSystemBatteryType(systemBatteryType);
        assertNotNull(systemBatteryTypeService.find(1));
    }

    @Test
    public void find1() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
        insertCustomerVehicleInfo(customerVehicleInfo);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        systemBatteryType.setId(customerVehicleInfo.getBatteryType());
        insertSystemBatteryType(systemBatteryType);
        assertNotNull(systemBatteryTypeService.find(1));

    }
}
