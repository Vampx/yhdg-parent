package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceSettingServiceTest extends BaseJunit4Test {
    @Autowired
    PriceSettingService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        assertNotNull(service.find(priceSetting.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        assertEquals(1, service.findPage(priceSetting).getTotalItems());
    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());

        assertTrue(service.create(priceSetting).isSuccess());
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        Integer batteryType = battery.getType();
        String [] priceNameList = new String[]{"xx","AS"};
        Double [] foregiftPriceList = new Double[]{1.5, 10.0};
        Double [] vehicleForegiftPriceList = new Double[]{1.1, 5.0};
        Double [] batteryForegiftPriceList = new Double[]{0.4, 5.0};
        Double [] rentPriceList = new Double[]{1.5, 11.0};
        Integer [] dayCountList = new Integer[]{1, 10};
        Double [] vehicleRentPriceList = new Double[]{1.0, 5.0};
        Double [] batteryRentPriceList = new Double[]{0.5, 6.0};
        Long [] priceIdList = new Long[]{};

        assertTrue(service.update(priceSetting, batteryType, priceNameList, foregiftPriceList,
                vehicleForegiftPriceList, batteryForegiftPriceList, rentPriceList, dayCountList,
                vehicleRentPriceList, batteryRentPriceList, priceIdList).isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        VehicleModel vehicleModel = newVehicleModel(agent.getId());
        insertVehicleModel(vehicleModel);

        Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
        insertVehicle(vehicle);

        PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
        insertPriceSetting(priceSetting);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);
        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
        insertRentPrice(rentPrice);

        assertTrue(service.delete(priceSetting.getId()).isSuccess());
    }

}
