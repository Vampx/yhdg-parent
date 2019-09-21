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
 * 通知消息
 */
@Getter
@Setter
public class CustomerNoticeMessage extends LongIdEntity {

    public enum Type {
        NOTICE(1, "通知"),
        CUSTOMER(2, "客户消息"),
        EXCHANGE_BATTERY(3, "换电"),
        RENT_VEHICLE(4, "租车"),
        PACKET_PERIOD_ORDER_EXPIRE(5, "换电租期到期"),
        CUSTOMER_INSTALLMENT_EXPIRE(6, "分期付到期"),
        CUSTOMER_COUPON_TICKET_EXPIRE(7, "优惠券到期");


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

    public String getTypeName() {
        if(type != null) {
            return CustomerNoticeMessage.Type.getName(type);
        }
        return "";
    }

    Integer type;
    String title;
    String content;
    Long customerId;
    String customerMobile;
    String customerFullname;
    Date receiveTime;
    Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
