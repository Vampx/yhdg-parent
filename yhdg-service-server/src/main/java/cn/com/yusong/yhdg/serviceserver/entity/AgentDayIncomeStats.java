package cn.com.yusong.yhdg.serviceserver.entity;

public class AgentDayIncomeStats {

    Integer agentId; //
    Long income;
    Long exchangeIncome; //

    public void init() {
        income = 0L;
        exchangeIncome = 0L;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public Long getExchangeIncome() {
        return exchangeIncome;
    }

    public void setExchangeIncome(Long exchangeIncome) {
        this.exchangeIncome = exchangeIncome;
    }

}