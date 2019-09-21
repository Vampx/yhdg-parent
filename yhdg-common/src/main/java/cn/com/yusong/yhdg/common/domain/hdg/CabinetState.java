package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CabinetState {

    Integer type;//电池型号
    String cabinetId;//电站Id
    Integer agentId;//运营商
    Integer fullVolumeCount;//可租(满电)
    Integer waitTakeCount;//可收(欠压)
    Integer emptyBoxCount;//可还(空箱)

    String batteryTypeName; //电池类型名称

    public String getBatteryTypeName() {
        return batteryTypeName;
    }

    public void setBatteryTypeName(String batteryTypeName) {
        this.batteryTypeName = batteryTypeName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getFullVolumeCount() {
        return fullVolumeCount;
    }

    public void setFullVolumeCount(Integer fullVolumeCount) {
        this.fullVolumeCount = fullVolumeCount;
    }

    public Integer getWaitTakeCount() {
        return waitTakeCount;
    }

    public void setWaitTakeCount(Integer waitTakeCount) {
        this.waitTakeCount = waitTakeCount;
    }

    public Integer getEmptyBoxCount() {
        return emptyBoxCount;
    }

    public void setEmptyBoxCount(Integer emptyBoxCount) {
        this.emptyBoxCount = emptyBoxCount;
    }

}
