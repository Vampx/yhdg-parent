package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupServiceTest extends BaseJunit4Test {
	@Autowired
	GroupOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		//押金券
		CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		customerCouponTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
		customerCouponTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(customerCouponTicket);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setId("111");
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
		customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
		packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
		insertGroupOrder(groupOrder);

		assertNotNull(service.find(groupOrder.getId()));
	}

	@Test
	public void findListByCustomerIdAndStatus() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		//押金券
		CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		customerCouponTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
		customerCouponTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(customerCouponTicket);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setId("111");
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
		customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
		packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
		insertGroupOrder(groupOrder);


		assertTrue(1 == service.findListByCustomerIdAndStatus(customer.getId(), GroupOrder.Status.PAY_OK.getValue()).size());

	}
}
