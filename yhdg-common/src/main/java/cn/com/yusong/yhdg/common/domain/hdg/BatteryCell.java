package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 电芯信息
 */
@Setter
@Getter
public class BatteryCell extends LongIdEntity {
	public enum Appearance {
		hard(3, "Y"),
		middle(2, "M"),
		soft(1, "R"),
		;

		private final int value;
		private final String name;

		Appearance(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();
		static {
			for (BatteryCell.Appearance e : BatteryCell.Appearance.values()) {
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

	String batteryId;//电池id
	String cellMfr;//电芯厂家
	String cellModel;//电芯型号
	String barcode;//条码编号
	Integer nominalCap;//组包容量 单位mAh
	Double acResistance;//交流内阻 单位mΩ
	Integer resilienceVol;//回弹电压 单位mV
	Integer staticVol;//静置电压 单位mV
	Integer circle;//循环次数
	Integer appearance;//外观指标1.R 2.M 3.Y
	String memo;//备注
	String operator;//操作人
	Date createTime;//创建时间

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
