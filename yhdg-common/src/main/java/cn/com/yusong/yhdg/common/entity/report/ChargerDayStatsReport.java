package cn.com.yusong.yhdg.common.entity.report;

import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 运营商日统计汇总
 */
public class ChargerDayStatsReport extends PageEntity implements AreaEntity {
    String id;
    String chargerName;
    Integer agentId;
    String agentName;
    Integer estateId;
    String estateName;
    String queryBeginDate;
    String queryEndDate;
    Integer orderCount;
    Integer money;
    Integer platformIncome;
    Integer agentIncome;
    Integer estateIncome;
    Integer power;
    Integer degree;
    Integer overloadCount;
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String street;
    Double lng;
    Double lat;
    Integer pileCount;
    Integer freeCount;
    Integer busyCount;
    Integer stats;
    Integer priceId;
    String priceName;
    String provinceName;
    String cityName;
    String districtName;
    Integer icCardMoney;//在线卡收入
    Integer offlineCardMoney;//离线卡收入
    Integer cancelCount;
    Integer unregisteredCardMoney;
    Integer coinMoney;
    Date updateTime;
    Integer weixinmpCount;//微信支付人数
    Integer alipayfwCount;//支付宝支付人数
    Integer icCardCount;//在线卡支付人数
    Integer offlineCardCount;//离线卡支付人数
    Integer coinCount;//硬币支付人数
    Integer electricPrice;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getUnregisteredCardMoney() {
        return unregisteredCardMoney;
    }

    public void setUnregisteredCardMoney(Integer unregisteredCardMoney) {
        this.unregisteredCardMoney = unregisteredCardMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

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

    public Integer getEstateId() {
        return estateId;
    }

    public void setEstateId(Integer estateId) {
        this.estateId = estateId;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
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

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(Integer platformIncome) {
        this.platformIncome = platformIncome;
    }

    public Integer getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(Integer agentIncome) {
        this.agentIncome = agentIncome;
    }

    public Integer getEstateIncome() {
        return estateIncome;
    }

    public void setEstateIncome(Integer estateIncome) {
        this.estateIncome = estateIncome;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getOverloadCount() {
        return overloadCount;
    }

    public void setOverloadCount(Integer overloadCount) {
        this.overloadCount = overloadCount;
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

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getPileCount() {
        return pileCount;
    }

    public void setPileCount(Integer pileCount) {
        this.pileCount = pileCount;
    }

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }

    public Integer getBusyCount() {
        return busyCount;
    }

    public void setBusyCount(Integer busyCount) {
        this.busyCount = busyCount;
    }

    public Integer getStats() {
        return stats;
    }

    public void setStats(Integer stats) {
        this.stats = stats;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    public Integer getIcCardMoney() {
        return icCardMoney;
    }

    public void setIcCardMoney(Integer icCardMoney) {
        this.icCardMoney = icCardMoney;
    }

    public Integer getOfflineCardMoney() {
        return offlineCardMoney;
    }

    public void setOfflineCardMoney(Integer offlineCardMoney) {
        this.offlineCardMoney = offlineCardMoney;
    }

    public Integer getCoinMoney() {
        return coinMoney;
    }

    public void setCoinMoney(Integer coinMoney) {
        this.coinMoney = coinMoney;
    }

    public Integer getWeixinmpCount() {
        return weixinmpCount;
    }

    public void setWeixinmpCount(Integer weixinmpCount) {
        this.weixinmpCount = weixinmpCount;
    }

    public Integer getAlipayfwCount() {
        return alipayfwCount;
    }

    public void setAlipayfwCount(Integer alipayfwCount) {
        this.alipayfwCount = alipayfwCount;
    }

    public Integer getIcCardCount() {
        return icCardCount;
    }

    public void setIcCardCount(Integer icCardCount) {
        this.icCardCount = icCardCount;
    }

    public Integer getOfflineCardCount() {
        return offlineCardCount;
    }

    public void setOfflineCardCount(Integer offlineCardCount) {
        this.offlineCardCount = offlineCardCount;
    }

    public Integer getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(Integer coinCount) {
        this.coinCount = coinCount;
    }

    public Integer getElectricPrice() {
        return electricPrice;
    }

    public void setElectricPrice(Integer electricPrice) {
        this.electricPrice = electricPrice;
    }
}
