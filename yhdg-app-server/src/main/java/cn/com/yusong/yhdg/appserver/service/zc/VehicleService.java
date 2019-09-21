package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.zc.*;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class VehicleService extends AbstractService {

    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    VehicleOrderMapper vehicleOrderMapper;
    @Autowired
    ShopStoreVehicleMapper shopStoreVehicleMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    ShopStoreVehicleBatteryMapper shopStoreVehicleBatteryMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    AppConfig config;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentMpOpenIdMapper agentMpOpenIdMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;

    public Vehicle findByVinNo(String vinNo) {
        return vehicleMapper.findByVinNo(vinNo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult bindVehicle(Agent agent, Customer customer, Shop shop, Vehicle vehicle, CustomerVehicleInfo customerVehicleInfo, ShopStoreVehicle shopStoreVehicle) {

        List<ShopStoreVehicleBattery> list = shopStoreVehicleBatteryMapper.findByStoreVehicle(shopStoreVehicle.getId());

        for (ShopStoreVehicleBattery shopStoreVehicleBattery : list) {
            Battery battery = batteryMapper.find(shopStoreVehicleBattery.getBatteryId());
            if (battery != null) {
                if (customerVehicleInfo.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
                    //换电
                    bindExchangeBattery(customer, battery, null, null, null);
                } else if (customerVehicleInfo.getCategory() == ConstEnum.Category.RENT.getValue()) {
                    //租电
                    CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customer.getId());
                    bindRentBattery(agent, customer, battery, shop, customerRentInfo);
                }
            }
        }

        PriceSetting priceSetting = priceSettingMapper.find(shopStoreVehicle.getPriceSettingId());
        if(priceSetting == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
        }
        //生成租车订单
        VehicleOrder vehicleOrder = new VehicleOrder();
        vehicleOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_ORDER));
        vehicleOrder.setPartnerId(agent.getPartnerId());
        vehicleOrder.setAgentId(agent.getId());
        vehicleOrder.setAgentName(agent.getAgentName());
        vehicleOrder.setAgentCode(agent.getAgentCode());
        vehicleOrder.setShopId(shop.getId());
        vehicleOrder.setShopName(shop.getShopName());
        vehicleOrder.setCustomerId(customer.getId());
        vehicleOrder.setCustomerMobile(customer.getMobile());
        vehicleOrder.setCustomerFullname(customer.getFullname());
        //车里有电池 绑定电池信息
        StringBuilder sb = new StringBuilder();
        for (ShopStoreVehicleBattery shopStoreVehicleBattery : list) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(shopStoreVehicleBattery.getBatteryId());
        }
        vehicleOrder.setBatteryId(sb.toString());
        vehicleOrder.setModelId(vehicle.getModelId());
        vehicleOrder.setVehicleId(vehicle.getId());
        vehicleOrder.setVinNo(vehicle.getVinNo());
        vehicleOrder.setVehicleName(vehicle.getVehicleName());
        vehicleOrder.setStatus(VehicleOrder.Status.RENT.getValue());//已租车
        vehicleOrder.setCurrentDistance(0);
        vehicleOrder.setCreateTime(new Date());
        vehicleOrderMapper.insert(vehicleOrder);

        //更新车辆信息
        customerVehicleInfoMapper.updateCustomerVehicleInfo(customer.getId(), vehicleOrder.getId(), vehicle.getId(), priceSetting.getVehicleName());

        //更新押金订单信息
        VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.findOneEnabled(customer.getId(), VehicleForegiftOrder.Status.PAY_OK.getValue(), vehicle.getAgentId(), vehicle.getModelId());
        if (vehicleForegiftOrder != null) {
            vehicleForegiftOrderMapper.updateOrderInfo(vehicleForegiftOrder.getId(), priceSetting.getVehicleName());
        }
        //更新组合订单信息
        GroupOrder groupOrder = groupOrderMapper.findOneEnabled(customer.getId(), GroupOrder.Status.PAY_OK.getValue(), vehicle.getAgentId(), vehicle.getModelId());
        if (groupOrder != null) {
            groupOrderMapper.updateOrderInfo(groupOrder.getId(), priceSetting.getVehicleName());
        }

        //更新租车订单状态
        VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.findOneEnabled(customer.getId(), VehiclePeriodOrder.Status.NOT_USE.getValue(), vehicle.getAgentId(), vehicle.getModelId());
        if (vehiclePeriodOrder != null) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, vehiclePeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
            vehiclePeriodOrderMapper.updateStatus(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_USE.getValue(),
                    VehiclePeriodOrder.Status.USED.getValue(), beginTime, endTime, priceSetting.getVehicleName());
        }

        //库存表去除该车辆电池
        for (ShopStoreVehicleBattery shopStoreVehicleBattery : list) {
            shopStoreVehicleBatteryMapper.clearVehicleBattery(shopStoreVehicleBattery.getStoreVehicleId());
        }
        //库存表去除该车辆
        shopStoreVehicleMapper.clearVehicle(shop.getId(), vehicle.getId());

        //更新车辆使用状态
        vehicleMapper.updateCustomerUse(vehicle.getId(), Vehicle.Status.IN_USE.getValue(), customer.getId(), customer.getMobile(), customer.getFullname());

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    public void bindExchangeBattery(Customer customer, Battery battery, Cabinet cabinet, CabinetBox cabinetBox, BespeakOrder bespeakOrder) {
        Date date = new Date();
        String shopId = null,shopName= null;
        //查询电池是否在库存中
        ShopStoreBattery shopStoreBattery = shopStoreBatteryMapper.findByBattery(battery.getId());
        //去库存
        if(shopStoreBattery != null){
            Shop shop = shopMapper.find(shopStoreBattery.getShopId());
            if(shop != null){
                shopId = shop.getId();
                shopName = shop.getShopName();
                shopStoreBatteryMapper.clearBattery(shopStoreBattery.getShopId(), battery.getId());
            }
        }


        BatteryOrder order = new BatteryOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        order.setPartnerId(customer.getPartnerId());
        order.setBatteryType(battery.getType());

        order.setCustomerId(customer.getId());
        Customer dbCustomer = customerMapper.find(customer.getId());
        if (dbCustomer != null) {
            order.setTakeAgentCompanyId(dbCustomer.getAgentCompanyId());
            AgentCompany agentCompany = agentCompanyMapper.find(dbCustomer.getAgentCompanyId());
            if (agentCompany != null) {
                order.setTakeAgentCompanyName(agentCompany.getCompanyName());
            }
        }
        order.setCustomerMobile(customer.getMobile());
        order.setCustomerFullname(customer.getFullname());
        if(cabinet != null){
            order.setAgentId(cabinet.getAgentId());
            order.setTakeCabinetId(cabinet.getId());
            order.setTakeCabinetName(cabinet.getCabinetName());
            if(cabinet.getShopId() != null){
                Shop shop = shopMapper.find(cabinet.getShopId());
                if(shop != null){
                    order.setTakeShopId(shop.getId());
                    order.setTakeShopName(shop.getShopName());
                }
            }
            order.setTakeBoxNum(cabinetBox.getBoxNum());
            order.setProvinceId(cabinet.getProvinceId());
            order.setCityId(cabinet.getCityId());
            order.setDistrictId(cabinet.getDistrictId());
            order.setAddress(cabinet.getAddress());
            order.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        }else{
            order.setTakeShopId(shopId);
            order.setTakeShopName(shopName);
            order.setAgentId(customer.getAgentId());
            order.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        }
        order.setTakeTime(new Date());
        order.setBatteryId(battery.getId());
        order.setInitVolume(battery.getVolume());
        order.setCurrentVolume(battery.getVolume());
        order.setInitCapacity(battery.getCurrentCapacity());
        order.setCurrentCapacity(battery.getCurrentCapacity());
        order.setOpenTime(date);
        order.setCreateTime(date);
        order.setCurrentDistance(0);
        order.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());
        batteryOrderMapper.insert(order);

        if(cabinet != null){
            //查询用户是否有预约订单
            if(bespeakOrder != null){
                //解锁
                Integer status = null;
                if(bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())
                        && bespeakOrder.getBespeakBoxNum().equals(cabinetBox.getBoxNum())){
                    cabinetBoxMapper.unlockBox(bespeakOrder.getBespeakCabinetId(),bespeakOrder.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue());
                    status = BespeakOrder.Status.TAKE.getValue();
                }else{
                    cabinetBoxMapper.unlockBox(bespeakOrder.getBespeakCabinetId(),bespeakOrder.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                    status = BespeakOrder.Status.OTHER_TAKE.getValue();
                }
                //更新预约单
                bespeakOrder.setTakeCabinetId(cabinet.getId());
                bespeakOrder.setTakeCabinetName(cabinet.getCabinetName());
                bespeakOrder.setTakeBoxNum(cabinetBox.getBoxNum());
                bespeakOrder.setTakeBatteryId(battery.getId());
                bespeakOrder.setTakeTime(new Date());
                bespeakOrder.setStatus(status);
                bespeakOrderMapper.take(bespeakOrder);
            }

            cabinetBoxMapper.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue());
            batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.IN_BOX_CUSTOMER_USE.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());
        }else{
            batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.CUSTOMER_OUT.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());
        }

        //写入用户关联用户信息
        CustomerExchangeBattery customerExchangeBattery = new CustomerExchangeBattery();
        customerExchangeBattery.setCustomerId(customer.getId());
        customerExchangeBattery.setAgentId(order.getAgentId());
        customerExchangeBattery.setBatteryId(battery.getId());
        customerExchangeBattery.setBatteryType(battery.getType());
        customerExchangeBattery.setBatteryOrderId(order.getId());
        customerExchangeBattery.setCreateTime(new Date());
        customerExchangeBatteryMapper.insert(customerExchangeBattery);

        //取新电时用户套餐变为使用中
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.USED.getValue(), order.getAgentId(), order.getBatteryType());
        if (packetPeriodOrder == null) {
            packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), order.getAgentId(), order.getBatteryType());
        }

        Agent agent = agentMapper.find(order.getAgentId());
        //客户首次换电处理拉新记录
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
        if (customerExchangeInfo != null) {
            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
            if (customerForegiftOrder != null && packetPeriodOrder != null) {
                //抵扣金额为空 非VIP套餐客户
                if (customerForegiftOrder.getReduceMoney() == null) {
                    handleLaxinCustomer(agent, customer, customerForegiftOrder.getMoney(), packetPeriodOrder.getMoney(), customerForegiftOrder.getCreateTime(), order.getCreateTime());
                }
            }
        }

        if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, packetPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
            int eft = packetPeriodOrderMapper.updateStatus(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), beginTime, endTime);
            if (eft > 0) {
                handleLaxinCustomerByMonth(agent, customer, packetPeriodOrder.getMoney());
            }
        }

        if(cabinet != null){
            CabinetOperateLog operateLog = new CabinetOperateLog();
            operateLog.setAgentId(cabinet.getAgentId());

            operateLog.setCabinetId(cabinet.getId());
            operateLog.setCabinetName(cabinet.getCabinetName());
            operateLog.setBoxNum(cabinetBox.getBoxNum());
            operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
            operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
            operateLog.setContent(String.format("换电订单%s, 打开满箱成功", order.getId()));
            operateLog.setOperator(customer.getFullname());
            operateLog.setCreateTime(new Date());
            cabinetOperateLogMapper.insert(operateLog);

            PushMetaData metaData = new PushMetaData();
            metaData.setSourceType(PushMessage.SourceType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue());
            metaData.setSourceId(order.getId());
            metaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(metaData);
        }

        if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
            PushOrderMessage pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.TAKE.getValue());
            pushOrderMessage.setSourceId(order.getId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);
        }

    }

    public void bindRentBattery(Agent agent, Customer customer, Battery battery, Shop shop, CustomerRentInfo customerRentInfo) {
        //生成租电订单
        RentOrder rentOrder = new RentOrder();
        rentOrder.setId(newOrderId(OrderId.OrderIdType.RENT_ORDER));
        rentOrder.setPartnerId(agent.getPartnerId());
        rentOrder.setAgentId(battery.getAgentId());
        rentOrder.setAgentName(agent.getAgentName());
        rentOrder.setAgentCode(agent.getAgentCode());
        rentOrder.setShopId(shop.getId());
        rentOrder.setShopName(shop.getShopName());
        rentOrder.setCustomerId(customer.getId());
        rentOrder.setCustomerMobile(customer.getMobile());
        rentOrder.setCustomerFullname(customer.getFullname());
        rentOrder.setBatteryType(battery.getType());
        rentOrder.setBatteryId(battery.getId());
        rentOrder.setStatus(RentOrder.Status.RENT.getValue());
        rentOrder.setCurrentVolume(battery.getVolume());
        rentOrder.setCurrentDistance(0);
        rentOrder.setCreateTime(new Date());
        rentOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());
        rentOrderMapper.insert(rentOrder);


        CustomerRentBattery customerRentBattery = new CustomerRentBattery();
        customerRentBattery.setCustomerId(customer.getId());
        customerRentBattery.setAgentId(battery.getAgentId());
        customerRentBattery.setBatteryId(battery.getId());
        customerRentBattery.setBatteryType(battery.getType());
        customerRentBattery.setRentOrderId(rentOrder.getId());
        int effect =  customerRentBatteryMapper.insert(customerRentBattery);
        if(effect > 0){
            //库存表去除该电池
            shopStoreBatteryMapper.clearBattery(shop.getId(), battery.getId());
            //电池使用
            batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.CUSTOMER_OUT.getValue(), rentOrder.getId(), new Date(), customer.getId(), customer.getMobile(), customer.getFullname());
        }

        //更新租电租金订单状态
        RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.findOneEnabled(customer.getId(), RentPeriodOrder.Status.NOT_USE.getValue(), agent.getId(),  battery.getType());
        if (rentPeriodOrder != null) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, rentPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
            rentPeriodOrderMapper.updateStatus(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_USE.getValue(),
                    RentPeriodOrder.Status.USED.getValue(), beginTime, endTime);
        }
    }

    public int findTodayOrderCount(long customerId) {
        String prefixRentOrder = OrderId.PREFIX_VEHICLE_ORDER;
        String format = DateFormatUtils.format(new Date(), OrderId.DATE_FORMAT);
        return vehicleOrderMapper.findCountByCustomer(customerId, prefixRentOrder+format+ "%");
    }

    public Vehicle find(Integer id){
         return  vehicleMapper.find(id);
    }
}
