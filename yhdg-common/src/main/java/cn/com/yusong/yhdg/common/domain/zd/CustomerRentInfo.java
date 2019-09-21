package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/*客户关联租电信息*/

@Getter
@Setter
public class CustomerRentInfo extends LongIdEntity {

    Integer agentId;
    Integer batteryType;
    Integer foregift;/*电池押金*/
    String foregiftOrderId;/*押金订单id*/
    String balanceShopId;
    Integer vehicleForegiftFlag; /*交过租车押金标志 1 是 0 否*/
    Date createTime;/*创建时间*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
