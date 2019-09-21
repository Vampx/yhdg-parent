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
 * 运营商出入流水帐
 */
@Setter
@Getter
public class AgentInOutMoney extends LongIdEntity {


    public enum AgentInOutMoneyColumn {
        AGENTNAME(1, "运营商名称"),
        CREATETIME(2, "发生时间"),
        BIZTYPE(3, "业务类型"),
        TYPE(4, "订单类型"),
        MONEY(5, "金额"),
        BALANCE(6, "剩余金额"),
        SOURCEID(7, "来源"),
        OPERATOR(8, "操作人"),;

        private final int value;
        private final String name;

        AgentInOutMoneyColumn(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (AgentInOutMoneyColumn e : AgentInOutMoneyColumn.values()) {
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

    public enum BizType {
        IN_AGENT_DEPOSIT_PAY(1, "运营商充值"),
        IN_AGENT_CUSTOMER_ROLLBACK(3, "骑手离职退回资金"),
        IN_AGENT_DEPOSIT_GIFT(5, "运营商充值折扣赠送"),
        IN_PACKET_PERIOD_RATIO(7, "骑手购买包时段套餐分成收入"),
        IN_WITHDRAW_REFUND(9, "提现退款"),
        IN_EXCHANGR_RATIO(11, "运营商按次分成收入"),
        IN_INSURANCE(13, "保险收入"),
        IN_FOREGIFT_REMAIN(19, "押金剩余收入"),
        IN_AGENT_FOREGIFT_WITHDRAW(21, "运营商押金提现"),
        IN_EXCHANGE_BALANCE_RECORD(23, "换电结算收入"),
        IN_RENT_BALANCE_RECORD(25, "租电结算收入"),

        OUT_AGENT_CUSTOMER_DEPOSIT(2, "站点骑手充值"),
        OUT_AGENT_PAY_LAXIN_ORDER(4, "运营商支付拉新订单"),
        OUT_AGENT_PAY_WITHDRAW_ORDER(6, "运营商余额提现订单"),
        OUT_AGENT_PAY_MATERIAL(8,"运营商设备支出"),
        OUT_PACKET_PERIOD_RATIO(10, "包时段套餐退款"),
        OUT_INSURANCE(12, "保险退款"),
        OUT_DEDUCTION_TICKET(14, "抵扣券支出"),
        OUT_AGENT_FOREGIFT_DEPOSIT_PAY(16, "运营商押金充值"),
        OUT_SHOP_CLEARING(26, "门店结算"),
        OUT_AGENT_DEGREE_PRICE(27, "运营商支付物业电费"),
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
    Integer bizType;//业务类型
    String bizId;//业务id
    Integer type;//类型 1 支出2 收入
    Integer money; //金额
    Integer balance; //剩余余额
    String operator;//操作人
    Date createTime;//创建时间

    @Transient
    String agentName;
    String queryTime;
    String statsDate;

    public AgentInOutMoney(Integer agentId, Integer money, Integer balance, Integer bizType, Integer type, String bizId, String operator, Date createTime) {
        this.agentId = agentId;
        this.money = money;
        this.balance = balance;
        this.bizType = bizType;
        this.type = type;
        this.bizId = bizId;
        this.operator = operator;
        this.createTime = createTime;
    }

    public AgentInOutMoney() {
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
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
