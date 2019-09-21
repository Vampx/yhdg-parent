package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

public class AgentSystemConfig extends StringIdEntity {

    Integer agentId;
    Integer categoryType;
    String categoryName;
    String configName;
    String configValue;
    Integer isReadOnly;
    Integer isShow;
    Integer valueType;

    public enum ValueType {
        STRING(1),
        INT(2),
        SWITCH(3);

        private final int value;

        private ValueType(int value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Integer getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(Integer isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }
}
