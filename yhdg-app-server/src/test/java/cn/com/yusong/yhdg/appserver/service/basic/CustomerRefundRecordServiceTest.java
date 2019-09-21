package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerRefundRecordService;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerRefundRecordServiceTest extends BaseJunit4Test {
    @Autowired
    CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;

    @Test
    public void addHdRecordAndUpdateStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        insertCustomerForegiftOrder(customerForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        InsuranceOrder insuranceOrder = newInsuranceOrder(customer);
        insuranceOrder.setStatus(InsuranceOrder.Status.PAID.getValue());
        insertInsuranceOrder(insuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        insertCustomerMultiOrder(customerMultiOrder);

        List<CustomerForegiftOrder> foregiftOrderList = new ArrayList();
        List<PacketPeriodOrder> packetPeriodOrderList = new ArrayList();
        List<InsuranceOrder> insuranceOrderList = new ArrayList();
        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList();
        foregiftOrderList.add(customerForegiftOrder);
        packetPeriodOrderList.add(packetPeriodOrder);
        insuranceOrderList.add(insuranceOrder);
        customerMultiOrderList.add(customerMultiOrder);

        RestResult restResult = customerRefundRecordService.addHdRecordAndUpdateStatus(customer, foregiftOrderList, packetPeriodOrderList, insuranceOrderList, customerMultiOrderList);
        assertEquals(0, restResult.getCode());
    }

    @Test
    public void addZdRecordAndUpdateStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        RentInsuranceOrder rentInsuranceOrder = newRentInsuranceOrder(customer);
        rentInsuranceOrder.setStatus(RentInsuranceOrder.Status.PAID.getValue());
        insertRentInsuranceOrder(rentInsuranceOrder);

        CustomerMultiOrder customerMultiOrder = newCustomerMultiOrder(partner.getId(), agent.getId(), customer.getId());
        insertCustomerMultiOrder(customerMultiOrder);

        List<RentForegiftOrder> foregiftOrderList = new ArrayList();
        List<RentPeriodOrder> rentPeriodOrderList = new ArrayList();
        List<RentInsuranceOrder> rentInsuranceOrderList = new ArrayList();
        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList();
        foregiftOrderList.add(rentForegiftOrder);
        rentPeriodOrderList.add(rentPeriodOrder);
        rentInsuranceOrderList.add(rentInsuranceOrder);
        customerMultiOrderList.add(customerMultiOrder);

        RestResult restResult = customerRefundRecordService.addZdRecordAndUpdateStatus(customer, foregiftOrderList, rentPeriodOrderList, rentInsuranceOrderList, customerMultiOrderList);
        assertEquals(0, restResult.getCode());
    }

    @Test
    public void addZcRecordAndUpdateStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
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
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), rentForegiftOrder.getBatteryId(), rentPeriodOrder.getShopId());
        groupOrder.setCategory(Battery.Category.RENT.getValue());
        groupOrder.setBatteryForegiftId(rentForegiftOrder.getId());
        groupOrder.setBatteryRentId(rentPeriodOrder.getId());
        insertGroupOrder(groupOrder);

        List<GroupOrder> groupOrderList = Arrays.asList(groupOrder);

       // customerRefundRecordService.addZcRecordAndUpdateStatus(customer, groupOrderList);
        assertTrue(1 == customerRefundRecordService.findListByCustomerIdAndStatus(customer.getId(), CustomerRefundRecord.Status.APPLY.getValue(), 0, 10, CustomerRefundRecord.Type.ZC.getValue()).size());

        RentForegiftOrder rentForegiftOrder1 = rentForegiftOrderService.find(groupOrder.getBatteryForegiftId());
        assertEquals(RentForegiftOrder.Status.APPLY_REFUND.getValue(), rentForegiftOrder1.getStatus().intValue());

        RentPeriodOrder rentPeriodOrder1 = rentPeriodOrderService.find(groupOrder.getBatteryRentId());
        assertEquals(RentPeriodOrder.Status.APPLY_REFUND.getValue(), rentPeriodOrder1.getStatus().intValue());
    }

    @Test
    public void findListByCustomerIdAndStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        
        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
        customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        insertCustomerRefundRecord(customerRefundRecord);

        List<CustomerRefundRecord> list = customerRefundRecordService.findListByCustomerIdAndStatus(customer.getId(), CustomerRefundRecord.Status.APPLY.getValue(), null, null, CustomerRefundRecord.Type.HD.getValue());

        assertEquals(1, list.size());
    }

    @Test
    public void findListByorderId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        
        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), "00000", 2);
        insertCustomerRefundRecord(customerRefundRecord);

        List<CustomerRefundRecord> list = customerRefundRecordService.findListByorderId(customer.getId(), customerRefundRecord.getOrderId());
        assertEquals(1, list.size());
    }

    @Test
    public void updateHdOrderAndRefundStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);


        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), customerForegiftOrder.getId(), CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
        customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        insertCustomerRefundRecord(customerRefundRecord);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.APPLY_REFUND.getValue());
        insertPacketPeriodOrder(packetPeriodOrder);

        CustomerRefundRecord customerRefundRecord2 = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), packetPeriodOrder.getId(), CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue());
        customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        insertCustomerRefundRecord(customerRefundRecord2);

        List<CustomerRefundRecord> list = new ArrayList<CustomerRefundRecord>();
        list.add(customerRefundRecord);
        list.add(customerRefundRecord2);

        RestResult restResult = customerRefundRecordService.updateHdOrderAndRefundStatus(list);
        assertEquals(0, restResult.getCode());
    }

    @Test
    public void updateZdOrderAndRefundStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);


        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), rentForegiftOrder.getId(), CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue());
        customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        insertCustomerRefundRecord(customerRefundRecord);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.APPLY_REFUND.getValue());
        insertRentPeriodOrder(rentPeriodOrder);

        CustomerRefundRecord customerRefundRecord2 = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), rentPeriodOrder.getId(), CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue());
        customerRefundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        insertCustomerRefundRecord(customerRefundRecord2);

        List<CustomerRefundRecord> list = new ArrayList<CustomerRefundRecord>();
        list.add(customerRefundRecord);
        list.add(customerRefundRecord2);

        RestResult restResult = customerRefundRecordService.updateHdOrderAndRefundStatus(list);
        assertEquals(0, restResult.getCode());
    }

    @Test
    public void updateZcOrderAndRefundStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Customer customer = newCustomer(partner.getId());
        customer.setBatteryType(systemBatteryType.getId());
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        RentForegiftOrder rentForegiftOrder = newRentForegiftOrder(partner.getId(), customer.getId(), customer.getAgentId());
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.APPLY_REFUND.getValue());
        insertRentForegiftOrder(rentForegiftOrder);

        RentPeriodOrder rentPeriodOrder = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.APPLY_REFUND.getValue());
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
        insertVehicleForegiftOrder(vehicleForegiftOrder);

        VehiclePeriodOrder vehiclePeriodOrder = newVehiclePeriodOrder(partner.getId(), customer.getId());
        insertVehiclePeriodOrder(vehiclePeriodOrder);

        GroupOrder groupOrder = newGroupOrder(partner.getId(), agent.getId(), customer.getId(), rentPrice.getId(), vehicleForegiftOrder.getId(), vehiclePeriodOrder.getId(), rentForegiftOrder.getBatteryId(), rentPeriodOrder.getShopId());
        groupOrder.setCategory(Battery.Category.RENT.getValue());
        groupOrder.setBatteryForegiftId(rentForegiftOrder.getId());
        groupOrder.setBatteryRentId(rentPeriodOrder.getId());
        insertGroupOrder(groupOrder);

        CustomerRefundRecord customerRefundRecord = newCustomerRefundRecord(partner.getId(), customer.getId(), agent.getId(), groupOrder.getId(), CustomerRefundRecord.SourceType.ZCGROUP.getValue());
        insertCustomerRefundRecord(customerRefundRecord);

        List<CustomerRefundRecord> customerRefundRecords = Arrays.asList(customerRefundRecord);
        customerRefundRecordService.updateZcOrderAndRefundStatus(customerRefundRecords);

        assertTrue(1 == customerRefundRecordService.findListByCustomerIdAndStatus(customer.getId(), CustomerRefundRecord.Status.CANCEL.getValue(), 0, 10, CustomerRefundRecord.Type.ZC.getValue()).size());

        RentForegiftOrder rentForegiftOrder1 = rentForegiftOrderService.find(groupOrder.getBatteryForegiftId());
        assertEquals(RentForegiftOrder.Status.PAY_OK.getValue(), rentForegiftOrder1.getStatus().intValue());

        RentPeriodOrder rentPeriodOrder1 = rentPeriodOrderService.find(groupOrder.getBatteryRentId());
        assertEquals(RentPeriodOrder.Status.NOT_USE.getValue(), rentPeriodOrder1.getStatus().intValue());
    }
}