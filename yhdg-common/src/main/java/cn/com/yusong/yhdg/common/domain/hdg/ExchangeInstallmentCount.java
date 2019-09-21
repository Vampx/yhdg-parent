package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
/*换电分期期数总表*/
public class ExchangeInstallmentCount extends LongIdEntity {
    Long settingId;/*换电分期设置ID*/
    Integer count;/*分期数*/
    Integer feeType;/*1 费率  2 固定手续费 3 无手续费*/
    Integer feeMoney;/*手续费金额*/
    Integer feePercentage; /*手续费百分比*/

    public enum FeeType{
        RATE(1, "费率"),
        FIXED_HANDLING_FEE(2, "固定手续费"),
        NO_HANDLING_FEE(3, "无手续费");

        private final int value;
        private final String name;

        FeeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ExchangeInstallmentCount.FeeType e : ExchangeInstallmentCount.FeeType.values()) {
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

}
