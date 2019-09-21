package cn.com.yusong.yhdg.common.domain.basic;

import lombok.Getter;
import lombok.Setter;

/*客户运营商余额*/
@Getter
@Setter
public class CustomerAgentBalance  {

    Long customerId;
    Integer agentId;
    String agentName;
    String agentCode;
    Integer agentBalance;

}
