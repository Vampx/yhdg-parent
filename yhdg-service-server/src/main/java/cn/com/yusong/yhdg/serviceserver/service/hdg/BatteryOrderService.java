package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BatteryOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(BatteryOrderService.class);

    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryOrderHistoryMapper batteryOrderHistoryMapper;

    /**
     * 换电订单取电超时
     */
    public void batteryOrderNotTakeTimeOut() {
        Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.BATTERY_ORDER_NOT_TAKE_TIMEOUT.getValue()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -(timeout == null ? 0 : timeout));
        List<BatteryOrder> batteryOrders = batteryOrderMapper.findNotTakeTimeout(BatteryOrder.OrderStatus.INIT.getValue(), calendar.getTime());

        for (BatteryOrder batteryOrder : batteryOrders) {
            insertPushMetaData(PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT.getValue(), batteryOrder.getId());
            insertPushMetaData(PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT.getValue(), batteryOrder.getId());
            Cabinet cabinet = cabinetMapper.find(batteryOrder.getTakeCabinetId());
            if(cabinet != null){
                AgentInfo agent = findAgentInfo(cabinet.getAgentId());
                FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), batteryOrder.getId(), batteryOrder.getProvinceId(), batteryOrder.getCityId(), batteryOrder.getDistrictId(), cabinet.getDispatcherId(), agent.getId(), agent.getAgentName(), batteryOrder.getBatteryId(), cabinet.getId(), cabinet.getCabinetName(), cabinet.getAddress(), batteryOrder.getTakeBoxNum(), FaultLog.FaultType.CODE_27.getValue(), PushMessage.SourceType.BATTERY_ORDER_NOT_TAKE_TIMEOUT.getName());
                batteryOrderMapper.updateNotTakeTimeout(batteryOrder.getId(), faultLog.getId());
            }
        }
    }

    /**
     * 用户用电超时
     */
    public void batteryOrderTakeTimeOut() {
        log.debug("用户用电超时推送...");
        Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.BATTERY_BINDING_TIME.getValue()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -(timeout == null ? 1 : timeout));
        List<BatteryOrder> batteryOrders = batteryOrderMapper.findTakeTimeout(BatteryOrder.OrderStatus.TAKE_OUT.getValue(), calendar.getTime());

        for (BatteryOrder batteryOrder : batteryOrders) {
            insertPushMetaData(PushMessage.SourceType.CUSTOMER_BATTERY_OVERTIME.getValue(), String.format("%d:%d", batteryOrder.getAgentId(), batteryOrder.getOrderCount()));
//            Cabinet cabinet = cabinetMapper.find(batteryOrder.getTakeCabinetId());
//            if(cabinet != null){
//                AgentInfo agent = findAgentInfo(cabinet.getAgentId());
//                FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), batteryOrder.getId(), batteryOrder.getProvinceId(), batteryOrder.getCityId(), batteryOrder.getDistrictId(), cabinet.getDispatcherId(), agent.getId(), agent.getAgentName(), batteryOrder.getBatteryId(), cabinet.getId(), cabinet.getCabinetName(), cabinet.getAddress(), batteryOrder.getTakeBoxNum(), FaultLog.FaultType.CODE_18.getValue(), PushMessage.SourceType.CUSTOMER_BATTERY_OVERTIME.getName());
//                batteryOrderMapper.updateTakeTimeout(batteryOrder.getId(), faultLog.getId());
//            }
        }
        log.debug("用户用电超时推送结束...");
    }

    /**
     * 移动三个月前历史数据
     */
    @Transactional(rollbackFor = Throwable.class)
    public void moveHistory() {
        Date date = new Date();
        date = DateUtils.addMonths(date, -3);
        String dayId = OrderId.PREFIX_BATTERY + DateFormatUtils.format(date, OrderId.DATE_FORMAT);
        List<String> monthIds = batteryOrderMapper.findHistoryId(dayId, BatteryOrder.OrderStatus.PAY.getValue());
        for (String monthId : monthIds) {
            String suffix = monthId.replace(OrderId.PREFIX_BATTERY, "20");
            String tableName = BatteryOrder.BATTERY_ORDER_TABLE_NAME + monthId.replace(OrderId.PREFIX_BATTERY, "20");
            List<String> tables=batteryOrderHistoryMapper.findTable(tableName);
            if (tables.isEmpty()) {
                batteryOrderHistoryMapper.createTable(suffix);
            }
            batteryOrderHistoryMapper.move(suffix, monthId, dayId, BatteryOrder.OrderStatus.PAY.getValue());
            batteryOrderMapper.deleteHistory(monthId, dayId, BatteryOrder.OrderStatus.PAY.getValue());
        }
    }
}
