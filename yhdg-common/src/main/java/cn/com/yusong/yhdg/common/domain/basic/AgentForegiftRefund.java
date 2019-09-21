package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AgentForegiftRefund extends LongIdEntity {
    private Integer agentId;
    private String agentName;
    private String agentCode;
    private Long customerId;
    private String mobile;
    private String fullname;
    private String foregiftOrderId;
    private Long refundRecordId;
    private Integer price;
    private Integer ticketMoney;
    private Integer deductionTicketMoney;
    private Integer payMoney;
    private Integer refundMoney;
    private Integer remainMoney;
    private String operatorName;
    private Date createTime;

}