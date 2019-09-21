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
public class BespeakOrder extends StringIdEntity {

    public enum Status {
        SUCCESS(1, "预约成功"),
        TAKE(2, "已取出"),
        OTHER_TAKE(3, "他箱取出"),
        CANCEL(4, "已取消"),
        EXPIRE(5, "已过期"),
        MANUAL_COMPLETE(6, "人工结束"),
        ;

        private final int value;
        private final String name;

        Status(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (BespeakOrder.Status e : BespeakOrder.Status.values()) {
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
    String agentName;
    String agentCode;/*运营商编号*/
    Long customerId;
    String customerMobile;
    String customerFullname;


    String bespeakCabinetId;
    String bespeakCabinetName;
    String bespeakBoxNum;
    String bespeakBatteryId;

    String takeCabinetId;
    String takeCabinetName;
    String takeBoxNum;
    String takeBatteryId;

    Integer status;

    Date takeTime;
    Date cancelTime;
    Date expireTime;
    Integer vehicleOrderFlag;  /*是否是通过租车流程产生的订单 1 是 0 否*/
    Date createTime;/*创建时间*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getStatusName() {
        if (status != null) {
            return BespeakOrder.Status.getName(status);
        }
        return "";
    }


}
