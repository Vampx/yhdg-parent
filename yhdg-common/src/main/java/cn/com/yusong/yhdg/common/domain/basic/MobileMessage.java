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
 * 短信消息
 */
@Setter
@Getter
public class MobileMessage extends LongIdEntity {

    public static final int DELAY_TIME_ZERO = 0;
    public static final int DELAY_THIRTY_SECOND = 30;

    public static final int MAX_RESEND_NUM = 1;

    public enum MessageStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),
        ;

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
        if(status != null) {
            return MessageStatus.getName(status);
        }
        return "";
    }

    public enum ClwCallbackStatus {

        DELIVRD("DELIVRD", "短消息转发成功"),
        EXPIRED("EXPIRED","短消息超过有效期"),
        UNDELIV("UNDELIV", "短消息是不可达的"),
        UNKNOWN("UNKNOWN", "未知短消息状态"),
        REJECTD("REJECTD", "短消息被短信中心拒绝"),
        DTBLACK("DTBLACK", "目的号码是黑名单号码"),
        ERR_104("ERR:104", "系统忙"),
        REJECT("REJECT", "审核驳回"),
        other("其他", "网关内部状态");

        private final String value;
        private final String name;

        ClwCallbackStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<String, String> map = new HashMap<String, String>();
        static {
            for (ClwCallbackStatus e : ClwCallbackStatus.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(String value) {
            return map.get(value);
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

    }

    public enum SourceType {
        SEND_ORDER(1, "寄件订单"),
        DELIVER_ORDER(2, "派件订单"),
        STORE_ORDER(3, "存包订单"),
        WITHDRAWAL(4, "提现"),
        DEPOSIT(5, "充值"),
        AUTH_CODE(6, "验证码"),
        FOREGIFT_ORDER(7,"押金订单"),
        PACKET_PERIOD_ORDER(8,"购买套餐订单")
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
        if(variable.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        builder.append("{");
        for(int i = 0; i < variable.length; i = i + 2) {
            builder.append("\"");
            builder.append(variable[i]);
            builder.append("\":");
            builder.append("\"");
            builder.append(variable[i + 1]);
            builder.append("\"");

            if(i < variable.length - 2) {
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    Integer partnerId;
    String sourceId;
    Integer sourceType;
    String content;
    String mobile;
    String variable; //变量
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
    String senderName;
    String partnerName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }


    public String getStatusName() {
        if(status != null) {
            return MessageStatus.getName(status);
        }
        return "";
    }

}
