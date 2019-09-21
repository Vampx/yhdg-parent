package cn.com.yusong.yhdg.common.entity.report;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Created by chen on 2017/7/19.
 */
public class ChargerMonthStatsReport extends PageEntity{
    Integer agentId;
    String agentName;
    String statsMonth;
    String chargerId;
    String chargerName;
    Integer money;
    Integer degree;
    Integer power;
    Integer orderCount;
    Integer icCardMoney;//在线卡收入
    Integer offlineCardMoney;//离线卡收入
    Integer cancelCount;
    Integer unregisteredCardMoney;
    Integer agentIncome;/*累计运营商收入*/
    Integer estateIncome;/*物业今日收入*/
    Integer platformIncome;
    Integer coinMoney;
    Date updateTime;


    public Integer getCoinMoney() {
        return coinMoney;
    }

    public void setCoinMoney(Integer coinMoney) {
        this.coinMoney = coinMoney;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(Integer agentIncome) {
        this.agentIncome = agentIncome;
    }

    public Integer getEstateIncome() {
        return estateIncome;
    }

    public void setEstateIncome(Integer estateIncome) {
        this.estateIncome = estateIncome;
    }

    public Integer getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(Integer platformIncome) {
        this.platformIncome = platformIncome;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getUnregisteredCardMoney() {
        return unregisteredCardMoney;
    }

    public void setUnregisteredCardMoney(Integer unregisteredCardMoney) {
        this.unregisteredCardMoney = unregisteredCardMoney;
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

    public String getStatsMonth() {
        return statsMonth;
    }

    public void setStatsMonth(String statsMonth) {
        this.statsMonth = statsMonth;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
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
}
