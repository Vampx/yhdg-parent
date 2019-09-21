package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户离线换电记录表
 */
@Setter
@Getter
public class CustomerOfflineExchangeRecord extends IntIdEntity {

    public enum Status {
        NO(1, "未发送"), SUCCESS(2, "发送成功"), FAIL(3, "发送失败");

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
    String putCabinetId;
    String putCabinetCode;
    String putBoxNum;
    String putBatteryId;
    String putBatteryCode;

    String takeCabinetId;
    String takeCabinetCode;
    String takeBoxNum;
    String takeBatteryId;
    String takeBatteryCode;

    Date exchangeTime;
    Integer status;
    Date handleTime;
    Date createTime;
}
