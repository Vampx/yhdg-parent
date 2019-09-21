package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 电池上报日志
 */
@Setter
@Getter
public class BatteryReportLog extends PageEntity {

    public static final int NOT_CHARGE = 0; //未充电
    public static final int CHARGING = 1; //充电中

    public final static String BATTERY_REPORT_LOG_TABLE_NAME = "hdg_battery_report_log_";

    public enum BatteryReportLogColumn {
        BATTERYID(1, "电池id"),
        VOLTAGE(2, "总电压"),
        ELECTRICITY(3, "电流"),
        CURRENTCAPACITY(4, "当前电量"),
        DISTANCE(5,"上报间隔距离"),
        SIMCODE(6,"sim卡号"),
        STRAND(7,"电池串数"),
        SINGLEVOLTAGE(8,"单体电压(mv)"),
        POWER(9,"功率"),
        TEMP(10, "温度(℃)"),
        CURRENTSIGNAL(11,"信号"),
        ADDRESS(12,"位置"),
        LNGLAT(13, "经度/纬度"),
        COORDINATETYPE(14,"经纬类型"),
        PROTECTSTATENAME(15,"保护状态"),
        FETNAME(16,"MOS指示状态"),
        POSITIONSTATENAME(17,"位置状态"),
        FETSTATUSNAME(18,"电池状态"),
        CHARGESTATUSNAME(19,"充电状态"),
        REPORTTIME(20,"上报时间");

        private final int value;
        private final String name;

