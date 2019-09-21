package cn.com.yusong.yhdg.common.domain.hdg;



import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *平台月统计
 */
public class PlatformMonthStats extends PageEntity {

    String statsMonth; /*统计日期 格式2017-01*/

    Long totalPlatformIncome; /*总收入*/
    Long incrementPlatformIncome; /*当日新增收入*/

    Long totalForegiftMoney; /*有效总押金 按分计算*/
    Long incrementForegiftMoney; /*当日新增押金 按分计算*/
    Long totalExchangeMoney; /*总换电金额(单次) 按分计算*/
    Long incrementExchangeMoney; /*当日新增换电金额(单次) 按分计算*/
    Long totalPacketPeriodMoney; /*总包时段订单(单次) 按分计算*/
    Long incrementPacketPeriodMoney; /*当日新增包时段订单(单次) 按分计算*/
    Long totalDepositMoney;/*总充值金额*/
    Long incrementDepositMoney;/*当日新增充值金额*/

    Long totalDepositCount;/*总充值次数*/
    Long incrementDepositCount;/*当日新增充值次数*/
    Long totalExchangeCount; /*当日总换电订单数*/
    Long incrementExchangeCount; /*当日新增换电订单数*/
    Long totalForegiftCount; /*当日总押金订单数*/
    Long incrementForegiftCount; /*当日新增押金订单数*/

    Long totalRefundMoney; /*总退款金额*/
    Long incrementRefundMoney; /*当日新增退款金额*/

    Long totalRefundForegiftMoney; /*总退款押金 按分计算*/
    Long incrementRefundForegiftMoney; /*当日新增退款押金 按分计算*/
    Long totalRefundExchangeMoney; /*总退款换电金额(单次) 按分计算*/
    Long incrementRefundExchangeMoney; /*当日新增退款换电金额(单次) 按分计算*/
    Long totalRefundPacketPeriodMoney; /*总包时段订单(单次)退款 按分计算*/
    Long incrementRefundPacketPeriodMoney; /*当日新增包时段订单(单次)退款 按分计算*/
    Long totalRefundDepositMoney;/*总充值退款金额*/
    Long incrementRefundDepositMoney;/*当日新增充值退款金额*/

    Long totalRefundDepositCount;/*总退款充值次数*/
    Long incrementRefundDepositCount;/*当日新增退款充值次数*/
    Long totalRefundExchangeCount; /*当日总退款换电订单数*/
    Long incrementRefundExchangeCount; /*当日新增退款换电订单数*/
    Long totalRefundForegiftCount; /*当日总退款押金订单数*/
    Long incrementRefundForegiftCount; /*当日新增退款押金订单数*/

    Long totalCabinetCount; /*总设备数*/
    Long incrementCabinetCount; /*当日新增设备数*/

    Long totalCustomerCount; /*客户总数*/
    Long incrementCustomerCount; /*当日新增客户数量*/

    Long totalFeedbackCount; /*总建议数量*/
    Long incrementFeedbackCount; /*当日建议数量*/

    Long agentIncome; /*当日 运营商总收入*/

    Long notUseCount; /*当日 未使用人数统计*/

    Date updateTime;

    public String getStatsMonth() {
        return statsMonth;
    }

    public void setStatsMonth(String statsMonth) {
        this.statsMonth = statsMonth;
    }

    public Long getTotalPlatformIncome() {
        return totalPlatformIncome;
    }

    public void setTotalPlatformIncome(Long totalPlatformIncome) {
        this.totalPlatformIncome = totalPlatformIncome;
    }

    public Long getIncrementPlatformIncome() {
        return incrementPlatformIncome;
    }

    public void setIncrementPlatformIncome(Long incrementPlatformIncome) {
        this.incrementPlatformIncome = incrementPlatformIncome;
    }

    public Long getTotalForegiftMoney() {
        return totalForegiftMoney;
    }

    public void setTotalForegiftMoney(Long totalForegiftMoney) {
        this.totalForegiftMoney = totalForegiftMoney;
    }

    public Long getIncrementForegiftMoney() {
        return incrementForegiftMoney;
    }

    public void setIncrementForegiftMoney(Long incrementForegiftMoney) {
        this.incrementForegiftMoney = incrementForegiftMoney;
    }

