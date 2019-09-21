package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
/**
vip换电电池押金
*/
@Setter
@Getter
public class VipExchangeBatteryForegift extends LongIdEntity {
    Long priceId;
    Integer agentId;
    Long foregiftId;
    Integer reduceMoney;
    String memo;
    Integer num;
    Date createTime;

    @Transient
    Integer money;
    Integer batteryType;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
