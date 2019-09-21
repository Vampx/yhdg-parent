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

@Getter
@Setter
public class RentForegiftOrder extends StringIdEntity {
    Integer partnerId;
    Integer agentId;
    String agentCode;
    String agentName;
    String shopId;
    String shopName;
    String agentCompanyId;
    String agentCompanyName;
    Integer batteryType;
    String batteryId;
    Integer price;
    Integer money;                     //金额 按分计
    Integer consumeDepositBalance; //消费充值余额
    Integer consumeGiftBalance; //消费赠送余额
    Long customerId;                //客户id
    String customerMobile;
    String customerFullname;
    Integer status;
    Date handleTime;                //处理时间
    Date createTime;
    String memo;                    //备注
    Date payTime;                   //支付时间
    Integer payType;                //支付类型
    Long foregiftId;

    String ticketName ; /*优惠券名称*/
    Integer ticketMoney ; /*优惠券金额*/
    Long couponTicketId; /*优惠券id*/

    String deductionTicketName;/*电池抵扣券名称*/
    Integer deductionTicketMoney; /*电池抵扣券券金额*/
    Long deductionTicketId; /*电池抵扣券id*/

    Date applyRefundTime;           //申请退款时间
    Date refundTime;                //退款时间
    Integer refundMoney;            //退款金额
    String refundPhoto;             //退款照片
    String refundOperator;          //退款人
    Date completeInstallmentTime;
    Integer vehicleOrderFlag;  /*是否是通过租车流程产生的订单 1 是 0 否*/
    @Transient
    String statusName, batteryTypeName, payOrderId;
    @Transient
    Long refundRecordId;
    @Transient
    Integer sourceType, defaultQueryStatus, payOrderMoney, refundableMoney;

    public enum Status {
        WAIT_PAY(1, "付款失败"),
        PAY_OK(2, "支付成功"),
        APPLY_REFUND(3,"申请退款"),
        REFUND_SUCCESS(4,"退款成功");

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
    public Date getApplyRefundTime() {
        return applyRefundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }
}
