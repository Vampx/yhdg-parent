package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AgentForegiftWithdrawOrder extends StringIdEntity {

    public enum AccountType {
        WEIXIN_MP(1, "公众号"), ALIPAY(2, "支付宝"), BALANCE(3, "余额");

        private final int value;
        private final String name;

        AccountType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (AgentForegiftWithdrawOrder.AccountType e : AgentForegiftWithdrawOrder.AccountType.values()) {
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

    public enum Status {
        TO_AUDIT(1, "待审核"), AUDIT_OK(2, "转账中"), AUDIT_NO(3, "审核不通过"), WITHDRAW_OK(4, "提现成功"), WITHDRAW_NO(5, "提现失败");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (AgentForegiftWithdrawOrder.Status e : AgentForegiftWithdrawOrder.Status.values()) {
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


    Integer partnerId;
    Integer category;
    Integer agentId;
    String agentName;
    String agentCode;
    Integer accountType; /*1 公众号 2 支付宝*/
    String  accountName; /*姓名*/
    String weixinAccount; /*微信账户 一般是openid*/
    String alipayAccount; /*支付宝账户 支付宝账户*/
    Integer money;/*提现金额*/
    Integer realMoney;/*实提金额*/
    Integer serviceMoney; /*手续费*/
    Integer status; /*1待审核 2 审核成功 3 审核失败 4 提现成功 5 提现失败*/
    String statusMessage;/*提示消息*/
    Date auditTime;
    Date handleTime;/*处理时间*/
    Date cancelTime;
    String auditUser; /*审核人*/
    String auditMemo;/*审核人意见*/
    String operator;
    Date createTime;

    @Transient
    Integer fromStatus;
    String belongAgentName;
    String belongPartnerName;
    Integer balance;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getStatusName(){
        if (status != null) {
            return AgentForegiftWithdrawOrder.Status.getName(status);
        }
        return "";
    }

    public String getCategoryName() {
        if (category != null) {
            return ConstEnum.Category.getName(category);
        }
        return "";
    }

    public String getAccountTypeName(){
        if (accountType != null) {
            return AccountType.getName(accountType);
        }
        return "";
    }
}
