package cn.com.yusong.yhdg.common.domain.zc;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 租车订单
 */
@Setter
@Getter
public class VehicleOrder extends StringIdEntity {
    public final static String RENT_ORDER_TABLE_NAME = "zd_vehicle_order_";

    public enum Status {
        RENT(1, "已租车"),
        BACK(2, "已还车");

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
    Integer modelId;/*车型id*/
    Integer vehicleId;/*车辆id*/
    String vinNo;/*车架号*/
    String vehicleName; /*车辆名称*/
    Date backTime;
    String backOperator;
    Integer status;
    Integer currentVolume;
    Integer currentDistance;
    Date createTime;

    @Transient
    String batteryTypeName;
    String modelName;

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

    public String getCreateDate(){
        if(createTime != null){
            return DateFormatUtils.format(createTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
    public String getBackDate(){
        if(backTime != null){
            return DateFormatUtils.format(backTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
}
