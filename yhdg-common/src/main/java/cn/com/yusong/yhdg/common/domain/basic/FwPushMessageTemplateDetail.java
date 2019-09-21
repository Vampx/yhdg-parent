package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import org.apache.commons.lang.StringUtils;

/**
 * 推送消息模板详情
 */
public class FwPushMessageTemplateDetail extends StringIdEntity {

    Integer alipayfwId;
    String keywordName;
    String keywordValue;
    String color;
    Integer orderNum;
    Integer templateId;

    String newId;
    public String replace(String... param) {
        if(param.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        String copy = keywordValue;
        for(int i = 0; i < param.length; i = i + 2) {
            copy = StringUtils.replace(copy, "${" + param[i] + "}", StringUtils.trimToEmpty(param[i + 1]));
        }
        return copy;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public Integer getAlipayfwId() {
        return alipayfwId;
    }

    public void setAlipayfwId(Integer alipayfwId) {
        this.alipayfwId = alipayfwId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getKeywordValue() {
        return keywordValue;
    }

    public void setKeywordValue(String keywordValue) {
        this.keywordValue = keywordValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
