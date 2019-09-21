package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetChargerMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetChargerReportDateMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetChargerReportMapper;
import cn.com.yusong.yhdg.cabinetserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCharger;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetChargerReport;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CabinetChargerService extends AbstractService{
    private static final Logger log = LogManager.getLogger(CabinetChargerService.class);

    @Autowired
    CabinetChargerMapper cabinetChargerMapper;
    @Autowired
    CabinetChargerReportMapper cabinetChargerReportMapper;
    @Autowired
    CabinetChargerReportDateMapper cabinetChargerReportDateMapper;


    public List<CabinetCharger> findListByCabinet(String cabinetId) {
        return cabinetChargerMapper.findListByCabinet(cabinetId);
    }

    public List<CabinetCharger> findByOldVersion(String chargerVersion) {
        return cabinetChargerMapper.findByOldVersion(chargerVersion);
    }

    public List<CabinetCharger> findOrInsert(String cabinetId, List<HeartParam.Box> boxList) {
        List<CabinetCharger>  cabinetChargerList = cabinetChargerMapper.findListByCabinet(cabinetId);
        if(cabinetChargerList == null || cabinetChargerList.size() == 0){
            for (int i = 0; i < boxList.size(); i++) {
                HeartParam.Box e = boxList.get(i);

                String boxNum = String.format("%02d", e.boxNum);
                CabinetCharger cabinetCharger = new CabinetCharger();
                cabinetCharger.setCabinetId(cabinetId);
                cabinetCharger.setBoxNum(boxNum);
                cabinetCharger.setChargerVersion(e.chargerVersion);
                cabinetCharger.setChargerModule(e.chargerModule);
                cabinetCharger.setChargeState(e.chargeState);
                cabinetCharger.setChargeStage(e.chargeStage);
                cabinetCharger.setChargeTime(e.chargeTime);
                cabinetCharger.setChargeVoltage(e.chargeVoltage);
                cabinetCharger.setBatteryVoltage(e.batteryVoltage);
                cabinetCharger.setChargeCurrent(e.chargeCurrent);
                cabinetCharger.setTransformerTemp(e.transformerTemperature);
                cabinetCharger.setHeatsinkTemp(e.heatsinkTemperature);
                cabinetCharger.setAmbientTemp(e.ambientTemperature);
                cabinetCharger.setChargerFault(e.chargerFault);

                cabinetCharger.setEnableCharge(ConstEnum.Flag.TRUE.getValue());
                cabinetCharger.setEnableLink(Constant.ENABLE_LINK);
                cabinetCharger.setAutoSwtichMode(Constant.AUTO_SWTICH_MODE);
                cabinetCharger.setMaxChargeVoltageOfStage1(Constant.MAX_CHARGE_VOLTAGE_OF_STAGE1);
                cabinetCharger.setChargeCurrentOfStage1(Constant.CHARGE_CURRENT_OF_STAGE1);
                cabinetCharger.setMaxChargeVoltageOfStage2(Constant.MAX_CHARGE_VOLTAGE_OF_STAGE2);
                cabinetCharger.setChargeCurrentOfStage2(Constant.CHARGE_CURRENT_OF_STAGE2);
                cabinetCharger.setSlopeVoltage(Constant.SLOPE_VOLTAGE);
                cabinetCharger.setChargeCurrentOfStage3(Constant.CHARGE_CURRENT_OF_STAGE3);
                cabinetCharger.setSlopeVoltage(Constant.SLOPE_VOLTAGE);
                cabinetCharger.setFullVoltage(Constant.FULL_VOLTAGE);
                cabinetCharger.setSlopeCurrent(Constant.SLOPE_CURRENT);
                cabinetCharger.setMinCurrentOfStage4(Constant.MIN_CURRENT_OF_STAGE4);
                cabinetCharger.setLowTemperatureMode(Constant.LOW_TEMPERATURE_MODE);
                cabinetCharger.setBoxForbidden(Constant.BOX_FORBIDDEN);
                cabinetCharger.setOther(Constant.OTHER);
                cabinetChargerMapper.insert(cabinetCharger);

                cabinetChargerList.add(cabinetCharger);
            }
        }
        return cabinetChargerList;
    }

    public void update(String cabinetId, List<HeartParam.Box> boxList) {
        for (int i = 0; i < boxList.size(); i++) {
            HeartParam.Box e = boxList.get(i);
            if(e.chargerModule != null && !e.chargerModule.equals(HeartParam.EMPTY_CHARGER_CODE)){
                String boxNum = String.format("%02d", e.boxNum);
                CabinetCharger cabinetCharger = new CabinetCharger();
                cabinetCharger.setCabinetId(cabinetId);
                cabinetCharger.setBoxNum(boxNum);
                cabinetCharger.setChargerVersion(e.chargerVersion);
                cabinetCharger.setChargerModule(e.chargerModule);
                cabinetCharger.setChargeState(e.chargeState);
                cabinetCharger.setChargeStage(e.chargeStage);
                cabinetCharger.setChargeTime(e.chargeTime);
                cabinetCharger.setChargeVoltage(e.chargeVoltage);
                cabinetCharger.setBatteryVoltage(e.batteryVoltage);
                cabinetCharger.setChargeCurrent(e.chargeCurrent);
                cabinetCharger.setTransformerTemp(e.transformerTemperature);
                cabinetCharger.setHeatsinkTemp(e.heatsinkTemperature);
                cabinetCharger.setAmbientTemp(e.ambientTemperature);
                cabinetCharger.setChargerFault(e.chargerFault);
                cabinetCharger.setBoxForbidden(e.boxForbidden);
                cabinetChargerMapper.update(cabinetCharger);

                //充电状态才保存充电器日志
//                if (cabinetCharger.getChargeStage() != CabinetCharger.ChargeStage.FREE.getValue() && cabinetCharger.getChargeStage() != CabinetCharger.ChargeStage.CHARGE_FULL.getValue()) {
                    CabinetChargerReport cabinetChargerReport = new CabinetChargerReport();
                    cabinetChargerReport.setCabinetId(cabinetCharger.getCabinetId());
                    cabinetChargerReport.setBoxNum(cabinetCharger.getBoxNum());
                    cabinetChargerReport.setChargerVersion(cabinetCharger.getChargerVersion());
                    cabinetChargerReport.setChargerModule(cabinetCharger.getChargerModule());
                    cabinetChargerReport.setChargeState(cabinetCharger.getChargeState());
                    cabinetChargerReport.setChargeStage(cabinetCharger.getChargeStage());
                    cabinetChargerReport.setChargeTime(cabinetCharger.getChargeTime());
                    cabinetChargerReport.setChargeVoltage(cabinetCharger.getChargeVoltage());
                    cabinetChargerReport.setBatteryVoltage(cabinetCharger.getBatteryVoltage());
                    cabinetChargerReport.setChargeCurrent(cabinetCharger.getChargeCurrent());
                    cabinetChargerReport.setTransformerTemp(cabinetCharger.getTransformerTemp());
                    cabinetChargerReport.setHeatsinkTemp(cabinetCharger.getHeatsinkTemp());
                    cabinetChargerReport.setAmbientTemp(cabinetCharger.getAmbientTemp());
                    cabinetChargerReport.setChargerFault(cabinetCharger.getChargerFault());
                    cabinetChargerReport.setCreateTime(new Date());
                    String suffix = DateFormatUtils.format(cabinetChargerReport.getCreateTime(), Constant.DATE_FORMAT_NO_LINE);
                    cabinetChargerReport.setSuffix(suffix);

                    //查询充电器的状态
                    CabinetCharger dbCabinetCharger = cabinetChargerMapper.find(cabinetId, boxNum);
                    cabinetChargerReport.setEnableCharge(dbCabinetCharger.getEnableCharge());
                    cabinetChargerReport.setEnableLink(dbCabinetCharger.getEnableLink());
                    cabinetChargerReport.setAutoSwtichMode(dbCabinetCharger.getAutoSwtichMode());
                    cabinetChargerReport.setMaxChargeVoltageOfStage1(dbCabinetCharger.getMaxChargeVoltageOfStage1());
                    cabinetChargerReport.setChargeCurrentOfStage1(dbCabinetCharger.getChargeCurrentOfStage1());
                    cabinetChargerReport.setMaxChargeVoltageOfStage2(dbCabinetCharger.getMaxChargeVoltageOfStage2());
                    cabinetChargerReport.setChargeCurrentOfStage2(dbCabinetCharger.getChargeCurrentOfStage2());
                    cabinetChargerReport.setSlopeVoltage(dbCabinetCharger.getSlopeVoltage());
                    cabinetChargerReport.setChargeCurrentOfStage3(dbCabinetCharger.getChargeCurrentOfStage3());
                    cabinetChargerReport.setSlopeVoltage(dbCabinetCharger.getSlopeVoltage());
                    cabinetChargerReport.setFullVoltage(dbCabinetCharger.getFullVoltage());
                    cabinetChargerReport.setSlopeCurrent(dbCabinetCharger.getSlopeCurrent());
                    cabinetChargerReport.setMinCurrentOfStage4(dbCabinetCharger.getMinCurrentOfStage4());
                    cabinetChargerReport.setLowTemperatureMode(dbCabinetCharger.getLowTemperatureMode());
                    cabinetChargerReport.setBoxForbidden(e.boxForbidden);
                    cabinetChargerReport.setOther(dbCabinetCharger.getOther());
                    insertCabinetChargerReport(cabinetChargerReport);
//                }
            }
        }
    }


    /**
     * 充电器日志插入
     */
    public void insertCabinetChargerReport(CabinetChargerReport cabinetChargerReport) {
        try{
            String tableName = String.format("%s%s", CabinetChargerReport.CABINET_CHARGER_REPORT_TABLE_NAME, cabinetChargerReport.getSuffix());
            boolean result = findCabinetChargerReportTable(tableName);
            if (!result) {
                cabinetChargerReportMapper.createTable(tableName);
            }
            cabinetChargerReport.setCreateTime(new Date());
            cabinetChargerReportMapper.insert(cabinetChargerReport);
            String reportDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
            if (cabinetChargerReportDateMapper.update(reportDate, cabinetChargerReport.getCabinetId(), cabinetChargerReport.getBoxNum()) == 0) {
                cabinetChargerReportDateMapper.create(reportDate, cabinetChargerReport.getCabinetId(), cabinetChargerReport.getBoxNum());
            }
        }catch (Exception e){
            log.error(String.format("换电柜充电器日志插入 保存 error=%s", e.getMessage()));
        }
    }

}
