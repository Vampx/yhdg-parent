package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Created by chen on 2017/10/30.
 */
public class BatchMobileMessage extends IntIdEntity {
    Long templateId;
    String templateName;
    String title;
    String content;
    String variable;
    String operatorName;
    Integer mobileCount;
    Date createTime;

    String mobile;
    String mobileMessageTemplateTitle;

    public String getMobileMessageTemplateTitle() {
        return mobileMessageTemplateTitle;
    }

    public void setMobileMessageTemplateTitle(String mobileMessageTemplateTitle) {
        this.mobileMessageTemplateTitle = mobileMessageTemplateTitle;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getMobileCount() {
        return mobileCount;
    }

    public void setMobileCount(Integer mobileCount) {
        this.mobileCount = mobileCount;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
