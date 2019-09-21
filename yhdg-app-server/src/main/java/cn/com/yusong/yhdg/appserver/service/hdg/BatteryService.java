package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryReportLogMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.BatterySequenceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatterySequence;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatteryService extends AbstractService {
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatterySequenceMapper batterySequenceMapper;
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;

    public Battery find(String id) {
        Battery battery =  batteryMapper.find(id);
        if(battery != null && battery.getBatteryType() != null){
            battery.setBatteryType(findBatteryType(battery.getType()).getTypeName());
        }
        return battery;
    }

    public Battery findByCode(String code) {
        Battery battery =  batteryMapper.findByCode(code);
        if(battery != null && battery.getBatteryType() != null){
            battery.setBatteryType(findBatteryType(battery.getType()).getTypeName());
        }
        return battery;
    }

    public Battery findByShellCode(String shellCode) {
        Battery battery =  batteryMapper.findByShellCode(shellCode);
        return battery;
    }

    public List<Battery> findCanRentByAgentId(Integer category, Integer status, Integer agentId) {
        return batteryMapper.findCanRentByAgentId(category, status, agentId);
    }

    public Battery findConditional(String name, String value) {
        return batteryMapper.findConditional(name, value);
    }

    public List<Map> findBoxBattery(String cabinetId) {
        return batteryMapper.findBoxBattery(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId, ConstEnum.Flag.TRUE.getValue());
    }

    public RestResult record(Battery battery) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int recordCount = 0;
        int gpsFlag = 0;

        if (battery.getReportTime() != null) {
            String suffix = DateFormatUtils.format(battery.getReportTime(), "yyyyww");
            recordCount = batteryReportLogMapper.findCount(battery.getId(), suffix);
            gpsFlag = batteryReportLogMapper.findGps(battery.getId(), suffix) > 0 ? 1 : 0;
        }
        map.put("recordCount", recordCount);
        map.put("gpsFlag", gpsFlag);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    public RestResult control(Battery battery, int controlType) {
        int lockSwitch = ConstEnum.Flag.FALSE.getValue();
        if(controlType == 1){
            lockSwitch = ConstEnum.Flag.TRUE.getValue();
        }
        batteryMapper.updateLockSwitch(battery.getId(), lockSwitch);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    public RestResult rescue(Battery battery) {
        Map result = new HashMap();
        if(battery.getRescueStatus() != null && battery.getRescueStatus() != Battery.RescueStatus.NOT.getValue()){
            result.put("status",battery.getRescueStatus());
        }else{
            batteryMapper.updateRescueStatus(battery.getId(), Battery.RescueStatus.WAIT.getValue());
            result.put("status",Battery.RescueStatus.WAIT.getValue());
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    private String nextBatteryId() {
        BatterySequence entity = new BatterySequence();
        batterySequenceMapper.insert(entity);
        String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
        return result;
    }

    public ExtResult create(String id, String code, String qrcode, String shellCode, int agentId, int type) {

        Battery battery = batteryMapper.findConditional("code", code);
        if (battery != null) {
            return ExtResult.failResult("电池已存在");
        }
        if (StringUtils.isEmpty(id)) {
            id = nextBatteryId();
        }
        if (StringUtils.isNotEmpty(qrcode)) {
            Battery result = batteryMapper.findConditional("qrcode", qrcode);
            if (result != null) {
                return ExtResult.failResult("二维码已存在");
            }
        } else {
            qrcode = "";
        }
        if (StringUtils.isNotEmpty(shellCode)) {
            Battery result = batteryMapper.findConditional("shell_code", shellCode);
            if (result != null) {
                return ExtResult.failResult("外壳编号已存在");
            }
        } else {
            shellCode = "";
        }
        battery = new Battery();
        battery.setId(id);
        battery.setCode(code);
        battery.setShellCode(shellCode);
        battery.setType(type);
        battery.setCategory(Battery.Category.EXCHANGE.getValue());
        battery.setAgentId(agentId);
        battery.setQrcode(qrcode);
        battery.setOrderDistance(0);
        battery.setTotalDistance(0L);
        battery.setIsActive(ConstEnum.Flag.TRUE.getValue());
        battery.setExchangeAmount(0);
        battery.setVolume(0);
        battery.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
        battery.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
        battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
        battery.setUseCount(0);
        battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        battery.setStatus(Battery.Status.NOT_USE.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        battery.setCreateTime(new Date());
        battery.setStayHeartbeat(Constant.STAY_HEARTBEAT);
        battery.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
        battery.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
        battery.setChargeCompleteVolume(95);

        battery.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
        battery.setShutdownVoltage(42000);
        battery.setAcceleretedSpeed(1);
        battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());

        if (batteryMapper.insert(battery) >= 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("创建失败");
        }
    }
}
