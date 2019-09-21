package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 租电vip套餐
 */
@Setter
@Getter
public class VipRentPrice extends LongIdEntity {
    Integer agentId;        //运营商ID
    Integer batteryType;
    String priceName;
    Date beginTime;
    Date endTime;
    Integer customerCount;
    Integer shopCount;
    Integer agentCompanyCount;
    Integer isActive;
    Date createTime;


    @Transient
    String agentName, batteryTypeName;
    Long foregiftId;
    Integer reduceMoney;/*减免金额*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
