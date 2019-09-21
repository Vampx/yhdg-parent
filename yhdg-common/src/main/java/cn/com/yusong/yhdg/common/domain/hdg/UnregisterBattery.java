package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class UnregisterBattery extends PageEntity {
    String id;
    String version;
    Integer voltage;            //电压
    Integer electricity;        //电流*/

    String cellMfr;//电芯厂家
    String cellModel;//电芯型号
    Integer nominalCap;//组包容量
    Double acResistance;//交流内阻
    Integer resilienceVol;/*回弹电压 单位mV*/
    Integer staticVol;/*静置电压 单位mV*/
    Integer circle;/*循环次数*/
    Integer cellCount;/*电芯串数*/
    Integer appearance;//外观指标1.R 2.M 3.Y
    String shellCode;           //外壳编号
    String code;
    String qrcode;//二维码

    Integer currentCapacity;    //当前容量
    Integer totalCapacity;      //总容量*/
    Integer useCount;           //使用次数
    Date produceDate;           //生产日期*/
    Integer protectState;       //保护状态 0为未保护，1发生保护
    Integer fetStatus;          //0充电  1放电  2不充电也不放电
    Integer strand;             //电池串数*/
    String temp;                //温度*/
    Double lng;                 //经度*/
    Double lat;                 //纬度*/
    Integer fet;                //bit0表示充电，bit1表示放电，0表示MOS关闭，1表示打开
    Integer positionState;      //0表示位置不移动 1表示位置移动中 2表示通电中
    Integer signalType;         //信号类型 0:2g 1:3G 2:4G 3:NBIot
    Integer currentSignal;      //当前信号*/
    String simCode;             //sim卡号*/
    Integer material;           //材质（1.三元,2.磷酸铁锂,3.铅酸）*/
    String singleVoltage;       //电池每串单体电压*/
    Integer heartInterval;      //心跳间隔（默认60秒）
    Integer stdInterval;        //待机时心跳间隔
    Integer volume;
    Integer isOnline;//上线状态
    Integer batteryType;//电池类型
    Date reportTime;            //上报时间
    Date createTime;

    @Transient
    String fetStatusName;//电池状态名称
    String protectStateName;   /*保护状态*/

    BatteryFormat batteryFormat1;//电池规格
    BatteryFormat batteryFormat2;//电池规格
    BatteryFormat batteryFormat3;//电池规格
    BatteryFormat batteryFormat4;//电池规格
    BatteryFormat batteryFormat5;//电池规格
    BatteryFormat batteryFormat6;//电池规格
    Integer expectCellCount;//电池规格电芯条数
    String cellBarcode;//电芯条码

    public enum Appearance {
        hard(3, "Y"),
        middle(2, "M"),
        soft(1, "R"),
        ;

        private final int value;
        private final String name;

        Appearance(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Battery.Appearance e : Battery.Appearance.values()) {
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

    public enum ProtectState {
        NOT_PROTECT(0, "未保护"),
        PROTECT(1, "发生保护");

        private final int value;
        private final String name;

        ProtectState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (UnregisterBatteryReportLog.ProtectState e : UnregisterBatteryReportLog.ProtectState.values()) {
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

    public enum BatteryType {
        UNREGISTER(1, "未注册"),
        EXCHANGE(2, "换电"),
        RENT(3, "租电"),
        ;

        private final int value;
        private final String name;

        BatteryType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (UnregisterBattery.BatteryType e : UnregisterBattery.BatteryType.values()) {
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


    public UnregisterBattery(String batteryId,
                             String version,
                             String voltage,
                             String electricity,
                             String currentCapacity,
                             String totalCapacity,
                             String useCount,
                             String produceDate,
                             String protectState,
                             String fetStatus,
                             String strand,
                             String temp,
                             Double lng,
                             Double lat,
                             String fet,
                             String positionState,
                             String currentSignal,
                             String simCode,
                             String singleVoltage,
                             String volume) {
        this.id = batteryId;
        this.reportTime = new Date();
        this.version = version;
        if (StringUtils.isNotEmpty(voltage)) {
            this.voltage = Integer.parseInt(voltage, 16);
        }
        if (StringUtils.isNotEmpty(electricity)) {
            this.electricity = Integer.parseInt(electricity, 16);
        }
        if (StringUtils.isNotEmpty(currentCapacity)) {
            this.currentCapacity = Integer.parseInt(currentCapacity, 16);
        }
        if (StringUtils.isNotEmpty(protectState)) {
            this.protectState = Integer.parseInt(protectState, 16);
        }
        if (StringUtils.isNotEmpty(fet)) {
            this.fet = Integer.parseInt(fet, 16);
        }
        if (StringUtils.isNotEmpty(strand)) {
            this.strand = Integer.parseInt(strand, 16);
        }
        if (StringUtils.isNotEmpty(temp)) {
            String[] list = StringUtils.split(temp, ", ");
            String t = null;
            for (String str : list) {
                if (t == null) {
                    t = String.valueOf((Integer.parseInt(str, 16) - 2731) / 10);
                } else {
                    t += "," + (Integer.parseInt(str, 16) - 2731) / 10;
                }
            }
            this.temp = t;
        }
        this.lng = lng;
        this.lat = lat;
        if (StringUtils.isNotEmpty(fetStatus)) {
            this.fetStatus = Integer.parseInt(fetStatus, 16);
        }
        if (StringUtils.isNotEmpty(currentSignal)) {
            this.currentSignal = Integer.parseInt(currentSignal);
        }

        this.simCode = simCode;


        if (StringUtils.isNotEmpty(totalCapacity)) {
            this.totalCapacity = Integer.parseInt(totalCapacity, 16);
        }
        if (StringUtils.isNotEmpty(positionState)) {
            this.positionState = Integer.parseInt(positionState, 16);
        }
        if (StringUtils.isNotEmpty(useCount)) {
            this.useCount = Integer.parseInt(useCount, 16);
        }
        if (StringUtils.isNotEmpty(volume)) {
            this.volume = Integer.parseInt(volume, 16);
        }
        if (StringUtils.isNotEmpty(produceDate)) {
            Integer produce = Integer.parseInt(produceDate, 16);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2000 + (produce >> 9));
            calendar.set(Calendar.MONTH, (produce >> 5) & 0x0f - 1);
            calendar.set(Calendar.DAY_OF_MONTH, produce & 0x1f);
            this.produceDate = calendar.getTime();
        }

        if (StringUtils.isNotEmpty(singleVoltage)) {
            String[] list = StringUtils.split(singleVoltage, ", ");
            String s = null;
            for (String str : list) {
                if (s == null) {
                    s = String.valueOf(Integer.parseInt(str, 16));
                } else {
                    s += "," + Integer.parseInt(str, 16);
                }
            }
            this.singleVoltage = s;
        }
    }

    public UnregisterBattery() {

    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getProduceDate() {
        return produceDate;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public String getFetStatusName() {
        if (fetStatus != null) {
            return Battery.FetStatus.getName(fetStatus);
        }
        return "";
    }

    public String getProtectStateName() {
        String name = "";
        if (null != protectState) {
            BatteryReportLog.ProtectState[] names = BatteryReportLog.ProtectState.values();


            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(protectState)))).reverse().toString().toCharArray();

            for (int i = 0; i < str.length; i++) {
                if ('1' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i].getName();
                }
            }
        }
        return name;
    }

    public String getSignalTypeName() {
        if(signalType != null) {
            return Battery.SignalType.getName(signalType);
        }
        return "";
    }

    public String getMaterialName() {
        if(material != null) {
            return Battery.Material.getName(material);
        }
        return "";
    }

}
