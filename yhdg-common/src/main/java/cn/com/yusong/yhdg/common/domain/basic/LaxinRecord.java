package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*拉新记录*/
@Setter
@Getter
public class LaxinRecord extends StringIdEntity {

    public enum Status {
        WAIT(1, "待付款"),
        TRANSFER(2, "转账中"),
        SUCCESS(3, "付款成功"),
        FAIL(4, "付款失败"),
        CANCEL(5, "已取消")
        ;


        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, Status> map = new HashMap<Integer, Status>();

        static {
            for (Status s : Status.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static String getName(int value) {
            Status status = map.get(value);
            if (status == null) {
                return null;
            }
            return status.name;
        }
    }

    Integer agentId; /*运营商id*/
    String agentName;
    String agentCode;
    Long laxinId;
    String laxinMobile; /*拉新手机号*/
    Integer laxinMoney; /*每次拉新获得金额*/
    Long targetCustomerId; /*目标用户id*/
    String targetMobile; /*目标手机号*/
    String targetFullname; /*目标姓名*/
    Integer payType; /*付款类型*/
    Integer status; /*付款状态*/
    Date payTime; /*付款时间*/
    Date transferTime; /*转账时间*/
    String orderId;
    Integer incomeType; /*佣金类型*/
    Integer foregiftMoney;
    Integer packetPeriodMoney;
    String mpOpenId;
    String accountName;
    String cancelCanuse;/*取消原因*/
    Date createTime;

    public String getPayTypeName() {
        if (payType == null) {
            return null;
        }
        return ConstEnum.PayType.getName(payType);
    }

    public String getStatusName() {
        if (status == null) {
            return null;
        }
        return Status.getName(status);
    }

    public String getIncomeTypeName() {
        if (incomeType == null) {
            return null;
        }
        return Laxin.IncomeType.getName(incomeType);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
