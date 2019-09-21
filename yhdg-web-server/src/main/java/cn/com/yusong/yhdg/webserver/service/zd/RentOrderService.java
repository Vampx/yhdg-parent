package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RentOrderService extends AbstractService {
    @Autowired
    private RentOrderMapper rentOrderMapper;
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private BatteryMapper batteryMapper;
    @Autowired
    private CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    private ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    private CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    private ShopMapper shopMapper;
    public Page findPage(RentOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentOrder> list = rentOrderMapper.findPageResult(search);
        for (RentOrder rentOrder: list) {
            Integer agentId = rentOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                rentOrder.setAgentName(findAgentInfo(rentOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (rentOrder.getBatteryType() != null) {
                rentOrder.setBatteryTypeName(findBatteryType(rentOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }

    public RentOrder find(String id) {
        return rentOrderMapper.find(id);
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult exchangeBattery(String id, String batteryId) {
        RentOrder order = rentOrderMapper.find(id);
        if (order == null) {
            return ExtResult.failResult("订单不存在");
        }
        Agent agent = agentMapper.find(order.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }
        if (order.getStatus() == RentOrder.Status.BACK.getValue()) {
            return ExtResult.failResult("订单的状态是使用中的才能完成");
        }

        Battery newBattery = batteryMapper.find(batteryId);
        if (newBattery == null) {
            return ExtResult.failResult("电池不存在");
        }
        if (newBattery.getAgentId() != order.getAgentId().intValue()) {
            return ExtResult.failResult("电池运营商不一致无法交换");
        }
        if (newBattery.getStatus() != Battery.Status.NOT_USE.getValue() && newBattery.getStatus() != Battery.Status.KEEPER_OUT.getValue()) {
            return ExtResult.failResult("电池状态是未使用或维护取出电池才能交换");
        }

        rentOrderMapper.updateBattery(order.getId(), newBattery.getId());
        customerExchangeBatteryMapper.updateBattery(order.getCustomerId(), newBattery.getId());
        batteryMapper.updateOrderId(newBattery.getId(), Battery.Status.CUSTOMER_OUT.getValue(),
                order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());

        batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());

        Battery oldBattery = batteryMapper.find(order.getBatteryId());
        ShopStoreBattery newShopStoreBattery  = shopStoreBatteryMapper.findByBattery(newBattery.getId());

        //入库处理
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(order.getCustomerId());
        Shop oldShop = null;
        if(StringUtils.isNotEmpty(customerExchangeInfo.getBalanceShopId())){
            oldShop = shopMapper.find(customerExchangeInfo.getBalanceShopId());
        }

        //旧电池属于门店
        if(oldShop != null){
            //新电池属于当前门店   更新库存电池  旧电池入库  新电池出库
            if(newShopStoreBattery != null && newShopStoreBattery.getShopId().equals(oldShop.getId())){
                ShopStoreBattery storeBattery = new ShopStoreBattery();
                storeBattery.setCategory(oldBattery.getCategory());
                storeBattery.setAgentId(agent.getId());
                storeBattery.setAgentName(agent.getAgentName());
                storeBattery.setAgentCode(agent.getAgentCode());
                storeBattery.setShopId(oldShop.getId());
                storeBattery.setShopName(oldShop.getShopName());
                storeBattery.setBatteryId(oldBattery.getId());
                storeBattery.setCreateTime(new Date());
                shopStoreBatteryMapper.insert(storeBattery);

                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
            }
            //新电池属于其他门店电池 旧电池入其他门店   新电池出库
            else if(newShopStoreBattery != null && !newShopStoreBattery.getShopId().equals(oldShop.getId())){
                ShopStoreBattery storeBattery = new ShopStoreBattery();
                storeBattery.setCategory(oldBattery.getCategory());
                storeBattery.setAgentId(agent.getId());
                storeBattery.setAgentName(agent.getAgentName());
                storeBattery.setAgentCode(agent.getAgentCode());
                Shop newShop = shopMapper.find(newShopStoreBattery.getShopId());
                storeBattery.setShopId(newShop.getId());
                storeBattery.setShopName(newShop.getShopName());
                storeBattery.setBatteryId(oldBattery.getId());
                storeBattery.setCreateTime(new Date());
                shopStoreBatteryMapper.insert(storeBattery);

                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
            }
        }
        //旧电不属于门店
        else{
            //新电池属于门店 旧电池入门店   新电池出库
            if(newShopStoreBattery != null) {
                ShopStoreBattery storeBattery = new ShopStoreBattery();
                storeBattery.setCategory(oldBattery.getCategory());
                storeBattery.setAgentId(agent.getId());
                storeBattery.setAgentName(agent.getAgentName());
                storeBattery.setAgentCode(agent.getAgentCode());
                Shop newShop = shopMapper.find(newShopStoreBattery.getShopId());
                storeBattery.setShopId(newShop.getId());
                storeBattery.setShopName(newShop.getShopName());
                storeBattery.setBatteryId(oldBattery.getId());
                storeBattery.setCreateTime(new Date());
                shopStoreBatteryMapper.insert(storeBattery);

                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
            }
        }


        return ExtResult.successResult();
    }
    public ExtResult updateStatusById(String id){
        RentOrder rentOrder=rentOrderMapper.find(id);
        if(rentOrder==null){
            return ExtResult.failResult("订单不存在");
        }
        if(rentOrder.getStatus()==RentOrder.Status.BACK.getValue()){
            return ExtResult.failResult(RentOrder.Status.BACK.getName());
        }else{
            rentOrderMapper.updateStatusById(id,RentOrder.Status.BACK.getValue(),new Date());
        }
        return ExtResult.successResult();
    }
}
