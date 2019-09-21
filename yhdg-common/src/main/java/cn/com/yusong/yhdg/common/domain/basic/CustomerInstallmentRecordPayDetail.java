package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户欠款记录付款明细
 */
@Setter
@Getter
public class CustomerInstallmentRecordPayDetail extends LongIdEntity {

    Long recordId;
    Integer num;//期数
    Integer partnerId;//平台id
    Long customerId;//客户id
    String fullname;//姓名
    String mobile;//手机号
    Integer agentId;//运营商id
    String agentName;//运营商名称
    String agentCode;
    Integer category; /*1 换电 2 租电*/
    Integer status;/*1 未付款 2 已付款*/
    String orderId;/*付款订单Id*/
    Date payTime;
    Integer payType;
    Integer totalMoney;//总金额
    Integer feeType;//手续费类型
    Integer feeMoney;//手续费
    Integer foregiftMoney;
    Integer packetMoney;
    Integer insuranceMoney;
    Integer money;/*本次支付金额*/
    Date expireTime;/*过期时间*/
    Date createTime;//创建时间

    @Transient
    String partnerName;
    String sourceId;

    public enum Status {
        WAIT_PAY(1, "未付款"),
        PAY_OK(2, "已付款"),
        REFUND(3, "已退款"),
        CANCEL(4, "已取消");

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

    public enum FeeType{
        RATE(1, "费率"),
        FIXED_HANDLING_FEE(2, "固定手续费"),
        NO_HANDLING_FEE(3, "无手续费");

        private final int value;
        private final String name;

        FeeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ExchangeInstallmentCount.FeeType e : ExchangeInstallmentCount.FeeType.values()) {
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

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getExpireTime() {
        return expireTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
