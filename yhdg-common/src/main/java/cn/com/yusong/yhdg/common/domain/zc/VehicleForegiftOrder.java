package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class VehicleForegiftOrder extends StringIdEntity {
    Integer partnerId ; /*商户id*/
    Integer agentId ; /*运营商Id*/
    String agentName ;
    String agentCode ; /*运营商编号*/
    String shopId ;
    String shopName ;
    Integer modelId ;/*车辆型号id*/
    String vehicleName ; /*车辆名称*/
    Integer batteryType ; /*电池类型*/
    Integer price ; /*押金金额 按分计*/
    Integer money ; /*支付金额 按分计*/
    Long customerId ; /*客户id*/
    String customerMobile ;/*客户手机*/
    String customerFullname ;/*客户名称*/
    Integer status  ; /*状态: 1 待支付 2 已支付 3 待退款 4 已退款*/
    Date handleTime ; /*处理时间*/
    String memo ; /*备注*/
    Integer payType  ; /*支付类型*/
    Date payTime ; /*支付时间*/
    Date refundTime ; /*退款时间*/
    Integer refundMoney; /*退款金额*/
    String refundOperator ; /*退款人*/
    Date createTime ; /*创建时间*/

    @Transient
    Integer refundableMoney;
    String modelName;
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

    public String getRefundDate(){
        if(refundTime != null){
            return DateFormatUtils.format(refundTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
    public String getPayDate(){
        if(payTime != null){
            return DateFormatUtils.format(payTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    public String getPayTypeName() {
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }
    public String getStatusName() {
        if(status != null) {
            return VehicleForegiftOrder.Status.getName(status);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }@JsonSerialize(using = DateTimeSerializer.class)
    public Date getRefundTime() {
        return refundTime;
    }@JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
