package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/*客户关联换电信息*/

@Getter
@Setter
public class CustomerExchangeInfo extends LongIdEntity {

    Integer agentId;
    Integer batteryType;
    Integer foregift;/*电池押金*/
    String foregiftOrderId;/*押金订单id*/
    String errorMessage;/*换电错误消息*/
    Date errorTime;/*换电出现错误的时间*/
    String balanceCabinetId;
    String balanceShopId;
    String balanceStationId;
    Integer vehicleForegiftFlag; /*交过租车押金标志 1 是 0 否*/
    Date createTime;/*创建时间*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getErrorTime() {
        return errorTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
