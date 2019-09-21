package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RentPriceServiceTest extends BaseJunit4Test {

    @Autowired
    RentPriceService rentPriceService;

    @Test
    public void find() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
        insertCustomerVehicleInfo(customerVehicleInfo);

        RentPrice rentPrice =newRentPrice(agent.getId(),customerVehicleInfo.getId());
        insertRentPrice(rentPrice);

        assertNotNull(rentPriceService.find(rentPrice.getId()));

    }

    @Test
    public void findSettingIdAll() throws Exception {
    }

}