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

/**
 * 微信模板消息
 */
@Getter
@Setter
public class WeixinmpTemplateMessage extends AbstractTemplateMessage {
    public static final int DELAY_TIME_ZERO = 0;
    public static final int DELAY_THIRTY_SECOND = 30;

    public static final int MAX_RESEND_NUM = 1;

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

    public enum SourceType {
        SYSTEM(1, "系统"),
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
            for (SourceType e : SourceType.values()) {
                if (e.getValue() == value) {
                    rtn = e.getName();
                    break;
                }
            }
            return rtn;
        }
    }

//    public enum Type {
//        customer(1, "客户"),
//        agent(2, "运营商"),
//        ;
//
//        private final int value;
//        private final String name;
//
//        private Type(int value, String name) {
//            this.value = value;
//            this.name = name;
//        }
//
//        private static Map<Integer, String> map = new HashMap<Integer, String>();
//        static {
//            for (Type e : Type.values()) {
//                map.put(e.getValue(), e.getName());
//            }
//        }
//
//        public static String getName(int value) {
//            return map.get(value);
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }

    public static String buildVariableJson(String[] variable) throws IOException {
        Map<String, String> param = new HashMap<String, String>();

        for(int i = 0; i < variable.length; i = i + 2) {
            param.put(variable[i], variable[i + 1]);
        }
        return AppUtils.encodeJson(param);
    }

    private Integer agentId;
    private Integer sourceType; //来源类型
    private String mobile; //手机号
    private Integer weixinmpId;
    private String openId;//openid
    private  Date handleTime;//处理时间
    private Date createTime;//创建模板消息时间
    private Integer delay; //延迟times发送
    private Integer resendNum; //重发
    private String url; //跳转的URL
    private String nickname;
    private Integer readCount;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
