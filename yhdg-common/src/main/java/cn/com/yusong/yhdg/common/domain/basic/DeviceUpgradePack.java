package cn.com.yusong.yhdg.common.domain.basic;

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
public class DeviceUpgradePack extends IntIdEntity {
    public enum DeviceType {
        IRON_TOWER(1, "铁塔"),
        ;

        private final int value;
        private final String name;

        DeviceType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (DeviceType e : DeviceType.values()) {
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

    Integer deviceType;
    String upgradeName;
    String fileName;
    String filePath;
    String oldVersion;
    String newVersion;
    Long size;
    String md5Sum;
    String memo;
    Date updateTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public String getDeviceTypeName() {
        return DeviceType.getName(deviceType);
    }
}
