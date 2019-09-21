package cn.com.yusong.yhdg.common.entity;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备电池统计
 */
@Setter
@Getter
public class CabinetBoxStats extends PageEntity {
    String cabinetId; //换电柜id
    Integer boxCount; //换电柜格口数量
    Integer emptyCount; //空箱数量
    Integer openCount; //门打开的数量
    Integer batteryCount; //有电池的格口数量
    Integer chargingCount; //正在充电的电池(充电中)
    Integer waitChargeCount; //待充数量(待充电)
    Integer completeChargeCount; //完成充电的数量(满电)
    String cabinetName; //换电柜名称
    String agentName; //运营商名称

    Integer isOnlineCount; //在线数量
    Integer notCompleteChargeCount; //未完成充电数量(欠压)

    Integer notPayCount;//未付款数量
    Integer notTakeCount; //未取出数量

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public Integer getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(Integer boxCount) {
        this.boxCount = boxCount;
    }

    public Integer getEmptyCount() {
        return emptyCount;
    }

    public void setEmptyCount(Integer emptyCount) {
        this.emptyCount = emptyCount;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
    }

    public Integer getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(Integer batteryCount) {
        this.batteryCount = batteryCount;
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

    public Integer getCompleteChargeCount() {
        return completeChargeCount;
    }

    public void setCompleteChargeCount(Integer completeChargeCount) {
        this.completeChargeCount = completeChargeCount;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getIsOnlineCount() {
        return isOnlineCount;
    }

    public void setIsOnlineCount(Integer isOnlineCount) {
        this.isOnlineCount = isOnlineCount;
    }

    public Integer getNotCompleteChargeCount() {
        return notCompleteChargeCount;
    }

    public void setNotCompleteChargeCount(Integer notCompleteChargeCount) {
        this.notCompleteChargeCount = notCompleteChargeCount;
    }

    public Integer getNotPayCount() {
        return notPayCount;
    }

    public void setNotPayCount(Integer notPayCount) {
        this.notPayCount = notPayCount;
    }

    public Integer getNotTakeCount() {
        return notTakeCount;
    }

    public void setNotTakeCount(Integer notTakeCount) {
        this.notTakeCount = notTakeCount;
    }
}
