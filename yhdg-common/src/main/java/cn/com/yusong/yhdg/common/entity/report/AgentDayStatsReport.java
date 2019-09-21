package cn.com.yusong.yhdg.common.entity.report;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 运营商日统计
 */
public class AgentDayStatsReport extends PageEntity {
    Integer agentId;
    String agentName;
    String statsDate;
    Integer money;/*今天累计总收入*/
    Integer totalMoney;/*累计总收入*/
    Integer agentIncome;/*累计运营商收入*/
    Integer income;/*运营商今日收入*/
    Integer totalEstateMoney;/*累计物业总收入*/
    Integer estateIncome;/*物业今日收入*/
    Integer totalDegree;/*累计总电量*/
    Integer totalOrderCount;/*累计总充电人数*/
    Integer totalOverloadCount;/*累计过载订单数*/
    Integer degree;
    Integer power;
    Integer orderCount;
    Integer overloadCount;
    Integer duration;
    Integer degreeMoney;
    Date updateTime;
    Integer chargerCount;/*桩点数*/
    Integer offLineCount;/*离线桩点数*/
    Integer noneIncomeCount;/*无收入桩点数*/
    Integer platformIncome;
    Integer icCardMoney;//在线卡收入
    Integer offlineCardMoney;//离线卡收入
    Integer cancelCount;
    Integer unregisteredCardMoney;
    Integer coinMoney;

    public boolean isEmpty() {
        return money + agentIncome + degree + icCardMoney + offlineCardMoney + power + estateIncome + orderCount + overloadCount + duration + degreeMoney +coinMoney == 0;
    }

    public void init() {
        money = 0;
        agentIncome = 0;
        degree = 0;
        power = 0;
        estateIncome = 0;
        orderCount = 0;
        overloadCount = 0;
        duration = 0;
        icCardMoney = 0;
        offlineCardMoney = 0;
        degreeMoney = 0;
        coinMoney = 0;
        updateTime = new Date();
    }

    public Integer getUnregisteredCardMoney() {
        return unregisteredCardMoney;
    }

    public void setUnregisteredCardMoney(Integer unregisteredCardMoney) {
        this.unregisteredCardMoney = unregisteredCardMoney;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(Integer agentIncome) {
        this.agentIncome = agentIncome;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getTotalEstateMoney() {
        return totalEstateMoney;
    }

    public void setTotalEstateMoney(Integer totalEstateMoney) {
        this.totalEstateMoney = totalEstateMoney;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getEstateIncome() {
        return estateIncome;
    }

    public void setEstateIncome(Integer estateIncome) {
        this.estateIncome = estateIncome;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getOverloadCount() {
        return overloadCount;
    }

    public void setOverloadCount(Integer overloadCount) {
        this.overloadCount = overloadCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDegreeMoney() {
        return degreeMoney;
    }

    public void setDegreeMoney(Integer degreeMoney) {
        this.degreeMoney = degreeMoney;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTotalDegree() {
        return totalDegree;
    }

    public void setTotalDegree(Integer totalDegree) {
        this.totalDegree = totalDegree;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Integer getTotalOverloadCount() {
        return totalOverloadCount;
    }

    public void setTotalOverloadCount(Integer totalOverloadCount) {
        this.totalOverloadCount = totalOverloadCount;
    }

    public Integer getOffLineCount() {
        return offLineCount;
    }

    public void setOffLineCount(Integer offLineCount) {
        this.offLineCount = offLineCount;
    }

    public Integer getNoneIncomeCount() {
        return noneIncomeCount;
    }

    public void setNoneIncomeCount(Integer noneIncomeCount) {
        this.noneIncomeCount = noneIncomeCount;
    }

    public Integer getChargerCount() {
        return chargerCount;
    }

    public void setChargerCount(Integer chargerCount) {
        this.chargerCount = chargerCount;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(Integer platformIncome) {
        this.platformIncome = platformIncome;
    }

    public Integer getIcCardMoney() {
        return icCardMoney;
    }

    public void setIcCardMoney(Integer icCardMoney) {
        this.icCardMoney = icCardMoney;
    }

    public Integer getOfflineCardMoney() {
        return offlineCardMoney;
    }

    public void setOfflineCardMoney(Integer offlineCardMoney) {
        this.offlineCardMoney = offlineCardMoney;
    }

    public Integer getCoinMoney() {
        return coinMoney;
    }

    public void setCoinMoney(Integer coinMoney) {
        this.coinMoney = coinMoney;
    }
}
