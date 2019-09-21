package cn.com.yusong.yhdg.batteryserver.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Result {
    public final static int BEGIN_CHARGE = 1;
    public final static int NOT_CHARGE = 2;

    public String id;
    public String code;
    public Integer rtnCode;
    public Integer chargeType;
    public Integer duration;
    public Integer nextHeartbeat;
    public Integer reportSingleVoltage;
    public Integer gpsSwitch;
    public Integer lockSwitch;
    public Integer gprsShutdown;
    public Integer shutdownVoltage;
    public Integer acceleretedSpeed;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}