package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

@Setter
@Getter
public class BatteryParameter extends PageEntity {

    public enum BatteryParameterColumn {
        CODE(1, "电池编号"),
        VERSION(2,"BMS版本"),
        LOCTYPENAME(3,"位置类型"),
        CURRENTSIGNAL(4, "当前信号"),
        SIGNALTYPENAME(5,"信号类型"),
        LNGLAT(6, "经度/纬度"),
        VOLTAGE(7, "总电压"),
        ELECTRICITY(8, "电流"),
        SERIALS(9, "电芯串数"),
        SINGLEVOLTAGE(10,"单体电压"),
        BALANCENAME(11,"电芯均衡状态"),
        TEMP(12, "温度"),
        CURRENTCAPACITY(13, "当前容量"),
        VOLUME(14,"当前电量"),
        CIRCLE(15,"循环次数"),
        MOSNAME(16,"MOS"),
        FAULTNAME(17,"故障类型"),
        HEARTINTERVAL(18,"心跳间隔"),
        ISMOTION(19,"运动值"),
        UNCAPSTATE(20,"开盖状态"),
        ENERGYSTATE(21,"工作状态"),
        PROTECTNAME(22,"保护状态"),
        HEARTTYPENAME(23, "心跳类型"),
        CREATETIME(24, "上报时间");

        private final int value;
        private final String name;

