package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户离线换电绑定电池表
 */
@Setter
@Getter
public class CustomerOfflineBattery extends IntIdEntity {

    public enum Status {
        NO(1, "未发送"), SUCCESS(2, "处理成功"), FAIL(3, "处理失败");

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

    Long customerId;
    String customerMobile;
    String customerFullname;
    String cabinetId;
    String cabinetCode;
    String boxNum;
    String batteryId;
    String batteryCode;
    Date exchangeTime;
    Integer status;
    Date handleTime;
    Date createTime;
}
