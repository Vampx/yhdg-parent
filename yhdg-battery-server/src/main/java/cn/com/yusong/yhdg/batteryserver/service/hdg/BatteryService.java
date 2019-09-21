package cn.com.yusong.yhdg.batteryserver.service.hdg;

import cn.com.yusong.yhdg.batteryserver.config.AppConfig;
import cn.com.yusong.yhdg.batteryserver.entity.Param;
import cn.com.yusong.yhdg.batteryserver.entity.Result;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.MobileMessageTemplateMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.batteryserver.persistence.hdg.*;
import cn.com.yusong.yhdg.batteryserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.utils.GPSUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BatteryService extends AbstractService {
    static final Logger log = LogManager.getLogger(BatteryService.class);
    static final Pattern LNG_PATTERN = Pattern.compile("(\\d{3})(\\d{2}.\\d{4})");
    static final Pattern LAT_PATTERN = Pattern.compile("(\\d{2})(\\d{2}.\\d{4})");

    @Autowired
    AppConfig appConfig;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    KeepOrderMapper keepOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    BatteryChargeRecordMapper batteryChargeRecordMapper;
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;
    @Autowired
    BatteryOrderBatteryReportLogMapper batteryOrderBatteryReportLogMapper;
    @Autowired
    BatteryChargeRecordDetailMapper batteryChargeRecordDetailMapper;
    @Autowired
    BatteryReportDateMapper batteryReportDateMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    UnregisterBatteryMapper unregisterBatteryMapper;
    @Autowired
    UnregisterBatteryReportLogMapper unregisterBatteryReportLogMapper;
    @Autowired
    MobileMessageTemplateMapper mobileMessageTemplateMapper;
    @Autowired
    BatterySequenceMapper batterySequenceMapper;
    @Autowired
    BatteryReportMapper batteryReportMapper;
    @Autowired
    BatteryParameterMapper batteryParameterMapper;
    @Autowired
    BatteryParameterLogMapper batteryParameterLogMapper;

    public Battery findByCode(String code){
        return batteryMapper.findByCode(code);
    }

    public int updateWaitCharge(String code) {
        return batteryMapper.updateWaitCharge(code, Battery.ChargeStatus.WAIT_CHARGE.getValue());
    }

    private void insertBatteryReportLog(BatteryReportLog batteryReportLog, Date date) {
        String tableName = String.format("%s%s", BatteryReportLog.BATTERY_REPORT_LOG_TABLE_NAME, batteryReportLog.getSuffix());
        boolean result = findTable(tableName);
        if (!result) {
            batteryReportLogMapper.createTable(tableName);
        }
        batteryReportLogMapper.insert(batteryReportLog);
        String reportDate = DateFormatUtils.format(date, Constant.DATE_FORMAT);
        if (batteryReportDateMapper.update(reportDate, batteryReportLog.getBatteryId()) == 0) {
            batteryReportDateMapper.create(reportDate, batteryReportLog.getBatteryId());
        }
    }

    private void batteryReportAndDealParam(BatteryReportLog batteryReportLog, Date date, Battery battery) {
        BatteryReport batteryReport = new BatteryReport();
        batteryReport.setHeartType(BatteryReport.HeartType.OTHER_HEART.getValue());
        batteryReport.setBatteryId(batteryReportLog.getBatteryId());
        batteryReport.setVoltage(batteryReportLog.getVoltage());
        batteryReport.setElectricity(batteryReportLog.getElectricity());
        batteryReport.setCurrentCapacity(batteryReportLog.getCurrentCapacity());
        batteryReport.setFault(batteryReportLog.getProtectState());/*保护状态*/
        batteryReport.setMos(batteryReportLog.getFet());  /*bit0表示充电，bit1表示放电，0表示MOS关闭，1表示打开*/
        batteryReport.setSerials(batteryReportLog.getStrand()); /*电池串数*/
        batteryReport.setTemp(batteryReportLog.getTemp());
        batteryReport.setLng(batteryReportLog.getLng());
        batteryReport.setLat(batteryReportLog.getLat());
        batteryReport.setDistance(batteryReportLog.getDistance());
        batteryReport.setCurrentSignal(batteryReportLog.getCurrentSignal());
        batteryReport.setIsMotion(batteryReportLog.getPositionState());/*0表示位置不移动 1表示位置移动中 2表示通电中*/
        batteryReport.setSimCode(batteryReportLog.getSimCode());
        batteryReport.setSingleVoltage(batteryReportLog.getSingleVoltage());
        batteryReport.setSuffix(batteryReportLog.getSuffix());
        batteryReport.setCode(battery.getCode());
        //电池上报
        insertBatteryReport(batteryReport, date);

        //电池参数
        dealBatteryParameter(batteryReport);

        //电池参数日志
        List<BatteryParameterLog> parameterLogList = batteryParameterLogMapper.findByBatteryId(battery.getId(), BatteryParameterLog.Status.NO_REPORT.getValue());
        if (parameterLogList.size() > 0) {
            for (BatteryParameterLog batteryParameterLog : parameterLogList) {
                batteryParameterLogMapper.report(batteryParameterLog.getId(), BatteryParameterLog.Status.REPORT.getValue(), new Date());
            }
        }
    }

    /**
     * 电池日志插入
     *
     * @param batteryReport
     */
    private void insertBatteryReport(BatteryReport batteryReport, Date date) {
        String tableName = String.format("%s%s", BatteryReport.BATTERY_REPORT_TABLE_NAME, batteryReport.getSuffix());
        boolean result = findTable(tableName);
        if (!result) {
            batteryReportMapper.createTable(tableName);
        }
        batteryReport.setCreateTime(new Date());
        batteryReportMapper.insert(batteryReport);
        String reportDate = DateFormatUtils.format(date, Constant.DATE_FORMAT);
        if (batteryReportDateMapper.update(reportDate, batteryReport.getBatteryId()) == 0) {
            batteryReportDateMapper.create(reportDate, batteryReport.getBatteryId());
        }
    }

    /**
     * 电池参数处理
     *
     * @param batteryReport
     */
    private BatteryParameter dealBatteryParameter(BatteryReport batteryReport) {
        //   if(batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()){
        BatteryParameter batteryParameter = batteryParameterMapper.find(batteryReport.getBatteryId());
        if (batteryParameter == null) {
            batteryParameter = batteryReport;
            batteryParameter.setId(batteryReport.getBatteryId());
            batteryParameter.setType(0);
            if (batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()) {
                batteryParameter.setLongReportTime(new Date());
            } else {
                batteryParameter.setShortReportTime(new Date());
            }
            batteryParameter.setCreateTime(new Date());
            batteryParameterMapper.insert(batteryParameter);
        } else {
            //比较参数信息
//                List<Map<String ,Object>> list = YhdgUtils.compareTwoClass(batteryParameter, batteryReport);
            //保存同步过来的参数信息
            Integer upBms = batteryParameter.getUpBms();
            batteryParameter = batteryReport;
            batteryParameter.setUpBms(upBms);
            batteryParameter.setId(batteryReport.getBatteryId());
            if (batteryReport.getHeartType() != null && batteryReport.getHeartType() == BatteryReport.HeartType.LONG_HEART.getValue()) {
                batteryParameter.setLongReportTime(new Date());
            } else {
                batteryParameter.setShortReportTime(new Date());
            }
            batteryParameterMapper.update(batteryParameter);

        }
        return batteryParameter;
        //  }
    }

/*    private void completeCharge(Battery battery) {
        Date now = new Date();
        if (battery.getChargeRecordId() != null) { //结束充电记录
            batteryChargeRecordMapper.updateEnd(battery.getChargeRecordId(), battery.getVolume(), now);

            if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_BATTERY_COMPLETE_CHARGE.getValue());
                pushMetaData.setSourceId(battery.getChargeRecordId().toString());
                pushMetaData.setCreateTime(now);
                pushMetaDataMapper.insert(pushMetaData);

            } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.KEEPER_BATTERY_COMPLETE_CHARGE.getValue());
                pushMetaData.setSourceId(battery.getChargeRecordId().toString());
                pushMetaData.setCreateTime(now);
                pushMetaDataMapper.insert(pushMetaData);
            }

            battery.setChargeRecordId(null);
        }
    }*/

   /* private void handleChargeStatus(Battery battery, int chargeStatus, Result result) {
        Date now = new Date();

        if (log.isDebugEnabled()) {
            log.debug("battery: {} IMEI:{} 0 上报: {}", battery.getId(), battery.getCode(), chargeStatus == 0 ? "未充电" : "充电中");
        }

        if (chargeStatus == BatteryReportLog.NOT_CHARGE) { //上报未充电
            if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.NOT_CHARGE.getValue()) {
                if (battery.getVolume() < battery.getChargeCompleteVolume()
                        && (battery.getStatus() == Battery.Status.NOT_USE.getValue() || battery.getStatus() == Battery.Status.KEEPER_OUT.getValue())) {
                    battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
                    result.chargeType = Result.BEGIN_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 状态 {} 电量:({}/{}) 1_1 上报未充电, 数据状态未充电, 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()), battery.getVolume(), battery.getChargeCompleteVolume());
                    }

                } else {
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 1 电量:({}/{}) 上报未充电, 数据状态未充电, 设置结束充电", battery.getId(), battery.getCode(), battery.getVolume(), battery.getChargeCompleteVolume());
                    }
                }


            } else if (battery.getChargeStatus() != null
                    && battery.getChargeStatus() == Battery.ChargeStatus.WAIT_CHARGE.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 2 上报未充电, 数据状态待充电, 设置开始充电", battery.getId(), battery.getCode());
                }


            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 3 上报未充电, 数据状态充电中, 设置结束充电", battery.getId(), battery.getCode());
                }

                completeCharge(battery); //结束充电
                if ((battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue())
                        && battery.getVolume() < battery.getChargeCompleteVolume()) {
                    battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
                    result.chargeType = Result.BEGIN_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 4 上报未充电, 电池使用状态{}, 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                    }

                } else {
                    result.chargeType = Result.NOT_CHARGE;

                    if (log.isDebugEnabled() && battery.getVolume() >= battery.getChargeCompleteVolume()) {
                        log.debug("battery: {} IMEI:{} 4_1 电池已充满", battery.getId(), battery.getCode());
                    }
                *//*    if (log.isDebugEnabled() && battery.getIsPermitCharge() == ConstEnum.Flag.FALSE.getValue()) {
                        log.debug("battery: {} IMEI:{} 5 上报未充电 禁止充电", battery.getId(), battery.getCode());
                    }
                    if (log.isDebugEnabled() && battery.getIsPermitCharge() == ConstEnum.Flag.TRUE.getValue()
                            && (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue())) {
                        log.debug("battery: {} IMEI:{} 6 上报未充电, 电池使用状态{}, 设置结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                    }*//*
                }

                if (battery.getStatus() == Battery.Status.IN_BOX.getValue()) {
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CABINET_END_CHARGE.getValue(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));
                } else if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_END_CHARGE.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getVolume(), new Date()));
                } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {

                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.KEEPER_END_CHARGE.getValue(), battery.getKeeperMobile(), battery.getKeeperName(), battery.getKeeperId(), battery.getVolume(), new Date()));
                }
            }

        } else if (chargeStatus == BatteryReportLog.CHARGING) { //上报充电中
            if ((battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue())) {
                result.chargeType = Result.BEGIN_CHARGE;
                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                battery.setLowVolumeNoticeTime(null);

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 7 维护充电 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

            } else if (battery.getChargeStatus() != null && battery.getChargeStatus() == Battery.ChargeStatus.NOT_CHARGE.getValue()) {
                result.chargeType = Result.NOT_CHARGE;

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 8 (非维护充电或禁止充电) 设置结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.WAIT_CHARGE.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 9 充电状态: 待充电 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                battery.setLowVolumeNoticeTime(null);

                if (battery.getStatus() == Battery.Status.IN_BOX.getValue()) {
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CABINET_START_CHARGE.getValue(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));
                } else if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_START_CHARGE.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getVolume(), new Date()));
                } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.KEEPER_START_CHARGE.getValue(), battery.getKeeperMobile(), battery.getKeeperName(), battery.getKeeperId(), battery.getVolume(), new Date()));
                }

            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                battery.setLowVolumeNoticeTime(null);

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 10 充电状态: 充电中 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }
            }

            if (battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                BatteryChargeRecord chargeRecord = null;
                if (battery.getChargeRecordId() == null) {
                    chargeRecord = new BatteryChargeRecord();
                    chargeRecord.setBatteryId(battery.getId());
                    chargeRecord.setAgentId(battery.getAgentId());
                    chargeRecord.setBeginTime(now);
                    chargeRecord.setBeginVolume(battery.getVolume());
                    chargeRecord.setCurrentVolume(battery.getVolume());
                    chargeRecord.setReportTime(now);
                    chargeRecord.setCreateTime(now);
                } else {
                    chargeRecord = batteryChargeRecordMapper.find(battery.getChargeRecordId());
                }

                if (battery.getStatus() == Battery.Status.IN_BOX.getValue()) {

                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.CABINET.getValue());
                        chargeRecord.setCabinetId(battery.getCabinetId());
                        chargeRecord.setCabinetName(battery.getCabinetName());
                        chargeRecord.setBoxNum(battery.getBoxNum());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                    }

                } else if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {

                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.CUSTOMER.getValue());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {

                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.KEEPER.getValue());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                } else if (battery.getStatus() == Battery.Status.NOT_USE.getValue()) {
                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.KEEPER.getValue());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                }

                if (battery.getChargeRecordId() == null) {
                    batteryChargeRecordMapper.insert(chargeRecord);
                    battery.setChargeRecordId(chargeRecord.getId());

                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceType(PushMessage.SourceType.KEEPER_BATTERY_START_CHARGE.getValue());
                    pushMetaData.setSourceId(battery.getChargeRecordId().toString());
                    pushMetaData.setCreateTime(now);
                    pushMetaDataMapper.insert(pushMetaData);

                } else {
                    batteryChargeRecordMapper.updateCurrentVolume(battery.getChargeRecordId(), battery.getVolume(), now);
                }

                //已用充电时长
                int useDuration = (int) (new Date().getTime() - chargeRecord.getBeginTime().getTime()) / 1000;
                //购买充电时长
                int duration = chargeRecord.getDuration() * 60 * 60;
                if (useDuration >= duration) {
                    //发送结束充电
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery{} IMEI{} chargeRecord{} 达到充电时长{} 返回结束充电", battery.getId(), battery.getCode(), chargeRecord.getId(), chargeRecord.getDuration());
                    }
                } else if (battery.getVolume() >= battery.getChargeCompleteVolume()) {
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery{} IMEI{} 状态{} chargeRecord{} 充电时长{} 已经满电({}/{}) 返回结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()), chargeRecord.getId(), chargeRecord.getDuration(), battery.getVolume(), battery.getChargeCompleteVolume());
                    }
                }

                int power = (int) (71.4 * battery.getElectricity() / 100 / 0.88);
                insertBatteryChargeRecordDetail(battery.getChargeRecordId(), battery.getVolume(), power, getTableSuffix(chargeRecord.getCreateTime()));
            }

        }

        if (log.isDebugEnabled()) {
            log.debug("battery: {} IMEI:{} 返回充电状态: {}", battery.getId(), battery.getCode(), result.chargeType);
        }
    }*/

    /*只处理充电状态*/
  /*  private void onlyHandleChargeStatus(Battery battery, int chargeStatus, Result result) {

        if (log.isDebugEnabled()) {
            log.debug("battery: {} IMEI:{} 0 上报: {}", battery.getId(), battery.getCode(), chargeStatus == 0 ? "未充电" : "充电中");
        }

        if (chargeStatus == BatteryReportLog.NOT_CHARGE) { //上报未充电
            if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.NOT_CHARGE.getValue()) {
                if (battery.getVolume() != null && battery.getVolume() < battery.getChargeCompleteVolume()
                        && (battery.getStatus() == Battery.Status.NOT_USE.getValue() || battery.getStatus() == Battery.Status.KEEPER_OUT.getValue())) {
                    battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
                    result.chargeType = Result.BEGIN_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 状态 {} 电量:({}/{}) 1_1 上报未充电, 数据状态未充电, 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()), battery.getVolume(), battery.getChargeCompleteVolume());
                    }

                } else {
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 1 电量:({}/{}) 上报未充电, 数据状态未充电, 设置结束充电", battery.getId(), battery.getCode(), battery.getVolume(), battery.getChargeCompleteVolume());
                    }
                }


            } else if (battery.getChargeStatus() != null
                    && battery.getChargeStatus() == Battery.ChargeStatus.WAIT_CHARGE.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 2 上报未充电, 数据状态待充电, 设置开始充电", battery.getId(), battery.getCode());
                }


            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 3 上报未充电, 数据状态充电中, 设置结束充电", battery.getId(), battery.getCode());
                }

                //completeCharge(battery); //结束充电
                if ((battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue())
                        && battery.getVolume() != null && battery.getVolume() < battery.getChargeCompleteVolume()) {
                    battery.setChargeStatus(Battery.ChargeStatus.WAIT_CHARGE.getValue());
                    result.chargeType = Result.BEGIN_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{} 4 上报未充电, 电池使用状态{}, 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                    }

                } else {
                    result.chargeType = Result.NOT_CHARGE;

                    if (log.isDebugEnabled() && battery.getVolume() != null && battery.getVolume() >= battery.getChargeCompleteVolume()) {
                        log.debug("battery: {} IMEI:{} 4_1 电池已充满", battery.getId(), battery.getCode());
                    }
                  *//*  if (log.isDebugEnabled() && battery.getIsPermitCharge() == ConstEnum.Flag.FALSE.getValue()) {
                        log.debug("battery: {} IMEI:{} 5 上报未充电 禁止充电", battery.getId(), battery.getCode());
                    }
                    if (log.isDebugEnabled() && battery.getIsPermitCharge() == ConstEnum.Flag.TRUE.getValue()
                            && (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue())) {
                        log.debug("battery: {} IMEI:{} 6 上报未充电, 电池使用状态{}, 设置结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                    }*//*
                }

            }

        } else if (chargeStatus == BatteryReportLog.CHARGING) { //上报充电中
            if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue() || battery.getStatus() == Battery.Status.NOT_USE.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                battery.setLowVolumeNoticeTime(null);

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 7 维护充电 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

            } else if (battery.getChargeStatus() != null && battery.getChargeStatus() == Battery.ChargeStatus.NOT_CHARGE.getValue()) {
                result.chargeType = Result.NOT_CHARGE;

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 8 (非维护充电或禁止充电) 设置结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.WAIT_CHARGE.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 9 充电状态: 待充电 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }

            } else if (battery.getChargeStatus() != null &&
                    battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                result.chargeType = Result.BEGIN_CHARGE;
                battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                battery.setLowVolumeNoticeTime(null);

                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{} 使用状态: {} 10 充电状态: 充电中 设置开始充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()));
                }
            }
            Date now = new Date();
            if (battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                BatteryChargeRecord chargeRecord = null;
                if (battery.getChargeRecordId() == null) {
                    chargeRecord = new BatteryChargeRecord();
                    chargeRecord.setBatteryId(battery.getId());
                    chargeRecord.setAgentId(battery.getAgentId());
                    chargeRecord.setBeginTime(now);
                    chargeRecord.setBeginVolume(battery.getVolume());
                    chargeRecord.setCurrentVolume(battery.getVolume());
                    chargeRecord.setReportTime(now);
                    chargeRecord.setCreateTime(now);
                } else {
                    chargeRecord = batteryChargeRecordMapper.find(battery.getChargeRecordId());
                }

                if (battery.getStatus() == Battery.Status.IN_BOX.getValue()) {

                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.CABINET.getValue());
                        chargeRecord.setCabinetId(battery.getCabinetId());
                        chargeRecord.setCabinetName(battery.getCabinetName());
                        chargeRecord.setBoxNum(battery.getBoxNum());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                    }

                } else if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()) {

                    if (battery.getChargeRecordId() == null) {
//                        chargeRecord.setType(BatteryChargeRecord.Type.CUSTOMER.getValue());
//                        chargeRecord.setCustomerId(battery.getCustomerId());
//                        chargeRecord.setCustomerMobile(battery.getCustomerMobile());
//                        chargeRecord.setCustomerFullname(battery.getCustomerFullname());
//                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {

                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.KEEPER.getValue());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                } else if (battery.getStatus() == Battery.Status.NOT_USE.getValue()) {
                    if (battery.getChargeRecordId() == null) {
                        chargeRecord.setType(BatteryChargeRecord.Type.KEEPER.getValue());
                        chargeRecord.setDuration(Constant.MAX_CHARGE_DURATION);
                        chargeRecord.setCurrentVolume(battery.getVolume());
                    }
                }

                if (battery.getChargeRecordId() == null) {
                    batteryChargeRecordMapper.insert(chargeRecord);
                    battery.setChargeRecordId(chargeRecord.getId());

                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceType(PushMessage.SourceType.KEEPER_BATTERY_START_CHARGE.getValue());
                    pushMetaData.setSourceId(battery.getChargeRecordId().toString());
                    pushMetaData.setCreateTime(now);
                    pushMetaDataMapper.insert(pushMetaData);

                } else {
                    batteryChargeRecordMapper.updateCurrentVolume(battery.getChargeRecordId(), battery.getVolume(), now);
                }

                //已用充电时长
                int useDuration = (int) (new Date().getTime() - chargeRecord.getBeginTime().getTime()) / 1000;
                //购买充电时长
                int duration = chargeRecord.getDuration() * 60 * 60;
                if (useDuration >= duration) {
                    //发送结束充电
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery{} IMEI{} chargeRecord{} 达到充电时长{} 返回结束充电", battery.getId(), battery.getCode(), chargeRecord.getId(), chargeRecord.getDuration());
                    }
                } else if (battery.getVolume() >= battery.getChargeCompleteVolume()) {
                    result.chargeType = Result.NOT_CHARGE;
                    if (log.isDebugEnabled()) {
                        log.debug("battery{} IMEI{} 状态{} chargeRecord{} 充电时长{} 已经满电({}/{}) 返回结束充电", battery.getId(), battery.getCode(), Battery.Status.getName(battery.getStatus()), chargeRecord.getId(), chargeRecord.getDuration(), battery.getVolume(), battery.getChargeCompleteVolume());
                    }
                }

                int power = 50 * battery.getElectricity();
                insertBatteryChargeRecordDetail(battery.getChargeRecordId(), battery.getVolume(), power, getTableSuffix(chargeRecord.getCreateTime()));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("battery: {} IMEI:{} 返回充电状态: {}", battery.getId(), battery.getCode(), result.chargeType);
        }
    }*/

    private String getTableSuffix(Date time) {
        String suffix = DateFormatUtils.format(time, "yyyyww");
        return suffix;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Result report(Param param) {
        Result result = new Result();
        Date now = new Date();

        //默认回包  响应码 1  充电状态0   充电时长 0  心跳时长180
        result.code = param.code;
        result.rtnCode = 1;
        result.chargeType = Result.NOT_CHARGE;
        result.duration = 0;
        result.nextHeartbeat = 180;
        result.reportSingleVoltage = 2;
        result.gpsSwitch = 0;
        result.lockSwitch = 0;
        result.gprsShutdown = 0;
        result.shutdownVoltage = 42000;
        result.acceleretedSpeed = 1;

        if (StringUtils.isEmpty(param.code)) {
            log.error("code不存在");
            return result;
        }

        //电池经纬度
        Double lng = 0.0, lat = 0.0;
        String coordinateType = "gprs";
        //计算经纬度
        if (StringUtils.isNotEmpty(param.lng_) || StringUtils.isNotEmpty(param.lat_)) {
            if (param.lng_.indexOf(".") == 3) {
                lng = Double.parseDouble(param.lng_);
                lat = Double.parseDouble(param.lat_);
            } else if (param.lng_.indexOf(".") == 5) {
                coordinateType = "gps";
                Matcher lngMatcher = LNG_PATTERN.matcher(param.lng_);
                if (lngMatcher.find()) {
                    lng = Double.parseDouble(lngMatcher.group(1)) + Double.parseDouble(lngMatcher.group(2)) / 60;
                }
                Matcher latMatcher = LAT_PATTERN.matcher(param.lat_);
                if (latMatcher.find()) {
                    lat = Double.parseDouble(latMatcher.group(1)) + Double.parseDouble(latMatcher.group(2)) / 60;
                }
                double[] d = GPSUtil.gps84_To_bd09(lat, lng);
                lng = d[1];
                lat = d[0];
            }
        }

        Battery battery = batteryMapper.findByCode(param.code);
        //添加电池
        if (battery == null) {
            battery = new Battery();
            String id = nextBatteryId();
            battery.setId(id);
            battery.setCode(param.code);
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
            batteryMapper.insert(battery);
        }
//        if (battery == null) {
//            UnregisterBattery unregisterBattery = new UnregisterBattery(param.code,
//                    param.version,
//                    param.voltage,
//                    param.electricity,
//                    param.surplusVolume,
//                    param.volume,
//                    param.second,
//                    param.produceDate,
//                    param.protectState,
//                    param.fetStatus,
//                    param.batteryStrand,
//                    param.ntc,
//                    lng,
//                    lat,
//                    param.fet,
//                    param.positionState,
//                    param.signal,
//                    param.simCode,
//                    param.singleVoltage,
//                    param.percent);
//
//            //增加未注册电池日志上报
//            insertUnregisterBattery(unregisterBattery);
//
//            UnregisterBatteryReportLog unregisterBatteryReportLog = new UnregisterBatteryReportLog(param.code,
//                    param.version,
//                    param.voltage,
//                    param.electricity,
//                    param.surplusVolume,
//                    param.volume,
//                    param.second,
//                    param.produceDate,
//                    param.protectState,
//                    param.fetStatus,
//                    param.batteryStrand,
//                    param.ntc,
//                    lng,
//                    lat,
//                    param.fet,
//                    param.positionState,
//                    param.signal,
//                    param.simCode,
//                    param.singleVoltage,
//                    param.percent);
//            unregisterBatteryReportLog.setSuffix(getTableSuffix(now));
//            insertUnregisterBatteryReportLog(unregisterBatteryReportLog);
//            log.error("code={}, 电池不存在", param.code);
//
//            return result;
//        }
        result.gpsSwitch = battery.getGpsSwitch();
        result.lockSwitch = battery.getLockSwitch();
        result.gprsShutdown = battery.getGprsShutdown();
        result.shutdownVoltage = battery.getShutdownVoltage();
        result.acceleretedSpeed = battery.getAcceleretedSpeed();

        result.id = battery.getId();
        result.reportSingleVoltage = battery.getIsReportVoltage() == ConstEnum.Flag.FALSE.getValue() ? 2 : battery.getIsReportVoltage();

        //初始化电池上报信息
        BatteryReportLog batteryReportLog = new BatteryReportLog(battery.getId(), param.version, param.voltage, param.electricity, param.surplusVolume,
                param.volume, param.second, param.produceDate, param.protectState, param.fet, param.batteryStrand, param.ntc, lng, lat, param.fetStatus, param.positionState, param.signal, param.chargeStatus, null, param.simCode, param.singleVoltage);
        batteryReportLog.setSuffix(getTableSuffix(now));
        batteryReportLog.setCoordinateType(coordinateType);

        BatteryReportLog.ProtectState[] protectStates = BatteryReportLog.ProtectState.values();

        char[] str = new StringBuilder(String.format("%0" + protectStates.length + "d", Long.parseLong(Integer.toBinaryString(batteryReportLog.getProtectState() == null ? 0 : batteryReportLog.getProtectState())))).reverse().toString().toCharArray();
        for (int i = 0; i < str.length; i++) {
            if ('1' == str[i]) {
                FaultLog faultLog = null;
                switch (protectStates[i].getValue()) {
                    case 1:
                        if (battery.getMonomerOvervoltageFaultLogId() == null) {
                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_8.getValue(), FaultLog.FaultType.CODE_8.getName());
                            battery.setMonomerOvervoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 2:
                        if (battery.getMonomerLowvoltageFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_9.getValue(), FaultLog.FaultType.CODE_9.getName());
                            battery.setMonomerLowvoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 3:
                        if (battery.getWholeOvervoltageFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_10.getValue(), FaultLog.FaultType.CODE_10.getName());
                            battery.setWholeOvervoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 4:
                        if (battery.getWholeLowvoltageFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_11.getValue(), FaultLog.FaultType.CODE_11.getName());
                            battery.setWholeLowvoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 5:
                        if (battery.getChargeOvertempFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_12.getValue(), FaultLog.FaultType.CODE_12.getName());
                            battery.setChargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 6:
                        if (battery.getChargeLowtempFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_13.getValue(), FaultLog.FaultType.CODE_13.getName());
                            battery.setChargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 7:
                        if (battery.getDischargeOvertempFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_14.getValue(), FaultLog.FaultType.CODE_14.getName());
                            battery.setDischargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 8:
                        if (battery.getDischargeLowtempFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_15.getValue(), FaultLog.FaultType.CODE_15.getName());
                            battery.setDischargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 9:
                        if (battery.getChargeOvercurrentFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_16.getValue(), FaultLog.FaultType.CODE_16.getName());
                            battery.setChargeOvercurrentFaultLogId(faultLog.getId());
                        }
                        break;
                    case 10:
                        if (battery.getDischargeOvercurrentFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_17.getValue(), FaultLog.FaultType.CODE_17.getName());
                            battery.setDischargeOvercurrentFaultLogId(faultLog.getId());
                        }
                        break;
                    case 11:
                        if (battery.getShortCircuitFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_18.getValue(), FaultLog.FaultType.CODE_18.getName());
                            battery.setShortCircuitFaultLogId(faultLog.getId());
                        }
                        break;
                    case 12:
                        if (battery.getTestingIcFaultLogId() == null) {

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_19.getValue(), FaultLog.FaultType.CODE_19.getName());
                            battery.setTestingIcFaultLogId(faultLog.getId());
                        }
                        break;
//                    case 13:
//                        if (battery.getSoftwareLockingFaultLogId() == null) {
//
//                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_20.getValue(), FaultLog.FaultType.CODE_20.getName());
//                            battery.setSoftwareLockingFaultLogId(faultLog.getId());
//                        }
//                        break;
                    default:
                        break;
                }
            }
        }
        if (param.hit != null && param.hit == ConstEnum.Flag.TRUE.getValue() && battery.getHitFaultLogId() == null) {
            FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_22.getValue(), FaultLog.FaultType.CODE_22.getName());
            battery.setHitFaultLogId(faultLog.getId());
        }
        if (param.dismantle != null && param.dismantle == ConstEnum.Flag.TRUE.getValue() && battery.getDismantleFaultLogId() == null) {
            FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_23.getValue(), FaultLog.FaultType.CODE_23.getName());
            battery.setDismantleFaultLogId(faultLog.getId());
        }

        battery.setVoltage(batteryReportLog.getVoltage());
        battery.setVersion(batteryReportLog.getVersion());
        battery.setElectricity(batteryReportLog.getElectricity());
        //电池电量毫安
        battery.setCurrentCapacity(batteryReportLog.getCurrentCapacity());

        //电池电量百分比
        int percentVolume = 0;
        if (StringUtils.isNotEmpty(param.percent) && StringUtils.isNumeric(param.percent)) {
            percentVolume = Integer.parseInt(param.percent, 16);
        } else {
            if (battery.getTotalCapacity() != null && battery.getTotalCapacity() != 0 && battery.getCurrentCapacity() != null) {
                percentVolume = (int) Math.ceil(100.0d * battery.getCurrentCapacity() / battery.getTotalCapacity());
            }
        }

        //上报协议距离间隔
        Integer distance = 0;
        percentVolume = percentVolume > 100 ? 100 : percentVolume;
        //经纬度不为空则计算上报协议距离间隔
