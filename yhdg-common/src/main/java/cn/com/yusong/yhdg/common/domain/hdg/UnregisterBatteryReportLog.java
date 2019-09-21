package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UnregisterBatteryReportLog extends PageEntity {

    public final static String UNREGISTER_BATTERY_REPORT_LOG_TABLE_NAME = "hdg_unregister_battery_report_log_";

    String code;
    String version;
    Integer voltage;            //电压
    Integer electricity;        //电流*/
    Integer currentCapacity;    //电流
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
    Integer currentSignal;      //当前信号*/
    String simCode;             //sim卡号*/
    String singleVoltage;       //电池每串单体电压*/
    Integer volume;
    Date createTime;

    @Transient
    Date queryLogTime;
    String suffix;
    String fetStatusName;//电池状态名称
    String protectStateName;   /*保护状态*/

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

    public UnregisterBatteryReportLog(String batteryId,
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
        this.code = batteryId;
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

    public UnregisterBatteryReportLog() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public Integer getProtectState() {
        return protectState;
    }

    public void setProtectState(Integer protectState) {
        this.protectState = protectState;
    }

    public Integer getFetStatus() {
        return fetStatus;
    }

    public void setFetStatus(Integer fetStatus) {
        this.fetStatus = fetStatus;
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

    public Integer getFet() {
        return fet;
    }

    public void setFet(Integer fet) {
        this.fet = fet;
    }

    public Integer getPositionState() {
        return positionState;
    }

    public void setPositionState(Integer positionState) {
        this.positionState = positionState;
    }

    public Integer getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(Integer currentSignal) {
        this.currentSignal = currentSignal;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getSingleVoltage() {
        return singleVoltage;
    }

    public void setSingleVoltage(String singleVoltage) {
        this.singleVoltage = singleVoltage;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getQueryLogTime() {
        return queryLogTime;
    }

    public void setQueryLogTime(Date queryLogTime) {
        this.queryLogTime = queryLogTime;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFetStatusName() {
        if (fetStatus!=null){
            return Battery.FetStatus.getName(fetStatus);
        }
        return "";
    }

    public String getProtectStateName() {
        String name = "";
        if (null != protectState) {
            String[] names = new String[]{"单体过压发生保护", "单体欠压发生保护", "整组过压发生保护", "整组欠压发生保护", "充电过温发生保护", "充电低温发生保护", "放电过温发生保护", "放电低温发生保护", "充电过流发生保护", "放电过流发生保护", "短路发生保护", "前端检测IC错误", "软件锁定MOS"};

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(protectState)))).reverse().toString().toCharArray();

            for (int i = 0; i < str.length; i++) {
                if ('1' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i];
                }
            }
        }
        return name;
    }
}
