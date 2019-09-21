package cn.com.yusong.yhdg.common.domain.basic;

public class AreaCharger {

    int areaId;//区域Id
    int areaLevel;//区域等级
    String areaName;//区域名称
    Integer chargerCount;//区内充电站数量
    Integer chargerPileCount;//区内充电站插座数量
    Integer chargerFaultCount;//区内充电站故障数量

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getChargerCount() {
        return chargerCount;
    }

    public void setChargerCount(Integer chargerCount) {
        this.chargerCount = chargerCount;
    }

    public Integer getChargerPileCount() {
        return chargerPileCount;
    }

    public void setChargerPileCount(Integer chargerPileCount) {
        this.chargerPileCount = chargerPileCount;
    }

    public int getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(int areaLevel) {
        this.areaLevel = areaLevel;
    }

    public Integer getChargerFaultCount() {
        return chargerFaultCount;
    }

    public void setChargerFaultCount(Integer chargerFaultCount) {
        this.chargerFaultCount = chargerFaultCount;
    }
}