//        if (coordinateType.equals("gps") && battery.getLat() != null && battery.getLng() != null && lat != 0.0 && lng != 0.0) {
//            distance = (int) GeoHashUtils.gps2m(lat, lng, battery.getLat(), battery.getLng());
//        }else{
//
//        }

        if (battery.getVolume() != null && battery.getVolume() - percentVolume > 0) {
            int mileage = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()).getConfigValue());
            distance = mileage * 1000 * (battery.getVolume() - percentVolume) / 100;
        }
        batteryReportLog.setDistance(distance);

        //电池上报并处理参数
        batteryReportAndDealParam(batteryReportLog, now, battery);
//            insertBatteryReportLog(batteryReportLog, now);


        battery.setVolume(percentVolume);

        battery.setTotalDistance((battery.getTotalDistance() == null ? batteryReportLog.getDistance() : battery.getTotalDistance()) + batteryReportLog.getDistance());
        battery.setOrderDistance((battery.getOrderDistance() == null ? batteryReportLog.getDistance() : battery.getOrderDistance()) + batteryReportLog.getDistance());

        battery.setSimMemo(batteryReportLog.getSimCode());
        battery.setTotalCapacity(batteryReportLog.getTotalCapacity());
        battery.setUseCount(batteryReportLog.getUseCount());
        battery.setProduceDate(batteryReportLog.getProduceDate());
        battery.setProtectState(batteryReportLog.getProtectState());
        battery.setFet(batteryReportLog.getFet());
        battery.setPositionState(batteryReportLog.getPositionState());
        battery.setStrand(batteryReportLog.getStrand());
        battery.setTemp(batteryReportLog.getTemp());

        if (coordinateType.equals("gps")) {
            battery.setLng(batteryReportLog.getLng());
            battery.setLat(batteryReportLog.getLat());
        }
        battery.setFetStatus(batteryReportLog.getFetStatus());
        battery.setCurrentSignal(batteryReportLog.getCurrentSignal());
        battery.setSingleVoltage(batteryReportLog.getSingleVoltage());
        battery.setReportTime(new Date());
        battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
