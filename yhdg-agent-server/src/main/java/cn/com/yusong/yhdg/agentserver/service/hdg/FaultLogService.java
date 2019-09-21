package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FaultLogService extends AbstractService {

    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;

    public Page findPage(FaultLog search) {
        Map<String, String> batteryBrandMap = findDictItemMap(DictCategory.CategoryType.BATTERY_BRAND.getValue());

        Page page = search.buildPage();
        page.setTotalItems(faultLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<FaultLog> faultLogs = faultLogMapper.findPageResult(search);
        for (FaultLog faultLog : faultLogs) {
            if (faultLog != null) {
                if (faultLog.getBrand() != null) {
                    faultLog.setBrandName(batteryBrandMap.get(faultLog.getBrand()));
                }
            }
        }
        page.setResult(faultLogs);
        return page;
    }

    public FaultLog find(Long id) {
        return faultLogMapper.find(id);
    }

    public int findCount(Integer status) {
        return faultLogMapper.findCount(status);
    }

    public ExtResult update(FaultLog entity) {
        cleanFaultLog(entity.getFaultType(), entity.getId());
        int total = faultLogMapper.handle(entity.getId(), FaultLog.HandleType.MANUAL.getValue(), entity.getHandleMemo(), entity.getHandleTime(), entity.getHandlerName(), FaultLog.Status.PROCESSED.getValue());
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }

    public ExtResult updateStatus(Long[] faultLogIds, String username) {
        Date now = new Date();
        for (Long id : faultLogIds) {
            FaultLog faultLog = faultLogMapper.find(id);
            cleanFaultLog(faultLog.getFaultType(), id);
            faultLogMapper.handle(id, FaultLog.HandleType.MANUAL.getValue(), "故障已处理", now, username, FaultLog.Status.PROCESSED.getValue());
        }
        return ExtResult.successResult();
    }

    private void cleanFaultLog(int faultType, long faultLogId) {
        if (faultType == FaultLog.FaultType.CODE_2.getValue()) {
            //修改subcabinet的offline_fault_log_id
            cabinetMapper.updateFaultLog("offlineFaultLogId", faultLogId);

        } else if (faultType == FaultLog.FaultType.CODE_3.getValue()) {
            //修改cabinet的all_full_fault_log_id和all_full_count
            cabinetMapper.updateFaultLog(faultLogId);

        } else if (faultType == FaultLog.FaultType.CODE_4.getValue()) {
            //修改hdg_battery_order的pay_timeout_fault_log_id
            batteryOrderMapper.updateFaultLog("payTimeoutFaultLogId", faultLogId);

        } else if (faultType == FaultLog.FaultType.CODE_5.getValue()) {
            //修改hdg_battery_order的not_take_timeout_fault_log_id
            batteryOrderMapper.updateFaultLog("notTakeTimeoutFaultLogId", faultLogId);

        } else if (faultType == FaultLog.FaultType.CODE_6.getValue()) {
            //修改hdg_battery的not_electrify_fault_log_id
            batteryMapper.updateFaultLog("notElectrifyFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_7.getValue()) {
            //修改hdg_battery的not_electrify_fault_log_id
            cabinetMapper.updateFaultLog("tempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_8.getValue()) {
            batteryMapper.updateFaultLog("monomerOvervoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_9.getValue()) {
            batteryMapper.updateFaultLog("monomerLowvoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_10.getValue()) {
            batteryMapper.updateFaultLog("wholeOvervoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_11.getValue()) {
            batteryMapper.updateFaultLog("wholeLowvoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_12.getValue()) {
            batteryMapper.updateFaultLog("chargeOvertempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_13.getValue()) {
            batteryMapper.updateFaultLog("chargeLowtempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_14.getValue()) {
            batteryMapper.updateFaultLog("dischargeOvertempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_15.getValue()) {
            batteryMapper.updateFaultLog("dischargeLowtempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_16.getValue()) {
            batteryMapper.updateFaultLog("chargeOvercurrentFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_17.getValue()) {
            batteryMapper.updateFaultLog("dischargeOvercurrentFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_18.getValue()) {
            batteryMapper.updateFaultLog("shortCircuitFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_19.getValue()) {
            batteryMapper.updateFaultLog("testingIcFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_20.getValue()) {
            batteryMapper.updateFaultLog("softwareLockingFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_21.getValue()) {
            cabinetBoxMapper.updateFaultLog("smokeFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_22.getValue()) {
            batteryMapper.updateFaultLog("hitFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_23.getValue()) {
            batteryMapper.updateFaultLog("dismantleFaultLogId", faultLogId);
        }
    }
}
