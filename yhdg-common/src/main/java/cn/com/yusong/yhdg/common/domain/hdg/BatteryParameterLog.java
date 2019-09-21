package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 电池参数修改日志
 */

@Setter
@Getter
public class BatteryParameterLog extends IntIdEntity {

    public enum Status {
        NO_REPORT(1, "未上报"),
        REPORT(2, "已上报"),
        CANCEL(3, "已作废"),
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

    String batteryId;
    String paramId;
    String paramName;
    String oldValue;
    String newValue;
    Integer status;
    Date reportTime;
    String operator;
    Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

}
