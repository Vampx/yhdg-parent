package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Setter
@Getter
public class Shop extends StringIdEntity implements AreaEntity,Distance {

    public enum Type {
        HDG_BATTERY_ORDER(1, "换电订单"),
        HDG_PACKET_PERIOD(2, "换电租金"),
        ZD_RENT_PERIOD(3, "租电租金");

        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
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

    public enum ActiveStatus {
        ENABLE(1, "启用"),
        DISABLE(2, "禁用");

        private final int value;
        private final String name;

        ActiveStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (ActiveStatus e : ActiveStatus.values()) {
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

    Integer agentId;/*运营商id*/
    String shopName;/*门店名称*/
    Integer provinceId;/*省*/
    Integer cityId;/*市*/
    Integer districtId;/*地区*/
    String street;/*街道*/
    Double lng;/*经度*/
    Double lat;/*纬度*/
    String geoHash;/*地理散列*/
    Integer activeStatus;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String workTime;
    String linkname;
    String tel;
    String address;
    String keyword;
    Integer platformRatio;
    Integer agentRatio;
    Integer provinceAgentRatio;
    Integer cityAgentRatio;
    Integer shopRatio;
    Integer shopFixedMoney;/*门店按固定金额分成*/
    Integer zcPlatformRatio;
    Integer zcAgentRatio;
    Integer zcProvinceAgentRatio;
    Integer zcCityAgentRatio;
    Integer zcShopRatio;
    Integer zcShopFixedMoney;/*门店按固定金额分成*/
    Integer  balance; /*门店余额*/
    String payPeopleMobile;
    String payPeopleName;
    String payPeopleMpOpenId;
    String payPeopleFwOpenId;
    String payPassword;
    Integer isAllowOpenBox;
    Integer totalIncome;
    Integer minPrice;
    Integer maxPrice;
    Integer isExchange;
    Integer IsRent;
    Integer isVehicle;
    Date createTime;

    @Transient
    String agentName;
    List<ShopUser> shopUserList;
    List<Cabinet> cabinetList;
    Integer carCount;
    Integer batteryCount;
    Integer vipFlag;
    @Transient
    double distance;
    @Transient
    String provinceName, cityName, districtName;
    String streetName, streetNumber;
    Long customerId;
    String businessScope;
    Integer vehicleVipFlag;



    public String getLinkname() {
        return linkname;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getPlatformRatio() {
        return platformRatio;
    }

    public void setPlatformRatio(Integer platformRatio) {
        this.platformRatio = platformRatio;
    }

    public Integer getAgentRatio() {
        return agentRatio;
    }

    public void setAgentRatio(Integer agentRatio) {
        this.agentRatio = agentRatio;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public Integer getProvinceId() {
        return provinceId;
    }

    @Override
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public Integer getCityId() {
        return cityId;
    }

    @Override
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Override
    public Integer getDistrictId() {
        return districtId;
    }

    @Override
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<ShopUser> getShopUserList() {
        return shopUserList;
    }

    public void setShopUserList(List<ShopUser> shopUserList) {
        this.shopUserList = shopUserList;
    }

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public Integer getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(Integer batteryCount) {
        this.batteryCount = batteryCount;
    }

    @Override
    public String getProvinceName() {
        return provinceName;
    }

    @Override
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String getCityName() {
        return cityName;
    }

    @Override
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String getDistrictName() {
        return districtName;
    }

    @Override
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getActiveStatusName() {
        if(activeStatus != null) {
            return Type.getName(activeStatus);
        }
        return "";
    }
}
