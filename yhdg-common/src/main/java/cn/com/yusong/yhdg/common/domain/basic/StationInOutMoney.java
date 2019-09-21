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
 * 站点出入流水帐
 */
@Setter
@Getter
public class StationInOutMoney extends LongIdEntity{

	public enum BizType {
		STATION_PAY_WITHDRAW_ORDER(1, "站点余额提现订单"),
		WITHDRAW_REFUND(2, "提现退款"),
		EXCHANGR_RATIO(3, "换电按次收入"),
		PACKET_PERIOD_RATIO(4, "包时段收入"),
		REFUND_PACKET_PERIOD_RATIO(5, "包时段退款"),
		IN_EXCHANGE_BALANCE_RECORD(6, "换电结算收入"),
		IN_RENT_BALANCE_RECORD(7, "租电结算收入"),
		IN_SHOP_CLEARING(8, "运营商结算收入"),
		;

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

	String stationId;
	Integer bizType;
	String bizId;
	Integer type;
	Integer money;
	Integer balance;
	String operator;
	Date createTime;

	@Transient
	String stationName;
	Integer agentId;
	String linkname;
	String tel;
	Integer platformRatio;
	Integer agentRatio;
	Integer provinceAgentRatio;
	Integer cityAgentRatio;
	Integer shopRatio;
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
			return StationInOutMoney.Type.getName(type);
		}
		return "";
	}
}
