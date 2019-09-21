package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
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
public class VehiclePeriodOrder extends StringIdEntity {


    Integer partnerId ; /*商户id*/
    Integer agentId ;/*运营商id*/
    String agentName ;
    String agentCode ; /*运营商编号*/
    String shopId; /*门店id*/
    String shopName ; /*门店名称*/
    String agentCompanyId; /*运营公司id*/
    String agentCompanyName ; /*运营公司名称*/
    String cabinetId; /*柜子id*/
    Integer modelId ;/*车辆型号id*/
    String vehicleName ; /*车辆名称*/
    Integer batteryType; /*电池类型*/
    Integer price; /*租金金额 按分计*/
    Integer money; /*支付金额 按分计*/
    Integer dayCount ; /*订单天数*/
    Date beginTime ;/*开始时间*/
    Date endTime ;/*结束时间*/
    Date expireTime; /*过期时间*/
    Integer status ; /*1 未付款 2 未使用 3 已取消 4 已使用 5 已过期*/
    Long customerId ; /*电池被用户使用时候 有值*/
    String customerMobile ; /*电池被用户使用时候 有值*/
    String customerFullname ; /*电池被用户使用时候 有值*/
    Integer payType; /*支付类型*/
    Date payTime ;/*支付时间*/
    Date refundTime ; /*退款时间*/
    Integer refundMoney ; /*退款金额*/
    String refundOperator ; /*退款人*/
    Date createTime  ;/*创建时间*/

    @Transient
    Integer refundableMoney;
    String modelName ;/*车辆型号id*/
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

    public String getPayTypeName() {
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }
    public String getStatusName() {
        if(status != null) {
            return RentPeriodOrder.Status.getName(status);
        }
        return "";
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
    public String getBeginDate(){
        if(beginTime != null){
            return DateFormatUtils.format(beginTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
    public String getEndDate(){
        if(endTime != null){
            return DateFormatUtils.format(endTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
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
    public Date getRefundTime() {
        return refundTime;
    }
}
