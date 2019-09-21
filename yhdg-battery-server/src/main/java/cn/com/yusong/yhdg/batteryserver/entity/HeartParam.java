package cn.com.yusong.yhdg.batteryserver.entity;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.utils.GPSUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电池心跳版本
 */
public class HeartParam {
    static final Pattern LNG_PATTERN = Pattern.compile("(\\d{3})(\\d{2}.\\d{4})");
    static final Pattern LAT_PATTERN = Pattern.compile("(\\d{2})(\\d{2}.\\d{4})");

    public String jsonData;//心跳json数据

    public String batteryId;
    public Integer heartType;

    //请求参数
    public String IMEI;//电池IMEI
    public String CCID;//Sim卡CCID号
    public String BmsVer;//BMS版本
    public Integer LocType;//位置类型（1.GPS 2.CELL）
    public Integer Sig;//信号强度（1-31）
    public Integer SigType;//信号类型
    public String LBS;//基站信息“mcc,mnc,lac,cellid”
    public String Lng;//经度
    public String Lat;//纬度
    public Integer Vol;//总电压,mV
    public Integer Cur;//电流,单位mA,放电负，充电正
    public Integer Cells;//电芯串数
    public String VolList;//单体电压列表，单位mV
    public Integer Bal;//电芯的均衡状态0关闭,1开启
    public String Temp;//温度
    public Integer ResCap;//剩余容量,单位mAH
    public Integer Soc;//Soc电量，单位0.1%
    public Integer Circle;//循环次数
    public Integer MOS;//充放电MOS状态
    public Integer Fault;//故障类型
    public Integer Heart;//心跳间隔
    public Integer Motion;//运动值(0:静止,1-10:运动等级)
    public Integer Uncap;//开盖状态(0:未开盖 1:开盖)
    public Integer Mode;//工作状态：0正常，1：待机
    public String Protect;//保护次数列表
    Integer BatteryLease;//电池租期



    //long heart
    public String CellModel;//电池型号
    public String CellMFR;//电芯厂家
    public String BattMFR;//电池厂家
    public String MFD;//生产日期
    public String BmsModel;//BMS型号
    public Integer Mat;//材质（1.三元,2.磷酸铁锂,3.铅酸）
    public Integer BattType;//电池类型
    public Integer NomCap;//标准容量,单位mAH
    public Integer CircleCap;//循环容量,单位mAH
    public Integer CellFullVol;//单体充满电压，单位mV
    public Integer CellCutVol;//单体截止电压，单位mV
    public Integer SelfDsgRate;//自放电率，单位0.1%
    public String OCVTable;//开路电压值,单位mV

