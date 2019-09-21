package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class DeviceCommand extends LongIdEntity {

    public enum Type {
        REPORT_LOG(1, "上报日志"),
        ;


        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, Type> map = new HashMap<Integer, Type>();

        static {
            for (Type s : Type.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static String getName(int value) {
            Type status = map.get(value);
            if (status == null) {
                return null;
            }
            return status.name;
        }
    }


    public enum Status {
        NOT(1, "未下发"),
        DISPATCHED(2, "已下发")
        ;


        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, Status> map = new HashMap<Integer, Status>();

        static {
            for (Status s : Status.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static String getName(int value) {
            Status status = map.get(value);
            if (status == null) {
                return null;
            }
            return status.name;
        }
    }

    Integer deviceType;
    String deviceId; /*设备id*/
    Integer type;  /*1 上报日志*/
    Integer status;  /*1 未执行 2 已下发*/
    Date dispatchTime;  /*下发时间*/
    String logDate;
    Date createTime;

    public String getDeviceTypeName() {
        return DeviceUpgradePack.DeviceType.getName(deviceType);
    }

    public String getTypeName() {
        return Type.getName(type);
    }

    public String getStatusName() {
        return Status.getName(status);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
