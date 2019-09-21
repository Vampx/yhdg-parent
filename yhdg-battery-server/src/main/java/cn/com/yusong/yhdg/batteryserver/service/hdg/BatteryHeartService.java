package cn.com.yusong.yhdg.batteryserver.service.hdg;

import cn.com.yusong.yhdg.batteryserver.config.AppConfig;
import cn.com.yusong.yhdg.batteryserver.entity.HeartParam;
import cn.com.yusong.yhdg.batteryserver.entity.HeartResult;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.MobileMessageTemplateMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.hdg.*;
import cn.com.yusong.yhdg.batteryserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.batteryserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class BatteryHeartService extends AbstractService {
    static final Logger log = LogManager.getLogger(BatteryHeartService.class);
    static final Pattern LNG_PATTERN = Pattern.compile("(\\d{3})(\\d{2}.\\d{4})");
    static final Pattern LAT_PATTERN = Pattern.compile("(\\d{2})(\\d{2}.\\d{4})");

    @Autowired
    AppConfig appConfig;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    KeepOrderMapper keepOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    BatteryReportMapper batteryReportMapper;
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;
    @Autowired
    BatteryReportDateMapper batteryReportDateMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    UnregisterBatteryMapper unregisterBatteryMapper;
    @Autowired
    UnregisterBatteryReportMapper unregisterBatteryReportMapper;
    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;
    @Autowired
    BatterySequenceMapper batterySequenceMapper;
    @Autowired
    BatteryParameterMapper batteryParameterMapper;
    @Autowired
    BatteryParameterLogMapper batteryParameterLogMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;

    @Transactional(rollbackFor = Throwable.class)
    public HeartResult heart(HeartParam param){
        Date now = new Date();
        HeartResult result = new HeartResult();
        result.code = param.IMEI;
        result.version = param.BmsVer;

        Battery battery = batteryMapper.findByCode(param.IMEI);
        //添加电池
        if (battery == null) {
            battery = new Battery();
            String id = nextBatteryId();
            battery.setId(id);
            battery.setCode(param.IMEI);
            battery.setType(2);//默认60v
            battery.setCategory(Battery.Category.EXCHANGE.getValue());
            battery.setAgentId(1);
            battery.setQrcode("");
            battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
            battery.setOrderDistance(0);
            battery.setTotalDistance(0L);
            battery.setIsActive(ConstEnum.Flag.TRUE.getValue());
            battery.setExchangeAmount(0);
            battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
            battery.setUseCount(0);
            battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
            battery.setIsNormal(ConstEnum.Flag.TRUE.getValue());
            battery.setStatus(Battery.Status.NOT_USE.getValue());
            battery.setCreateTime(new Date());
            battery.setStayHeartbeat(Constant.STAY_HEARTBEAT);
            battery.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
            battery.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
            battery.setStandbyHeartbeat(Constant.STANDBY_HEARTBEAT);
            battery.setDormancyHeartbeat(Constant.DORMANCY_HEARTBEAT);
            battery.setChargeCompleteVolume(95);
            battery.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
            battery.setLockSwitch(ConstEnum.Flag.FALSE.getValue());
            battery.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
            battery.setShutdownVoltage(42000);
            battery.setAcceleretedSpeed(1);
            battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
            battery.setUpLineStatus(ConstEnum.Flag.FALSE.getValue());
            battery.setLockSwitch(Battery.LockSwitch.DISCHG_CLOSE.getValue());
            batteryMapper.insert(battery);
        }

        //添加电池上报信息
        BatteryReport batteryReport = new BatteryReport();
        param.setProperties(batteryReport);//设置参数
        batteryReport.setBatteryId(battery.getId());
        batteryReport.setCabinetId(battery.getCabinetId());
        batteryReport.setBoxNum(battery.getBoxNum());
        batteryReport.setCreateTime(now);
        String suffix = DateFormatUtils.format(batteryReport.getCreateTime(), Constant.DATE_FORMAT_NO_LINE);
        batteryReport.setSuffix(suffix);

        Integer distance = 0;
        //经纬度不为空则计算上报协议距离间隔
        if ( battery.getLat() != null && battery.getLng() != null && batteryReport.getLat() != null && batteryReport.getLng() != null) {
            distance = (int) GeoHashUtils.gps2m(batteryReport.getLat(), batteryReport.getLng(), battery.getLat(), battery.getLng());
        }

        //电池上报保存
        batteryReport.setDistance(distance);
        insertBatteryReport(batteryReport, now);

        battery.setCurrentSignal(batteryReport.getCurrentSignal());
        battery.setSignalType(batteryReport.getSignalType());
        battery.setVoltage(batteryReport.getVoltage());
        battery.setElectricity(batteryReport.getElectricity());
        if (batteryReport.getLocType() == BatteryParameter.LocType.GPS.getValue()) {
            battery.setLat(batteryReport.getLat());
            battery.setLng(batteryReport.getLng());
            battery.setGpsUpdateTime(new Date());
        }
        battery.setTemp(batteryReport.getTemp());
        battery.setTotalCapacity(batteryReport.getNominalCapacity());
        battery.setCurrentCapacity(batteryReport.getCurrentCapacity());
        battery.setCircle(batteryReport.getCircle());
        battery.setSingleVoltage(batteryReport.getSingleVoltage());
        if(batteryReport.getVolume() != null){
            battery.setVolume(batteryReport.getVolume());
        }

        //低电量推送
        dealLowVolume(battery, now);

        //故障处理
        if(batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryParameter.HeartType.NORMAL_HEART.getValue()){
            dealFaultLog(batteryReport.getFault(), battery, batteryReport, now);
        }

        //压差处理
        dealVoltageDiff(batteryReport, battery, now);

        //更新电池信息
        if(batteryReport.getBattType() != null){
            battery.setType(batteryReport.getBattType());
        }
        battery.setSimMemo(batteryReport.getSimCode());
        if(batteryReport.getVersion() != null){
            battery.setVersion(batteryReport.getVersion());
        }

        if(batteryReport.getElectricity() != null){
            if(batteryReport.getElectricity() > 0){
                battery.setFetStatus(Battery.FetStatus.CHARGE.getValue());
            }else if(batteryReport.getElectricity() < 0){
                battery.setFetStatus(Battery.FetStatus.DISCHARGE.getValue());
            }else{
                battery.setFetStatus(Battery.FetStatus.STATIC.getValue());
            }
        }
        battery.setTotalDistance((battery.getTotalDistance() == null ? distance : battery.getTotalDistance()) + distance);
        battery.setOrderDistance((battery.getOrderDistance() == null ? distance : battery.getOrderDistance()) + distance);

        battery.setMoveHeartbeat(batteryReport.getHeartInterval());
        battery.setStayHeartbeat(batteryReport.getMotionless());
        battery.setStandbyHeartbeat(batteryReport.getStandby());
        battery.setDormancyHeartbeat(batteryReport.getDormancy());

        battery.setReportTime(new Date());
        battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        batteryMapper.update(battery);

        //换电 电池轨迹
        if ((battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()
                || battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) && battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
            if (StringUtils.isNotEmpty(battery.getOrderId())) {
                batteryOrderMapper.update(battery.getOrderId(), battery.getVolume(), battery.getOrderDistance(),battery.getCurrentCapacity());
                //换电订单电池上报日志
                BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());
                insertBatteryOrderBatteryReportLog(battery, batteryReport, getTableSuffix(batteryOrder.getCreateTime()));
            }
        }

        //租电 电池操作日志 电池轨迹
        if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue() && battery.getCategory() == Battery.Category.RENT.getValue()) {
            //客户电池操作日志
            if (battery.getCustomerId() != null) {
                boolean saveFlag = true;
                //查询上条记录 和电池状态不一致，进行保存
                BatteryOperateLog batteryOperateLog = batteryOperateLogMapper.findLastByCustomer(battery.getCustomerId());
                if (battery.getFetStatus() == Battery.FetStatus.CHARGE.getValue()) {
                    if (batteryOperateLog != null && batteryOperateLog.getOperateType() == BatteryOperateLog.OperateType.CUSTOMER_START_CHARGE.getValue()) {
                        saveFlag = false;
                    }
                } else if (battery.getFetStatus() == Battery.FetStatus.DISCHARGE.getValue()) {
                    if (batteryOperateLog != null && batteryOperateLog.getOperateType() == BatteryOperateLog.OperateType.CUSTOMER_START_USE.getValue()) {
                        saveFlag = false;
                    }
                } else if (battery.getFetStatus() == Battery.FetStatus.STATIC.getValue()) {
                    saveFlag = false;
                }

                if (saveFlag) {
                    batteryOperateLog = new BatteryOperateLog();
                    batteryOperateLog.setBatteryId(battery.getId());
                    if (battery.getFetStatus() == Battery.FetStatus.CHARGE.getValue()) {
                        batteryOperateLog.setOperateType(BatteryOperateLog.OperateType.CUSTOMER_START_CHARGE.getValue());
                    } else {
                        batteryOperateLog.setOperateType(BatteryOperateLog.OperateType.CUSTOMER_START_USE.getValue());
                    }
                    batteryOperateLog.setCustomerId(battery.getCustomerId());
                    batteryOperateLog.setCustomerMobile(battery.getCustomerMobile());
                    batteryOperateLog.setCustomerFullname(battery.getCustomerFullname());
//                    batteryOperateLog.setShopId(battery.getShopId());
//                    batteryOperateLog.setShopName(battery.getShopName());
                    batteryOperateLog.setVolume(battery.getVolume());
                    batteryOperateLog.setCreateTime(new Date());
                    batteryOperateLogMapper.insert(batteryOperateLog);
                }
            }

            //轨迹
            //insertBatteryOrderBatteryReportLog(battery, batteryReport, getTableSuffix(batteryOrder.getCreateTime()));

        }

        //电池参数
        BatteryParameter batteryParameter = dealBatteryParameter(batteryReport);

        //返回
        result.id = battery.getId();
        List<BatteryParameterLog> parameterLogList = batteryParameterLogMapper.findByBatteryId(battery.getId(), BatteryParameterLog.Status.NO_REPORT.getValue());
        if(parameterLogList.size() > 0){
            for(BatteryParameterLog batteryParameterLog : parameterLogList){
                String newValue = batteryParameterLog.getNewValue();
                if(isInt(newValue)){
                    result.json.put(BatteryParameter.Parameter.getParameter(batteryParameterLog.getParamId()), Integer.parseInt(newValue));
                }else if(isDouble(newValue)){
                    result.json.put(BatteryParameter.Parameter.getParameter(batteryParameterLog.getParamId()), Double.parseDouble(newValue));
                }else{
                    result.json.put(BatteryParameter.Parameter.getParameter(batteryParameterLog.getParamId()), newValue);
                }
                batteryParameterLogMapper.report(batteryParameterLog.getId(), BatteryParameterLog.Status.REPORT.getValue(), new Date());
            }
        }
        if(batteryParameter != null && batteryParameter.getUpBms() != null &&  batteryParameter.getUpBms() == ConstEnum.Flag.TRUE.getValue() ){
            result.json.put("Heart", 30);
        }


        //电池锁定 放电关
        if(battery.getLockSwitch() != null && battery.getLockSwitch() == Battery.LockSwitch.DISCHG_CLOSE.getValue()){
            result.json.put("MOS", 2);
        }
        //电池锁定 放电开
        else if(battery.getLockSwitch() != null && battery.getLockSwitch() == Battery.LockSwitch.DISCHG_OPEN.getValue()){
            result.json.put("MOS", 0);
        }
        //电池锁定 如果不控制电池锁定，租电电池是否打开根据租期来判断，换电电池mos传0
        else if(battery.getLockSwitch() == null ||  battery.getLockSwitch() == Battery.LockSwitch.NO.getValue()){
            //电池租期
            if(battery.getCategory() == Battery.Category.RENT.getValue()){
                if(battery.getCustomerId() != null){
                    Long expireTime = getRentExpireTime(battery, battery.getCustomerId());
                    Long batteryLease = expireTime - new Date().getTime()/1000;
                    if(batteryLease < 0 ){
                        batteryLease = 0l;
                    }
                    result.json.put("BatteryLease", batteryLease.intValue());
                }else{
                    result.json.put("BatteryLease", 0);
                }
            }else{
                result.json.put("MOS", 0);
            }
        }

        //一键自救 (暂时发送自救后，如果保护状态没修复，一直发送)
        if(battery.getMonomerLowvoltageFaultLogId() != null || battery.getWholeLowvoltageFaultLogId() != null){
            if(battery.getRescueStatus() != null && (battery.getRescueStatus() == Battery.RescueStatus.WAIT.getValue())){
                result.json.put("Rescue", 1);
                //更新状态为已发送
                batteryMapper.updateRescueStatus(battery.getId(), Battery.RescueStatus.WAIT.getValue(), Battery.RescueStatus.END.getValue());
            }else if(battery.getRescueStatus() != null &&  battery.getRescueStatus() == Battery.RescueStatus.END.getValue()){
                result.json.put("Rescue", 1);
            }
        }
        //无保护后，如果有已经自救的，更新为已修复
        if(battery.getMonomerLowvoltageFaultLogId() == null && battery.getWholeLowvoltageFaultLogId() == null){
            if(battery.getRescueStatus() != null && (battery.getRescueStatus() == Battery.RescueStatus.END.getValue())){
                //更新状态为已修复
                batteryMapper.updateRescueStatus(battery.getId(), Battery.RescueStatus.END.getValue(), Battery.RescueStatus.RECOVERED.getValue());
            }
        }


        return result;
    }

    /**
     * 电池日志插入
     * @param batteryReport
     */
    private void insertBatteryReport(BatteryReport batteryReport, Date date) {
        String tableName = String.format("%s%s", BatteryReport.BATTERY_REPORT_TABLE_NAME, batteryReport.getSuffix());
        boolean result = findTable(tableName);
        if (!result) {
            batteryReportMapper.createTable(tableName);
        }
        batteryReportMapper.insert(batteryReport);
        String reportDate = DateFormatUtils.format(date, Constant.DATE_FORMAT);
        if (batteryReportDateMapper.update(reportDate, batteryReport.getBatteryId()) == 0) {
            batteryReportDateMapper.create(reportDate, batteryReport.getBatteryId());
        }
    }

    /**
     * 告警处理（过压欠压不再做告警处理）
     *
     * @param protectState
     */
    public void dealFaultLog(Integer protectState, Battery battery, BatteryReport batteryReport, Date now) {
        BatteryReportLog.ProtectState[] protectStates = BatteryReportLog.ProtectState.values();

        char[] str = new StringBuilder(String.format("%0" + protectStates.length + "d", Long.parseLong(Integer.toBinaryString(protectState == null ? 0 : protectState)))).reverse().toString().toCharArray();
        for (int i = 0; i < str.length; i++) {
            if ('1' == str[i]) {
                FaultLog faultLog = null;
                switch (protectStates[i].getValue()) {
                    case 1:
                        if (battery.getMonomerOvervoltageFaultLogId() == null) {
                            //单体过压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_1.getValue(), FaultLog.FaultType.CODE_1.getName(), now);
                            battery.setMonomerOvervoltageFaultLogId(faultLog.getId());
                        }else{
                            if(battery.getElectricity() > 0){
                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_1.getValue(), battery.getId());
                                faultLogMapper.updateFaultLevel(battery.getMonomerOvervoltageFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
                            }
                        }
                        break;
                    case 2:
                        if (battery.getMonomerLowvoltageFaultLogId() == null) {

                            //单体欠压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_2.getValue(), FaultLog.FaultType.CODE_2.getName(), now);
                            battery.setMonomerLowvoltageFaultLogId(faultLog.getId());
                        }else{
                            if(battery.getElectricity() < 0){
                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_2.getValue(), battery.getId());
                                faultLogMapper.updateFaultLevel(battery.getMonomerLowvoltageFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
                            }
                        }
                        break;
                    case 3:
                        if (battery.getWholeOvervoltageFaultLogId() == null) {

                            //整组过压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_3.getValue(), FaultLog.FaultType.CODE_3.getName(), now);
                            battery.setWholeOvervoltageFaultLogId(faultLog.getId());
                        }else{
                            if(battery.getElectricity() > 0){
                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_3.getValue(), battery.getId());
                                faultLogMapper.updateFaultLevel(battery.getWholeOvervoltageFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
                            }
                        }
                        break;
                    case 4:
                        if (battery.getWholeLowvoltageFaultLogId() == null) {

                            //整组欠压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_4.getValue(), FaultLog.FaultType.CODE_4.getName(), now);
                            battery.setWholeLowvoltageFaultLogId(faultLog.getId());
                        }else{
                            if(battery.getElectricity() < 0){
                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_4.getValue(), battery.getId());
                                faultLogMapper.updateFaultLevel(battery.getWholeLowvoltageFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
                            }
                        }
                        break;
                    case 5:
                        if (battery.getChargeOvertempFaultLogId() == null) {

                            //充电过温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_5.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

//                            Integer sysTemp = null;
//                            if(batteryReport != null && batteryReport.getChgOtTrip() != null )
//                                sysTemp = ( batteryReport.getChgOtTrip() - 2731) / 10;
//                            String content = String.format("电池【电量%d%%】出现充电高温异常，当前温度值：【%s/%s℃】，请监控该电池",battery.getVolume(),  battery.getTemp(), sysTemp);

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_5.getValue(), FaultLog.FaultType.CODE_5.getName(), now);
                            battery.setChargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 6:
                        if (battery.getChargeLowtempFaultLogId() == null) {

                            //充电低温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_6.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_6.getValue(), FaultLog.FaultType.CODE_6.getName(), now);
                            battery.setChargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 7:
                        if (battery.getDischargeOvertempFaultLogId() == null) {

                            //放电过温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_7.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_7.getValue(), FaultLog.FaultType.CODE_7.getName(), now);
                            battery.setDischargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 8:
                        if (battery.getDischargeLowtempFaultLogId() == null) {

                            //放电低温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_8.getValue(), FaultLog.FaultType.CODE_8.getName(), now);
                            battery.setDischargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 9:
                        if (battery.getChargeOvercurrentFaultLogId() == null) {
                            //充电过流发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_9.getValue(), FaultLog.FaultType.CODE_9.getName(), now);
                            battery.setChargeOvercurrentFaultLogId(faultLog.getId());
                        }else{
//                            if(battery.getElectricity() > 0){
//                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue(), battery.getId());
//                                faultLogMapper.updateFaultLevel(battery.getChargeOvercurrentFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
//                            }
                        }
                        break;
                    case 10:
                        if (battery.getDischargeOvercurrentFaultLogId() == null) {
                            //放电过流发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_10.getValue(), FaultLog.FaultType.CODE_10.getName(), now);
                            battery.setDischargeOvercurrentFaultLogId(faultLog.getId());
                        }else{
//                            if(battery.getElectricity() < 0){
//                                insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue(), battery.getId());
//                                faultLogMapper.updateFaultLevel(battery.getDischargeOvercurrentFaultLogId() , FaultLog.FaultLevel.IMPORTANCE.getValue());
//                            }
                        }
                        break;
                    case 11:
                        if (battery.getShortCircuitFaultLogId() == null) {
                            //短路发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog(FaultLog.FaultLevel.MEDIUM.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_11.getValue(), FaultLog.FaultType.CODE_11.getName(), now);
                            battery.setShortCircuitFaultLogId(faultLog.getId());
                        }
                        break;
                    case 12:
                        if (battery.getTestingIcFaultLogId() == null) {
                            //前端检测IC错误 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog(FaultLog.FaultLevel.MEDIUM.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_12.getValue(), FaultLog.FaultType.CODE_12.getName(), now);
                            battery.setTestingIcFaultLogId(faultLog.getId());
                        }
                        break;
                    case 13:
                        if (battery.getSoftwareLockingFaultLogId() == null) {
                            //保护板充电MOS锁定 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_13.getValue(), battery.getId());
                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_13.getValue(), FaultLog.FaultType.CODE_13.getName(), now);
                            battery.setSoftwareLockingFaultLogId(faultLog.getId());
                        }
                        break;
                    case 14:
                        if (battery.getDischargeLockingFaultLogId() == null) {
                            //保护板放电MOS锁定 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_14.getValue(), battery.getId());
                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_14.getValue(), FaultLog.FaultType.CODE_14.getName(), now);
                            battery.setDischargeLockingFaultLogId(faultLog.getId());
                        }
                        break;
                    case 15:
//                        if (battery.getChargeMosFaultLogId() == null) {
//                            //充电MOS异常 插入运营商告警
//                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_15.getValue(), battery.getId());
//                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_15.getValue(), FaultLog.FaultType.CODE_15.getName(), now);
//                            battery.setChargeMosFaultLogId(faultLog.getId());
//                        }
                        break;
                    case 16:
//                        if (battery.getDischargeMosFaultLogId() == null) {
//                            //放电MOS异常 插入运营商告警
//                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_16.getValue(), battery.getId());
//                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_16.getValue(), FaultLog.FaultType.CODE_16.getName(), now);
//                            battery.setDischargeMosFaultLogId(faultLog.getId());
//                        }
                        break;


                    default:
                        break;
                }
            } else {
                switch (protectStates[i].getValue()) {
                    case 1:
                        if (battery.getMonomerOvervoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getMonomerOvervoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setMonomerOvervoltageFaultLogId(null);
                        break;
                    case 2:
                        if (battery.getMonomerLowvoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getMonomerLowvoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setMonomerLowvoltageFaultLogId(null);
                        break;
                    case 3:
                        if (battery.getWholeOvervoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getWholeOvervoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setWholeOvervoltageFaultLogId(null);
                        break;
                    case 4:
                        if (battery.getWholeLowvoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getWholeLowvoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setWholeLowvoltageFaultLogId(null);
                        break;
                    case 5:
//                        if (battery.getChargeOvertempFaultLogId() != null)
//                            faultLogMapper.handle(battery.getChargeOvertempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//                        battery.setChargeOvertempFaultLogId(null);
                        break;
                    case 6:
                        if (battery.getChargeLowtempFaultLogId() != null)
                            faultLogMapper.handle(battery.getChargeLowtempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setChargeLowtempFaultLogId(null);
                        break;
                    case 7:
//                        if (battery.getDischargeOvertempFaultLogId() != null)
//                            faultLogMapper.handle(battery.getDischargeOvertempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setDischargeOvertempFaultLogId(null);
                        break;
                    case 8:
                        if (battery.getDischargeLowtempFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeLowtempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeLowtempFaultLogId(null);
                        break;
                    case 9:
//                        if (battery.getChargeOvercurrentFaultLogId() != null)
//                            faultLogMapper.handle(battery.getChargeOvercurrentFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setChargeOvercurrentFaultLogId(null);
                        break;
                    case 10:
//                        if (battery.getDischargeOvercurrentFaultLogId() != null)
//                            faultLogMapper.handle(battery.getDischargeOvercurrentFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setDischargeOvercurrentFaultLogId(null);
                        break;
                    case 11:
                        if (battery.getShortCircuitFaultLogId() != null)
                            faultLogMapper.handle(battery.getShortCircuitFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setShortCircuitFaultLogId(null);
                        break;
                    case 12:
                        if (battery.getTestingIcFaultLogId() != null)
                            faultLogMapper.handle(battery.getTestingIcFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setTestingIcFaultLogId(null);
                        break;
                    case 13:
                        if (battery.getSoftwareLockingFaultLogId() != null)
                            faultLogMapper.handle(battery.getSoftwareLockingFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setSoftwareLockingFaultLogId(null);
                        break;
                    case 14:
                        if (battery.getDischargeLockingFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeLockingFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeLockingFaultLogId(null);
                        break;
                    case 15:
                        if (battery.getChargeMosFaultLogId() != null)
                            faultLogMapper.handle(battery.getChargeMosFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setChargeMosFaultLogId(null);
                        break;
                    case 16:
                        if (battery.getDischargeMosFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeMosFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeMosFaultLogId(null);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 保存告警信息
     *
     * @param faultLevel
     * @param agentId
     * @param agentName
     * @param batteryId
     * @param faultType
     * @param faultContent
     * @return
     */
    public FaultLog insertFaultLog(Integer faultLevel, Integer agentId, String agentName, String batteryId, String cabinetId, String cabinetName, String boxNum, Integer faultType, String faultContent, Date now) {
        FaultLog data = new FaultLog();
        data.setFaultLevel(faultLevel);
        data.setAgentId(agentId);
        data.setAgentName(agentName);
        data.setBatteryId(batteryId);
        data.setCabinetId(cabinetId);
        data.setCabinetName(boxNum);
        data.setBoxNum(batteryId);
        data.setFaultType(faultType);
        data.setFaultContent(faultContent);
        data.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        data.setCreateTime(now);
        faultLogMapper.create(data);
        return data;
    }

    public void insertPushMetaData(Integer sourceType, String sourceId) {
        PushMetaData data = new PushMetaData();
        data.setSourceType(sourceType);
        data.setSourceId(sourceId);
        data.setCreateTime(new Date());
        pushMetaDataMapper.insert(data);
    }

    /**
     * 低电量推送
     * @param battery
     */
    public void dealLowVolume(Battery battery, Date now) {
        int lowVolume = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.LOW_VOLUME.getValue()).getConfigValue());
        int lowVolumeInterval = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.LOW_VOLUME_INTERVAL.getValue()).getConfigValue());
        if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue() && battery.getChargeStatus() != Battery.ChargeStatus.CHARGING.getValue()) {
            if (battery.getVolume() != null && battery.getVolume() <= lowVolume ) {
                //第一次推送给运营商
//                if(battery.getLowVolumeNoticeVolume() == null){
//                    //推送运营商
//                    PushMetaData pushMetaData = new PushMetaData();
//                    pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT.getValue());
//                    pushMetaData.setSourceId(battery.getId());
//                    pushMetaData.setCreateTime(now);
//                    if (log.isDebugEnabled()) {
//                        log.debug("battery: {} IMEI:{}  上报电量: {}，低电量设置值：{}，低电量间隔值：{}第一次需要发送推送运营商!", battery.getId(), battery.getCode(), battery.getVolume(), lowVolume, lowVolumeInterval);
//                    }
//                    pushMetaDataMapper.insert(pushMetaData);
//                }
//                MobileMessageTemplate template = mobileMessageTemplateMapper.find(0, MobileMessageTemplate.Type.LOW_ELECTRICITY.getValue());
//                String[] variables = template.getVariable().split(",");
//                String[] values = {battery.getCustomerFullname(), battery.getId()};
//                String[] variable = new String[variables.length * 2];
//                int j = 0;
//                for (int i = 0; i < variable.length; i++) {
//                    if (i % 2 == 0) {
//                        variable[i] = variables[j];
//                    } else {
//                        variable[i] = values[j];
//                        j++;
//                    }
//                }
//                String content = template.replace(variable);
//                try {
//                    ClientBizUtils.Resp<Msg212000001> resp = ClientBizUtils.sendMobileMessage(appConfig,
//                            battery.getCustomerMobile(),
//                            content,
//                            MobileMessageTemplate.DEFAULT_APP_ID,
//                            template.getId().byteValue(),
//                            MobileMessage.buildVariableJson(variable),
//                            template.getCode());
//                    battery.setLowVolumeNoticeVolume(battery.getVolume());
//                    battery.setLowVolumeNoticeTime(now); //设置通知发送时间
//                } catch (Exception e) {
//                    log.error("send sms error", e);
//                }

                List<Integer> lowVolumeList = new ArrayList<Integer>();
                while (true) {
                    lowVolumeList.add(lowVolume);
                    lowVolume = lowVolume - lowVolumeInterval;
                    if (lowVolume < 0) {
                        break;
                    }
                }

                for (int i = 0; i < lowVolumeList.size(); i++) {
                    if (i == lowVolumeList.size() - 1) {
                        if ((battery.getVolume() <= lowVolumeList.get(i))
                                && !(battery.getLowVolumeNoticeVolume() != null && battery.getLowVolumeNoticeVolume() <= lowVolumeList.get(i))) {
                            //当前电量在低电量与低电量间隔内，上次推送电量不在低电量与低电量间隔内,发送推送
                            //例如 设置30%， 每5%推送 ，那么第一次上报29%要推送，接着28%就不需要推送，要等到小于等于25%才能推送
                            battery.setLowVolumeNoticeVolume(battery.getVolume());
                            battery.setLowVolumeNoticeTime(now); //设置通知发送时间
                            PushMetaData pushMetaData = new PushMetaData();
                            pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue());
                            pushMetaData.setSourceId(battery.getId());
                            pushMetaData.setCreateTime(now);
                            if (log.isDebugEnabled()) {
                                log.debug("battery: {} IMEI:{}  上报电量: {}，低电量设置值：{}，低电量间隔值：{}需要发送推送!", battery.getId(), battery.getCode(), battery.getVolume(), lowVolume, lowVolumeInterval);
                            }
                            pushMetaDataMapper.insert(pushMetaData);

                        }
                    } else {
                        if (battery.getVolume() != null && (battery.getVolume() <= lowVolumeList.get(i) && battery.getVolume() > lowVolumeList.get(i + 1))
                                && !(battery.getLowVolumeNoticeVolume() != null && battery.getLowVolumeNoticeVolume() <= lowVolumeList.get(i) && battery.getLowVolumeNoticeVolume() > lowVolumeList.get(i + 1))) {
                            //当前电量在低电量与低电量间隔内，上次推送电量不在低电量与低电量间隔内,发送推送
                            //例如 设置30%， 每5%推送 ，那么第一次上报29%要推送，接着28%就不需要推送，要等到小于等于25%才能推送
                            battery.setLowVolumeNoticeVolume(battery.getVolume());
                            battery.setLowVolumeNoticeTime(now); //设置通知发送时间
                            PushMetaData pushMetaData = new PushMetaData();
                            pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_BATTERY_VOLUME_LOW.getValue());
                            pushMetaData.setSourceId(battery.getId());
                            pushMetaData.setCreateTime(now);
                            if (log.isDebugEnabled()) {
                                log.debug("battery: {} IMEI:{}  上报电量: {}，低电量设置值：{}，低电量间隔值：{}需要发送推送!", battery.getId(), battery.getCode(), battery.getVolume(), lowVolume, lowVolumeInterval);
                            }
                            pushMetaDataMapper.insert(pushMetaData);
                        }
                    }
                }
            }
        }
    }

    /**
     * 压差处理
     * @param battery
     */
    public void dealVoltageDiff(BatteryReport batteryReport, Battery battery, Date now) {
       if(StringUtils.isEmpty(batteryReport.getSingleVoltage())){
           return;
       }
        String [] singleVoltageStrs =  StringUtils.split(batteryReport.getSingleVoltage(), ",");
        Integer [] singleVoltages = new Integer[singleVoltageStrs.length];
        for(int i=0 ; i < singleVoltageStrs.length; i++){
            singleVoltages[i] = Integer.parseInt(singleVoltageStrs[i]);
        }
        List<Integer> list = Arrays.asList(singleVoltages);
        if(!list.isEmpty()){
            Collections.sort(list);
            int minVoltage = list.get(0);
            int maxVoltage = list.get(list.size() -1);
            int voltageDiff = maxVoltage - minVoltage;

            //已充满
            if(batteryReport.getVolume() != null && batteryReport.getVolume() == 100){
                if(battery.getFullVoltageDiff() == null || battery.getFullVoltageDiff() < voltageDiff){
                    battery.setFullVoltageDiff(voltageDiff);
                    battery.setFullVoltageDiffTime(now);
                }
            }

            //放电
            if(batteryReport.getElectricity() < 0 ){
                if(battery.getDischargeVoltageDiff() == null || battery.getDischargeVoltageDiff() < voltageDiff){
                    battery.setDischargeVoltageDiff(voltageDiff);
                    battery.setDischargeVoltageDiffTime(now);
                }
            }

            //当前
            battery.setRealVoltageDiff(voltageDiff);
            battery.setRealVoltageDiffTime(now);

            //压差推送
            //int maxVolDiff = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.BATTERY_MAX_VOL_DIFF.getValue()).getConfigValue());
            int maxVolDiff = 500;
            if(voltageDiff >= maxVolDiff ){
                if(battery.getMaxVolDiffLogId() == null ){
                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceType(PushMessage.SourceType.VOL_DIFF_HIGH.getValue());
                    pushMetaData.setSourceId(String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                    pushMetaData.setCreateTime(now);
                    pushMetaDataMapper.insert(pushMetaData);

                    FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.MEDIUM.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_17.getValue(), FaultLog.FaultType.CODE_17.getName(), now);
                    battery.setMaxVolDiffLogId(faultLog.getId());
                }
            }else{
                if(battery.getMaxVolDiffLogId() != null)
                faultLogMapper.handle(battery.getMaxVolDiffLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                battery.setMaxVolDiffLogId(null);
            }

            //单体电压小于最低电压，断电异常推送
            BatteryParameter batteryParameter = batteryParameterMapper.find(batteryReport.getBatteryId());
            if(batteryParameter != null && StringUtils.isNotEmpty(batteryParameter.getOcvTable())){
                Double minOcvTable = Double.parseDouble(StringUtils.split(batteryParameter.getOcvTable(), ",")[0]);
                if(minVoltage < minOcvTable){
                    if(battery.getSignVolLowLogId() == null ){
                        PushMetaData pushMetaData = new PushMetaData();
                        pushMetaData.setSourceType(PushMessage.SourceType.SIGH_VOL_LOW.getValue());
                        pushMetaData.setSourceId(String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                        pushMetaData.setCreateTime(now);
                        pushMetaDataMapper.insert(pushMetaData);

                        FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_20.getValue(), FaultLog.FaultType.CODE_20.getName(), now);
                        battery.setSignVolLowLogId(faultLog.getId());
                    }
                }else{
                    if(battery.getSignVolLowLogId() != null)
                    faultLogMapper.handle(battery.getSignVolLowLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                    battery.setSignVolLowLogId(null);
                }
            }
        }
    }

    /**
     * 换电订单上报日志
     * @param battery
     * @param batteryReport
     * @param suffix
     */
    public void insertBatteryOrderBatteryReportLog(Battery battery, BatteryReport batteryReport, String suffix) {
        String tableName = BatteryOrderBatteryReportLog.BATTERY_ORDER_REPORT_LOG_TABLE_NAME + suffix;
        boolean result = findTable(tableName);
        if (!result) {
            batteryOrderBatteryReportLogMapper.createTable(tableName);
        }
        BatteryOrderBatteryReportLog reportLog = new BatteryOrderBatteryReportLog();
        reportLog.setOrderId(battery.getOrderId());
        reportLog.setVolume(battery.getVolume());
        reportLog.setTemp(battery.getTemp());
        reportLog.setLng(batteryReport.getLng());
        reportLog.setLat(batteryReport.getLat());
        reportLog.setDistance(batteryReport.getDistance());
        reportLog.setCurrentSignal(battery.getCurrentSignal());
        reportLog.setCoordinateType("gps");
        reportLog.setReportTime(new Date());
        reportLog.setSuffix(suffix);
        reportLog.setAddress("");
        batteryOrderBatteryReportLogMapper.insert(reportLog);
    }

    /**
     * 电池参数处理
     * @param batteryReport
     */
    private BatteryParameter dealBatteryParameter(BatteryReport batteryReport) {
     //   if(batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()){
            BatteryParameter batteryParameter = batteryParameterMapper.find(batteryReport.getBatteryId());
            if(batteryParameter == null){
                batteryParameter = batteryReport;
                batteryParameter.setId(batteryReport.getBatteryId());
                batteryParameter.setType(0);
                if(batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()) {
                    batteryParameter.setLongReportTime(new Date());
                }else{
                    batteryParameter.setShortReportTime(new Date());
                }
                batteryParameter.setCreateTime(new Date());
                batteryParameterMapper.insert(batteryParameter);
            }else{
                //比较参数信息
//                List<Map<String ,Object>> list = YhdgUtils.compareTwoClass(batteryParameter, batteryReport);
                //保存同步过来的参数信息
                Integer upBms = batteryParameter.getUpBms();
                batteryParameter = batteryReport;
                batteryParameter.setUpBms(upBms);
                batteryParameter.setId(batteryReport.getBatteryId());
                if(batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()) {
                    batteryParameter.setLongReportTime(new Date());
                }else{
                    batteryParameter.setShortReportTime(new Date());
                }
                batteryParameterMapper.update(batteryParameter);

            }
            return batteryParameter;
      //  }
    }

    private String nextBatteryId() {
        BatterySequence entity = new BatterySequence();
        batterySequenceMapper.insert(entity);
        String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
        return result;
    }

    private String getTableSuffix(Date time) {
        String suffix = DateFormatUtils.format(time, "yyyyww");
        return suffix;
    }

    public static boolean isInt(String str) {
        boolean isInt = Pattern.compile("^[1-9]\\d*$").matcher(str).find();
        return isInt ;

    }

    public static boolean isDouble(String str) {
        boolean isDouble = Pattern.compile("^[0-9]+(.[0-9]+)?$").matcher(str).find();
        return isDouble;

    }
}
