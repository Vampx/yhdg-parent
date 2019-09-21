package cn.com.yusong.yhdg.frontserver.constant;

import java.util.HashMap;
import java.util.Map;

public class ConstEnum {

       public enum VideoConvertStatus {
        WAIT(1, "等待优化"),
        RUNNING(2, "优化中"),
        SUCCESS(3, "优化完成"),
        FAIL(4, "优化失败");

        private final int value;
        private final String name;

        VideoConvertStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }
        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (VideoConvertStatus s : VideoConvertStatus.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static  String getName(int value) {
            return map.get(value);
        }
    }

}
