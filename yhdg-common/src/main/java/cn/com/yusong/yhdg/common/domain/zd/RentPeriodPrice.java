package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 租电包时段套餐
 */
@Getter
@Setter
public class RentPeriodPrice extends LongIdEntity {
    private Integer agentId;
    private String agentName;
    private String agentCode;
    private Integer batteryType;
    private Long foregiftId;
    private Integer dayCount;
    private Integer price;
    private String memo;
    private Date createTime;

    @Transient
    private String batteryTypeName;
}
