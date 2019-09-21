//package cn.com.yusong.yhdg.agentserver.service.hdg;
//
//import cn.com.yusong.yhdg.common.domain.basic.Customer;
//import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
//import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
//import cn.com.yusong.yhdg.common.domain.basic.OrderId;
//import cn.com.yusong.yhdg.common.domain.hdg.*;
//import cn.com.yusong.yhdg.common.entity.json.ExtResult;
//import cn.com.yusong.yhdg.common.entity.pagination.Page;
//import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerExchangeBatteryMapper;
//import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerForegiftOrderMapper;
//import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
//import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
//import cn.com.yusong.yhdg.agentserver.service.AbstractService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class VehicleOrderService extends AbstractService {
//    @Autowired
//    VehicleOrderMapper rentVehicleOrderMapper;
//    @Autowired
//    ShopMapper shopMapper;
//    @Autowired
//    BatteryOrderMapper batteryOrderMapper;
//    @Autowired
//    CustomerMapper customerMapper;
//    @Autowired
//    BatteryMapper batteryMapper;
//    @Autowired
//    CustomerForegiftOrderMapper customerForegiftOrderMapper;
//    @Autowired
//    PacketPeriodOrderMapper packetPeriodOrderMapper;
//    @Autowired
//    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
//
//    public Page findPage(VehicleOrder search) {
//        Page page = search.buildPage();
//        page.setTotalItems(rentVehicleOrderMapper.findPageCount(search));
//        search.setBeginIndex(page.getOffset());
//        List<VehicleOrder> list = rentVehicleOrderMapper.findPageResult(search);
//        for (VehicleOrder rentVehicleOrder : list) {
//            if (rentVehicleOrder != null) {
//                if (rentVehicleOrder.getAgentId() != null) {
//                    rentVehicleOrder.setAgentName(findAgentInfo(rentVehicleOrder.getAgentId()).getAgentName());
//                }
//                if (StringUtils.isNotEmpty(rentVehicleOrder.getShopId())) {
//                    Shop shop = shopMapper.find(rentVehicleOrder.getShopId());
//                    if (shop != null) {
//                        rentVehicleOrder.setShopName(shop.getShopName());
//                    }
//                }
//                if (rentVehicleOrder.getVehicleId() != null) {
//                }
//            }
//        }
//        page.setResult(list);
//
//        return page;
//    }
//
//    public VehicleOrder find(String id) {
//        VehicleOrder rentVehicleOrder = rentVehicleOrderMapper.find(id);
//        if (rentVehicleOrder.getAgentId() != null) {
//            rentVehicleOrder.setAgentName(findAgentInfo(rentVehicleOrder.getAgentId()).getAgentName());
//        }
//        return rentVehicleOrder;
//    }
//
////    public List<VehicleOrder> findList(long id) {
////        return rentVehicleOrderMapper.findList(id);
////    }
//    public List<VehicleOrder> findList(String id) {
//        return rentVehicleOrderMapper.findList(id);
//    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public ExtResult create(String customerForegiftOrderId, String packetPeriodOrderId, Long customerId, String vehicleId, String batteryId) {
//        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerForegiftOrderId);
//        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(packetPeriodOrderId);
//        Customer customer = customerMapper.find(customerId);
//        Battery battery = batteryMapper.find(batteryId);
//
//        //换电订单
//        /*if (customerForegiftOrder.getForegiftType() == CustomerForegiftOrder.ForegiftType.EXCHANGE_FOREGIFT.getValue()
//                || customerForegiftOrder.getForegiftType() == CustomerForegiftOrder.ForegiftType.EXCHANGE_RENT_SELL_FOREGIFT.getValue()) {
//            return ExtResult.failResult("只支持租车业务！");
//        }*/
//        if (customerForegiftOrder == null) {
//            return  ExtResult.failResult("押金订单不存在！");
//        }
//        if (customerForegiftOrder.getStatus() != CustomerForegiftOrder.Status.PAY_OK.getValue()) {
//            return ExtResult.failResult("押金未支付成功！");
//        }
//
//        if (packetPeriodOrder == null) {
//            return  ExtResult.failResult("包时段订单不存在！");
//        }
//
//        if (customer == null) {
//            return ExtResult.failResult("客户信息不存在！");
//        }
//        if (battery == null) {
//            return ExtResult.failResult("该电池不存在！");
//        }
//        if (battery.getStatus() != Battery.Status.NOT_USE.getValue() && battery.getStatus() != Battery.Status.KEEPER_OUT.getValue()) {
//            return ExtResult.failResult("该电池不可用！");
//        }
//        List<CustomerExchangeBattery> customerExchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(customerId);
//        for (CustomerExchangeBattery customerExchangeBattery : customerExchangeBatteryList) {
//            if (customerExchangeBattery.getBatteryId() != null) {
//                return ExtResult.failResult("该用户已绑定电池！");
//            }
//        }
//
//        //租车订单
////        if (customerForegiftOrder.getForegiftType() == CustomerForegiftOrder.ForegiftType.VEHICLE_FOREGIFT.getValue()
////                || customerForegiftOrder.getForegiftType() == CustomerForegiftOrder.ForegiftType.VEHICLE_RENT_SELL_FOREGIFT.getValue()) {
//
//            //绑定车辆
//            VehicleOrder vehicleOrder = new VehicleOrder();
//            vehicleOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_ORDER));
//            vehicleOrder.setVehicleId(vehicleId);
//            vehicleOrder.setBeginTime(packetPeriodOrder.getBeginTime());
//            vehicleOrder.setEndTime(packetPeriodOrder.getEndTime());
//            vehicleOrder.setDuration(packetPeriodOrder.getDayCount());
//            vehicleOrder.setCustomerId(customerId.intValue());
//            vehicleOrder.setCustomerMobile(customer.getMobile());
//            vehicleOrder.setCustomerFullname(customer.getFullname());
//            vehicleOrder.setBatteryType(battery.getType());
//            vehicleOrder.setMoney(packetPeriodOrder.getMoney());
//            vehicleOrder.setStatus(VehicleOrder.Status.CUSTOMER_USE.getValue());
//            vehicleOrder.setCreateTime(new Date());
//            rentVehicleOrderMapper.insert(vehicleOrder);
//
//            //绑定电池
//            BatteryOrder batteryOrder = new BatteryOrder();
//            batteryOrder.setId(newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
//            batteryOrder.setPartnerId(customer.getPartnerId());
//            batteryOrder.setBatteryId(batteryId);
//            batteryOrder.setCurrentDistance(0);
//            batteryOrder.setCustomerId(customerId);
//            batteryOrder.setCustomerFullname(customer.getFullname());
//            batteryOrder.setCustomerMobile(customer.getMobile());
//            batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
//            batteryOrder.setPayType(packetPeriodOrder.getPayType());
//            batteryOrder.setMoney(packetPeriodOrder.getMoney());
//            batteryOrder.setCreateTime(vehicleOrder.getCreateTime());
//            batteryOrderMapper.insert(batteryOrder);
//
//            //更新用户表数据batteryId,batteryType,batteryOrderId,vehicleId,vehicleModelId,vehicleOrderId,
//            int customerResult = customerExchangeBatteryMapper.updateVehicleInfo(customerId, batteryId, battery.getType(), batteryOrder.getId());
//            if (customerResult == 0) {
//                return ExtResult.failResult("绑定车辆失败！");
//            }
//
//            //更新电池表数据customerId,customerMobile,customerFullname
//            int updateBatteryResult = batteryMapper.updateOrderId(batteryId, Battery.Status.CUSTOMER_OUT.getValue(), batteryOrder.getId(),new Date(), customerId.intValue(),customer.getMobile(),customer.getFullname());
//            if (updateBatteryResult == 0) {
//                return ExtResult.failResult("绑定电池失败！");
//            }
////        }
//        return ExtResult.successResult("绑定成功！");
//    }
//}
