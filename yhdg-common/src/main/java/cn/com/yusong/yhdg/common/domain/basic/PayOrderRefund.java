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
 * Created by chen on 2017/10/28.
 */
@Getter
@Setter
public abstract class PayOrderRefund extends LongIdEntity {
    public enum BizType {
        FOREGIFT_ORDER_CUSTOMER_REFUND(1, "客户换电押金退款"),
        PACKET_PERIOD_ORDER_REFUND(2,"客户换电套餐退款"),
        INSURANCE_ORDER_REFUND(3, "保险退款"),
        RENT_FOREGIFT_ORDER_REFUND(4, "客户租电押金退款"),
        RENT_PERIOD_ORDER_REFUND(5,"客户租电套餐退款"),
        RENT_INSURANCE_ORDER_REFUND(6, "租电保险退款"),
        VEHICLE_GROUP_ORDER_REFUND(7, "租车组合订单退款"),
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

    String orderId;
    Integer partnerId;
    Integer agentId;
    String agentName; //运营商名称
    String agentCode; //运营商名称
    Integer refundMoney;
    Integer bizType;
    String bizId;
    Long customerId;
    String customerMobile;/*手机号*/
    String customerFullname;
    Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }


    public static class SourceDetail {
        public String orderId;
        public int num;
    }

    public String getBizTypeName() {
        if (bizType != null){
            return BizType.getName(bizType);
        }
        return "";
    }
}
