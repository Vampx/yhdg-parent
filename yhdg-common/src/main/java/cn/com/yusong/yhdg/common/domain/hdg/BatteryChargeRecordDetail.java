package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电池充电记录详情
 */
@Setter
@Getter
public class BatteryChargeRecordDetail extends LongIdEntity {
    public final static String BATTERY_CHARGE_RECORD_DETAIL_TABLE_NAME = "hdg_battery_charge_record_detail_";

    Date reportTime;             /*上报时间*/
    Integer currentVolume;       /*当前电量*/
    Integer currentPower;        /*当前功率*/

    @Transient
    String suffix;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
    }

    public Integer getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(Integer currentPower) {
        this.currentPower = currentPower;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public static String getBatteryChargeRecordDetailTableName() {
        return BATTERY_CHARGE_RECORD_DETAIL_TABLE_NAME;
    }
}
