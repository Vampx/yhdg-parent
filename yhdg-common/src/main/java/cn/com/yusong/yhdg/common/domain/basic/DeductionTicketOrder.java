package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class DeductionTicketOrder extends LongIdEntity {
    Integer category;
    Integer agentId;
    String agentName;
    String agentCode;
    Long customerId;
    String mobile;
    String fullname;
    Integer ticketMoney;
    Integer money;
    String memo;
    Date createTime;
}