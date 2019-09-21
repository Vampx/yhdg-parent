package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 客户关联租车信息
 */
@Getter
@Setter
public class CustomerVehicleInfo extends LongIdEntity {

    Integer agentId;/*运营商id*/
    Integer modelId;/*车辆型号id*/
    Integer vehicleId;
    String vehicleName;/*车辆名称*/
    Integer batteryType; /*电池类型*/
    Integer foregift;/*车辆押金*/
    String foregiftOrderId;/*押金订单id*/
    Long rentPriceId;/*小套餐id*/
    Integer vipPriceId;/*vip套餐id*/
    String vehicleOrderId;/*租车订单id*/
    String balanceShopId;/*结算门店id*/
    Date createTime;/*创建时间*/
    Integer category; /*1 换电 2 租电*/

    @Transient
    String agentName;
    String shopName;
    String modelName;
    String vinNo;
    String batteryTypeName;
    String fullname;
    String mobile;
    String batteryId;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public enum Category {
        EXCHANGE(1,"换电"),
        RENT(2,"租电");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        public static List<CustomerVehicleInfo.Category> list = new ArrayList<CustomerVehicleInfo.Category>();

        static {
            for (CustomerVehicleInfo.Category e : CustomerVehicleInfo.Category.values()) {
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

    public String getCategoryName() {
        if(category != null) {
            return PriceSetting.Category.getName(category);
        }
        return "";
    }


}
