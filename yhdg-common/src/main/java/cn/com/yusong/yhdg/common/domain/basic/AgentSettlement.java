package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 运营商结算审核
 */
public class AgentSettlement extends IntIdEntity {

    public enum State {
        INIT(1, "待结算"),
        AUDITING(2, "待审核"),
        COMPLETE(3, "结算完成"),
        REFUSE(4, "已拒绝"),;

        private final int value;
        private final String name;

        State(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (State e : State.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public String getStateName() {
        if (state != null) {
            return State.getName(state);
        }
        return "";
    }

    Integer agentId; //
    String memo; //备注
    Integer state; //状态

    Integer money; //总收入
    Integer orderCount; //总订单数
    Integer exchangeMoney; //换电收入
    Integer packetPeriodMoney; //包月套餐收入
    Integer packetPeriodCount; //包月套餐购买量
    Integer provinceIncome; //省代收入
    Integer cityIncome; //市代收入

    String operator; //操作人
    String reason; //原因
    Date generateTime; //生成时间
    Date handleTime; //处理时间
    Date createTime; //创建时间

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(Integer exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }

    public Integer getPacketPeriodMoney() {
        return packetPeriodMoney;
    }

    public void setPacketPeriodMoney(Integer packetPeriodMoney) {
        this.packetPeriodMoney = packetPeriodMoney;
    }

    public Integer getPacketPeriodCount() {
        return packetPeriodCount;
    }

    public void setPacketPeriodCount(Integer packetPeriodCount) {
        this.packetPeriodCount = packetPeriodCount;
    }

    public Integer getProvinceIncome() {
        return provinceIncome;
    }

    public void setProvinceIncome(Integer provinceIncome) {
        this.provinceIncome = provinceIncome;
    }

    public Integer getCityIncome() {
        return cityIncome;
    }

    public void setCityIncome(Integer cityIncome) {
        this.cityIncome = cityIncome;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
