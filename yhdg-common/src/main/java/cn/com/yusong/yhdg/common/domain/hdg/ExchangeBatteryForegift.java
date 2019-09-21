package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeBatteryForegift extends LongIdEntity {
    Integer agentId;
    Integer batteryType;
    Integer money;
    String memo;

    @Transient
    String typeName;//类型名称
    Integer reduceMoney;
    Long foregiftId;
    Long vipExchangeId;
    Integer indexNum;
}
