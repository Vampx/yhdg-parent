package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Calendar;
import java.util.Date;

/**
 * 换电订单电池上报日志
 */
@Setter
@Getter
public class BatteryOrderBatteryReportLog extends PageEntity {

    public final static String BATTERY_ORDER_REPORT_LOG_TABLE_NAME = "hdg_battery_order_battery_report_log_";

    String orderId; //
    Date reportTime; //
    Integer volume; //
    String temp; //
    Double lng; //
    Double lat; //
    String coordinateType; //
    Integer distance;
    Integer currentSignal; //
    String address; //

    @Transient
    String suffix;

    public static String getBatteryOrderReportLogTableName() {
        return BATTERY_ORDER_REPORT_LOG_TABLE_NAME;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
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

    public Integer getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(Integer currentSignal) {
        this.currentSignal = currentSignal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }
}
