package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CustomerMultiOrderDetail extends LongIdEntity {
    public enum SourceType {
        HDGFOREGIFT(1, "换电押金订单"), HDGPACKETPERIOD(2, "换电租金订单"), HDGINSURANCE(3, "换电保险订单"),
        ZDFOREGIFT(4, "租电押金订单"), ZDPACKETPERIOD(5, "租电租金订单"), ZDINSURANCE(6, "租电保险订单"),
        ZCGROUP(7, "租车组合订单"),
        ;

        private final int value;
        private final String name;

        private SourceType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (CustomerMultiOrderDetail.SourceType e : CustomerMultiOrderDetail.SourceType.values()) {
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
    private Long orderId;
    private Integer num;
    private Integer sourceType;
    private String sourceId;
    private Integer money;


    public String getSourceTypeName() {
        if (sourceType != null) {
            return CustomerMultiOrderDetail.SourceType.getName(sourceType);
        }
        return "";
    }
}