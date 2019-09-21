package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电池利用率
 */
@Setter
@Getter
public class BatteryUtilize extends LongIdEntity {
    String batteryId;
    String cabinetId;/*取电柜子id*/
    String cabinetName; /*取电柜子名称*/
    Date putTime; /*放电时间*/
    Date takeTime; /*取电时间*/
    String utilize;

    public BatteryUtilize() {
    }

    public BatteryUtilize(String batteryId, String cabinetId, String cabinetName, Date putTime, Date takeTime, String utilize) {
        this.batteryId = batteryId;
        this.cabinetId = cabinetId;
        this.cabinetName = cabinetName;
        this.putTime = putTime;
        this.takeTime = takeTime;
        this.utilize = utilize;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
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

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPutTime() {
        return putTime;
    }

    public void setPutTime(Date putTime) {
        this.putTime = putTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Date takeTime) {
        this.takeTime = takeTime;
    }

    public String getUtilize() {
        return utilize;
    }

    public void setUtilize(String utilize) {
        this.utilize = utilize;
    }

}
