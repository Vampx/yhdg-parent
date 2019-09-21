package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 语音消息
 */
@Getter
@Setter
public class VoiceMessage extends LongIdEntity {
    public static final int DELAY_TIME_ZERO = 0;
    public static final int DELAY_THIRTY_SECOND = 30;
    public static final int MAX_RESEND_NUM = 1;

    public enum MessageStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),;

        private final int value;
        private final String name;

        MessageStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (MessageStatus e : MessageStatus.values()) {
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

    public String getMessageStatusName() {
        if (status != null) {
            return MessageStatus.getName(status);
        }
        return "";
    }

    public enum SourceType {
        BATTERY_ORDER(1, "换电订单"),
        ;

        private final int value;
        private final String name;

        SourceType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getSourceTypeName(int value) {
            String rtn = null;
            for (SourceType e : SourceType.values()) {
                if (e.getValue() == value) {
                    rtn = e.getName();
                    break;
                }
            }
            return rtn;
        }
    }

    public static String buildVariableJson(String[] variable) {
        StringBuilder builder = new StringBuilder();
        if (variable.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        builder.append("{");
        for (int i = 0; i < variable.length; i = i + 2) {
            builder.append("\"");
            builder.append(variable[i]);
            builder.append("\":");
            builder.append("\"");
            builder.append(variable[i + 1]);
            builder.append("\"");

            if (i < variable.length - 2) {
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    Integer partnerId;
    Integer agentId;
    String agentName;
    String agentCode;
    String sourceId;
    Integer sourceType;
    String content;
    // 被叫显号,可在语音控制台中找到所购买的显号
    String calledShowNumber;
    // 被叫号码
    String calledNumber;
    // 当模板中存在变量时需要设置此值
    String variable;
    // 音量 取值范围 0--200
    Integer volume;
    // 播放次数
    Integer playTimes;
    String templateCode; //编码
    Date handleTime;
    Date createTime;
    Integer status;
    Integer senderId;
    Integer moduleId;
    Integer type;
    Integer delay;
    String msgId;
    Integer resendNum;
    String callbackStatus;

    @Transient
    String senderName, partnerName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
