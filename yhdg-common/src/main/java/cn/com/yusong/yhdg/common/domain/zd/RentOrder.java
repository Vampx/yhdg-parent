package cn.com.yusong.yhdg.common.domain.zd;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 租电订单
 */
@Setter
@Getter
public class RentOrder extends StringIdEntity {
    public final static String RENT_ORDER_TABLE_NAME = "zd_rent_order_";

    public enum Status {
        RENT(2, "已租电"),
        BACK(1, "已还电");

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
    String agentName;
    String agentCode;
    String shopId;
    String shopName;
    Long customerId;
    String customerMobile;
    String customerFullname;
    Integer batteryType;
    String batteryId;
    Date backTime;
    String backOperator;
    Integer status;
    Integer currentVolume;
    Integer currentDistance;
    Date createTime;
    Integer vehicleOrderFlag;  /*是否是通过租车流程产生的订单 1 是 0 否*/
    @Transient
    String batteryTypeName;

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBackTime() {
        return backTime;
    }
}