        BatteryParameterColumn(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BatteryParameterColumn e : BatteryParameterColumn.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum HeartType {
        NORMAL_HEART(1, "短心跳"),
        LONG_HEART(2, "长心跳"),
        CABINET_HEART(3, "柜子心跳"),
        OTHER_HEART(4, "第三方电池心跳"),;

        private final int value;
        private final String name;

        HeartType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HeartType e : HeartType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum LocType {
        GPS(1, "GPS"),
        CELL(2, "CELL");

        private final int value;
        private final String name;

        LocType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LocType e : LocType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum BattType {
        V_36(0, "36V"),
        V_48(1, "48V"),
        V_60(2, "60V"),
        V_72(3, "72V");

        private final int value;
        private final String name;

        BattType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BattType e : BattType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Fault {
        CODE_1(1, "单体过压发生保护"),
        CODE_2(2, "单体欠压发生保护"),
        CODE_3(3, "整组过压发生保护"),
        CODE_4(4, "整组欠压发生保护"),
        CODE_5(5, "充电过温发生保护"),
        CODE_6(6, "充电低温发生保护"),
        CODE_7(7, "放电过温发生保护"),
        CODE_8(8, "放电低温发生保护"),
        CODE_9(9, "充电过流发生保护"),
        CODE_10(10, "放电过流发生保护"),
        CODE_11(11, "短路发生保护"),
        CODE_12(12, "前端检测IC错误"),
        CODE_13(13, "保护板充电MOS锁定"),
        CODE_14(14, "保护板放电MOS锁定"),
        CODE_15(15, "充电MOS异常"),
        CODE_16(16, "放电MOS异常");

        private final int value;
        private final String name;

        Fault(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Fault e : Fault.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Protect {
        CODE_1(1, "短路"),
        CODE_2(2, "充电过流"),
        CODE_3(3, "放电过流"),
        CODE_4(4, "单体过压"),
        CODE_5(5, "单体欠压"),
        CODE_6(6, "充电高温"),
        CODE_7(7, "充电低温"),
        CODE_8(8, "放电高温"),
        CODE_9(9, "放电低温"),
        CODE_10(10, "整体过压"),
        CODE_11(11, "整体欠压"),;

        private final int value;
        private final String name;

        Protect(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Protect e : Protect.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Mode {
        CODE_0(0, "运动状态"),
        CODE_1(1, "静止状态"),
        CODE_2(2, "存储状态"),
        CODE_3(3, "休眠状态");

        private final int value;
        private final String name;

        Mode(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Mode e : Mode.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum LockWitch {
        NO(1, "未锁定"),
        LOCK_CLOSE(2, "强制关锁"),
        LOCK_OPEN(3, "强制开锁");

        private final int value;
        private final String name;

        LockWitch(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LockWitch e : LockWitch.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum HardOcTrip {
        CODE_0(0, "8mV/16mV"),
        CODE_1(1, "11mV/22mV"),
        CODE_2(2, "14mV/28mV"),
        CODE_3(3, "17mV/34mV"),
        CODE_4(4, "19mV/38mV"),
        CODE_5(5, "22mV/44mV"),
        CODE_6(6, "25mV/50mV"),
        CODE_7(7, "28mV/56mV"),
        CODE_8(8, "31mV/62mV"),
        CODE_9(9, "33mV/66mV"),
        CODE_10(10, "36mV/72mV"),
        CODE_11(11, "39mV/78mV"),
        CODE_12(12, "42mV/84mV"),
        CODE_13(13, "44mV/88mV"),
        CODE_14(14, "47mV/94mV"),
        CODE_15(15, "50mV/100mV"),;


        private final int value;
        private final String name;

        HardOcTrip(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HardOcTrip e : HardOcTrip.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public static String getName(int value, int type) {
            if(map.get(value) != null){
                String [] names = map.get(value).split("/");
                if(type == 1){
                    return names[1];
                }else{
                    return names[0];
                }
            }
            return "";
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum HardOcDelay {
        CODE_0(0, "8mS"),
        CODE_1(1, "20mS"),
        CODE_2(2, "40mS"),
        CODE_3(3, "80mS"),
        CODE_4(4, "160mS"),
        CODE_5(5, "320mS"),
        CODE_6(6, "640mS"),
        CODE_7(7, "1280mS");


        private final int value;
        private final String name;

        HardOcDelay(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HardOcDelay e : HardOcDelay.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum SCTrip {
        CODE_0(0, "22mV/44mV"),
        CODE_1(1, "33mV/66mV"),
        CODE_2(2, "44mV/88mV"),
        CODE_3(3, "56mV/112mV"),
        CODE_4(4, "67mV/134mV"),
        CODE_5(5, "78mV/156mV"),
        CODE_6(6, "89mV/178mV"),
        CODE_7(7, "100mV/200mV"),;

        private final int value;
        private final String name;

        SCTrip(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (SCTrip e : SCTrip.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public static String getName(int value, int type) {
            if(map.get(value) != null){
                String [] names = map.get(value).split("/");
                if(type == 1){
                    return names[1];
                }else{
                    return names[0];
                }
            }
            return "";
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum SCDelay {
        CODE_0(0, "70uS"),
        CODE_1(1, "100uS"),
        CODE_2(2, "200uS"),
        CODE_3(3, "400uS"),;


        private final int value;
        private final String name;

        SCDelay(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (SCDelay e : SCDelay.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum HardOvDelay {
        CODE_0(0, "1S"),
        CODE_1(1, "2S"),
        CODE_2(2, "4S"),
        CODE_3(3, "8S"),;


        private final int value;
        private final String name;

        HardOvDelay(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HardOvDelay e : HardOvDelay.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum HardUvDelay {
        CODE_0(0, "1S"),
        CODE_1(1, "4S"),
        CODE_2(2, "8S"),
        CODE_3(3, "16S"),;


        private final int value;
        private final String name;

        HardUvDelay(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HardUvDelay e : HardUvDelay.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Function {
        CODE_1(1, "开关功能"),
        CODE_2(2, "负载检测"),
        CODE_3(3, "均衡功能"),
        CODE_4(4, "充电均衡"),
        CODE_5(5, "LED功能"),
        CODE_6(6, "5个LED"),
        CODE_7(7, "预留"),;

        private final int value;
        private final String name;

        Function(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Function e : Function.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum NtcConfig {
        CODE_1(1, "NTC1"),
        CODE_2(2, "NTC2"),
        CODE_3(3, "NTC3"),
        CODE_4(4, "NTC4"),
        CODE_5(5, "NTC5"),
        CODE_6(6, "NTC6"),
        CODE_7(7, "NTC7"),
        CODE_8(7, "NTC8"),;

        private final int value;
        private final String name;

        NtcConfig(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (NtcConfig e : NtcConfig.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Parameter {
        IMEI( "code","IMEI","电池IMEI"),
        CCID( "simCode","CCID","Sim卡CCID号"),
        CellModel( "cellModel","CellModel","电池型号"),
        CellMFR( "cellMfr","CellMFR","电芯厂家"),
        BattMFR( "battMfr","BattMFR","电池厂家"),
        MFD( "mfd","MFD","生产日期"),
        BmsVer( "version","BmsVer","BMS版本"),
        BmsModel( "bmsModel","BmsModel","BMS型号"),
        Mat( "material","Mat","材质"),
        BattType( "battType","BattType","电池类型"),
        NomCap( "nominalCapacity","NomCap","标准容量"),
        CircleCap( "circleCapacity","CircleCap","循环容量"),
        CellFullVol( "cellFullVol","CellFullVol","单体充满电压"),
        CellCutVol( "cellCutVol","CellCutVol","单体截止电压"),
        SelfDsgRate( "selfDsgRate","SelfDsgRate","自放电率"),
        OCVTable( "ocvTable","OCVTable","开路电压值"),
        COVTrip( "cellOvTrip","COVTrip","单体过压保护阈值"),
        COVResm( "cellOvResume","COVResm","单体过压恢复阈值"),
        COVDelay( "cellOvDelay","COVDelay","单体过压延时"),
        CUVTrip( "cellUvTrip","CUVTrip","单体欠压保护阈值"),
        CUVResm( "cellUvResume","CUVResm","单体欠压保护阈值"),
        CUVDelay( "cellUvDelay","CUVDelay","单体欠压延时"),
        POVTrip( "packOvTrip","POVTrip","整体过压保护阈值"),
        POVResm( "packOvResume","POVResm","整体过压恢复阈值"),
        POVDelay( "packOvDelay","POVDelay","整体过压延时"),
        PUVTrip( "packUvTrip","PUVTrip","整体欠压保护阈值"),
        PUVResm( "packUvResume","PUVResm","整体欠压恢复阈值"),
        PUVDelay( "packUvDelay","PUVDelay","整体欠压延时"),
        ChgOTTrip( "chgOtTrip","ChgOTTrip","充电高温保护阈值"),
        ChgOTResm( "chgOtResume","ChgOTResm","充电高温恢复阈值"),
        ChgOTDelay( "chgOtDelay","ChgOTDelay","充电高温延时"),
        ChgUTTrip( "chgUtTrip","ChgUTTrip","电低温保护阈值"),
        ChgUTResm( "chgUtResume","ChgUTResm","充电低温恢复阈值"),
        ChgUTDelay( "chgUtDelay","ChgUTDelay","充电低温延时"),
        DsgOTTrip( "dsgOtTrip","DsgOTTrip","放电高温保护阈值"),
        DsgOTResm( "dsgOtResume","DsgOTResm","放电高温恢复阈值"),
        DsgOTDelay( "dsgOtDelay","DsgOTDelay","放电高温延时"),
        DsgUTTrip( "dsgUtTrip","DsgUTTrip","放电低温保护阈值"),
        DsgUTResm( "dsgUtResume","DsgUTResm","放电低温恢复阈值"),
        DsgUTDelay( "dsgUtDelay","DsgUTDelay","放电低温延时"),
        ChgOCTrip( "chgOcTrip","ChgOCTrip","充电过流保护阈值"),
        ChgOCDelay( "chgOcDelay","ChgOCDelay","充电过流延时"),
        ChgOCRels( "chgOcRelease","ChgOCRels","充电过流释放时间"),
        DsgOCTrip( "dsgOcTrip","DsgOCTrip","放电过流保护阈值"),
        DsgOCDelay( "dsgOcDelay","DsgOCDelay","放电过流延时"),
        DsgOCRels( "dsgOcRelease","DsgOCRels","放电过流释放时间"),
        RSNS( "rsns","RSNS","硬件过流、短路保护阈值翻倍"),
        HardOCTrip( "hardOcTrip","HardOCTrip","硬件过流保护阈值"),
        HardOCDelay( "hardOcDelay","HardOCDelay","硬件过流保护延时"),
        SCTrip( "scTrip","SCTrip","短路保护阈值"),
        SCDelay( "scDelay","SCDelay","短路保护延时"),
        HardOVTrip( "hardOvTrip","HardOVTrip","硬件单体过压保护阈值"),
        HardOVDelay( "hardOvDelay","HardOVDelay","硬件单体过压保护延时"),
        HardUVTrip( "hardUvTrip","HardUVTrip","硬件单体欠压保护阈值"),
        HardUVDelay( "hardUvDelay","HardUVDelay","硬件单体欠压保护延时"),
        SDRels( "sdRelease","SDRels","短路释放时间"),
        Function( "function","Function","功能配置"),
        NTCConfig( "ntcConfig","NTCConfig","NTC配置"),
        Cells( "serials","Cells","电芯串数"),
        SampleR( "sampleR","SampleR","电流采样电阻值"),
        Heart( "heartInterval","Heart","运动状态时心跳间隔"),
        Motionless( "motionless","Motionless","静止状态时心跳间隔"),
        Standby( "standby","Standby","存储状态时心跳间隔"),
        Dormancy( "dormancy","Dormancy","休眠状态时心跳间隔"),
        MaxCap( "totalCapacity","MaxCap","实际容量"),
        ;

        private final String value;
        private final String parameter;
        private final String name;

        Parameter(String value, String parameter, String name) {
            this.value = value;
            this.parameter = parameter;
            this.name = name;
        }

        private static Map<String, String> parameterMap = new HashMap<String, String>();

        static {
            for (Parameter e : Parameter.values()) {
                parameterMap.put(e.getValue(), e.getParameter());
            }
        }

        public static String getParameter(String value) {
            return parameterMap.get(value);
        }

        private static Map<String, String> map = new HashMap<String, String>();

        static {
            for (Parameter e : Parameter.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(String value) {
            return map.get(value);
        }

        public String getValue() {
            return value;
        }

        public String getParameter() {
            return parameter;
        }

        public String getName() {
            return name;
        }
    }


    public enum LinkStatus {
        CODE_1(1, "检测到网络模块"),
        CODE_2(2, "SIM卡正常"),
        CODE_3(3, "网络注册成功"),
        CODE_4(4, "GPRS激活"),
        CODE_5(5, "服务器连接成功"),
        CODE_6(6, "数据接收成功"),
        ;


        private final int value;
        private final String name;

        LinkStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LinkStatus e : LinkStatus.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }


    String id;
    Integer type;
    String code;//电池IMEI
    Integer heartType;
    String simCode;//Sim卡CCID号
    String version;//BMS版本
    Integer locType;            //位置类型（1.GPS 2.CELL）
    Integer currentSignal;      //当前信号*/
    Integer signalType;         //信号类型 0:2g 1:3G 2:4G 3:NBIot
    String lbs;//基站信息“mcc,mnc,lac,cellid”
    Double lng;                 //经度*/
    Double lat;                 //纬度*/
    Integer voltage;            //电压
    Integer electricity;        //电流*/
    Integer serials;//电芯串数*/
    String singleVoltage;       //电池每串单体电压*/
    Integer balance;            //电芯的均衡状态0关闭,1开启*/
    String temp;                //温度*/
    Integer currentCapacity;    //当前容量
    Integer volume;
    Integer circle;           //循环次数
    Integer mos;                //bit0表示充电，bit1表示放电，0表示MOS关闭，1表示打开
    Integer fault;       //告警状态 0为未保护，1发生保护
    Integer heartInterval;      //心跳间隔（默认60秒）
    Integer isMotion;           //运动值(0:静止,1-10:运动等级)
    Integer uncapState;         //开盖状态 0:未开盖 1:开盖
    Integer mode;        //工作状态：0正常，1：待机
    String protect;//保护次数b
    Integer batteryLease;//电池租期
    Integer linkStatus;//网络连接状态

    //长心跳
    String cellModel;//电池型号
    String cellMfr;//电芯厂家
    String battMfr;//电池厂家
    String mfd;//生产日期
    String bmsModel;               //BMS型号
    Integer material;           //材质（1.三元,2.磷酸铁锂,3.铅酸）*/
    Integer battType;//电池类型*/
    Integer nominalCapacity;    //标准容量*/
    Integer circleCapacity;//循环容量,单位mAH
    Integer cellFullVol;//单体充满电压，单位mV
    Integer cellCutVol;//单体截止电压，单位mV
    Integer selfDsgRate;//自放电率，单位0.1%
    String ocvTable;//开路电压值,单位mV
    Integer cellOvTrip;//单体过压保护阈值，单位mV
    Integer cellOvResume;//单体过压恢复阈值，单位mV
    Integer cellOvDelay;//单体过压延时，单位S
    Integer cellUvTrip;//单体欠压保护阈值，单位mV
    Integer cellUvResume;//单体欠压恢复阈值，单位mV
    Integer cellUvDelay;//单体欠压延时，单位S
    Integer packOvTrip;//整体过压保护阈值，单位mV
    Integer packOvResume;//整体过压恢复阈值，单位mV
    Integer packOvDelay;//整体过压延时，单位S
    Integer packUvTrip;//整体欠压保护阈值，单位mV
    Integer packUvResume;//整体欠压恢复阈值，单位mV
    Integer packUvDelay;//整体欠压延时，单位S
    Integer chgOtTrip;//充电高温保护阈值，单位0.1K
    Integer chgOtResume;//充电高温恢复阈值，单位0
    Integer chgOtDelay;//充电高温延时，单位S
    Integer chgUtTrip;//充电低温保护阈值，单位0.1K
    Integer chgUtResume;//充电低温恢复阈值，单位0.1K
    Integer chgUtDelay;//充电低温延时，单位S
    Integer dsgOtTrip;//放电高温保护阈值，单位0.1K
    Integer dsgOtResume;//放电高温恢复阈值，单位0.1K
    Integer dsgOtDelay;//放电高温延时，单位S
    Integer dsgUtTrip;//放电低温保护阈值，单位0.1K
    Integer dsgUtResume;//放电低温恢复阈值，单位0.1K
    Integer dsgUtDelay;//放电低温延时，单位S
    Integer chgOcTrip;//充电过流保护阈值，单位mA
    Integer chgOcDelay;//充电过流延时，单位S
    Integer chgOcRelease;//充电过流释放时间，单位S
    Integer dsgOcTrip;//放电过流保护阈值，单位mA
    Integer dsgOcDelay;//放电过流延时，单位S
    Integer dsgOcRelease;//放电过流释放时间，单位S
    Integer rsns;//1：硬件过流、短路保护阈值翻倍
    Integer hardOcTrip;//硬件过流保护阈值
    Integer hardOcDelay;//硬件过流保护延时
    Integer scTrip;//短路保护阈值
    Integer scDelay;//短路保护延时
    Integer hardOvTrip;//硬件单体过压保护阈值,单位mV
    Integer hardOvDelay;//硬件单体过压保护延时
    Integer hardUvTrip;//硬件单体欠压保护阈值,单位mV
    Integer hardUvDelay;//硬件单体欠压保护延时
    Integer sdRelease;//短路释放时间
    Integer function;//功能配置
    Integer ntcConfig;//NTC配置
    Integer sampleR;//电流采样电阻值，单位0.1R
    Integer motionless; //静止状态时心跳间隔
    Integer standby;        //存储状态时心跳间隔
    Integer dormancy;        //休眠状态时心跳间隔

    String cabinetId;//当前所属站点
    String boxNum;//当前所在格口

    Integer totalCapacity;      //总容量*/

    Integer distance;

    Integer lockWitch;

    Date shortReportTime;
    Date longReportTime;
    Date createTime;

    Integer upBms;

    String jsonData;/*心跳json数据*/


    @Transient
    Date queryLogTime;
    String suffix;
    Double bdLng;
    Double bdLat;

    Integer rentPeriodType;/*租金周期 月*/
    Integer rentPeriodMoney; /*租金周期 每个周期收多少钱*/
    Date rentExpireTime;/*租金周期 过期时间*/
    String idsData;
    String cabinetName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getShortReportTime() {
        return shortReportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getLongReportTime() {
        return longReportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getQueryLogTime() {
        return queryLogTime;
    }

    public String getHeartTypeName() {
        if(heartType != null) {
            return HeartType.getName(heartType);
        }
        return "";
    }

    public String getLocTypeName() {
        if(locType != null) {
            return LocType.getName(locType);
        }
        return "";
    }

    public String getSignalTypeName() {
        if(signalType != null) {
            return Battery.SignalType.getName(signalType);
        }
        return "";
    }

    public String getBattTypeName() {
        if(battType != null) {
            return BattType.getName(battType);
        }
        return "";
    }

    public String getModeName() {
        if(mode != null) {
            return Mode.getName(mode);
        }
        return "";
    }

    public String getMaterialName() {
        if(material != null) {
            return Battery.Material.getName(material);
        }
        return "";
    }

    public String getMosName() {
        String name = "";
        if (null != mos) {
            String[] names = new String[]{"充电", "放电"};

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(mos)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                name += names[i];
                if ('1' == str[i]) {
                    name += "开";
                } else {
                    name += "关";
                }
            }
        }
        return name;
    }

    public String getFaultName() {
        String name = "";
        if (null != fault) {
            Fault[] names = Fault.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(fault)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if ('1' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i].name;
                }
            }
        }
        return name;
    }

    public String getBalanceName() {
        String name = "";
        if (balance != null && serials != null) {
            String[] names = new String[serials];
            for(int i=0;i<serials;i++){
                names[i] =  String.valueOf(i+1);
            }
            char[] str = new StringBuilder(String.format("%0" + serials + "d", Long.parseLong(Integer.toBinaryString(balance)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                name += names[i];
                if ('1' == str[i]) {
                    name += "开启";
                } else {
                    name += "关闭";
                }
            }
        }
        return name;
    }

    public String getProtectName() {
        String name = "";
        if (StringUtils.isNotEmpty(protect)) {
            Protect[] names = Protect.values();
            String [] protectStrs = protect.split(",");
            for (int i = 0; i < protectStrs.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                name += names[i].name + Integer.parseInt(protectStrs[i]) + "次";
            }
        }
        return name;
    }

    public String getHardOcTripName() {
        if(rsns != null && hardOcTrip != null) {
            return HardOcTrip.getName(hardOcTrip, rsns);
        }
        return "";
    }

    public String getHardOcDelayName() {
        if(hardOcDelay != null) {
            return HardOcDelay.getName(hardOcDelay);
        }
        return "";
    }

    public String getSCTripName() {
        if(rsns != null && scTrip != null) {
            return SCTrip.getName(scTrip, rsns);
        }
        return "";
    }

    public String getSCDelayName() {
        if(scDelay != null) {
            return SCDelay.getName(scDelay);
        }
        return "";
    }

    public String getHardOvDelayName() {
        if(hardOvDelay != null) {
            return HardOvDelay.getName(hardOvDelay);
        }
        return "";
    }

    public String getHardUvDelayName() {
        if(hardUvDelay != null) {
            return HardUvDelay.getName(hardUvDelay);
        }
        return "";
    }

    public String getLinkStatusName() {
        if(linkStatus != null) {
            return LinkStatus.getName(linkStatus);
        }
        return "";
    }

    public String getFunctionName() {
        String name = "";
        if (null != function) {
            Function[] names = Function.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(function)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                if ('1' == str[i]) {
                    name += names[i].name +"开";
                }else{
                    name += names[i].name +"关";
                }
            }
        }
        return name;
    }

    public List getFunctionList() {
        List<Map>list = new ArrayList();
        if(function != null){
            for (int i = 0; i < 8; i++) {
                Map map = new HashMap();
                map.put("name",Function.getName(i+1));
                map.put("value",function >> i & 0x01);
                list.add(map);
            }
        }
        return list;
    }

    public List getNtcList() {
        List<Map>list = new ArrayList();
        if(ntcConfig != null){
            for (int i = 0; i < 8; i++) {
                Map map = new HashMap();
                map.put("name",NtcConfig.getName(i+1));
                map.put("value",ntcConfig >> i & 0x01);
                list.add(map);
            }
        }
        return list;
    }

    public List getOCVTableList() {
        String[] names = new String[]{"0%","5%","10%","15%","20%","25%","30%","35%","40%","45%","50%","55%","60%","65%","70%","75%","80%","85%","90%","95%","100%"};
        List<Map>list = new ArrayList();
        if(function != null){
            for (int i = 0; i < names.length ; i++) {
                Map map = new HashMap();
                map.put("name",names[i]);
                map.put("value", null);
                if(ocvTable != null){
                    String [] ocvTableStrs = ocvTable.split(",");
                    if(ocvTableStrs.length > i){
                        map.put("value", ocvTableStrs[i]);
                    }
                }
                list.add(map);
            }
        }
        return list;
    }

    public String getNtcConfigName() {
        String name = "";
        if (null != ntcConfig) {
            NtcConfig[] names = NtcConfig.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(ntcConfig)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                if ('1' == str[i]) {
                    name += names[i].name +"开";
                }else{
                    name += names[i].name +"关";
                }
            }
        }
        return name;
    }




}
