package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Vehicle extends IntIdEntity {

    public enum UpLineStatus {
        NOT_ONLINE(0, "未上线"),
        ONLINE(1, "已上线");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        UpLineStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (Vehicle.UpLineStatus e : Vehicle.UpLineStatus.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    public enum LockSwitch {
        NO(0, "不控制"),
        DISCHG_CLOSE(1, "强制锁定"),
        DISCHG_OPEN(2, "强制开锁"),
        ;

        private final int value;
        private final String name;

        LockSwitch(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LockSwitch e : LockSwitch.values()) {
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

    public enum Status {
        NOT_USE(1, "空闲"),
        IN_SHOP(2, "门店中"),
        IN_USE(3, "使用中");


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


    public enum TBIT_Parameter {
        SOFTVERSION("SOFTVERSION", "版本号", 0 ),
        GSM("GSM", "GSM信号", 0 ),
        GPS("GPS", "GPS信号", 0 ),
        VBAT("VBAT", "电池电压，充电接口电压，充电电流", 0 ),
        VIN("VIN", "外接电源输入电压", 0 ),
        LOGIN("LOGIN", "是否连接成功", 0 ),

        PSW("PSW", "防盗器密码", 1 ),
        DOMAIN("DOMAIN", "设置服务器地址和端口", 1 ),
        FREQ("FREQ", "GPS位置上报频度", 1 ),
        TRACE("TRACE", "开启、关闭追踪", 1 ),
        PULSE("PULSE", "心跳包频度", 1 ),
        ;


        private final String value;
        private final String name;
        private final int enableWrite;

        TBIT_Parameter(String value, String name, Integer enableWrite) {
            this.value = value;
            this.name = name;
            this.enableWrite = enableWrite;
        }

        private static Map<String, String> map = new HashMap<String, String>();

        static {
            for (TBIT_Parameter e : TBIT_Parameter.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        private static Map<String, Integer> map1 = new HashMap<String, Integer>();

        static {
            for (TBIT_Parameter e : TBIT_Parameter.values()) {
                map1.put(e.getValue(), e.getEnableWrite());
            }
        }

        public static String getName(String value) {
            return map.get(value);
        }

        public static Integer getWrite(String value) {
            return map1.get(value);
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Integer getEnableWrite() {
            return enableWrite;
        }
    }

    String vinNo; /*车架号*/
    Integer modelId;/*车辆型号id*/
    String vehicleName;/*车辆名称*/
    String version;
    Integer agentId;/*运营商id*/
    String agentName; /*运营商名称*/
    String agentCode; /*运营商编号*/
    String shopId; /*门店id*/
    String shopName;/*门店名称*/
    String batteryId;/*电池id*/
    Integer activeStatus;/*激活状态*/
    Integer status;/*使用状态*/
    Long customerId;/*客户id*/
    String customerMobile;/*客户手机*/
    String customerFullname;/*客户名称*/
    Integer lockSwitch;/*0 不控制 1 放电关 2 放电开*/
    Integer lockStatus;/*0 未锁 1 锁定*/
    Integer upLineStatus;/*上线状态 0 未上线 1  已上线*/
    Date upLineTime; /*上线时间*/
    Integer isOnline;/*是否在线*/
    Integer isActive;/*是否启用*/
    Date reportTime;/*上报时间*/
    String memo;/*备注*/
    Date createTime;/*创建时间*/

    @Transient
    Integer leisure, inUse, inShop, vehicleCommon, firstDataFlag;
    String modelName;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpLineTime() {
        return upLineTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getStatusName() {
        if (status != null) {
            return Status.getName(status);
        }
        return "";
    }

    public String getUpLineStatusName() {
        if (upLineStatus != null) {
            return Vehicle.UpLineStatus.getName(upLineStatus);
        }
        return "";

    }
}
