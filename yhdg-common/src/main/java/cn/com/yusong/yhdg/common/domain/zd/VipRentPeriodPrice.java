package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Vip租电包时段套餐
 */
@Setter
@Getter
public class VipRentPeriodPrice extends LongIdEntity {
    Long priceId;
    Integer agentId;        //运营商ID
    String agentName;
    String agentCode;
    Integer batteryType;
    Long foregiftId;
    Integer dayCount;
    Integer price;
    String memo;
    Date createTime;


    @Transient
    String batteryTypeName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
