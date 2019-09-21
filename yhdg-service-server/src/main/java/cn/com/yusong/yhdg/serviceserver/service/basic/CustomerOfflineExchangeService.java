package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CustomerOfflineExchangeService extends AbstractService {
    final static Logger log = LogManager.getLogger(CustomerOfflineExchangeService.class);

    @Autowired
    CustomerOfflineBatteryMapper customerOfflineBatteryMapper;
    @Autowired
    CustomerOfflineExchangeRecordMapper customerOfflineExchangeRecordMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;

    public void dealOfflineBattery() {
        Date now = new Date();

        //处理用户提交的数据
        int  limit = 100;

        while (true) {
            List<CustomerOfflineBattery> orderList = customerOfflineBatteryMapper.findList(CustomerOfflineBattery.Status.NO.getValue(), limit);
            if(orderList.isEmpty()){
                break;
            }
            for(CustomerOfflineBattery offlineBattery : orderList){
                try {
                    boolean dealFlag = true;

                    //校验环节
                    Battery battery = batteryMapper.findByCode(offlineBattery.getBatteryCode());
                    if (battery == null) {
                        log.debug("%s电池不存在", offlineBattery.getBatteryCode());
                        dealFlag  = false;
                    }else{
                        offlineBattery.setBatteryId(battery.getId());
                    }
                    Cabinet cabinet = cabinetMapper.findByMac(offlineBattery.getCabinetCode());
                    if (cabinet == null) {
                        log.debug("%s柜子不存在",offlineBattery.getCabinetCode());
                        dealFlag  = false;
                    }else{
                        offlineBattery.setCabinetId(cabinet.getId());
                    }

                    //判断1：是否绑定有旧电
                    if(dealFlag){
                        boolean dealNewBattery = true;
                        CustomerExchangeBattery customerExchangeBattery = customerExchangeBatteryMapper.findOneByCustomer(offlineBattery.getCustomerId());
                        if(customerExchangeBattery != null){
                            //旧电绑定时间与当前绑定时间比较
                            if(customerExchangeBattery.getCreateTime() == null ||
                                    customerExchangeBattery.getCreateTime().compareTo(offlineBattery.getExchangeTime()) < 0){
                                //前 旧电和其他用户解绑
                                complete(customerExchangeBattery.getBatteryOrderId());
                            }else{
                                //后 不处理（同时不绑定新电）
                                dealNewBattery = false;
                            }
                        }

                        //判断2：新电绑定时间与当前绑定时间比较
                        if(dealNewBattery){
                            Date newBindTime = null;
                            //如果新电池没有和其他用户绑定，还要判断新电池有没有之后生成的订单
                            CustomerExchangeBattery newExchangeBattery = customerExchangeBatteryMapper.findByBattery(offlineBattery.getBatteryId());
                            if(newExchangeBattery == null){
                                BatteryOrder order = batteryOrderMapper.findLastByBattery(offlineBattery.getBatteryId());
                                if(order != null){
                                    newBindTime = order.getTakeTime();
                                }
                            }else{
                                newBindTime = newExchangeBattery.getCreateTime();
                            }

                            if(newBindTime == null || newBindTime.compareTo(offlineBattery.getExchangeTime()) < 0){
                                //处理数据
                                if(newExchangeBattery != null){
                                    //新电和其他用户解绑
                                    complete(newExchangeBattery.getBatteryOrderId());
                                }
                                //用户绑定电池
                                createNewOrder(offlineBattery, cabinet, battery, now);
                            }
                        }
                    }

                } catch (Exception e) {
                    //更新状态
                    customerOfflineBatteryMapper.complete(offlineBattery.getId(), offlineBattery.getCabinetId(), offlineBattery.getBatteryId(), CustomerOfflineBattery.Status.FAIL.getValue(), now);
                    log.error("用户绑定离线电池异常", e);
                }
                //更新状态
                customerOfflineBatteryMapper.complete(offlineBattery.getId(),  offlineBattery.getCabinetId(), offlineBattery.getBatteryId(), CustomerOfflineBattery.Status.SUCCESS.getValue(), now);
            }
        }
    }

    public void dealOfflineExchangeRecord() {
        Date now = new Date();

        //处理用户提交的数据
        int  limit = 100;

        while (true) {
            List<CustomerOfflineExchangeRecord> orderList = customerOfflineExchangeRecordMapper.findList(CustomerOfflineBattery.Status.NO.getValue(), limit);
            if(orderList.isEmpty()){
                break;
            }
            for(CustomerOfflineExchangeRecord record : orderList){
                try {
                    //结束旧单
                    Battery oldBattery = batteryMapper.findByCode(record.getPutBatteryCode());
                    Cabinet oldCabinet = cabinetMapper.findByMac(record.getPutCabinetCode());
                    if(oldBattery != null && oldCabinet != null){
                        record.setPutCabinetId(oldCabinet.getId());
                        record.setPutBatteryId(oldBattery.getId());

                        BatteryOrder batteryOrder = batteryOrderMapper.findLastByCustomer(oldBattery.getId(), record.getExchangeTime(), null, record.getCustomerId(), BatteryOrder.OrderStatus.TAKE_OUT.getValue());
                        if(batteryOrder != null){
                            batteryOrderMapper.payOk(batteryOrder.getId(), null, BatteryOrder.OrderStatus.OFFLINE_EXCHANGE.getValue(),
                                    oldCabinet.getId(), oldCabinet.getCabinetName(), record.getPutBoxNum(), record.getExchangeTime(),
                                    ConstEnum.PayType.PLATFORM.getValue(), new Date(), 0, 0, 0, null, null);
                        }
                    }
                    //生产新电
                    Customer customer = customerMapper.find(record.getCustomerId());
                    Battery newBattery = batteryMapper.findByCode(record.getTakeBatteryCode());
                    Cabinet newCabinet = cabinetMapper.findByMac(record.getTakeCabinetCode());
                    if(newBattery != null && newCabinet != null){
                        record.setTakeCabinetId(newCabinet.getId());
                        record.setTakeBatteryId(newBattery.getId());

                        BatteryOrder batteryOrder = batteryOrderMapper.findLastByCustomer(newBattery.getId(), null, record.getExchangeTime(), record.getCustomerId(), BatteryOrder.OrderStatus.TAKE_OUT.getValue());
                        if(batteryOrder == null){
                            BatteryOrder order = new BatteryOrder();
                            order.setId(newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
                            order.setPartnerId(customer.getPartnerId());
                            order.setBatteryType(newBattery.getType());
                            order.setCustomerId(customer.getId());
                            order.setCustomerMobile(customer.getMobile());
                            order.setCustomerFullname(customer.getFullname());
                            if(newCabinet != null){
                                order.setAgentId(newCabinet.getAgentId());
                                order.setTakeCabinetId(newCabinet.getId());
                                order.setTakeCabinetName(newCabinet.getCabinetName());
                                order.setTakeBoxNum(record.getTakeBoxNum());
                                order.setProvinceId(newCabinet.getProvinceId());
                                order.setCityId(newCabinet.getCityId());
                                order.setDistrictId(newCabinet.getDistrictId());
                                order.setAddress(newCabinet.getAddress());
                                order.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
                            }
                            order.setTakeTime(record.getExchangeTime());
                            order.setBatteryId(newBattery.getId());
                            order.setInitVolume(newBattery.getVolume());
                            order.setCurrentVolume(newBattery.getVolume());
                            order.setInitCapacity(newBattery.getCurrentCapacity());
                            order.setCurrentCapacity(newBattery.getCurrentCapacity());
                            order.setOpenTime(record.getExchangeTime());
                            order.setCreateTime(now);
                            order.setCurrentDistance(0);
                            order.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
                            batteryOrderMapper.insert(order);
                        }
                    }
                } catch (Exception e) {
                    //更新状态
                    customerOfflineExchangeRecordMapper.complete(record.getId(), record.getPutCabinetId(), record.getPutBatteryId(), record.getTakeCabinetId(), record.getTakeBatteryId(), CustomerOfflineExchangeRecord.Status.FAIL.getValue(), now);
                    log.error("用户离线换电记录生成异常", e);
                }
                //更新状态
                customerOfflineExchangeRecordMapper.complete(record.getId(), record.getPutCabinetId(), record.getPutBatteryId(), record.getTakeCabinetId(), record.getTakeBatteryId(), CustomerOfflineExchangeRecord.Status.SUCCESS.getValue(), now);
            }
        }
    }


    @Transactional(rollbackFor = Throwable.class)
    public int complete(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);

        int effect = batteryOrderMapper.payOk(id, null, BatteryOrder.OrderStatus.OFFLINE_EXCHANGE.getValue(),null, null, null, null,
                ConstEnum.PayType.PLATFORM.getValue(), new Date(), 0, 0, 0, null, null);
        if (effect == 0) {
            log.debug("%s订单状态不对", order.getId());
            return effect;
        }
        Agent agent = agentMapper.find(order.getAgentId());
        if (effect == 1) {
            customerExchangeBatteryMapper.clearBattery(order.getCustomerId(), order.getBatteryId());
            batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());

            if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                pushOrderMessage.setSourceId(order.getId());
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }
        }
        return effect;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BatteryOrder createNewOrder(CustomerOfflineBattery offlineBattery, Cabinet cabinet, Battery battery, Date now) {
        Customer customer = customerMapper.find(offlineBattery.getCustomerId());

        BatteryOrder order = new BatteryOrder();
        order.setId(newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        order.setPartnerId(customer.getPartnerId());
        order.setBatteryType(battery.getType());
        order.setCustomerId(customer.getId());
        order.setCustomerMobile(customer.getMobile());
        order.setCustomerFullname(customer.getFullname());
        if(cabinet != null){
            order.setAgentId(cabinet.getAgentId());
            order.setTakeCabinetId(cabinet.getId());
            order.setTakeCabinetName(cabinet.getCabinetName());
            order.setTakeBoxNum(offlineBattery.getBoxNum());
            order.setProvinceId(cabinet.getProvinceId());
            order.setCityId(cabinet.getCityId());
            order.setDistrictId(cabinet.getDistrictId());
            order.setAddress(cabinet.getAddress());
            order.setOrderStatus(BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        }
        order.setTakeTime(offlineBattery.getExchangeTime());
        order.setBatteryId(battery.getId());
        order.setInitVolume(battery.getVolume());
        order.setCurrentVolume(battery.getVolume());
        order.setInitCapacity(battery.getCurrentCapacity());
        order.setCurrentCapacity(battery.getCurrentCapacity());
        order.setOpenTime(now);
        order.setCreateTime(now);
        order.setCurrentDistance(0);
        order.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        batteryOrderMapper.insert(order);

        batteryMapper.updateCustomerUse(battery.getId(), Battery.Status.CUSTOMER_OUT.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());

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

        if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {
            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, packetPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
            int eft = packetPeriodOrderMapper.updateStatus(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), beginTime, endTime);
        }
        Agent agent = agentMapper.find(order.getAgentId());
        if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
            PushOrderMessage pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.TAKE.getValue());
            pushOrderMessage.setSourceId(order.getId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);
        }

        return order;
    }
}
