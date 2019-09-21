package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户退款记录表
 */
@Setter
@Getter
public class CustomerRefundRecord extends LongIdEntity {

    public enum Status {
        APPLY(1, "审核中"), FINISH(2, "退款完成"), FAIL(3, "退款失败"), CANCEL(4, "已取消");

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
    public enum Type {
        HD(1, "换电"), ZD(2, "租电"), ZC(3, "租车");

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
    public enum SourceType {
        HDGFOREGIFT(1, "换电押金订单"), HDGPACKETPERIOD(2, "换电租金订单"), HDGINSURANCE(3, "换电保险订单"),
        ZDFOREGIFT(4, "租电押金订单"), ZDPACKETPERIOD(5, "租电租金订单"), ZDINSURANCE(6, "租电保险订单"),
        HDMULTI(7, "换电多通道订单"),ZDMULTI(8, "租电多通道订单"),
        ZCGROUP(9,"租车组合订单"),
        ZCMULTI(10,"租车多通道订单"),
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

    public enum RefundType {
        BALANCE((byte)1, "退余额"),
        RETURN((byte)2, "原路返回");

        private final byte value;
        private final String name;

        RefundType(byte value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Byte, String> map = new HashMap<Byte, String>();

        static {
            for (RefundType e : RefundType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(byte value) {
            return map.get(value);
        }

        public byte getValue() {
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
    private String orderId;
    private Long customerId;
    private String mobile;
    private String fullname;
    private Integer sourceType;
    private String sourceId;
    private Integer refundMoney;
    private Date refundTime;
    private String moneyInfo;
    private Integer status;
    private Date createTime;
    private Integer refundType;
    private String ptPayOrderId;
    private Date cancelTime;

    private Integer type;
    private String statusName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getStatusName() {
        if (status != null) {
            return CustomerRefundRecord.Status.getName(status);
        }
        return "";
    }

}
