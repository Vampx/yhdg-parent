package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
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
public class Withdraw extends StringIdEntity {

    public enum Type {
        CUSTOMER(1, "客户"),
        AGENT(2, "运营商"),
        SHOP(3, "门店"),
        SYSTEM(4, "系统商户"),
        ESTATE(5, "物业"),
        AGENT_COMPANY(6,"运营公司"),;

        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Withdraw.Type e : Withdraw.Type.values()) {
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

    public enum AccountType {
        WEIXIN_MP(1, "公众号"), ALIPAY(2, "支付宝"), WEIXIN(3, "微信");

        private final int value;
        private final String name;

        AccountType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Withdraw.AccountType e : Withdraw.AccountType.values()) {
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
        TO_AUDIT(1, "待审核"), AUDIT_OK(2, "转账中"), AUDIT_NO(3, "审核不通过"), WITHDRAW_OK(4, "提现成功"), WITHDRAW_NO(5, "提现失败"), OFFLINE(6, "线下提现");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Withdraw.Status e : Withdraw.Status.values()) {
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

    Integer type; /*1 客户 2 运营商 3 门店*/
    Integer partnerId;
    Long customerId;
    String customerFullname; /*客户姓名*/
    String customerMobile; /*客户手机号*/
    Integer agentId;
    String agentName;
    String agentCode;
    String shopId;
    String shopName;
    Long estateId;
    String agentCompanyId;
    String agentCompanyName;
    String estateName;
    Integer platformAccountId;
    String platformAccountName;
    Integer accountType; /*1 公众号 2 支付宝 3 微信*/
    String  accountName; /*姓名*/
    String wxOpenId; /*微信openId*/
    String weixinAccount; /*公众号账户 一般是openid*/
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
    Date createTime;
    Integer belongAgentId;

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

    public String getTypeName() {
        if (type != null) {
            return Withdraw.Type.getName(type);
        }
        return "";
    }

    public String getOrganizeName() {
        if (type == Type.CUSTOMER.value) {
            return customerFullname;
        }else if (type == Type.AGENT.value) {
            return agentName;
        }else if (type == Type.SHOP.value) {
            return shopName;
        } else if (type == Type.AGENT_COMPANY.value) {
            return agentCompanyName;
        } else if (type == Type.SYSTEM.value) {
            return platformAccountName;
        } else if (type == Type.ESTATE.value) {
            return estateName;
        }
        return "";
    }

    public String getStatusName(){
        if (status != null) {
            return Withdraw.Status.getName(status);
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
