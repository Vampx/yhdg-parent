package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BatteryStatis {
    @Transient
    Integer factoryBatteryCount;    //工厂总电池数
    @Transient
    Integer fullCount;              //满电数
    @Transient
    Integer chargingCount;          //未充满数
    @Transient
    Integer waitChargeCount;        //待充电数

    public Integer getFactoryBatteryCount() {
        return factoryBatteryCount;
    }

    public void setFactoryBatteryCount(Integer factoryBatteryCount) {
        this.factoryBatteryCount = factoryBatteryCount;
    }

    public Integer getFullCount() {
        return fullCount;
    }

    public void setFullCount(Integer fullCount) {
        this.fullCount = fullCount;
    }

    public Integer getChargingCount() {
        return chargingCount;
    }

    public void setChargingCount(Integer chargingCount) {
        this.chargingCount = chargingCount;
    }

    public Integer getWaitChargeCount() {
        return waitChargeCount;
    }

    public void setWaitChargeCount(Integer waitChargeCount) {
        this.waitChargeCount = waitChargeCount;
    }
}
