package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 包时段套餐
 */
@Setter
@Getter
public class PacketPeriodPrice extends LongIdEntity {
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
    List<PacketPeriodPriceRenew> renewList = new ArrayList<PacketPeriodPriceRenew>();
}
