package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.appserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
@Service
public class BespeakOrderService extends AbstractService{

    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
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
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    AgentMapper agentMapper;

    public BespeakOrder find(String id) {
        return bespeakOrderMapper.find(id);
    }

    public BespeakOrder findSuccessByCustomer(long customerId) {
        return bespeakOrderMapper.findSuccessByCustomer(customerId, BespeakOrder.Status.SUCCESS.getValue());
    }

    public int findFailCountForToday(long customerId) {
        return bespeakOrderMapper.findFailCountForToday(customerId);
    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult cancelOrder(String id, long customerId) {
        BespeakOrder order = bespeakOrderMapper.find(id);
        if(order == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        // 取消时间需要设置
        if(bespeakOrderMapper.updateStatus(id, BespeakOrder.Status.SUCCESS.getValue(), BespeakOrder.Status.CANCEL.getValue(), new Date()) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "更新订单状态出错");
        }

        // 清空格口状态
        cabinetBoxMapper.unlockBox(order.getBespeakCabinetId(), order.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.FULL.getValue());

        return RestResult.SUCCESS;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult createOrder(String cabinetId, String boxNum, long customerId) {
        Customer customer = customerMapper.find(customerId);
        if(customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Agent agent = agentMapper.find(cabinet.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        CabinetBox cabinetBox = cabinetBoxMapper.find(cabinetId, boxNum);
        if (cabinetBox == null) {
            return  RestResult.result(RespCode.CODE_2.getValue(),"预约箱号不存在");
        }
        if(cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.FULL.getValue()){
            return  RestResult.result(RespCode.CODE_2.getValue(),"箱门状态不允许预约");
        }
        Battery bespeakBattery = batteryMapper.find(cabinetBox.getBatteryId());
        if (bespeakBattery == null) {
            return  RestResult.result(RespCode.CODE_2.getValue(),"预约箱中电池不存在");
        }

        String boxCount = agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_BOX_COUNT.getValue(), cabinet.getAgentId());
        if(StringUtils.isEmpty(boxCount)){//默认10个格口
            boxCount = "10";
        }
        if(cabinetBoxMapper.findBespeakCount(cabinetId, CabinetBox.BoxStatus.BESPEAK.getValue()) >= Integer.parseInt(boxCount)){
            return RestResult.result(RespCode.CODE_2.getValue(), "可预约箱门数已满不允许预约");
        }

        BespeakOrder bespeakOrder = findSuccessByCustomer(customerId);
        if(bespeakOrder != null){
            return RestResult.result(RespCode.CODE_2.getValue(), "已存在预约订单，不允许重复预约");
        }

        String count = agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_MAX_CANCEL.getValue(), cabinet.getAgentId());
        if(StringUtils.isEmpty(count)){//默认2次
            count = "2";
        }
        if(bespeakOrderMapper.findFailCountForToday(customerId) >= Integer.parseInt(count)){
            return RestResult.result(RespCode.CODE_2.getValue(), "已超出当日预约次数不允许预约");
        }

        Integer batteryType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
        if(customerExchangeInfo != null){
            batteryType = customerExchangeInfo.getBatteryType();
        }else{
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListMapper.findByCustomer(cabinet.getAgentId(), customerId);
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }
        }
        if(batteryType == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "请先交押金再预约");
        }
        if(batteryType != bespeakBattery.getType().intValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "与箱门中电池类型不一致不允许预约");
        }

        //用户存在电池时进行判断
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryMapper.findListByCustomer(customerId);
        if(batteryList.size() > 0) {
            boolean statusFlag = false;
            boolean volumeFlag = false;
            for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                Battery battery = batteryMapper.find(customerExchangeBattery.getBatteryId());
                if(battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                    statusFlag = true;
                }
                if(battery.getVolume() != null && battery.getVolume() <= cabinet.getPermitExchangeVolume()) {
                    volumeFlag = true;
                }
            }
            if(!statusFlag) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池状态不允许预约");
            }
            if(!volumeFlag) {
                return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池电量大于可换电电量%d不允许预约",cabinet.getPermitExchangeVolume()));
            }
        }

        if (cabinetBoxMapper.lockBoxByBackBattery (
                cabinetId,
                boxNum,
                new Date(),
                CabinetBox.BoxStatus.FULL.getValue(),
                CabinetBox.BoxStatus.BESPEAK.getValue()) == 0) {
            return  RestResult.result(RespCode.CODE_1.getValue(),"锁定满箱失败");
        }


        //生产订单
        String mins = agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_TIME.getValue(), cabinet.getAgentId());
        if(StringUtils.isEmpty(mins)){//默认30分钟
            mins = "30";
        }
        Date expireTime =  DateUtils.addMinutes(new Date(), Integer.parseInt(mins));

        BespeakOrder order = new BespeakOrder();
        order.setId( orderIdService.newOrderId(OrderId.OrderIdType.BESPEAK_ORDER));
        order.setPartnerId(customer.getPartnerId());
        order.setAgentId(cabinet.getAgentId());
        order.setAgentName(agent.getAgentName());
        order.setCustomerId(customerId);
        order.setCustomerMobile(customer.getMobile());
        order.setCustomerFullname(customer.getFullname());
        order.setBespeakCabinetId(cabinetId);
        order.setBespeakCabinetName(cabinet.getCabinetName());
        order.setBespeakBoxNum(boxNum);
        order.setBespeakBatteryId(cabinetBox.getBatteryId());
        order.setStatus(BespeakOrder.Status.SUCCESS.getValue());
        order.setExpireTime(expireTime);
        order.setCreateTime(new Date());
        order.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        bespeakOrderMapper.insert(order);

        Map<String,String> map = new HashMap<String,String>();
        map.put("orderId",order.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(),"",map);
    }
}