    public Long getTotalExchangeMoney() {
        return totalExchangeMoney;
    }

    public void setTotalExchangeMoney(Long totalExchangeMoney) {
        this.totalExchangeMoney = totalExchangeMoney;
    }

    public Long getIncrementExchangeMoney() {
        return incrementExchangeMoney;
    }

    public void setIncrementExchangeMoney(Long incrementExchangeMoney) {
        this.incrementExchangeMoney = incrementExchangeMoney;
    }

    public Long getTotalPacketPeriodMoney() {
        return totalPacketPeriodMoney;
    }

    public void setTotalPacketPeriodMoney(Long totalPacketPeriodMoney) {
        this.totalPacketPeriodMoney = totalPacketPeriodMoney;
    }

    public Long getIncrementPacketPeriodMoney() {
        return incrementPacketPeriodMoney;
    }

    public void setIncrementPacketPeriodMoney(Long incrementPacketPeriodMoney) {
        this.incrementPacketPeriodMoney = incrementPacketPeriodMoney;
    }

    public Long getTotalDepositMoney() {
        return totalDepositMoney;
    }

    public void setTotalDepositMoney(Long totalDepositMoney) {
        this.totalDepositMoney = totalDepositMoney;
    }

    public Long getIncrementDepositMoney() {
        return incrementDepositMoney;
    }

    public void setIncrementDepositMoney(Long incrementDepositMoney) {
        this.incrementDepositMoney = incrementDepositMoney;
    }

    public Long getTotalDepositCount() {
        return totalDepositCount;
    }

    public void setTotalDepositCount(Long totalDepositCount) {
        this.totalDepositCount = totalDepositCount;
    }

    public Long getIncrementDepositCount() {
        return incrementDepositCount;
    }

    public void setIncrementDepositCount(Long incrementDepositCount) {
        this.incrementDepositCount = incrementDepositCount;
    }

    public Long getTotalExchangeCount() {
        return totalExchangeCount;
    }

    public void setTotalExchangeCount(Long totalExchangeCount) {
        this.totalExchangeCount = totalExchangeCount;
    }

    public Long getIncrementExchangeCount() {
        return incrementExchangeCount;
    }

    public void setIncrementExchangeCount(Long incrementExchangeCount) {
        this.incrementExchangeCount = incrementExchangeCount;
    }

    public Long getTotalRefundExchangeCount() {
        return totalRefundExchangeCount;
    }

    public void setTotalRefundExchangeCount(Long totalRefundExchangeCount) {
        this.totalRefundExchangeCount = totalRefundExchangeCount;
    }

    public Long getIncrementRefundExchangeCount() {
        return incrementRefundExchangeCount;
    }

    public void setIncrementRefundExchangeCount(Long incrementRefundExchangeCount) {
        this.incrementRefundExchangeCount = incrementRefundExchangeCount;
    }

    public Long getTotalForegiftCount() {
        return totalForegiftCount;
    }

    public void setTotalForegiftCount(Long totalForegiftCount) {
        this.totalForegiftCount = totalForegiftCount;
    }

    public Long getIncrementForegiftCount() {
        return incrementForegiftCount;
    }

    public void setIncrementForegiftCount(Long incrementForegiftCount) {
        this.incrementForegiftCount = incrementForegiftCount;
    }

    public Long getTotalRefundMoney() {
        return totalRefundMoney;
    }

    public void setTotalRefundMoney(Long totalRefundMoney) {
        this.totalRefundMoney = totalRefundMoney;
    }

    public Long getIncrementRefundMoney() {
        return incrementRefundMoney;
    }

    public void setIncrementRefundMoney(Long incrementRefundMoney) {
        this.incrementRefundMoney = incrementRefundMoney;
    }

    public Long getTotalRefundForegiftMoney() {
        return totalRefundForegiftMoney;
    }

    public void setTotalRefundForegiftMoney(Long totalRefundForegiftMoney) {
        this.totalRefundForegiftMoney = totalRefundForegiftMoney;
    }

    public Long getIncrementRefundForegiftMoney() {
        return incrementRefundForegiftMoney;
    }

    public void setIncrementRefundForegiftMoney(Long incrementRefundForegiftMoney) {
        this.incrementRefundForegiftMoney = incrementRefundForegiftMoney;
    }

