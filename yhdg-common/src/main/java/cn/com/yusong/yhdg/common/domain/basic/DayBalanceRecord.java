package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 日结算记录
 */
@Getter
@Setter
public class DayBalanceRecord extends LongIdEntity {
    public enum Status {
        WAIT_CONFIRM(1,"待确认"),
        CONFIRM_OK_BY_WEIXINMP(2,"已确认(公众号)"),
        CONFIRM_OK_BY_OFFLINE(3,"已确认(线下)"),
        SUCCESS(4, "处理成功"),
        FAILURE(5, "处理失败");

        private final int value;
        private final String name;

        Status(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (DayBalanceRecord.Status e : DayBalanceRecord.Status.values()) {
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

    public enum BizType {
        CABINET(1,"换电柜"),
        ;

        private final int value;
        private final String name;

        BizType(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (DayBalanceRecord.BizType e : DayBalanceRecord.BizType.values()) {
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

    public enum BalanceDate {
        T_1(0, "T+1"),
        WEEK_1(71, "周一"),
        WEEK_2(71, "周二"),
        WEEK_3(71, "周三"),
        WEEK_4(71, "周四"),
        WEEK_5(71, "周五"),
        WEEK_6(71, "周六"),
        WEEK_7(71, "周日"),
        DAY_1(1, "1号"),
        DAY_5(5, "5号"),
        DAY_10(10, "10号"),
        DAY_15(15, "15号"),
        DAY_20(20, "20号"),
        DAY_25(25, "25号"),
        ;

        private final int value;
        private final String name;

        BalanceDate(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (DayBalanceRecord.BizType e : DayBalanceRecord.BizType.values()) {
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

        public Date getDate(int value) {
            Calendar calendar = DateUtils.truncate(new GregorianCalendar(), Calendar.DAY_OF_MONTH);
            Date date = null;

            if(value == DayBalanceRecord.BalanceDate.T_1.getValue()) {
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_1.getValue()) {

                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_2.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_3.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_4.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_5.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_6.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.WEEK_7.getValue()) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.DAY_1.getValue()) {
                while (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.DAY_5.getValue()) {
                while (calendar.get(Calendar.DAY_OF_MONTH) != 5) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.DAY_15.getValue()) {
                while (calendar.get(Calendar.DAY_OF_MONTH) != 15) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.DAY_20.getValue()) {
                while (calendar.get(Calendar.DAY_OF_MONTH) != 20) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else if(value == DayBalanceRecord.BalanceDate.DAY_25.getValue()) {
                while (calendar.get(Calendar.DAY_OF_MONTH) != 25) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                date = calendar.getTime();

            } else {
                throw new IllegalArgumentException(String.format("value = %d", value));
            }

            return DateUtils.addSeconds(DateUtils.addDays(date, 1), -1);
        }
    }

    String orderId;
    String balanceDate; //结算日期
    Integer bizType;
    Integer agentId;
    String agentName;
    String agentCode;
    Integer income;
    Integer money;
    Integer packetMoney;
    Integer exchangeMoney;
    Integer refundPacketMoney;
    Integer refundExchangeMoney;
    String memo;
    Integer status;
    Date handleTime;
    Date confirmTime;
    String confirmUser;
    Date createTime;



    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
