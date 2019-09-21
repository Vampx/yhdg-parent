package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 平台日统计
 */

@Getter
@Setter
public class PlatformDayStats extends PageEntity {

    String statsDate; /*统计日期 格式2017-01-01*/

    Long totalPlatformIncome; /*总收入(----------------------------取日统计数据------------------------------------)*/
    Long incrementPlatformIncome; /*当日新增收入(----------------------------取日统计数据------------------------------------)*/

    Long totalForegiftMoney; /*有效总押金 按分计算(----------------------------单独统计已统计------------------------------------)*/
    Long incrementForegiftMoney; /*当日新增押金 按分计算(----------------------------单独统计已统计------------------------------------)*/
    Long totalExchangeMoney; /*总换电金额(单次) 按分计算(----------------------------取日统计数据------------------------------------)*/
    Long incrementExchangeMoney; /*当日新增换电金额(单次) 按分计算*/
    Long totalPacketPeriodMoney; /*总包时段订单(单次) 按分计算(----------------------------取日统计数据------------------------------------)*/
    Long incrementPacketPeriodMoney; /*当日新增包时段订单(单次) 按分计算*/
    Long totalDepositMoney;/*总充值金额(----------------------------单独统计已统计------------------------------------)*/
    Long incrementDepositMoney;/*当日新增充值金额(----------------------------单独统计已统计------------------------------------)*/

    Long totalDepositCount;/*总充值次数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementDepositCount;/*当日新增充值次数(----------------------------单独统计已统计------------------------------------)*/
    Long totalExchangeCount; /*当日总换电订单数(----------------------------取日统计数据------------------------------------)*/
    Long incrementExchangeCount; /*当日新增换电订单数*/
    Long totalForegiftCount; /*当日总押金订单数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementForegiftCount; /*当日新增押金订单数(----------------------------单独统计已统计------------------------------------)*/

    Long totalRefundMoney; /*总退款金额(----------------------------单独统计已统计------------------------------------)*/
    Long incrementRefundMoney; /*当日新增退款金额*/

    Long totalRefundForegiftMoney; /*总退款押金 按分计算(----------------------------单独统计已统计------------------------------------)*/
    Long incrementRefundForegiftMoney; /*当日新增退款押金 按分计算(----------------------------单独统计已统计------------------------------------)*/
    Long totalRefundExchangeMoney; /*总退款换电金额(单次) 按分计算(----------------------------取日统计数据------------------------------------)*/
    Long incrementRefundExchangeMoney; /*当日新增退款换电金额(单次) 按分计算*/
    Long totalRefundPacketPeriodMoney; /*总包时段订单(单次)退款 按分计算(----------------------------取日统计数据------------------------------------)*/
    Long incrementRefundPacketPeriodMoney; /*当日新增包时段订单(单次)退款 按分计算*/
    Long totalRefundDepositMoney;/*总充值退款金额(----------------------------单独统计已统计------------------------------------)*/
    Long incrementRefundDepositMoney;/*当日新增充值退款金额(----------------------------单独统计已统计------------------------------------)*/

    Long totalRefundDepositCount;/*总退款充值次数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementRefundDepositCount;/*当日新增退款充值次数(----------------------------单独统计已统计------------------------------------)*/
    Long totalRefundExchangeCount; /*当日总退款换电订单数(----------------------------取日统计数据------------------------------------)*/
    Long incrementRefundExchangeCount; /*当日新增退款换电订单数*/
    Long totalRefundForegiftCount; /*当日总退款押金订单数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementRefundForegiftCount; /*当日新增退款押金订单数(----------------------------单独统计已统计------------------------------------)*/

    Long totalCabinetCount; /*总设备数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementCabinetCount; /*当日新增设备数(----------------------------单独统计已统计------------------------------------)*/

    Long totalCustomerCount; /*客户总数(----------------------------单独统计已统计------------------------------------)*/
    Long incrementCustomerCount; /*当日新增客户数量(----------------------------单独统计已统计------------------------------------)*/

    Long totalFeedbackCount; /*总建议数量(----------------------------单独统计已统计------------------------------------)*/
    Long incrementFeedbackCount; /*当日建议数量(----------------------------单独统计已统计------------------------------------)*/

    Long agentIncome; /*当日 运营商总收入*/

    Long notUseCount; /*当日 未使用人数统计(----------------------------单独统计已统计------------------------------------)*/

    Date updateTime;/*更新时间*/


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }
}