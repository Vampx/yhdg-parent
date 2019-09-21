package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 换电柜格口
 */
@Setter
@Getter
public class CabinetBox extends PageEntity {

    public final static int TYPE_NOT_SUPPORT_CHARGE = 0;

    public enum BoxState {
        CODE_1(1, "门状态"),
        CODE_2(2, "烟雾传感器"),
        CODE_3(3, "插座电源"),
        CODE_4(4, "格口风扇"),
        CODE_5(5, "格口通讯"),
        CODE_6(6, "充电控制"),
        CODE_7(7, "NFC识别状态"),
        ;


        private final int value;
        private final String name;

        BoxState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BoxState e : BoxState.values()) {
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

    String cabinetId; //主柜id
    String boxNum; //换电柜格子编号
    Integer agentId;
    Integer type; //格子类型
    Integer subtype;
    Integer isActive;
    Integer isOnline;
    Integer boxStatus;
    String batteryId;
    Date bespeakTime;
    Integer isOpen; //门是否打开  1 打开 0 关闭
    Long smokeFaultLogId;
    Date openTime;
    Date lockTime;
    Integer openType; //打开类型
    Long openerId; //打开人
    Integer power;
    Integer boxVersion;
    Integer boxTemp;
    Integer fanSpeed;
    Integer chargeFullVolume;
    Integer electricity;
    String forbiddenCause;
    String operator;/*禁用人*/
    Date operatorTime;/*禁用时间*/

    Integer boxState;

    @Transient
    int viewFlag; //查看标记

    @Transient
    String batteryTypeName; //电池类型名称
    String updateBoxNum; //修改箱体
    Integer volume;
    Long customerId;
    Integer fullVolume;
    Integer chargeStatus;
    Integer batteryStatus;
    Integer batteryFullVolume, upLineStatus;
    String shellCode, imei;//外壳编号
    Integer batteryIsNormal;//电池是否正常
    Integer faultFlag;//是否存在告警
    @Transient
    Battery battery;
    CabinetCharger cabinetCharger;
    @Transient
    Cabinet cabinet;
    public enum OpenType {
        BATTERY_ORDER_OPEN_EMPTY_BOX(1, "换电开空箱"),
        BATTERY_ORDER_OPEN_FULL_BOX(2, "换电开新电"),
        KEEP_ORDER_OPEN_EMPTY_BOX(3, "维护开空箱"),
        KEEP_ORDER_OPEN_FULL_BOX(4, "维护开满箱"),
        BACK_ORDER_OPEN_EMPTY_BOX(5, "退租开空箱");

        private final int value;
        private final String name;

        OpenType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (OpenType e : OpenType.values()) {
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

    public enum BoxStatus {
        EMPTY(1, "空箱"),
        FULL(2, "满箱"),
        BESPEAK(3, "预约"),
        EMPTY_LOCK(4, "空箱锁定"), /*只有开箱成功后 才会变成锁定状态*/
        FULL_LOCK(5, "满箱锁定"), /*只有开箱成功后 才会变成锁定状态*/
        BACK_LOCK(6, "退租锁定"),
        CUSTOMER_USE(7, "客户使用"),
        ;

        private final int value;
        private final String name;

        BoxStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BoxStatus e : BoxStatus.values()) {
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

    public String getBoxStatusName() {
        if (boxStatus != null) {
            return BoxStatus.getName(boxStatus);
        }
        return "";
    }

    public String getChargeStatusName() {
        if (chargeStatus != null) {
            return Battery.ChargeStatus.getName(chargeStatus);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBespeakTime() {
        return bespeakTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getOpenTime() {
        return openTime;
    }

    public String getBoxStateName() {
        String name = "";
        if (null != boxState) {
            BoxState[] names = BoxState.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(boxState)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                if ('1' == str[i]) {
                    if(i==0){
                        name += names[i].name +"关";
                    } else if (i == 1) {
                        name += names[i].name + "报警";
                    } else if (i == 2) {
                        name += names[i].name + "有电";
                    } else if (i == 3) {
                        name += names[i].name + "开启";
                    } else if (i == 4) {
                        name += names[i].name + "异常";
                    } else if (i == 5) {
                        name += names[i].name + "充电控制开启";
                    } else if (i == 6) {
                        name += names[i].name + "检测到NFC";
                    }
                }else{
                    if(i==0) {
                        name += names[i].name + "开";
                    } else if (i == 1) {
                        name += names[i].name + "正常";
                    } else if (i == 2) {
                        name += names[i].name + "没电";
                    } else if (i == 3) {
                        name += names[i].name + "关闭";
                    } else if (i == 4) {
                        name += names[i].name + "正常";
                    } else if (i == 5) {
                        name += names[i].name + "充电控制关闭";
                    } else if (i == 6) {
                        name += names[i].name + "未检测到NFC";
                    }
                }
            }
        }
        return name;
    }

    @Transient
    String background;
    public String getBackground() {
        int status = getStatus();
        switch (status){
            case 1: return "#48bcff";
            case 2: return "#36c640";
            case 3: return "#ff4242";
            case 4: return "#f0f0f0";
            case 5: return "#ffb239";
            case 6: return "#999999";
            case 8: return "#2f4f4f";
            default: return "#c6364529";
        }
    }

    @Transient
    String statusName;
    public String getStatusName() {
        int status = getStatus();
        switch (status){
            case 1: return "空闲中";
            case 2:
            case 3: return null;
            case 4: return "未上线";
            case 5: return "锁定中";
            case 6: return "禁用";
            case 8: return  "异常标识电池";
            default: return "异常";
        }
    }

    public int getStatus() {
        final int EMPTY_BOX = 1, FULL_POWER = 2, IN_ELECTRICIZE = 3, UP_LINE_STATUS=4, LOCK = 5, PROHIBIT = 6, EXCEPTION = 7, ABNORMAL = 8;//1 空闲 2 可换 3 充电中 4 未上线 5 锁定 6 禁用 7 异常 8异常标识电池
        int status = EXCEPTION; //7 异常
        if (isActive == ConstEnum.Flag.FALSE.getValue()) {
            status = PROHIBIT;// 6 禁用
        } else if (boxStatus == CabinetBox.BoxStatus.BESPEAK.getValue()
                || boxStatus == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
                || boxStatus == CabinetBox.BoxStatus.FULL_LOCK.getValue()
                || boxStatus == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
            status = LOCK; // 5 锁定
        } else if (battery != null) {
            if (boxStatus == CabinetBox.BoxStatus.CUSTOMER_USE.getValue() &&
                    (battery.status == Battery.Status.IN_BOX_NOT_PAY.getValue()
                            || battery.status == Battery.Status.IN_BOX_CUSTOMER_USE.getValue())) {
                status = LOCK; // 5 锁定
            } else if(battery.upLineStatus == 0){
                status = UP_LINE_STATUS; // 4 未上线
            } else if ((battery.status == Battery.Status.IN_BOX.getValue() || battery.status == Battery.Status.NOT_USE.getValue())
                    && battery.volume < chargeFullVolume) {
                status = IN_ELECTRICIZE; // 3 充电中
            } else if ((battery.status == Battery.Status.IN_BOX.getValue() || battery.status == Battery.Status.NOT_USE.getValue())
                    && battery.volume >= chargeFullVolume) {
                status = FULL_POWER; //2 满电(可换)
            }
            if (battery.isNormal == ConstEnum.Flag.FALSE.getValue()) {
                status = ABNORMAL;//8 异常标识电池
            }
        } else if (boxStatus == BoxStatus.EMPTY.value) {
            status = EMPTY_BOX; //1 空箱  空闲
        }
        return status;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getLockTime() {
        return lockTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getOperatorTime() {
        return operatorTime;
    }

}
