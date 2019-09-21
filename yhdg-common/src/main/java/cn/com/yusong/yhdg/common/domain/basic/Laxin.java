package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*拉新*/
@Setter
@Getter
public class Laxin extends LongIdEntity {

    public enum IncomeType {
        TIMES(1, "按次"),
        MONTH(2, "按月")
        ;


        private final int value;
        private final String name;

        IncomeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, IncomeType> map = new HashMap<Integer, IncomeType>();

        static {
            for (IncomeType s : IncomeType.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static String getName(int value) {
            IncomeType type = map.get(value);
            if (type == null) {
                return null;
            }
            return type.name;
        }
    }

    Integer partnerId;
    Integer agentId; /*运营商id*/
    String agentName;
    String agentCode;
    String mobile; /*手机号*/
    String password; /*密码*/
    Integer laxinMoney; /*每次拉新获得金额*/
    Integer ticketMoney; /*客户购买租金优惠金额*/
    Integer ticketDayCount; /*优惠券过期天数*/
    Integer packetPeriodMoney; /*购买租金获得金额*/
    Integer packetPeriodMonth; /*购买租金获得金额过期月份*/
    Integer isActive;
    Integer incomeType;
    String memo;
    Integer intervalDay;
    Date createTime; /*创建时间*/

    @Autowired
    String partnerName;

    public String getIncomeTypeName() {
        return IncomeType.getName(incomeType);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
