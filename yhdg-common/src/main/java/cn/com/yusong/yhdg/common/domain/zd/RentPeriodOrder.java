package cn.com.yusong.yhdg.common.domain.zd;

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

/**
 * 包月订单
 */
@Getter
@Setter
public class RentPeriodOrder extends StringIdEntity {
    Integer partnerId;
    Integer agentId;
    String agentName;
    String agentCode;
    Integer batteryType;
    String shopId;
    String shopName;
    String agentCompanyId;
    String agentCompanyName;
    Long activityId;
    Integer dayCount;
    Date beginTime;
    Date endTime;
    Date expireTime; /*订单过期的时间*/
    Integer status;             /*1 未付款 2 未使用 3 已取消 4 已使用 5 已过期*/
    Long customerId;           /*电池被用户使用时候 有值*/
    String customerMobile;     /*电池被用户使用时候 有值*/
    String customerFullname;    /*电池被用户使用时候 有值*/
    Integer payType;           /*支付类型*/
    Date payTime;
    Date refundTime;
    Integer refundMoney;
    Integer price;
    Integer money;
    Integer consumeDepositBalance; //消费充值余额
    Integer consumeGiftBalance; //消费赠送余额
    String ticketName ; /*优惠券名称*/
    Integer ticketMoney ; /*优惠券金额*/
    Long couponTicketId; /*优惠券id*/
    String operatorMemo;
    Date createTime;
    Date completeInstallmentTime;
    Integer vehicleOrderFlag;  /*是否是通过租车流程产生的订单 1 是 0 否*/
    @Transient
    String batteryTypeName, memo;
    Integer remainingDay;
    Integer queryFlag;
    String payOrderId;
    Integer payOrderMoney;
    Integer refundableMoney;
    Date handleTime;
    Long refundRecordId;
    Integer sourceType;
    String cabinetName;

    public enum Status {
        NOT_PAY(1,"未付款"),
        NOT_USE(2, "未使用"),
        USED(3, "已使用"),
        EXPIRED(4, "已过期"),
        APPLY_REFUND(5,"申请退款"),
        REFUND(6, "已退款")
        ;

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Status e : Status.values()) {
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

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }

    public String getPayTypeName() {
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
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
