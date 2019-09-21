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
 * 电芯电池条码规则
 */
@Setter
@Getter
public class BatteryCellRegular extends LongIdEntity {
	Long cellFormatId;//电芯规格Id
	Long batteryFormatId;//电池规格Id
	Integer regularType;//规则类型 1.电芯条码  2.电池条码
	String regular;//规则
	String regularName;//规则名称
	Integer resetType;//清零方式
	Integer num;//当前值
	Date createTime;//创建时间
	Date updateTime;//更新时间

	public enum RegularType {
		CELL_FORMAT(1, "电芯条码规则"),
		BATTERY_FORMAT(2, "电池条码规则");

		private final int value;
		private final String name;

		RegularType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();
		static {
			for (BatteryCellRegular.RegularType e : BatteryCellRegular.RegularType.values()) {
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

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
