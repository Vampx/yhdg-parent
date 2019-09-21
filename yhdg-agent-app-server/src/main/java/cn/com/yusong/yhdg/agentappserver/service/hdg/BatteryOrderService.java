package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BatteryOrderService extends AbstractService {

    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;
    @Autowired
    ShopStoreBatteryMapper shopStoreBatteryMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;


    public BatteryOrder find(String id) {
        BatteryOrder batteryOrder = batteryOrderMapper.find(id);
        batteryOrder.setBatteryTypeName(findBatteryType(batteryOrder.getBatteryType()).getTypeName());
        return batteryOrder;
    }

    public BatteryOrder findByBatteryId(String batteryId) {
        return batteryOrderMapper.findByBatteryId(batteryId);
    }

    public List<BatteryOrder> findList(Integer agentId, String keyword, int offset, int limit) {
        List<BatteryOrder> list = batteryOrderMapper.findList(agentId, keyword, offset, limit);
        for (BatteryOrder batteryOrder : list) {
            batteryOrder.setBatteryTypeName(findBatteryType(batteryOrder.getBatteryType()).getTypeName());
        }
        return list;
    }

    public List<BatteryOrder> findListByCabinetId(Integer agentId, String cabinetId,Date beginTime,Date endTime) {
        return batteryOrderMapper.findListByCabinetId(agentId, cabinetId,beginTime, endTime);
    }

    public List<BatteryOrder> findByCabinetId(Integer agentId, String cabinetId, int offset, int limit) {
        List<BatteryOrder> list = batteryOrderMapper.findByCabinetId(agentId, cabinetId,offset, limit);
        for (BatteryOrder order : list) {
            order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
        }
        return list;
    }

    public int findCountByCabinetId(Integer agentId, String cabinetId, Integer orderStatus) {
        return batteryOrderMapper.findCountByCabinetId(agentId, cabinetId, orderStatus);
    }

    public int findActiveUserCountByCabinetId(Integer agentId, String cabinetId, List<Integer> status) {
        return batteryOrderMapper.findActiveUserCountByCabinetId(agentId, cabinetId, status);
    }

    public List<BatteryOrder> findByBatteryList(Integer agentId, String batteryId, String idAndName, int offset, int limit) {
        return batteryOrderMapper.findByBatteryList(agentId, batteryId, idAndName, offset, limit);
    }

    public List<BatteryOrder> findBatteryListByCustomer(Integer agentId, String batteryId, Long customerId) {
        return batteryOrderMapper.findBatteryListByCustomer(agentId, batteryId, customerId);
    }

    public List<BatteryOrder> findByBattery(Integer agentId, String shopId, String batteryId, String idAndName, int offset, int limit) {
        return batteryOrderMapper.findByBattery(agentId, shopId, batteryId, idAndName, offset, limit);
    }

    public List<BatteryOrder> findByShopBatteryList(Integer agentId, String batteryId, List cabinetList, int offset, int limit) {
        return batteryOrderMapper.findByShopBatteryList(agentId, batteryId, cabinetList, offset, limit);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult complete(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单不存在");
        }
        if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单已结束");
        }
        Customer customer = customerMapper.find(order.getCustomerId());
        Agent agent = agentMapper.find(order.getAgentId());
        int effect = batteryOrderMapper.complete(id, new Date(), ConstEnum.PayType.PLATFORM.getValue(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue());
        if (effect == 1) {
            Battery battery = batteryMapper.find(order.getBatteryId());

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
            } else{
                //电池入库处理
//                if(order.getTakeShopId() != null && order.getOrderStatus() == BatteryOrder.OrderStatus.TAKE_OUT.getValue()){
//                    ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
//                    shopStoreBattery.setCategory(Battery.Category.EXCHANGE.getValue());
//                    shopStoreBattery.setAgentId(order.getAgentId());
//                    shopStoreBattery.setAgentName(agent.getAgentName());
//                    shopStoreBattery.setShopId(order.getTakeShopId());
//                    shopStoreBattery.setShopName(order.getTakeShopName());
//                    shopStoreBattery.setBatteryId(order.getBatteryId());
//                    shopStoreBattery.setCreateTime(new Date());
//                    shopStoreBatteryMapper.insert(shopStoreBattery);
//                }
            }

            if(agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                pushOrderMessage.setSourceId(order.getId());
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }

            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
        }

        return RestResult.result(RespCode.CODE_1.getValue(), "订单状态错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult exchangeBattery(String id, String batteryId) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单不存在");
        }
        Agent agent = agentMapper.find(order.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "运营商不存在");
        }
        if (order.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue() || order.getOrderStatus() == BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue()) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单的状态是使用中的才能交换");
        }

        Battery newBattery = batteryMapper.find(batteryId);
        if (newBattery == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "电池不存在");
        }
        if (newBattery.getAgentId() != order.getAgentId().intValue()) {
            return RestResult.result(RespCode.CODE_1.getValue(), "电池运营商不一致无法交换");
        }
        if (newBattery.getStatus() != Battery.Status.NOT_USE.getValue() && newBattery.getStatus() != Battery.Status.KEEPER_OUT.getValue()) {
            return RestResult.result(RespCode.CODE_1.getValue(), "电池状态是未使用或维护取出电池才能交换");
        }

        batteryOrderMapper.updateBattery(order.getId(), newBattery.getId());
        customerExchangeBatteryMapper.updateBattery(order.getCustomerId(), order.getBatteryId(), newBattery.getId());
        batteryMapper.updateOrderId(newBattery.getId(), Battery.Status.CUSTOMER_OUT.getValue(),
                order.getId(), new Date(), order.getCustomerId(), order.getCustomerMobile(), order.getCustomerFullname());

        batteryMapper.clearCustomer(order.getBatteryId(), Battery.Status.NOT_USE.getValue());

