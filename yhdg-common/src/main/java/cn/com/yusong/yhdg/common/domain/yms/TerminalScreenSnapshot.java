package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 终端截屏
 */
public class TerminalScreenSnapshot extends LongIdEntity {

    Integer agentId;
    String terminalId;
    String snapshotPath;
    Date snapTime;
    Date createTime;

    @Transient
    Integer descendant;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(Date snapTime) {
        this.snapTime = snapTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDescendant() {
        return descendant;
    }

    public void setDescendant(Integer descendant) {
        this.descendant = descendant;
    }

    @Override
    public String toString() {
        return "TerminalScreenSnapshot{" +
                "agentId=" + agentId +
                ", terminalId='" + terminalId + '\'' +
                ", snapshotPath='" + snapshotPath + '\'' +
                ", snapTime=" + snapTime +
                ", createTime=" + createTime +
                ", descendant=" + descendant +
                '}';
    }
}
