package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ReliefStation extends LongIdEntity implements AreaEntity {

    public enum Star {
        ONE_STAR(1, "一星"),
        TWO_STAR(2, "二星"),
        THREE_STAR(3, "三星"),
        FOUR_STAR(4, "四星"),
        FIVE_STAR(5, "五星");

        private final int value;
        private final String name;

        Star(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Star e : Star.values()) {
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

    public String getStarName() {
        if (this.star != null) {
            return Star.getName(this.star);
        }
        return "";
    }

    Integer partnerId;
    String stationName; //救助站名称
    String tel;
    String imagePath;//图片路径
    String introduce;//经营介绍
    Integer provinceId; /*省份id*/
    Integer cityId; /*城市id*/
    Integer districtId; /*区id*/
    Integer star; /*星级*/
    Integer maxPrice; /*最大价格*/
    Integer minPrice; /*最小价格*/
    String street; /*街道id*/
    Double lng;
    Double lat;

    @Transient
    String provinceName, cityName, districtName;
    String streetName, streetNumber;
    String partnerName;

    @Override
    public String getProvinceName() {
        return this.provinceName;
    }

    @Override
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String getCityName() {
        return this.cityName;
    }

    @Override
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String getDistrictName() {
        return this.districtName;
    }

    @Override
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

}
