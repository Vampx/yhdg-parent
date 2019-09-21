package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 智租客户信息
 */
@Setter
@Getter
public class ZhizuCustomer extends LongIdEntity {

    String mobile;/*手机号*/
    String idCard;
    String realMobile;
    String realIdCard;
    Date rentTime;
    Integer batteryType;
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    Integer isActive;
    Date updateTime;
    Date createTime;
}
