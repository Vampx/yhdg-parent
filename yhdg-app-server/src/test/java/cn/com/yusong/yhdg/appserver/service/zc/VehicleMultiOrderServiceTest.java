package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleMultiOrderServiceTest extends BaseJunit4Test {
	@Autowired
	VehicleMultiOrderService service;

	@Test
	public void payByThird() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
		customerMultiOrder.setType(CustomerMultiOrder.Type.ZC.getValue());
		insertCustomerMultiOrder(customerMultiOrder);

		service.payByThird(customer, customerMultiOrder.getId(), 1000, ConstEnum.PayType.WEIXIN);

		assertEquals(1,jdbcTemplate.queryForInt("select count(*) from bas_weixin_pay_order where customer_id = ?", customer.getId()));
	}

	@Test
	public void payByBalance() {
		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		CustomerCouponTicket customerCouponTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		customerCouponTicket.setMoney(100);
		customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
		insertCustomerCouponTicket(customerCouponTicket);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setId("111");
		customerForegiftOrder.setMoney(1000);
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
		customerForegiftOrder.setCouponTicketId(customerCouponTicket.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
		packetPeriodOrder.setMoney(1000);
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
		packetPeriodOrder.setCouponTicketId(customerCouponTicket.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
		rentPeriodOrder.setMoney(1000);
		rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
		insertRentPeriodOrder(rentPeriodOrder);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(),priceSetting.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		vehicleForegiftOrder.setMoney(1000);
		vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.WAIT_PAY.getValue());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		vehiclePeriodOrder.setMoney(1000);
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setMoney(4000);
		groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
		insertGroupOrder(groupOrder);

		CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
		customerMultiOrder.setType(CustomerMultiOrder.Type.ZC.getValue());
		customerMultiOrder.setTotalMoney(4000);
		insertCustomerMultiOrder(customerMultiOrder);

		int num = 0;
		CustomerMultiOrderDetail customerMultiOrderDetail = newCustomerMultiOrderDetail(customerMultiOrder.getId(), num++, groupOrder.getId(), CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue(), 4000);
		insertCustomerMultiOrderDetail(customerMultiOrderDetail);

		service.payBalance(customer, customerMultiOrder.getId(), groupOrder.getMoney());

		assertEquals(CustomerMultiPayDetail.Status.HAVE_PAY.getValue(),jdbcTemplate.queryForInt("select status from bas_customer_multi_pay_detail where customer_id = ?", customer.getId()));
		assertEquals(CustomerMultiOrder.Status.HAVE_PAY.getValue(),jdbcTemplate.queryForInt("select status from bas_customer_multi_order where id = ?", customerMultiOrder.getId()));
		assertEquals(GroupOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where id = ?", groupOrder.getId()));
		assertEquals(VehiclePeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_period_order where id = ?", vehiclePeriodOrder.getId()));
		assertEquals(VehicleForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from zc_vehicle_foregift_order where id = ?", vehicleForegiftOrder.getId()));
		assertEquals(PacketPeriodOrder.Status.NOT_USE.getValue(), jdbcTemplate.queryForInt("select status from hdg_packet_period_order where id = ?", packetPeriodOrder.getId()));
		assertEquals(CustomerForegiftOrder.Status.PAY_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", customerForegiftOrder.getId()));
	}
}
