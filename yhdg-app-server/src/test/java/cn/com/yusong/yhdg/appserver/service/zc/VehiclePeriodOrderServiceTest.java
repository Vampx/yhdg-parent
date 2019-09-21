package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zc.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VehiclePeriodOrderServiceTest extends BaseJunit4Test {



	@Autowired
	VehiclePeriodOrderService vehiclePeriodOrderService;

	@Test
	public void find() {
		Partner partner = newPartner();
		insertPartner(partner);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		assertNotNull(vehiclePeriodOrderService.find(vehiclePeriodOrder.getId()));
	}

	@Test
	public void payByThird_1() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
		insertCustomerCouponTicketGift(customerCouponTicketGift);

		//押金券
		CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
		foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(foregiftTicket);
		//租金券
		CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
		rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(rentTicket);
		//抵扣券
		CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
		deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(deductionTicket);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		priceSetting.setCategory(2);
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		RestResult restResult = vehiclePeriodOrderService.payByThird(customer.getId(),0,rentPrice.getId(),rentPrice.getRentPrice(),
				rentTicket.getId(), ConstEnum.PayType.WEIXIN);
		assertEquals(0, restResult.getCode());

		//判断返回微信订单
		WeixinPayOrder weixinPayOrder = (WeixinPayOrder) restResult.getData();
		assertNotNull(weixinPayOrder);

		assertEquals(10000L, jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
		assertEquals((int)rentPrice.getVehicleRentPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_period_order where customer_id = ?", customer.getId()));
		assertEquals((rentPrice.getRentPrice()-rentTicket.getMoney()), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));
		assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));
	}


	/**
	 * 押金余额支付(使用优惠券购买套餐)
	 */
	@Test
	public void payBalance_1() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
		insertCustomerCouponTicketGift(customerCouponTicketGift);

		//押金券
		CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
		foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(foregiftTicket);
		//租金券
		CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
		rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(rentTicket);
		//抵扣券
		CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
		deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(deductionTicket);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		RestResult restResult = vehiclePeriodOrderService.payBalance(customer.getId(),0,rentPrice.getId(),rentPrice.getRentPrice(),
				rentTicket.getId());
		assertEquals(0, restResult.getCode());

		assertEquals(10000L- (rentPrice.getRentPrice()-rentTicket.getMoney()), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
		assertEquals((int)rentPrice.getVehicleRentPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_period_order where customer_id = ?", customer.getId()));
		assertEquals(rentPrice.getRentPrice()-rentTicket.getMoney(), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));
		assertEquals(CustomerCouponTicket.Status.USED.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));

	}

	/**
	 * 押金余额支付(不使用优惠券购买套餐)
	 */
	@Test
	public void payBalance_2() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
		insertCustomerCouponTicketGift(customerCouponTicketGift);

		//押金券
		CustomerCouponTicket foregiftTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		foregiftTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
		foregiftTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(foregiftTicket);
		//租金券
		CustomerCouponTicket rentTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		rentTicket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
		rentTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(rentTicket);
		//抵扣券
		CustomerCouponTicket deductionTicket = newCustomerCouponTicket(partner.getId(), agent.getId(), customer.getMobile());
		deductionTicket.setTicketType(CustomerCouponTicket.TicketType.DEDUCTION.getValue());
		deductionTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
		insertCustomerCouponTicket(deductionTicket);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		priceSetting.setCategory(2);
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		RestResult restResult = vehiclePeriodOrderService.payBalance(customer.getId(),0,rentPrice.getId(),rentPrice.getRentPrice(),
				0);
		assertEquals(0, restResult.getCode());

		assertEquals(10000L- rentPrice.getRentPrice(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", customer.getId()));
		assertEquals((int)rentPrice.getVehicleRentPrice(), jdbcTemplate.queryForInt("select money from zc_vehicle_period_order where customer_id = ?", customer.getId()));
		assertEquals((int)rentPrice.getRentPrice(), jdbcTemplate.queryForInt("select money from zc_group_order where customer_id = ?", customer.getId()));
		assertEquals(CustomerCouponTicket.Status.NOT_USER.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_coupon_ticket where id = ?", rentTicket.getId()));

	}

	@Test
	public void findLastEndTime() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);
		VehiclePeriodOrder vehiclePeriodOrder =newVehiclePeriodOrder(partner.getId(),customer.getId());
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.USED.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);
		assertNotNull(vehiclePeriodOrderService.findLastEndTime(customer.getId()));

	}

	@Test
	public void findListByNoUsed() throws Exception {

		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);
		VehiclePeriodOrder vehiclePeriodOrder =newVehiclePeriodOrder(partner.getId(),customer.getId());
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_USE.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);
		assertNotNull(vehiclePeriodOrderService.findListByNoUsed(customer.getId()));
	}

	@Test
	public void payByMulti() {
		Partner partner = newPartner();
		insertPartner(partner);
		Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		customer.setBatteryType(1);
		customer.setBalance(10000);
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);
		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), battery.getType());
		insertAgentBatteryType(agentBatteryType);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		vehiclePeriodOrderService.payByMulti(customer.getId(), 0, rentPrice.getId(), rentPrice.getRentPrice(), 0);
		assertEquals(GroupOrder.Status.WAIT_PAY.getValue(), jdbcTemplate.queryForInt("select status from zc_group_order where customer_id = ?", customer.getId()));

	}
}
