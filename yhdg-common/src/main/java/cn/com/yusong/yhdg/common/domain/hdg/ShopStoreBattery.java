package cn.com.yusong.yhdg.common.domain.hdg;

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
public class ShopStoreBattery extends LongIdEntity {
    public enum Category {
        EXCHANGE(1, "换电"),
        RENT(2, "租电");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Battery.Category e : Battery.Category.values()) {
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

    Integer category;/*1 换电 2 租电*/
    Integer agentId;
    String agentName;
    String agentCode;
    String shopId;
    String shopName;/*门店名称*/
    String batteryId;
    Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