//        Battery oldBattery = batteryMapper.find(order.getBatteryId());
//        ShopStoreBattery newShopStoreBattery  = shopStoreBatteryMapper.findByBattery(newBattery.getId());

        //入库处理
//        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(order.getCustomerId());
//        Shop oldShop = null;
//        if(StringUtils.isNotEmpty(customerExchangeInfo.getBalanceShopId())){
//            oldShop = shopMapper.find(customerExchangeInfo.getBalanceShopId());
//        }
//
//        //旧电池属于门店
//        if(oldShop != null){
//            //新电池属于当前门店   更新库存电池  旧电池入库  新电池出库
//            if(newShopStoreBattery != null && newShopStoreBattery.getShopId().equals(oldShop.getId())){
//                ShopStoreBattery storeBattery = new ShopStoreBattery();
//                storeBattery.setCategory(oldBattery.getCategory());
//                storeBattery.setAgentId(agent.getId());
//                storeBattery.setAgentName(agent.getAgentName());
//                storeBattery.setAgentCode(agent.getAgentCode());
//                storeBattery.setShopId(oldShop.getId());
//                storeBattery.setShopName(oldShop.getShopName());
//                storeBattery.setBatteryId(oldBattery.getId());
//                storeBattery.setCreateTime(new Date());
//                shopStoreBatteryMapper.insert(storeBattery);
//
//                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
//            }
//            //新电池属于其他门店电池 旧电池入其他门店   新电池出库
//            else if(newShopStoreBattery != null && !newShopStoreBattery.getShopId().equals(oldShop.getId())){
//                ShopStoreBattery storeBattery = new ShopStoreBattery();
//                storeBattery.setCategory(oldBattery.getCategory());
//                storeBattery.setAgentId(agent.getId());
//                storeBattery.setAgentName(agent.getAgentName());
//                storeBattery.setAgentCode(agent.getAgentCode());
//                Shop newShop = shopMapper.find(newShopStoreBattery.getShopId());
//                storeBattery.setShopId(newShop.getId());
//                storeBattery.setShopName(newShop.getShopName());
//                storeBattery.setBatteryId(oldBattery.getId());
//                storeBattery.setCreateTime(new Date());
//                shopStoreBatteryMapper.insert(storeBattery);
//
//                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
//            }
//        }
//        //旧电不属于门店
//        else{
//            //新电池属于门店 旧电池入门店   新电池出库
//            if(newShopStoreBattery != null) {
//                ShopStoreBattery storeBattery = new ShopStoreBattery();
//                storeBattery.setCategory(oldBattery.getCategory());
//                storeBattery.setAgentId(agent.getId());
//                storeBattery.setAgentName(agent.getAgentName());
//                storeBattery.setAgentCode(agent.getAgentCode());
//                Shop newShop = shopMapper.find(newShopStoreBattery.getShopId());
//                storeBattery.setShopId(newShop.getId());
//                storeBattery.setShopName(newShop.getShopName());
//                storeBattery.setBatteryId(oldBattery.getId());
//                storeBattery.setCreateTime(new Date());
//                shopStoreBatteryMapper.insert(storeBattery);
//
//                shopStoreBatteryMapper.deleteByShopBatteryId(newShopStoreBattery.getShopId(), newBattery.getId());
//            }
//        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult toBackBatteryOrder(String id) {
        BatteryOrder order = batteryOrderMapper.find(id);
        if (order == null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单不存在");
        }

        if (order.getOrderStatus() != BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue()) {
            return RestResult.result(RespCode.CODE_1.getValue(), "订单的状态是入柜未付款的才能转为退租订单");
        }

        //3天内此退租操作只能执行一次
        int exists = backBatteryOrderMapper.existLastOrder(order.getAgentId(), order.getCustomerId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        if (exists > 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "3天内退租操作只能执行一次");
        }

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

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    public RestResult findBatteryReportLogByOrderId(String orderId, String trackTime, int offset, int limit) {
        String suffix = null;
        if (orderId != null) {
            BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
            if (batteryOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
            }
            suffix= DateFormatUtils.format(batteryOrder.getCreateTime(), "yyyyww");
        }else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                suffix = DateFormatUtils.format(format.parse(trackTime), "yyyyww");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isEmpty(batteryOrderBatteryReportLogMapper.findTableExist("hdg_battery_order_battery_report_log_" + suffix))) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在");
        }

        List<BatteryOrderBatteryReportLog> reportLoglist = batteryOrderBatteryReportLogMapper.findListByOrderId(orderId, trackTime, suffix, offset, limit);
        List<Map> list = new ArrayList<Map>(reportLoglist.size());
        if (!reportLoglist.isEmpty()) {
            for (BatteryOrderBatteryReportLog batteryOrderBatteryReportLog : reportLoglist) {
                NotNullMap line = new NotNullMap(30);
                line.putString("createTime", DateFormatUtils.format(batteryOrderBatteryReportLog.getReportTime(), "yyyy-MM-dd HH:mm:ss"));
                line.putDouble("lat", batteryOrderBatteryReportLog.getLat());
                line.putDouble("lng", batteryOrderBatteryReportLog.getLng());
                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);
    }

    /**
     * 根据门店id查询订单列表
     * @param shopId
     * @param keyword
     * @param offset
     * @param limit
     * @return
     */
    public List<BatteryOrder> findListByShop(String shopId, String keyword, int offset, int limit) {
        List<BatteryOrder> list = batteryOrderMapper.findListByShop(shopId, keyword, offset, limit);
        for (BatteryOrder order : list) {
            order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
        }
        return list;
    }

    public int countShopTodayOrderNum(String shopId, Date startTime, Date endTime) {
        return batteryOrderMapper.countShopTodayOrderNum(shopId, startTime, endTime);
    }

    public RestResult customerOverTimeList(int agentId, int offset, int limit) {
        List<Map> list = new ArrayList<Map>();
        Date now = new Date();

        Integer timeout = Integer.valueOf(systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.BATTERY_BINDING_TIME.getValue()));
           if(timeout == null){
               timeout = 1;
           }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -timeout);

        int count = batteryOrderMapper.findTakeTimeoutCount(agentId, BatteryOrder.OrderStatus.TAKE_OUT.getValue(), calendar.getTime());


        List<BatteryOrder> batteryOrderList = batteryOrderMapper.findTakeTimeout(agentId, BatteryOrder.OrderStatus.TAKE_OUT.getValue(), calendar.getTime(), offset, limit);
        for (BatteryOrder batteryOrder : batteryOrderList) {
            Map line = new HashMap();
            line.put("cabinetName", batteryOrder.getTakeCabinetName());
            line.put("takeTime", DateFormatUtils.format(batteryOrder.getTakeTime(), Constant.DATE_TIME_FORMAT));
            long overTime = (now.getTime() - batteryOrder.getTakeTime().getTime())/1000/60/60 ;
            line.put("overTime", (int)overTime);
            line.put("customerName", batteryOrder.getCustomerFullname());
            line.put("customerMoblie", batteryOrder.getCustomerMobile());
            line.put("batteryId", batteryOrder.getBatteryId());
            line.put("volume", batteryOrder.getCurrentVolume());
            list.add(line);
        }

        Map returnMap = new HashMap();
        returnMap.put("count",count);
        returnMap.put("orderList",list);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
    }
}
