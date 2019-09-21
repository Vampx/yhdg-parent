package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 门店日收入统计
 */
@Setter
@Getter
public class ShopDayStats extends PageEntity {
    String shopId;
    String shopName;
    String statsDate; /*统计日期 格式2017-01-01*/
    Integer category;
    Integer partnerId;
    String partnerName;
    Integer agentId;
    String agentName;
    Integer money;
    Integer exchangeMoney;
    Integer packetPeriodMoney;
    Integer refundPacketPeriodMoney;
    Integer orderCount;
    Integer packetPeriodCount;
    Integer refundPacketPeriodCount;

    //加入按运营商维度分配给门店金额
    Integer agentForegiftMoney;
    Integer agentPacketPeriodMoney;
    Integer agentInsuranceMoney;
    Integer agentRefundForegiftMoney;
    Integer agentRefundPacketPeriodMoney;
    Integer agentRefundInsuranceMoney;

    Integer agentForegiftCount;
    Integer agentPacketPeriodCount;
    Integer agentInsuranceCount;
    Integer agentRefundForegiftCount;
    Integer agentRefundPacketPeriodCount;
    Integer agentRefundInsuranceCount;

    Date updateTime;

    public void init() {
        money = 0;
        exchangeMoney = 0;
        packetPeriodMoney = 0;
        refundPacketPeriodMoney = 0;
        orderCount = 0;
        packetPeriodCount = 0;
        refundPacketPeriodCount = 0;

        agentForegiftMoney = 0;
        agentPacketPeriodMoney = 0;
        agentInsuranceMoney = 0;
        agentRefundForegiftMoney = 0;
        agentRefundPacketPeriodMoney = 0;
        agentRefundInsuranceMoney = 0;

        agentForegiftCount = 0;
        agentPacketPeriodCount = 0;
        agentInsuranceCount = 0;
        agentRefundForegiftCount = 0;
        agentRefundPacketPeriodCount = 0;
        agentRefundInsuranceCount = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}