    public Integer COVTrip;//单体过压保护阈值，单位mV
    public Integer COVResm;//单体过压恢复阈值，单位mV
    public Integer COVDelay;//单体过压延时，单位S
    public Integer CUVTrip;//单体欠压保护阈值，单位mV
    public Integer CUVResm;//单体欠压恢复阈值，单位mV
    public Integer CUVDelay;//单体欠压延时，单位S
    public Integer POVTrip;//整体过压保护阈值，单位mV
    public Integer POVResm;//整体过压恢复阈值，单位mV
    public Integer POVDelay;//整体过压延时，单位S
    public Integer PUVTrip;//整体欠压保护阈值，单位mV
    public Integer PUVResm;//整体欠压恢复阈值，单位mV
    public Integer PUVDelay;//整体欠压延时，单位S
    public Integer ChgOTTrip;//充电高温保护阈值，单位0.1K
    public Integer ChgOTResm;//充电高温恢复阈值，单位0.1K
    public Integer ChgOTDelay;//充电高温延时，单位S
    public Integer ChgUTTrip;//充电低温保护阈值，单位0.1K
    public Integer ChgUTResm;//充电低温恢复阈值，单位0.1K
    public Integer ChgUTDelay;//充电低温延时，单位S
    public Integer DsgOTTrip;//放电高温保护阈值，单位0.1K
    public Integer DsgOTResm;//放电高温恢复阈值，单位0.1K
    public Integer DsgOTDelay;//放电高温延时，单位S
    public Integer DsgUTTrip;//放电低温保护阈值，单位0.1K
    public Integer DsgUTResm;//放电低温恢复阈值，单位0.1K
    public Integer DsgUTDelay;//放电低温延时，单位S
    public Integer ChgOCTrip;//充电过流保护阈值，单位mA
    public Integer ChgOCDelay;//充电过流延时，单位S
    public Integer ChgOCRels;//充电过流释放时间，单位S
    public Integer DsgOCTrip;//放电过流保护阈值，单位mA
    public Integer DsgOCDelay;//放电过流延时，单位S
    public Integer DsgOCRels;//放电过流释放时间，单位S
    public Integer RSNS;//1：硬件过流、短路保护阈值翻倍
    public Integer HardOCTrip;//硬件过流保护阈值
    public Integer HardOCDelay;//硬件过流保护延时
    public Integer SCTrip;//短路保护阈值
    public Integer SCDelay;//短路保护延时
    public Integer HardOVTrip;//硬件单体过压保护阈值,单位mV
    public Integer HardOVDelay;//硬件单体过压保护延时
    public Integer HardUVTrip;//硬件单体欠压保护阈值,单位mV
    public Integer HardUVDelay;//硬件单体欠压保护延时
    public Integer SDRels;//短路释放时间
    public Integer Function;//功能配置
    public Integer NTCConfig;//NTC配置
    public Integer SampleR;//电流采样电阻值，单位0.1R
    Integer Motionless; //静止状态时心跳间隔
    Integer Standby;        //存储状态时心跳间隔
    Integer Dormancy;        //休眠状态时心跳间隔
    public Long MaxCap;//实际容量,单位mAH

