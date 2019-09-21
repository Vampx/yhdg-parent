package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 换电柜配置信息
 */
@Setter
@Getter
public class CabinetProperty implements Serializable {
    String cabinetId;
    Integer orderNum;
    Integer isActive;
    String propertyName;
    String propertyValue;

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
