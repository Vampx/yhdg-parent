package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 续费包时段套餐
 */
@Setter
@Getter
public class PacketPeriodPriceRenew extends LongIdEntity {
    Long priceId;
    Integer agentId;        //运营商ID
    String agentName;
    String agentCode;
    Integer batteryType;
    Long foregiftId;
    Integer dayCount;
    Integer price;
    Integer limitCount;
    Integer dayLimitCount;
    String memo;
    Integer isTicket;
    Date createTime;

    @Transient
    String batteryTypeName;
}
