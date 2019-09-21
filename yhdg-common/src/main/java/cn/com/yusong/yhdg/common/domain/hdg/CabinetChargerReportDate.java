package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/27.
 */
@Setter
@Getter
public class CabinetChargerReportDate extends PageEntity {
    String cabinetId; //
    String boxNum;
    String reportDate; //
    Integer recordCount; //

    String yeah,month,day;

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

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