    public void setProperties(BatteryParameter batteryReport){
        DecimalFormat df=new DecimalFormat("0.0");

        batteryReport.setJsonData(jsonData);
        batteryReport.setHeartType(heartType);
        batteryReport.setCode(IMEI);
        batteryReport.setSimCode(CCID);
        batteryReport.setVersion(BmsVer);
        batteryReport.setLocType(LocType);
        batteryReport.setCurrentSignal(Sig);
        batteryReport.setSignalType(SigType);
        batteryReport.setLbs(LBS);

        if(StringUtils.isNotEmpty(Lng) && StringUtils.isNotEmpty(Lat)){
            //电池经纬度
            Double lng = 0.0, lat = 0.0;
            //计算经纬度
            if(LocType != null && LocType == 1){
                Matcher lngMatcher = LNG_PATTERN.matcher(Lng);
                if (lngMatcher.find()) {
                    lng = Double.parseDouble(lngMatcher.group(1)) + Double.parseDouble(lngMatcher.group(2)) / 60;
                }
                Matcher latMatcher = LAT_PATTERN.matcher(Lat);
                if (latMatcher.find()) {
                    lat = Double.parseDouble(latMatcher.group(1)) + Double.parseDouble(latMatcher.group(2)) / 60;
                }
                double[] d = GPSUtil.gps84_To_bd09(lat, lng);
                lng = d[1];
                lat = d[0];
            }else{
                lng = Double.parseDouble(Lng);
                lat = Double.parseDouble(Lat);
            }
            batteryReport.setLng(lng);
            batteryReport.setLat(lat);
        }


        batteryReport.setVoltage(Vol);
        batteryReport.setElectricity(Cur);
        batteryReport.setSerials(Cells);
        batteryReport.setSingleVoltage(VolList);
        batteryReport.setBalance(Bal);
        String temp = null;
        if(StringUtils.isNotEmpty(Temp)){

            String [] strs = Temp.split(",");
            for(String v : strs){
                v = df.format((float)(Integer.parseInt(v) - 2731) / 10);
                if(temp == null){
                    temp = v;
                }else{
                    temp += "," + v;
                }
            }
        }
        batteryReport.setTemp(temp);
        batteryReport.setCurrentCapacity(ResCap);
        if(Soc != null){
            batteryReport.setVolume(Soc/10);
        }
        batteryReport.setCircle(Circle);
        batteryReport.setMos(MOS);
        batteryReport.setFault(Fault);
        batteryReport.setHeartInterval(Heart);
        batteryReport.setIsMotion(Motion);
        batteryReport.setUncapState(Uncap);
        batteryReport.setMode(Mode);
        batteryReport.setProtect(Protect);
        batteryReport.setBatteryLease(BatteryLease);

        //长心跳
        batteryReport.setCellModel(CellModel);
        batteryReport.setCellMfr(CellMFR);
        batteryReport.setBattMfr(BattMFR);
        batteryReport.setMfd(MFD);
        batteryReport.setBmsModel(BmsModel);
        batteryReport.setMaterial(Mat);
        batteryReport.setBattType(BattType);
        batteryReport.setNominalCapacity(NomCap);
        batteryReport.setCircleCapacity(CircleCap);
        batteryReport.setCellFullVol(CellFullVol);
        batteryReport.setCellCutVol(CellCutVol);
        batteryReport.setSelfDsgRate(SelfDsgRate);
        batteryReport.setOcvTable(OCVTable);
        batteryReport.setCellOvTrip(COVTrip);
        batteryReport.setCellOvResume(COVResm);
        batteryReport.setCellOvDelay(COVDelay);
        batteryReport.setCellUvTrip(CUVTrip);
        batteryReport.setCellUvResume(CUVResm);
        batteryReport.setCellUvDelay(CUVDelay);
        batteryReport.setPackOvTrip(POVTrip);
        batteryReport.setPackOvResume(POVResm);
        batteryReport.setPackOvDelay(POVDelay);
        batteryReport.setPackUvTrip(PUVTrip);
        batteryReport.setPackUvResume(PUVResm);
        batteryReport.setPackUvDelay(PUVDelay);
        batteryReport.setChgOtTrip(ChgOTTrip);
        batteryReport.setChgOtResume(ChgOTResm);

        batteryReport.setChgOtDelay(ChgOTDelay);
        batteryReport.setChgUtTrip(ChgUTTrip);
        batteryReport.setChgUtResume(ChgUTResm);
        batteryReport.setChgUtDelay(ChgUTDelay);
        batteryReport.setDsgOtTrip(DsgOTTrip);
        batteryReport.setDsgOtResume(DsgOTResm);
        batteryReport.setDsgOtDelay(DsgOTDelay);
        batteryReport.setDsgUtTrip(DsgUTTrip);
        batteryReport.setDsgUtResume(DsgUTResm);
        batteryReport.setDsgUtDelay(DsgUTDelay);
        batteryReport.setChgOcTrip(ChgOCTrip);
        batteryReport.setChgOcDelay(ChgOCDelay);
        batteryReport.setChgOcRelease(ChgOCRels);
        batteryReport.setDsgOcTrip(DsgOCTrip);
        batteryReport.setDsgOcDelay(DsgOCDelay);
        batteryReport.setDsgOcRelease(DsgOCRels);
        batteryReport.setRsns(RSNS);
        batteryReport.setHardOcTrip(HardOCTrip);
        batteryReport.setHardOcDelay(HardOCDelay);
        batteryReport.setScTrip(SCTrip);
        batteryReport.setScDelay(SCDelay);
        batteryReport.setHardOvTrip(HardOVTrip);
        batteryReport.setHardOvDelay(HardOVDelay);
        batteryReport.setHardUvTrip(HardUVTrip);
        batteryReport.setHardUvDelay(HardUVDelay);
        batteryReport.setSdRelease(SDRels);
        batteryReport.setFunction(Function);
        batteryReport.setNtcConfig(NTCConfig);
        batteryReport.setSampleR(SampleR);
        batteryReport.setMotionless(Standby);
        batteryReport.setStandby(Standby);
        batteryReport.setDormancy(Dormancy);

        if(MaxCap != null){
            if(MaxCap > 100000000){
                MaxCap = MaxCap/1000;
            }
            batteryReport.setTotalCapacity(MaxCap.intValue());
        }

    }
}
