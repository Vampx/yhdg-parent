package cn.com.yusong.yhdg.batteryserver.entity;

public class Param {
    public String code;
    public String version;
    public String voltage;
    public String electricity;
    public String surplusVolume;
    public String volume;
    public String second;
    public String produceDate;
    public String protectState;
    public String fet;
    public String batteryStrand;
    public String ntcSize;
    public String ntc;
    public String lng_;
    public String lat_;
    public String fetStatus;
    public String positionState;
    public String signal;
    public String chargeStatus;
    public String simCode;
    public String singleVoltage;
    public String percent;
    public Integer hit;
    public Integer dismantle;

    public Param() {
    }

    public Param(String code, String version, String voltage, String electricity, String surplusVolume, String volume, String second, String produceDate, String protectState, String fet, String batteryStrand, String ntcSize, String ntc, String lng_, String lat_, String fetStatus, String positionState, String signal, String chargeStatus, String simCode, String singleVoltage, String percent, Integer hit, Integer dismantle) {
        this.code = code;
        this.version = version;
        this.voltage = voltage;
        this.electricity = electricity;
        this.surplusVolume = surplusVolume;
        this.volume = volume;
        this.second = second;
        this.produceDate = produceDate;
        this.protectState = protectState;
        this.fet = fet;
        this.batteryStrand = batteryStrand;
        this.ntcSize = ntcSize;
        this.ntc = ntc;
        this.lng_ = lng_;
        this.lat_ = lat_;
        this.fetStatus = fetStatus;
        this.positionState = positionState;
        this.signal = signal;
        this.chargeStatus = chargeStatus;
        this.simCode = simCode;
        this.singleVoltage = singleVoltage;
        this.percent = percent;
        this.hit = hit;
        this.dismantle = dismantle;
    }
}