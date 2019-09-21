package cn.com.yusong.yhdg.common.domain.hdg;

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
public class InsuranceOrder extends StringIdEntity {

    public enum Status {
        NOT_PAY (1, "未付款"),
        PAID(2,"已支付"),
        EXPIRED(3,"已过期"),
        APPLY_REFUND(4,"申请退款"),
        REFUND_SUCCESS(5,"退款成功");

        private final int value;
        private final String name;

        Status(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (InsuranceOrder.Status e : InsuranceOrder.Status.values()) {
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

    Integer partnerId; /*平台id*/
    Integer agentId;/*运营商id*/
    String cabinetId;
    String agentName;
    Integer batteryType;/*电池类型*/
    String agentCode;/*运营商编号*/
    Integer monthCount; /*订单月份*/
    Date beginTime;/*开始束时间*/
    Date endTime;/*结束时间*/
    Integer status;/*1 未付款 2 已支付 3 已过期*/
    Long customerId;
    String customerMobile;
    String customerFullname;
    Integer payType;
    Date payTime;/*支付时间*/
    Integer price;/*保费*/
    Integer paid;/*保额*/
    Integer money;/*实付金额*/
    Integer consumeDepositBalance; //消费充值余额
    Integer consumeGiftBalance; //消费赠送余额

    Date applyRefundTime;           //申请退款时间
    Date refundTime;                //退款时间
    Integer refundMoney;            //退款金额
    String refundPhoto;             //退款照片
    String refundOperator;          //退款人
    Date completeInstallmentTime;
    Date createTime;/*创建时间*/
    @Transient
    String batteryTypeName;
    String payOrderId;
    Integer payOrderMoney;
    Integer refundableMoney;
    Date handleTime;
    String memo;
    Long refundRecordId;
    Integer sourceType;
    String partnerName;
    Integer queryAntiStatus;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusName() {
        if (status != null) {
            return InsuranceOrder.Status.getName(status);
        }
        return "";
    }

    public String getPayTypeName() {
        if (payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }
}
