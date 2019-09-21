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
 * 物业出入流水帐
 */
@Setter
@Getter
public class EstateInOutMoney extends LongIdEntity{

	public enum BizType {
		ESTATE_PAY_WITHDRAW_ORDER(1, "物业余额提现订单"),
		WITHDRAW_REFUND(2, "提现退款"),
		IN_ESTATE_DEGREE_PRICE(3, "物业电费收入");

		private final int value;
		private final String name;

		BizType(int value, String name) {
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
		PAY(1, "支出"),
		INCOME(2, "收入"),
		;
		private final int value;
		private final String name;

		Type(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();

		static {
			for (AgentInOutMoney.Type e : AgentInOutMoney.Type.values()) {
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


	Long estateId;
	Integer bizType;
	String bizId;
	Integer type;
	Integer money;
	Integer balance;
	String operator;
	Double price; /*电价*/
	Integer useVolume; /*用电量*/
	Date createTime;

	@Transient
	String estateName;
	String linkname;
	String tel;
	String queryTime;


	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public String getBizTypeName() {
		if(bizType != null) {
			return BizType.getName(bizType);
		}
		return "";
	}
	public String getTypeName() {
		if (type != null) {
			return Type.getName(type);
		}
		return "";
	}
}
