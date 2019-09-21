package cn.com.yusong.yhdg.common.domain.zd;

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
 * 租电押金分期设置
 */
@Getter
@Setter
public class RentInstallmentSetting extends LongIdEntity {
	public enum Num{
		TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"),FIVE(5,"5"),SIX(6,"6"),SEVEN(7,"7"),EIGHT(8,"8"),NINE(9,"9"),TEN(10,"10");

		private final int value;
		private final String name;

		Num(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();

		static {
			for (Num e : Num.values()) {
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

	String mobile;
	String fullname;
	Integer agentId;
	String agentName;
	String agentCode;
	Date deadlineTime;
	Integer totalMoney;
	Integer batteryType;
	Long foregiftId;
	Integer foregiftMoney;
	Long packetId;
	Integer packetMoney;
	Long insuranceId;
	Integer insuranceMoney;
	Date createTime;

	@Transient
	String batteryTypeName;
	Integer installmentNum;//分期数
	Date finalInstallmentTime;//最后分期时间
	Integer paidInstallmentNum;//分期已付期数
	Integer paidInstallmentMoney;//分期已付金额
	Integer installmentRestMoney;//分期剩余金额

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getDeadlineTime() {
		return deadlineTime;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getFinalInstallmentTime() {
		return finalInstallmentTime;
	}

}
