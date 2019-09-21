package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;

import java.util.Comparator;

/**
 * 地区
 */
public class Area extends IntIdEntity {

    public static final Comparator<Area> COMPARATOR_BY_AREA_CODE =  new Comparator<Area>() {
        @Override
        public int compare(Area o1, Area o2) {
            return o1.getAreaCode().compareTo(o2.getAreaCode());
        }
    };

    private Integer areaLevel; //区域级别 1 省份 2 城市 3 区
    private String areaCode; //区域代码6位
    private String areaName; //区域名称
    private Integer parentId; //上级
    private String letter;
    private Integer baiduId;

    private Double longitude;//经度

    private Double latitude;//纬度

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getBaiduId() {
        return baiduId;
    }

    public void setBaiduId(Integer baiduId) {
        this.baiduId = baiduId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
