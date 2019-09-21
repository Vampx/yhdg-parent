package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;

import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.domain.basic.CustomerPayTrack;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
@Service
public class BackBatteryOrderService extends AbstractService{

    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;

    public BackBatteryOrder find(String id) {
        return backBatteryOrderMapper.find(id);
    }

    public BackBatteryOrder findBatteryOrder(long customerId, Integer orderStatus) {
        return backBatteryOrderMapper.findBatteryOrder(customerId, orderStatus);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult cancelOrder(String id, long customerId) {
        BackBatteryOrder order = backBatteryOrderMapper.find(id);
        if(order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        // 取消时间需要设置
        if(backBatteryOrderMapper.updateStatus(id, BackBatteryOrder.OrderStatus.SUCCESS.getValue(), BackBatteryOrder.OrderStatus.CANCEL.getValue(), new Date()) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "更新订单状态出错");
        }

        // 清空格口状态
        cabinetBoxMapper.unlockBox(order.getCabinetId(), order.getBoxNum(), CabinetBox.BoxStatus.BACK_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());

        return RestResult.SUCCESS;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult createOrder(String cabinetId, long customerId) {
        Customer customer = customerMapper.find(customerId);
        if(customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryMapper.findListByCustomer(customerId);
        if(batteryList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), " 电池不存在");
        }

        boolean backFlag = false;
        for(CustomerExchangeBattery customerExchangeBattery : batteryList){
            Battery battery = batteryMapper.find(customerExchangeBattery.getBatteryId());
            if(battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                backFlag = true;
                break;
            }
        }

        if(!backFlag) {
            return RestResult.result(RespCode.CODE_2.getValue(), " 电池状态不允许退租");
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        //查询客户上次申请退租与本次间隔时间，如果小于3天，不允许退租
//        int exists = backBatteryOrderMapper.existLastOrder(cabinet.getAgentId(), customer.getId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
//        if(exists > 0 ){
//            return RestResult.result(RespCode.CODE_2.getValue(), "与上次退租时隔过短，不允许退租");
//        }

        List<CabinetBox> boxList = cabinetBoxMapper.findAllEmpty(cabinetId,CabinetBox.BoxStatus.EMPTY.getValue());
        if (boxList.size() <= 1) {
            return  RestResult.result(RespCode.CODE_1.getValue(),"申请空箱失败");
        }

        //得到一个空箱
        CabinetBox box = boxList.get((int) (Math.random() * boxList.size()));

        if (cabinetBoxMapper.lockBoxByBackBattery (
                box.getCabinetId(),
                box.getBoxNum(),
                new Date(),
                CabinetBox.BoxStatus.EMPTY.getValue(),
                CabinetBox.BoxStatus.BACK_LOCK.getValue()) == 0) {
            return  RestResult.result(RespCode.CODE_1.getValue(),"锁定空箱失败");
        }

        String mins;
        if (cabinet.getAgentId() != null && cabinet.getAgentId() != 0) {
            mins = findAgentConfigValue(ConstEnum.SystemConfigKey.BACK_BESPEAK_TIME.getValue(), cabinet.getAgentId());
        } else {
            mins = findConfigValue(ConstEnum.SystemConfigKey.BACK_BESPEAK_TIME.getValue());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, Integer.parseInt(mins));
        //普通退租过期时间24小时
        Date expireTime = calendar.getTime();
        BackBatteryOrder order = insertBackBatteryOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER),
                cabinet.getAgentId(),
                box.getBoxNum(),
                cabinetId,
                cabinet.getCabinetName(),
                customerId,
                customer.getFullname(),
                customer.getMobile(),
                BackBatteryOrder.OrderStatus.SUCCESS.getValue(),
                expireTime);

     // customerMapper.updateBackBatteryOrderId(customerId, order.getId());

        Map<String,String> map = new HashMap<String,String>();
        map.put("orderId",order.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult createFastOrder(String cabinetId, long customerId) {
        Customer customer = customerMapper.find(customerId);
        if(customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryMapper.findListByCustomer(customerId);
        if(batteryList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), " 电池不存在");
        }


        boolean backFlag = false;
        for(CustomerExchangeBattery customerExchangeBattery : batteryList){
            Battery battery = batteryMapper.find(customerExchangeBattery.getBatteryId());
            if(battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                backFlag = true;
                break;
            }
        }

        if(!backFlag) {
            return RestResult.result(RespCode.CODE_2.getValue(), " 电池状态不允许退租");
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        //查询客户上次申请退租与本次间隔时间，如果小于3天，不允许退租
        int exists = backBatteryOrderMapper.existLastOrder(cabinet.getAgentId(), customer.getId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        if(exists > 0 ){
            return RestResult.result(RespCode.CODE_2.getValue(), "与上次退租时隔过短，不允许退租");
        }

        List<CabinetBox> boxList = cabinetBoxMapper.findAllEmpty(cabinetId,CabinetBox.BoxStatus.EMPTY.getValue());
        if (boxList.size() <= 1) {
            return  RestResult.result(RespCode.CODE_1.getValue(),"申请空箱失败");
        }

        //得到一个空箱
        CabinetBox box = boxList.get((int) (Math.random() * boxList.size()));

        if (cabinetBoxMapper.lockBoxByBackBattery (
                box.getCabinetId(),
                box.getBoxNum(),
                new Date(),
                CabinetBox.BoxStatus.EMPTY.getValue(),
                CabinetBox.BoxStatus.BACK_LOCK.getValue()) == 0) {
            return  RestResult.result(RespCode.CODE_1.getValue(),"锁定空箱失败");
        }

        String mins;
        if (cabinet.getAgentId() != null && cabinet.getAgentId() != 0) {
            mins = agentSystemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.BACK_BESPEAK_TIME.getValue(), cabinet.getAgentId());
        } else {
            mins = systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.BACK_BESPEAK_TIME.getValue());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, Integer.parseInt(mins));
        //快速退租过期时间五分钟
        Date expireTime = calendar.getTime();
        BackBatteryOrder order = insertBackBatteryOrder(
                orderIdService.newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER),
                cabinet.getAgentId(),
                box.getBoxNum(),
                cabinetId,
                cabinet.getCabinetName(),
                customerId,
                customer.getFullname(),
                customer.getMobile(),
                BackBatteryOrder.OrderStatus.SUCCESS.getValue(),
                expireTime);
  //      customerMapper.updateBackBatteryOrderId(customerId, order.getId());

        Map<String,String> map = new HashMap<String,String>();
        map.put("orderId",order.getId());

        //客户退电池消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(customer.getAgentId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.BACK_BATTERY.getValue());
        customerPayTrack.setMemo("退电池");
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        return RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }

    private BackBatteryOrder insertBackBatteryOrder(String id, Integer agentId, String boxNum, String cabinetId, String cabinetName,
                                                    Long customerId, String customerFullname, String customerMobile,
                                                    Integer orderStatus, Date expireTime) {
        BackBatteryOrder order = new BackBatteryOrder();
        order.setId(id);
        order.setAgentId(agentId);
        order.setBoxNum(boxNum);
        order.setCabinetId(cabinetId);
        order.setCabinetName(cabinetName);
        order.setCustomerId(customerId);
        order.setCustomerFullname(customerFullname);
        order.setCustomerMobile(customerMobile);
        order.setCreateTime(new Date());
        order.setOrderStatus(orderStatus);
        order.setExpireTime(expireTime);
        backBatteryOrderMapper.insert(order);
        return order;
    }
}
