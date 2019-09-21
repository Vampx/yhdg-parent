package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 终端上传日志
 */
public class TerminalUploadLog extends LongIdEntity {

    public enum Type {
        DUBUG(1, "调试"),
        INFO(2, "信息"),
        WARN(3, "警告"),
        ERROR(4, "错误"),
        ;

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

    public enum Status {
        INIT(1, "未下发"),
        NOTICE(2, "已下发"),
        UPLOAD(3, "已上传"),
        ;

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

    Integer agentId; //运营商Id
    String terminalId; //终端Id
    Integer type;//类型
    Integer status;//状态
    String filePath;//下载地址
    String logTime;//日志时间
    Date uploadTime;//上传时间
    Date createTime; //创建并初始化对象的时间

    @Transient
    Integer terminalGroupId;//终端分组
    Date queryLogTime;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(Integer terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public Date getQueryLogTime() {
        return queryLogTime;
    }

    public void setQueryLogTime(Date queryLogTime) {
        this.queryLogTime = queryLogTime;
    }

    public String getTypeName() {
        if(type != null) {
            return Type.getName(type);
        }
        return "";
    }

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }
}
