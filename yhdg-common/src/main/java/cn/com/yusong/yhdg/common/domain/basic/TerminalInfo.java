package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;

public class TerminalInfo extends LongIdEntity implements AreaEntity {

    Integer agentId;
    String terminalName;
    String terminalCode;
    Integer strategyId;
    Integer provinceId; //所在省份
    Integer cityId; //所在城市
    Integer districtId; //所在区域
    String street; //街道
    Integer keepUserId;
    Integer siteChargeId;
    Float lat;
    Float lng;

    @Transient
    String provinceName, cityName, districtName;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getKeepUserId() {
        return keepUserId;
    }

    public void setKeepUserId(Integer keepUserId) {
        this.keepUserId = keepUserId;
    }

    public Integer getSiteChargeId() {
        return siteChargeId;
    }

    public void setSiteChargeId(Integer siteChargeId) {
        this.siteChargeId = siteChargeId;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
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
}
