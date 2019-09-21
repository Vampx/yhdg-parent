package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 客户购买押金租金退电池退押金租金消费轨迹
 *
 */
@Getter
@Setter
public class CustomerPayTrack extends LongIdEntity {

    Integer agentId;
    Long customerId;                //客户id
    String customerMobile;
    String customerFullname;
    String memo;
    Integer trackType;
    Date createTime;

    @Transient
    String agentName;

    public enum TrackType {
        FOREGIFT_RENT(1, "买押金租金"),
        RENT(2, "续租"),
        BACK_BATTERY(3, "退电池"),
        BACK_FOREGIFT(4, "退押金"),
        BACK_RENT(5, "退租金"),
        BACK_MULTI(6, "退多通道"),
        BACK_VEHICLE_GROUP(7, "退租车组合订单"),;

        private final int value;
        private final String name;

        TrackType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (TrackType e : TrackType.values()) {
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


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
