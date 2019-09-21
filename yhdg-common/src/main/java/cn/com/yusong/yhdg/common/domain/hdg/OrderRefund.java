
package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单退款
 */
@Setter
@Getter
public class OrderRefund extends PageEntity {

    public enum Type {
        BATTERY_ORDER(1, "换电订单"),
        PACKET_PERIOD_ORDER(2, "包时段订单"),
        CUSTOMER_BALANCE(3, "客户余额");


        private final int value;
        private final String name;

        private Type(int value, String name) {
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

    public enum Status {
        WAIT_EXAMINE(1, "待审核"),
        WAIT_CONFIRM(2, "待确认"),
        FAILURE_EXAMINE(3, "审核拒绝"),
        FAILURE_CONFIRM(4, "确认拒绝"),
        REFUND_SUCCESS(5, "退款成功"),
        ;


        private final int value;
        private final String name;

        private Status(int value, String name) {
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


    Integer id;
    Integer agentId;
    Integer type;
    String sourceId;
    Integer status;
    Integer money;
    Integer customerId;
    String customerFullname;
    String customerMobile;

    Integer applyMoney;
    String applyReason;
    String applyOperator;
    Date applyRefundTime;

    Integer refundMoney;
    String refundReason;
    String refundOperator;
    Date refundTime;

    String financeReason;
    String financeOperator;
    Date financeTime;
    String transferPath;

    Date createTime;


    String agentName;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getTypeName() {
        if (type != null) {
            return Type.getName(type);
        }
        return "";
    }

    public String getStatusName() {
        if (status != null) {
            return Status.getName(status);
        }
        return "";
    }
}
