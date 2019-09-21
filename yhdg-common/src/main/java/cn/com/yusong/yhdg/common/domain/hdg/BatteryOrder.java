package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeNotSecondSerializer;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 换电订单
 */
@Setter
@Getter
public class BatteryOrder extends StringIdEntity {
    public final static String BATTERY_ORDER_TABLE_NAME = "hdg_battery_order_";

    Integer partnerId;
    Integer agentId;
    Integer batteryType;
    Date openTime; /*开箱时间*/
    String batteryId;
    String takeCabinetId;/*取电柜子id*/
    String takeCabinetName; /*取电柜子名称*/
    String takeBoxNum; /*取电箱号*/
    Date takeTime; /*取电时间*/
    Integer initVolume; /*初始电量*/
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String putCabinetId; /*放电柜子id*/
    String putCabinetName; /*放电柜子名称*/
    String putGroupName;
    String putShopId; /*放入电池店铺id*/
    String putShopName; /*放入电池店铺名称*/
    String takeShopId; /*取出电池店铺id*/
    String takeShopName; /*取出电池店铺名称*/
    String putAgentCompanyId; /*放入电池运营公司id*/
    String putAgentCompanyName; /*放入电池运营公司名称*/
    String takeAgentCompanyId; /*取出电池运营公司id*/
    String takeAgentCompanyName; /*取出电池运营公司名称*/
    String putBoxNum; /*放电箱号*/
    Date putTime; /*放电时间*/
    Date payTime; /*付款时间*/
    Integer payType; /*支付类型*/
    Integer price;
    Integer money; /*金额*/
    Integer currentVolume; /*当前电量*/
    Integer currentDistance; /*骑行距离*/
    Integer refundMoney;
    Integer refundStatus;
    Integer refundTime;
    Long customerId;
    String customerMobile;
    String customerFullname;
    Integer orderStatus;
    String packetPeriodOrderId;
    String address;
    String refundReason; /*退款原因*/
    String ticketName; /*优惠券名称*/
    Integer ticketMoney; /*优惠券金额*/
    Long couponTicketId; /*优惠券id*/
    Date errorTime; /*换电出现错误的时间*/
    String errorMessage; /*换电出现错误的时消息*/
    Long payTimeoutFaultLogId; /*付款超时推送时间*/
    Long notTakeTimeoutFaultLogId; /*未取出超时推送时间*/
    Long takeTimeoutFaultLogId; /*取出超时推送时间*/
    Date lowVolumeVoiceTime;
    Date createTime;
    Date completeTime;
    Integer vehicleOrderFlag;  /*是否是通过租车流程产生的订单 1 是 0 否*/
    Integer initCapacity;/*取出电池容量*/
    Integer currentCapacity;/*当前电池容量*/

    @Transient
    transient Date takeBeginTime, takeEndTime, putBeginTime, putEndTime;
    String suffix;
    Double utilize;
    String cabinetCompanyId;
    String agentName;
    String batteryTypeName;
    Integer queryFlag;
    String batteryCode;
    String shellCode;
    Integer orderCount;

    public enum OrderStatus {
        INIT(1, "未取电池"),
        TAKE_OUT(2, "使用中"),
        CHARGING(3, "充电中"),
        IN_BOX_WAIT_PAY(4, "入柜未付款"),
        PAY(5, "已付款"),
        MANUAL_COMPLETE(6, "人工结束");

        private final int value;
        private final String name;

        OrderStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (OrderStatus e : OrderStatus.values()) {
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

    public String getOrderStatusName() {
        if (orderStatus != null) {
            return OrderStatus.getName(orderStatus);
        }
        return "";
    }

    public String getPayTypeName() {
        if (payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getOpenTime() {
        return openTime;
    }

    @JsonSerialize(using = DateTimeNotSecondSerializer.class)
    public Date getTakeTime() {
        return takeTime;
    }

    @JsonSerialize(using = DateTimeNotSecondSerializer.class)
    public Date getPutTime() {
        return putTime;
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
    public Date getCompleteTime() {
        return completeTime;
    }
}
