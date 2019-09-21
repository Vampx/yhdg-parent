package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志结算记录
 */
@Setter
@Getter
public class AgentDayBalanceRecord extends LongIdEntity {
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
        AGENT(1, "运营商"),
        ;

        private final int value;
        private final String name;

        private BizType(int value, String name) {
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

    String orderId; /* （bas_balance_transfer_order）转账订单id、不要加外键*/
    String balanceDate;/*结算日期*/
    Integer bizType;/*终端类型*/
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;//运营商编号
    Integer income; /*分成后的收入*/
    Integer money;/*总金额*/
    Integer packetMoney; /*包时段收入金额*/
    Integer exchangeMoney; /*按次收入金额*/
    Integer refundPacketMoney; /*包时段退款金额*/
    Integer refundExchangeMoney; /*按次退款金额*/
    String memo;/*备注*/
    Integer status;/*状态*/
    Date handleTime;/*操作时间*/
    Date confirmTime;/*提交时间*/
    String confirmUser;/*提交人*/
    Date createTime;/*创建时间*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getConfirmTime() {
        return confirmTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
