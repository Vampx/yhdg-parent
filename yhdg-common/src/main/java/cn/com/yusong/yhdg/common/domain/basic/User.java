package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 人员
 */
@Setter
@Getter
public class User extends LongIdEntity implements AreaEntity,Distance {
    public enum AccountType {
        PLATFORM(1, "平台用户"),
        AGENT(2, "运营商用户"),
        SHOP(3, "门店用户"),
        AGENT_COMPANY(4,"运营公司用户"),
        ESTATE(5,"物业公司用户"),
        STATION(6, "站点用户"),
        STATION_BIZ(7, "站点拓展人员用户"),
        LAXIN(10,"拉新用户"),
        ;

        private final int value;
        private final String name;

        AccountType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (AccountType e : AccountType.values()) {
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

    Integer agentId;//运营商id
    String shopId;//门店id
    Integer shopRoleId;//门店角色id
    String stationId;//站点id
    Integer stationRoleId;//站点角色id
    Long estateId; // 物业Id
    String agentCompanyId;//运营公司id
    Integer agentCompanyRoleId;//运营公司角色id
    Integer roleId;//角色id
    String nickname;
    String loginName;//登陆时间
    String password;//登陆密码
    String fullname;//全名
    String photoPath;//图像路径
    Integer gender;//性别
    Integer isActive;//是否启用
    Integer isProtected;    /*1 受保护的 0 不受保护*/
    Integer accountType; /*账号类型 1 平台用户 2 运营商用户 3 门店用户 4.运营公司用户 5 物业用户 6 站点用户 7 拓展人员*/
    String address;//地址
    String mobile;//手机号码
    String memo;//备注
    Date lastLoginTime;//最后登陆时间
    Date createTime;//最后登陆时间
    Integer pushType;
    String pushToken;
    Integer deptId;//部门id
    Integer isAdmin;//1：是；0：否
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    Integer isPush;
    @Transient
    String deptName, roleName, agentName, shopName, stationName, estateName;
    Integer shopFlag;
    Integer stationFlag;
    Integer estateFlag;
    Integer cabinetShopFlag;
    Integer stationBizUserFlag;
    String shopRoleName;
    String stationRoleName;
    String agentCompanyName;
    String agentCompanyRoleName;

    @Override
    public double getDistance() {
        return 0;
    }

    @Override
    public void setDistance(double distance) {

    }

    @Override
    public Double getLng() {
        return null;
    }

    @Override
    public Double getLat() {
        return null;
    }

    @Override
    public Integer getProvinceId() {
        return null;
    }

    @Override
    public void setProvinceId(Integer provinceId) {

    }

    @Override
    public Integer getCityId() {
        return null;
    }

    @Override
    public void setCityId(Integer cityId) {

    }

    @Override
    public Integer getDistrictId() {
        return null;
    }

    @Override
    public void setDistrictId(Integer districtId) {

    }

    @Override
    public String getStreet() {
        return null;
    }

    @Override
    public void setStreet(String street) {

    }

    @Override
    public String getProvinceName() {
        return null;
    }

    @Override
    public void setProvinceName(String provinceName) {

    }

    @Override
    public String getCityName() {
        return null;
    }

    @Override
    public void setCityName(String cityName) {

    }

    @Override
    public String getDistrictName() {
        return null;
    }

    @Override
    public void setDistrictName(String districtName) {

    }
}
