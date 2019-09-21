package cn.com.yusong.yhdg.common.domain.basic;

import java.util.HashMap;
import java.util.Map;

public class SyncCursor {
    public enum Type {
        BATTERY_ORDER(1, "换电订单"),
        BATTERY_ORDER_FOR_PUT(2, "换电订单结束"),
        FAULT_LOG(3, "故障"),
        PACKET_PERIOD_ORDER(4, "包月订单");

        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
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

    String id;
    Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
