package cn.com.yusong.yhdg.serviceserver.service.zc;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class VehiclePeriodOrderServiceTest extends BaseJunit4Test {
    @Autowired
    VehiclePeriodOrderService vehiclePeriodOrderService;

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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
        insertCustomerVehicleInfo(customerVehicleInfo);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_USE.getValue());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        vehiclePeriodOrderService.used();

        assertEquals(VehiclePeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ? ", vehiclePeriodOrder.getId()));
        Map row = jdbcTemplate.queryForMap("select begin_time,end_time from zc_vehicle_period_order where id = '"+vehiclePeriodOrder.getId()+"'");
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

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
        insertCustomerVehicleInfo(customerVehicleInfo);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.USED.getValue());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        vehiclePeriodOrderService.expire();

        assertEquals(VehiclePeriodOrder.Status.EXPIRED.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ? ", vehiclePeriodOrder.getId()));
    }

}