/*        if (batteryReportLog.getChargeStatus() != null) {
            handleChargeStatus(battery, batteryReportLog.getChargeStatus(), result);
        }*/
//        int lowVolume = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.LOW_VOLUME.getValue()).getConfigValue());
//        int lowVolumeInterval = Integer.parseInt(systemConfigMapper.find(ConstEnum.SystemConfigKey.LOW_VOLUME_INTERVAL.getValue()).getConfigValue());
//        if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue() && battery.getChargeStatus() != Battery.ChargeStatus.CHARGING.getValue()) {
//            if (battery.getVolume() <= lowVolume && battery.getLowVolumeNoticeTime() == null) {
////                MobileMessageTemplate template = mobileMessageTemplateMapper.find(0, MobileMessageTemplate.Type.LOW_ELECTRICITY.getValue());
////                String[] variables = template.getVariable().split(",");
////                String[] values = {battery.getCustomerFullname(), battery.getId()};
////                String[] variable = new String[variables.length * 2];
////                int j = 0;
////                for (int i = 0; i < variable.length; i++) {
////                    if (i % 2 == 0) {
////                        variable[i] = variables[j];
////                    } else {
////                        variable[i] = values[j];
////                        j++;
////                    }
////                }
////                String content = template.replace(variable);
////                try {
////                    ClientBizUtils.Resp<Msg212000001> resp = ClientBizUtils.sendMobileMessage(appConfig,
////                            battery.getCustomerMobile(),
////                            content,
////                            MobileMessageTemplate.DEFAULT_APP_ID,
////                            template.getId().byteValue(),
////                            MobileMessage.buildVariableJson(variable),
////                            template.getCode());
////                    battery.setLowVolumeNoticeVolume(battery.getVolume());
////                    battery.setLowVolumeNoticeTime(now); //设置通知发送时间
////                } catch (Exception e) {
////                    log.error("send sms error", e);
////                }
//            }
          /*  List<Integer> lowVolumeList = new ArrayList<Integer>();
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
                    if ((battery.getVolume() <= lowVolumeList.get(i) && battery.getVolume() > lowVolumeList.get(i + 1))
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
            }*/
