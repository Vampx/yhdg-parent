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

@Setter
@Getter
public class CustomerMultiOrder extends LongIdEntity {
    public enum Status {
        NOT_PAY(1, "未支付"),
        IN_PAY(2, "付款中"),
        HAVE_PAY(3, "付款完成"),
        APPLY_REFUND(4,"申请退款"),
        REFUND_SUCCESS(5,"退款成功"),
        ;

        private final int value;
        private final String name;

        private Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (CustomerMultiOrder.Status e : CustomerMultiOrder.Status.values()) {
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
        HD(1, "换电"),
        ZD(2, "租电"),
        ZC(3, "租车"),
        ;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (CustomerInOutMoney.Type e : CustomerInOutMoney.Type.values()) {
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

    private Integer totalMoney;
    private Integer debtMoney;
    private Integer partnerId;
    private Long customerId;
    private String fullname;
    private String mobile;
    private Integer agentId;
    private String agentName;
    private String agentCode;
    private Integer status;
    private Integer type;
    private Date applyRefundTime;           //申请退款时间
    private Date refundTime;                //退款时间
    private Integer refundMoney;            //退款金额
    private String refundOperator;          //退款人
    private Date createTime;

    @Transient
    String payOrderId;
    Integer payOrderMoney;
    Integer refundableMoney;
    Date handleTime;
    String memo;
    Long refundRecordId;
    Integer sourceType;
    String partnerName;

    public String getStatusName() {
        if (status != null) {
            return CustomerMultiOrder.Status.getName(status);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}