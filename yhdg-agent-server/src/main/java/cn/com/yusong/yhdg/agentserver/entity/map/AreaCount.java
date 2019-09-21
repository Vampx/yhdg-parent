package cn.com.yusong.yhdg.agentserver.entity.map;


import java.util.Comparator;

/**
 * 区域数量
 */
public class AreaCount {
    public static final Comparator<AreaCount> COMPARATOR_BY_COUNT =  new Comparator<AreaCount>() {
        @Override
        public int compare(AreaCount o1, AreaCount o2) {
            return o1.count - o2.count;
        }
    };

    int areaId;
    String areaName;
    double lng;
    double lat;
    int count;

    public AreaCount(int areaId, String areaName, double lng, double lat, int count) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.lng = lng;
        this.lat = lat;
        this.count = count;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
