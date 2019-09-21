package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 优惠券订单
 */
@Getter
@Setter
public class CustomerCouponTicket extends LongIdEntity {

    Integer partnerId;
    Integer agentId;
    String ticketName;
    Integer money; //优惠金额 按分计
    Integer status;
    Date useTime;
    Date beginTime;
    Date expireTime;
    String customerMobile;
    String memo;
    Integer ticketType; /*优惠券类型*/
    Integer giveType; /*赠送类型*/
    Integer sourceType;
    String sourceId;
    Integer category;
    Date createTime;
    String operator; /*操作人*/

    @Transient
    String ids; //由逗号分割的Id字符串
    @Transient
    String agentName;
    String partnerName;

    public enum Category {
        EXCHANGE(1,"换电"),
        RENT(2,"租电"),
        VEHICLE(3,"租车");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        public static List<Category> list = new ArrayList<Category>();

        static {
            for (Category e :Category.values()) {
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

    public enum TicketType {
        FOREGIFT(1, "押金消费券"),
        RENT(2, "租金消费券"),
        DEDUCTION(3, "抵扣券");

        private final int value;
        private final String name;

        TicketType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (TicketType e : TicketType.values()) {
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


    public enum GiveType {
        PAY_FOREGIFT(1, "支付押金"),
        PAY_RENT(2, "支付租金"),
        PAY_AGENT(3, "运营商发放"),
        LAXIN(4, "拉新发放");

        private final int value;
        private final String name;

        GiveType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        public static List<GiveType> list = new ArrayList<GiveType>();

        static {
            for (GiveType e : GiveType.values()) {
                map.put(e.getValue(), e.getName());
                if(e.getValue()!=PAY_AGENT.getValue()){
                    list.add(e);
                }
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
        NOT_USER(1, "未使用"),
        USED(2, "已使用"),
        EXPIRED(3, "已过期");

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

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }

    public String getTicketTypeName() {
        if(ticketType != null) {
            return TicketType.getName(ticketType);
        }
        return "";
    }

    public String getGiveTypeName() {
        if(giveType != null) {
            return GiveType.getName(giveType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
