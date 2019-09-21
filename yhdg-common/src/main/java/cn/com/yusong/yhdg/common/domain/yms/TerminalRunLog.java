package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 终端运行日志
 */
public class TerminalRunLog extends PageEntity {
    public enum LogLevel {
        VERBOSE(2, "追踪"),
        DEBUG(3, "调试"),
        INFO(4, "信息"),
        WARN(5, "警告"),
        ERROR(6, "错误"),
        ASSERT(7, "断言"),
        ;

        static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for(LogLevel e : LogLevel.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        private final int value;
        private final String name;

        private LogLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    String terminalId;
    Long now;
    Long num;
    Integer agentId;
    Date reportTime;
    Integer logLevel;
    String tag;
    String content;
    Date createTime;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLogLevelName() {
        return LogLevel.getName(logLevel);
    }
}