//        }
//        if (result.chargeType == Result.NOT_CHARGE) {
//            battery.setChargeOrderId(null);
//        }
        batteryMapper.update(battery);
        if (StringUtils.isNotEmpty(battery.getOrderId()) && (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue() || battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue())) {
            batteryMapper.updateStatus(battery.getCode(), battery.getStatus(), Battery.Status.CUSTOMER_OUT.getValue());
            batteryOrderMapper.updateOrderStatus(battery.getOrderId(), BatteryOrder.OrderStatus.INIT.getValue(), BatteryOrder.OrderStatus.TAKE_OUT.getValue());
        }
        if (battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue()
                || battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
            if (StringUtils.isNotEmpty(battery.getOrderId())) {
                BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());

                batteryOrderMapper.update(battery.getOrderId(), battery.getVolume(), battery.getOrderDistance(),battery.getCurrentCapacity());
                //换电订单电池上报日志
                insertBatteryOrderBatteryReportLog(battery, batteryReportLog, getTableSuffix(batteryOrder.getCreateTime()));
            }
        } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {
            if (StringUtils.isNotEmpty(battery.getOrderId())) {
                keepOrderMapper.update(battery.getOrderId(), battery.getVolume());
            }
        }
        if (battery.getPositionState() != null) {
            //根据电池当前移动状态 更改心跳时长
            if (battery.getPositionState() == Battery.PositionState.NOT_MOVE.getValue()) {
                result.nextHeartbeat = battery.getStayHeartbeat();
            } else if (battery.getPositionState() == Battery.PositionState.MOVE.getValue()) {
                result.nextHeartbeat = battery.getMoveHeartbeat();
            } else if (battery.getPositionState() == Battery.PositionState.ELECTRICITY.getValue()) {
                result.nextHeartbeat = battery.getElectrifyHeartbeat();

                if (battery.getNotElectrifyFaultLogId() != null) {
                    faultLogMapper.handle(battery.getNotElectrifyFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                }
            }
        }
        result.rtnCode = 0;
        return result;
    }

    private String nextBatteryId() {
        BatterySequence entity = new BatterySequence();
        batterySequenceMapper.insert(entity);
        String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
        return result;
    }

    public int update(Battery battery) {
        return batteryMapper.update(battery);
    }

    public void insertBatteryOrderBatteryReportLog(Battery battery, BatteryReportLog batteryReportLog, String suffix) {
        String tableName = BatteryOrderBatteryReportLog.BATTERY_ORDER_REPORT_LOG_TABLE_NAME + suffix;
        boolean result = findTable(tableName);
        if (!result) {
            batteryOrderBatteryReportLogMapper.createTable(tableName);
        }
        BatteryOrderBatteryReportLog reportLog = new BatteryOrderBatteryReportLog();
        reportLog.setOrderId(battery.getOrderId());
        reportLog.setVolume(battery.getVolume());
        reportLog.setTemp(battery.getTemp());
        reportLog.setLng(batteryReportLog.getLng());
        reportLog.setLat(batteryReportLog.getLat());
        reportLog.setDistance(batteryReportLog.getDistance());
        reportLog.setCurrentSignal(battery.getCurrentSignal());
        reportLog.setCoordinateType(batteryReportLog.getCoordinateType());
        reportLog.setReportTime(new Date());
        reportLog.setSuffix(suffix);
        reportLog.setAddress(batteryReportLog.getAddress());
        batteryOrderBatteryReportLogMapper.insert(reportLog);
    }

    public void insertBatteryChargeRecordDetail(Long id, Integer volume, Integer power, String suffix) {
        String tableName = BatteryChargeRecordDetail.BATTERY_CHARGE_RECORD_DETAIL_TABLE_NAME + suffix;
        boolean result = findTable(tableName);
        if (!result) {
            batteryChargeRecordDetailMapper.createTable(tableName);
        }
        BatteryChargeRecordDetail batteryChargeRecordDetail = new BatteryChargeRecordDetail();
        batteryChargeRecordDetail.setId(id);
        batteryChargeRecordDetail.setReportTime(new Date());
        batteryChargeRecordDetail.setCurrentPower(power);
        batteryChargeRecordDetail.setCurrentVolume(volume);
        batteryChargeRecordDetail.setSuffix(suffix);
        batteryChargeRecordDetailMapper.insert(batteryChargeRecordDetail);
    }

    //未注册电池插入
    public void insertUnregisterBattery(UnregisterBattery unregisterBattery) {
        UnregisterBattery entity = unregisterBatteryMapper.find(unregisterBattery.getId());

        unregisterBattery.setCreateTime(new Date());
        if (entity == null) {
            unregisterBatteryMapper.insert(unregisterBattery);
        } else {
            unregisterBatteryMapper.update(unregisterBattery);
        }
    }

    //未注册电池日志插入
    public void insertUnregisterBatteryReportLog(UnregisterBatteryReportLog unregisterBatteryReportLog) {
        String tableName = String.format("%s%s", UnregisterBatteryReportLog.UNREGISTER_BATTERY_REPORT_LOG_TABLE_NAME, unregisterBatteryReportLog.getSuffix());
        String table = unregisterBatteryReportLogMapper.findTable(tableName);
        if (table == null) {
            unregisterBatteryReportLogMapper.createTable(tableName);
        }

        unregisterBatteryReportLog.setCreateTime(new Date());
        unregisterBatteryReportLogMapper.insert(unregisterBatteryReportLog);

    }

    public FaultLog insertFaultLog(Integer faultLevel, Integer agentId, String agentName, String batteryId, Integer faultType, String faultContent) {
        FaultLog data = new FaultLog();
        data.setFaultLevel(faultLevel);
        data.setAgentId(agentId);
        data.setAgentName(agentName);
        data.setBatteryId(batteryId);
        data.setFaultType(faultType);
        data.setFaultContent(faultContent);
        data.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        data.setCreateTime(new Date());
        faultLogMapper.create(data);
        return data;
    }

}
