package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
/*换电分期期数明细表*/
public class ExchangeInstallmentCountDetail extends LongIdEntity {

    Long countId;/*换电分期期数总表ID*/
    Integer num;/*分期数*/
    Integer feeType;/*1 费率  2 固定手续费 3 无手续费*/
    Integer feeMoney;/*手续费金额*/
    Integer feePercentage; /*手续费百分比*/
    Integer minForegiftPercentage;
    Integer minForegiftMoney;
    Integer minPacketPeriodPercentage;
    Integer minPacketPeriodMoney;
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

    public enum StagingTime{
        TWO_PHASE(2, "二期"),
        THREE_PHASE(3, "三期"),
        FOUR_PHASE(4, "四期"),
        FIVE_PHASE(5, "五期"),
        SIX_PHASE(6, "六期"),
        SEVEN_PHASE(7, "七期"),
        EIGHT_PHASE(8, "八期"),
        NINE_PHASE(9, "九期"),
        TEN_PHASE(10, "十期"),
        ELEVEN_PHASE(11, "十一期"),
        TWELVE_PHASE(12, "十二期");


        private final int value;
        private final String name;

        StagingTime(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ExchangeInstallmentCountDetail.StagingTime e : ExchangeInstallmentCountDetail.StagingTime.values()) {
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
