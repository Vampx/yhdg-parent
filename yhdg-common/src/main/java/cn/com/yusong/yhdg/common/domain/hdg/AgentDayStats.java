package cn.com.yusong.yhdg.common.domain.hdg;
import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *运营商日收入表
 */
@Setter
@Getter
public class AgentDayStats extends PageEntity {
    Integer partnerId;
    String partnerName;
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;
    String statsDate; /*统计日期 格式2017-01-01*/
    Integer category;
    
    Integer income; /*/*当日新增运营收入+平台收入+省代收入+省代收入=当日总金额*/
    Integer money;/*当日总金额*/
    Integer orderCount;/*订单次数*/

    Integer exchangeMoney; /*当日换电金额(分成前) 按分计算*/
    Integer packetPeriodMoney; /*当日包时段订单(分成前) 按分计算*/
    Integer refundExchangeMoney; /*当日退款换电金额(分成前) 按分计算*/
    Integer refundPacketPeriodMoney;/*退款包时段订单金额(分成前) 按分计算*/

    Integer agentExchangeMoney; /*当日换电金额(分成后) 按分计算(按次)*/
    Integer agentPacketPeriodMoney; /*当日包时段订单(分成后) 按分计算(租金)*/
    Integer agentRefundExchangeMoney; /*当日退款换电金额(分成后) 按分计算*/
    Integer agentRefundPacketPeriodMoney;/*退款包时段订单金额(分成后) 按分计算*/

    Integer shopMoney; /*当日门店总金额*/
    Integer shopExchangeMoney; /*当日门店按次金额*/
    Integer shopPacketPeriodMoney; /*当日门店包时段订单金额*/
    Integer shopRefundPacketPeriodMoney;/*当日门店包时段退款订单金额*/

    Integer agentCompanyMoney; /*当日门店总金额*/
    Integer agentCompanyExchangeMoney; /*当日门店按次金额*/
    Integer agentCompanyPacketPeriodMoney; /*当日门店包时段订单金额*/
    Integer agentCompanyRefundPacketPeriodMoney;/*当日门店包时段退款订单金额*/

    Integer exchangeCount; /*当日新增换电订单数（换电次数）*/
    Integer packetPeriodCount; /*当日新增包时段订单数*/
    Integer refundExchangeCount; /*当日新增退款换电订单数*/
    Integer packetPeriodOrderCount; /*购买包时段订单次数*/
    Integer refundPacketPeriodOrderCount; /*当日新增退款包时段订单次数*/

    Integer platformIncome; /*平台收入*/
    Integer provinceIncome;/*省代收入*/
    Integer cityIncome;/*市代收入*/
    Integer foregiftRemainMoney;
    Integer deductionTicketMoney;
    Integer laxinPayMoney;

    Integer foregiftMoney;
    Integer foregiftCount;
    Integer foregiftRefundMoney;
    Integer foregiftRefundCount;

    Integer insuranceMoney;
    Integer insuranceCount;
    Integer insuranceRefundMoney;
    Integer insuranceRefundCount;

    Integer electricDegree;
    Integer electricPrice;/*（电费）*/

    Integer cabinetCount;/*设备数量*/
    Integer batteryCount; /*电池数量*/

    Integer activeCustomerCount; /*活跃客户数（换电人数）*/
    Date updateTime;


    @Transient
    String suffix;
    Integer orgType;/*1 平台 2 运营商 3 省代 4 市代 */
    Integer cabinetForegiftMoney;/*设备押金金额*/
    Integer cabinetRentMoney;/*设备租金金额*/
    Integer batteryRentMoney;/*电池租金金额*/
    Integer idCardAuthMoney;/*客户认证金额*/

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

        activeCustomerCount = 0;

        foregiftMoney = 0;
        foregiftCount = 0;
        foregiftRefundMoney = 0;
        foregiftRefundCount = 0;

        insuranceMoney = 0;
        insuranceCount = 0;
        insuranceRefundMoney = 0;
        insuranceRefundCount = 0;

        electricDegree = 0;
        electricPrice = 0;

        cabinetCount = 0;
        batteryCount = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}