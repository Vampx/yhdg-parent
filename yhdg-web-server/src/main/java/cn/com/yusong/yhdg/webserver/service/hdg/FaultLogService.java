package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
        if (faultType == FaultLog.FaultType.CODE_1.getValue()) {
            batteryMapper.updateFaultLog("monomerOvervoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_2.getValue()) {
            batteryMapper.updateFaultLog("monomerLowvoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_3.getValue()) {
            batteryMapper.updateFaultLog("wholeOvervoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_4.getValue()) {
            batteryMapper.updateFaultLog("wholeLowvoltageFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_5.getValue()) {
            batteryMapper.updateFaultLog("chargeOvertempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_6.getValue()) {
            batteryMapper.updateFaultLog("chargeLowtempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_7.getValue()) {
            batteryMapper.updateFaultLog("dischargeOvertempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_8.getValue()) {
            batteryMapper.updateFaultLog("dischargeLowtempFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_9.getValue()) {
            batteryMapper.updateFaultLog("chargeOvercurrentFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_10.getValue()) {
            batteryMapper.updateFaultLog("dischargeOvercurrentFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_11.getValue()) {
            batteryMapper.updateFaultLog("shortCircuitFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_12.getValue()) {
            batteryMapper.updateFaultLog("testingIcFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_13.getValue()) {
            batteryMapper.updateFaultLog("softwareLockingFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_14.getValue()) {
            batteryMapper.updateFaultLog("dischargeLockingFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_15.getValue()) {
            batteryMapper.updateFaultLog("chargeMosFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_16.getValue()) {
            batteryMapper.updateFaultLog("dischargeMosFaultLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_17.getValue()) {
            batteryMapper.updateFaultLog("maxVolDiffLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_20.getValue()) {
            batteryMapper.updateFaultLog("signVolLowLogId", faultLogId);
        } else if (faultType == FaultLog.FaultType.CODE_21.getValue()) {
            cabinetBoxMapper.updateFaultLog("smokeFaultLogId", faultLogId);
        }
    }
}
