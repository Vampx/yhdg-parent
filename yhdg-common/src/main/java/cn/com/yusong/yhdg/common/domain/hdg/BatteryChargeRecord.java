package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 电池充电记录
 */
@Setter
@Getter
public class BatteryChargeRecord extends LongIdEntity {
    public final static String BATTERY_CHARGE_RECORD_TABLE_NAME = "hdg_battery_charge_record_";

    Integer agentId;
    String batteryId;
    Integer type;           /*1 客户充电 2 柜子充电 3 维护充电*/
    Date reportTime;
    Date beginTime;         /*充电开始时间*/
    Date endTime;           /*充电结束时间*/
    Integer beginVolume;    /*开始电量 百分比*/
    Integer currentVolume;  /*当前电量 百分比*/
    Integer duration;    /*充电时长 分钟*/
    /*柜子充电*/
    String cabinetId;
    String cabinetName;
    String boxNum;

    Date createTime;
    @Transient
    String agentName; //运营商
    @Transient
    String typeName; //类型名称

    public enum Type {
        CUSTOMER(1, "客户充电"),
        CABINET(2, "柜子充电"),
        KEEPER(3, "维护充电");

        private final int value;
        private final String name;

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (BatteryChargeRecord.Type e : BatteryChargeRecord.Type.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBeginVolume() {
        return beginVolume;
    }

    public void setBeginVolume(Integer beginVolume) {
        this.beginVolume = beginVolume;
    }

    public Integer getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static String getBatteryChargeRecordTableName() {
        return BATTERY_CHARGE_RECORD_TABLE_NAME;
    }
}
