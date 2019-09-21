package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.domain.PageEntity;

import java.util.HashMap;
import java.util.Map;

/**
 *     终端属性
 */
public class TerminalProperty extends PageEntity {

    public enum Flag {
        TRUE(0, "启用"),
        FALSE(1, "禁用"),
        ;

        private final int value;
        private final String name;

        private Flag(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    String terminalId; //终端id
    Integer orderNum; //播放顺序
    Integer isActive; //是否启用
    String propertyName; //属性名称
    String propertyValue; //属性值

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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
