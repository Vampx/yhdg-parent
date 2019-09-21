package cn.com.yusong.yhdg.common.domain.hdg;



import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *运营商月收入表
 */
@Setter
@Getter
public class AgentMonthStats extends PageEntity {
    Integer partnerId;
    String partnerName;
    String statsMonth; /*统计日期 格式2017-01*/
    Integer category;

    Integer agentId;
    String agentCode;
    String agentName;

    Integer income; /*/*当月新增运营收入+平台收入+省代收入+省代收入=当日总金额*/
    Integer money;/*当月总金额*/
    Integer orderCount;/*订单次数*/

    Integer exchangeMoney; /*当月换电金额(分成前) 按分计算*/
    Integer packetPeriodMoney; /*当月包时段订单(分成前) 按分计算*/
    Integer refundExchangeMoney; /*当月退款换电金额(分成前) 按分计算*/
    Integer refundPacketPeriodMoney;/*退款包时段订单金额(分成前) 按分计算*/

    Integer agentExchangeMoney; /*当月换电金额(分成后) 按分计算*/
    Integer agentPacketPeriodMoney; /*当月包时段订单(分成后) 按分计算*/
    Integer agentRefundExchangeMoney; /*当月退款换电金额(分成后) 按分计算*/
    Integer agentRefundPacketPeriodMoney;/*退款包时段订单金额(分成后) 按分计算*/

    Integer shopMoney; /*当月门店总金额*/
    Integer shopExchangeMoney; /*当月门店按次金额*/
    Integer shopPacketPeriodMoney; /*当月门店包时段订单金额*/
    Integer shopRefundPacketPeriodMoney;/*当月门店包时段退款订单金额*/

    Integer agentCompanyMoney; /*当月运营公司总金额*/
    Integer agentCompanyExchangeMoney; /*当月运营公司按次金额*/
    Integer agentCompanyPacketPeriodMoney; /*当月运营公司包时段订单金额*/
    Integer agentCompanyRefundPacketPeriodMoney;/*当月运营公司包时段退款订单金额*/

    Integer exchangeCount; /*当月新增换电订单数*/
    Integer packetPeriodCount; /*当月新增包时段订单数*/
    Integer refundExchangeCount; /*当月新增退款换电订单数*/
    Integer packetPeriodOrderCount; /*购买包时段订单次数*/
    Integer refundPacketPeriodOrderCount; /*当月新增退款包时段订单次数*/

    Integer platformIncome; /*平台收入*/
    Integer provinceIncome;/*省代收入*/
    Integer cityIncome;/*市代收入*/
    Integer foregiftRemainMoney;
    Integer deductionTicketMoney;

    Integer laxinPayMoney;
    Integer cabinetForegiftMoney;
    Integer cabinetRentMoney;
    Integer batteryRentMoney;
    Integer idCardAuthMoney;


    Integer foregiftMoney;
    Integer foregiftCount;
    Integer foregiftRefundMoney;
    Integer foregiftRefundCount;

    Integer insuranceMoney;
    Integer insuranceCount;
    Integer insuranceRefundMoney;
    Integer insuranceRefundCount;

    Integer electricDegree;
    Integer electricPrice;

    Integer cabinetCount; /*设备数量*/
    Integer batteryCount; /*电池数量*/

    Integer activeCustomerCount; /*活跃客户数*/
    Date updateTime;

    public void init() {
        income = 0;
        money = 0;
        orderCount = 0;

        exchangeMoney = 0;
        packetPeriodMoney = 0;
        refundExchangeMoney = 0;
        refundPacketPeriodMoney = 0;

        agentExchangeMoney = 0;
        agentPacketPeriodMoney = 0;
        agentRefundExchangeMoney = 0;
        agentRefundPacketPeriodMoney = 0;

        shopMoney = 0;
        shopExchangeMoney = 0;
        shopPacketPeriodMoney = 0;
        shopRefundPacketPeriodMoney = 0;

        agentCompanyMoney = 0;
        agentCompanyExchangeMoney = 0;
        agentCompanyPacketPeriodMoney = 0;
        agentCompanyRefundPacketPeriodMoney = 0;

        exchangeCount = 0;
        packetPeriodCount = 0;
        refundExchangeCount = 0;
        packetPeriodOrderCount = 0;
        refundPacketPeriodOrderCount = 0;

        platformIncome = 0;
        provinceIncome = 0;
        cityIncome = 0;
        foregiftRemainMoney = 0;
        deductionTicketMoney = 0;

        laxinPayMoney= 0;
        cabinetForegiftMoney= 0;
        cabinetRentMoney= 0;
        batteryRentMoney= 0;
        idCardAuthMoney= 0;

        foregiftMoney = 0;
        foregiftCount = 0;
        foregiftRefundMoney = 0;
        foregiftRefundCount = 0;

        insuranceMoney = 0;
        insuranceCount = 0;
        insuranceRefundMoney = 0;
        insuranceRefundCount = 0;

        activeCustomerCount = 0;
        cabinetCount = 0;
        batteryCount = 0;

        electricDegree = 0;
        electricPrice = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}