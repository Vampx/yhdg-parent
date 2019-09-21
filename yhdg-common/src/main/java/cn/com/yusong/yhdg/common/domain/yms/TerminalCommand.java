package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 终端命令
 */
public class TerminalCommand extends LongIdEntity {

    public enum Status {
        WAIT(1, "未执行"),
        DISPATCH(2, "已下发"),
        SUCCESS(3, "执行成功"),
        FAIL(4, "执行失败");

        private final int value;
        private final String name;

        private Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Status e : Status.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum Type {
        RESTART(1, "重启"),
        SHUTDOWN(2, "关机"),
        SCREENSHOT(3, "截屏"),
        INITIALIZATION(4, "初始化"),
        UPLOAD_ERROR_LOG(5, "上传错误日志"),
        OFFLINE(6,"离线播放"),
        ONLINE(7,"在线播放"),
        UPGRADE(8,"升级");

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    Integer agentId;
    String terminalId;
    Integer type;
    String content;
    Integer status;
    String failureReason;
    Date dispatchTime;
    Date execTime;
    Date createTime;

    @Transient
    String plateNum;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public Integer getDescendant() {
        return descendant;
    }

    public void setDescendant(Integer descendant) {
        this.descendant = descendant;
    }
}
