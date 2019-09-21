package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ShopStoreVehicle extends LongIdEntity {

    public enum Category {
        EXCHANGE(1, "换电"),
        RENT(2, "租电"),
        NOT_RENT(3, "不租电");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ShopStoreVehicle.Category e : ShopStoreVehicle.Category.values()) {
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

    Integer category; /*1 换电 2 租电*/
    Integer agentId;
    String agentName;
    String agentCode;
    String shopId;
    String shopName;/*门店名称*/
    Long priceSettingId;/*租车套餐设置id*/
    Integer vehicleId;/*车辆id*/
    String vinNo;/*车架号*/
    Integer batteryCount;/*电池数*/
    Date createTime;

    @Transient
    String settingName;
    Integer modelId;
    String modelName;
    String vehicleName;
    String batteryCategory;
    String batteryTypeName;
    String[] ids; //修改时电池id

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getCategoryName() {
        if (category != null) {
            return ShopStoreVehicle.Category.getName(category);
        }
        return "";
    }
}
