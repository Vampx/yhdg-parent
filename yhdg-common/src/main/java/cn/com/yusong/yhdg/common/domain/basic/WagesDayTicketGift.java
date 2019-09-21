package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 工资日优惠配置类
 */
@Getter
@Setter
public class WagesDayTicketGift extends LongIdEntity {
    Integer agentId;    /*运营商Id*/
    Integer category;
    Long ticketGiftId; /*优惠券配置*/
    String customerMobile;   /*客户手机*/
    @Transient
    String agentName;
    @Transient
    String newType;


}
