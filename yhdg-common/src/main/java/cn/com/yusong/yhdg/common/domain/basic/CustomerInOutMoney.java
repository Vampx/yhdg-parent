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
 * 客户出入流水帐
 */
@Setter
@Getter
public class CustomerInOutMoney extends LongIdEntity {

    public enum BizType {
        IN_CUSTOMER_EXCHANGE_REFUND(1, "换电退款"),
        IN_CUSTOMER_PACKET_PERIOD_ORDER_REFUND(3, "换电包时段套餐退款"),
        IN_CUSTOMER_FOREGIFT_REFUND(5, "换电押金退款"),
        IN_CUSTOMER_DEPOSIT_PAY(7, "客户充值"),
        IN_CUSTOMER_WEB_DEPOSIT(9, "WEB充值"),
        IN_CUSTOMER_DEPOSIT_GIFT(11, "客户充值赠送"),
        IN_CUSTOMER_INSURANCE_REFUND(13, "客户保险退款"),
        IN_CUSTOMER_DEDUCTION_TICKET(15, "客户抵扣券退款"),
        IN_CUSTOMER_LAXIN_INCOME(17, "拉新收入"),
        IN_CUSTOMER_WITHDRAW_REFUND(19, "提现退款收入"),
        IN_CUSTOMER_RENT_PERIOD_ORDER_REFUND(21, "租电包时段套餐退款"),
        IN_CUSTOMER_RENT_FOREGIFT_REFUND(23, "租电押金退款"),
        IN_CUSTOMER_RENT_INSURANCE_REFUND(25, "客户租电保险退款"),
        IN_CUSTOMER_MULTI_REFUND(27, "多通道退款"),
        IN_CUSTOMER_VEHICLE_GROUP_REFUND(29, "租车组合订单退款"),

        OUT_CUSTOMER_EXCHANGE_PAY(2, "换电支付"),
        OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY(4, "换电包时段套餐支付"),
        OUT_CUSTOMER_FOREGIFT_PAY(6, "换电押金支付"),
        OUT_CUSTOMER_DEPOSIT_REFUND(8, "客户充值退款"),
        OUT_CUSTOMER_BALANCE_REFUND(10, "客户余额扣款"),
        OUT_CUSTOMER_RESIGNATION(12, "客户离职扣除运营商资金"),
        OUT_CUSTOMER_INSURANCE(14, "客户保险支付"),
        OUT_CUSTOMER_WITHDRAW(16, "申请提现"),
        OUT_CUSTOMER_RENT_PERIOD_ORDER_PAY(18, "租电包时段套餐支付"),
        OUT_CUSTOMER_RENT_FOREGIFT_PAY(20, "租电押金支付"),
        OUT_CUSTOMER_RENT_INSURANCE(22, "客户租电保险支付"),
        OUT_CUSTOMER_INSTALLMENT_PAY_ORDER(24, "客户押金分期欠款支付"),
        OUT_CUSTOMER_MULTI_PAY(26, "多通道支付"),
        OUT_CUSTOMER_VEHICLE_GROUP_PAY(27, "租车组合支付"),

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

    Long customerId;
    Integer bizType;//业务类型
    String bizId;//订单id
    Integer type;//类型 1 支出 2 收入
    Integer money; //金额 按分计
    Integer balance;//剩余金额 按分计
    String statusInfo;//状态信息
    Date createTime;

    @Transient
    String customerName;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getBizTypeName() {
        if(bizType != null) {
            return BizType.getName(bizType);
        }
        return "";
    }
    public String getTypeName() {
        if(type != null) {
            return Type.getName(type);
        }
        return "";
    }
}
