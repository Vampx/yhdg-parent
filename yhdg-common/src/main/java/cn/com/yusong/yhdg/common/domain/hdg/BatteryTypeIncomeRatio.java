package cn.com.yusong.yhdg.common.domain.hdg;

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
 * 电池类型收入分配
 */
@Setter
@Getter
public class BatteryTypeIncomeRatio extends LongIdEntity {

	Integer agentId;
	Integer batteryType;/*电池类型*/
	Integer rentPeriodType;/*租金周期  月*/
	Integer rentPeriodMoney; /*租金周期 每个周期收多少钱*/
	Date rentExpireTime;/*租金周期 过期时间*/
	Integer isReview; /*是否需要审核 0 免审 1 审核*/
	Date createTime;//创建时间

	@Transient
	String agentName, batteryTypeName;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getRentExpireTime() {
		return rentExpireTime;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
