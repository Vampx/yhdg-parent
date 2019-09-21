package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券赠送
 */
@Getter
@Setter
public class CustomerCouponTicketGift extends LongIdEntity  {

    Integer agentId;    /*运营商Id*/
    Integer type;       /*赠送类型*/
    Integer payCount;   /*购买天数*/
    Integer dayCount;   /*有效天数*/
    Integer wagesDay;   /*工作日*/
    Integer money;      /*金额*/
    Integer isActive;
    Integer category;

    @Transient
    String agentName;
    @Transient
    Integer newType;

    public enum Category {
        EXCHANGE(1,"换电"),
        RENT(2,"租电"),
        VEHICLE(3,"租车");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        public static List<Category> list = new ArrayList<Category>();

        static {
            for (Category e : Category.values()) {
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
        BUY_FOREGIFT(2, "支付押金"),
        BUY_RENT(3, "支付租金"),
        WAGES_GIVE(4,"工资日赠送");

        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        public static List<Type> vehicleTypeList = new ArrayList<Type>();

        static {
            for (Type e : Type.values()) {
                if(e.getValue() != WAGES_GIVE.getValue()){
                    vehicleTypeList.add(e);
                }
            }
        }

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

    public String getTypeName() {
        if(type != null) {
            return Type.getName(type);
        }
        return "";
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getWagesDay() {
        return wagesDay;
    }

    public void setWagesDay(Integer wagesDay) {
        this.wagesDay = wagesDay;
    }
}
