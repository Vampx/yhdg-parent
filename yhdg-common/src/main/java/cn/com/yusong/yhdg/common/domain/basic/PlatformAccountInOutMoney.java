package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台账户流水
 */
public class PlatformAccountInOutMoney extends LongIdEntity {

	public enum BizType {
		IN_PACKET_PERIOD_RATIO(1, "骑手购买包时段套餐分成收入"),
		IN_WITHDRAW_SERVICE_MONEY(3, "提现服务费"),
		IN_WITHDRAW_REFUND(5, "提现退款"),
		IN_AGENT_PAY_MATERIAL(7,"运营商设备支付"),
		IN_EXCHANGR_RATIO(9, "按次分成收入"),
		IN_EXCHANGE_BALANCE_RECORD(11, "换电结算收入"),
		IN_RENT_BALANCE_RECORD(13, "租电结算收入"),

		OUT_WITHDRAW(2, "提现退款"),
		OUT_PACKET_PERIOD_RATIO(4, "包时段套餐退款"),
		;

		private final int value;
		private final String name;

		private BizType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();
		static {
			for (BizType e : BizType.values()) {
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

	public enum Type {
		IN(2, "收入"),
		OUT(1, "支出"),

		;

		private final int value;
		private final String name;

		private Type(int value, String name) {
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

	Integer platformAccountId;
	String bizId;//业务id
	Integer bizType;//业务类型
	Integer type;//类型 1 支出 2 收入
	Integer money;//金额
	Integer balance;//剩余金额
	String operator;//操作人
	Date createTime;

	@Transient
	String queryTime;

	public PlatformAccountInOutMoney() {
	}

	public PlatformAccountInOutMoney(Integer platformAccountId, String bizId, Integer bizType, Integer type, Integer money, Integer balance, String operator, Date createTime) {
		this.platformAccountId = platformAccountId;
		this.bizId = bizId;
		this.bizType = bizType;
		this.type = type;
		this.money = money;
		this.balance = balance;
		this.operator = operator;
		this.createTime = createTime;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPlatformAccountId() {
		return platformAccountId;
	}

	public void setPlatformAccountId(Integer platformAccountId) {
		this.platformAccountId = platformAccountId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBizTypeName() {
		if(bizType != null) {
			return BizType.getName(bizType);
		}
		return "";
	}
	public String getTypeName() {
		if(type != null) {
			return Type.getName(type);
		}
		return "";
	}
}
