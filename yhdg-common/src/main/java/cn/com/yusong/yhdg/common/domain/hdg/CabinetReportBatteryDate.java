package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/27.
 */
@Setter
@Getter
public class CabinetReportBatteryDate extends PageEntity {
    String batteryId; //
    String reportDate; //
    Integer recordCount; //

    String yeah,month,day;

    public String getYeah() {
        return yeah;
    }

    public void setYeah(String yeah) {
        this.yeah = yeah;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
}