        BatteryReportLogColumn(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BatteryReportLog.BatteryReportLogColumn e : BatteryReportLog.BatteryReportLogColumn.values()) {
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

        ProtectState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ProtectState e : ProtectState.values()) {
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

    public enum ChargeStatus {
        NOT_CHARGE(0, "未充电"),
        CHARGING(1, "充电中");

        private final int value;
        private final String name;

        ChargeStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ChargeStatus e : ChargeStatus.values()) {
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

    public enum ChargeStatusName {
        CODE_0(0, "充电器过压保护"),
        CODE_1(1, "充电器过流保护"),
        CODE_2(2, "充电器过温保护"),
        CODE_3(3, "充电器充电时间异常"),
        CODE_4(4, "电池充满"),
        CODE_5(5, "1充电中 0未充电"),
        CODE_6(6, "充电器转接板通讯异常"),;

        private final int value;
        private final String name;

        ChargeStatusName(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ChargeStatusName e : ChargeStatusName.values()) {
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

    String batteryId; //
    Date reportTime; //
    Integer voltage; //
    Integer electricity; //
    Integer volume;
    Integer currentCapacity; //
    Integer protectState;            /*保护状态 0为未保护，1发生保护*/
    Integer fet;                     /*MOS指示状态 0表示MOS关闭，1表示打开*/
    Integer strand; //
    String temp; //
    Double lng; //
    Double lat; //
    String coordinateType; //
    Integer distance;
    Integer fetStatus; //
    Integer power; //
    Integer currentSignal; //
    Integer chargeStatus; //
    Integer isElectrify; //
    Integer positionState; //
    String address; //
    String simCode; //
    String singleVoltage; //

    @Transient
    String suffix;
    String protectStateName;   /*保护状态*/
    String fetName;   /*MOS*/
    String version;   /*版本*/
    Integer totalCapacity;   /*总容量*/
    Integer useCount;   /*使用次数*/
    Date produceDate;   /*生产日期*/
    String chargeStatusName; //充电状态名称
    String positionStateName; //位置状态名称
    String fetStatusName;//状态名称
    Double bdLng; //
    Double bdLat; //

    public BatteryReportLog(String batteryId, String version, String voltage, String electricity, String currentCapacity, String totalCapacity, String useCount, String produceDate, String protectState, String fet, String strand, String temp, Double lng, Double lat, String fetStatus, String positionState, String currentSignal, String chargeStatus, String address, String simCode, String singleVoltage) {
        this.batteryId = batteryId;
        this.reportTime = new Date();
        this.version = version;
        if (StringUtils.isNotEmpty(voltage)) {
            this.voltage = Integer.parseInt(voltage, 16) * 10;
        }
        if (StringUtils.isNotEmpty(electricity)) {

            this.electricity = Integer.parseInt(electricity, 16);
            if (this.electricity > 32767) {
                this.electricity = 65535 - this.electricity;
            }
            this.electricity = this.electricity * 10;
        }
        if (StringUtils.isNotEmpty(currentCapacity)) {
            this.currentCapacity = Integer.parseInt(currentCapacity, 16) * 10;
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
        if (StringUtils.isNotEmpty(chargeStatus)) {
            this.chargeStatus = Integer.parseInt(chargeStatus, 16);
        }
        this.address = address;
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

    public BatteryReportLog(String batteryId, Integer voltage, Integer electricity, Integer volume, Integer protectState, Integer fet, Double lng, Double lat, Integer chargeStatus, Integer temp1, Integer temp2, String address, Integer currentCapacity, Integer power) {
        this.batteryId = batteryId;
        this.reportTime = new Date();
        this.voltage = voltage;
        this.electricity = electricity;
        this.volume = volume;
        if (electricity > 0) {
            this.fetStatus = Battery.FetStatus.CHARGE.getValue();
        } else if (electricity < 0) {
            this.fetStatus = Battery.FetStatus.DISCHARGE.getValue();

        } else if (electricity == 0) {
            this.fetStatus = Battery.FetStatus.STATIC.getValue();

        }
        this.protectState = protectState;
        this.fet = fet;
        this.lng = lng;
        this.lat = lat;
        this.chargeStatus = chargeStatus;
        this.currentCapacity = currentCapacity;
        this.address = address;
        this.power = power;
        if (temp1 != null && temp2 != null) {
            String t = String.valueOf((temp1 - 2731) / 10);
            t = t + "," + String.valueOf((temp2 - 2731) / 10);
            this.temp = t;
        }
    }

    public BatteryReportLog() {

    }

    public Double getBdLng() {
        return bdLng;
    }

    public void setBdLng(Double bdLng) {
        this.bdLng = bdLng;
    }

    public Double getBdLat() {
        return bdLat;
    }

    public void setBdLat(Double bdLat) {
        this.bdLat = bdLat;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getSingleVoltage() {
        return singleVoltage;
    }

    public void setSingleVoltage(String singleVoltage) {
        this.singleVoltage = singleVoltage;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public Integer getElectricity() {
        return electricity;
    }

    public void setElectricity(Integer electricity) {
        this.electricity = electricity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Integer getProtectState() {
        return protectState;
    }

    public void setProtectState(Integer protectState) {
        this.protectState = protectState;
    }

    public Integer getFet() {
        return fet;
    }

    public void setFet(Integer fet) {
        this.fet = fet;
    }

    public Integer getStrand() {
        return strand;
    }

    public void setStrand(Integer strand) {
        this.strand = strand;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getFetStatus() {
        return fetStatus;
    }

    public void setFetStatus(Integer fetStatus) {
        this.fetStatus = fetStatus;
    }

    public Integer getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(Integer currentSignal) {
        this.currentSignal = currentSignal;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public Integer getIsElectrify() {
        return isElectrify;
    }

    public void setIsElectrify(Integer isElectrify) {
        this.isElectrify = isElectrify;
    }

    public Integer getPositionState() {
        return positionState;
    }

    public void setPositionState(Integer positionState) {
        this.positionState = positionState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtectStateName() {
        String name = "";
        if (null != protectState) {
            ProtectState[] names = ProtectState.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(protectState)))).reverse().toString().toCharArray();

            for (int i = 0; i < str.length; i++) {
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

    public String getFetName() {
        String name = "";
        if (null != fet) {
            String[] names = new String[]{"充电MOS", "放电MOS"};

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(fet)))).reverse().toString().toCharArray();

            for (int i = 0; i < str.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                name += names[i];
                if ('1' == str[i]) {
                    name += "打开";
                } else {
                    name += "关闭";
                }
            }
        }
        return name;
    }

    public String getChargeStatusName() {
        if (chargeStatus != null) {
            chargeStatusName = ChargeStatus.getName(chargeStatus);
            if (StringUtils.isEmpty(chargeStatusName)) {
                char[] str = new StringBuilder(String.format("%0" + BatteryReportLog.ChargeStatusName.values().length + "d", Long.parseLong(Integer.toBinaryString(chargeStatus)))).reverse().toString().toCharArray();
                for (int i = 0; i < str.length; i++) {
                    if (i == 5 && str[4] != '1') {
                        if (StringUtils.isNotEmpty(chargeStatusName)) {
                            chargeStatusName += "," + ChargeStatus.getName(str[i] - '0');
                        } else {
                            chargeStatusName = ChargeStatus.getName(str[i] - '0');
                        }
                    } else {
                        if ('1' == str[i]) {
                            if (StringUtils.isNotEmpty(chargeStatusName)) {
                                chargeStatusName += "," + BatteryReportLog.ChargeStatusName.getName(i);
                            } else {
                                chargeStatusName = BatteryReportLog.ChargeStatusName.getName(i);
                            }
                        }
                    }
                }
            }
            return chargeStatusName;
        }
        return "";
    }

    public String getPositionStateName() {
        if (positionState != null) {
            return Battery.PositionState.getName(positionState);
        }
        return "";
    }

    public String getFetStatusName() {
        if (fetStatus != null) {
            return Battery.FetStatus.getName(fetStatus);
        }
        return "";
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
