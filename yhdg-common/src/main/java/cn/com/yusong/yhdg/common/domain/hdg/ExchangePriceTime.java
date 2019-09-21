package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 换电价格按次
 */
@Setter
@Getter
public class ExchangePriceTime extends IntIdEntity {

    Integer agentId;
    Integer batteryType;
    Integer activeSingleExchange;
    Integer volumePrice;
    Integer timesPrice;

    public enum ActiveType {
        TIMES(1, "按次"),
        VOLUME(2, "按量"),
        NOT_TIMES_VOLUME(3, "不开启按次换电");

        private final int value;
        private final String name;

        ActiveType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (ActiveType e : ActiveType.values()) {
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


    @Transient
    String batteryTypeName;

    public Integer getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(Integer batteryType) {
        this.batteryType = batteryType;
    }

    public Integer getVolumePrice() {
        return volumePrice;
    }

    public void setVolumePrice(Integer volumePrice) {
        this.volumePrice = volumePrice;
    }

    public Integer getTimesPrice() {
        return timesPrice;
    }

    public void setTimesPrice(Integer timesPrice) {
        this.timesPrice = timesPrice;
    }

    public String getBatteryTypeName() {
        return batteryTypeName;
    }

    public void setBatteryTypeName(String batteryTypeName) {
        this.batteryTypeName = batteryTypeName;
    }
}
