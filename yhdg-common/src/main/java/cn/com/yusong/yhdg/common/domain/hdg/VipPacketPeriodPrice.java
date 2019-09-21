package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Vip换电包时段套餐
 */
@Setter
@Getter
public class VipPacketPeriodPrice extends LongIdEntity {
    Long vipForegiftId;
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
    Integer isTicket;
    String memo;
    Date createTime;


    @Transient
    String batteryTypeName;
    List<VipPacketPeriodPriceRenew> renewList = new ArrayList<VipPacketPeriodPriceRenew>();

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
