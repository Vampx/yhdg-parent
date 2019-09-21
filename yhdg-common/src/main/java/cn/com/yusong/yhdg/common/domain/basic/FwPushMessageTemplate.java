package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送消息模板详情
 */
public class FwPushMessageTemplate extends IntIdEntity {
    public Map<String, String> colorMap = new HashMap<String, String>();

    Integer alipayfwId;
    String templateName;
    String variable;
    String fwCode;
    Integer isActive;
    String memo;

    public static Map<String, String> toDetailMap(String[] variable, List<FwPushMessageTemplateDetail> detailList) {
        Map<String, String> map = new HashMap<String, String>();
        for(FwPushMessageTemplateDetail detail : detailList) {
            map.put(detail.getId(), detail.replace(variable));
        }
        return map;
    }

    public Integer getAlipayfwId() {
        return alipayfwId;
    }

    public void setAlipayfwId(Integer alipayfwId) {
        this.alipayfwId = alipayfwId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getFwCode() {
        return fwCode;
    }

    public void setFwCode(String fwCode) {
        this.fwCode = fwCode;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
