package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 换电柜日收入统计
 */
@Setter
@Getter
public class CabinetDayStats extends PageEntity {

    String cabinetId; //换电柜id
    String cabinetName;
    String statsDate; /*统计日期 格式2017-01-01*/
    Integer agentId;
    String agentName;

    Integer money;
    Integer foregiftMoney;
    Integer refundForegiftMoney;
    Integer packetPeriodMoney;
    Integer refundPacketPeriodMoney;/*新加*/
    Integer exchangeMoney; /*当日新增换电金额(单次) 按分计算*/
    Integer insuranceMoney;
    Integer refundInsuranceMoney;

    Integer agentMoney;
    Integer agentPacketPeriodMoney;
    Integer agentExchangeMoney;
    Integer agentRefundPacketPeriodMoney;

    Integer shopMoney;
    Integer shopPacketPeriodMoney;
    Integer shopExchangeMoney;
    Integer shopRefundPacketPeriodMoney;

    Integer agentCompanyMoney;
    Integer agentCompanyPacketPeriodMoney;
    Integer agentCompanyExchangeMoney;
    Integer agentCompanyRefundPacketPeriodMoney;

    Integer foregiftCount;
    Integer refundForegiftCount;
    Integer packetPeriodCount;
    Integer refundPacketPeriodCount;/*新加*/
    Integer orderCount;
    Integer insuranceCount;
    Integer refundInsuranceCount;
    Integer activeCustomerCount; /*活跃客户数*/

    Integer electricDegree;
    Integer unitPrice;
    Integer electricPrice;
    Date updateTime;

    Double price;

    public void init() {
        money = 0;
        foregiftMoney = 0;
        refundForegiftMoney = 0;
        packetPeriodMoney = 0;
        refundPacketPeriodMoney= 0;
        exchangeMoney = 0; /*当日新增换电金额(单次) 按分计算*/
        insuranceMoney = 0;
        refundInsuranceMoney = 0;

        agentMoney = 0;
        agentPacketPeriodMoney= 0;
        agentExchangeMoney= 0;
        agentRefundPacketPeriodMoney= 0;

        shopMoney = 0;
        shopPacketPeriodMoney= 0;
        shopExchangeMoney= 0;
        shopRefundPacketPeriodMoney= 0;

        agentCompanyMoney = 0;
        agentCompanyPacketPeriodMoney = 0;
        agentCompanyExchangeMoney = 0;
        agentCompanyRefundPacketPeriodMoney = 0;

        foregiftCount = 0;
        refundForegiftCount = 0;
        packetPeriodCount = 0;
        refundPacketPeriodCount = 0;
        orderCount = 0;
        insuranceCount =0;
        refundInsuranceCount = 0;
        activeCustomerCount = 0;

        electricDegree = 0;
        unitPrice = 0;
        electricPrice = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}