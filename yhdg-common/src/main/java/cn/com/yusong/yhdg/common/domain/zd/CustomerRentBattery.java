package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;


/*客户关联租电电池信息*/

@Setter
@Getter
public class CustomerRentBattery extends IntIdEntity {

    Long customerId;
    Integer agentId;
    String batteryId;/*电池编号*/
    Integer batteryType;/*电池类型*/
    String rentOrderId;
}
