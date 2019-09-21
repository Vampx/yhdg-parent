package cn.com.yusong.yhdg.common.entity.report;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.PageEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 运营商日统计明细
 */
public class AgentDayStatsDetailReport extends PageEntity implements AreaEntity,Distance {
    public enum Status {
        FREE(1, "空闲"),
        BUSY(2, "繁忙"),
        FAULT(3, "故障"),
        BUILD(4, "建设中")
        ;

        private final int value;
        private final String name;

        private Status(int value, String name) {
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

    Integer agentId;
    String agentName;
    String chargerId;
    String chargerName;
    String statsDate;

    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String street;
    Double lng;
    Double lat;

    Long orderCount;
    Long customerCount;
    Long money;
    Long platformIncome;
    Long agentIncome;
    Long estateIncome;
    Long power;
    Long degree;
    Long overloadCount;
    Long icCardMoney;//在线卡收入
    Long offlineCardMoney;//离线卡收入

    Integer priceId;
    Integer estateId;
    Integer isOnline;
    String beginDate;
    String endDate;

    @Transient
    String provinceName, cityName, districtName;
    double distance;
    Integer freePoint, pointCount, busyPoint;
    String estateName;
    String priceName;
    String queryBeginDate;
    String queryEndDate;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
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

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(Long platformIncome) {
        this.platformIncome = platformIncome;
    }

    public Long getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(Long agentIncome) {
        this.agentIncome = agentIncome;
    }

    public Long getEstateIncome() {
        return estateIncome;
    }

    public void setEstateIncome(Long estateIncome) {
        this.estateIncome = estateIncome;
    }

    public Long getPower() {
        return power;
    }

    public void setPower(Long power) {
        this.power = power;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Long getOverloadCount() {
        return overloadCount;
    }

    public void setOverloadCount(Long overloadCount) {
        this.overloadCount = overloadCount;
    }

    public void setIcCardMoney(Long icCardMoney) {
        this.icCardMoney = icCardMoney;
    }

    public void setOfflineCardMoney(Long offlineCardMoney) {
        this.offlineCardMoney = offlineCardMoney;
    }

    public Long getIcCardMoney() {
        return icCardMoney;
    }

    public Long getOfflineCardMoney() {
        return offlineCardMoney;
    }

    public Integer getFreePoint() {
        return freePoint;
    }

    public void setFreePoint(Integer freePoint) {
        this.freePoint = freePoint;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public Integer getBusyPoint() {
        return busyPoint;
    }

    public void setBusyPoint(Integer busyPoint) {
        this.busyPoint = busyPoint;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Integer getEstateId() {
        return estateId;
    }

    public void setEstateId(Integer estateId) {
        this.estateId = estateId;
    }

    public String getQueryBeginDate() {
        return queryBeginDate;
    }

    public void setQueryBeginDate(String queryBeginDate) {
        this.queryBeginDate = queryBeginDate;
    }

    public String getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
