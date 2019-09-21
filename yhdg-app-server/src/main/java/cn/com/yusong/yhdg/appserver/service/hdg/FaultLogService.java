package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FaultLogService {
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public int findCountByDispatcher(long dispatcherId, int status) {
        return faultLogMapper.findCountByDispatcher(dispatcherId, status);
    }

    public Map<String ,Integer> findCabenitCount(long dispatcherId,String cabenitId,Integer status ){
        List<FaultLog> logList =
                faultLogMapper.findDispatcher(dispatcherId, cabenitId,status);
        Map<String,Integer> map = new HashMap<String,Integer>();
        if(!logList.isEmpty()){
            map.put("faultCount", logList.size());
            map.put("faultLevel", logList.get(0).getFaultLevel());
        }
        else {
            map.put("faultCount", 0);
            map.put("faultLevel", 0);
        }
        return  map;
    }

    public List<FaultLog> findList(long dispatcherId,Integer status,int offset,int limit){
        return faultLogMapper.findByStatus(dispatcherId,status,offset,limit);

    }

    public int handle(long id, int handleType, String handleMemo, String handlerName, Date handleTime) {
        int faultType = faultLogMapper.findFaultType(id);       //故障类型

        if (faultType == FaultLog.FaultType.CODE_2.getValue()) {
            //修改subcabinet的offline_fault_log_id
            cabinetMapper.updateFaultLog2("offlineFaultLogId",id);

        } else if (faultType == FaultLog.FaultType.CODE_3.getValue()) {
            //修改cabinet的all_full_fault_log_id和all_full_count
            cabinetMapper.updateFaultLog1(id);

        } else if (faultType == FaultLog.FaultType.CODE_4.getValue()) {
            //修改hdg_battery_order的pay_timeout_fault_log_id
            batteryOrderMapper.updateFaultLog("payTimeoutFaultLogId", id);

        } else if (faultType == FaultLog.FaultType.CODE_5.getValue()) {
            //修改hdg_battery_order的not_take_timeout_fault_log_id
            batteryOrderMapper.updateFaultLog("notTakeTimeoutFaultLogId", id);

        } else if (faultType == FaultLog.FaultType.CODE_6.getValue()) {
            //修改hdg_battery的not_electrify_fault_log_id
            batteryMapper.updateFaultLog(id);
        } else if (faultType == FaultLog.FaultType.CODE_7.getValue()) {
            //修改subcabinet的temp_fault_log_id
            cabinetMapper.updateFaultLog2("tempFaultLogId", id);
        }

        return faultLogMapper.handle(id, handleType, handleMemo, handlerName, handleTime, FaultLog.Status.PROCESSED.getValue(), FaultLog.Status.WAIT_PROCESS.getValue());
    }


}
