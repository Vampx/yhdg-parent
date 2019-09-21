package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障日志
 */
@Setter
@Getter
public class FaultLog extends LongIdEntity {

    Integer faultLevel;     /*故障级别*/
    String orderId;         /*订单id*/
    Integer provinceId;     /*省份id*/
    Integer cityId;         /*城市id*/
    Integer districtId;     /*区id*/
    Long dispatcherId;      //调度人员id
    Integer agentId;        /*运营商Id*/
    String agentName;       /*运营商名称*/
    String agentCode;      /*运营商编号*/
    String batteryId;     /*电池ID*/
    String groupId;
    String groupName;
    String cabinetId;       /*换电柜id*/
    String cabinetName;
    String cabinetAddress;
    String boxNum;
    Integer faultType;      /*故障类型*/
    String faultContent;    /*故障内容*/
    Integer status;
    Integer handleType; //处理类型
    String handleMemo;      /*处理信息*/
    Date handleTime;        /*处理时间*/
    String handlerName;     /*处理人*/
    Date createTime;
    String brand;                   //品牌

    @Transient
    String brandName;
    Integer batteryDetailFlag;
    List<Integer> batteryFaultList;

    public enum FaultLevel {
        GENERAL(1, "低"),
        MEDIUM(2, "中"),
        IMPORTANCE(3, "高")

        ;

        private final int value;
        private final String name;

        FaultLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (FaultLevel e : FaultLevel.values()) {
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

    public String getFaultLevelName() {
        if(faultLevel != null) {
            return FaultLevel.getName(faultLevel);
        }
        return "";
    }

    public enum Status {
        WAIT_PROCESS(1, "未处理"),
        PROCESSED(2, "已处理")
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

    public enum FaultType {
        CODE_1(1, "单体过压发生保护"),
        CODE_2(2, "单体欠压发生保护"),
        CODE_3(3, "整组过压发生保护"),
        CODE_4(4, "整组欠压发生保护"),
        CODE_5(5, "充电过温发生保护"),
        CODE_6(6, "充电低温发生保护"),
        CODE_7(7, "放电过温发生保护"),
        CODE_8(8, "放电低温发生保护"),
        CODE_9(9, "充电过流发生保护"),
        CODE_10(10, "放电过流发生保护"),
        CODE_11(11, "短路发生保护"),
        CODE_12(12, "前端检测IC错误"),
        CODE_13(13, "保护板充电MOS锁定"),
        CODE_14(14, "保护板放电MOS锁定"),
        CODE_15(15, "充电MOS异常"),
        CODE_16(16, "放电MOS异常"),

        CODE_17(17, "压差过大异常"),
        CODE_18(18, "骑手租赁电池超时"),
        CODE_19(19, "未绑定电池在外超时"),

        CODE_20(20, "电池单体电压小于最小电压断电"),

        CODE_21(21, "柜子离线"),
        CODE_22(22, "柜子温度过高"),
        CODE_23(23, "柜子温度过低"),
        CODE_24(24, "骑手未关门"),
        CODE_25(25, "柜子电池通讯异常"),
        CODE_26(26, "异常标注电池"),
        CODE_27(27, "骑手电池未取超时"),
        ;

        private final int value;
        private final String name;

        FaultType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (FaultType e : FaultType.values()) {
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

    public enum HandleType {
        SYSTEM(1, "系统处理"),
        MANUAL(2, "人工处理")
        ;

        private final int value;
        private final String name;

        HandleType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HandleType e : HandleType.values()) {
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

    public String getFaultTypeName() {
        if(faultType != null) {
            return FaultType.getName(faultType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }


}