    public Long getTotalRefundExchangeMoney() {
        return totalRefundExchangeMoney;
    }

    public void setTotalRefundExchangeMoney(Long totalRefundExchangeMoney) {
        this.totalRefundExchangeMoney = totalRefundExchangeMoney;
    }

    public Long getIncrementRefundExchangeMoney() {
        return incrementRefundExchangeMoney;
    }

    public void setIncrementRefundExchangeMoney(Long incrementRefundExchangeMoney) {
        this.incrementRefundExchangeMoney = incrementRefundExchangeMoney;
    }

    public Long getTotalRefundPacketPeriodMoney() {
        return totalRefundPacketPeriodMoney;
    }

    public void setTotalRefundPacketPeriodMoney(Long totalRefundPacketPeriodMoney) {
        this.totalRefundPacketPeriodMoney = totalRefundPacketPeriodMoney;
    }

    public Long getIncrementRefundPacketPeriodMoney() {
        return incrementRefundPacketPeriodMoney;
    }

    public void setIncrementRefundPacketPeriodMoney(Long incrementRefundPacketPeriodMoney) {
        this.incrementRefundPacketPeriodMoney = incrementRefundPacketPeriodMoney;
    }

    public Long getTotalRefundDepositMoney() {
        return totalRefundDepositMoney;
    }

    public void setTotalRefundDepositMoney(Long totalRefundDepositMoney) {
        this.totalRefundDepositMoney = totalRefundDepositMoney;
    }

    public Long getIncrementRefundDepositMoney() {
        return incrementRefundDepositMoney;
    }

    public void setIncrementRefundDepositMoney(Long incrementRefundDepositMoney) {
        this.incrementRefundDepositMoney = incrementRefundDepositMoney;
    }

    public Long getTotalRefundDepositCount() {
        return totalRefundDepositCount;
    }

    public void setTotalRefundDepositCount(Long totalRefundDepositCount) {
        this.totalRefundDepositCount = totalRefundDepositCount;
    }

    public Long getIncrementRefundDepositCount() {
        return incrementRefundDepositCount;
    }

    public void setIncrementRefundDepositCount(Long incrementRefundDepositCount) {
        this.incrementRefundDepositCount = incrementRefundDepositCount;
    }

    public Long getTotalRefundForegiftCount() {
        return totalRefundForegiftCount;
    }

    public void setTotalRefundForegiftCount(Long totalRefundForegiftCount) {
        this.totalRefundForegiftCount = totalRefundForegiftCount;
    }

    public Long getIncrementRefundForegiftCount() {
        return incrementRefundForegiftCount;
    }

    public void setIncrementRefundForegiftCount(Long incrementRefundForegiftCount) {
        this.incrementRefundForegiftCount = incrementRefundForegiftCount;
    }

    public Long getTotalCabinetCount() {
        return totalCabinetCount;
    }

    public void setTotalCabinetCount(Long totalCabinetCount) {
        this.totalCabinetCount = totalCabinetCount;
    }

    public Long getIncrementCabinetCount() {
        return incrementCabinetCount;
    }

    public void setIncrementCabinetCount(Long incrementCabinetCount) {
        this.incrementCabinetCount = incrementCabinetCount;
    }

    public Long getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomerCount(Long totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public Long getIncrementCustomerCount() {
        return incrementCustomerCount;
    }

    public void setIncrementCustomerCount(Long incrementCustomerCount) {
        this.incrementCustomerCount = incrementCustomerCount;
    }

    public Long getTotalFeedbackCount() {
        return totalFeedbackCount;
    }

    public void setTotalFeedbackCount(Long totalFeedbackCount) {
        this.totalFeedbackCount = totalFeedbackCount;
    }

    public Long getIncrementFeedbackCount() {
        return incrementFeedbackCount;
    }

    public void setIncrementFeedbackCount(Long incrementFeedbackCount) {
        this.incrementFeedbackCount = incrementFeedbackCount;
    }

    public Long getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(Long agentIncome) {
        this.agentIncome = agentIncome;
    }

    public Long getNotUseCount() {
        return notUseCount;
    }

    public void setNotUseCount(Long notUseCount) {
        this.notUseCount = notUseCount;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}