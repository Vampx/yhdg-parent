package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 租车VIP 套餐
 */
@Getter
@Setter
public class VehicleVipPrice  extends IntIdEntity {
	Integer agentId;/*运营商id*/
	Integer modelId;/*车型id*/
	Integer batteryType;/*电池类型*/
	String priceName;/*套餐名称*/
	Integer priceSettingId;/*租车套餐设置id*/
	Integer rentPriceId;/*租车套餐id*/
	Integer foregiftPrice;/*押金金额*/
	Integer vehicleForegiftPrice;/*车辆押金金额*/
	Integer batteryForegiftPrice;/*电池押金金额*/
	Integer rentPrice;/*租金金额*/
	Integer dayCount;/*天数*/
	Integer vehicleRentPrice;/*车辆租金金额*/
	Integer batteryRentPrice;/*电池租金金额*/
	Date beginTime;/*开始时间*/
	Date endTime;/*结束时间*/
	Integer shopCount;/*门店数量*/
	Integer agentCompanyCount;/*运营公司数量*/
	Integer customerCount;/*骑手数量*/
	Integer isActive;/*是否启用*/
	String memo;/*备注*/
	Date createTime;

	@Transient
	String categoryName;
	String vehicleName;
	String modelName;
	String agentName;
	Integer category;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}
}
