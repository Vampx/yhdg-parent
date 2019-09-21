package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.OrderDateCount;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryOrderService extends AbstractService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;


    public Page findPage(BatteryOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryOrder> list = batteryOrderMapper.findPageResult(search);
        for (BatteryOrder batteryOrder : list) {
            if (batteryOrder.getAgentId() != null) {
                batteryOrder.setAgentName(findAgentInfo(batteryOrder.getAgentId()).getAgentName());
            }
            if (StringUtils.isNotEmpty(batteryOrder.getTakeShopId())) {
                Shop shop = shopMapper.find(batteryOrder.getTakeShopId());
                if (shop != null) {
                    batteryOrder.setTakeShopName(shop.getShopName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public Page findPageForbalance(BatteryOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryOrderMapper.findPageForBalanceCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batteryOrderMapper.findPageForBalanceResult(search));
        return page;
    }

    public BatteryOrder find(String id) {
        return batteryOrderMapper.find(id);
    }

    public int findOrderCount(Integer agentId, String beginPrefixId, String endPrefixId, String takeCabinetId) {
        return batteryOrderMapper.findOrderCount(agentId, beginPrefixId, endPrefixId, takeCabinetId);
    }

    public List<OrderDateCount> findDateOrderCount(Integer agentId, String beginPrefixId, String endPrefixId, String takeCabinetId) {
        return batteryOrderMapper.findDateOrderCount(agentId, beginPrefixId, endPrefixId, takeCabinetId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult complete(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return ExtResult.failResult("订单不存在");
        }
        if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
            return ExtResult.failResult("订单已结束");
        }
        Customer customer = customerMapper.find(order.getCustomerId());


        int effect = batteryOrderMapper.complete(id, new Date(), ConstEnum.PayType.PLATFORM.getValue(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue());
        if (effect == 1) {

            if (customer != null && customerExchangeBatteryMapper.findByCustomerId(customer.getId()) != null) {
                BackBatteryOrder backBatteryOrder = backBatteryOrderMapper.findByCustomerId(order.getCustomerId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
                if (backBatteryOrder != null) {
                    // 设置取消时间
                    backBatteryOrderMapper.updateStatus(backBatteryOrder.getId(), BackBatteryOrder.OrderStatus.CANCEL.getValue(), new Date());
                    // 清空格口状态
                    cabinetBoxMapper.unlockBox(backBatteryOrder.getCabinetId(), backBatteryOrder.getBoxNum(), CabinetBox.BoxStatus.BACK_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                }
            }
            customerExchangeBatteryMapper.clearBattery(order.getCustomerId(), order.getBatteryId());
            batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());
            //如果格口状态为客户使用中，修改为已入箱
            if(order.getPutCabinetId() != null && order.getPutBoxNum() != null){
                CabinetBox box = cabinetBoxMapper.find(order.getPutCabinetId(), order.getPutBoxNum());
                if(box.getBatteryId() != null && order.getBatteryId().equals(box.getBatteryId())){
                    // 清空格口状态
                    cabinetBoxMapper.unlockBox(order.getPutCabinetId(), order.getPutBoxNum(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                }
            }else{
                //电池入库处理
//                if(order.getTakeShopId() != null  && order.getOrderStatus() == BatteryOrder.OrderStatus.TAKE_OUT.getValue()){
//                    ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
//                    shopStoreBattery.setCategory(Battery.Category.EXCHANGE.getValue());
//                    shopStoreBattery.setAgentId(order.getAgentId());
//                    AgentInfo agentInfo = findAgentInfo(order.getAgentId());
//                    shopStoreBattery.setAgentName(agentInfo.getAgentName());
//                    shopStoreBattery.setShopId(order.getTakeShopId());
//                    shopStoreBattery.setShopName(order.getTakeShopName());
//                    shopStoreBattery.setBatteryId(order.getBatteryId());
//                    shopStoreBattery.setCreateTime(new Date());
//                    shopStoreBatteryMapper.insert(shopStoreBattery);
//                }
            }

            Agent agent = agentMapper.find(order.getAgentId());
            if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                pushOrderMessage.setSourceId(order.getId());
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }

            return ExtResult.successResult();
        }

        return ExtResult.failResult("订单状态错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult exchangeBattery(String id, String batteryId) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return ExtResult.failResult("订单不存在");
        }
        Agent agent = agentMapper.find(order.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }
        if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
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

        batteryOrderMapper.updateBattery(order.getId(), newBattery.getId());
        customerExchangeBatteryMapper.updateBattery(order.getCustomerId(), newBattery.getId());
        batteryMapper.updateOrderId(newBattery.getId(), Battery.Status.CUSTOMER_OUT.getValue(),
                order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());

        batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());

 /*         Battery oldBattery = batteryMapper.find(order.getBatteryId());
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
*/
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult toBackBatteryOrder(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return ExtResult.failResult("订单不存在");
        }

        if (order.getOrderStatus() != BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue()) {
            return ExtResult.failResult("订单的状态是入柜未付款的才能转为退租订单");
        }

        //3天内此退租操作只能执行一次
        int exists = backBatteryOrderMapper.existLastOrder(order.getAgentId(), order.getCustomerId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        if (exists > 0) {
            return ExtResult.failResult("3天内退租操作只能执行一次");
        }

        Customer customer = customerMapper.find(order.getCustomerId());

        //结束换电订单
        batteryOrderMapper.complete(id, new Date(), ConstEnum.PayType.PLATFORM.getValue(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue());
        customerExchangeBatteryMapper.clearBattery(order.getCustomerId(), order.getBatteryId());
        batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.IN_BOX.getValue());

        //生成已完成的退租订单
        BackBatteryOrder backBatteryOrder = new BackBatteryOrder();
        backBatteryOrder.setId(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER));
        backBatteryOrder.setAgentId(order.getAgentId());
        backBatteryOrder.setBatteryId(order.getBatteryId());
        backBatteryOrder.setBoxNum(order.getPutBoxNum());
        backBatteryOrder.setCabinetId(order.getPutCabinetId());
        backBatteryOrder.setCabinetName(order.getPutCabinetName());
        backBatteryOrder.setCustomerId(order.getCustomerId());
        backBatteryOrder.setCustomerFullname(order.getCustomerFullname());
        backBatteryOrder.setCustomerMobile(order.getCustomerMobile());
        backBatteryOrder.setCreateTime(new Date());
        backBatteryOrder.setBackTime(new Date());
        backBatteryOrder.setOrderStatus(BackBatteryOrder.OrderStatus.EXCHANGE.getValue());
        backBatteryOrderMapper.insert(backBatteryOrder);

        Agent agent = agentMapper.find(order.getAgentId());
        if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
            PushOrderMessage pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
            pushOrderMessage.setSourceId(order.getId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);

            pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.BACK.getValue());
            pushOrderMessage.setSourceId(backBatteryOrder.getId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);
        }

        return ExtResult.successResult();
    }

}
