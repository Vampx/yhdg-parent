package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备注册信息(设备号相关)
 */
@Getter
@Setter
public class Device extends IntIdEntity {

    public enum Type {
        IRON(1, "铁塔");

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Device.Type e : Device.Type.values()) {
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

    Integer type; //类型
    String deviceId;
    String version;
    Date heartTime;//心跳时间

    @Transient
    private Integer terminalDetailFlag;//是否升级版本

    public String getTypeName() {
        if (type != null) {
            return Type.getName(type);
        }
        return "";
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }

}
