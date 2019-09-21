package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *     播放列表
 */
public class Playlist extends IntIdEntity {

    public enum Status {
        SCORE_HERO(1, "制作中"),
        WAIT_AUDIT(2, "待审核"),
        PUBLISHED(3, "已发布"),
        AUDIT_REJECT(4, "审核不通过"),;

        private final int value;
        private final String name;
        private static Map<Integer, String> map = new HashMap<Integer, String>();

        private Status(int value, String name) {
            this.value = value;
            this.name = name;
        }
        static {
            for (Status s : Status.values()) {
                map.put(s.getValue(), s.getName());
            }
        }
        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }


    Integer version;//版本
    Integer agentId;//运营商

    String playlistName;
    Date createTime;//审核时间
    Integer status;//状态
    String auditUser;
    Date autitTime;
    String auditMemo;

    @Transient
    String agentName;

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getAutitTime() {
        return autitTime;
    }

    public void setAutitTime(Date autitTime) {
        this.autitTime = autitTime;
    }

    public String getAuditMemo() {
        return auditMemo;
    }

    public void setAuditMemo(String auditMemo) {
        this.auditMemo = auditMemo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playListName) {
        this.playlistName = playListName;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
