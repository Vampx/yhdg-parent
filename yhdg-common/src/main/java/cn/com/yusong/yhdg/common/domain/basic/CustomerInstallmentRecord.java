package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户分期记录
 */
@Setter
@Getter
public class CustomerInstallmentRecord extends LongIdEntity {

    Integer partnerId;//平台id
    Long customerId;//客户id
    String fullname;//姓名
    String mobile;//手机号
    Integer agentId;//运营商id
    String agentName;//运营商名称
    String agentCode;
    Integer category; /*1 换电 2 租电*/
    Integer status;/*1 未支付 2 分期中 3 已完成*/
    Integer totalMoney;//总金额
    Integer paidMoney;//已支付金额
    Long exchangeSettingId;//换电分期设置id
    Long rentSettingId;//租电分期设置id
    Date createTime;//创建时间
    Integer feeMoney;//手续费
    Integer foregiftMoney;
    Integer packetMoney;
    @Transient
    String partnerName;
    String batteryTypeName;
    Integer insuranceMoney;


    public enum Status {
        WAIT_PAY(1, "未支付"),
        PAY_ING(2, "分期中"),
        PAY_OK(3, "已完成"),
        REFUND_SUCCESS(4, "已退款");

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

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
