package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备位置纠错
 */
@Setter
@Getter
public class ShopAddressCorrection extends LongIdEntity implements AreaEntity {

    Integer agentId;
    String shopId;
    String shopName;
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String street;
    Double lng;
    Double lat;
    String memo;
    Long customerId;
    String customerMobile;
    String customerFullname;
    Integer status;                 /*1 未审核 2 审核通过 3 审核不通过*/
    Date createTime;

    @Transient
    String provinceName, cityName, districtName;

    public enum Status {
        AUDIT_NO(1, "未审核"),
        AUDIT_PASS(2, "审核通过"),
        AUDIT_REJECT(3, "审核不通过"),
        ;

        private final int value;
        private final String name;

        Status(int value, String name) {
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

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getAddress() {
        return StringUtils.trimToEmpty(getProvinceName()) + StringUtils.trimToEmpty(getCityName()) + StringUtils.trimToEmpty(getDistrictName());
    }
}
