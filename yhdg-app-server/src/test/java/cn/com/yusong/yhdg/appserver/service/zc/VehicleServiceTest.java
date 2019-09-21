package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class VehicleServiceTest extends BaseJunit4Test{


    @Autowired
    VehicleService vehicleService;
    @Test
    public void findByVinNo() throws Exception {
    }


    @Test
    public void bindExchangeBattery() throws Exception {
    }

    @Test
    public void bindRentBattery() throws Exception {
    }

    @Test
    public void findTodayOrderCount() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);
        Shop shop =newShop(agent.getId());
        shop.setIsVehicle(1);
        shop.setIsExchange(1);
        shop.setIsRent(1);
        insertShop(shop);
        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);
        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery =newBattery(agent.getId(),systemBatteryType.getId());
        insertBattery(battery);

        VehicleOrder vehicleOrder = newVehicleOrder(agent.getId(),shop.getId(),vehicleModel.getId(),vehicle.getId(),customer.getId(),battery.getType());
        vehicleOrder.setPartnerId(partner.getId());
        vehicleOrder.setStatus(3);
        insertVehicleOrder(vehicleOrder);
        int todayOrderCount = vehicleService.findTodayOrderCount(customer.getId());
        assertEquals(1,todayOrderCount);

    }

    @Test
    public void find() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);
        Shop shop =newShop(agent.getId());
        shop.setIsVehicle(1);
        shop.setIsExchange(1);
        shop.setIsRent(1);
        insertShop(shop);
        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);
        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);
        assertNotNull(vehicleService.find(vehicle.getId()));
    }


    @Test
    public void bindVehicle() {

        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        customer.setBatteryType(1);
        customer.setBalance(10000);
        insertCustomer(customer);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
        customerVehicleInfo.setCategory(1);
        insertCustomerVehicleInfo(customerVehicleInfo);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
        insertRentPrice(rentPrice);

        ShopStoreVehicle shopStoreVehicle = newShopStoreVehicle(agent.getId(), shop.getId(), priceSetting.getId(), vehicle.getId());
        insertShopStoreVehicle(shopStoreVehicle);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        ShopStoreVehicleBattery shopStoreVehicleBattery = newShopStoreVehicleBattery(shopStoreVehicle.getId(), battery.getId());
        insertShopStoreVehicleBattery(shopStoreVehicleBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder.setEndTime(new Date());
        insertPacketPeriodOrder(packetPeriodOrder);

        VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.PAY_OK.getValue());
        vehicleForegiftOrder.setModelId(vehicle.getModelId());
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_USE.getValue());
        vehiclePeriodOrder.setModelId(vehicle.getModelId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
        groupOrder.setStatus(GroupOrder.Status.PAY_OK.getValue());
        groupOrder.setModelId(vehicle.getModelId());
        insertGroupOrder(groupOrder);

        vehicleService.bindVehicle(agent, customer, shop, vehicle, customerVehicleInfo, shopStoreVehicle);

        assertEquals(Vehicle.Status.IN_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle where id = ?", vehicle.getId()));
        assertEquals(VehiclePeriodOrder.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));

    }

}