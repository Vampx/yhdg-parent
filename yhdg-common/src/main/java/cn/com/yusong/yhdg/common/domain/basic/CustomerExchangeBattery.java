package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/*客户关联换电电池信息*/

@Setter
@Getter
public class CustomerExchangeBattery extends IntIdEntity {

    Long customerId;
    Integer agentId;
    String batteryId;/*电池编号*/
    Integer batteryType;/*电池类型*/
    String batteryOrderId;/*电池订单id*/
    String backBatteryOrderId;/*退租电池订单*/
    Date createTime;

    String batteryCode;
}
