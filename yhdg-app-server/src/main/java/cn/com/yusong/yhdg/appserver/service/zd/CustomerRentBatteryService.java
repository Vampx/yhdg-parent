package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerRentBatteryService  extends AbstractService {

    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    RentOrderMapper rentOrderMapper;

    public List<CustomerRentBattery> findListByCustomer(long customerId) {
        return customerRentBatteryMapper.findListByCustomer(customerId);
    }

    public int exists(Long customerId) {
        return customerRentBatteryMapper.exists(customerId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int bind(Agent agent, Customer customer, Battery battery, Shop shop, CustomerRentInfo customerRentInfo) {
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
        rentOrder.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
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
        return  effect;
    }
}
