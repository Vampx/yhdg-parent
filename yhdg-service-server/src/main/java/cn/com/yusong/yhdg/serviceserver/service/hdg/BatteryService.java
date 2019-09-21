package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BatteryService extends AbstractService {
    final static Logger log = LogManager.getLogger(BatteryService.class);

    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetMapper cabinetMapper;

    /**
     * 电池离线
     */
    public void refreshOnlineStatus() {
        Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.BATTERY_OFFLINE_TIME.getValue()));
        Calendar reportTime = Calendar.getInstance();
        reportTime.add(Calendar.MINUTE, -timeout);
        List<Battery> batteryList = batteryMapper.findOnline(reportTime.getTime(), ConstEnum.Flag.TRUE.getValue());

        for (Battery battery : batteryList) {
            batteryMapper.updateOnline(battery.getId(), ConstEnum.Flag.FALSE.getValue());
        }
    }

    /**
     * 电池使用天数
     */
    public void batteryUseDay() {

        int offset = 0, limit = 1000;
        while (true) {
            List<Battery> batteryList = batteryMapper.findAll(offset, limit);
            if (batteryList.isEmpty()) {
                break;
            }
            for(Battery battery: batteryList){
                if(battery.getUpLineTime() != null) {
                    int day = (int) getUseDay(battery.getUpLineTime());
                    batteryMapper.updateUseDay(battery.getId(), day);
                } else {
                    batteryMapper.updateUseDay(battery.getId(), 0);
                }
            }

            offset += limit;
        }
    }

    /**
     * 未绑定电池在外超时异常
     */
    public void unbindBatteryOutOverTime() {
        log.debug("未绑定电池在外超时异常推送...");

        Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.UNBIND_BATTERY_OUT_TIME.getValue()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -(timeout == null ? 0 : timeout));

        List<Battery> batteryList = batteryMapper.findFreeOutTime(Battery.Status.NOT_USE.getValue(), calendar.getTime());

        for (Battery battery : batteryList) {
          //  insertPushMetaData(PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getValue(), battery.getId());

            insertPushMetaData(PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getValue(), String.format("%d:%d", battery.getAgentId(), battery.getCountByNotUse()));

//
//            AgentInfo agent = findAgentInfo(battery.getAgentId());
//            insertFaultLog(FaultLog.FaultLevel.MEDIUM.getValue(), battery.getId(), null, null, null, null, agent.getId(), agent.getAgentName(), battery.getId(), null, null, null, null, FaultLog.FaultType.CODE_19.getValue(), PushMessage.SourceType.UNBIND__BATTERY_OUT_OVERTIME.getName());

        }
        log.debug("未绑定电池在外超时异常推送结束...");
    }

    public static long getUseDay(Date date) {
        long useDay = 0;
        Date now = new Date();
        if (date.getTime() < now.getTime()) {
            useDay = (now.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
        }
        return useDay;
    }
}
