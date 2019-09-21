package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台出入流水帐
 */
public class PlatformInOutMoney extends LongIdEntity {

    public enum BizType {
        PACKET_PERIOD_RATIO(1, "骑手购买包时段套餐分成收入"),
        ;

        private final int value;
        private final String name;

        private BizType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (BizType e : BizType.values()) {
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

    public enum Type {
        PAY(1, "支出"),
        INCOME(2, "收入"),
        REFUND(3, "退款"),
        ;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
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

    Integer platformId;
    Integer money; //发生金额
    Integer balance; //剩余余额
    Integer bizType;//业务类型
    Integer type;//
    String sourceId;
    Date createTime;

    String queryTime;

    public PlatformInOutMoney() {
    }

    public PlatformInOutMoney(Integer platformId, Integer money, Integer balance, Integer bizType, Integer type, String sourceId, Date createTime) {
        this.platformId = platformId;
        this.money = money;
        this.balance = balance;
        this.bizType = bizType;
        this.type = type;
        this.sourceId = sourceId;
        this.createTime = createTime;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBizTypeName() {
        if(bizType != null) {
            return BizType.getName(bizType);
        }
        return "";
    }
    public String getTypeName() {
        if(type != null) {
            return Type.getName(type);
        }
        return "";
    }
}
