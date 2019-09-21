package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;

import java.util.Date;

/**
 * 推送所需数据的元数据
 */
public class PushMetaData extends LongIdEntity {
    Integer sourceType;
    String sourceId;
    Date createTime;

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
