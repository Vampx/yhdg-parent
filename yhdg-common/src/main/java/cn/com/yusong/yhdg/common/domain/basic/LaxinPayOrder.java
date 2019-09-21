package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class LaxinPayOrder extends StringIdEntity {

    public enum Status {
        WAIT(1, "待付款"),
        SUCCESS(2, "付款成功")
        ;


        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, Status> map = new HashMap<Integer, Status>();

        static {
            for (Status s : Status.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static String getName(int value) {
            Status status = map.get(value);
            if (status == null) {
                return null;
            }
            return status.name;
        }
    }

    Integer agentId; /*运营商id*/
    String agentName;
    String agentCode;
    Integer money;
    Integer recordCount;
    Integer payType;
    Integer status;
    Date payTime;
    Date createTime;

    public String getPayTypeName() {
        if (payType == null) {
            return null;
        }
        return ConstEnum.PayType.getName(payType);
    }

    public String getStatusName() {
        if (status == null) {
            return null;
        }
        return Status.getName(status);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
