package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 运营商押金流水表
 * */

@Getter
@Setter
public class AgentForegiftInOutMoney extends IntIdEntity {

    public enum BizType {
        IN_CUSTOMER_FOREGIFT(1, "骑手购买押金"),
        IN_AGENT_DEPOSIT_PAY(3, "运营商充值"),

        OUT_CUSTOMER_FOREGIFT(2, "骑手退押金"),
        OUT_AGENT_PAY_WITHDRAW_ORDER(4, "运营商提现"),
        ;

        private final int value;
        private final String name;

        BizType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BizType e : BizType.values()) {
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
        IN(1, "收入"),
        OUT(2, "支出"),
        ;


        private final int value;
        private final String name;

        Type(int value, String name) {
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
    Integer category;
    Integer bizType;//业务类型
    String bizId;//业务id
    Integer type;//类型 1 支出2 收入
    Integer money; //金额
    Integer balance; //剩余余额
    Integer remainMoney;//预留金额
    Integer ratio;//比例
    String operator;//操作人
    Date createTime;//创建时间

    @Transient
    String agentName;
    String queryTime;
    String statsDate;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getCategoryName() {
        if (category != null) {
            return ConstEnum.Category.getName(category);
        }
        return "";
    }

    public String getBizTypeName() {
        if (bizType != null) {
            return BizType.getName(bizType);
        }
        return "";
    }

    public String getTypeName() {
        if (type != null) {
            return Type.getName(type);
        }
        return "";
    }
}
