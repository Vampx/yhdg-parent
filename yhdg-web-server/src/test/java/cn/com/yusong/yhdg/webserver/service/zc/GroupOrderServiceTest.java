package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import com.alipay.api.AlipayApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupOrderServiceTest extends BaseJunit4Test {
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

		RentPrice rentPrice = newRentPrice(agent.getId(), partner.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), agentBatteryType.getBatteryType(), agent.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		insertGroupOrder(groupOrder);

		assertNotNull(service.find(groupOrder.getId()));
	}

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		RentPrice rentPrice = newRentPrice(agent.getId(), partner.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), agentBatteryType.getBatteryType(), agent.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		insertGroupOrder(groupOrder);

		assertTrue(1 == service.findPage(groupOrder).getTotalItems());
		assertTrue(1 == service.findPage(groupOrder).getResult().size());
	}

	@Test
	public void refund() throws AlipayApiException {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		RentPrice rentPrice = newRentPrice(agent.getId(), partner.getId());
		insertRentPrice(rentPrice);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		vehicleForegiftOrder.setMoney(1000);
		vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.APPLY_REFUND.getValue());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		vehiclePeriodOrder.setMoney(1000);
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.APPLY_REFUND.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setMoney(1000);
		customerForegiftOrder.setDeductionTicketMoney(0);
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), agentBatteryType.getBatteryType(), agent.getId());
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.APPLY_REFUND.getValue());
		packetPeriodOrder.setMoney(1000);
		insertPacketPeriodOrder(packetPeriodOrder);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setStatus(GroupOrder.Status.APPLY_REFUND.getValue());
		insertGroupOrder(groupOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), groupOrder.getId(), CustomerRefundRecord.SourceType.ZCGROUP.getValue());
		customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		int refundType = 1;
		int sourceType = customerRefundRecord.getSourceType();
		String sourceId = customerRefundRecord.getSourceId();
		int refundMoney = 1000;
		int refundableMoney = 4000;

		String userName = "userName";
		Long refundRecordId = customerRefundRecord.getId();
		String orderId = null;
		Boolean test = false;

		DataResult refund = service.refund(userName, refundType, sourceType, sourceId, refundMoney, refundableMoney, refundRecordId, orderId, test);
		assertTrue(refund.isSuccess());

	}

	//换电退车
	@Test
	public void refund_1() throws AlipayApiException {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		vehicleForegiftOrder.setMoney(1000);
		vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.APPLY_REFUND.getValue());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		vehiclePeriodOrder.setMoney(1000);
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.APPLY_REFUND.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setMoney(1000);
		customerForegiftOrder.setDeductionTicketMoney(0);
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), agentBatteryType.getBatteryType(), agent.getId());
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.APPLY_REFUND.getValue());
		packetPeriodOrder.setMoney(1000);
		insertPacketPeriodOrder(packetPeriodOrder);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
				systemBatteryType.getId(),
				partner.getId(),
				agent.getId(),
				battery.getId(), customer.getId());
		insertBatteryOrder(batteryOrder);

		CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
		insertCustomerExchangeBattery(customerExchangeBattery);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		VehicleOrder vehicleOrder = newVehicleOrder(agent.getId(),shop.getId(),vehicleModel.getId(),vehicle.getId(),customer.getId(),1);
		vehicleOrder.setPartnerId(partner.getId());
		vehicleOrder.setStatus(VehicleOrder.Status.RENT.getValue());
		insertVehicleOrder(vehicleOrder);

		CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
		customerVehicleInfo.setCategory(ConstEnum.Category.EXCHANGE.getValue());
		customerVehicleInfo.setVehicleId(vehicle.getId());
		customerVehicleInfo.setForegiftOrderId(vehicleForegiftOrder.getId());
		customerVehicleInfo.setForegift(vehicleForegiftOrder.getMoney());
		customerVehicleInfo.setRentPriceId(rentPrice.getId());
		customerVehicleInfo.setVehicleOrderId(vehicleOrder.getId());
		customerVehicleInfo.setBalanceShopId(shop.getId());
		insertCustomerVehicleInfo(customerVehicleInfo);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setShopId(shop.getId());
		groupOrder.setStatus(GroupOrder.Status.APPLY_REFUND.getValue());
		insertGroupOrder(groupOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), groupOrder.getId(), CustomerRefundRecord.SourceType.ZCGROUP.getValue());
		customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		int refundType = 1;
		int sourceType = customerRefundRecord.getSourceType();
		String sourceId = customerRefundRecord.getSourceId();
		int refundMoney = 1000;
		int refundableMoney = 4000;

		String userName = "userName";
		Long refundRecordId = customerRefundRecord.getId();
		String orderId = null;
		Boolean test = false;

		DataResult refund = service.refund(userName, refundType, sourceType, sourceId, refundMoney, refundableMoney, refundRecordId, orderId, test);
		assertTrue(refund.isSuccess());

	}

	//租电退车
	@Test
	public void refund_2() throws AlipayApiException {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		VehicleForegiftOrder vehicleForegiftOrder = newVehicleForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		vehicleForegiftOrder.setMoney(1000);
		vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.APPLY_REFUND.getValue());
		insertVehicleForegiftOrder(vehicleForegiftOrder);

		VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
		vehiclePeriodOrder.setMoney(1000);
		vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.APPLY_REFUND.getValue());
		insertVehiclePeriodOrder(vehiclePeriodOrder);

		CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
		customerForegiftOrder.setMoney(1000);
		customerForegiftOrder.setDeductionTicketMoney(0);
		customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
		insertCustomerForegiftOrder(customerForegiftOrder);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
		insertAgentBatteryType(agentBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), agentBatteryType.getBatteryType(), agent.getId());
		packetPeriodOrder.setStatus(PacketPeriodOrder.Status.APPLY_REFUND.getValue());
		packetPeriodOrder.setMoney(1000);
		insertPacketPeriodOrder(packetPeriodOrder);

		Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
		insertBattery(battery);

		Shop shop = newShop(agent.getId());
		insertShop(shop);

		RentOrder rentOrder = newRentOrder(newOrderId(OrderId.OrderIdType.RENT_ORDER), partner.getId(), agent.getId(), shop.getId(), customer.getId(), battery.getId(), systemBatteryType.getId());
		insertRentOrder(rentOrder);

		CustomerRentBattery CustomerRentBattery = newCustomerRentBattery(customer.getId(),agent.getId(), battery.getId(), battery.getType());
		CustomerRentBattery.setRentOrderId(rentOrder.getId());
		insertCustomerRentBattery(CustomerRentBattery);

		VehicleModel vehicleModel = newVehicleModel(agent.getId());
		insertVehicleModel(vehicleModel);

		Vehicle vehicle = newVehicle(agent.getId(), vehicleModel.getId());
		insertVehicle(vehicle);

		PriceSetting priceSetting = newPriceSetting(agent.getId(), vehicleModel.getId());
		insertPriceSetting(priceSetting);

		RentPrice rentPrice = newRentPrice(agent.getId(), priceSetting.getId());
		insertRentPrice(rentPrice);

		VehicleOrder vehicleOrder = newVehicleOrder(agent.getId(),shop.getId(),vehicleModel.getId(),vehicle.getId(),customer.getId(),1);
		vehicleOrder.setPartnerId(partner.getId());
		vehicleOrder.setStatus(VehicleOrder.Status.RENT.getValue());
		insertVehicleOrder(vehicleOrder);

		CustomerVehicleInfo customerVehicleInfo = newCustomerVehicleInfo(agent.getId(), customer.getId());
		customerVehicleInfo.setCategory(ConstEnum.Category.RENT.getValue());
		customerVehicleInfo.setVehicleId(vehicle.getId());
		customerVehicleInfo.setForegiftOrderId(vehicleForegiftOrder.getId());
		customerVehicleInfo.setForegift(vehicleForegiftOrder.getMoney());
		customerVehicleInfo.setRentPriceId(rentPrice.getId());
		customerVehicleInfo.setVehicleOrderId(vehicleOrder.getId());
		customerVehicleInfo.setBalanceShopId(shop.getId());
		insertCustomerVehicleInfo(customerVehicleInfo);

		GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), customerForegiftOrder.getId(), packetPeriodOrder.getId());
		groupOrder.setShopId(shop.getId());
		groupOrder.setStatus(GroupOrder.Status.APPLY_REFUND.getValue());
		insertGroupOrder(groupOrder);

		CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), groupOrder.getId(), CustomerRefundRecord.SourceType.ZCGROUP.getValue());
		customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
		insertCustomerRefundRecord(customerRefundRecord);

		int refundType = 1;
		int sourceType = customerRefundRecord.getSourceType();
		String sourceId = customerRefundRecord.getSourceId();
		int refundMoney = 1000;
		int refundableMoney = 4000;

		String userName = "userName";
		Long refundRecordId = customerRefundRecord.getId();
		String orderId = null;
		Boolean test = false;

		DataResult refund = service.refund(userName, refundType, sourceType, sourceId, refundMoney, refundableMoney, refundRecordId, orderId, test);
		assertTrue(refund.isSuccess());

	}

}
