package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.staticserver.persistence.basic.*;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.*;
import cn.com.yusong.yhdg.staticserver.service.basic.AbstractService;
import cn.com.yusong.yhdg.staticserver.service.basic.OrderIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatteryOrderService extends AbstractService {

    @Autowired
    CollectionAddressMapper collectionAddressMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;

    public BatteryOrder find(String id) {
        return batteryOrderMapper.find(id);
    }

    public BatteryOrder createNewOrder(Customer customer, Battery battery, Cabinet cabinet, CabinetBox cabinetBox, BespeakOrder bespeakOrder) {
        Date date = new Date();

        BatteryOrder order = new BatteryOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        order.setPartnerId(customer.getPartnerId());
        order.setAgentId(cabinet.getAgentId());
        order.setBatteryType(battery.getType());
        order.setProvinceId(cabinet.getProvinceId());
        order.setCityId(cabinet.getCityId());
        order.setDistrictId(cabinet.getDistrictId());
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
        Shop shop = shopMapper.find(cabinet.getShopId());
        if(shop != null){
            order.setTakeShopId(shop.getId());
            order.setTakeShopName(shop.getShopName());
        }
        order.setTakeCabinetId(cabinet.getId());
        order.setTakeCabinetName(cabinet.getCabinetName());
        order.setTakeBoxNum(cabinetBox.getBoxNum());
        order.setTakeTime(new Date());
        order.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        order.setBatteryId(battery.getId());
        order.setInitVolume(battery.getVolume());
        order.setCurrentVolume(battery.getVolume());
        order.setInitCapacity(battery.getCurrentCapacity());
        order.setCurrentCapacity(battery.getCurrentCapacity());
        order.setOpenTime(date);
        order.setCreateTime(date);
        order.setAddress(cabinet.getAddress());
        order.setCurrentDistance(0);
        batteryOrderMapper.insert(order);

        batteryMapper.updateCustomerUse(cabinetBox.getBatteryId(), Battery.Status.IN_BOX_CUSTOMER_USE.getValue(), order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());

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

        //customerMapper.updateBattery(customer.getId(), battery.getId(), order.getId());

        //写入用户关联用户信息
        CustomerExchangeBattery customerExchangeBattery = new CustomerExchangeBattery();
        customerExchangeBattery.setCustomerId(customer.getId());
        customerExchangeBattery.setAgentId(cabinet.getAgentId());
        customerExchangeBattery.setBatteryId(battery.getId());
        customerExchangeBattery.setBatteryType(battery.getType());
        customerExchangeBattery.setBatteryOrderId(order.getId());
        customerExchangeBattery.setCreateTime(new Date());
        customerExchangeBatteryMapper.insert(customerExchangeBattery);

        PushMetaData metaData = new PushMetaData();
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_OPEN_NEW_BATTER_BOX.getValue());
        metaData.setSourceId(order.getId());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        Agent agent = agentMapper.find(cabinet.getAgentId());
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
