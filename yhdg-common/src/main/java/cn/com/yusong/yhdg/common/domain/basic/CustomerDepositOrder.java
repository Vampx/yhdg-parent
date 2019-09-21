package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CustomerDepositOrder extends StringIdEntity {
    Integer partnerId;
    Integer money;
    Integer gift;
    Long customerId;
    String customerMobile;
    String customerFullname;
    Integer status;
    Date handleTime;
    String memo;
    Integer payType;
    Integer clientType;
    Date refundTime; /*退款时间*/
    Integer refundMoney; /*退款金额*/
    String refundPhoto; /*退款照片*/
    String refundOperator; /*退款人*/
    String refundReason; /*退款原因*/
    Date createTime;

    @Transient
    Long totalCount, totalMoney, totalMonthMoney;
    @Transient
    String fullname;
    String platformName;
    @Transient
    Integer realMoney; //实际到账金额
    Integer currentBalance;//骑手当前余额
    String partnerName;

    public enum Status {
        NOT(1, "未付款"),
        OK(2, "充值成功"),
        REFUND(3, "已退款");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (CustomerDepositOrder.Status e : CustomerDepositOrder.Status.values()) {
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

    public String getPayTypeName() {
        if (payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }

    public String getStatusName() {
        if (status != null) {
            return Status.getName(status);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
