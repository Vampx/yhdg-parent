package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PushOrderMessage extends IntIdEntity {
    public enum SourceType {
        TAKE(1, "取电"),
        PUT(2, "放电"),
        BACK(3, "退电"),
        PUT_ERROR(6, "放电异常"),
        ;

        private final int value;
        private final String name;

        SourceType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (SourceType e : SourceType.values()) {
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

    public enum SendStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),;

        private final int value;
        private final String name;

        SendStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (SendStatus e : SendStatus.values()) {
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


    Integer sendStatus; //发送状态
    Integer sourceType; //
    String sourceId;
    Date createTime; //创建时间
    Date handleTime; //发送成功的时间
    Integer resendNum; //重发次数
    Integer agentId;
}
