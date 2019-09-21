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
public class AgentDepositOrder extends StringIdEntity {

    public enum Status {
        NOT_PAID(1, "未支付"), HAVE_PAID(2, "已支付");

        private final int value;
        private final String name;

        Status(int value, String name) {
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

    private Integer partnerId;
    private Integer agentId;
    private String agentName;
    private String agentCode;
    private Integer money;
    private Integer status;
    private Date handleTime;
    private Integer payType;
    private String operator;
    private Date createTime;

    public String getPayTypeName(){
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime(){return handleTime;}

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime(){return createTime;}
}