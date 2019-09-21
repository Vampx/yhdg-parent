package cn.com.yusong.yhdg.common.domain.zc;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 租车套餐设置
 */
@Getter
@Setter
public class PriceSetting extends LongIdEntity {

	String settingName;/*套餐名称*/
	Integer agentId; /*运营商id*/
	String agentName; /*运营商名称*/
	String agentCode;/*运营商编号*/
	Integer modelId;/*车型id*/
	String vehicleName;/*车辆配置*/
	Integer category;/*1 换电 2 租电 3 不租电*/
	Integer batteryType;/*电池型号*/
	Integer batteryCount;/*电池数*/
	Integer minPrice;/*最底价格*/
	Integer maxPrice; /*最高价格*/
	Integer isActive;/*是否启用*/
	Date createTime;/*创建时间*/

	@Transient
	String modelName;/*车型名称*/
	String modelImagePath;/*外观图*/
	Integer vehicleCount;/*库存数*/

	public enum Category {
		EXCHANGE(1,"换电"),
		RENT(2,"租电"),
		NOT_RENT(3,"不租电");

		private final int value;
		private final String name;

		Category(int value, String name) {
			this.value = value;
			this.name = name;
		}

		private static Map<Integer, String> map = new HashMap<Integer, String>();
		public static List<Category> list = new ArrayList<Category>();

		static {
			for (Category e : Category.values()) {
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

	public String getCategoryName() {
		if(category != null) {
			return Category.getName(category);
		}
		return "";
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

}
