package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleServiceTest extends BaseJunit4Test {
    @Autowired
    VehicleService service;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(), ConstEnum.Flag.TRUE.getValue());
        insertVehicle(vehicle);
        assertNotNull(service.find(vehicle.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertVehicle(vehicle);
        assertEquals(1, service.findPage(vehicle).getTotalItems());
    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        assertTrue(service.create(vehicle).isSuccess());
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertVehicle(vehicle);
        assertTrue(service.update(vehicle).isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertVehicle(vehicle);
        assertTrue(service.delete(vehicle.getId()).isSuccess());
    }

    @Test
    public void clearUpLineTime() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Vehicle vehicle = newVehicle(agent.getId(),ConstEnum.Flag.TRUE.getValue());
        insertVehicle(vehicle);
        assertEquals(1, service.clearUpLineTime(vehicle.getId()));
    }
}
