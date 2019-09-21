package cn.com.yusong.yhdg.common.domain;

public interface AreaEntity {
    public Integer getProvinceId();
    public void setProvinceId(Integer provinceId);

    public Integer getCityId();
    public void setCityId(Integer cityId);

    public Integer getDistrictId();
    public void setDistrictId(Integer districtId);

    public String getStreet();
    public void setStreet(String street);

    public String getProvinceName();
    public void setProvinceName(String provinceName);

    public String getCityName();
    public void setCityName(String cityName);

    public String getDistrictName();
    public void setDistrictName(String districtName);
}
