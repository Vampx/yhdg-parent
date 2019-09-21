package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算记录
 */
@Setter
@Getter
public class BalanceRecord extends LongIdEntity {
    public enum Status {
        WAIT_CONFIRM(1, "待确认"),
        CONFIRM_OK(2, "已确认"),
        CONFIRM_OK_BY_OFFLINE(3, "已确认(线下)"),;
        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BalanceRecord.Status e : BalanceRecord.Status.values()) {
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
        SHOP(2, "门店"),
        PARTNER(3, "平台"),
        AGENT_COMPANY(4,"运营公司"),
        ;

        private final int value;
        private final String name;

        BizType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BalanceRecord.BizType e : BalanceRecord.BizType.values()) {
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

    String balanceDate;/*结算日期*/
    Integer category;
    Integer bizType;/*终端类型*/
    Integer partnerId;
    String partnerName;
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;//运营商编号
    String shopId;/*门店id*/
    String shopName;/*门店名称*/
    String agentCompanyId;/*运营公司id*/
    String agentCompanyName;/*运营公司名称*/
    Integer money;/*总金额*/
    Integer packetPeriodMoney; /*包时段收入金额*/
    Integer exchangeMoney; /*按次收入金额*/
    Integer insuranceMoney; /*保险收入金额*/
    Integer provinceIncome;
    Integer cityIncome;
    Integer foregiftRemainMoney; /*押金剩余收入金额*/
    Integer refundInsuranceMoney;/*保险退款金额*/
    Integer refundPacketPeriodMoney;/*包时段退款金额*/
    Integer deductionTicketMoney;/*抵扣券支出*/

    Integer status;/*状态*/
    String orderId; /* （bas_balance_transfer_order）转账订单id、不要加外键*/
    String confirmOperator;/*确认人*/
    Date confirmTime;/*确认时间*/
    String imagePath;/*图片地址*/
    String memo;/*备注*/
    Date createTime;/*创建时间*/

    @Transient
    String suffix;
    Integer totalMoney;
    Integer serviceType;//1.收入 2.支出

    public void init() {
        money = 0;
        packetPeriodMoney = 0;
        exchangeMoney = 0;
        insuranceMoney = 0;
        provinceIncome = 0;
        cityIncome = 0;
        foregiftRemainMoney = 0;
        refundInsuranceMoney = 0;
        refundPacketPeriodMoney = 0;
        deductionTicketMoney = 0;

        status = Status.WAIT_CONFIRM.value;
        createTime = new Date();
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
