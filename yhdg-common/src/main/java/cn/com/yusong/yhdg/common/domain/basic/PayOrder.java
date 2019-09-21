package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 2017/10/28.
 */
@Getter
@Setter
public abstract class PayOrder extends StringIdEntity {
    public enum SourceType {
        DEPOSIT_ORDER_CUSTOMER_PAY(1, "客户充值支付费用"),
        BATTERY_ORDER_CUSTOMER_PAY(2, "客户换电支付费用"),
        FOREGIFT_ORDER_CUSTOMER_PAY(3, "客户换电押金支付费用"),
        PACKET_PERIOD_ORDER_PAY(4,"客户购买换电套餐支付费用"),
        LAXIN_PAY_ORDER_PAY(7, "拉新订单支付费用"),
        DEPOSIT_ORDER_AGENT_PAY(8, "运营商充值支付费用"),
        DEPOSIT_ORDER_SHOP_PAY(9, "门店充值支付费用"),
        RENT_FOREGIFT_ORDER_PAY(10, "客户租电押金支付费用"),
        RENT_PERIOD_ORDER_PAY(11,"客户购买租电套餐支付费用"),
        CUSTOMER_EXCHANGE_FIRST_MONEY_PAY(12,"客户换电押金首付支付费用"),
        FOREGIFT_DEPOSIT_ORDER_AGENT_PAY(13,"运营商押金充值支付费用"),
        MULTI_ORDER_CUSTOMER_PAY(14, "客户多通道支付费用"),
        CUSTOMER_INSTALLMENT_MONEY_PAY(15,"客户押金分期支付费用"),
        CUSTOMER_RENT_FIRST_MONEY_PAY(16,"客户租电押金首付支付费用"),
        CUSTOMER_RENT_GROUP_MONEY_PAY(17,"客户租车组合订单支付费用"),
        CUSTOMER_RENT_GROUP_RENT_MONEY_PAY(18,"客户租车组合订单套餐续租支付费用"),
        MULTI_ORDER_VEHICLE_PAY(19,"客户租车多通道支付费用"),
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

    public enum Status {
        INIT(1, "未付款"), SUCCESS(2, "成功"), REFUND_SUCCESS(3, "退款成功");

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

    Integer partnerId;
    Integer agentId;
    Long cabinetCompanyId;
    Long customerId;
    Integer money; //费用
    Integer sourceType; //关联的订单类型
    String sourceId; //关联的订单ID
    String paymentId;//第三方支付的id
    Integer orderStatus;
    Date handleTime;
    Date refundTime;
    Integer refundMoney;
    String memo; //备注
    Date createTime;

    String mobile;/*手机号*/

    @Transient
    String agentName; //运营商名称
    String customerName;
    String orderRefundId;

    public String getSourceTypeName() {
        if(sourceType != null) {
            return SourceType.getName(sourceType);
        }
        return "";
    }

    public String getStatusName() {
        if(orderStatus != null) {
            return Status.getName(orderStatus);
        }
        return "";
    }


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }


    public SourceDetail parseSourceId() {
        String[] array = StringUtils.split(sourceId, "_");
        SourceDetail sourceDetail = new SourceDetail();
        sourceDetail.orderId = array[0];
        sourceDetail.num = Integer.parseInt(array[1]);
        return sourceDetail;
    }

    public static class SourceDetail {
        public String orderId;
        public int num;
    }
}
