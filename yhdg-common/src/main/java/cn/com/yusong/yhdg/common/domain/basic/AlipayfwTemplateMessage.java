package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AlipayfwTemplateMessage extends AbstractTemplateMessage {
    Integer agentId;
    Integer sourceType;
    String mobile;
    String openId;
    Integer alipayfwId;
    Date handleTime;
    Date createTime;
    Integer delay;
    Integer resendNum;
    String url; //跳转的URL

    private String nickname;
    private Integer readCount;

    public enum MessageStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),
        DISABLED(4, "禁用")
        ;

        private final int value;
        private final String name;

        private MessageStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (WeixinmpTemplateMessage.MessageStatus e : WeixinmpTemplateMessage.MessageStatus.values()) {
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

    public enum SourceType {
        CUSTOMER_DEPOSIT_ORDER(1, "充值订单"),
        CHARGER_ORDER(2, "充电订单"),
        CHARGER(3, "充电桩"),
        SYSTEM(4, "系统")
        ;

        private final int value;
        private final String name;

        private SourceType(int value, String name) {
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
            for (WeixinmpTemplateMessage.SourceType e : WeixinmpTemplateMessage.SourceType.values()) {
                if (e.getValue() == value) {
                    rtn = e.getName();
                    break;
                }
            }
            return rtn;
        }
    }

    public enum Type {
        CHARGE_COMPLETE(1, "充电完成通知"),
        CHARGE_BEGIN(2, "充电开始通知"),
        CHARGE_CANCEL(3, "充电取消通知"),
        CUSTOMER_DEPOSIT_COMPLETE(4,"充值成功通知"),
        CHARGER_WARNING(5,"设备告警通知"),
        CHARGER_ONLINE(6,"设备上线通知"),
        CHARGER_OFFLINE(7,"设备离线通知"),
        SYSTEM_FAULT(8, "系统故障")
        ;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public static String buildVariableJson(String[] variable) throws IOException {
        Map<String, String> param = new HashMap<String, String>();

        for(int i = 0; i < variable.length; i = i + 2) {
            param.put(variable[i], variable[i + 1]);
        }
        return AppUtils.encodeJson(param);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
