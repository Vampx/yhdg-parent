package cn.com.yusong.yhdg.serviceserver.service.zd;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class RentPeriodOrderServiceTest extends BaseJunit4Test {
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;

    @Test
    public void used() {
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

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerRentInfo(customerRentInfo);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        rentPeriodOrderService.used();

        assertEquals(RentPeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id = ? ", rentPeriodOrder.getId()));
        Map row = jdbcTemplate.queryForMap("select begin_time,end_time from zd_rent_period_order where id = '"+rentPeriodOrder.getId()+"'");
        assertNotNull(row.get("begin_time"));
        assertNotNull(row.get("end_time"));
    }

    @Test
    public void expire() {
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

        CustomerRentInfo customerRentInfo = newCustomerRentInfo(customer.getId(), rentForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerRentInfo(customerRentInfo);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setShopId(shop.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.USED.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        rentPeriodOrderService.expire();

        assertEquals(RentPeriodOrder.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from zd_rent_period_order where id = ? ", rentPeriodOrder.getId()));
    }
}
