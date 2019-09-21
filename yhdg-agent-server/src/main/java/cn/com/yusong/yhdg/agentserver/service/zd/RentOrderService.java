package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopStoreBatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.commons.lang.StringUtils;
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

/*
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
*/


        return ExtResult.successResult();
    }
//    @Transactional(rollbackFor = Throwable.class)
//    public ExtResult extendRent(RentOrder entity, String operator) {
//        RentOrder dbRentOrder = rentOrderMapper.find(entity.getId());
//        //增加天数
//        dbRentOrder.setDayCount(dbRentOrder.getDayCount() + entity.getDayCount());
//        //延长过期时间
//        Date newEndTime = DateUtils.addDays(dbRentPeriodOrder.getEndTime(), entity.getDayCount());
//        dbRentPeriodOrder.setEndTime(newEndTime);
//        //更新状态
//        if (newEndTime.compareTo(new Date()) > 0) {
//            if (dbRentPeriodOrder.getStatus() == RentPeriodOrder.Status.EXPIRED.getValue()) {
//                dbRentPeriodOrder.setStatus(RentPeriodOrder.Status.USED.getValue());
//            }
//        }
//
//        Date now = new Date();
//        String nowDate = DateFormatUtils.format(now, Constant.DATE_TIME_FORMAT);
//        String operatorMemo = "";
//        if (StringUtils.isEmpty(dbRentPeriodOrder.getOperatorMemo())) {
//            operatorMemo = "" + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" + "\n";
//        } else {
//            operatorMemo = dbRentPeriodOrder.getOperatorMemo() + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" +"\n";
//        }
//
//        int result = rentPeriodOrderMapper.extendRent(dbRentPeriodOrder.getId(), dbRentPeriodOrder.getDayCount(), dbRentPeriodOrder.getEndTime(), dbRentPeriodOrder.getStatus(), operatorMemo);
//        if (result == 0) {
//            return ExtResult.failResult("操作失败");
//        }
//        return ExtResult.successResult();
//    }
//
//    public List<RentPeriodOrder> findCanRefundByCustomerId(Long customerId) {
//        return rentPeriodOrderMapper.findCanRefundByCustomerId(customerId);
//    }
    public ExtResult updateStatusById(String id){
        RentOrder rentOrder=rentOrderMapper.find(id);
        if(rentOrder==null){
            return ExtResult.failResult("订单不存在");
        }
        if(rentOrder.getStatus()==RentOrder.Status.BACK.getValue()){
            return ExtResult.failResult(RentOrder.Status.BACK.getName());
        }else{
            rentOrderMapper.updateStatusById(id);
        }
        return ExtResult.successResult();
    }
}
