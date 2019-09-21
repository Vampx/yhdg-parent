package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 租电电池押金
 */
@Getter
@Setter
public class RentBatteryForegift extends LongIdEntity {
    private Integer agentId;
    private Integer batteryType;
    private Integer money;
    private String memo;

    @Transient
    String typeName;
    Integer reduceMoney;
    Long foregiftId;
    Long vipPriceId;
}
